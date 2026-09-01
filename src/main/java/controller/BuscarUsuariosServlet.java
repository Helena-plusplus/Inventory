package controller;

import dao.UsuarioDAO;
import model.Usuario;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/buscar-usuarios")
public class BuscarUsuariosServlet extends HttpServlet {

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

        request.setCharacterEncoding("UTF-8");

        String busca =
                request.getParameter("busca");

        if (busca == null) {
            busca = "";
        }

        busca = busca.trim();

        ArrayList<Usuario> usuarios =
                new ArrayList<Usuario>();

        if (!busca.isEmpty()) {

            UsuarioDAO dao =
                    new UsuarioDAO();

            String termo =
                    busca;

            if (termo.startsWith("@")) {
                termo = termo.substring(1);
            }

            usuarios =
                    dao.buscarPorUsernameParcial(
                            termo
                    );

            if (usuarios.isEmpty()) {

                usuarios =
                        dao.buscarPorNome(
                                termo
                        );
            }
        }

        response.setContentType(
                "text/html;charset=UTF-8"
        );

        StringBuilder html =
                new StringBuilder();

        html.append(
                "<!DOCTYPE html>"
        );

        html.append(
                "<html lang='pt-BR'>"
        );

        html.append("<head>");

        html.append(
                "<meta charset='UTF-8'>"
        );

        html.append(
                "<meta name='viewport' "
                + "content='width=device-width,"
                + " initial-scale=1.0'>"
        );

        html.append(
                "<title>Buscar usuários - Inventory</title>"
        );

        html.append(
                "<link rel='stylesheet' "
                + "href='style.css'>"
        );

        html.append(
                "<style>"
                + "body{"
                + "background:"
                + "radial-gradient("
                + "circle at top,#35105f,"
                + "#12091b 55%,#09050d);"
                + "min-height:100vh;"
                + "}"

                + ".busca-usuarios{"
                + "max-width:900px;"
                + "margin:45px auto;"
                + "padding:20px;"
                + "}"

                + ".caixa-busca{"
                + "background:#181020;"
                + "border:1px solid #4b2370;"
                + "border-radius:20px;"
                + "padding:30px;"
                + "}"

                + ".form-busca{"
                + "display:flex;"
                + "gap:10px;"
                + "margin-top:20px;"
                + "}"

                + ".campo-busca{"
                + "flex:1;"
                + "padding:14px;"
                + "background:#100b15;"
                + "border:1px solid #47305a;"
                + "border-radius:9px;"
                + "color:white;"
                + "font-size:16px;"
                + "}"

                + ".botao-busca{"
                + "padding:14px 25px;"
                + "border:0;"
                + "border-radius:9px;"
                + "background:#7c3aed;"
                + "color:white;"
                + "font-weight:bold;"
                + "cursor:pointer;"
                + "}"

                + ".resultados{"
                + "display:grid;"
                + "grid-template-columns:"
                + "repeat(auto-fill,minmax(220px,1fr));"
                + "gap:15px;"
                + "margin-top:25px;"
                + "}"

                + ".usuario-card{"
                + "display:block;"
                + "background:#160d1e;"
                + "border:1px solid #352044;"
                + "border-radius:14px;"
                + "padding:18px;"
                + "color:white;"
                + "text-decoration:none;"
                + "transition:.25s;"
                + "}"

                + ".usuario-card:hover{"
                + "transform:translateY(-4px);"
                + "border-color:#8b5cf6;"
                + "}"

                + ".foto-usuario{"
                + "width:75px;"
                + "height:75px;"
                + "border-radius:50%;"
                + "object-fit:cover;"
                + "border:3px solid #7c3aed;"
                + "}"

                + ".sem-foto{"
                + "width:75px;"
                + "height:75px;"
                + "border-radius:50%;"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:center;"
                + "background:#281833;"
                + "color:#999;"
                + "}"

                + ".username{"
                + "color:#b98be8;"
                + "margin-top:5px;"
                + "}"

                + "</style>"
        );

        html.append("</head>");
        html.append("<body>");

        html.append("<header>");

        html.append("<h1>Inventory</h1>");

        html.append("<nav>");

        html.append(
                "<a href='index.html'>Início</a>"
        );

        html.append(
                "<a href='jogos'>Jogos</a>"
        );

        html.append(
                "<a href='biblioteca'>Biblioteca</a>"
        );

        html.append(
                "<a href='buscar-usuarios'>"
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

        html.append(
                "<main class='busca-usuarios'>"
        );

        html.append(
                "<div class='caixa-busca'>"
        );

        html.append(
                "<h2>Buscar usuários</h2>"
        );

        html.append(
                "<p>Pesquise pelo nome ou username.</p>"
        );

        html.append(
                "<form "
                + "class='form-busca' "
                + "method='GET' "
                + "action='buscar-usuarios'>"
        );

        html.append(
                "<input "
                + "class='campo-busca' "
                + "type='text' "
                + "name='busca' "
                + "value='"
                + escapar(busca)
                + "' "
                + "placeholder='Nome ou @username'>"
        );

        html.append(
                "<button "
                + "class='botao-busca' "
                + "type='submit'>"
                + "Buscar"
                + "</button>"
        );

        html.append("</form>");

        if (!busca.isEmpty()) {

            html.append(
                    "<div class='resultados'>"
            );

            for (Usuario usuario : usuarios) {

                html.append(
                        "<a "
                        + "class='usuario-card' "
                        + "href='perfil-usuario?id="
                        + usuario.getId()
                        + "'>"
                );

                String foto =
                        usuario.getFoto();

                if (foto != null &&
                        !foto.trim().isEmpty()) {

                    String caminho =
                            foto.trim();

                    if (!caminho.startsWith("http://")
                            &&
                            !caminho.startsWith("https://")) {

                        caminho =
                                request.getContextPath()
                                + "/foto-perfil?arquivo="
                                + java.net.URLEncoder.encode(
                                        caminho,
                                        "UTF-8"
                                );
                    }

                    html.append(
                            "<img "
                            + "class='foto-usuario' "
                            + "src='"
                            + escapar(caminho)
                            + "'>"
                    );

                } else {

                    html.append(
                            "<div class='sem-foto'>"
                            + "Sem foto"
                            + "</div>"
                    );
                }

                html.append(
                        "<h3>"
                        + escapar(
                                usuario.getNome()
                          )
                        + "</h3>"
                );

                html.append(
                        "<div class='username'>"
                        + "@"
                        + escapar(
                                usuario.getUsername()
                          )
                        + "</div>"
                );

                html.append(
                        "</a>"
                );
            }

            html.append("</div>");

            if (usuarios.isEmpty()) {

                html.append(
                        "<p style='margin-top:25px;color:#999;'>"
                        + "Nenhum usuário encontrado."
                        + "</p>"
                );
            }
        }

        html.append("</div>");
        html.append("</main>");

        html.append("</body>");
        html.append("</html>");

        response.getWriter().println(
                html.toString()
        );
    }

    private String escapar(String texto) {

        if (texto == null) {
            return "";
        }

        return texto
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}