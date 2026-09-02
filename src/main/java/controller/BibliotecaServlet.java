package controller;

import dao.Conexao;
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

@WebServlet("/biblioteca")
public class BibliotecaServlet extends HttpServlet {

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

            Usuario usuario =
                    (Usuario) sessao.getAttribute("usuario");

            int idUsuario =
                    usuario.getId();

            List<Jogo> jogando =
                    carregarJogos(
                            idUsuario,
                            "jogando"
                    );

            List<Jogo> zerados =
                    carregarJogos(
                            idUsuario,
                            "zerado"
                    );

            List<Jogo> queroJogar =
                    carregarJogos(
                            idUsuario,
                            "quero jogar"
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

            html.append(
                    "<meta name='viewport' " +
                    "content='width=device-width, initial-scale=1.0'>"
            );

            html.append(
                    "<title>Biblioteca - GameBoxd</title>"
            );

            html.append(
                    "<link rel='stylesheet' " +
                    "href='style.css'>"
            );

            html.append("<style>");

            html.append(
                    "body{" +
                    "margin:0;" +
                    "background:#14181c;" +
                    "color:#fff;" +
                    "font-family:Arial,Helvetica,sans-serif;" +
                    "}"
            );

            html.append(
                    ".library-page{" +
                    "max-width:1200px;" +
                    "margin:auto;" +
                    "padding:30px 20px 60px;" +
                    "}"
            );

            html.append(
                    ".library-hero{" +
                    "background:linear-gradient(135deg,#251436,#202830);" +
                    "border:1px solid #3a2946;" +
                    "border-radius:18px;" +
                    "padding:30px;" +
                    "margin-bottom:25px;" +
                    "}"
            );

            html.append(
                    ".library-hero h2{" +
                    "font-size:32px;" +
                    "margin:0 0 8px;" +
                    "}"
            );

            html.append(
                    ".library-hero p{" +
                    "margin:0;" +
                    "color:#9299a2;" +
                    "}"
            );

            html.append(
                    ".section{" +
                    "background:#202830;" +
                    "border:1px solid #2f373e;" +
                    "border-radius:15px;" +
                    "padding:24px;" +
                    "margin-bottom:25px;" +
                    "}"
            );

            html.append(
                    ".section-header{" +
                    "display:flex;" +
                    "justify-content:space-between;" +
                    "align-items:center;" +
                    "margin-bottom:20px;" +
                    "}"
            );

            html.append(
                    ".section-title{" +
                    "margin:0;" +
                    "font-size:23px;" +
                    "}"
            );

            html.append(
                    ".count{" +
                    "background:#171b20;" +
                    "border:1px solid #343c43;" +
                    "border-radius:20px;" +
                    "padding:6px 11px;" +
                    "font-size:13px;" +
                    "color:#a3aab1;" +
                    "}"
            );

            html.append(
                    ".games-grid{" +
                    "display:grid;" +
                    "grid-template-columns:" +
                    "repeat(auto-fill,minmax(165px,1fr));" +
                    "gap:18px;" +
                    "}"
            );

            html.append(
                    ".game-card{" +
                    "background:#171b20;" +
                    "border:1px solid #2f363d;" +
                    "border-radius:11px;" +
                    "overflow:hidden;" +
                    "transition:.2s;" +
                    "}"
            );

            html.append(
                    ".game-card:hover{" +
                    "transform:translateY(-4px);" +
                    "border-color:#7300d1;" +
                    "}"
            );

            html.append(
                    ".cover-area{" +
                    "height:245px;" +
                    "background:#292f35;" +
                    "}"
            );

            html.append(
                    ".game-cover{" +
                    "width:100%;" +
                    "height:245px;" +
                    "object-fit:cover;" +
                    "display:block;" +
                    "}"
            );

            html.append(
                    ".game-info{" +
                    "padding:12px;" +
                    "}"
            );

            html.append(
                    ".game-title{" +
                    "font-size:14px;" +
                    "font-weight:bold;" +
                    "line-height:1.35;" +
                    "min-height:38px;" +
                    "}"
            );

            html.append(
                    ".rate-btn{" +
                    "display:block;" +
                    "background:#6300c0;" +
                    "color:white;" +
                    "text-align:center;" +
                    "padding:9px;" +
                    "margin-top:10px;" +
                    "border-radius:7px;" +
                    "text-decoration:none;" +
                    "font-size:13px;" +
                    "font-weight:bold;" +
                    "}"
            );

            html.append(
                    ".rate-btn:hover{" +
                    "background:#8300ed;" +
                    "}"
            );

            html.append(
                    ".empty{" +
                    "text-align:center;" +
                    "padding:30px;" +
                    "color:#7f8790;" +
                    "}"
            );

            html.append(
                    "@media(max-width:600px){" +
                    ".games-grid{" +
                    "grid-template-columns:repeat(2,1fr);" +
                    "gap:12px;" +
                    "}" +
                    ".cover-area,.game-cover{" +
                    "height:210px;" +
                    "}" +
                    "}"
            );

            html.append("</style>");

            html.append("</head>");
            html.append("<body>");

            // HEADER

            html.append("<header>");
            html.append("<h1>GameBoxd</h1>");
            html.append("<nav>");

            html.append("<a href='index.html'>Início</a>");
            html.append("<a href='jogos'>Jogos</a>");
            html.append("<a href='biblioteca'>Biblioteca</a>");
            html.append("<a href='buscar-usuarios.html'>Buscar usuários</a>");
            html.append("<a href='listas'>Listas</a>");
            html.append("<a href='perfil'>Meu Perfil</a>");
            html.append("<a href='logout'>Sair</a>");

            html.append("</nav>");
            html.append("</header>");

            html.append(
                    "<main class='library-page'>"
            );

            html.append(
                    "<section class='library-hero'>"
            );

            html.append(
                    "<h2>Minha Biblioteca</h2>"
            );

            html.append(
                    "<p>" +
                    "Organize todos os seus jogos no GameBoxd." +
                    "</p>"
            );

            html.append("</section>");

            // JOGANDO

            html.append(
                    "<section class='section'>"
            );

            html.append(
                    "<div class='section-header'>" +
                    "<h2 class='section-title'>🎮 Jogando</h2>" +
                    "<span class='count'>" +
                    jogando.size() +
                    "</span>" +
                    "</div>"
            );

            if (jogando.isEmpty()) {

                html.append(
                        "<div class='empty'>" +
                        "Nenhum jogo sendo jogado." +
                        "</div>"
                );

            } else {

                html.append(
                        "<div class='games-grid'>"
                );

                for (Jogo jogo :
                        jogando) {

                    html.append(
                            montarCard(
                                    jogo,
                                    request
                            )
                    );
                }

                html.append("</div>");
            }

            html.append("</section>");

            // ZERADOS

            html.append(
                    "<section class='section'>"
            );

            html.append(
                    "<div class='section-header'>" +
                    "<h2 class='section-title'>✅ Zerados</h2>" +
                    "<span class='count'>" +
                    zerados.size() +
                    "</span>" +
                    "</div>"
            );

            if (zerados.isEmpty()) {

                html.append(
                        "<div class='empty'>" +
                        "Nenhum jogo zerado ainda." +
                        "</div>"
                );

            } else {

                html.append(
                        "<div class='games-grid'>"
                );

                for (Jogo jogo :
                        zerados) {

                    html.append(
                            montarCard(
                                    jogo,
                                    request
                            )
                    );
                }

                html.append("</div>");
            }

            html.append("</section>");

            // QUERO JOGAR

            html.append(
                    "<section class='section'>"
            );

            html.append(
                    "<div class='section-header'>" +
                    "<h2 class='section-title'>🎯 Quero jogar</h2>" +
                    "<span class='count'>" +
                    queroJogar.size() +
                    "</span>" +
                    "</div>"
            );

            if (queroJogar.isEmpty()) {

                html.append(
                        "<div class='empty'>" +
                        "Nenhum jogo na sua lista." +
                        "</div>"
                );

            } else {

                html.append(
                        "<div class='games-grid'>"
                );

                for (Jogo jogo :
                        queroJogar) {

                    html.append(
                            montarCard(
                                    jogo,
                                    request
                            )
                    );
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

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect("index.html");
        }
    }

    // =====================================================
    // BUSCAR JOGOS
    // =====================================================

    private List<Jogo> carregarJogos(
            int idUsuario,
            String status)
            throws Exception {

        List<Jogo> jogos =
                new ArrayList<>();

        Connection conexao =
                Conexao.conectar();

        PreparedStatement stmt =
                conexao.prepareStatement(
                        "SELECT j.id,j.titulo,j.capa,b.status " +
                        "FROM biblioteca b " +
                        "INNER JOIN jogo j " +
                        "ON j.id=b.id_jogo " +
                        "WHERE b.id_usuario=? " +
                        "AND b.status=? " +
                        "ORDER BY b.id DESC"
                );

        stmt.setInt(1, idUsuario);
        stmt.setString(2, status);

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

            jogo.status =
                    rs.getString("status");

            jogos.add(jogo);
        }

        rs.close();
        stmt.close();
        conexao.close();

        return jogos;
    }

