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

            // FAVICON
            html.append(
                    "<link rel='icon' " +
                    "type='image/png' " +
                    "href='LOGO.png'>"
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

            html.append(
                    "*{" +
                    "box-sizing:border-box;" +
                    "}"
            );

            html.append(
                    "body{" +
                    "margin:0;" +
                    "font-family:Arial,Helvetica,sans-serif;" +
                    "background:" +
                    "radial-gradient(circle at top left,#31134b 0%,transparent 35%)," +
                    "radial-gradient(circle at bottom right,#24103c 0%,transparent 35%)," +
                    "#0f0b14;" +
                    "color:#fff;" +
                    "min-height:100vh;" +
                    "}"
            );

            // =====================================================
            // PÁGINA
            // =====================================================

            html.append(
                    ".perfil-page{" +
                    "max-width:1200px;" +
                    "margin:0 auto;" +
                    "padding:38px 20px 70px;" +
                    "}"
            );

            // =====================================================
            // HERO
            // =====================================================

            html.append(
                    ".perfil-card{" +
                    "position:relative;" +
                    "overflow:hidden;" +
                    "display:flex;" +
                    "align-items:center;" +
                    "gap:28px;" +
                    "padding:32px;" +
                    "border-radius:24px;" +
                    "background:linear-gradient(135deg," +
                    "rgba(43,18,61,.95)," +
                    "rgba(24,28,36,.96));" +
                    "border:1px solid rgba(155,70,220,.25);" +
                    "box-shadow:0 20px 60px rgba(0,0,0,.35);" +
                    "}"
            );

            html.append(
                    ".perfil-card:before{" +
                    "content:'';" +
                    "position:absolute;" +
                    "width:300px;" +
                    "height:300px;" +
                    "right:-120px;" +
                    "top:-150px;" +
                    "background:#7200d6;" +
                    "opacity:.13;" +
                    "border-radius:50%;" +
                    "}"
            );

            // FOTO

            html.append(
                    ".foto-area{" +
                    "position:relative;" +
                    "flex-shrink:0;" +
                    "}"
            );

            html.append(
                    ".perfil-foto{" +
                    "width:155px;" +
                    "height:155px;" +
                    "border-radius:50%;" +
                    "object-fit:cover;" +
                    "display:block;" +
                    "border:4px solid #7d16d6;" +
                    "background:#17131d;" +
                    "box-shadow:" +
                    "0 0 0 6px rgba(125,22,214,.12)," +
                    "0 0 35px rgba(125,22,214,.35);" +
                    "}"
            );

            // INFORMAÇÕES

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
                    "font-size:38px;" +
                    "line-height:1.1;" +
                    "margin:0;" +
                    "font-weight:800;" +
                    "letter-spacing:-.5px;" +
                    "}"
            );

            html.append(
                    ".perfil-username{" +
                    "margin-top:8px;" +
                    "font-size:16px;" +
                    "color:#a39aaa;" +
                    "}"
            );

            html.append(
                    ".perfil-bio{" +
                    "max-width:700px;" +
                    "margin-top:18px;" +
                    "font-size:16px;" +
                    "line-height:1.6;" +
                    "color:#ddd6e4;" +
                    "}"
            );

            // EMBLEMA

            html.append(
                    ".love-badge{" +
                    "display:inline-flex;" +
                    "align-items:center;" +
                    "gap:6px;" +
                    "margin-top:17px;" +
                    "padding:8px 14px;" +
                    "border-radius:30px;" +
                    "background:linear-gradient(135deg,#6900c9,#9200ff);" +
                    "font-size:13px;" +
                    "font-weight:bold;" +
                    "box-shadow:0 8px 22px rgba(110,0,200,.25);" +
                    "}"
            );

            // =====================================================
            // ESTATÍSTICAS
            // =====================================================

            html.append(
                    ".perfil-stats{" +
                    "display:flex;" +
                    "gap:12px;" +
                    "flex-wrap:wrap;" +
                    "margin-top:24px;" +
                    "}"
            );

            html.append(
                    ".stat-card{" +
                    "min-width:110px;" +
                    "padding:13px 16px;" +
                    "border-radius:13px;" +
                    "background:rgba(255,255,255,.045);" +
                    "border:1px solid rgba(255,255,255,.07);" +
                    "}"
            );

            html.append(
                    ".stat-numero{" +
                    "font-size:24px;" +
                    "font-weight:800;" +
                    "}"
            );

            html.append(
                    ".stat-texto{" +
                    "margin-top:4px;" +
                    "font-size:12px;" +
                    "text-transform:uppercase;" +
                    "letter-spacing:.6px;" +
                    "color:#95909d;" +
                    "}"
            );

            // =====================================================
            // BOTÕES
            // =====================================================

            html.append(
                    ".perfil-buttons{" +
                    "display:flex;" +
                    "gap:10px;" +
                    "flex-wrap:wrap;" +
                    "margin-top:20px;" +
                    "}"
            );

            html.append(
                    ".perfil-button{" +
                    "display:inline-flex;" +
                    "align-items:center;" +
                    "justify-content:center;" +
                    "padding:11px 17px;" +
                    "border-radius:10px;" +
                    "background:linear-gradient(135deg,#6500c7,#8500ec);" +
                    "color:#fff;" +
                    "text-decoration:none;" +
                    "font-size:14px;" +
                    "font-weight:bold;" +
                    "transition:.2s;" +
                    "box-shadow:0 8px 20px rgba(99,0,192,.18);" +
                    "}"
            );

            html.append(
                    ".perfil-button:hover{" +
                    "transform:translateY(-2px);" +
                    "box-shadow:0 12px 28px rgba(99,0,192,.3);" +
                    "}"
            );

            // =====================================================
            // GRID PRINCIPAL
            // =====================================================

            html.append(
                    ".perfil-grid{" +
                    "display:grid;" +
                    "grid-template-columns:minmax(0,2fr) minmax(270px,1fr);" +
                    "gap:22px;" +
                    "margin-top:22px;" +
                    "}"
            );

            // =====================================================
            // SEÇÕES
            // =====================================================

            html.append(
                    ".secao{" +
                    "background:rgba(29,25,35,.88);" +
                    "border:1px solid rgba(255,255,255,.07);" +
                    "border-radius:18px;" +
                    "padding:22px;" +
                    "margin-bottom:22px;" +
                    "box-shadow:0 12px 35px rgba(0,0,0,.18);" +
                    "}"
            );

            html.append(
                    ".secao-titulo{" +
                    "display:flex;" +
                    "align-items:center;" +
                    "justify-content:space-between;" +
                    "margin:0 0 20px;" +
                    "font-size:20px;" +
                    "font-weight:800;" +
                    "}"
            );

            // =====================================================
            // FAVORITOS
            // =====================================================

            html.append(
                    ".jogos-grid{" +
                    "display:grid;" +
                    "grid-template-columns:" +
                    "repeat(auto-fill,minmax(145px,1fr));" +
                    "gap:17px;" +
                    "}"
            );

            html.append(
                    ".jogo-card{" +
                    "overflow:hidden;" +
                    "border-radius:14px;" +
                    "background:#15121a;" +
                    "border:1px solid #2f2935;" +
                    "transition:.25s;" +
                    "}"
            );

            html.append(
                    ".jogo-card:hover{" +
                    "transform:translateY(-6px);" +
                    "border-color:#7d16d6;" +
                    "box-shadow:0 15px 30px rgba(0,0,0,.3);" +
                    "}"
            );

            html.append(
                    ".capa-container{" +
                    "width:100%;" +
                    "height:225px;" +
                    "overflow:hidden;" +
                    "background:#15121a;" +
                    "}"
            );

            html.append(
                    ".jogo-capa{" +
                    "width:100%;" +
                    "height:225px;" +
                    "object-fit:cover;" +
                    "display:block;" +
                    "transition:.3s;" +
                    "}"
            );

            html.append(
                    ".jogo-card:hover .jogo-capa{" +
                    "transform:scale(1.04);" +
                    "}"
            );

            html.append(
                    ".jogo-info{" +
                    "padding:13px;" +
                    "}"
            );

            html.append(
                    ".jogo-titulo{" +
                    "font-size:14px;" +
                    "font-weight:bold;" +
                    "line-height:1.4;" +
                    "color:#f2edf6;" +
                    "}"
            );

            // =====================================================
            // LISTAS
            // =====================================================

            html.append(
                    ".lista-card{" +
                    "background:rgba(20,17,25,.82);" +
                    "border:1px solid #312b36;" +
                    "border-radius:14px;" +
                    "padding:17px;" +
                    "margin-bottom:14px;" +
                    "}"
            );

            html.append(
                    ".lista-nome{" +
                    "font-size:18px;" +
                    "font-weight:800;" +
                    "margin-bottom:15px;" +
                    "}"
            );

            html.append(
                    ".lista-jogos{" +
                    "display:grid;" +
                    "grid-template-columns:" +
                    "repeat(auto-fill,minmax(85px,1fr));" +
                    "gap:10px;" +
                    "}"
            );

            html.append(
                    ".lista-capa{" +
                    "width:100%;" +
                    "height:125px;" +
                    "object-fit:cover;" +
                    "display:block;" +
                    "border-radius:9px;" +
                    "background:#17131d;" +
                    "border:1px solid #2d2732;" +
                    "transition:.2s;" +
                    "}"
            );

            html.append(
                    ".lista-capa:hover{" +
                    "transform:scale(1.03);" +
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
                    "padding:12px 0;" +
                    "border-bottom:1px solid rgba(255,255,255,.07);" +
                    "}"
            );

            html.append(
                    ".usuario-item:last-child{" +
                    "border-bottom:none;" +
                    "}"
            );

            html.append(
                    ".foto-mini{" +
                    "width:46px;" +
                    "height:46px;" +
                    "border-radius:50%;" +
                    "object-fit:cover;" +
                    "background:#17131d;" +
                    "border:2px solid #403748;" +
                    "flex-shrink:0;" +
                    "}"
            );

            html.append(
                    ".usuario-link{" +
                    "color:#fff;" +
                    "text-decoration:none;" +
                    "font-weight:bold;" +
                    "font-size:14px;" +
                    "transition:.2s;" +
                    "}"
            );

            html.append(
                    ".usuario-link:hover{" +
                    "color:#b85dff;" +
                    "}"
            );

            // =====================================================
            // VAZIO
            // =====================================================

            html.append(
                    ".vazio{" +
                    "text-align:center;" +
                    "padding:28px 10px;" +
                    "color:#827b89;" +
                    "font-size:14px;" +
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
                    "padding:25px 18px;" +
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

                    ".perfil-foto{" +
                    "width:125px;" +
                    "height:125px;" +
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

            html.append("</header>");

            // =====================================================
            // PÁGINA
            // =====================================================

            html.append(
                    "<main class='perfil-page'>"
            );

            // =====================================================
            // HERO PERFIL
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
                        "font-size:55px;'>" +
                        "👤" +
                        "</div>"
                );
            }

            html.append("</div>");

            html.append(
                    "<div class='perfil-info'>"
            );

            html.append(
                    "<h1 class='perfil-nome'>" +
                    escaparHtml(usuario.getNome()) +
                    "</h1>"
            );

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

            if (usuario.getBio() != null &&
                    !usuario.getBio().trim().isEmpty()) {

                html.append(
                        "<div class='perfil-bio'>" +
                        escaparHtml(usuario.getBio()) +
                        "</div>"
                );
            }

            if (especial) {

                html.append(
                        "<span class='love-badge'>" +
                        "♡ My Love" +
                        "</span>"
                );
            }

            // =====================================================
            // STATS
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
            // GRID
            // =====================================================

            html.append(
                    "<div class='perfil-grid'>"
            );

            // =====================================================
            // ESQUERDA
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
                    "❤️ Favoritos" +
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
                    "📚 Minhas listas" +
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
            // DIREITA
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
                    "👥 Seguidores" +
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
                    "➕ Seguindo" +
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
    // CARD FAVORITO
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
                    "font-size:21px;'>" +
                    "👤" +
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

        // Markdown: [texto](url)

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

        // Caso seja somente o APP ID

        if (capa.matches("\\d+")) {

            return
                    "https://cdn.akamai.steamstatic.com/" +
                    "steam/apps/" +
                    capa +
                    "/library_600x900_2x.jpg";
        }

        // Caso contenha /apps/123456/

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

        // URL normal

        if (capa.startsWith("http://") ||
                capa.startsWith("https://")) {

            return capa;
        }

        // Caminho local

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