package controller;

import dao.Conexao;
import model.Usuario;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                "<meta name='viewport' "
                + "content='width=device-width, "
                + "initial-scale=1.0'>"
        );

        html.append(
                "<title>Minhas Listas - Inventory</title>"
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
                "body{"
                + "margin:0;"
                + "background:"
                + "radial-gradient("
                + "circle at top,"
                + "#35105f,"
                + "#12091b 50%,"
                + "#09050d"
                + ");"
                + "min-height:100vh;"
                + "color:white;"
                + "}"
        );

        html.append(
                ".listas-container{"
                + "max-width:1150px;"
                + "margin:45px auto;"
                + "padding:20px;"
                + "}"
        );

        html.append(
                ".listas-header{"
                + "background:"
                + "linear-gradient("
                + "135deg,#291044,#4b1680"
                + ");"
                + "border:1px solid #54256f;"
                + "border-radius:22px;"
                + "padding:35px;"
                + "margin-bottom:30px;"
                + "box-shadow:"
                + "0 18px 50px rgba(0,0,0,.35);"
                + "}"
        );

        html.append(
                ".listas-header h2{"
                + "font-size:32px;"
                + "margin:0 0 8px;"
                + "}"
        );

        html.append(
                ".listas-header p{"
                + "color:#cfc4d8;"
                + "margin:0;"
                + "}"
        );

        html.append(
                ".form-criar-lista{"
                + "display:flex;"
                + "gap:10px;"
                + "margin-top:22px;"
                + "}"
        );

        html.append(
                ".form-criar-lista input{"
                + "flex:1;"
                + "padding:14px;"
                + "background:#100b15;"
                + "border:1px solid #47305a;"
                + "border-radius:9px;"
                + "color:white;"
                + "font-size:15px;"
                + "outline:none;"
                + "}"
        );

        html.append(
                ".form-criar-lista input:focus{"
                + "border-color:#a855f7;"
                + "}"
        );

        html.append(
                ".botao-criar-lista{"
                + "padding:14px 22px;"
                + "border:none;"
                + "border-radius:9px;"
                + "background:"
                + "linear-gradient("
                + "135deg,#7c3aed,#a855f7"
                + ");"
                + "color:white;"
                + "font-weight:bold;"
                + "cursor:pointer;"
                + "}"
        );

        html.append(
                ".lista{"
                + "background:"
                + "linear-gradient("
                + "145deg,#21142c,#140b1b"
                + ");"
                + "border:1px solid #442252;"
                + "border-radius:20px;"
                + "padding:25px;"
                + "margin-bottom:25px;"
                + "box-shadow:"
                + "0 10px 30px rgba(0,0,0,.2);"
                + "}"
        );

        html.append(
                ".lista-topo{"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:space-between;"
                + "gap:15px;"
                + "margin-bottom:22px;"
                + "}"
        );

        html.append(
                ".nome-lista{"
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
                + "color:#e0a5b6;"
                + "cursor:pointer;"
                + "}"
        );

        html.append(
                ".botao-excluir:hover{"
                + "background:#381722;"
                + "}"
        );

        html.append(
                ".jogos-lista{"
                + "display:grid;"
                + "grid-template-columns:"
                + "repeat(auto-fill,minmax(155px,1fr));"
                + "gap:15px;"
                + "}"
        );

        html.append(
                ".jogo-lista{"
                + "background:#160d1e;"
                + "border:1px solid #34203d;"
                + "border-radius:13px;"
                + "padding:9px;"
                + "overflow:hidden;"
                + "transition:.25s;"
                + "}"
        );

        html.append(
                ".jogo-lista:hover{"
                + "transform:translateY(-4px);"
                + "border-color:#7c3aed;"
                + "}"
        );

        html.append(
                ".capa-lista{"
                + "width:100%;"
                + "height:205px;"
                + "object-fit:cover;"
                + "display:block;"
                + "border-radius:9px;"
                + "background:#24152f;"
                + "}"
        );

        html.append(
                ".sem-capa{"
                + "width:100%;"
                + "height:205px;"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:center;"
                + "background:"
                + "linear-gradient("
                + "135deg,#24152e,#110b16"
                + ");"
                + "border-radius:9px;"
                + "color:#777;"
                + "text-align:center;"
                + "}"
        );

        html.append(
                ".nome-jogo-lista{"
                + "font-size:14px;"
                + "font-weight:bold;"
                + "margin-top:9px;"
                + "line-height:1.3;"
                + "}"
        );

        html.append(
                ".form-adicionar-jogo{"
                + "display:flex;"
                + "gap:10px;"
                + "margin-top:20px;"
                + "}"
        );

        html.append(
                ".form-adicionar-jogo select{"
                + "flex:1;"
                + "padding:12px;"
                + "background:#100b15;"
                + "border:1px solid #47305a;"
                + "border-radius:9px;"
                + "color:white;"
                + "outline:none;"
                + "}"
        );

        html.append(
                ".botao-adicionar{"
                + "padding:12px 18px;"
                + "border:none;"
                + "border-radius:9px;"
                + "background:#6d28d9;"
                + "color:white;"
                + "font-weight:bold;"
                + "cursor:pointer;"
                + "}"
        );

        html.append(
                ".botao-adicionar:hover{"
                + "background:#7c3aed;"
                + "}"
        );

        html.append(
                ".vazio{"
                + "padding:30px;"
                + "border:1px dashed #4a3158;"
                + "border-radius:15px;"
                + "text-align:center;"
                + "color:#8e8295;"
                + "background:rgba(20,11,27,.5);"
                + "}"
        );

        html.append(
                "@media(max-width:650px){"
                + ".listas-container{"
                + "padding:10px;"
                + "margin:25px auto;"
                + "}"
                + ".listas-header{"
                + "padding:25px 18px;"
                + "}"
                + ".listas-header h2{"
                + "font-size:27px;"
                + "}"
                + ".form-criar-lista,"
                + ".form-adicionar-jogo{"
                + "flex-direction:column;"
                + "}"
                + ".lista{"
                + "padding:18px 14px;"
                + "}"
                + ".jogos-lista{"
                + "grid-template-columns:"
                + "repeat(2,1fr);"
                + "gap:10px;"
                + "}"
                + ".capa-lista,"
                + ".sem-capa{"
                + "height:190px;"
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
                "<a href='buscar-usuarios'>"
                + "Buscar usuários"
                + "</a>"
        );

        html.append(
                "<a href='listas'>"
                + "Listas"
                + "</a>"
        );

        html.append(
                "<a href='perfil'>"
                + "Meu Perfil"
                + "</a>"
        );

        html.append(
                "<a href='logout'>"
                + "Sair"
                + "</a>"
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
                + "Crie coleções personalizadas com seus jogos."
                + "</p>"
        );

        // =====================================================
        // CRIAR LISTA
        // =====================================================

        html.append(
                "<form "
                + "class='form-criar-lista' "
                + "method='POST' "
                + "action='criar-lista'>"
        );

        html.append(
                "<input "
                + "type='text' "
                + "name='nome' "
                + "maxlength='80' "
                + "placeholder='Nome da nova lista...' "
                + "required>"
        );

        html.append(
                "<button "
                + "class='botao-criar-lista' "
                + "type='submit'>"
                + "Criar lista"
                + "</button>"
        );

        html.append("</form>");

        html.append("</section>");

        // =====================================================
        // CONEXÃO
        // =====================================================

        Connection conexao = null;
        PreparedStatement stmtListas = null;
        PreparedStatement stmtJogos = null;
        PreparedStatement stmtItens = null;

        ResultSet rsListas = null;
        ResultSet rsJogos = null;
        ResultSet rsItens = null;

        try {

            conexao =
                    Conexao.conectar();

            if (conexao == null) {

                html.append(
                        "<div class='vazio'>"
                        + "Erro ao conectar ao banco."
                        + "</div>"
                );

            } else {

                // =================================================
                // GARANTIR TABELA LISTA
                // =================================================

                String criarLista =
                        "CREATE TABLE IF NOT EXISTS lista ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "id_usuario INTEGER NOT NULL,"
                        + "nome TEXT NOT NULL,"
                        + "data_criacao TEXT "
                        + "DEFAULT CURRENT_TIMESTAMP"
                        + ")";

                PreparedStatement tabelaLista =
                        conexao.prepareStatement(
                                criarLista
                        );

                tabelaLista.executeUpdate();
                tabelaLista.close();

                // =================================================
                // GARANTIR TABELA LISTA_JOGO
                // =================================================

                String criarListaJogo =
                        "CREATE TABLE IF NOT EXISTS lista_jogo ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "id_lista INTEGER NOT NULL,"
                        + "id_jogo INTEGER NOT NULL,"
                        + "data_adicionado TEXT "
                        + "DEFAULT CURRENT_TIMESTAMP,"
                        + "UNIQUE(id_lista,id_jogo)"
                        + ")";

                PreparedStatement tabelaListaJogo =
                        conexao.prepareStatement(
                                criarListaJogo
                        );

                tabelaListaJogo.executeUpdate();
                tabelaListaJogo.close();

                // =================================================
                // JOGOS DISPONÍVEIS
                // =================================================

                String sqlJogos =
                        "SELECT id,titulo "
                        + "FROM jogo "
                        + "ORDER BY titulo";

                stmtJogos =
                        conexao.prepareStatement(
                                sqlJogos
                        );

                rsJogos =
                        stmtJogos.executeQuery();

                StringBuilder opcoes =
                        new StringBuilder();

                while (
                        rsJogos.next()
                ) {

                    opcoes.append(
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
                rsJogos = null;

                stmtJogos.close();
                stmtJogos = null;

                // =================================================
                // BUSCAR LISTAS
                // =================================================

                String sqlListas =
                        "SELECT id,nome "
                        + "FROM lista "
                        + "WHERE id_usuario = ? "
                        + "ORDER BY id DESC";

                stmtListas =
                        conexao.prepareStatement(
                                sqlListas
                        );

                stmtListas.setInt(
                        1,
                        idUsuario
                );

                rsListas =
                        stmtListas.executeQuery();

                boolean possuiLista =
                        false;

                while (
                        rsListas.next()
                ) {

                    possuiLista =
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
                            "<div class='nome-lista'>"
                            + escapar(nomeLista)
                            + "</div>"
                    );

                    // =================================================
                    // EXCLUIR LISTA
                    // =================================================

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
                            + "return confirm('Excluir esta lista?');"
                            + "\">"
                            + "Excluir"
                            + "</button>"
                    );

                    html.append("</form>");

                    html.append("</div>");

                    // =================================================
                    // JOGOS DA LISTA
                    // =================================================

                    String sqlItens =
                            "SELECT "
                            + "j.id,"
                            + "j.titulo,"
                            + "j.capa "
                            + "FROM lista_jogo lj "
                            + "INNER JOIN jogo j "
                            + "ON j.id = lj.id_jogo "
                            + "WHERE lj.id_lista = ? "
                            + "ORDER BY lj.id ASC";

                    stmtItens =
                            conexao.prepareStatement(
                                    sqlItens
                            );

                    stmtItens.setInt(
                            1,
                            idLista
                    );

                    rsItens =
                            stmtItens.executeQuery();

                    boolean possuiJogos =
                            false;

                    html.append(
                            "<div class='jogos-lista'>"
                    );

                    while (
                            rsItens.next()
                    ) {

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
                                    + "' "
                                    + "onerror=\""
                                    + "this.style.display='none';"
                                    + "this.nextElementSibling"
                                    + ".style.display='flex';"
                                    + "\">"
                            );

                            html.append(
                                    "<div "
                                    + "class='sem-capa' "
                                    + "style='display:none;'>"
                                    + "Capa indisponível"
                                    + "</div>"
                            );

                        } else {

                            html.append(
                                    "<div "
                                    + "class='sem-capa'>"
                                    + "Sem capa"
                                    + "</div>"
                            );
                        }

                        html.append(
                                "<div "
                                + "class='nome-jogo-lista'>"
                                + escapar(titulo)
                                + "</div>"
                        );

                        html.append(
                                "</article>"
                        );
                    }

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

                    if (rsItens != null) {
                        rsItens.close();
                        rsItens = null;
                    }

                    if (stmtItens != null) {
                        stmtItens.close();
                        stmtItens = null;
                    }

                    // =================================================
                    // ADICIONAR JOGO
                    // =================================================

                    html.append(
                            "<form "
                            + "class='form-adicionar-jogo' "
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
                            + "Escolha um jogo..."
                            + "</option>"
                    );

                    html.append(
                            opcoes.toString()
                    );

                    html.append("</select>");

                    html.append(
                            "<button "
                            + "class='botao-adicionar' "
                            + "type='submit'>"
                            + "Adicionar"
                            + "</button>"
                    );

                    html.append("</form>");

                    html.append("</section>");
                }

                if (!possuiLista) {

                    html.append(
                            "<div class='vazio'>"
                            + "Você ainda não criou nenhuma lista."
                            + "</div>"
                    );
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            html.append(
                    "<div class='vazio'>"
                    + "Erro ao carregar suas listas."
                    + "</div>"
            );

        } finally {

            try {
                if (rsItens != null) {
                    rsItens.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (rsListas != null) {
                    rsListas.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (rsJogos != null) {
                    rsJogos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (stmtItens != null) {
                    stmtItens.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (stmtListas != null) {
                    stmtListas.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (stmtJogos != null) {
                    stmtJogos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (conexao != null) {
                    conexao.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        html.append("</main>");
        html.append("</body>");
        html.append("</html>");

        response.getWriter().println(
                html.toString()
        );
    }

    // =====================================================
    // PREPARAR CAPA
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

        // =================================================
        // URL COMPLETA
        // NÃO MODIFICAR
        // =================================================

        if (caminho.startsWith("http://")
                ||
                caminho.startsWith("https://")) {

            return caminho;
        }

        // =================================================
        // MARKDOWN
        // [texto](URL)
        // =================================================

        if (caminho.startsWith("[")
                &&
                caminho.contains("](")
                &&
                caminho.endsWith(")")) {

            int posicao =
                    caminho.indexOf("](");

            caminho =
                    caminho.substring(
                            posicao + 2,
                            caminho.length() - 1
                    );

            if (caminho.startsWith("http://")
                    ||
                    caminho.startsWith("https://")) {

                return caminho;
            }
        }

        // =================================================
        // SOMENTE ID STEAM
        // =================================================

        if (caminho.matches("\\d+")) {

            return
                    "https://cdn.akamai.steamstatic.com/"
                    + "steam/apps/"
                    + caminho
                    + "/library_600x900_2x.jpg";
        }

        // =================================================
        // URL STEAM COM /apps/ID
        // =================================================

        Pattern pattern =
                Pattern.compile(
                        "/apps/(\\d+)"
                );

        Matcher matcher =
                pattern.matcher(
                        caminho
                );

        if (matcher.find()) {

            return
                    "https://cdn.akamai.steamstatic.com/"
                    + "steam/apps/"
                    + matcher.group(1)
                    + "/library_600x900_2x.jpg";
        }

        // =================================================
        // CAMINHO LOCAL
        // =================================================

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

    // =====================================================
    // ESCAPAR HTML
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