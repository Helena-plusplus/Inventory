
package controller;

import dao.Conexao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/jogos")
public class JogosServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

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
                "<title>Jogos - Inventory</title>"
        );

        html.append(
                "<link rel='stylesheet' href='style.css'>"
        );

        // =====================================================
        // CSS DA PÁGINA
        // =====================================================

        html.append("<style>");

        html.append(
                "body {"
                + "background:linear-gradient(135deg,#0d0714,#160b24,#0d0714);"
                + "}"
        );

        html.append(
                ".jogos-container {"
                + "max-width:1200px;"
                + "margin:45px auto;"
                + "padding:20px;"
                + "}"
        );

        html.append(
                ".titulo-jogos {"
                + "text-align:center;"
                + "margin-bottom:10px;"
                + "font-size:38px;"
                + "font-weight:bold;"
                + "background:linear-gradient(90deg,#a855f7,#7c3aed,#c084fc);"
                + "-webkit-background-clip:text;"
                + "-webkit-text-fill-color:transparent;"
                + "}"
        );

        html.append(
                ".subtitulo-jogos {"
                + "text-align:center;"
                + "color:#aaa;"
                + "font-size:16px;"
                + "margin-bottom:40px;"
                + "}"
        );

        // =====================================================
        // GRID
        // =====================================================

        html.append(
                ".catalogo-jogos {"
                + "display:grid;"
                + "grid-template-columns:"
                + "repeat(auto-fill,minmax(210px,1fr));"
                + "gap:28px;"
                + "}"
        );

        // =====================================================
        // CARD
        // =====================================================

        html.append(
                ".card-jogo {"
                + "position:relative;"
                + "background:linear-gradient(145deg,#21152d,#17101f);"
                + "border:1px solid #38204d;"
                + "padding:12px;"
                + "border-radius:16px;"
                + "text-align:center;"
                + "overflow:hidden;"
                + "transition:all .3s ease;"
                + "box-shadow:0 8px 25px rgba(0,0,0,.35);"
                + "}"
        );

        html.append(
                ".card-jogo:hover {"
                + "transform:translateY(-8px);"
                + "border-color:#8b5cf6;"
                + "box-shadow:0 15px 35px rgba(124,58,237,.35);"
                + "}"
        );

        // =====================================================
        // CAPA
        // =====================================================

        html.append(
                ".capa-jogo {"
                + "width:100%;"
                + "height:285px;"
                + "object-fit:cover;"
                + "border-radius:12px;"
                + "display:block;"
                + "transition:transform .3s ease;"
                + "}"
        );

        html.append(
                ".card-jogo:hover .capa-jogo {"
                + "transform:scale(1.03);"
                + "}"
        );

        html.append(
                ".sem-capa {"
                + "width:100%;"
                + "height:285px;"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:center;"
                + "background:#120d18;"
                + "border-radius:12px;"
                + "color:#777;"
                + "}"
        );

        // =====================================================
        // TÍTULO
        // =====================================================

        html.append(
                ".card-jogo h3 {"
                + "font-size:18px;"
                + "margin:15px 5px 8px;"
                + "color:#fff;"
                + "min-height:44px;"
                + "}"
        );

        // =====================================================
        // INFORMAÇÕES
        // =====================================================

        html.append(
                ".info-jogo {"
                + "display:flex;"
                + "justify-content:center;"
                + "flex-wrap:wrap;"
                + "gap:7px;"
                + "margin-bottom:15px;"
                + "}"
        );

        html.append(
                ".tag-jogo {"
                + "background:#2d183e;"
                + "border:1px solid #4c2670;"
                + "color:#c084fc;"
                + "padding:5px 9px;"
                + "border-radius:20px;"
                + "font-size:12px;"
                + "}"
        );

        // =====================================================
        // BOTÃO
        // =====================================================

        html.append(
                ".botao-biblioteca {"
                + "display:block;"
                + "margin-top:12px;"
                + "padding:12px;"
                + "background:linear-gradient(135deg,#7c3aed,#9333ea);"
                + "color:white;"
                + "text-decoration:none;"
                + "border-radius:9px;"
                + "font-weight:bold;"
                + "transition:.25s;"
                + "box-shadow:0 5px 15px rgba(124,58,237,.25);"
                + "}"
        );

        html.append(
                ".botao-biblioteca:hover {"
                + "background:linear-gradient(135deg,#9333ea,#a855f7);"
                + "transform:scale(1.03);"
                + "}"
        );

        // =====================================================
        // EFEITO ROXO
        // =====================================================

        html.append(
                ".brilho-card {"
                + "position:absolute;"
                + "width:100px;"
                + "height:100px;"
                + "background:#9333ea;"
                + "filter:blur(70px);"
                + "opacity:.15;"
                + "top:-40px;"
                + "right:-40px;"
                + "pointer-events:none;"
                + "}"
        );

        // =====================================================
        // RESPONSIVO
        // =====================================================

        html.append(
                "@media(max-width:600px){"
                + ".jogos-container{"
                + "margin:20px auto;"
                + "padding:12px;"
                + "}"
                + ".titulo-jogos{"
                + "font-size:30px;"
                + "}"
                + ".catalogo-jogos{"
                + "grid-template-columns:"
                + "repeat(2,1fr);"
                + "gap:15px;"
                + "}"
                + ".capa-jogo,.sem-capa{"
                + "height:220px;"
                + "}"
                + ".card-jogo{"
                + "padding:9px;"
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
        // CONTEÚDO
        // =====================================================

        html.append(
                "<main class='jogos-container'>"
        );

        html.append(
                "<h2 class='titulo-jogos'>"
                + "🎮 Explore os Jogos"
                + "</h2>"
        );

        html.append(
                "<p class='subtitulo-jogos'>"
                + "Descubra novos jogos e adicione "
                + "os seus favoritos à biblioteca."
                + "</p>"
        );

        html.append(
                "<div class='catalogo-jogos'>"
        );

        // =====================================================
        // BANCO
        // =====================================================

        try {

            Connection conexao =
                    Conexao.conectar();

            if (conexao == null) {

                html.append(
                        "<p>Erro ao conectar ao banco.</p>"
                );

            } else {

                String sql =
                        "SELECT id, titulo, descricao, "
                        + "genero, plataforma, "
                        + "ano_lancamento, capa "
                        + "FROM jogo "
                        + "ORDER BY titulo";

                PreparedStatement stmt =
                        conexao.prepareStatement(sql);

                ResultSet resultado =
                        stmt.executeQuery();

                while (resultado.next()) {

                    int id =
                            resultado.getInt("id");

                    String titulo =
                            resultado.getString("titulo");

                    String genero =
                            resultado.getString("genero");

                    String plataforma =
                            resultado.getString("plataforma");

                    String ano =
                            resultado.getString(
                                    "ano_lancamento"
                            );

                    String capa =
                            resultado.getString("capa");

                    html.append(
                            "<article class='card-jogo'>"
                    );

                    html.append(
                            "<div class='brilho-card'></div>"
                    );

                    // =================================================
                    // CAPA
                    // =================================================

                    if (capa != null &&
                            !capa.trim().isEmpty()) {

                        String caminhoCapa =
                                capa.trim();

                        if (!caminhoCapa.startsWith(
                                "http://")
                                &&
                                !caminhoCapa.startsWith(
                                "https://")) {

                            if (caminhoCapa.startsWith("/")) {

                                caminhoCapa =
                                        request.getContextPath()
                                        + caminhoCapa;

                            } else {

                                caminhoCapa =
                                        request.getContextPath()
                                        + "/"
                                        + caminhoCapa;
                            }
                        }

                        html.append(
                                "<img "
                                + "class='capa-jogo' "
                                + "src='"
                                + escapar(caminhoCapa)
                                + "' "
                                + "alt='Capa de "
                                + escapar(titulo)
                                + "'>"
                        );

                    } else {

                        html.append(
                                "<div class='sem-capa'>"
                                + "🎮 Sem capa"
                                + "</div>"
                        );
                    }

                    // =================================================
                    // TÍTULO
                    // =================================================

                    html.append(
                            "<h3>"
                            + escapar(titulo)
                            + "</h3>"
                    );

                    // =================================================
                    // TAGS
                    // =================================================

                    html.append(
                            "<div class='info-jogo'>"
                    );

                    if (genero != null &&
                            !genero.trim().isEmpty()) {

                        html.append(
                                "<span class='tag-jogo'>"
                                + "🎯 "
                                + escapar(genero)
                                + "</span>"
                        );
                    }

                    if (plataforma != null &&
                            !plataforma.trim().isEmpty()) {

                        html.append(
                                "<span class='tag-jogo'>"
                                + "🎮 "
                                + escapar(plataforma)
                                + "</span>"
                        );
                    }

                    if (ano != null &&
                            !ano.trim().isEmpty()) {

                        html.append(
                                "<span class='tag-jogo'>"
                                + "📅 "
                                + escapar(ano)
                                + "</span>"
                        );
                    }

                    html.append("</div>");

                    // =================================================
                    // BOTÃO
                    // =================================================

                    html.append(
                            "<a "
                            + "class='botao-biblioteca' "
                            + "href='adicionar-biblioteca?id="
                            + id
                            + "'>"
                            + "＋ Minha biblioteca"
                            + "</a>"
                    );

                    html.append("</article>");
                }

                resultado.close();
                stmt.close();
                conexao.close();
            }

        } catch (Exception e) {

            e.printStackTrace();

            html.append(
                    "<p>Erro ao carregar os jogos.</p>"
            );
        }

        html.append("</div>");

        html.append("</main>");

        html.append("</body>");

        html.append("</html>");

        response.getWriter().println(
                html.toString()
        );
    }

    // =====================================================
    // ESCAPAR HTML
    // =====================================================

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

