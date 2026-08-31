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

@WebServlet("/favorito")
public class FavoritoServlet extends HttpServlet {

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
                    (Usuario) sessao.getAttribute("usuario");

            int idUsuario =
                    usuario.getId();

            int idJogo =
                    Integer.parseInt(
                            request.getParameter("idJogo")
                    );

            Connection conexao =
                    Conexao.conectar();

            // =================================================
            // VERIFICAR SE JÁ É FAVORITO
            // =================================================

            String verificar =
                    "SELECT id "
                    + "FROM favorito "
                    + "WHERE id_usuario = ? "
                    + "AND id_jogo = ?";

            PreparedStatement stmtVerificar =
                    conexao.prepareStatement(
                            verificar
                    );

            stmtVerificar.setInt(
                    1,
                    idUsuario
            );

            stmtVerificar.setInt(
                    2,
                    idJogo
            );

            ResultSet rs =
                    stmtVerificar.executeQuery();

            boolean jaFavorito =
                    rs.next();

            rs.close();
            stmtVerificar.close();

            // =================================================
            // SE JÁ É FAVORITO -> REMOVER
            // =================================================

            if (jaFavorito) {

                String remover =
                        "DELETE FROM favorito "
                        + "WHERE id_usuario = ? "
                        + "AND id_jogo = ?";

                PreparedStatement stmtRemover =
                        conexao.prepareStatement(
                                remover
                        );

                stmtRemover.setInt(
                        1,
                        idUsuario
                );

                stmtRemover.setInt(
                        2,
                        idJogo
                );

                stmtRemover.executeUpdate();

                stmtRemover.close();

            } else {

                // =============================================
                // CONTAR FAVORITOS
                // =============================================

                String contar =
                        "SELECT COUNT(*) "
                        + "FROM favorito "
                        + "WHERE id_usuario = ?";

                PreparedStatement stmtContar =
                        conexao.prepareStatement(
                                contar
                        );

                stmtContar.setInt(
                        1,
                        idUsuario
                );

                ResultSet rsContar =
                        stmtContar.executeQuery();

                int quantidade = 0;

                if (rsContar.next()) {

                    quantidade =
                            rsContar.getInt(1);
                }

                rsContar.close();
                stmtContar.close();

                // =============================================
                // LIMITE DE 5
                // =============================================

                if (quantidade >= 5) {

                    conexao.close();

                    response.sendRedirect(
                            "jogos?erro=favoritos"
                    );

                    return;
                }

                // =============================================
                // ADICIONAR
                // =============================================

                String adicionar =
                        "INSERT INTO favorito "
                        + "(id_usuario, id_jogo) "
                        + "VALUES (?, ?)";

                PreparedStatement stmtAdicionar =
                        conexao.prepareStatement(
                                adicionar
                        );

                stmtAdicionar.setInt(
                        1,
                        idUsuario
                );

                stmtAdicionar.setInt(
                        2,
                        idJogo
                );

                stmtAdicionar.executeUpdate();

                stmtAdicionar.close();
            }

            conexao.close();

            response.sendRedirect(
                    "jogos"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect(
                    "jogos"
            );
        }
    }
}