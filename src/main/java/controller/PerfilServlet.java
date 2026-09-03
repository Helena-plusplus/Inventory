package controller;

import dao.Conexao;
import dao.UsuarioDAO;
import model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

@WebServlet("/perfil")
public class PerfilServlet extends HttpServlet {

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

        try {

            Usuario usuarioSessao =
                    (Usuario) sessao.getAttribute("usuario");

            int idUsuario =
                    usuarioSessao.getId();

            UsuarioDAO dao =
                    new UsuarioDAO();

            Usuario usuario =
                    dao.buscarPorId(idUsuario);

            if (usuario == null) {

                response.sendRedirect("login.html");
                return;
            }

            int totalSeguidores =
                    dao.contarSeguidores(idUsuario);

            int totalSeguindo =
                    dao.contarSeguindo(idUsuario);

            List<Usuario> seguidores =
                    dao.listarSeguidores(idUsuario);

            List<Usuario> seguindo =
                    dao.listarSeguindo(idUsuario);

            List<Jogo> favoritos =
                    carregarFavoritos(idUsuario);

            List<Lista> listas =
                    carregarListas(idUsuario);

            boolean especial =
                    usuario.getEmail() != null
                    &&
                    usuario.getEmail().equalsIgnoreCase(
                            "rebecarodriguesduarte2@gmail.com"
                    );

            response.setContentType(
                    "text/html;charset=UTF-8"
            );

            StringBuilder html =
                    new StringBuilder();

            // =====================================================
            // HTML
            // =====================================================

            html.append("<!DOCTYPE html>");
            html.append("<html lang='pt-BR'>");

            html.append("<head>");

            html.append("<meta charset='UTF-8'>");

            html.append(
                    "<meta name='viewport' " +
                    "content='width=device-width, initial-scale=1.0'>"
            );

            // =====================================================
            // FAVICON
            // =====================================================

            html.append(
                    "<link rel='icon' " +
                    "type='image/png' " +
                    "href='favicon.png'>"
            );

            html.append(
                    "<title>Meu Perfil - Inventory</title>"
            );

            html.append(
                    "<link rel='stylesheet' " +
                    "href='style.css'>"
            );

            // =====================================================
            // CSS
            // =====================================================

            html.append("<style>");

            // RESET

            html.append(
                    "*{" +
                    "box-sizing:border-box;" +
                    "}"
            );

            // =====================================================
            // BODY
            // =====================================================

            html.append(
                    "body{" +
                    "margin:0;" +
                    "font-family:Arial,Helvetica,sans-serif;" +
                    "background:" +
                    "radial-gradient(circle at 15% 0%,#2b1240 0%,transparent 30%)," +
                    "radial-gradient(circle at 100% 100%,#1d0b2d 0%,transparent 35%)," +
                    "#0d0714;" +
                    "color:#fff;" +
                    "min-height:100vh;" +
                    "}"
            );

            // =====================================================
            // PÁGINA
            // =====================================================

            html.append(
                    ".perfil-page{" +
                    "max-width:1180px;" +
                    "margin:0 auto;" +
                    "padding:36px 20px 70px;" +
                    "}"
            );

            // =====================================================
            // CARD PRINCIPAL
            // =====================================================

            html.append(
                    ".perfil-card{" +
                    "position:relative;" +
                    "overflow:hidden;" +
                    "display:flex;" +
                    "align-items:center;" +
                    "gap:28px;" +
                    "padding:30px;" +
                    "background:linear-gradient(145deg,#1d0e29,#130a1c);" +
                    "border:1px solid #43235a;" +
                    "border-radius:20px;" +
                    "box-shadow:0 18px 50px rgba(32,0,55,.40);" +
                    "}"
            );

            html.append(
                    ".perfil-card:before{" +
                    "content:'';" +
                    "position:absolute;" +
                    "width:300px;" +
                    "height:300px;" +
                    "right:-130px;" +
                    "top:-170px;" +
                    "border-radius:50%;" +
                    "background:#7417a8;" +
                    "opacity:.12;" +
                    "}"
            );

            // =====================================================
            // FOTO
            // =====================================================

            html.append(
                    ".foto-area{" +
                    "position:relative;" +
                    "z-index:2;" +
                    "flex-shrink:0;" +
                    "}"
            );

            html.append(
                    ".perfil-foto{" +
                    "width:145px;" +
                    "height:145px;" +
                    "border-radius:50%;" +
                    "object-fit:cover;" +
                    "display:block;" +
                    "border:3px solid #7222a4;" +
                    "background:#160b20;" +
                    "box-shadow:0 0 0 6px rgba(114,34,164,.10);" +
                    "}"
            );

            // =====================================================
            // INFO
            // =====================================================

            html.append(
                    ".perfil-info{" +
                    "position:relative;" +
                    "z-index:2;" +
                    "flex:1;" +
                    "min-width:0;" +
                    "}"
            );

            html.append(
                    ".perfil-nome{" +
                    "margin:0;" +
                    "font-size:37px;" +
                    "font-weight:700;" +
                    "line-height:1.15;" +
                    "color:#fff;" +
                    "}"
            );

            html.append(
                    ".perfil-username{" +
                    "margin-top:7px;" +
                    "font-size:15px;" +
                    "color:#aa8bbd;" +
                    "}"
            );

            html.append(
                    ".perfil-bio{" +
                    "max-width:700px;" +
                    "margin-top:17px;" +
                    "font-size:15px;" +
                    "line-height:1.65;" +
                    "color:#d7c6df;" +
                    "}"
            );

            // =====================================================
            // EMBLEMA
            // =====================================================

            html.append(
                    ".love-badge{" +
                    "display:inline-block;" +
                    "margin-top:15px;" +
                    "padding:6px 12px;" +
                    "border-radius:7px;" +
                    "background:#251033;" +
                    "border:1px solid #71369a;" +
                    "color:#cf9beb;" +
                    "font-size:12px;" +
                    "font-weight:bold;" +
                    "}"
            );

            // =====================================================
            // ESTATÍSTICAS
            // =====================================================

            html.append(
                    ".perfil-stats{" +
                    "display:flex;" +
                    "gap:10px;" +
                    "flex-wrap:wrap;" +
                    "margin-top:23px;" +
                    "}"
            );

            html.append(
                    ".stat-card{" +
                    "min-width:105px;" +
                    "padding:12px 15px;" +
                    "background:#160b20;" +
                    "border:1px solid #382047;" +
                    "border-radius:10px;" +
                    "}"
            );

            html.append(
                    ".stat-card:hover{" +
                    "border-color:#64338a;" +
                    "}"
            );

            html.append(
                    ".stat-numero{" +
                    "font-size:23px;" +
                    "font-weight:700;" +
                    "color:#fff;" +
                    "}"
            );

            html.append(
                    ".stat-texto{" +
                    "margin-top:4px;" +
                    "font-size:11px;" +
                    "color:#9a80a9;" +
                    "text-transform:uppercase;" +
                    "letter-spacing:.6px;" +
                    "}"
            );

            // =====================================================
            // BOTÕES
            // =====================================================

            html.append(
                    ".perfil-buttons{" +
                    "display:flex;" +
                    "gap:9px;" +
                    "flex-wrap:wrap;" +
                    "margin-top:18px;" +
                    "}"
            );

            html.append(
                    ".perfil-button{" +
                    "display:inline-block;" +
                    "padding:10px 16px;" +
                    "border-radius:8px;" +
                    "background:#6819a0;" +
                    "border:1px solid #7c2bb5;" +
                    "color:#fff;" +
                    "text-decoration:none;" +
                    "font-size:13px;" +
                    "font-weight:bold;" +
                    "transition:.2s;" +
                    "}"
            );

            html.append(
                    ".perfil-button:hover{" +
                    "background:#7e24ba;" +
                    "border-color:#9743ce;" +
                    "}"
            );

            // =====================================================
            // GRID
            // =====================================================

            html.append(
                    ".perfil-grid{" +
                    "display:grid;" +
                    "grid-template-columns:minmax(0,2fr) minmax(280px,1fr);" +
                    "gap:20px;" +
                    "margin-top:20px;" +
                    "}"
            );

            html.append(
                    ".perfil-grid > div{" +
                    "min-width:0;" +
                    "}"
            );

            // =====================================================
            // SEÇÕES
            // =====================================================

            html.append(
                    ".secao{" +
                    "background:linear-gradient(145deg,#180d23,#110916);" +
                    "border:1px solid #352044;" +
                    "border-radius:16px;" +
                    "padding:20px;" +
                    "margin-bottom:20px;" +
                    "box-shadow:0 12px 30px rgba(25,0,45,.18);" +
                    "}"
            );

            html.append(
                    ".secao:hover{" +
                    "border-color:#4a285d;" +
                    "}"
            );

            html.append(
                    ".secao-titulo{" +
                    "margin:0 0 18px;" +
                    "font-size:19px;" +
                    "font-weight:700;" +
                    "color:#f4ecf8;" +
                    "}"
            );

            // =====================================================
            // FAVORITOS
            // =====================================================

            html.append(
                    ".jogos-grid{" +
                    "display:grid;" +
                    "grid-template-columns:repeat(auto-fill,minmax(145px,1fr));" +
                    "gap:15px;" +
                    "}"
            );

            html.append(
                    ".jogo-card{" +
                    "overflow:hidden;" +
                    "background:#100817;" +
                    "border:1px solid #32203e;" +
                    "border-radius:11px;" +
                    "transition:.2s;" +
                    "}"
            );

            html.append(
                    ".jogo-card:hover{" +
                    "transform:translateY(-3px);" +
                    "border-color:#7024a2;" +
                    "box-shadow:0 12px 25px rgba(73,0,110,.22);" +
                    "}"
            );

            html.append(
                    ".capa-container{" +
                    "width:100%;" +
                    "height:220px;" +
                    "overflow:hidden;" +
                    "background:#100817;" +
                    "}"
            );

            html.append(
                    ".jogo-capa{" +
                    "width:100%;" +
                    "height:220px;" +
                    "object-fit:cover;" +
                    "display:block;" +
                    "transition:.25s;" +
                    "}"
            );

            html.append(
                    ".jogo-card:hover .jogo-capa{" +
                    "transform:scale(1.03);" +
                    "}"
            );

            html.append(
                    ".jogo-info{" +
                    "padding:12px;" +
                    "background:#100817;" +
                    "}"
            );

            html.append(
                    ".jogo-titulo{" +
                    "font-size:13px;" +
                    "font-weight:bold;" +
                    "line-height:1.4;" +
                    "color:#ebe1f0;" +
                    "}"
            );

            // =====================================================
            // LISTAS
            // =====================================================

            html.append(
                    ".lista-card{" +
                    "background:#130a1b;" +
                    "border:1px solid #34203f;" +
                    "border-radius:11px;" +
                    "padding:16px;" +
                    "margin-bottom:12px;" +
                    "}"
            );

            html.append(
                    ".lista-card:hover{" +
                    "border-color:#5d267b;" +
                    "}"
            );

            html.append(
                    ".lista-nome{" +
                    "margin-bottom:14px;" +
                    "font-size:17px;" +
                    "font-weight:700;" +
                    "color:#f2eaf6;" +
                    "}"
            );

            html.append(
                    ".lista-jogos{" +
                    "display:grid;" +
                    "grid-template-columns:repeat(auto-fill,minmax(84px,1fr));" +
                    "gap:9px;" +
                    "}"
            );

            html.append(
                    ".lista-capa{" +
                    "width:100%;" +
                    "height:120px;" +
                    "object-fit:cover;" +
                    "display:block;" +
                    "border-radius:7px;" +
                    "background:#100817;" +
                    "border:1px solid #302039;" +
                    "transition:.2s;" +
                    "}"
            );

            html.append(
                    ".lista-capa:hover{" +
                    "transform:scale(1.03);" +
                    "border-color:#7024a2;" +
                    "}"
            );

            // =====================================================
            // USUÁRIOS
            // =====================================================

            html.append(
                    ".usuario-item{" +
                    "display:flex;" +
                    "align-items:center;" +
                    "gap:12px;" +
                    "padding:11px 0;" +
                    "border-bottom:1px solid #30203a;" +
                    "}"
            );

            html.append(
                    ".usuario-item:last-child{" +
                    "border-bottom:none;" +
                    "}"
            );

            html.append(
                    ".foto-mini{" +
                    "width:44px;" +
                    "height:44px;" +
                    "border-radius:50%;" +
                    "object-fit:cover;" +
                    "background:#150b1e;" +
                    "border:1px solid #4a2a5b;" +
                    "flex-shrink:0;" +
                    "}"
            );

            html.append(
                    ".usuario-link{" +
                    "color:#eee6f3;" +
                    "text-decoration:none;" +
                    "font-weight:bold;" +
                    "font-size:14px;" +
                    "}"
            );

            html.append(
                    ".usuario-link:hover{" +
                    "color:#b96add;" +
                    "}"
            );

            // =====================================================
            // VAZIO
            // =====================================================

            html.append(
                    ".vazio{" +
                    "text-align:center;" +
                    "padding:25px 8px;" +
                    "color:#816d8b;" +
                    "font-size:13px;" +
                    "}"
            );

            // =====================================================
            // RESPONSIVO
            // =====================================================

            html.append(
                    "@media(max-width:850px){" +

                    ".perfil-card{" +
                    "flex-direction:column;" +
                    "text-align:center;" +
                    "}" +

                    ".perfil-stats{" +
                    "justify-content:center;" +
                    "}" +

                    ".perfil-buttons{" +
                    "justify-content:center;" +
                    "}" +

                    ".perfil-grid{" +
                    "grid-template-columns:1fr;" +
                    "}" +

                    ".perfil-nome{" +
                    "font-size:31px;" +
                    "}" +

                    "}"
            );

            html.append(
                    "@media(max-width:500px){" +

                    ".perfil-page{" +
                    "padding-left:12px;" +
                    "padding-right:12px;" +
                    "}" +

                    ".perfil-card{" +
                    "padding:22px 16px;" +
                    "}" +

                    ".perfil-foto{" +
                    "width:120px;" +
                    "height:120px;" +
                    "}" +

                    ".secao{" +
                    "padding:16px;" +
                    "}" +

                    ".jogos-grid{" +
                    "grid-template-columns:repeat(2,1fr);" +
                    "}" +

                    "}"
            );

            html.append("</style>");

            html.append("</head>");

            html.append("<body>");

            // =====================================================
            // HEADER
            // =====================================================

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
                    "<a href='buscar-usuarios'>" +
                    "Buscar usuários" +
                    "</a>"
            );

