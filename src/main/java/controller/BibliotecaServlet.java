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

            html.append(
                    "<title>Biblioteca - GameBoxd</title>"
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
                    "* {" +
                    "box-sizing:border-box;" +
                    "}"
            );

            html.append(
                    "body {" +
                    "margin:0;" +
                    "background:#14101b;" +
                    "color:#ffffff;" +
                    "font-family:Arial,Helvetica,sans-serif;" +
                    "}"
            );

            html.append(
                    ".biblioteca-page {" +
                    "max-width:1200px;" +
                    "margin:0 auto;" +
                    "padding:30px 20px 60px;" +
                    "}"
            );

            // =====================================================
            // TOPO
            // =====================================================

            html.append(
                    ".biblioteca-topo {" +
                    "background:linear-gradient(135deg,#24102f,#1b1820);" +
                    "border:1px solid #40244f;" +
                    "border-radius:18px;" +
                    "padding:28px;" +
                    "margin-bottom:25px;" +
                    "}"
            );

            html.append(
                    ".biblioteca-topo h2 {" +
                    "margin:0 0 8px;" +
                    "font-size:32px;" +
                    "}"
            );

            html.append(
                    ".biblioteca-topo p {" +
                    "margin:0;" +
                    "color:#98919f;" +
                    "}"
            );

            // =====================================================
            // SEÇÃO
            // =====================================================

            html.append(
                    ".biblioteca-secao {" +
                    "background:#202830;" +
                    "border:1px solid #303942;" +
                    "border-radius:15px;" +
                    "padding:24px;" +
                    "margin-bottom:25px;" +
                    "}"
            );

            html.append(
                    ".secao-header {" +
                    "display:flex;" +
                    "justify-content:space-between;" +
                    "align-items:center;" +
                    "margin-bottom:20px;" +
                    "}"
            );

            html.append(
                    ".secao-titulo {" +
                    "margin:0;" +
                    "font-size:23px;" +
                    "}"
            );

            html.append(
                    ".contador {" +
                    "background:#171b20;" +
                    "border:1px solid #363e46;" +
                    "padding:6px 11px;" +
                    "border-radius:20px;" +
                    "font-size:13px;" +
                    "color:#aaa;" +
                    "}"
            );

            // =====================================================
            // GRID
            // =====================================================

            html.append(
                    ".jogos-grid {" +
                    "display:grid;" +
                    "grid-template-columns:repeat(auto-fill,minmax(165px,1fr));" +
                    "gap:18px;" +
                    "}"
            );

            // =====================================================
            // CARD
            // =====================================================

            html.append(
                    ".jogo-card {" +
                    "background:transparent;" +
                    "border:1px solid #303840;" +
                    "border-radius:11px;" +
                    "overflow:hidden;" +
                    "transition:0.2s;" +
                    "}"
            );

            html.append(
                    ".jogo-card:hover {" +
                    "transform:translateY(-4px);" +
                    "border-color:#7300d1;" +
                    "}"
            );

            // =====================================================
            // CAPA
            // =====================================================

            html.append(
                    ".capa-container {" +
                    "width:100%;" +
                    "height:245px;" +
                    "overflow:hidden;" +
                    "background:transparent;" +
                    "}"
            );

            html.append(
                    ".jogo-capa {" +
                    "width:100%;" +
                    "height:245px;" +
                    "object-fit:cover;" +
                    "display:block;" +
                    "background:transparent;" +
                    "}"
            );

            // =====================================================
            // INFO
            // =====================================================

            html.append(
                    ".jogo-info {" +
                    "padding:12px;" +
                    "background:#171b20;" +
                    "}"
            );

            html.append(
                    ".jogo-titulo {" +
                    "font-size:14px;" +
                    "font-weight:bold;" +
                    "line-height:1.35;" +
                    "min-height:38px;" +
                    "}"
            );

            // =====================================================
            // BOTÃO
            // =====================================================

            html.append(
                    ".botao-avaliar {" +
                    "display:block;" +
                    "margin-top:11px;" +
                    "padding:9px;" +
                    "background:#6500c7;" +
                    "color:#ffffff;" +
                    "text-decoration:none;" +
                    "text-align:center;" +
                    "border-radius:7px;" +
                    "font-size:13px;" +
                    "font-weight:bold;" +
                    "}"
            );

            html.append(
                    ".botao-avaliar:hover {" +
                    "background:#8300ed;" +
                    "}"
            );

            // =====================================================
            // VAZIO
            // =====================================================

            html.append(
                    ".vazio {" +
                    "text-align:center;" +
                    "color:#7f8790;" +
                    "padding:30px;" +
                    "}"
            );

            // =====================================================
            // RESPONSIVO
            // =====================================================

            html.append(
                    "@media(max-width:600px) {" +

                    ".biblioteca-page {" +
                    "padding:20px 12px 40px;" +
                    "}" +

                    ".biblioteca-topo h2 {" +
                    "font-size:26px;" +
                    "}" +

                    ".jogos-grid {" +
                    "grid-template-columns:repeat(2,1fr);" +
                    "gap:12px;" +
                    "}" +

                    ".capa-container," +
                    ".jogo-capa {" +
                    "height:210px;" +
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

            html.append("<h1>GameBoxd</h1>");

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
                    "<a href='buscar-usuarios.html'>" +
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
                    "<main class='biblioteca-page'>"
            );

            html.append(
                    "<section class='biblioteca-topo'>"
            );

            html.append(
                    "<h2>Minha Biblioteca</h2>"
            );

            html.append(
                    "<p>" +
                    "Seus jogos organizados por status." +
                    "</p>"
            );

            html.append("</section>");

            // =====================================================
            // JOGANDO
            // =====================================================

            html.append(
                    "<section class='biblioteca-secao'>"
            );

            html.append(
                    "<div class='secao-header'>"
            );

            html.append(
                    "<h2 class='secao-titulo'>" +
                    "🎮 Jogando" +
                    "</h2>"
            );

            html.append(
                    "<span class='contador'>" +
                    jogando.size() +
                    "</span>"
            );

            html.append("</div>");

            if (jogando.isEmpty()) {

                html.append(
                        "<div class='vazio'>" +
                        "Nenhum jogo sendo jogado." +
                        "</div>"
                );

            } else {

                html.append(
                        "<div class='jogos-grid'>"
                );

                for (Jogo jogo :
                        jogando) {

                    html.append(
                            montarCard(
                                    jogo
                            )
                    );
                }

                html.append("</div>");
            }

            html.append("</section>");

            // =====================================================
            // ZERADOS
            // =====================================================

            html.append(
                    "<section class='biblioteca-secao'>"
            );

            html.append(
                    "<div class='secao-header'>"
            );

            html.append(
                    "<h2 class='secao-titulo'>" +
                    "✅ Zerados" +
                    "</h2>"
            );

            html.append(
                    "<span class='contador'>" +
                    zerados.size() +
                    "</span>"
            );

            html.append("</div>");

            if (zerados.isEmpty()) {

                html.append(
                        "<div class='vazio'>" +
                        "Nenhum jogo zerado ainda." +
                        "</div>"
                );

            } else {

                html.append(
                        "<div class='jogos-grid'>"
                );

                for (Jogo jogo :
                        zerados) {

                    html.append(
                            montarCard(
                                    jogo
                            )
                    );
                }

                html.append("</div>");
            }

            html.append("</section>");

            // =====================================================
            // QUERO JOGAR
            // =====================================================

            html.append(
                    "<section class='biblioteca-secao'>"
            );

            html.append(
                    "<div class='secao-header'>"
            );

            html.append(
                    "<h2 class='secao-titulo'>" +
                    "🎯 Quero jogar" +
                    "</h2>"
            );

            html.append(
                    "<span class='contador'>" +
                    queroJogar.size() +
                    "</span>"
            );

            html.append("</div>");

            if (queroJogar.isEmpty()) {

                html.append(
                        "<div class='vazio'>" +
                        "Nenhum jogo na sua lista." +
                        "</div>"
                );

            } else {

                html.append(
                        "<div class='jogos-grid'>"
                );

                for (Jogo jogo :
                        queroJogar) {

                    html.append(
                            montarCard(
                                    jogo
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

    // =========================================================
    // CARREGAR JOGOS
    // =========================================================

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
                        "SELECT " +
                        "j.id, " +
                        "j.titulo, " +
                        "j.capa, " +
                        "b.status " +
                        "FROM biblioteca b " +
                        "INNER JOIN jogo j " +
                        "ON j.id = b.id_jogo " +
                        "WHERE b.id_usuario = ? " +
                        "AND b.status = ? " +
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

    // =========================================================
    // CARD
    // =========================================================

    private String montarCard(
            Jogo jogo) {

        StringBuilder html =
                new StringBuilder();

        // Usa exatamente o valor salvo no banco
        String capa =
                jogo.capa;

        html.append(
                "<div class='jogo-card'>"
        );

        html.append(
                "<div class='capa-container'>"
        );

        if (capa != null &&
                !capa.trim().isEmpty()) {

            html.append(
                    "<img " +
                    "class='jogo-capa' " +
                    "src='" +
                    escaparHtml(capa.trim()) +
                    "' " +
                    "alt='Capa de " +
                    escaparHtml(jogo.titulo) +
                    "'>"
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

        html.append(
                "<a class='botao-avaliar' " +
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
    // CLASSE JOGO
    // =========================================================

    private static class Jogo {

        int id;

        String titulo;

        String capa;

        String status;
    }
}