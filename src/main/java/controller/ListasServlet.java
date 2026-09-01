package controller;

import dao.Conexao;
import model.Usuario;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
@WebServlet("/listas")
public class ListasServlet extends HttpServlet {

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

        Usuario usuario =
                (Usuario) sessao.getAttribute("usuario");

        int idUsuario =
                usuario.getId();

        response.setContentType(
                "text/html;charset=UTF-8"
        );

        StringBuilder html =
                new StringBuilder();

        html.append("<!DOCTYPE html>");
        html.append("<html lang='pt-BR'>");

        html.append("<head>");

        html.append(
                "<meta charset='UTF-8'>"
        );

        html.append(
                "<meta name='viewport' "
                + "content='width=device-width,"
                + " initial-scale=1.0'>"
        );

        html.append(
                "<title>Minhas Listas - Inventory</title>"
        );

        html.append(
                "<link rel='stylesheet' "
                + "href='style.css'>"
        );

        html.append("<style>");

        html.append(
                "body{"
                + "background:"
                + "radial-gradient("
                + "circle at top,#35105f,"
                + "#12091b 50%,#09050d);"
                + "min-height:100vh;"
                + "color:white;"
                + "}"
        );

        html.append(
                ".listas-container{"
                + "max-width:1100px;"
                + "margin:45px auto;"
                + "padding:20px;"
                + "}"
        );

        html.append(
                ".listas-header{"
                + "background:linear-gradient("
                + "135deg,#291044,#4b1680);"
                + "border:1px solid #54256f;"
                + "border-radius:20px;"
                + "padding:30px;"
                + "margin-bottom:30px;"
                + "}"
        );

        html.append(
                ".listas-header h2{"
                + "margin:0 0 8px;"
                + "font-size:32px;"
                + "}"
        );

        html.append(
                ".listas-header p{"
                + "color:#c9bfd0;"
                + "}"
        );

        html.append(
                ".criar-lista{"
                + "display:flex;"
                + "gap:10px;"
                + "margin-top:20px;"
                + "}"
        );

        html.append(
                ".criar-lista input{"
                + "flex:1;"
                + "padding:13px;"
                + "background:#100b15;"
                + "border:1px solid #4a3159;"
                + "border-radius:9px;"
                + "color:white;"
                + "font-size:15px;"
                + "}"
        );

        html.append(
                ".botao-criar{"
                + "padding:13px 22px;"
                + "border:none;"
                + "border-radius:9px;"
                + "background:linear-gradient("
                + "135deg,#7c3aed,#a855f7);"
                + "color:white;"
                + "font-weight:bold;"
                + "cursor:pointer;"
                + "}"
        );

        html.append(
                ".lista{"
                + "background:linear-gradient("
                + "145deg,#21142c,#140b1b);"
                + "border:1px solid #442252;"
                + "border-radius:20px;"
                + "padding:25px;"
                + "margin-bottom:25px;"
                + "}"
        );

        html.append(
                ".lista-topo{"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:space-between;"
                + "gap:15px;"
                + "margin-bottom:20px;"
                + "}"
        );

        html.append(
                ".lista-nome{"
                + "font-size:24px;"
                + "font-weight:bold;"
                + "}"
        );

        html.append(
                ".botao-excluir{"
                + "padding:9px 14px;"
                + "border:1px solid #613048;"
                + "border-radius:8px;"
                + "background:#25121c;"
                + "color:#dc9cad;"
                + "cursor:pointer;"
                + "}"
        );

        html.append(
                ".jogos-lista{"
                + "display:grid;"
                + "grid-template-columns:"
                + "repeat(auto-fill,minmax(150px,1fr));"
                + "gap:15px;"
                + "}"
        );

        html.append(
                ".jogo-lista{"
                + "background:#160d1e;"
                + "border:1px solid #34203d;"
                + "border-radius:12px;"
                + "padding:9px;"
                + "}"
        );

        html.append(
                ".capa-lista{"
                + "width:100%;"
                + "height:190px;"
                + "object-fit:cover;"
                + "border-radius:8px;"
                + "display:block;"
                + "background:#24152f;"
                + "}"
        );

        html.append(
                ".jogo-lista h4{"
                + "font-size:14px;"
                + "margin:10px 3px;"
                + "line-height:1.3;"
                + "}"
        );

        html.append(
                ".sem-capa{"
                + "height:190px;"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:center;"
                + "background:#24152f;"
                + "border-radius:8px;"
                + "color:#777;"
                + "}"
        );

        html.append(
                ".adicionar-jogo{"
                + "display:flex;"
                + "gap:10px;"
                + "margin-top:20px;"
                + "}"
        );

