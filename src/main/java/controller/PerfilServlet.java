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
                    (Usuario) sessao.getAttribute(
                            "usuario"
                    );

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
                    usuario.getEmail()
                            .equalsIgnoreCase(
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

            html.append(
                    "<meta charset='UTF-8'>"
            );

            html.append(
                    "<meta name='viewport' " +
                    "content='width=device-width, " +
                    "initial-scale=1.0'>"
            );

            html.append(
                    "<title>Meu Perfil - Inventory</title>"
            );

            // =====================================================
            // FAVICON
            // =====================================================

            html.append(
                    "<link rel='icon' " +
                    "type='image/png' " +
                    "href='icon.png'>"
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
                    "html,body{" +
                    "margin:0;" +
                    "padding:0;" +
                    "width:100%;" +
                    "min-height:100%;" +
                    "}"
            );

            // =====================================================
            // BODY
            // =====================================================

            html.append(
                    "body{" +
                    "background:" +
                    "radial-gradient(" +
                    "circle at top," +
                    "#35105f 0%," +
                    "#160b22 45%," +
                    "#09060d 100%);" +
                    "color:#fff;" +
                    "font-family:Arial,Helvetica,sans-serif;" +
                    "min-height:100vh;" +
                    "}"
            );

            // =====================================================
            // HEADER
            // =====================================================

            html.append(
                    "header{" +
                    "width:100%;" +
                    "display:flex;" +
                    "align-items:center;" +
                    "justify-content:space-between;" +
                    "padding:18px 40px;" +
                    "background:#0d0914;" +
                    "border-bottom:1px solid #30203d;" +
                    "}"
            );

            // LOGO + INVENTORY

            html.append(
                    ".logo-area{" +
                    "display:flex;" +
                    "align-items:center;" +
                    "gap:9px;" +
                    "}"
            );

            html.append(
                    ".logo-header{" +
                    "width:40px;" +
                    "height:40px;" +
                    "object-fit:contain;" +
                    "display:block;" +
                    "flex-shrink:0;" +
                    "}"
            );

            html.append(
                    ".logo-area h1{" +
                    "margin:0;" +
                    "color:#fff;" +
                    "font-size:30px;" +
                    "font-weight:bold;" +
                    "}"
            );

            // MENU

            html.append(
                    "header nav{" +
                    "display:flex;" +
                    "align-items:center;" +
                    "gap:25px;" +
                    "}"
            );

            html.append(
                    "header nav a{" +
                    "color:#b9afc5;" +
                    "text-decoration:none;" +
                    "font-size:14px;" +
                    "transition:.2s;" +
                    "}"
            );

            html.append(
                    "header nav a:hover{" +
                    "color:#c084fc;" +
                    "}"
            );

            // =====================================================
            // PÁGINA
            // =====================================================

            html.append(
                    ".perfil-page{" +
                    "max-width:1150px;" +
                    "margin:0 auto;" +
                    "padding:30px 20px 60px;" +
                    "}"
            );

            // =====================================================
            // CARD PRINCIPAL
            // =====================================================

            html.append(
                    ".perfil-card{" +
                    "background:" +
                    "linear-gradient(" +
                    "135deg,#24102f,#140b1b);" +
                    "border:1px solid #4b2464;" +
                    "border-radius:20px;" +
                    "padding:30px;" +
                    "display:flex;" +
                    "align-items:center;" +
                    "gap:25px;" +
                    "box-shadow:" +
                    "0 20px 55px rgba(0,0,0,.45);" +
                    "}"
            );

            // =====================================================
            // FOTO
            // =====================================================

            html.append(
                    ".perfil-foto{" +
                    "width:125px;" +
                    "height:125px;" +
                    "border-radius:50%;" +
                    "object-fit:cover;" +
                    "border:3px solid #7c3aed;" +
                    "background:#120a18;" +
                    "flex-shrink:0;" +
                    "box-shadow:" +
                    "0 0 25px rgba(124,58,237,.28);" +
                    "}"
            );

            html.append(
                    ".perfil-info{" +
                    "flex:1;" +
                    "min-width:0;" +
                    "}"
            );

            html.append(
                    ".perfil-nome{" +
                    "font-size:31px;" +
                    "margin:0;" +
                    "font-weight:bold;" +
                    "color:#fff;" +
                    "}"
            );

            html.append(
                    ".perfil-username{" +
                    "color:#b98be8;" +
                    "margin-top:6px;" +
                    "}"
            );

            html.append(
                    ".perfil-bio{" +
                    "color:#cfc7d6;" +
                    "margin-top:14px;" +
                    "line-height:1.5;" +
                    "}"
            );

            // =====================================================
            // LOVE BADGE
            // =====================================================

            html.append(
                    ".love-badge{" +
                    "display:inline-block;" +
                    "margin-top:12px;" +
                    "padding:8px 15px;" +
                    "background:" +
                    "linear-gradient(" +
                    "135deg,#351344,#6d2b8c,#9d4edd);" +
                    "border:1px solid #c889e8;" +
                    "border-radius:20px;" +
                    "font-size:13px;" +
                    "font-weight:bold;" +
                    "color:#ffeaff;" +
                    "box-shadow:" +
                    "0 0 15px rgba(168,85,247,.18);" +
                    "}"
            );

            // =====================================================
            // STATS
            // =====================================================

            html.append(
                    ".perfil-stats{" +
                    "display:flex;" +
                    "gap:28px;" +
                    "margin-top:20px;" +
                    "flex-wrap:wrap;" +
                    "}"
            );

            html.append(
                    ".stat-numero{" +
                    "font-size:24px;" +
                    "font-weight:bold;" +
                    "color:#fff;" +
                    "}"
            );

            html.append(
                    ".stat-texto{" +
                    "font-size:13px;" +
                    "color:#91889b;" +
                    "margin-top:3px;" +
                    "}"
            );

            // =====================================================
            // BOTÕES
            // =====================================================

            html.append(
                    ".perfil-buttons{" +
                    "display:flex;" +
                    "gap:10px;" +
                    "margin-top:18px;" +
                    "flex-wrap:wrap;" +
                    "}"
            );

            html.append(
                    ".perfil-button{" +
                    "display:inline-block;" +
                    "padding:10px 16px;" +
                    "background:" +
                    "linear-gradient(" +
                    "135deg,#6d18b8,#8b2be2);" +
                    "border:1px solid #8d39d7;" +
                    "border-radius:8px;" +
                    "color:white;" +
                    "text-decoration:none;" +
                    "font-weight:bold;" +
                    "font-size:14px;" +
                    "transition:.2s;" +
                    "}"
            );

            html.append(
                    ".perfil-button:hover{" +
                    "background:" +
                    "linear-gradient(" +
                    "135deg,#8325d3,#a855f7);" +
                    "transform:translateY(-2px);" +
                    "}"
            );

            // EXCLUIR

            html.append(
                    ".excluir-button{" +
                    "background:#29101f;" +
                    "border:1px solid #633052;" +
                    "color:#c98aa9;" +
                    "}"
            );

            html.append(
                    ".excluir-button:hover{" +
                    "background:#42162e;" +
                    "border-color:#88436d;" +
                    "color:#e1a9c2;" +
                    "}"
            );

            // =====================================================
            // GRID
            // =====================================================

            html.append(
                    ".perfil-grid{" +
                    "display:grid;" +
                    "grid-template-columns:2fr 1fr;" +
                    "gap:22px;" +
                    "margin-top:22px;" +
                    "align-items:start;" +
                    "}"
            );

            // =====================================================
            // SEÇÕES
            // =====================================================

            html.append(
                    ".secao{" +
                    "background:" +
                    "linear-gradient(" +
                    "145deg,#1d1128,#130b1a);" +
                    "border:1px solid #392348;" +
                    "border-radius:15px;" +
                    "padding:22px;" +
                    "margin-bottom:22px;" +
                    "box-shadow:" +
                    "0 10px 30px rgba(0,0,0,.25);" +
                    "}"
            );

            html.append(
                    ".secao-titulo{" +
                    "font-size:22px;" +
                    "font-weight:bold;" +
                    "margin:0 0 18px;" +
                    "color:#fff;" +
                    "}"
            );

            // =====================================================
            // JOGOS
            // =====================================================

            html.append(
                    ".jogos-grid{" +
                    "display:grid;" +
                    "grid-template-columns:" +
                    "repeat(auto-fill,minmax(145px,1fr));" +
                    "gap:16px;" +
                    "}"
            );

            html.append(
                    ".jogo-card{" +
                    "background:#120a18;" +
                    "border:1px solid #33203d;" +
                    "border-radius:11px;" +
                    "overflow:hidden;" +
                    "transition:.2s;" +
                    "}"
            );

            html.append(
                    ".jogo-card:hover{" +
                    "transform:translateY(-4px);" +
                    "border-color:#7c3aed;" +
                    "box-shadow:" +
                    "0 10px 25px rgba(124,58,237,.16);" +
                    "}"
            );

            html.append(
                    ".capa-container{" +
                    "height:215px;" +
                    "width:100%;" +
                    "overflow:hidden;" +
                    "background:#120a18;" +
                    "}"
            );

            html.append(
                    ".jogo-capa{" +
                    "width:100%;" +
                    "height:215px;" +
                    "object-fit:cover;" +
                    "display:block;" +
                    "background:#120a18;" +
                    "}"
            );

            html.append(
                    ".jogo-info{" +
                    "padding:11px;" +
                    "}"
            );

            html.append(
                    ".jogo-titulo{" +
                    "font-size:14px;" +
                    "font-weight:bold;" +
                    "line-height:1.35;" +
                    "color:#fff;" +
                    "}"
            );

            // =====================================================
            // LISTAS
            // =====================================================

            html.append(
                    ".lista-card{" +
                    "background:#120a18;" +
                    "border:1px solid #33203d;" +
                    "border-radius:11px;" +
                    "padding:16px;" +
                    "margin-bottom:14px;" +
                    "}"
            );

            html.append(
                    ".lista-nome{" +
                    "font-size:18px;" +
                    "font-weight:bold;" +
                    "margin-bottom:14px;" +
                    "color:#fff;" +
                    "}"
            );

            html.append(
                    ".lista-jogos{" +
                    "display:grid;" +
                    "grid-template-columns:" +
                    "repeat(auto-fill,minmax(90px,1fr));" +
                    "gap:10px;" +
                    "}"
            );

            html.append(
                    ".lista-capa{" +
                    "width:100%;" +
                    "height:130px;" +
                    "object-fit:cover;" +
                    "display:block;" +
                    "background:#120a18;" +
                    "border-radius:7px;" +
                    "}"
            );

            // =====================================================
            // USUÁRIOS
            // =====================================================

            html.append(
                    ".usuario-item{" +
                    "display:flex;" +
                    "align-items:center;" +
                    "gap:11px;" +
                    "padding:11px 0;" +
                    "border-bottom:1px solid #31203b;" +
                    "}"
            );

            html.append(
                    ".usuario-item:last-child{" +
                    "border-bottom:none;" +
                    "}"
            );

            html.append(
                    ".foto-mini{" +
                    "width:42px;" +
                    "height:42px;" +
                    "border-radius:50%;" +
                    "object-fit:cover;" +
                    "background:#120a18;" +
                    "flex-shrink:0;" +
                    "border:1px solid #6d28a9;" +
                    "}"
            );

            html.append(
                    ".usuario-link{" +
                    "color:#fff;" +
                    "text-decoration:none;" +
                    "font-weight:bold;" +
                    "}"
            );

            html.append(
                    ".usuario-link:hover{" +
                    "color:#c084fc;" +
                    "}"
            );

            // =====================================================
            // VAZIO
            // =====================================================

            html.append(
                    ".vazio{" +
                    "text-align:center;" +
                    "padding:22px 5px;" +
                    "color:#81768d;" +
                    "}"
            );

            // =====================================================
            // RESPONSIVO
            // =====================================================

            html.append(
                    "@media(max-width:800px){" +

                    "header{" +
                    "padding:15px 20px;" +
                    "flex-direction:column;" +
                    "gap:15px;" +
                    "}" +

                    "header nav{" +
                    "gap:15px;" +
                    "flex-wrap:wrap;" +
                    "justify-content:center;" +
                    "}" +

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

                    ".logo-header{" +
                    "width:35px;" +
                    "height:35px;" +
                    "}" +

                    ".logo-area h1{" +
                    "font-size:26px;" +
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

            html.append(
                    "<div class='logo-area'>"
            );

            html.append(
                    "<img " +
                    "src='icon.png' " +
                    "class='logo-header' " +
                    "alt='Logo Inventory'>"
            );

            html.append(
                    "<h1>Inventory</h1>"
            );

            html.append("</div>");

            html.append("<nav>");

            html.append(
                    "<a href='index.html'>Início</a>"
            );

            html.append(
                    "<a href='buscar-usuarios'>Buscar usuários</a>"
            );

            html.append(
                    "<a href='jogos'>Jogos</a>"
            );

            html.append(
                    "<a href='perfil'>Meu Perfil</a>"
            );

            html.append(
                    "<a href='biblioteca'>Biblioteca</a>"
            );

            html.append(
                    "<a href='listas'>Listas</a>"
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
            // CARD DO PERFIL
            // =====================================================

            html.append(
                    "<section class='perfil-card'>"
            );

            String foto =
                    prepararFoto(
                            usuario.getFoto(),
                            request
                    );

            if (!foto.isEmpty()) {

                html.append(
                        "<img " +
                        "class='perfil-foto' " +
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
            // ESTATÍSTICAS
            // =====================================================

            html.append(
                    "<div class='perfil-stats'>"
            );

            html.append(
                    "<div>" +
                    "<div class='stat-numero'>" +
                    totalSeguidores +
                    "</div>" +
                    "<div class='stat-texto'>Seguidores</div>" +
                    "</div>"
            );

            html.append(
                    "<div>" +
                    "<div class='stat-numero'>" +
                    totalSeguindo +
                    "</div>" +
                    "<div class='stat-texto'>Seguindo</div>" +
                    "</div>"
            );

            html.append(
                    "<div>" +
                    "<div class='stat-numero'>" +
                    favoritos.size() +
                    "</div>" +
                    "<div class='stat-texto'>Favoritos</div>" +
                    "</div>"
            );

            html.append(
                    "<div>" +
                    "<div class='stat-numero'>" +
                    listas.size() +
                    "</div>" +
                    "<div class='stat-texto'>Listas</div>" +
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

            html.append(
                    "<a class='perfil-button excluir-button' " +
                    "href='excluir-conta' " +
                    "onclick=\"return confirm(" +
                    "'Tem certeza que deseja excluir sua conta? " +
                    "Esta ação não pode ser desfeita.'" +
                    ");\">" +
                    "Excluir conta" +
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

                            if (!capa.isEmpty()) {

                                html.append(
                                        "<img " +
                                        "class='lista-capa' " +
                                        "src='" +
                                        escaparHtml(capa) +
                                        "' " +
                                        "alt='Capa de " +
                                        escaparHtml(
                                                jogo.titulo
                                        ) +
                                        "'>"
                                );
                            }
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

        html.append(
                "<div class='jogo-card'>"
        );

        html.append(
                "<div class='capa-container'>"
        );

        if (!capa.isEmpty()) {

            html.append(
                    "<img " +
                    "class='jogo-capa' " +
                    "src='" +
                    escaparHtml(capa) +
                    "' " +
                    "alt='Capa de " +
                    escaparHtml(jogo.titulo) +
                    "'>"
            );

        } else {

            html.append(
                    "<div style='height:215px;" +
                    "display:flex;" +
                    "align-items:center;" +
                    "justify-content:center;" +
                    "color:#777;'>" +
                    "Sem capa" +
                    "</div>"
            );
        }

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

            return "";
        }

        capa =
                capa.trim();

        // Markdown [nome](url)

        if (capa.startsWith("[") &&
                capa.contains("](") &&
                capa.endsWith(")")) {

            int inicio =
                    capa.indexOf("](") + 2;

            int fim =
                    capa.lastIndexOf(")");

            if (fim > inicio) {

                capa =
                        capa.substring(
                                inicio,
                                fim
                        );
            }
        }

        // Apenas ID Steam

        if (capa.matches("\\d+")) {

            return
                    "https://cdn.akamai.steamstatic.com/" +
                    "steam/apps/" +
                    capa +
                    "/library_600x900_2x.jpg";
        }

        // URL Steam com /apps/ID

        java.util.regex.Matcher matcher =
                java.util.regex.Pattern
                        .compile("/apps/(\\d+)")
                        .matcher(capa);

        if (matcher.find()) {

            return
                    "https://cdn.akamai.steamstatic.com/" +
                    "steam/apps/" +
                    matcher.group(1) +
                    "/library_600x900_2x.jpg";
        }

        // URL normal

        if (capa.startsWith("http://") ||
                capa.startsWith("https://")) {

            return capa;
        }

        // Caminho absoluto

        if (capa.startsWith("/")) {

            return
                    request.getContextPath()
                    + capa;
        }

        // Caminho relativo

        return
                request.getContextPath()
                + "/"
                + capa;
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