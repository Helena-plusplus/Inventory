
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

        html.append("<meta charset='UTF-8'>");

        html.append(
                "<meta name='viewport' " +
                "content='width=device-width, initial-scale=1.0'>"
        );

        html.append(
                "<title>Biblioteca - Inventory</title>"
        );

        html.append(
                "<link rel='stylesheet' href='style.css'>"
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
                + "background:"
                + "radial-gradient(circle at top,#32105f 0%,#12051f 45%,#09050d 100%);"
                + "min-height:100vh;"
                + "}"
        );

        html.append(
                ".biblioteca-container {"
                + "max-width:1200px;"
                + "margin:0 auto;"
                + "padding:45px 25px 70px;"
                + "}"
        );

        // =====================================================
        // HERO
        // =====================================================

        html.append(
                ".biblioteca-hero {"
                + "position:relative;"
                + "padding:40px;"
                + "margin-bottom:35px;"
                + "border-radius:22px;"
                + "overflow:hidden;"
                + "background:"
                + "linear-gradient(135deg,#291044,#4b1680,#241034);"
                + "border:1px solid rgba(177,92,255,.3);"
                + "box-shadow:0 15px 45px rgba(0,0,0,.4);"
                + "}"
        );

        html.append(
                ".biblioteca-hero:before {"
                + "content:'';"
                + "position:absolute;"
                + "width:250px;"
                + "height:250px;"
                + "border-radius:50%;"
                + "background:rgba(180,80,255,.18);"
                + "right:-70px;"
                + "top:-90px;"
                + "filter:blur(5px);"
                + "}"
        );

        html.append(
                ".biblioteca-hero h2 {"
                + "font-size:34px;"
                + "margin:0 0 10px;"
                + "position:relative;"
                + "}"
        );

        html.append(
                ".biblioteca-hero p {"
                + "color:#d7c9e5;"
                + "font-size:16px;"
                + "position:relative;"
                + "}"
        );

        // =====================================================
        // ABAS
        // =====================================================

        html.append(
                ".abas {"
                + "display:flex;"
                + "gap:12px;"
                + "margin:30px 0 45px;"
                + "flex-wrap:wrap;"
                + "}"
        );

        html.append(
                ".aba {"
                + "display:flex;"
                + "align-items:center;"
                + "gap:8px;"
                + "padding:13px 20px;"
                + "background:rgba(35,20,48,.9);"
                + "border:1px solid #492260;"
                + "color:#ddd;"
                + "text-decoration:none;"
                + "border-radius:12px;"
                + "font-weight:bold;"
                + "transition:.25s;"
                + "box-shadow:0 5px 15px rgba(0,0,0,.2);"
                + "}"
        );

        html.append(
                ".aba:hover {"
                + "transform:translateY(-3px);"
                + "background:#6f20a8;"
                + "border-color:#a855f7;"
                + "color:white;"
                + "box-shadow:0 8px 25px rgba(168,85,247,.3);"
                + "}"
        );

        // =====================================================
        // SEÇÕES
        // =====================================================

        html.append(
                ".secao-biblioteca {"
                + "margin-bottom:65px;"
                + "scroll-margin-top:30px;"
                + "}"
        );

        html.append(
                ".titulo-secao {"
                + "display:flex;"
                + "align-items:center;"
                + "gap:12px;"
                + "margin-bottom:8px;"
                + "}"
        );

        html.append(
                ".titulo-secao h2 {"
                + "font-size:27px;"
                + "margin:0;"
                + "}"
        );

        html.append(
                ".linha-roxa {"
                + "height:3px;"
                + "width:65px;"
                + "background:linear-gradient(90deg,#8b2be2,#c084fc);"
                + "border-radius:10px;"
                + "margin:12px 0 25px;"
                + "}"
        );

        // =====================================================
        // CATÁLOGO
        // =====================================================

        html.append(
                ".catalogo {"
                + "display:grid;"
                + "grid-template-columns:repeat(auto-fill,minmax(205px,1fr));"
                + "gap:25px;"
                + "}"
        );

        // =====================================================
        // CARD
        // =====================================================

        html.append(
                ".card-jogo {"
                + "position:relative;"
                + "overflow:hidden;"
                + "background:linear-gradient(145deg,#21152c,#140d1b);"
                + "border:1px solid #382047;"
                + "padding:13px;"
                + "border-radius:16px;"
                + "text-align:center;"
                + "transition:.3s;"
                + "box-shadow:0 10px 25px rgba(0,0,0,.3);"
                + "}"
        );

        html.append(
                ".card-jogo:hover {"
                + "transform:translateY(-8px);"
                + "border-color:#9146d4;"
                + "box-shadow:0 15px 35px rgba(139,43,226,.25);"
                + "}"
        );

        html.append(
                ".capa {"
                + "width:100%;"
                + "height:285px;"
                + "object-fit:cover;"
                + "border-radius:11px;"
                + "display:block;"
                + "transition:.4s;"
                + "}"
        );

        html.append(
                ".card-jogo:hover .capa {"
                + "transform:scale(1.04);"
                + "}"
        );

        html.append(
                ".card-jogo h3 {"
                + "font-size:18px;"
                + "margin:15px 5px 8px;"
                + "color:#fff;"
                + "}"
        );

        html.append(
                ".card-jogo p {"
                + "color:#aaa;"
                + "margin:6px;"
                + "}"
        );

        // =====================================================
        // SEM CAPA
        // =====================================================

        html.append(
                ".sem-capa {"
                + "height:285px;"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:center;"
                + "background:linear-gradient(135deg,#24152e,#110b16);"
                + "border-radius:11px;"
                + "color:#777;"
                + "border:1px dashed #56356a;"
                + "}"
        );

        // =====================================================
        // ESTRELAS
        // =====================================================

        html.append(
                ".estrelas {"
                + "color:#ffd166;"
                + "font-size:21px;"
                + "margin:10px 0;"
                + "text-shadow:0 0 10px rgba(255,209,102,.25);"
                + "}"
        );

        // =====================================================
        // HORAS
        // =====================================================

        html.append(
                ".horas {"
                + "display:inline-block;"
                + "color:#c7b5d6;"
                + "background:#21152b;"
                + "padding:7px 10px;"
                + "border-radius:20px;"
                + "font-size:13px;"
                + "margin-top:8px;"
                + "}"
        );

        // =====================================================
        // RESENHA
        // =====================================================

        html.append(
                ".resenha {"
                + "background:#120b18;"
                + "border-left:3px solid #8b2be2;"
                + "padding:12px;"
                + "border-radius:8px;"
                + "text-align:left;"
                + "margin-top:12px;"
                + "color:#ccc;"
                + "font-size:14px;"
                + "line-height:1.5;"
                + "}"
        );

        // =====================================================
        // BOTÕES
        // =====================================================

        html.append(
                ".botoes-status {"
                + "display:flex;"
                + "flex-direction:column;"
                + "gap:9px;"
                + "margin-top:16px;"
                + "}"
        );

        html.append(
                ".botao {"
                + "display:block;"
                + "padding:11px;"
                + "background:linear-gradient(135deg,#7020a8,#942fe0);"
                + "color:white;"
                + "text-decoration:none;"
                + "border-radius:9px;"
                + "font-weight:bold;"
                + "font-size:14px;"
                + "transition:.25s;"
                + "}"
        );

        html.append(
                ".botao:hover {"
                + "transform:translateY(-2px);"
                + "filter:brightness(1.15);"
                + "box-shadow:0 7px 18px rgba(148,47,224,.3);"
                + "}"
        );

        html.append(
                ".botao-verde {"
                + "background:linear-gradient(135deg,#087f55,#10a36d);"
                + "}"
        );

        html.append(
                ".botao-avaliar {"
                + "background:linear-gradient(135deg,#8b3f00,#d97706);"
                + "}"
        );

        // =====================================================
        // VAZIO
        // =====================================================

        html.append(
                ".vazio {"
                + "background:linear-gradient(145deg,#1c1225,#120b18);"
                + "border:1px dashed #56356a;"
                + "padding:35px;"
                + "border-radius:15px;"
                + "text-align:center;"
                + "color:#aaa;"
                + "}"
        );

        // =====================================================
        // RESPONSIVO
        // =====================================================

        html.append(
                "@media(max-width:600px){"
                + ".biblioteca-container{padding:25px 15px 50px;}"
                + ".biblioteca-hero{padding:28px 22px;}"
                + ".biblioteca-hero h2{font-size:27px;}"
                + ".catalogo{grid-template-columns:repeat(2,1fr);gap:12px;}"
                + ".capa,.sem-capa{height:220px;}"
                + ".card-jogo{padding:9px;}"
                + ".card-jogo h3{font-size:15px;}"
                + ".aba{font-size:13px;padding:10px 13px;}"
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
                "<main class='biblioteca-container'>"
        );

        html.append(
                "<section class='biblioteca-hero'>"
        );

        html.append(
                "<h2>🎮 Minha Biblioteca</h2>"
        );

        html.append(
                "<p>"
                + "Organize sua coleção, acompanhe o que está jogando "
                + "e registre os jogos que já zerou."
                + "</p>"
        );

        html.append("</section>");

        // =====================================================
        // ABAS
        // =====================================================

        html.append("<div class='abas'>");

        html.append(
                "<a class='aba' href='#jogando'>"
                + "🎮 Jogando"
                + "</a>"
        );

        html.append(
                "<a class='aba' href='#zerados'>"
                + "🏆 Zerados"
                + "</a>"
        );

        html.append(
                "<a class='aba' href='#quero'>"
                + "📚 Quero jogar"
                + "</a>"
        );

        html.append("</div>");

        // =====================================================
        // JOGANDO
        // =====================================================

        html.append(
                "<section id='jogando' class='secao-biblioteca'>"
        );

        html.append(
                "<div class='titulo-secao'>"
                + "<h2>🎮 Jogando</h2>"
                + "</div>"
        );

        html.append(
                "<div class='linha-roxa'></div>"
        );

        html.append("<div class='catalogo'>");

        int quantidadeJogando = 0;

        try {

            Connection conexao =
                    Conexao.conectar();

            String sql =
                    "SELECT j.id, j.titulo, j.genero, "
                    + "j.plataforma, j.capa "
                    + "FROM biblioteca b "
                    + "INNER JOIN jogo j ON j.id = b.id_jogo "
                    + "WHERE b.id_usuario = ? "
                    + "AND b.status = 'jogando' "
                    + "ORDER BY j.titulo";

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(1, idUsuario);

            ResultSet rs =
                    stmt.executeQuery();

            while (rs.next()) {

                quantidadeJogando++;

                int id =
                        rs.getInt("id");

                String titulo =
                        rs.getString("titulo");

                String genero =
                        rs.getString("genero");

                String plataforma =
                        rs.getString("plataforma");

                String capa =
                        rs.getString("capa");

                html.append(
                        "<article class='card-jogo'>"
                );

                adicionarCapa(
                        html,
                        request,
                        capa,
                        titulo
                );

                html.append(
                        "<h3>"
                        + escapar(titulo)
                        + "</h3>"
                );

                html.append(
                        "<p>"
                        + escapar(genero)
                        + "</p>"
                );

                html.append(
                        "<p>🎮 "
                        + escapar(plataforma)
                        + "</p>"
                );

                html.append(
                        "<div class='botoes-status'>"
                );

                html.append(
                        "<a class='botao botao-avaliar' "
                        + "href='avaliar?id="
                        + id
                        + "'>"
                        + "⭐ Avaliar e zerar"
                        + "</a>"
                );

                html.append(
                        "<a class='botao' "
                        + "href='status-jogo?id="
                        + id
                        + "&status=quero%20jogar'>"
                        + "📚 Quero jogar"
                        + "</a>"
                );

                html.append("</div>");

                html.append("</article>");
            }

            rs.close();
            stmt.close();
            conexao.close();

        } catch (Exception e) {

            e.printStackTrace();

            html.append(
                    "<p>Erro ao carregar jogos.</p>"
            );
        }

        html.append("</div>");

        if (quantidadeJogando == 0) {

            html.append(
                    "<div class='vazio'>"
                    + "🎮<br><br>"
                    + "Você não está jogando nenhum jogo no momento."
                    + "</div>"
            );
        }

        html.append("</section>");

        // =====================================================
        // ZERADOS
        // =====================================================

        html.append(
                "<section id='zerados' class='secao-biblioteca'>"
        );

        html.append(
                "<div class='titulo-secao'>"
                + "<h2>🏆 Zerados</h2>"
                + "</div>"
        );

        html.append(
                "<div class='linha-roxa'></div>"
        );

        html.append("<div class='catalogo'>");

        int quantidadeZerados = 0;

        try {

            Connection conexao =
                    Conexao.conectar();

            String sql =
                    "SELECT j.id, j.titulo, j.genero, "
                    + "j.capa, a.nota, a.comentario, "
                    + "b.horas_jogadas "
                    + "FROM avaliacao a "
                    + "INNER JOIN jogo j ON j.id = a.id_jogo "
                    + "LEFT JOIN biblioteca b "
                    + "ON b.id_usuario = a.id_usuario "
                    + "AND b.id_jogo = a.id_jogo "
                    + "WHERE a.id_usuario = ? "
                    + "AND b.status = 'zerado' "
                    + "ORDER BY a.data_avaliacao DESC";

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(1, idUsuario);

            ResultSet rs =
                    stmt.executeQuery();

            while (rs.next()) {

                quantidadeZerados++;

                int id =
                        rs.getInt("id");

                String titulo =
                        rs.getString("titulo");

                String genero =
                        rs.getString("genero");

                String capa =
                        rs.getString("capa");

                double nota =
                        rs.getDouble("nota");

                String comentario =
                        rs.getString("comentario");

                double horas =
                        rs.getDouble("horas_jogadas");

                html.append(
                        "<article class='card-jogo'>"
                );

                adicionarCapa(
                        html,
                        request,
                        capa,
                        titulo
                );

                html.append(
                        "<h3>"
                        + escapar(titulo)
                        + "</h3>"
                );

                html.append(
                        "<p>"
                        + escapar(genero)
                        + "</p>"
                );

                html.append(
                        "<div class='estrelas'>"
                );

                int estrelas =
                        (int) Math.round(nota);

                for (int i = 1; i <= 5; i++) {

                    html.append(
                            i <= estrelas
                            ? "★"
                            : "☆"
                    );
                }

                html.append(
                        " " + nota + "/5"
                );

                html.append("</div>");

                html.append(
                        "<div class='horas'>"
                        + "⏱️ "
                        + horas
                        + " horas jogadas"
                        + "</div>"
                );

                if (comentario != null &&
                        !comentario.trim().isEmpty()) {

                    html.append(
                            "<div class='resenha'>"
                            + "<strong>💬 Resenha</strong><br>"
                            + escapar(comentario)
                            + "</div>"
                    );
                }

                html.append(
                        "<div class='botoes-status'>"
                );

                html.append(
                        "<a class='botao' "
                        + "href='avaliar?id="
                        + id
                        + "'>"
                        + "✏️ Editar avaliação"
                        + "</a>"
                );

                html.append("</div>");

                html.append("</article>");
            }

            rs.close();
            stmt.close();
            conexao.close();

        } catch (Exception e) {

            e.printStackTrace();

            html.append(
                    "<p>Erro ao carregar Zerados.</p>"
            );
        }

        html.append("</div>");

        if (quantidadeZerados == 0) {

            html.append(
                    "<div class='vazio'>"
                    + "🏆<br><br>"
                    + "Você ainda não zerou nenhum jogo."
                    + "</div>"
            );
        }

        html.append("</section>");

        // =====================================================
        // QUERO JOGAR
        // =====================================================

        html.append(
                "<section id='quero' class='secao-biblioteca'>"
        );

        html.append(
                "<div class='titulo-secao'>"
                + "<h2>📚 Quero jogar</h2>"
                + "</div>"
        );

        html.append(
                "<div class='linha-roxa'></div>"
        );

        html.append("<div class='catalogo'>");

        int quantidadeQuero = 0;

        try {

            Connection conexao =
                    Conexao.conectar();

            String sql =
                    "SELECT j.id, j.titulo, j.genero, "
                    + "j.plataforma, j.capa "
                    + "FROM biblioteca b "
                    + "INNER JOIN jogo j ON j.id = b.id_jogo "
                    + "WHERE b.id_usuario = ? "
                    + "AND b.status = 'quero jogar' "
                    + "ORDER BY j.titulo";

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(1, idUsuario);

            ResultSet rs =
                    stmt.executeQuery();

            while (rs.next()) {

                quantidadeQuero++;

                int id =
                        rs.getInt("id");

                String titulo =
                        rs.getString("titulo");

                String genero =
                        rs.getString("genero");

                String plataforma =
                        rs.getString("plataforma");

                String capa =
                        rs.getString("capa");

                html.append(
                        "<article class='card-jogo'>"
                );

                adicionarCapa(
                        html,
                        request,
                        capa,
                        titulo
                );

                html.append(
                        "<h3>"
                        + escapar(titulo)
                        + "</h3>"
                );

                html.append(
                        "<p>"
                        + escapar(genero)
                        + "</p>"
                );

                html.append(
                        "<p>🎮 "
                        + escapar(plataforma)
                        + "</p>"
                );

                html.append(
                        "<div class='botoes-status'>"
                );

                html.append(
                        "<a class='botao botao-verde' "
                        + "href='status-jogo?id="
                        + id
                        + "&status=jogando'>"
                        + "🎮 Começar a jogar"
                        + "</a>"
                );

                html.append("</div>");

                html.append("</article>");
            }

            rs.close();
            stmt.close();
            conexao.close();

        } catch (Exception e) {

            e.printStackTrace();

            html.append(
                    "<p>Erro ao carregar biblioteca.</p>"
            );
        }

        html.append("</div>");

        if (quantidadeQuero == 0) {

            html.append(
                    "<div class='vazio'>"
                    + "📚<br><br>"
                    + "Sua lista de jogos desejados está vazia."
                    + "</div>"
            );
        }

        html.append("</section>");

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

    private void adicionarCapa(
            StringBuilder html,
            HttpServletRequest request,
            String capa,
            String titulo) {

        if (capa != null &&
                !capa.trim().isEmpty()) {

            String caminhoCapa;

            if (capa.startsWith("http://") ||
                    capa.startsWith("https://")) {

                caminhoCapa = capa;

            } else {

                caminhoCapa =
                        request.getContextPath()
                        + "/"
                        + capa;
            }

            html.append(
                    "<img class='capa' "
                    + "src='"
                    + caminhoCapa
                    + "' "
                    + "alt='Capa de "
                    + escapar(titulo)
                    + "'>"
            );

        } else {

            html.append(
                    "<div class='sem-capa'>"
                    + "🎮<br>Sem capa"
                    + "</div>"
            );
        }
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

