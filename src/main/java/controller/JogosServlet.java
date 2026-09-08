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

        String busca =
                request.getParameter("busca");

        if (busca == null) {
            busca = "";
        }

        busca = busca.trim();

        String generoFiltro =
                request.getParameter("genero");

        if (generoFiltro == null) {
            generoFiltro = "";
        }

        generoFiltro = generoFiltro.trim();

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
                "<title>Jogos - Inventory</title>"
        );

        // FAVICON
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
        // CSS DA PÁGINA
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

        html.append(
                "body{" +
                "background:" +
                "linear-gradient(" +
                "135deg,#0d0714,#160b24,#0d0714);" +
                "color:white;" +
                "font-family:Arial,Helvetica,sans-serif;" +
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
                "border-bottom:1px solid #30263a;" +
                "}"
        );

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
        // CONTAINER
        // =====================================================

        html.append(
                ".jogos-container{" +
                "max-width:1200px;" +
                "margin:45px auto;" +
                "padding:20px;" +
                "}"
        );

        // =====================================================
        // TÍTULO
        // =====================================================

        html.append(
                ".titulo-jogos{" +
                "text-align:center;" +
                "margin-bottom:10px;" +
                "font-size:38px;" +
                "font-weight:bold;" +
                "background:" +
                "linear-gradient(" +
                "90deg,#a855f7,#7c3aed,#c084fc);" +
                "-webkit-background-clip:text;" +
                "-webkit-text-fill-color:transparent;" +
                "}"
        );

        html.append(
                ".subtitulo-jogos{" +
                "text-align:center;" +
                "color:#aaa;" +
                "font-size:16px;" +
                "margin-bottom:35px;" +
                "}"
        );

        // =====================================================
        // FILTRO
        // =====================================================

        html.append(
                ".filtro-genero{" +
                "display:flex;" +
                "align-items:center;" +
                "justify-content:center;" +
                "gap:10px;" +
                "margin-bottom:35px;" +
                "flex-wrap:wrap;" +
                "}"
        );

        html.append(
                ".filtro-genero label{" +
                "color:#ddd;" +
                "font-weight:bold;" +
                "}"
        );

        html.append(
                ".filtro-genero select{" +
                "padding:10px 14px;" +
                "background:#17101f;" +
                "color:#fff;" +
                "border:1px solid #63328c;" +
                "border-radius:8px;" +
                "outline:none;" +
                "}"
        );

        // =====================================================
        // GRID
        // =====================================================

        html.append(
                ".catalogo-jogos{" +
                "display:grid;" +
                "grid-template-columns:" +
                "repeat(auto-fill,minmax(210px,1fr));" +
                "gap:28px;" +
                "}"
        );

        // =====================================================
        // CARD
        // =====================================================

        html.append(
                ".card-jogo{" +
                "position:relative;" +
                "background:" +
                "linear-gradient(" +
                "145deg,#21152d,#17101f);" +
                "border:1px solid #38204d;" +
                "padding:12px;" +
                "border-radius:16px;" +
                "text-align:center;" +
                "overflow:hidden;" +
                "transition:all .3s ease;" +
                "box-shadow:" +
                "0 8px 25px rgba(0,0,0,.35);" +
                "}"
        );

        html.append(
                ".card-jogo:hover{" +
                "transform:translateY(-8px);" +
                "border-color:#8b5cf6;" +
                "box-shadow:" +
                "0 15px 35px rgba(124,58,237,.35);" +
                "}"
        );

        // =====================================================
        // CAPAS DOS JOGOS
        // =====================================================

        html.append(
                ".capa-jogo{" +
                "width:100%;" +
                "height:285px;" +
                "object-fit:cover;" +
                "border-radius:12px;" +
                "display:block;" +
                "transition:transform .3s ease;" +
                "background:#120d18;" +
                "}"
        );

        html.append(
                ".card-jogo:hover .capa-jogo{" +
                "transform:scale(1.03);" +
                "}"
        );

        html.append(
                ".sem-capa{" +
                "width:100%;" +
                "height:285px;" +
                "display:flex;" +
                "align-items:center;" +
                "justify-content:center;" +
                "background:#120d18;" +
                "border-radius:12px;" +
                "color:#777;" +
                "}"
        );

        // =====================================================
        // TÍTULO DO JOGO
        // =====================================================

        html.append(
                ".card-jogo h3{" +
                "font-size:18px;" +
                "margin:15px 5px 8px;" +
                "color:#fff;" +
                "min-height:44px;" +
                "}"
        );

        // =====================================================
        // INFORMAÇÕES
        // =====================================================

        html.append(
                ".info-jogo{" +
                "display:flex;" +
                "justify-content:center;" +
                "flex-wrap:wrap;" +
                "gap:7px;" +
                "margin-bottom:15px;" +
                "}"
        );

        html.append(
                ".tag-jogo{" +
                "background:#2d183e;" +
                "border:1px solid #4c2670;" +
                "color:#c084fc;" +
                "padding:5px 9px;" +
                "border-radius:20px;" +
                "font-size:12px;" +
                "}"
        );

        // =====================================================
        // BOTÃO
        // =====================================================

        html.append(
                ".botao-biblioteca{" +
                "display:block;" +
                "margin-top:12px;" +
                "padding:12px;" +
                "background:" +
                "linear-gradient(" +
                "135deg,#7c3aed,#9333ea);" +
                "color:white;" +
                "text-decoration:none;" +
                "border-radius:9px;" +
                "font-weight:bold;" +
                "transition:.25s;" +
                "box-shadow:" +
                "0 5px 15px rgba(124,58,237,.25);" +
                "}"
        );

        html.append(
                ".botao-biblioteca:hover{" +
                "background:" +
                "linear-gradient(" +
                "135deg,#9333ea,#a855f7);" +
                "transform:scale(1.03);" +
                "}"
        );

        // =====================================================
        // BRILHO
        // =====================================================

        html.append(
                ".brilho-card{" +
                "position:absolute;" +
                "width:100px;" +
                "height:100px;" +
                "background:#9333ea;" +
                "filter:blur(70px);" +
                "opacity:.15;" +
                "top:-40px;" +
                "right:-40px;" +
                "pointer-events:none;" +
                "}"
        );

        // =====================================================
        // RESPONSIVO
        // =====================================================

        html.append(
                "@media(max-width:600px){" +

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

                ".jogos-container{" +
                "margin:20px auto;" +
                "padding:12px;" +
                "}" +

                ".titulo-jogos{" +
                "font-size:30px;" +
                "}" +

                ".catalogo-jogos{" +
                "grid-template-columns:" +
                "repeat(2,1fr);" +
                "gap:15px;" +
                "}" +

                ".capa-jogo,.sem-capa{" +
                "height:220px;" +
                "}" +

                ".card-jogo{" +
                "padding:9px;" +
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
        // CONTEÚDO
        // =====================================================

        html.append(
                "<main class='jogos-container'>"
        );

        html.append(
                "<h2 class='titulo-jogos'>" +
                "🎮 Explore os Jogos" +
                "</h2>"
        );

        html.append(
                "<p class='subtitulo-jogos'>" +
                "Descubra novos jogos, filtre por gênero " +
                "e escolha seus favoritos." +
                "</p>"
        );

        // =====================================================
        // FILTRO
        // =====================================================

        html.append(
                "<form method='GET' " +
                "action='jogos' " +
                "class='filtro-genero'>"
        );

        html.append(
                "<label for='genero'>" +
                "Filtrar por gênero:" +
                "</label>"
        );

        html.append(
                "<select id='genero' " +
                "name='genero' " +
                "onchange='this.form.submit()'>"
        );

        html.append(
                "<option value=''>" +
                "Todos os gêneros" +
                "</option>"
        );

        String[] generos = {

            "Ação",
            "Aventura",
            "RPG",
            "Terror",
            "Tiro",
            "Estratégia",
            "Corrida",
            "Esporte",
            "Simulação",
            "Plataforma"

        };

        for (String genero : generos) {

            String selecionado =
                    generoFiltro.equalsIgnoreCase(
                            genero
                    )
                    ? " selected"
                    : "";

            html.append(
                    "<option value='" +
                    escapar(genero) +
                    "'" +
                    selecionado +
                    ">" +
                    escapar(genero) +
                    "</option>"
            );
        }

        html.append("</select>");

        if (!generoFiltro.isEmpty()) {

            html.append(
                    "<a href='jogos' " +
                    "style='" +
                    "padding:10px 14px;" +
                    "background:#2d183e;" +
                    "color:#ddd;" +
                    "border:1px solid #4c2670;" +
                    "border-radius:8px;" +
                    "text-decoration:none;" +
                    "'>" +
                    "Limpar" +
                    "</a>"
            );
        }

        html.append("</form>");

        // =====================================================
        // GRID
        // =====================================================

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
                        "<div class='nenhum-jogo'>" +
                        "Erro ao conectar ao banco." +
                        "</div>"
                );

            } else {

                String sql;

                if (!busca.isEmpty() &&
                        !generoFiltro.isEmpty()) {

                    sql =
                            "SELECT id, titulo, genero, " +
                            "plataforma, ano_lancamento, capa " +
                            "FROM jogo " +
                            "WHERE titulo LIKE ? " +
                            "AND genero LIKE ? " +
                            "ORDER BY titulo";

                } else if (!busca.isEmpty()) {

                    sql =
                            "SELECT id, titulo, genero, " +
                            "plataforma, ano_lancamento, capa " +
                            "FROM jogo " +
                            "WHERE titulo LIKE ? " +
                            "ORDER BY titulo";

                } else if (!generoFiltro.isEmpty()) {

                    sql =
                            "SELECT id, titulo, genero, " +
                            "plataforma, ano_lancamento, capa " +
                            "FROM jogo " +
                            "WHERE genero LIKE ? " +
                            "ORDER BY titulo";

                } else {

                    sql =
                            "SELECT id, titulo, genero, " +
                            "plataforma, ano_lancamento, capa " +
                            "FROM jogo " +
                            "ORDER BY titulo";
                }

                PreparedStatement stmt =
                        conexao.prepareStatement(sql);

                int parametro = 1;

                if (!busca.isEmpty()) {

                    stmt.setString(
                            parametro++,
                            "%" + busca + "%"
                    );
                }

                if (!generoFiltro.isEmpty()) {

                    stmt.setString(
                            parametro++,
                            "%" + generoFiltro + "%"
                    );
                }

                ResultSet resultado =
                        stmt.executeQuery();

                int quantidadeJogos = 0;

                while (resultado.next()) {

                    quantidadeJogos++;

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
                    // CAPA CORRIGIDA
                    // =================================================

                    if (capa != null &&
                            !capa.trim().isEmpty()) {

                        String caminhoCapa =
                                prepararCapa(
                                        capa,
                                        request
                                );

                        if (caminhoCapa != null &&
                                !caminhoCapa.isEmpty()) {

                            html.append(
                                    "<img " +
                                    "class='capa-jogo' " +
                                    "src='" +
                                    escapar(caminhoCapa) +
                                    "' " +
                                    "alt='Capa de " +
                                    escapar(titulo) +
                                    "'>"
                            );

                        } else {

                            html.append(
                                    "<div class='sem-capa'>" +
                                    "🎮 Sem capa" +
                                    "</div>"
                            );
                        }

                    } else {

                        html.append(
                                "<div class='sem-capa'>" +
                                "🎮 Sem capa" +
                                "</div>"
                        );
                    }

                    // =================================================
                    // TÍTULO
                    // =================================================

                    html.append(
                            "<h3>" +
                            escapar(titulo) +
                            "</h3>"
                    );

                    // =================================================
                    // INFORMAÇÕES
                    // =================================================

                    html.append(
                            "<div class='info-jogo'>"
                    );

                    if (genero != null &&
                            !genero.trim().isEmpty()) {

                        html.append(
                                "<span class='tag-jogo'>" +
                                "🎯 " +
                                escapar(genero) +
                                "</span>"
                        );
                    }

                    if (plataforma != null &&
                            !plataforma.trim().isEmpty()) {

                        html.append(
                                "<span class='tag-jogo'>" +
                                "🎮 " +
                                escapar(plataforma) +
                                "</span>"
                        );
                    }

                    if (ano != null &&
                            !ano.trim().isEmpty()) {

                        html.append(
                                "<span class='tag-jogo'>" +
                                "📅 " +
                                escapar(ano) +
                                "</span>"
                        );
                    }

                    html.append("</div>");

                    // =================================================
                    // BOTÃO
                    // =================================================

                    html.append(
                            "<a " +
                            "class='botao-biblioteca' " +
                            "href='adicionar-biblioteca?id=" +
                            id +
                            "'>" +
                            "＋ Minha biblioteca" +
                            "</a>"
                    );

                    html.append("</article>");
                }

                if (quantidadeJogos == 0) {

                    html.append(
                            "<div " +
                            "style='grid-column:1/-1;" +
                            "text-align:center;" +
                            "padding:30px;" +
                            "color:#aaa;'>" +
                            "Nenhum jogo encontrado." +
                            "</div>"
                    );
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

    // =========================================================
    // PREPARAR CAPA
    // =========================================================

    private String prepararCapa(
            String capa,
            HttpServletRequest request) {

        if (capa == null ||
                capa.trim().isEmpty()) {

            return "";
        }

        capa = capa.trim();

        // =====================================================
        // MARKDOWN
        // =====================================================

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

        // =====================================================
        // APENAS ID STEAM
        // =====================================================

        if (capa.matches("\\d+")) {

            return
                    "https://cdn.akamai.steamstatic.com/" +
                    "steam/apps/" +
                    capa +
                    "/library_600x900_2x.jpg";
        }

        // =====================================================
        // URL STEAM COM /apps/ID
        // =====================================================

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

        // =====================================================
        // URL DA INTERNET
        // =====================================================

        if (capa.startsWith("http://") ||
                capa.startsWith("https://")) {

            return capa;
        }

        // =====================================================
        // CAMINHO ABSOLUTO
        // =====================================================

        if (capa.startsWith("/")) {

            return
                    request.getContextPath()
                    + capa;
        }

        // =====================================================
        // CAMINHO RELATIVO
        // =====================================================

        return
                request.getContextPath()
                + "/"
                + capa;
    }

    // =========================================================
    // ESCAPAR HTML
    // =========================================================

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