            html.append(
                    "<a href='listas'>Listas</a>"
            );

            html.append(
                    "<a href='perfil'>Meu Perfil</a>"
            );

            html.append(
                    "<a href='logout'>Sair</a>"
            );

            html.append("</nav>");

            // =====================================================
            // CONTEÚDO
            // =====================================================

            html.append(
                    "<main class='perfil-page'>"
            );

            // =====================================================
            // PERFIL
            // =====================================================

            html.append(
                    "<section class='perfil-card'>"
            );

            String foto =
                    prepararFoto(
                            usuario.getFoto(),
                            request
                    );

            html.append(
                    "<div class='foto-area'>"
            );

            if (!foto.isEmpty()) {

                html.append(
                        "<img class='perfil-foto' " +
                        "src='" +
                        escaparHtml(foto) +
                        "' " +
                        "alt='Foto de perfil'>"
                );

            } else {

                html.append(
                        "<div class='perfil-foto' " +
                        "style='display:flex;" +
                        "align-items:center;" +
                        "justify-content:center;" +
                        "font-size:38px;" +
                        "font-weight:bold;" +
                        "color:#69447a;'>" +
                        "U" +
                        "</div>"
                );
            }

            html.append("</div>");

            html.append(
                    "<div class='perfil-info'>"
            );