    // =====================================================
    // CARD
    // =====================================================

    private String montarCard(
            Jogo jogo,
            HttpServletRequest request) {

        String capa =
                prepararCapa(
                        jogo.capa,
                        request
                );

        StringBuilder html =
                new StringBuilder();

        html.append(
                "<div class='game-card'>"
        );

        html.append(
                "<div class='cover-area'>"
        );

        if (!capa.isEmpty()) {

            html.append(
                    "<img class='game-cover' " +
                    "src='" +
                    escaparHtml(capa) +
                    "' " +
                    "alt='Capa do jogo'>"
            );

        } else {

            html.append(
                    "<div style='height:100%;" +
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
                "<div class='game-info'>"
        );

        html.append(
                "<div class='game-title'>" +
                escaparHtml(jogo.titulo) +
                "</div>"
        );

        html.append(
                "<a class='rate-btn' " +
                "href='avaliar?id=" +
                jogo.id +
                "'>" +
                "Avaliar jogo" +
                "</a>"
        );

        html.append("</div>");
        html.append("</div>");

        return html.toString();
    }

    // =====================================================
    // CAPA
    // =====================================================

    private String prepararCapa(
        String caminho,
        HttpServletRequest request) {

    if (caminho == null ||
            caminho.trim().isEmpty()) {

        return "";
    }

    caminho = caminho.trim();

    // URL já pronta
    if (caminho.startsWith("http://") ||
            caminho.startsWith("https://")) {

        return caminho;
    }

    // Se o banco tiver somente o App ID
    if (caminho.matches("\\d+")) {

        return
                "https://shared.fastly.steamstatic.com/" +
                "store_item_assets/steam/apps/" +
                caminho +
                "/library_600x900.jpg";
    }

    while (caminho.startsWith("/")) {
        caminho = caminho.substring(1);
    }

    return
            request.getContextPath()
            + "/"
            + caminho;
}

    // =====================================================
    // ESCAPAR
    // =====================================================

    private String escaparHtml(
            String texto) {

        if (texto == null) {
            return "";
        }

        return texto
                .replace("&","&amp;")
                .replace("<","&lt;")
                .replace(">","&gt;")
                .replace("\"","&quot;")
                .replace("'","&#39;");
    }

    // =====================================================
    // CLASSE
    // =====================================================

    private static class Jogo {

        int id;
        String titulo;
        String capa;
        String status;
    }
}