        html.append(
                ".adicionar-jogo select{"
                + "flex:1;"
                + "padding:11px;"
                + "background:#100b15;"
                + "border:1px solid #47305a;"
                + "border-radius:8px;"
                + "color:white;"
                + "}"
        );

        html.append(
                ".botao-adicionar{"
                + "padding:11px 18px;"
                + "border:none;"
                + "border-radius:8px;"
                + "background:#6d28d9;"
                + "color:white;"
                + "font-weight:bold;"
                + "cursor:pointer;"
                + "}"
        );

        html.append(
                ".vazio{"
                + "padding:35px;"
                + "border:1px dashed #4a3158;"
                + "border-radius:15px;"
                + "text-align:center;"
                + "color:#8e8295;"
                + "}"
        );

        html.append(
                "@media(max-width:600px){"
                + ".criar-lista,"
                + ".adicionar-jogo{"
                + "flex-direction:column;"
                + "}"
                + ".lista-topo{"
                + "align-items:flex-start;"
                + "}"
                + ".jogos-lista{"
                + "grid-template-columns:repeat(2,1fr);"
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
        // CONTEÚDO
        // =====================================================

        html.append(
                "<main class='listas-container'>"
        );

        html.append(
                "<section class='listas-header'>"
        );

        html.append(
                "<h2>Minhas listas</h2>"
        );

        html.append(
                "<p>"
                + "Crie coleções personalizadas "
                + "com seus jogos favoritos."
                + "</p>"
        );

        html.append(
                "<form "
                + "class='criar-lista' "
                + "method='POST' "
                + "action='criar-lista'>"
        );

        html.append(
                "<input "
                + "type='text' "
                + "name='nome' "
                + "maxlength='80' "
                + "placeholder='Nome da nova lista...'"
                + " required>"
        );

        html.append(
                "<button "
                + "class='botao-criar' "
                + "type='submit'>"
                + "Criar lista"
                + "</button>"
        );

        html.append("</form>");

        html.append("</section>");

        // =====================================================
        // LISTAS
        // =====================================================

        try {

            Connection conexao =
                    Conexao.conectar();

            String sqlListas =
                    "SELECT id, nome "
                    + "FROM lista "
                    + "WHERE id_usuario = ? "
                    + "ORDER BY data_criacao DESC";

            PreparedStatement stmtListas =
                    conexao.prepareStatement(
                            sqlListas
                    );

            stmtListas.setInt(
                    1,
                    idUsuario
            );

            ResultSet rsListas =
                    stmtListas.executeQuery();

            boolean possuiListas =
                    false;

            // buscar jogos para o select
            String sqlJogos =
                    "SELECT id, titulo "
                    + "FROM jogo "
                    + "ORDER BY titulo";

            PreparedStatement stmtJogos =
                    conexao.prepareStatement(
                            sqlJogos
                    );

            ResultSet rsJogos =
                    stmtJogos.executeQuery();

            StringBuilder opcoesJogos =
                    new StringBuilder();

            while (rsJogos.next()) {

                opcoesJogos.append(
                        "<option value='"
                        + rsJogos.getInt("id")
                        + "'>"
                        + escapar(
                                rsJogos.getString("titulo")
                          )
                        + "</option>"
                );
            }

            rsJogos.close();
            stmtJogos.close();

            while (rsListas.next()) {

                possuiListas =
                        true;

                int idLista =
                        rsListas.getInt("id");

                String nomeLista =
                        rsListas.getString("nome");

                html.append(
                        "<section class='lista'>"
                );

                html.append(
                        "<div class='lista-topo'>"
                );

                html.append(
                        "<div class='lista-nome'>"
                        + escapar(nomeLista)
                        + "</div>"
                );

                html.append(
                        "<form "
                        + "method='POST' "
                        + "action='excluir-lista'>"
                );

                html.append(
                        "<input "
                        + "type='hidden' "
                        + "name='idLista' "
                        + "value='"
                        + idLista
                        + "'>"
                );

                html.append(
                        "<button "
                        + "class='botao-excluir' "
                        + "type='submit' "
                        + "onclick=\""
                        + "return confirm("
                        + "'Excluir esta lista?'"
                        + ");\">"
                        + "Excluir"
                        + "</button>"
                );

                html.append("</form>");

                html.append("</div>");

                // =============================================
                // JOGOS DA LISTA
                // =============================================

                String sqlItens =
                        "SELECT "
                        + "j.id, "
                        + "j.titulo, "
                        + "j.capa "
                        + "FROM lista_jogo lj "
                        + "INNER JOIN jogo j "
                        + "ON j.id = lj.id_jogo "
                        + "WHERE lj.id_lista = ? "
                        + "ORDER BY lj.data_adicionado";

                PreparedStatement stmtItens =
                        conexao.prepareStatement(
                                sqlItens
                        );

                stmtItens.setInt(
                        1,
                        idLista
                );

                ResultSet rsItens =
                        stmtItens.executeQuery();

                html.append(
                        "<div class='jogos-lista'>"
                );

                boolean possuiJogos =
                        false;

                while (rsItens.next()) {

                    possuiJogos =
                            true;

                    String titulo =
                            rsItens.getString(
                                    "titulo"
                            );

                    String capa =
                            rsItens.getString(
                                    "capa"
                            );

                    html.append(
                            "<article "
                            + "class='jogo-lista'>"
                    );

                    String caminhoCapa =
                            prepararCapa(
                                    request,
                                    capa
                            );

                    if (caminhoCapa != null) {

                        html.append(
                                "<img "
                                + "class='capa-lista' "
                                + "src='"
                                + escapar(caminhoCapa)
                                + "' "
                                + "alt='"
                                + escapar(titulo)
                                + "'>"
                        );

                    } else {

                        html.append(
                                "<div class='sem-capa'>"
                                + "Sem capa"
                                + "</div>"
                        );
                    }

                    html.append(
                            "<h4>"
                            + escapar(titulo)
                            + "</h4>"
                    );

                    html.append(
                            "</article>"
                    );
                }

                rsItens.close();
                stmtItens.close();

                html.append(
                        "</div>"
                );

                if (!possuiJogos) {

                    html.append(
                            "<div class='vazio'>"
                            + "Esta lista ainda não possui jogos."
                            + "</div>"
                    );
                }

                // =============================================
                // ADICIONAR JOGO
                // =============================================

                html.append(
                        "<form "
                        + "class='adicionar-jogo' "
                        + "method='POST' "
                        + "action='adicionar-jogo-lista'>"
                );

                html.append(
                        "<input "
                        + "type='hidden' "
                        + "name='idLista' "
                        + "value='"
                        + idLista
                        + "'>"
                );

                html.append(
                        "<select "
                        + "name='idJogo' "
                        + "required>"
                );

                html.append(
                        "<option value=''>"
                        + "Adicionar jogo..."
                        + "</option>"
                );

                html.append(
                        opcoesJogos.toString()
                );

                html.append(
                        "</select>"
                );

                html.append(
                        "<button "
                        + "class='botao-adicionar' "
                        + "type='submit'>"
                        + "Adicionar"
                        + "</button>"
                );

                html.append(
                        "</form>"
                );

                html.append(
                        "</section>"
                );
            }

            rsListas.close();
            stmtListas.close();
            conexao.close();

            if (!possuiListas) {

                html.append(
                        "<div class='vazio'>"
                        + "Você ainda não criou nenhuma lista."
                        + "</div>"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            html.append(
                    "<div class='vazio'>"
                    + "Erro ao carregar suas listas."
                    + "</div>"
            );
        }

        html.append("</main>");

        html.append("</body>");
        html.append("</html>");

        response.getWriter().println(
                html.toString()
        );
    }

    // =====================================================
    // CAPA
    // =====================================================

    private String prepararCapa(
            HttpServletRequest request,
            String capa) {

        if (capa == null ||
                capa.trim().isEmpty()) {

            return null;
        }

        String caminho =
                capa.trim();

        // URL salva em Markdown
        if (caminho.startsWith("[")
                &&
                caminho.contains("](")
                &&
                caminho.endsWith(")")) {

            int pos =
                    caminho.indexOf("](");

            caminho =
                    caminho.substring(
                            pos + 2,
                            caminho.length() - 1
                    );
        }

        // Steam App ID
        Pattern pattern =
                Pattern.compile(
                        "/apps/(\\d+)"
                );

        Matcher matcher =
                pattern.matcher(caminho);

        if (matcher.find()) {

            String appId =
                    matcher.group(1);

            caminho =
                    "https://shared.cloudflare.steamstatic.com/"
                    + "store_item_assets/steam/apps/"
                    + appId
                    + "/library_600x900_2x.jpg";
        }

        if (!caminho.startsWith("http://")
                &&
                !caminho.startsWith("https://")) {

            while (
                    caminho.startsWith("/")
            ) {

                caminho =
                        caminho.substring(1);
            }

            caminho =
                    request.getContextPath()
                    + "/"
                    + caminho;
        }

        return caminho;
    }

    // =====================================================
    // ESCAPAR
    // =====================================================

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