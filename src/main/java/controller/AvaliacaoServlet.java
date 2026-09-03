package controller;

import dao.Conexao;
import model.Usuario;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/avaliar")
public class AvaliacaoServlet extends HttpServlet {

    // =========================================================
    // GET - MOSTRAR TELA DE AVALIAÇÃO
    // =========================================================

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

            response.sendRedirect("biblioteca");
            return;
        }

        try {

            int idJogo =
                    Integer.parseInt(idTexto);

            Connection conexao =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conexao.prepareStatement(
                            "SELECT titulo, capa " +
                            "FROM jogo " +
                            "WHERE id = ?"
                    );

            stmt.setInt(1, idJogo);

            ResultSet resultado =
                    stmt.executeQuery();

            if (!resultado.next()) {

                resultado.close();
                stmt.close();
                conexao.close();

                response.sendRedirect("biblioteca");
                return;
            }

            String titulo =
                    resultado.getString("titulo");

            String capa =
                    resultado.getString("capa");

            resultado.close();
            stmt.close();
            conexao.close();

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
                    "<title>Avaliar " +
                    escaparHtml(titulo) +
                    " - Inventory</title>"
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

            // BODY

            html.append(
                    "html,body{" +
                    "width:100%;" +
                    "min-height:100%;" +
                    "margin:0;" +
                    "padding:0;" +
                    "}"
            );

            html.append(
                    "body{" +
                    "font-family:Arial,Helvetica,sans-serif;" +
                    "background:" +
                    "radial-gradient(circle at 15% 0%,#29103d 0%,transparent 32%)," +
                    "radial-gradient(circle at 100% 100%,#1c0a2a 0%,transparent 35%)," +
                    "#0d0714;" +
                    "color:#fff;" +
                    "min-height:100vh;" +
                    "}"
            );

            // =====================================================
            // HEADER
            // =====================================================

            html.append(
                    "body > header{" +
                    "position:relative !important;" +
                    "width:100% !important;" +
                    "min-height:72px;" +
                    "margin:0 !important;" +
                    "padding:18px 40px !important;" +
                    "display:flex !important;" +
                    "align-items:center !important;" +
                    "justify-content:space-between !important;" +
                    "gap:30px;" +
                    "background:#150a1e !important;" +
                    "border-bottom:1px solid #382047 !important;" +
                    "}"
            );

            html.append(
                    "body > header h1{" +
                    "margin:0 !important;" +
                    "padding:0 !important;" +
                    "font-size:28px !important;" +
                    "font-weight:700 !important;" +
                    "color:#fff !important;" +
                    "}"
            );

            html.append(
                    "body > header nav{" +
                    "display:flex !important;" +
                    "align-items:center !important;" +
                    "justify-content:flex-end !important;" +
                    "flex-wrap:wrap !important;" +
                    "gap:24px !important;" +
                    "margin:0 !important;" +
                    "padding:0 !important;" +
                    "}"
            );

            html.append(
                    "body > header nav a{" +
                    "display:inline-block !important;" +
                    "margin:0 !important;" +
                    "padding:0 !important;" +
                    "color:#ac8cbc !important;" +
                    "font-size:14px !important;" +
                    "font-weight:500 !important;" +
                    "text-decoration:none !important;" +
                    "}"
            );

            html.append(
                    "body > header nav a:hover{" +
                    "color:#c17ade !important;" +
                    "}"
            );

            // =====================================================
            // CONTAINER
            // =====================================================

            html.append(
                    ".avaliacao-page{" +
                    "width:100%;" +
                    "max-width:760px;" +
                    "margin:0 auto;" +
                    "padding:40px 20px 70px;" +
                    "}"
            );

            // =====================================================
            // CARD
            // =====================================================

            html.append(
                    ".avaliacao-container{" +
                    "width:100%;" +
                    "background:linear-gradient(145deg,#1b0d27,#110816);" +
                    "border:1px solid #3c2050;" +
                    "border-radius:20px;" +
                    "padding:35px;" +
                    "text-align:center;" +
                    "box-shadow:0 18px 50px rgba(35,0,55,.30);" +
                    "}"
            );

            // =====================================================
            // CAPA
            // =====================================================

            html.append(
                    ".capa-avaliacao{" +
                    "width:190px;" +
                    "height:265px;" +
                    "display:block;" +
                    "margin:0 auto 24px;" +
                    "object-fit:cover;" +
                    "border-radius:10px;" +
                    "background:#140a1c;" +
                    "border:1px solid #4d2864;" +
                    "box-shadow:0 12px 30px rgba(0,0,0,.35);" +
                    "}"
            );

            // =====================================================
            // TITULO
            // =====================================================

            html.append(
                    ".titulo-avaliacao{" +
                    "margin:0;" +
                    "font-size:29px;" +
                    "font-weight:700;" +
                    "color:#fff;" +
                    "}"
            );

            html.append(
                    ".subtitulo-avaliacao{" +
                    "margin:10px 0 0;" +
                    "color:#aa8cba;" +
                    "font-size:15px;" +
                    "}"
            );

            // =====================================================
            // ESTRELAS
            // =====================================================

            html.append(
                    ".estrelas{" +
                    "display:flex;" +
                    "flex-direction:row;" +
                    "justify-content:center;" +
                    "gap:5px;" +
                    "margin:25px 0;" +
                    "}"
            );

            html.append(
                    ".estrelas input{" +
                    "display:none;" +
                    "}"
            );

            html.append(
                    ".estrelas label{" +
                    "font-size:41px;" +
                    "color:#62506b;" +
                    "cursor:pointer;" +
                    "transition:.2s;" +
                    "line-height:1;" +
                    "}"
            );

            html.append(
                    ".estrelas label:hover{" +
                    "color:#b960df;" +
                    "}"
            );

            html.append(
                    ".estrelas input:checked + label{" +
                    "color:#9b3dcb;" +
                    "}"
            );

            // =====================================================
            // HORAS
            // =====================================================

            html.append(
                    ".horas-container{" +
                    "margin-top:20px;" +
                    "text-align:left;" +
                    "}"
            );

            html.append(
                    ".horas-container label{" +
                    "display:block;" +
                    "margin-bottom:8px;" +
                    "font-size:14px;" +
                    "font-weight:bold;" +
                    "color:#d8c8df;" +
                    "}"
            );

            html.append(
                    ".campo-horas{" +
                    "width:100%;" +
                    "padding:13px;" +
                    "box-sizing:border-box;" +
                    "background:#100817;" +
                    "color:#fff;" +
                    "border:1px solid #40264d;" +
                    "border-radius:9px;" +
                    "font-size:15px;" +
                    "outline:none;" +
                    "}"
            );

            html.append(
                    ".campo-horas:focus{" +
                    "border-color:#7c29a9;" +
                    "}"
            );

            // =====================================================
            // RESENHA
            // =====================================================

            html.append(
                    ".campo-resenha{" +
                    "width:100%;" +
                    "height:155px;" +
                    "margin-top:20px;" +
                    "padding:14px;" +
                    "box-sizing:border-box;" +
                    "background:#100817;" +
                    "color:#fff;" +
                    "border:1px solid #40264d;" +
                    "border-radius:9px;" +
                    "resize:vertical;" +
                    "font-family:Arial,Helvetica,sans-serif;" +
                    "font-size:15px;" +
                    "outline:none;" +
                    "}"
            );

            html.append(
                    ".campo-resenha:focus{" +
                    "border-color:#7c29a9;" +
                    "}"
            );

            // =====================================================
            // BOTÃO
            // =====================================================

            html.append(
                    ".botao-postar{" +
                    "margin-top:22px;" +
                    "padding:12px 30px;" +
                    "border:1px solid #8232af;" +
                    "border-radius:8px;" +
                    "background:#6819a0;" +
                    "color:#fff;" +
                    "font-weight:bold;" +
                    "cursor:pointer;" +
                    "font-size:15px;" +
                    "transition:.2s;" +
                    "}"
            );

            html.append(
                    ".botao-postar:hover{" +
                    "background:#8127b8;" +
                    "border-color:#a04acb;" +
                    "}"
            );

            // =====================================================
            // RESPONSIVO
            // =====================================================

            html.append(
                    "@media(max-width:850px){" +

                    "body > header{" +
                    "flex-direction:column !important;" +
                    "padding:18px 15px !important;" +
                    "gap:15px !important;" +
                    "}" +

                    "body > header nav{" +
                    "justify-content:center !important;" +
                    "gap:15px !important;" +
                    "}" +

                    ".avaliacao-page{" +
                    "padding:25px 12px 50px;" +
                    "}" +

                    ".avaliacao-container{" +
                    "padding:25px 18px;" +
                    "}" +

                    "}"
            );

            html.append(
                    "@media(max-width:500px){" +

                    ".capa-avaliacao{" +
                    "width:160px;" +
                    "height:225px;" +
                    "}" +

                    ".titulo-avaliacao{" +
                    "font-size:24px;" +
                    "}" +

                    ".estrelas label{" +
                    "font-size:34px;" +
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
                    "<a href='buscar-usuarios'>Buscar usuários</a>"
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
                    "<main class='avaliacao-page'>"
            );

            html.append(
                    "<div class='avaliacao-container'>"
            );

            // =====================================================
            // CAPA
            // =====================================================

            String caminhoCapa =
                    prepararCapa(
                            capa,
                            request
                    );

            if (caminhoCapa != null &&
                    !caminhoCapa.isEmpty()) {

                html.append(
                        "<img " +
                        "class='capa-avaliacao' " +
                        "src='" +
                        escaparHtml(caminhoCapa) +
                        "' " +
                        "alt='Capa de " +
                        escaparHtml(titulo) +
                        "' " +
                        "onerror='this.style.display=\"none\";'>"
                );
            }

            // =====================================================
            // TITULO
            // =====================================================

            html.append(
                    "<h2 class='titulo-avaliacao'>" +
                    escaparHtml(titulo) +
                    "</h2>"
            );

            html.append(
                    "<p class='subtitulo-avaliacao'>" +
                    "O que você achou desse jogo?" +
                    "</p>"
            );

            // =====================================================
            // FORM
            // =====================================================

            html.append(
                    "<form method='POST' action='avaliar'>"
            );

            html.append(
                    "<input type='hidden' " +
                    "name='idJogo' " +
                    "value='" +
                    idJogo +
                    "'>"
            );

            // =====================================================
            // ESTRELAS
            // =====================================================

            html.append(
                    "<p><strong>Sua nota</strong></p>"
            );

            html.append(
                    "<div class='estrelas'>"
            );

            for (int i = 1; i <= 5; i++) {

                html.append(
                        "<input " +
                        "type='radio' " +
                        "id='estrela" +
                        i +
                        "' " +
                        "name='nota' " +
                        "value='" +
                        i +
                        "' " +
                        "required>"
                );

                html.append(
                        "<label for='estrela" +
                        i +
                        "'>★</label>"
                );
            }

            html.append("</div>");

            // =====================================================
            // HORAS
            // =====================================================

            html.append(
                    "<div class='horas-container'>"
            );

            html.append(
                    "<label for='horasJogadas'>" +
                    "Horas jogadas" +
                    "</label>"
            );

            html.append(
                    "<input " +
                    "class='campo-horas' " +
                    "type='number' " +
                    "id='horasJogadas' " +
                    "name='horasJogadas' " +
                    "min='0' " +
                    "step='0.1' " +
                    "placeholder='Ex: 25.5' " +
                    "required>"
            );

            html.append("</div>");

            // =====================================================
            // RESENHA
            // =====================================================

            html.append(
                    "<textarea " +
                    "class='campo-resenha' " +
                    "name='comentario' " +
                    "placeholder='Escreva sua resenha...' " +
                    "required></textarea>"
            );

            // =====================================================
            // BOTAO
            // =====================================================

            html.append(
                    "<button " +
                    "class='botao-postar' " +
                    "type='submit'>" +
                    "Postar avaliação" +
                    "</button>"
            );

            html.append("</form>");

            html.append("</div>");

            html.append("</main>");

            html.append("</body>");

            html.append("</html>");

            response.getWriter().println(
                    html.toString()
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect("biblioteca");
        }
    }

    // =========================================================
    // POST - SALVAR AVALIAÇÃO
    // =========================================================

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

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

            int idJogo =
                    Integer.parseInt(
                            request.getParameter("idJogo")
                    );

            double nota =
                    Double.parseDouble(
                            request.getParameter("nota")
                    );

            double horasJogadas =
                    Double.parseDouble(
                            request.getParameter("horasJogadas")
                    );

            String comentario =
                    request.getParameter("comentario");

            Connection conexao =
                    Conexao.conectar();

            // =====================================================
            // SALVAR/ATUALIZAR AVALIAÇÃO
            // =====================================================

            String sql =
                    "INSERT INTO avaliacao " +
                    "(id_usuario, id_jogo, nota, comentario, horas_jogadas) " +
                    "VALUES (?, ?, ?, ?, ?) " +
                    "ON CONFLICT(id_usuario, id_jogo) " +
                    "DO UPDATE SET " +
                    "nota = excluded.nota, " +
                    "comentario = excluded.comentario, " +
                    "horas_jogadas = excluded.horas_jogadas, " +
                    "data_avaliacao = CURRENT_TIMESTAMP";

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(1, idUsuario);
            stmt.setInt(2, idJogo);
            stmt.setDouble(3, nota);
            stmt.setString(4, comentario);
            stmt.setDouble(5, horasJogadas);

            stmt.executeUpdate();

            stmt.close();
            conexao.close();

            // =====================================================
            // MUDAR PARA ZERADO
            // =====================================================

            try {

                Connection conexao2 =
                        Conexao.conectar();

                PreparedStatement atualizar =
                        conexao2.prepareStatement(
                                "UPDATE biblioteca " +
                                "SET status = 'zerado' " +
                                "WHERE id_usuario = ? " +
                                "AND id_jogo = ?"
                        );

                atualizar.setInt(1, idUsuario);
                atualizar.setInt(2, idJogo);

                atualizar.executeUpdate();

                atualizar.close();
                conexao2.close();

            } catch (Exception erroBiblioteca) {

                erroBiblioteca.printStackTrace();
            }

            response.sendRedirect("biblioteca");

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect("biblioteca");
        }
    }

    // =========================================================
    // PREPARAR CAPA
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

        // =====================================================
        // MARKDOWN
        // =====================================================

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

        // =====================================================
        // SOMENTE APP ID
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

            String appId =
                    matcher.group(1);

            return
                    "https://cdn.akamai.steamstatic.com/" +
                    "steam/apps/" +
                    appId +
                    "/library_600x900_2x.jpg";
        }

        // =====================================================
        // URL NORMAL
        // =====================================================

        if (capa.startsWith("http://") ||
                capa.startsWith("https://")) {

            return capa;
        }

        // =====================================================
        // CAMINHO LOCAL
        // =====================================================

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
}