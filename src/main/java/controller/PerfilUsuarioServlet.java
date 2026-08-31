package controller;

import dao.Conexao;
import dao.UsuarioDAO;
import model.Usuario;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
                idTexto.trim().isEmpty()) {

            response.sendRedirect(
                    "buscar-usuarios.html"
            );

            return;
        }

        int idPerfil;

        try {

            idPerfil =
                    Integer.parseInt(idTexto);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    "buscar-usuarios.html"
            );

            return;
        }

        Usuario usuarioLogado =
                (Usuario) sessao.getAttribute(
                        "usuario"
                );

        UsuarioDAO usuarioDAO =
                new UsuarioDAO();

        Usuario perfil =
                usuarioDAO.buscarPorId(
                        idPerfil
                );

        if (perfil == null) {

            response.sendRedirect(
                    "buscar-usuarios.html"
            );

            return;
        }

        int idLogado =
                usuarioLogado.getId();

        boolean mesmoUsuario =
                idLogado == idPerfil;

        boolean seguindo =
                usuarioDAO.seguindo(
                        idLogado,
                        idPerfil
                );

        int seguidores =
                usuarioDAO.contarSeguidores(
                        idPerfil
                );

        int seguindoQuantidade =
                usuarioDAO.contarSeguindo(
                        idPerfil
                );

        response.setContentType(
                "text/html;charset=UTF-8"
        );

        StringBuilder html =
                new StringBuilder();

        html.append("<!DOCTYPE html>");
        html.append("<html lang='pt-BR'>");

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
                "<title>"
                + escapar(perfil.getNome())
                + " - Inventory</title>"
        );

        html.append(
                "<link rel='stylesheet' "
                + "href='style.css'>"
        );

        // =====================================================
        // CSS
        // =====================================================

        html.append("<style>");

        html.append(
                "body{"
                + "margin:0;"
                + "background:"
                + "radial-gradient("
                + "circle at top,#35135a,"
                + "#160b22 40%,#0b0710 80%);"
                + "min-height:100vh;"
                + "color:white;"
                + "}"
        );

        html.append(
                ".usuario-container{"
                + "max-width:1000px;"
                + "margin:45px auto;"
                + "padding:20px;"
                + "}"
        );

        html.append(
                ".usuario-box{"
                + "background:"
                + "linear-gradient("
                + "145deg,#21142c,#140b1b"
                + ");"
                + "border:1px solid #47225f;"
                + "border-radius:24px;"
                + "padding:40px;"
                + "box-shadow:"
                + "0 25px 60px rgba(0,0,0,.4);"
                + "}"
        );

        html.append(
                ".usuario-topo{"
                + "text-align:center;"
                + "border-bottom:1px solid #382043;"
                + "padding-bottom:30px;"
                + "}"
        );

        html.append(
                ".foto-usuario{"
                + "width:160px;"
                + "height:160px;"
                + "border-radius:50%;"
                + "object-fit:cover;"
                + "border:5px solid #7c3aed;"
                + "box-shadow:"
                + "0 0 30px rgba(124,58,237,.35);"
                + "}"
        );

        html.append(
                ".sem-foto{"
                + "width:160px;"
                + "height:160px;"
                + "margin:auto;"
                + "border-radius:50%;"
                + "background:#251532;"
                + "border:5px solid #7c3aed;"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:center;"
                + "color:#999;"
                + "}"
        );

        html.append(
                ".nome-usuario{"
                + "font-size:32px;"
                + "margin:18px 0 4px;"
                + "}"
        );

        html.append(
                ".username-usuario{"
                + "color:#a855f7;"
                + "font-size:16px;"
                + "}"
        );

        html.append(
                ".bio-usuario{"
                + "max-width:650px;"
                + "margin:15px auto;"
                + "color:#c5bdca;"
                + "line-height:1.6;"
                + "}"
        );

        // =====================================================
        // ESTATÍSTICAS
        // =====================================================

        html.append(
                ".estatisticas{"
                + "display:flex;"
                + "justify-content:center;"
                + "gap:15px;"
                + "margin-top:25px;"
                + "flex-wrap:wrap;"
                + "}"
        );

        html.append(
                ".estatistica{"
                + "background:#160d1e;"
                + "border:1px solid #392347;"
                + "border-radius:12px;"
                + "padding:14px 25px;"
                + "min-width:100px;"
                + "}"
        );

        html.append(
                ".estatistica strong{"
                + "display:block;"
                + "color:#c084fc;"
                + "font-size:22px;"
                + "}"
        );

        html.append(
                ".estatistica span{"
                + "color:#999;"
                + "font-size:12px;"
                + "}"
        );

        // =====================================================
        // SEGUIR
        // =====================================================

        html.append(
                ".botao-seguir{"
                + "display:inline-block;"
                + "margin-top:22px;"
                + "padding:13px 35px;"
                + "border-radius:10px;"
                + "border:none;"
                + "background:"
                + "linear-gradient("
                + "135deg,#7c3aed,#a855f7"
                + ");"
                + "color:white;"
                + "font-weight:bold;"
                + "cursor:pointer;"
                + "font-size:15px;"
                + "}"
        );

        html.append(
                ".botao-seguindo{"
                + "background:#261832;"
                + "border:1px solid #8b5cf6;"
                + "}"
        );

        // =====================================================
        // INFORMAÇÕES
        // =====================================================

        html.append(
                ".informacoes{"
                + "display:grid;"
                + "grid-template-columns:"
                + "repeat(2,1fr);"
                + "gap:15px;"
                + "margin-top:30px;"
                + "}"
        );

        html.append(
                ".info-card{"
                + "background:#160d1e;"
                + "border:1px solid #352044;"
                + "border-radius:12px;"
                + "padding:17px;"
                + "}"
        );

        html.append(
                ".info-card strong{"
                + "display:block;"
                + "color:#a855f7;"
                + "font-size:12px;"
                + "margin-bottom:6px;"
                + "}"
        );

        html.append(
                ".info-card span{"
                + "color:#ddd;"
                + "}"
        );

        html.append(
                "@media(max-width:600px){"
                + ".usuario-container{"
                + "padding:10px;"
                + "}"
                + ".usuario-box{"
                + "padding:25px 18px;"
                + "}"
                + ".informacoes{"
                + "grid-template-columns:1fr;"
                + "}"
                + ".nome-usuario{"
                + "font-size:27px;"
                + "}"
                + "}"
        );

        html.append("</style>");

        html.append("</head>");

        html.append("<body>");

        // =====================================================
        // HEADER
        // =====================================================

        html.append("<header>");

        html.append(
                "<h1>Inventory</h1>"
        );

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

        // =====================================================
        // PERFIL
        // =====================================================

        html.append(
                "<main class='usuario-container'>"
        );

        html.append(
                "<div class='usuario-box'>"
        );

        html.append(
                "<section class='usuario-topo'>"
        );

        // =====================================================
        // FOTO
        // =====================================================

        String foto =
                perfil.getFoto();

        if (foto != null &&
                !foto.trim().isEmpty()) {

            String caminhoFoto =
                    foto.trim();

            if (!caminhoFoto.startsWith(
                    "http://")
                    &&
                    !caminhoFoto.startsWith(
                    "https://")) {

                while (
                        caminhoFoto.startsWith("/")
                ) {

                    caminhoFoto =
                            caminhoFoto.substring(1);
                }

                caminhoFoto =
                        request.getContextPath()
                        + "/foto-perfil?arquivo="
                        + URLEncoder.encode(
                                caminhoFoto,
                                "UTF-8"
                        );
            }

            html.append(
                    "<img "
                    + "class='foto-usuario' "
                    + "src='"
                    + escapar(caminhoFoto)
                    + "' "
                    + "alt='Foto de "
                    + escapar(
                            perfil.getNome()
                      )
                    + "'>"
            );

        } else {

            html.append(
                    "<div class='sem-foto'>"
                    + "Sem foto"
                    + "</div>"
            );
        }

        // =====================================================
        // NOME
        // =====================================================

        html.append(
                "<h2 class='nome-usuario'>"
                + escapar(perfil.getNome())
                + "</h2>"
        );

        html.append(
                "<div class='username-usuario'>"
                + "@"
                + escapar(perfil.getUsername())
                + "</div>"
        );

        // =====================================================
        // BIO
        // =====================================================

        if (perfil.getBio() != null &&
                !perfil.getBio().trim().isEmpty()) {

            html.append(
                    "<div class='bio-usuario'>"
                    + escapar(perfil.getBio())
                    + "</div>"
            );
        }

        // =====================================================
        // ESTATÍSTICAS
        // =====================================================

        html.append(
                "<div class='estatisticas'>"
        );

        html.append(
                "<div class='estatistica'>"
                + "<strong>"
                + seguidores
                + "</strong>"
                + "<span>Seguidores</span>"
                + "</div>"
        );

        html.append(
                "<div class='estatistica'>"
                + "<strong>"
                + seguindoQuantidade
                + "</strong>"
                + "<span>Seguindo</span>"
                + "</div>"
        );

        html.append(
                "</div>"
        );

        // =====================================================
        // BOTÃO
        // =====================================================

        if (!mesmoUsuario) {

            html.append(
                    "<form "
                    + "method='POST' "
                    + "action='seguir'>"
            );

            html.append(
                    "<input "
                    + "type='hidden' "
                    + "name='idUsuario' "
                    + "value='"
                    + idPerfil
                    + "'>"
            );

            if (seguindo) {

                html.append(
                        "<input "
                        + "type='hidden' "
                        + "name='acao' "
                        + "value='deixar'>"
                );

                html.append(
                        "<button "
                        + "class='botao-seguir "
                        + "botao-seguindo' "
                        + "type='submit'>"
                        + "Seguindo"
                        + "</button>"
                );

            } else {

                html.append(
                        "<input "
                        + "type='hidden' "
                        + "name='acao' "
                        + "value='seguir'>"
                );

                html.append(
                        "<button "
                        + "class='botao-seguir' "
                        + "type='submit'>"
                        + "Seguir"
                        + "</button>"
                );
            }

            html.append("</form>");
        }

        html.append(
                "</section>"
        );

        // =====================================================
        // INFORMAÇÕES
        // =====================================================

        html.append(
                "<section class='informacoes'>"
        );

        html.append(
                "<div class='info-card'>"
                + "<strong>País</strong>"
                + "<span>"
                + valor(
                        perfil.getPais(),
                        "Não informado"
                  )
                + "</span>"
                + "</div>"
        );

        html.append(
                "<div class='info-card'>"
                + "<strong>Plataforma favorita</strong>"
                + "<span>"
                + valor(
                        perfil.getPlataformaFavorita(),
                        "Não informado"
                  )
                + "</span>"
                + "</div>"
        );

        html.append(
                "</section>"
        );

        html.append(
                "</div>"
        );

        html.append(
                "</main>"
        );

        html.append("</body>");
        html.append("</html>");

        response.getWriter().println(
                html.toString()
        );
    }

    // =====================================================
    // VALOR
    // =====================================================

    private String valor(
            String texto,
            String padrao) {

        if (texto == null ||
                texto.trim().isEmpty()) {

            return padrao;
        }

        return escapar(texto);
    }

    // =====================================================
    // ESCAPAR
    // =====================================================

    private String escapar(
            String texto) {

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