            // NOME

            html.append(
                    "<h1 class='perfil-nome'>" +
                    escaparHtml(usuario.getNome()) +
                    "</h1>"
            );

            // USERNAME

            String username =
                    usuario.getUsername();

            if (username == null ||
                    username.trim().isEmpty()) {

                username =
                        usuario.getNome();
            }

            html.append(
                    "<div class='perfil-username'>@" +
                    escaparHtml(username) +
                    "</div>"
            );

            // BIO

            if (usuario.getBio() != null &&
                    !usuario.getBio().trim().isEmpty()) {

                html.append(
                        "<div class='perfil-bio'>" +
                        escaparHtml(usuario.getBio()) +
                        "</div>"
                );
            }

            // EMBLEMA

            if (especial) {

                html.append(
                        "<span class='love-badge'>" +
                        "My Love" +
                        "</span>"
                );
            }

            // =====================================================
            // ESTATÍSTICAS
            // =====================================================

            html.append(
                    "<div class='perfil-stats'>"
            );

            html.append(
                    "<div class='stat-card'>" +
                    "<div class='stat-numero'>" +
                    totalSeguidores +
                    "</div>" +
                    "<div class='stat-texto'>" +
                    "Seguidores" +
                    "</div>" +
                    "</div>"
            );

            html.append(
                    "<div class='stat-card'>" +
                    "<div class='stat-numero'>" +
                    totalSeguindo +
                    "</div>" +
                    "<div class='stat-texto'>" +
                    "Seguindo" +
                    "</div>" +
                    "</div>"
            );

