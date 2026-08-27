package controller;

import dao.BibliotecaDAO;
import model.Usuario;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/adicionar-biblioteca")
public class AdicionarBibliotecaServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sessao =
                request.getSession(false);

        // Verificar se está logado
        if (sessao == null ||
                sessao.getAttribute("usuario") == null) {

            response.sendRedirect("login.html");
            return;
        }

        String idTexto =
                request.getParameter("id");

        if (idTexto == null ||
                idTexto.trim().isEmpty()) {

            response.sendRedirect("index.html");
            return;
        }

        try {

            int idJogo =
                    Integer.parseInt(idTexto);

            Usuario usuario =
                    (Usuario) sessao.getAttribute("usuario");

            int idUsuario =
                    usuario.getId();

            BibliotecaDAO dao =
                    new BibliotecaDAO();

            // Verificar se já está na biblioteca
            boolean possui =
                    dao.possuiJogo(
                            idUsuario,
                            idJogo
                    );

            if (!possui) {

                dao.adicionar(
                        idUsuario,
                        idJogo,
                        "quero jogar"
                );

            }

            // Voltar para a página inicial
            response.sendRedirect("index.html");

        } catch (NumberFormatException e) {

            response.sendRedirect("index.html");

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect("index.html");
        }
    }
}