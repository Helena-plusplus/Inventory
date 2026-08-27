package controller;

import dao.UsuarioDAO;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Usuario;

@WebServlet("/buscar-usuario")
public class BuscarUsuarioServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Verifica se está logado
        HttpSession sessao =
                request.getSession(false);

        if (sessao == null ||
                sessao.getAttribute("usuario") == null) {

            response.sendRedirect("login.html");
            return;
        }

        String username =
                request.getParameter("nome");

        response.setContentType(
                "text/html;charset=UTF-8"
        );

        if (username == null ||
                username.trim().isEmpty()) {

            response.getWriter().println(
                    "<h2>Digite um nome de usuário.</h2>"
                    + "<a href='buscar-usuarios.html'>"
                    + "Voltar"
                    + "</a>"
            );

            return;
        }

        // Remove @ caso o usuário digite @helena
        username = username.trim();

        if (username.startsWith("@")) {
            username = username.substring(1);
        }

        UsuarioDAO dao =
                new UsuarioDAO();

        ArrayList<Usuario> usuarios =
                dao.buscarPorUsernameParcial(username);

        StringBuilder html =
                new StringBuilder();

        html.append("<!DOCTYPE html>");
        html.append("<html lang='pt-BR'>");

        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<meta name='viewport' ");
        html.append("content='width=device-width, initial-scale=1.0'>");
        html.append("<title>Buscar pessoas - GameBoxd</title>");
        html.append("<link rel='stylesheet' href='style.css'>");
        html.append("</head>");

        html.append("<body>");

        // =========================
        // HEADER
        // =========================

        html.append("<header>");

        html.append("<h1>GameBoxd</h1>");

        html.append("<nav>");

        html.append("<a href='index.html'>");
        html.append("Início");
        html.append("</a>");

        html.append("<a href='buscar-usuarios.html'>");
        html.append("Buscar pessoas");
        html.append("</a>");

        html.append("<a href='perfil'>");
        html.append("Meu Perfil");
        html.append("</a>");

        html.append("<a href='logout'>");
        html.append("Sair");
        html.append("</a>");

        html.append("</nav>");

        html.append("</header>");

        // =========================
        // CONTEUDO
        // =========================

        html.append("<main>");

        html.append("<section>");

        html.append("<h2>");
        html.append("Resultados para @");
        html.append(username);
        html.append("</h2>");

        if (usuarios.isEmpty()) {

            html.append(
                    "<p>Nenhum usuário encontrado.</p>"
            );

        } else {

            html.append(
                    "<div class='resultados-usuarios'>"
            );

            for (Usuario usuario : usuarios) {

                html.append(
                        "<article class='card-usuario'>"
                );

                // FOTO
                if (usuario.getFoto() != null
                        && !usuario.getFoto().isEmpty()) {

                    html.append(
                            "<img src='imagens/"
                            + usuario.getFoto()
                            + "' "
                            + "class='foto-usuario'"
                            + ">"
                    );

                } else {

                    html.append(
                            "<div class='foto-usuario "
                            + "sem-foto-usuario'>"
                            + "?"
                            + "</div>"
                    );
                }

                // USERNAME
                html.append("<h3>");
                html.append("@");
                html.append(usuario.getUsername());
                html.append("</h3>");

                // NOME
                html.append("<p>");
                html.append(usuario.getNome());
                html.append("</p>");

                // BOTAO
                html.append(
                        "<a href='perfil-usuario?id="
                        + usuario.getId()
                        + "'>"
                        + "Ver perfil"
                        + "</a>"
                );

                html.append("</article>");
            }

            html.append("</div>");
        }

        html.append("</section>");

        html.append("</main>");

        html.append("</body>");
        html.append("</html>");

        response.getWriter().println(
                html.toString()
        );
    }
}