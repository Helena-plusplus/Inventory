package controller;

import dao.UsuarioDAO;
import model.Usuario;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

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
        // PEGAR ID DO PERFIL
        // =====================================================

        String idTexto =
                request.getParameter("id");

        if (idTexto == null ||
                idTexto.trim().isEmpty()) {

            response.sendRedirect(
                    "buscar-usuarios"
            );

            return;
        }

        int idPerfil;

        try {

            idPerfil =
                    Integer.parseInt(idTexto);

        } catch (NumberFormatException e) {

            response.sendRedirect(
                    "buscar-usuarios"
            );

            return;
        }

        // =====================================================
        // USUÁRIOS
        // =====================================================

        Usuario usuarioLogado =
                (Usuario) sessao.getAttribute(
                        "usuario"
                );

        int idLogado =
                usuarioLogado.getId();

        UsuarioDAO dao =
                new UsuarioDAO();

        Usuario perfil =
                dao.buscarPorId(idPerfil);

        if (perfil == null) {

            response.sendRedirect(
                    "buscar-usuarios"
            );

            return;
        }

        // =====================================================
        // SEGUIMENTO
        // =====================================================

        boolean mesmoUsuario =
                idLogado == idPerfil;

        boolean seguindo =
                false;

        if (!mesmoUsuario) {

            seguindo =
                    dao.seguindo(
                            idLogado,
                            idPerfil
                    );
        }

        int quantidadeSeguidores =
                dao.contarSeguidores(
                        idPerfil
                );

        int quantidadeSeguindo =
                dao.contarSeguindo(
                        idPerfil
                );

        // =====================================================
        // LISTAS
        // =====================================================

        ArrayList<Usuario> seguidores =
                dao.listarSeguidores(
                        idPerfil
                );

        ArrayList<Usuario> seguindoLista =
                dao.listarSeguindo(
                        idPerfil
                );

        // =====================================================
        // RESPOSTA
        // =====================================================

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
                + "content='width=device-width, "
                + "initial-scale=1.0'>"
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
                + "circle at top,"
                + "#35105f,"
                + "#140b1d 45%,"
                + "#09060d"
                + ");"
                + "min-height:100vh;"
                + "color:white;"
                + "}"
        );

        html.append(
                ".perfil-usuario-container{"
                + "max-width:1000px;"
                + "margin:45px auto;"
                + "padding:20px;"
                + "}"
        );

        html.append(
                ".perfil-usuario-box{"
                + "background:"
                + "linear-gradient("
                + "145deg,#21142c,#140b1b"
                + ");"
                + "border:1px solid #4c2465;"
                + "border-radius:24px;"
                + "padding:35px;"
                + "box-shadow:"
                + "0 25px 60px rgba(0,0,0,.4);"
                + "}"
        );

        // =====================================================
        // TOPO
        // =====================================================

        html.append(
                ".topo-usuario{"
                + "text-align:center;"
                + "padding-bottom:30px;"
                + "border-bottom:1px solid #382043;"
                + "}"
        );

        html.append(
                ".foto-usuario{"
                + "width:150px;"
                + "height:150px;"
                + "border-radius:50%;"
                + "object-fit:cover;"
                + "border:5px solid #7c3aed;"
                + "box-shadow:"
                + "0 0 30px rgba(124,58,237,.35);"
                + "}"
        );

        html.append(
                ".sem-foto{"
                + "width:150px;"
                + "height:150px;"
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
                + "margin:18px 0 5px;"
                + "}"
        );

        html.append(
                ".username-usuario{"
                + "color:#b98be8;"
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
                + "min-width:120px;"
                + "padding:16px 22px;"
                + "background:#160d1e;"
                + "border:1px solid #382045;"
                + "border-radius:13px;"
                + "}"
        );

        html.append(
                ".estatistica strong{"
                + "display:block;"
                + "font-size:23px;"
                + "color:#c084fc;"
                + "}"
        );

        html.append(
                ".estatistica span{"
                + "color:#999;"
                + "font-size:12px;"
                + "}"
        );

        // =====================================================
        // BOTÃO SEGUIR
        // =====================================================

        html.append(
                ".botao-seguir{"
                + "margin-top:22px;"
                + "padding:13px 35px;"
                + "border:none;"
                + "border-radius:10px;"
                + "background:"
                + "linear-gradient("
                + "135deg,#7c3aed,#a855f7"
                + ");"
                + "color:white;"
                + "font-weight:bold;"
                + "font-size:15px;"
                + "cursor:pointer;"
                + "}"
        );

        html.append(
                ".botao-seguindo{"
                + "background:#29173a;"
                + "border:1px solid #8b5cf6;"
                + "}"
        );

        // =====================================================
        // SEÇÕES
        // =====================================================

        html.append(
                ".secao-seguidores{"
                + "margin-top:35px;"
                + "}"
        );

        html.append(
                ".titulo-secao{"
                + "font-size:23px;"
                + "margin-bottom:6px;"
                + "}"
        );

        html.append(
                ".linha{"
                + "width:45px;"
                + "height:3px;"
                + "background:#8b5cf6;"
                + "border-radius:10px;"
                + "margin-bottom:20px;"
                + "}"
        );

        html.append(
                ".lista-usuarios{"
                + "display:grid;"
                + "grid-template-columns:"
                + "repeat(auto-fill,minmax(220px,1fr));"
                + "gap:12px;"
                + "}"
        );

        html.append(
                ".card-seguidor{"
                + "display:flex;"
                + "align-items:center;"
                + "gap:12px;"
                + "padding:13px;"
                + "background:#160d1e;"
                + "border:1px solid #33203d;"
                + "border-radius:13px;"
                + "text-decoration:none;"
                + "color:white;"
                + "transition:.2s;"
                + "}"
        );

        html.append(
                ".card-seguidor:hover{"
                + "border-color:#8b5cf6;"
                + "transform:translateY(-2px);"
                + "}"
        );

        html.append(
                ".mini-foto{"
                + "width:52px;"
                + "height:52px;"
                + "border-radius:50%;"
                + "object-fit:cover;"
                + "border:2px solid #7c3aed;"
                + "flex-shrink:0;"
                + "}"
        );

        html.append(
                ".mini-sem-foto{"
                + "width:52px;"
                + "height:52px;"
                + "border-radius:50%;"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:center;"
                + "background:#281733;"
                + "color:#888;"
                + "font-size:10px;"
                + "flex-shrink:0;"
                + "}"
        );

        html.append(
                ".info-seguidor strong{"
                + "display:block;"
                + "font-size:15px;"
                + "}"
        );

        html.append(
                ".info-seguidor span{"
                + "color:#a855f7;"
                + "font-size:12px;"
                + "}"
        );

        html.append(
                ".vazio{"
                + "padding:25px;"
                + "background:#160d1e;"
                + "border:1px dashed #493254;"
                + "border-radius:13px;"
                + "color:#8d8194;"
                + "text-align:center;"
                + "}"
        );

        html.append(
                "@media(max-width:600px){"
                + ".perfil-usuario-container{"
                + "padding:10px;"
                + "}"
                + ".perfil-usuario-box{"
                + "padding:25px 18px;"
                + "}"
                + ".nome-usuario{"
                + "font-size:27px;"
                + "}"
                + ".lista-usuarios{"
                + "grid-template-columns:1fr;"
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

        // =====================================================
        // CONTEÚDO
        // =====================================================

        html.append(
                "<main class='perfil-usuario-container'>"
        );

        html.append(
                "<div class='perfil-usuario-box'>"
        );

        html.append(
                "<section class='topo-usuario'>"
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

            if (!caminhoFoto.startsWith("http://")
                    &&
                    !caminhoFoto.startsWith("https://")) {

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
                    + escapar(perfil.getNome())
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
                + quantidadeSeguidores
                + "</strong>"
                + "<span>Seguidores</span>"
                + "</div>"
        );

        html.append(
                "<div class='estatistica'>"
                + "<strong>"
                + quantidadeSeguindo
                + "</strong>"
                + "<span>Seguindo</span>"
                + "</div>"
        );

        html.append("</div>");

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
        // SEGUIDORES
        // =====================================================

        html.append(
                "<section class='secao-seguidores'>"
        );

        html.append(
                "<h2 class='titulo-secao'>"
                + "Seguidores"
                + "</h2>"
        );

        html.append(
                "<div class='linha'></div>"
        );

        if (seguidores.isEmpty()) {

            html.append(
                    "<div class='vazio'>"
                    + "Este usuário ainda não possui seguidores."
                    + "</div>"
            );

        } else {

            html.append(
                    "<div class='lista-usuarios'>"
            );

            for (Usuario seguidor :
                    seguidores) {

                html.append(
                        criarCardUsuario(
                                request,
                                seguidor
                        )
                );
            }

            html.append(
                    "</div>"
            );
        }

        html.append(
                "</section>"
        );

        // =====================================================
        // SEGUINDO
        // =====================================================

        html.append(
                "<section class='secao-seguidores'>"
        );

        html.append(
                "<h2 class='titulo-secao'>"
                + "Seguindo"
                + "</h2>"
        );

        html.append(
                "<div class='linha'></div>"
        );

        if (seguindoLista.isEmpty()) {

            html.append(
                    "<div class='vazio'>"
                    + "Este usuário ainda não segue ninguém."
                    + "</div>"
            );

        } else {

            html.append(
                    "<div class='lista-usuarios'>"
            );

            for (Usuario seguido :
                    seguindoLista) {

                html.append(
                        criarCardUsuario(
                                request,
                                seguido
                        )
                );
            }

            html.append(
                    "</div>"
            );
        }

        html.append(
                "</section>"
        );

        html.append(
                "</div>"
        );

        html.append(
                "</main>"
        );

        html.append(
                "</body>"
        );

        html.append(
                "</html>"
        );

        response.getWriter().println(
                html.toString()
        );
    }

    // =====================================================
    // CARD DE USUARIO
    // =====================================================

    private String criarCardUsuario(
            HttpServletRequest request,
            Usuario usuario) throws IOException {

        StringBuilder card =
                new StringBuilder();

        card.append(
                "<a "
                + "class='card-seguidor' "
                + "href='perfil-usuario?id="
                + usuario.getId()
                + "'>"
        );

        String foto =
                usuario.getFoto();

        if (foto != null &&
                !foto.trim().isEmpty()) {

            String caminhoFoto =
                    foto.trim();

            if (!caminhoFoto.startsWith(
                    "http://")
                    &&
                    !caminhoFoto.startsWith(
                    "https://")) {

                caminhoFoto =
                        request.getContextPath()
                        + "/foto-perfil?arquivo="
                        + URLEncoder.encode(
                                caminhoFoto,
                                "UTF-8"
                        );
            }

            card.append(
                    "<img "
                    + "class='mini-foto' "
                    + "src='"
                    + escapar(caminhoFoto)
                    + "' "
                    + "alt='Foto'>"
            );

        } else {

            card.append(
                    "<div class='mini-sem-foto'>"
                    + "Sem foto"
                    + "</div>"
            );
        }

        card.append(
                "<div class='info-seguidor'>"
        );

        card.append(
                "<strong>"
                + escapar(
                        usuario.getNome()
                  )
                + "</strong>"
        );

        card.append(
                "<span>@"
                + escapar(
                        usuario.getUsername()
                  )
                + "</span>"
        );

        card.append(
                "</div>"
        );

        card.append("</a>");

        return card.toString();
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