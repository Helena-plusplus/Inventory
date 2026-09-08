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
                            "SELECT titulo, capa "
                            + "FROM jogo "
                            + "WHERE id = ?"
                    );

            stmt.setInt(
                    1,
                    idJogo
            );

            ResultSet resultado =
                    stmt.executeQuery();

            if (!resultado.next()) {

                resultado.close();
                stmt.close();
                conexao.close();

                response.sendRedirect(
                        "biblioteca"
                );

                return;
            }

            String titulo =
                    resultado.getString(
                            "titulo"
                    );

            String capa =
                    resultado.getString(
                            "capa"
                    );

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

            html.append(
                    "<!DOCTYPE html>"
            );

            html.append(
                    "<html lang='pt-BR'>"
            );

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
                    "<title>Avaliar "
                    + escapar(titulo)
                    + " - Inventory</title>"
            );

            html.append(
                    "<link rel='icon' "
                    + "type='image/png' "
                    + "href='icon.png'>"
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
                    "* {"
                    + "box-sizing:border-box;"
                    + "}"
            );

            html.append(
                    "body {"
                    + "margin:0;"
                    + "min-height:100vh;"
                    + "background:"
                    + "radial-gradient("
                    + "circle at top,"
                    + "#35105f 0%,"
                    + "#160b22 45%,"
                    + "#09060d 100%"
                    + ");"
                    + "color:#fff;"
                    + "font-family:"
                    + "Arial,Helvetica,sans-serif;"
                    + "}"
            );

            // =====================================================
            // HEADER
            // =====================================================

            html.append(
                    "header {"
                    + "width:100%;"
                    + "min-height:80px;"
                    + "padding:14px 35px;"
                    + "display:flex;"
                    + "align-items:center;"
                    + "justify-content:space-between;"
                    + "gap:25px;"
                    + "background:rgba(10,6,15,0.96);"
                    + "border-bottom:1px solid #322044;"
                    + "}"
            );

            html.append(
                    ".logo-area {"
                    + "display:flex;"
                    + "align-items:center;"
                    + "gap:9px;"
                    + "flex-shrink:0;"
                    + "}"
            );

            html.append(
                    ".logo-header {"
                    + "width:40px !important;"
                    + "height:40px !important;"
                    + "max-width:40px !important;"
                    + "max-height:40px !important;"
                    + "object-fit:contain !important;"
                    + "display:block !important;"
                    + "flex-shrink:0;"
                    + "}"
            );

            html.append(
                    ".logo-area h1 {"
                    + "margin:0;"
                    + "padding:0;"
                    + "font-size:30px;"
                    + "font-weight:bold;"
                    + "line-height:1;"
                    + "color:#fff;"
                    + "}"
            );

            html.append(
                    "nav {"
                    + "display:flex;"
                    + "align-items:center;"
                    + "justify-content:flex-end;"
                    + "gap:28px;"
                    + "flex-wrap:wrap;"
                    + "}"
            );

            html.append(
                    "nav a {"
                    + "color:#aaa1b5;"
                    + "text-decoration:none;"
                    + "font-size:14px;"
                    + "font-weight:bold;"
                    + "transition:0.2s;"
                    + "}"
            );

            html.append(
                    "nav a:hover {"
                    + "color:#b66cff;"
                    + "}"
            );

            // =====================================================
            // CONTAINER
            // =====================================================

            html.append(
                    ".avaliacao-container {"
                    + "max-width:600px;"
                    + "margin:50px auto;"
                    + "padding:35px;"
                    + "background:"
                    + "linear-gradient("
                    + "135deg,"
                    + "#24102f,"
                    + "#140b1b"
                    + ");"
                    + "border:1px solid #4b2464;"
                    + "border-radius:16px;"
                    + "text-align:center;"
                    + "box-shadow:"
                    + "0 15px 45px "
                    + "rgba(0,0,0,0.35);"
                    + "}"
            );

            // =====================================================
            // CAPA
            // =====================================================

            html.append(
                    ".capa-avaliacao {"
                    + "width:180px !important;"
                    + "height:250px !important;"
                    + "max-width:180px !important;"
                    + "max-height:250px !important;"
                    + "object-fit:cover !important;"
                    + "border-radius:8px;"
                    + "display:block;"
                    + "margin:0 auto 20px;"
                    + "}"
            );

            // =====================================================
            // TITULO
            // =====================================================

            html.append(
                    ".avaliacao-container h2 {"
                    + "margin:10px 0;"
                    + "font-size:26px;"
                    + "color:#fff;"
                    + "}"
            );

            html.append(
                    ".avaliacao-container p {"
                    + "color:#bbb0c2;"
                    + "}"
            );

            // =====================================================
            // ESTRELAS
            // =====================================================

            html.append(
                    ".estrelas {"
                    + "display:flex;"
                    + "flex-direction:row-reverse;"
                    + "justify-content:center;"
                    + "gap:5px;"
                    + "margin:22px 0;"
                    + "}"
            );

            html.append(
                    ".estrelas input {"
                    + "display:none;"
                    + "}"
            );

            html.append(
                    ".estrelas label {"
                    + "font-size:42px;"
                    + "line-height:1;"
                    + "color:#666;"
                    + "cursor:pointer;"
                    + "transition:0.2s;"
                    + "}"
            );

            /*
             * IMPORTANTE:
             *
             * Como os elementos são renderizados na ordem
             * 5, 4, 3, 2, 1 e o container usa row-reverse,
             * podemos usar ~ para pintar todas as estrelas
             * até a selecionada.
             */

            html.append(
                    ".estrelas label:hover,"
                    + ".estrelas label:hover ~ label,"
                    + ".estrelas input:checked ~ label {"
                    + "color:#ffd700;"
                    + "text-shadow:"
                    + "0 0 8px "
                    + "rgba(255,215,0,0.35);"
                    + "}"
            );

            // =====================================================
            // HORAS
            // =====================================================

            html.append(
                    ".horas-container {"
                    + "margin-top:20px;"
                    + "text-align:left;"
                    + "}"
            );

            html.append(
                    ".horas-container label {"
                    + "display:block;"
                    + "margin-bottom:8px;"
                    + "color:#ddd;"
                    + "font-weight:bold;"
                    + "}"
            );

            html.append(
                    ".campo-horas {"
                    + "width:100%;"
                    + "padding:12px;"
                    + "box-sizing:border-box;"
                    + "background:#14101a;"
                    + "color:white;"
                    + "border:1px solid #493252;"
                    + "border-radius:8px;"
                    + "font-size:16px;"
                    + "outline:none;"
                    + "}"
            );

            html.append(
                    ".campo-horas:focus {"
                    + "border-color:#8b35d6;"
                    + "}"
            );

            // =====================================================
            // RESENHA
            // =====================================================

            html.append(
                    ".campo-resenha {"
                    + "width:100%;"
                    + "height:150px;"
                    + "padding:15px;"
                    + "box-sizing:border-box;"
                    + "background:#14101a;"
                    + "color:white;"
                    + "border:1px solid #493252;"
                    + "border-radius:8px;"
                    + "resize:vertical;"
                    + "font-family:Arial,Helvetica,sans-serif;"
                    + "font-size:15px;"
                    + "margin-top:20px;"
                    + "outline:none;"
                    + "}"
            );

            html.append(
                    ".campo-resenha:focus {"
                    + "border-color:#8b35d6;"
                    + "}"
            );

            html.append(
                    ".campo-resenha::placeholder,"
                    + ".campo-horas::placeholder {"
                    + "color:#71677a;"
                    + "}"
            );

            // =====================================================
            // BOTÃO
            // =====================================================

            html.append(
                    ".botao-postar {"
                    + "margin-top:20px;"
                    + "padding:12px 30px;"
                    + "border:none;"
                    + "border-radius:7px;"
                    + "background:"
                    + "linear-gradient("
                    + "135deg,"
                    + "#7c3aed,"
                    + "#9333ea"
                    + ");"
                    + "color:white;"
                    + "font-weight:bold;"
                    + "cursor:pointer;"
                    + "font-size:16px;"
                    + "transition:0.2s;"
                    + "}"
            );

            html.append(
                    ".botao-postar:hover {"
                    + "background:#a33cff;"
                    + "transform:translateY(-1px);"
                    + "}"
            );

            // =====================================================
            // RESPONSIVO
            // =====================================================

            html.append(
                    "@media(max-width:800px) {"

                    + "header {"
                    + "padding:14px 20px;"
                    + "flex-direction:column;"
                    + "align-items:flex-start;"
                    + "}"

                    + "nav {"
                    + "justify-content:flex-start;"
                    + "gap:16px;"
                    + "}"

                    + "}"
            );

            html.append(
                    "@media(max-width:600px) {"

                    + ".avaliacao-container {"
                    + "margin:25px 12px;"
                    + "padding:25px 18px;"
                    + "}"

                    + ".logo-header {"
                    + "width:36px !important;"
                    + "height:36px !important;"
                    + "}"

                    + ".logo-area h1 {"
                    + "font-size:26px;"
                    + "}"

                    + ".capa-avaliacao {"
                    + "width:160px !important;"
                    + "height:225px !important;"
                    + "}"

                    + ".estrelas label {"
                    + "font-size:36px;"
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
                    "<div class='logo-area'>"
                    + "<img "
                    + "src='icon.png' "
                    + "alt='Logo Inventory' "
                    + "class='logo-header'>"
                    + "<h1>Inventory</h1>"
                    + "</div>"
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
            // CONTEUDO
            // =====================================================

            html.append(
                    "<main class='avaliacao-container'>"
            );

            // =====================================================
            // CAPA
            // =====================================================

            String caminhoCapa =
                    prepararCapa(
                            request,
                            capa
                    );

            if (caminhoCapa != null &&
                    !caminhoCapa.isEmpty()) {

                html.append(
                        "<img "
                        + "class='capa-avaliacao' "
                        + "src='"
                        + escapar(caminhoCapa)
                        + "' "
                        + "alt='Capa de "
                        + escapar(titulo)
                        + "' "
                        + "onerror=\""
                        + "this.style.display='none';"
                        + "\""
                        + ">"
                );
            }

            // =====================================================
            // TITULO
            // =====================================================

            html.append("<h2>");

            html.append(
                    escapar(titulo)
            );

            html.append("</h2>");

            html.append(
                    "<p>O que você achou desse jogo?</p>"
            );

            // =====================================================
            // FORM
            // =====================================================

            html.append(
                    "<form method='POST' "
                    + "action='avaliar'>"
            );

            html.append(
                    "<input type='hidden' "
                    + "name='idJogo' "
                    + "value='"
                    + idJogo
                    + "'>"
            );

            // =====================================================
            // ESTRELAS
            // =====================================================

            html.append(
                    "<p><strong>Sua nota:</strong></p>"
            );

            html.append(
                    "<div class='estrelas'>"
            );

            /*
             * A ordem precisa ser 5 -> 1.
             *
             * Visualmente, com row-reverse:
             *
             * 1  2  3  4  5
             *
             * Quando selecionar 4:
             *
             * ★  ★  ★  ★  ☆
             */

            for (int i = 5; i >= 1; i--) {

                html.append(
                        "<input "
                        + "type='radio' "
                        + "id='estrela"
                        + i
                        + "' "
                        + "name='nota' "
                        + "value='"
                        + i
                        + "' "
                        + "required>"
                );

                html.append(
                        "<label "
                        + "for='estrela"
                        + i
                        + "'>"
                        + "★"
                        + "</label>"
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
                    "<label for='horasJogadas'>"
                    + "Horas jogadas"
                    + "</label>"
            );

            html.append(
                    "<input "
                    + "class='campo-horas' "
                    + "type='number' "
                    + "id='horasJogadas' "
                    + "name='horasJogadas' "
                    + "min='0' "
                    + "step='0.1' "
                    + "placeholder='Ex: 25.5' "
                    + "required>"
            );

            html.append("</div>");

            // =====================================================
            // RESENHA
            // =====================================================

            html.append(
                    "<textarea "
                    + "class='campo-resenha' "
                    + "name='comentario' "
                    + "placeholder='Escreva sua resenha...' "
                    + "required></textarea>"
            );

            html.append("<br>");

            // =====================================================
            // BOTÃO
            // =====================================================

            html.append(
                    "<button "
                    + "class='botao-postar' "
                    + "type='submit'>"
                    + "Postar avaliação"
                    + "</button>"
            );

            html.append("</form>");

            html.append("</main>");

            html.append("</body>");

            html.append("</html>");

            response.getWriter().println(
                    html.toString()
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect(
                    "biblioteca"
            );
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

        request.setCharacterEncoding(
                "UTF-8"
        );

        HttpSession sessao =
                request.getSession(false);

        if (sessao == null ||
                sessao.getAttribute("usuario") == null) {

            response.sendRedirect(
                    "login.html"
            );

            return;
        }

        try {

            Usuario usuario =
                    (Usuario) sessao.getAttribute(
                            "usuario"
                    );

            int idUsuario =
                    usuario.getId();

            int idJogo =
                    Integer.parseInt(
                            request.getParameter(
                                    "idJogo"
                            )
                    );

            double nota =
                    Double.parseDouble(
                            request.getParameter(
                                    "nota"
                            )
                    );

            double horasJogadas =
                    Double.parseDouble(
                            request.getParameter(
                                    "horasJogadas"
                            )
                    );

            String comentario =
                    request.getParameter(
                            "comentario"
                    );

            Connection conexao =
                    Conexao.conectar();

            // =====================================================
            // SALVAR / ATUALIZAR AVALIAÇÃO
            // =====================================================

            String sql =
                    "INSERT INTO avaliacao "
                    + "(id_usuario, id_jogo, nota, "
                    + "comentario, horas_jogadas) "
                    + "VALUES (?, ?, ?, ?, ?) "
                    + "ON CONFLICT(id_usuario, id_jogo) "
                    + "DO UPDATE SET "
                    + "nota = excluded.nota, "
                    + "comentario = excluded.comentario, "
                    + "horas_jogadas = excluded.horas_jogadas, "
                    + "data_avaliacao = CURRENT_TIMESTAMP";

            PreparedStatement stmt =
                    conexao.prepareStatement(
                            sql
                    );

            stmt.setInt(
                    1,
                    idUsuario
            );

            stmt.setInt(
                    2,
                    idJogo
            );

            stmt.setDouble(
                    3,
                    nota
            );

            stmt.setString(
                    4,
                    comentario
            );

            stmt.setDouble(
                    5,
                    horasJogadas
            );

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
                                "UPDATE biblioteca "
                                + "SET status = 'zerado', "
                                + "horas_jogadas = ? "
                                + "WHERE id_usuario = ? "
                                + "AND id_jogo = ?"
                        );

                atualizar.setDouble(
                        1,
                        horasJogadas
                );

                atualizar.setInt(
                        2,
                        idUsuario
                );

                atualizar.setInt(
                        3,
                        idJogo
                );

                atualizar.executeUpdate();

                atualizar.close();
                conexao2.close();

            } catch (Exception erroBiblioteca) {

                erroBiblioteca.printStackTrace();
            }

            // =====================================================
            // VOLTAR PARA BIBLIOTECA
            // =====================================================

            response.sendRedirect(
                    "biblioteca"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect(
                    "biblioteca"
            );
        }
    }

    // =========================================================
    // PREPARAR CAPA
    // =========================================================

    private String prepararCapa(
            HttpServletRequest request,
            String capa) {

        if (capa == null ||
                capa.trim().isEmpty()) {

            return null;
        }

        String caminho =
                capa.trim();

        // =====================================================
        // MARKDOWN
        // =====================================================

        if (caminho.startsWith("[") &&
                caminho.contains("](") &&
                caminho.endsWith(")")) {

            int inicio =
                    caminho.indexOf("](");

            caminho =
                    caminho.substring(
                            inicio + 2,
                            caminho.length() - 1
                    );
        }

        // =====================================================
        // STEAM APP ID
        // =====================================================

        if (caminho.matches("\\d+")) {

            return
                    "https://cdn.akamai.steamstatic.com/"
                    + "steam/apps/"
                    + caminho
                    + "/library_600x900_2x.jpg";
        }

        // =====================================================
        // /apps/ID
        // =====================================================

        Pattern pattern =
                Pattern.compile(
                        "/apps/(\\d+)"
                );

        Matcher matcher =
                pattern.matcher(
                        caminho
                );

        if (matcher.find()) {

            String appId =
                    matcher.group(1);

            return
                    "https://cdn.akamai.steamstatic.com/"
                    + "steam/apps/"
                    + appId
                    + "/library_600x900_2x.jpg";
        }

        // =====================================================
        // URL
        // =====================================================

        if (caminho.startsWith("http://") ||
                caminho.startsWith("https://")) {

            return caminho;
        }

        // =====================================================
        // CAMINHO LOCAL
        // =====================================================

        while (
                caminho.startsWith("/")
        ) {

            caminho =
                    caminho.substring(1);
        }

        return
                request.getContextPath()
                + "/"
                + caminho;
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