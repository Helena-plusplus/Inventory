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
                            "quero_jogar"
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
                    "<title>Minha Biblioteca - GameBoxd</title>"
            );

            html.append(
                    "<link rel='stylesheet' href='style.css'>"
            );

            html.append("<style>");

            html.append(
                    ".biblioteca-container {" +
                    "max-width:1100px;" +
                    "margin:40px auto;" +
                    "padding:20px;" +
                    "}"
            );

            html.append(
                    ".biblioteca-titulo {" +
                    "text-align:center;" +
                    "margin-bottom:35px;" +
                    "}"
            );

            html.append(
                    ".biblioteca-secao {" +
                    "background:#202830;" +
                    "padding:25px;" +
                    "border-radius:14px;" +
                    "margin-bottom:30px;" +
                    "}"
            );

            html.append(
                    ".biblioteca-secao h2 {" +
                    "margin-top:0;" +
                    "margin-bottom:20px;" +
                    "}"
            );

            html.append(
                    ".jogos-grid {" +
                    "display:grid;" +
                    "grid-template-columns:" +
                    "repeat(auto-fill,minmax(160px,1fr));" +
                    "gap:20px;" +
                    "}"
            );

            html.append(
                    ".jogo-card {" +
                    "background:#14181c;" +
                    "padding:12px;" +
                    "border-radius:10px;" +
                    "text-align:center;" +
                    "transition:0.2s;" +
                    "}"
            );

            html.append(
                    ".jogo-card:hover {" +
                    "transform:translateY(-3px);" +
                    "}"
            );

            html.append(
                    ".jogo-capa {" +
                    "width:100%;" +
                    "height:220px;" +
                    "object-fit:cover;" +
                    "border-radius:8px;" +
                    "display:block;" +
                    "}"
            );

            html.append(
                    ".sem-capa {" +
                    "width:100%;" +
                    "height:220px;" +
                    "background:#2a3036;" +
                    "border-radius:8px;" +
                    "display:flex;" +
                    "align-items:center;" +
                    "justify-content:center;" +
                    "color:#777;" +
                    "}"
            );

            html.append(
                    ".nome-jogo {" +
                    "margin-top:12px;" +
                    "font-weight:bold;" +
                    "color:white;" +
                    "}"
            );

            html.append(
                    ".acoes-jogo {" +
                    "display:flex;" +
                    "justify-content:center;" +
                    "gap:8px;" +
                    "margin-top:12px;" +
                    "flex-wrap:wrap;" +
                    "}"
            );

            html.append(
                    ".botao {" +
                    "display:inline-block;" +
                    "padding:8px 12px;" +
                    "background:#6300c0;" +
                    "color:white;" +
                    "text-decoration:none;" +
                    "border-radius:6px;" +
                    "font-size:13px;" +
                    "font-weight:bold;" +
                    "}"
            );

            html.append(
                    ".botao:hover {" +
                    "background:#7d00ef;" +
                    "}"
            );

            html.append(
                    ".vazio {" +
                    "text-align:center;" +
                    "color:#999;" +
                    "padding:20px;" +
                    "}"
            );

            html.append("</style>");

            html.append("</head>");

            html.append("<body>");

            // =========================================
            // HEADER
            // =========================================

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
                    "<a href='buscar-usuarios.html'>"
                    + "Buscar usuários"
                    + "</a>"
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

            // =========================================
            // CONTEÚDO
            // =========================================

            html.append(
                    "<main class='biblioteca-container'>"
            );

            html.append(
                    "<div class='biblioteca-titulo'>"
            );

            html.append(
                    "<h2>Minha Biblioteca</h2>"
            );

            html.append(
                    "<p>Organize seus jogos no GameBoxd.</p>"
            );

            html.append("</div>");

            // =========================================
            // JOGANDO
            // =========================================

            html.append(
                    "<section class='biblioteca-secao'>"
            );

            html.append(
                    "<h2>🎮 Jogando</h2>"
            );

            if (jogando.isEmpty()) {

                html.append(
                        "<div class='vazio'>"
                        + "Nenhum jogo sendo jogado."
                        + "</div>"
                );

            } else {

                html.append(
                        "<div class='jogos-grid'>"
                );

                for (Jogo jogo :
                        jogando) {

                    html.append(
                            montarJogo(
                                    jogo,
                                    request
                            )
                    );
                }

                html.append("</div>");
            }

            html.append("</section>");

            // =========================================
            // ZERADOS
            // =========================================

            html.append(
                    "<section class='biblioteca-secao'>"
            );

            html.append(
                    "<h2>✅ Zerados</h2>"
            );

            if (zerados.isEmpty()) {

                html.append(
                        "<div class='vazio'>"
                        + "Nenhum jogo zerado ainda."
                        + "</div>"
                );

            } else {

                html.append(
                        "<div class='jogos-grid'>"
                );

                for (Jogo jogo :
                        zerados) {

                    html.append(
                            montarJogo(
                                    jogo,
                                    request
                            )
                    );
                }

                html.append("</div>");
            }

            html.append("</section>");

            // =========================================
            // QUERO JOGAR
            // =========================================

            html.append(
                    "<section class='biblioteca-secao'>"
            );

            html.append(
                    "<h2>🎯 Quero jogar</h2>"
            );

            if (queroJogar.isEmpty()) {

                html.append(
                        "<div class='vazio'>"
                        + "Nenhum jogo na sua lista de desejos."
                        + "</div>"
                );

            } else {

                html.append(
                        "<div class='jogos-grid'>"
                );

                for (Jogo jogo :
                        queroJogar) {

                    html.append(
                            montarJogo(
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
    // CARREGAR JOGOS
    // =====================================================

    private List<Jogo> carregarJogos(
            int idUsuario,
            String status)
            throws Exception {

        List<Jogo> jogos =
                new ArrayList<>();

        Connection conexao =
                Conexao.conectar();

        String sql =
                "SELECT j.id, " +
                "j.titulo, " +
                "j.capa, " +
                "b.status " +
                "FROM biblioteca b " +
                "INNER JOIN jogo j " +
                "ON j.id = b.id_jogo " +
                "WHERE b.id_usuario = ? " +
                "AND b.status = ? " +
                "ORDER BY b.id DESC";

        PreparedStatement stmt =
                conexao.prepareStatement(sql);

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
    // MONTAR CARD
    // =====================================================

    private String montarJogo(
            Jogo jogo,
            HttpServletRequest request) {

        StringBuilder html =
                new StringBuilder();

        html.append(
                "<div class='jogo-card'>"
        );

        String capa =
                prepararCapa(
                        jogo.capa,
                        request
                );

        if (capa != null &&
                !capa.isEmpty()) {

            html.append(
                    "<img " +
                    "class='jogo-capa' " +
                    "src='" +
                    escaparHtml(capa) +
                    "' " +
                    "alt='Capa de " +
                    escaparHtml(jogo.titulo) +
                    "' " +
                    "onerror=\"this.style.display='none';" +
                    "this.nextElementSibling.style.display='flex';\">"
            );

            html.append(
                    "<div class='sem-capa' " +
                    "style='display:none;'>"
                    + "Sem capa"
                    + "</div>"
            );

        } else {

            html.append(
                    "<div class='sem-capa'>"
                    + "Sem capa"
                    + "</div>"
            );
        }

        html.append(
                "<div class='nome-jogo'>"
                +
                escaparHtml(
                        jogo.titulo
                )
                +
                "</div>"
        );

        html.append(
                "<div class='acoes-jogo'>"
        );

        // Botão avaliar
        html.append(
                "<a class='botao' " +
                "href='avaliar?id=" +
                jogo.id +
                "'>" +
                "Avaliar" +
                "</a>"
        );

        html.append("</div>");

        html.append("</div>");

        return html.toString();
    }

    // =====================================================
    // PREPARAR CAPA
    // =====================================================

    private String prepararCapa(
            String caminho,
            HttpServletRequest request) {

        if (caminho == null ||
                caminho.trim().isEmpty()) {

            return "";
        }

        caminho =
                caminho.trim();

        // =========================================
        // URL COMPLETA
        // NÃO ALTERAR
        // =========================================

        if (caminho.startsWith("http://") ||
                caminho.startsWith("https://")) {

            return caminho;
        }

        // =========================================
        // MARKDOWN
        // =========================================

        if (caminho.startsWith("[") &&
                caminho.contains("](") &&
                caminho.endsWith(")")) {

            int inicio =
                    caminho.indexOf("](") + 2;

            int fim =
                    caminho.lastIndexOf(")");

            if (inicio < fim) {

                String url =
                        caminho.substring(
                                inicio,
                                fim
                        );

                if (url.startsWith("http://") ||
                        url.startsWith("https://")) {

                    return url;
                }
            }
        }

        // =========================================
        // SOMENTE ID
        // =========================================

        if (caminho.matches("\\d+")) {

            return
                    "https://cdn.akamai.steamstatic.com/" +
                    "steam/apps/" +
                    caminho +
                    "/library_600x900_2x.jpg";
        }

        // =========================================
        // CAMINHO LOCAL
        // =========================================

        while (caminho.startsWith("/")) {

            caminho =
                    caminho.substring(1);
        }

        return
                request.getContextPath()
                + "/"
                + caminho;
    }

    // =====================================================
    // ESCAPAR HTML
    // =====================================================

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

    // =====================================================
    // CLASSE JOGO
    // =====================================================

    private static class Jogo {

        int id;

        String titulo;

        String capa;

        String status;
    }
}