            html.append(
                    "<div class='stat-card'>" +
                    "<div class='stat-numero'>" +
                    favoritos.size() +
                    "</div>" +
                    "<div class='stat-texto'>" +
                    "Favoritos" +
                    "</div>" +
                    "</div>"
            );

            html.append(
                    "<div class='stat-card'>" +
                    "<div class='stat-numero'>" +
                    listas.size() +
                    "</div>" +
                    "<div class='stat-texto'>" +
                    "Listas" +
                    "</div>" +
                    "</div>"
            );

            html.append("</div>");

            // =====================================================
            // BOTÕES
            // =====================================================

            html.append(
                    "<div class='perfil-buttons'>"
            );

            html.append(
                    "<a class='perfil-button' " +
                    "href='listas'>" +
                    "Minhas listas" +
                    "</a>"
            );

            html.append(
                    "<a class='perfil-button' " +
                    "href='buscar-usuarios'>" +
                    "Buscar usuários" +
                    "</a>"
            );

            html.append("</div>");

            html.append("</div>");

            html.append("</section>");

            // =====================================================
            // GRID PRINCIPAL
            // =====================================================

            html.append(
                    "<div class='perfil-grid'>"
            );

            // =====================================================
            // COLUNA ESQUERDA
            // =====================================================

            html.append("<div>");

