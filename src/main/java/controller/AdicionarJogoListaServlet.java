package controller;

import dao.Conexao;
import model.Usuario;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/adicionar-jogo-lista")
public class AdicionarJogoListaServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession sessao =
                request.getSession(false);

        if (sessao == null ||
                sessao.getAttribute("usuario") == null) {

            response.sendRedirect("login.html");
            return;
        }

        try {

            Usuario usuario =
                    (Usuario) sessao.getAttribute(
                            "usuario"
                    );

            int idUsuario =
                    usuario.getId();

            int idLista =
                    Integer.parseInt(
                            request.getParameter(
                                    "idLista"
                            )
                    );

            int idJogo =
                    Integer.parseInt(
                            request.getParameter(
                                    "idJogo"
                            )
                    );

            Connection conexao =
                    Conexao.conectar();

            // =================================================
            // VERIFICAR DONO DA LISTA
            // =================================================

            String verificar =
                    "SELECT id "
                    + "FROM lista "
                    + "WHERE id = ? "
                    + "AND id_usuario = ?";

            PreparedStatement stmtVerificar =
                    conexao.prepareStatement(
                            verificar
                    );

            stmtVerificar.setInt(
                    1,
                    idLista
            );

            stmtVerificar.setInt(
                    2,
                    idUsuario
            );

            ResultSet rs =
                    stmtVerificar.executeQuery();

            if (!rs.next()) {

                rs.close();
                stmtVerificar.close();
                conexao.close();

                response.sendRedirect(
                        "listas"
                );

                return;
            }

            rs.close();
            stmtVerificar.close();

            // =================================================
            // ADICIONAR
            // =================================================

            String inserir =
                    "INSERT OR IGNORE INTO lista_jogo "
                    + "(id_lista, id_jogo) "
                    + "VALUES (?, ?)";

            PreparedStatement stmt =
                    conexao.prepareStatement(
                            inserir
                    );

            stmt.setInt(
                    1,
                    idLista
            );

            stmt.setInt(
                    2,
                    idJogo
            );

            stmt.executeUpdate();

            stmt.close();
            conexao.close();

            response.sendRedirect(
                    "listas"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect(
                    "listas"
            );
        }
    }
}