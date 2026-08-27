package controller;

import dao.SeguidorDAO;
import model.Usuario;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/seguir")
public class SeguirServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sessao =
                request.getSession(false);

        // Verificar login
        if (sessao == null ||
                sessao.getAttribute("usuario") == null) {

            response.sendRedirect("login.html");
            return;
        }

        // Usuário que está logado
        Usuario usuarioLogado =
                (Usuario) sessao.getAttribute("usuario");

        // ID da pessoa que será seguida
        String idTexto =
                request.getParameter("id");

        if (idTexto == null ||
                idTexto.trim().isEmpty()) {

            response.sendRedirect("index.html");
            return;
        }

        try {

            int idSeguido =
                    Integer.parseInt(idTexto);

            // Não pode seguir a si mesmo
            if (usuarioLogado.getId() == idSeguido) {

                response.sendRedirect(
                        "perfil-usuario?id="
                        + idSeguido
                );

                return;
            }

            SeguidorDAO dao =
                    new SeguidorDAO();

            // Verifica se já segue
            boolean jaSegue =
                    dao.seguindo(
                            usuarioLogado.getId(),
                            idSeguido
                    );

            if (!jaSegue) {

                dao.seguir(
                        usuarioLogado.getId(),
                        idSeguido
                );

            } else {

                // Se já segue, deixa de seguir
                dao.deixarDeSeguir(
                        usuarioLogado.getId(),
                        idSeguido
                );
            }

            // Voltar para o perfil
            response.sendRedirect(
                    "perfil-usuario?id="
                    + idSeguido
            );

        } catch (NumberFormatException e) {

            response.sendRedirect("index.html");

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect("index.html");
        }
    }
}