            // =====================================================
            // FAVORITOS
            // =====================================================

            html.append(
                    "<section class='secao'>"
            );

            html.append(
                    "<h2 class='secao-titulo'>" +
                    "Favoritos" +
                    "</h2>"
            );

            if (favoritos.isEmpty()) {

                html.append(
                        "<div class='vazio'>" +
                        "Nenhum jogo favorito ainda." +
                        "</div>"
                );

            } else {

                html.append(
                        "<div class='jogos-grid'>"
                );

                for (Jogo jogo :
                        favoritos) {

                    html.append(
                            montarCardJogo(
                                    jogo,
                                    request
                            )
                    );
                }

                html.append("</div>");
            }

            html.append("</section>");

            // =====================================================
            // LISTAS
            // =====================================================

            html.append(
                    "<section class='secao'>"
            );

            html.append(
                    "<h2 class='secao-titulo'>" +
                    "Minhas listas" +
                    "</h2>"
            );

            if (listas.isEmpty()) {

                html.append(
                        "<div class='vazio'>" +
                        "Nenhuma lista criada ainda." +
                        "</div>"
                );

            } else {

                for (Lista lista :
                        listas) {

                    html.append(
                            "<div class='lista-card'>"
                    );

                    html.append(
                            "<div class='lista-nome'>" +
                            escaparHtml(lista.nome) +
                            "</div>"
                    );

                    if (lista.jogos.isEmpty()) {

                        html.append(
                                "<div class='vazio'>" +
                                "Esta lista está vazia." +
                                "</div>"
                        );

                    } else {

                        html.append(
                                "<div class='lista-jogos'>"
                        );

                        for (Jogo jogo :
                                lista.jogos) {

                            String capa =
                                    prepararCapa(
                                            jogo.capa,
                                            request
                                    );

                            if (capa == null ||
                                    capa.isEmpty()) {

                                continue;
                            }

                            html.append(
                                    "<img " +
                                    "class='lista-capa' " +
                                    "src='" +
                                    escaparHtml(capa) +
                                    "' " +
                                    "alt='Capa de " +
                                    escaparHtml(jogo.titulo) +
                                    "' " +
                                    "onerror='this.style.display=\"none\";'>"
                            );
                        }

                        html.append("</div>");
                    }

                    html.append("</div>");
                }
            }

            html.append("</section>");

            html.append("</div>");

            // =====================================================
            // COLUNA DIREITA
            // =====================================================

            html.append("<div>");

            // =====================================================
            // SEGUIDORES
            // =====================================================

            html.append(
                    "<section class='secao'>"
            );

            html.append(
                    "<h2 class='secao-titulo'>" +
                    "Seguidores" +
                    "</h2>"
            );

            if (seguidores.isEmpty()) {

                html.append(
                        "<div class='vazio'>" +
                        "Nenhum seguidor ainda." +
                        "</div>"
                );

            } else {

                for (Usuario u :
                        seguidores) {

                    html.append(
                            montarUsuario(
                                    u,
                                    request
                            )
                    );
                }
            }

