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

@WebServlet("/excluir-lista")
public class ExcluirListaServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

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

            Connection conexao =
                    Conexao.conectar();

            // =================================================
            // VERIFICAR DONO
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
            // APAGAR JOGOS DA LISTA
            // =================================================

            String apagarJogos =
                    "DELETE FROM lista_jogo "
                    + "WHERE id_lista = ?";

            PreparedStatement stmtJogos =
                    conexao.prepareStatement(
                            apagarJogos
                    );

            stmtJogos.setInt(
                    1,
                    idLista
            );

            stmtJogos.executeUpdate();

            stmtJogos.close();

            // =================================================
            // APAGAR LISTA
            // =================================================

            String apagarLista =
                    "DELETE FROM lista "
                    + "WHERE id = ? "
                    + "AND id_usuario = ?";

            PreparedStatement stmtLista =
                    conexao.prepareStatement(
                            apagarLista
                    );

            stmtLista.setInt(
                    1,
                    idLista
            );

            stmtLista.setInt(
                    2,
                    idUsuario
            );

            stmtLista.executeUpdate();

            stmtLista.close();
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