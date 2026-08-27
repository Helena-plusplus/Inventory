package controller;

import dao.SeguidorDAO;
import dao.UsuarioDAO;
import model.Usuario;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/perfil-usuario")
public class PerfilUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(
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

        String idTexto =
                request.getParameter("id");

        if (idTexto == null ||
                idTexto.isEmpty()) {

            response.sendRedirect("index.html");
            return;
        }

        try {

            int id =
                    Integer.parseInt(idTexto);

            UsuarioDAO usuarioDAO =
                    new UsuarioDAO();

            Usuario perfil =
                    usuarioDAO.buscarPorId(id);

            if (perfil == null) {

                response.sendRedirect(
                        "buscar-usuarios.html"
                );

                return;
            }

            Usuario usuarioLogado =
                    (Usuario) sessao.getAttribute(
                            "usuario"
                    );

            SeguidorDAO seguidorDAO =
                    new SeguidorDAO();

            boolean seguindo =
                    seguidorDAO.seguindo(
                            usuarioLogado.getId(),
                            perfil.getId()
                    );

            int seguidores =
                    seguidorDAO.contarSeguidores(
                            perfil.getId()
                    );

            response.setContentType(
                    "text/html;charset=UTF-8"
            );

            StringBuilder html =
                    new StringBuilder();

            html.append("<!DOCTYPE html>");
            html.append("<html lang='pt-BR'>");

            html.append("<head>");
            html.append("<meta charset='UTF-8'>");
            html.append("<meta name='viewport' ");
            html.append("content='width=device-width, initial-scale=1.0'>");
            html.append("<title>Perfil - GameBoxd</title>");
            html.append("<link rel='stylesheet' href='style.css'>");
            html.append("</head>");

            html.append("<body>");

            // HEADER
            html.append("<header>");

            html.append("<h1>GameBoxd</h1>");

            html.append("<nav>");

            html.append(
                    "<a href='index.html'>Início</a>"
            );

            html.append(
                    "<a href='buscar-usuarios.html'>"
                    + "Buscar usuários"
                    + "</a>"
            );

            html.append(
                    "<a href='perfil'>Meu Perfil</a>"
            );

            html.append(
                    "<a href='logout'>Sair</a>"
            );

            html.append("</nav>");

            html.append("</header>");

            // PERFIL
            html.append("<main>");

            html.append(
                    "<div class='perfil-usuario'>"
            );

            // FOTO
            if (perfil.getFoto() != null &&
                    !perfil.getFoto().isEmpty()) {

                html.append(
                        "<img class='foto-perfil' "
                        + "src='imagens/"
                        + perfil.getFoto()
                        + "' "
                        + "alt='Foto de perfil'>"
                );

            } else {

                html.append(
                        "<div class='foto-perfil "
                        + "sem-foto'>"
                        + "?"
                        + "</div>"
                );
            }

            // USERNAME
            html.append("<h2>");
            html.append("@");
            html.append(perfil.getUsername());
            html.append("</h2>");

            // NOME
            html.append("<p>");
            html.append(perfil.getNome());
            html.append("</p>");

            // BIO
            if (perfil.getBio() != null &&
                    !perfil.getBio().isEmpty()) {

                html.append("<p>");
                html.append(perfil.getBio());
                html.append("</p>");
            }

            // SEGUIDORES
            html.append("<p>");
            html.append("Seguidores: ");
            html.append(seguidores);
            html.append("</p>");

            // BOTÃO
            if (usuarioLogado.getId() != perfil.getId()) {

                html.append(
                        "<a class='botao-seguir' "
                        + "href='seguir?id="
                        + perfil.getId()
                        + "'>"
                );

                if (seguindo) {
                    html.append("Deixar de seguir");
                } else {
                    html.append("Seguir");
                }

                html.append("</a>");
            }

            html.append("</div>");

            html.append("</main>");

            html.append("</body>");
            html.append("</html>");

            response.getWriter().println(
                    html.toString()
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect(
                    "buscar-usuarios.html"
            );
        }
    }
}