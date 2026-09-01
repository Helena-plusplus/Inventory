package controller;

import dao.Conexao;
import model.Usuario;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/criar-lista")
public class CriarListaServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // =====================================================
        // VERIFICAR LOGIN
        // =====================================================

        HttpSession sessao =
                request.getSession(false);

        if (sessao == null ||
                sessao.getAttribute("usuario") == null) {

            response.sendRedirect("login.html");
            return;
        }

        // =====================================================
        // USUARIO LOGADO
        // =====================================================

        Usuario usuario =
                (Usuario) sessao.getAttribute("usuario");

        int idUsuario =
                usuario.getId();

        // =====================================================
        // NOME DA LISTA
        // =====================================================

        String nome =
                request.getParameter("nome");

        if (nome == null) {

            response.sendRedirect(
                    "listas"
            );

            return;
        }

        nome =
                nome.trim();

        // =====================================================
        // VALIDAR NOME
        // =====================================================

        if (nome.isEmpty()) {

            response.sendRedirect(
                    "listas"
            );

            return;
        }

        // Limite de segurança
        if (nome.length() > 80) {

            nome =
                    nome.substring(
                            0,
                            80
                    );
        }

        Connection conexao = null;
        PreparedStatement stmt = null;

        try {

            // =================================================
            // CONECTAR AO SQLITE
            // =================================================

            conexao =
                    Conexao.conectar();

            if (conexao == null) {

                response.sendRedirect(
                        "listas"
                );

                return;
            }

            // =================================================
            // GARANTIR TABELA LISTA
            // =================================================

            String criarTabela =
                    "CREATE TABLE IF NOT EXISTS lista ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "id_usuario INTEGER NOT NULL,"
                    + "nome TEXT NOT NULL,"
                    + "data_criacao TEXT "
                    + "DEFAULT CURRENT_TIMESTAMP,"
                    + "FOREIGN KEY(id_usuario) "
                    + "REFERENCES usuario(id)"
                    + ")";

            PreparedStatement stmtTabela =
                    conexao.prepareStatement(
                            criarTabela
                    );

            stmtTabela.executeUpdate();

            stmtTabela.close();

            // =================================================
            // CRIAR LISTA
            // =================================================

            String sql =
                    "INSERT INTO lista "
                    + "(id_usuario, nome) "
                    + "VALUES (?, ?)";

            stmt =
                    conexao.prepareStatement(
                            sql
                    );

            stmt.setInt(
                    1,
                    idUsuario
            );

            stmt.setString(
                    2,
                    nome
            );

            int resultado =
                    stmt.executeUpdate();

            System.out.println(
                    "================================="
            );

            if (resultado > 0) {

                System.out.println(
                        "LISTA CRIADA COM SUCESSO!"
                );

                System.out.println(
                        "USUARIO: "
                        + idUsuario
                );

                System.out.println(
                        "NOME: "
                        + nome
                );

            } else {

                System.out.println(
                        "ERRO AO CRIAR LISTA!"
                );
            }

            System.out.println(
                    "================================="
            );

            // =================================================
            // FECHAR
            // =================================================

            stmt.close();
            stmt = null;

            conexao.close();
            conexao = null;

            // =================================================
            // VOLTAR PARA LISTAS
            // =================================================

            response.sendRedirect(
                    "listas"
            );

        } catch (Exception e) {

            System.out.println(
                    "================================="
            );

            System.out.println(
                    "ERRO AO CRIAR LISTA:"
            );

            e.printStackTrace();

            System.out.println(
                    "================================="
            );

            response.sendRedirect(
                    "listas"
            );

        } finally {

            try {

                if (stmt != null) {
                    stmt.close();
                }

            } catch (Exception e) {

                e.printStackTrace();
            }

            try {

                if (conexao != null) {
                    conexao.close();
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }
}