            html.append("</section>");

            // =====================================================
            // SEGUINDO
            // =====================================================

            html.append(
                    "<section class='secao'>"
            );

            html.append(
                    "<h2 class='secao-titulo'>" +
                    "Seguindo" +
                    "</h2>"
            );

            if (seguindo.isEmpty()) {

                html.append(
                        "<div class='vazio'>" +
                        "Você ainda não segue ninguém." +
                        "</div>"
                );

            } else {

                for (Usuario u :
                        seguindo) {

                    html.append(
                            montarUsuario(
                                    u,
                                    request
                            )
                    );
                }
            }

            html.append("</section>");

            html.append("</div>");

            html.append("</div>");

            html.append("</main>");

            html.append("</body>");

            html.append("</html>");

            response.getWriter().println(
                    html.toString()
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect("index.html");
        }
    }

    // =========================================================
    // FAVORITOS
    // =========================================================

    private List<Jogo> carregarFavoritos(
            int idUsuario)
            throws Exception {

        List<Jogo> jogos =
                new ArrayList<>();

        Connection conexao =
                Conexao.conectar();

        PreparedStatement stmt =
                conexao.prepareStatement(
                        "SELECT j.id,j.titulo,j.capa " +
                        "FROM favorito f " +
                        "INNER JOIN jogo j " +
                        "ON j.id=f.id_jogo " +
                        "WHERE f.id_usuario=? " +
                        "ORDER BY f.id DESC"
                );

        stmt.setInt(1, idUsuario);

        ResultSet rs =
                stmt.executeQuery();

        while (rs.next()) {

            Jogo jogo =
                    new Jogo();

            jogo.id =
                    rs.getInt("id");

            jogo.titulo =
                    rs.getString("titulo");

            jogo.capa =
                    rs.getString("capa");

            jogos.add(jogo);
        }

        rs.close();
        stmt.close();
        conexao.close();

        return jogos;
    }

    // =========================================================
    // LISTAS
    // =========================================================

    private List<Lista> carregarListas(
            int idUsuario)
            throws Exception {

        List<Lista> listas =
                new ArrayList<>();

        Connection conexao =
                Conexao.conectar();

        PreparedStatement stmt =
                conexao.prepareStatement(
                        "SELECT id,nome " +
                        "FROM lista " +
                        "WHERE id_usuario=? " +
                        "ORDER BY id DESC"
                );

        stmt.setInt(1, idUsuario);

        ResultSet rs =
                stmt.executeQuery();

        while (rs.next()) {

            Lista lista =
                    new Lista();

            lista.id =
                    rs.getInt("id");

            lista.nome =
                    rs.getString("nome");

            lista.jogos =
                    carregarJogosLista(
                            lista.id
                    );

            listas.add(lista);
        }

        rs.close();
        stmt.close();
        conexao.close();

        return listas;
    }

    // =========================================================
    // JOGOS DA LISTA
    // =========================================================

    private List<Jogo> carregarJogosLista(
            int idLista)
            throws Exception {

        List<Jogo> jogos =
                new ArrayList<>();

        Connection conexao =
                Conexao.conectar();

        PreparedStatement stmt =
                conexao.prepareStatement(
                        "SELECT j.id,j.titulo,j.capa " +
                        "FROM lista_jogo lj " +
                        "INNER JOIN jogo j " +
                        "ON j.id=lj.id_jogo " +
                        "WHERE lj.id_lista=? " +
                        "ORDER BY lj.id ASC"
                );

        stmt.setInt(1, idLista);

        ResultSet rs =
                stmt.executeQuery();

        while (rs.next()) {

            Jogo jogo =
                    new Jogo();

            jogo.id =
                    rs.getInt("id");

            jogo.titulo =
                    rs.getString("titulo");

            jogo.capa =
                    rs.getString("capa");

            jogos.add(jogo);
        }

        rs.close();
        stmt.close();
        conexao.close();

        return jogos;
    }

    // =========================================================
    // CARD DE JOGO
    // =========================================================

    private String montarCardJogo(
            Jogo jogo,
            HttpServletRequest request) {

        StringBuilder html =
                new StringBuilder();

        String capa =
                prepararCapa(
                        jogo.capa,
                        request
                );

        if (capa == null ||
                capa.isEmpty()) {

            capa =
                    request.getContextPath()
                    + "/capa?id="
                    + jogo.id;
        }

        html.append(
                "<div class='jogo-card'>"
        );

        html.append(
                "<div class='capa-container'>"
        );

        html.append(
                "<img " +
                "class='jogo-capa' " +
                "src='" +
                escaparHtml(capa) +
                "' " +
                "alt='Capa de " +
                escaparHtml(jogo.titulo) +
                "' " +
                "onerror='this.style.display=\"none\";'>"
        );

        html.append("</div>");

        html.append(
                "<div class='jogo-info'>"
        );

        html.append(
                "<div class='jogo-titulo'>" +
                escaparHtml(jogo.titulo) +
                "</div>"
        );

        html.append("</div>");

        html.append("</div>");

        return html.toString();
    }

    // =========================================================
    // USUARIO
    // =========================================================

    private String montarUsuario(
            Usuario usuario,
            HttpServletRequest request) {

        StringBuilder html =
                new StringBuilder();

        String foto =
                prepararFoto(
                        usuario.getFoto(),
                        request
                );

        html.append(
                "<div class='usuario-item'>"
        );

        if (!foto.isEmpty()) {

            html.append(
                    "<img " +
                    "class='foto-mini' " +
                    "src='" +
                    escaparHtml(foto) +
                    "' " +
                    "alt='Foto'>"
            );

        } else {

            html.append(
                    "<div class='foto-mini' " +
                    "style='display:flex;" +
                    "align-items:center;" +
                    "justify-content:center;" +
                    "font-size:16px;" +
                    "font-weight:bold;" +
                    "color:#69447a;'>" +
                    "U" +
                    "</div>"
            );
        }

        html.append(
                "<a class='usuario-link' " +
                "href='perfil-usuario?id=" +
                usuario.getId() +
                "'>" +
                escaparHtml(usuario.getNome()) +
                "</a>"
        );

        html.append("</div>");

        return html.toString();
    }

    // =========================================================
    // FOTO
    // =========================================================

    private String prepararFoto(
            String foto,
            HttpServletRequest request) {

        if (foto == null ||
                foto.trim().isEmpty()) {

            return "";
        }

        foto =
                foto.trim();

        if (foto.startsWith("http://") ||
                foto.startsWith("https://")) {

            return foto;
        }

        if (foto.startsWith("/")) {

            return
                    request.getContextPath()
                    + foto;
        }

        return
                request.getContextPath()
                + "/foto-perfil?arquivo="
                + foto;
    }

    // =========================================================
    // CAPA
    // =========================================================

    private String prepararCapa(
            String capa,
            HttpServletRequest request) {

        if (capa == null ||
                capa.trim().isEmpty()) {

            return null;
        }

        capa =
                capa.trim();

        // MARKDOWN

        if (capa.startsWith("[") &&
                capa.contains("](") &&
                capa.endsWith(")")) {

            int inicio =
                    capa.indexOf("](");

            if (inicio >= 0) {

                String url =
                        capa.substring(
                                inicio + 2,
                                capa.length() - 1
                        );

                if (url.startsWith("http://") ||
                        url.startsWith("https://")) {

                    return url;
                }
            }
        }

        // APP ID

        if (capa.matches("\\d+")) {

            return
                    "https://cdn.akamai.steamstatic.com/" +
                    "steam/apps/" +
                    capa +
                    "/library_600x900_2x.jpg";
        }

        // URL STEAM

        java.util.regex.Matcher matcher =
                java.util.regex.Pattern
                        .compile("/apps/(\\d+)")
                        .matcher(capa);

        if (matcher.find()) {

            String appId =
                    matcher.group(1);

            return
                    "https://cdn.akamai.steamstatic.com/" +
                    "steam/apps/" +
                    appId +
                    "/library_600x900_2x.jpg";
        }

        // URL NORMAL

        if (capa.startsWith("http://") ||
                capa.startsWith("https://")) {

            return capa;
        }

        // CAMINHO LOCAL

        return
                request.getContextPath()
                + "/"
                + capa.replaceFirst("^/+", "");
    }

    // =========================================================
    // ESCAPAR HTML
    // =========================================================

    private String escaparHtml(
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

    // =========================================================
    // CLASSES
    // =========================================================

    private static class Jogo {

        int id;

        String titulo;

        String capa;
    }

    private static class Lista {

        int id;

        String nome;

        List<Jogo> jogos =
                new ArrayList<>();
    }
}