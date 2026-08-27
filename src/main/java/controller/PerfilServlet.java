
package controller;

import dao.Conexao;
import model.Usuario;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/perfil")
public class PerfilServlet extends HttpServlet {

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

        String foto =
                usuario.getFoto();

        String caminhoFoto = "";

        if (foto != null &&
                !foto.trim().isEmpty()) {

            String fotoLimpa =
                    foto.trim();

            if (fotoLimpa.startsWith("http://") ||
                    fotoLimpa.startsWith("https://")) {

                caminhoFoto =
                        fotoLimpa;

            } else {

                caminhoFoto =
                        request.getContextPath()
                        + "/foto-perfil?arquivo="
                        + URLEncoder.encode(
                                fotoLimpa,
                                "UTF-8"
                        );
            }
        }

        StringBuilder html =
                new StringBuilder();

        html.append("<!DOCTYPE html>");
        html.append("<html lang='pt-BR'>");

        html.append("<head>");

        html.append("<meta charset='UTF-8'>");

        html.append(
                "<meta name='viewport' "
                + "content='width=device-width, initial-scale=1.0'>"
        );

        html.append(
                "<title>Meu Perfil - Inventory</title>"
        );

        html.append(
                "<link rel='stylesheet' href='style.css'>"
        );

        html.append("<style>");

        html.append(
                "body{"
                + "background:"
                + "radial-gradient(circle at top,#3b1260 0%,#180d23 38%,#0b0710 100%);"
                + "min-height:100vh;"
                + "}"
        );

        html.append(
                ".perfil-container{"
                + "max-width:1050px;"
                + "margin:40px auto 70px;"
                + "padding:20px;"
                + "}"
        );

        html.append(
                ".perfil-capa{"
                + "height:210px;"
                + "border-radius:24px;"
                + "background:linear-gradient(135deg,#4c1d95,#7c3aed,#a855f7,#301050);"
                + "position:relative;"
                + "overflow:hidden;"
                + "}"
        );

        html.append(
                ".perfil-card{"
                + "margin-top:-75px;"
                + "position:relative;"
                + "background:linear-gradient(145deg,#21152c,#130b19);"
                + "border:1px solid #45265c;"
                + "border-radius:24px;"
                + "padding:35px;"
                + "box-shadow:0 20px 60px rgba(0,0,0,.45);"
                + "}"
        );

        html.append(
                ".foto-wrapper{"
                + "display:flex;"
                + "justify-content:center;"
                + "margin-top:-115px;"
                + "}"
        );

        html.append(
                ".foto-perfil{"
                + "width:185px;"
                + "height:185px;"
                + "border-radius:50%;"
                + "object-fit:cover;"
                + "border:6px solid #1a1022;"
                + "box-shadow:0 0 0 5px #8b5cf6,0 0 40px rgba(139,92,246,.55);"
                + "background:#251532;"
                + "}"
        );

        html.append(
                ".sem-foto{"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:center;"
                + "font-size:50px;"
                + "color:#9c8cab;"
                + "}"
        );

        html.append(
                ".perfil-topo{text-align:center;}"
        );

        html.append(
                ".nome-perfil{"
                + "font-size:32px;"
                + "margin:18px 0 3px;"
                + "color:#fff;"
                + "}"
        );

        html.append(
                ".username-perfil{"
                + "color:#c084fc;"
                + "font-size:16px;"
                + "font-weight:bold;"
                + "}"
        );

        html.append(
                ".bio-destaque{"
                + "max-width:650px;"
                + "margin:15px auto;"
                + "color:#b7adbf;"
                + "line-height:1.6;"
                + "}"
        );

        html.append(
                ".botao-editar{"
                + "display:inline-flex;"
                + "margin-top:20px;"
                + "padding:12px 22px;"
                + "background:linear-gradient(135deg,#7c3aed,#a855f7);"
                + "color:white;"
                + "text-decoration:none;"
                + "border-radius:10px;"
                + "font-weight:bold;"
                + "}"
        );

        html.append(
                ".estatisticas{"
                + "display:grid;"
                + "grid-template-columns:repeat(3,1fr);"
                + "gap:15px;"
                + "margin:35px 0;"
                + "}"
        );

        html.append(
                ".estatistica{"
                + "background:#160d1f;"
                + "border:1px solid #352044;"
                + "padding:18px;"
                + "border-radius:14px;"
                + "text-align:center;"
                + "}"
        );

        html.append(
                ".numero{"
                + "display:block;"
                + "font-size:27px;"
                + "font-weight:800;"
                + "color:#c084fc;"
                + "}"
        );

        html.append(
                ".label{"
                + "display:block;"
                + "font-size:12px;"
                + "color:#91869a;"
                + "margin-top:5px;"
                + "}"
        );

        html.append(
                ".dados-perfil{"
                + "display:grid;"
                + "grid-template-columns:repeat(2,1fr);"
                + "gap:15px;"
                + "margin-top:25px;"
                + "}"
        );

        html.append(
                ".dado{"
                + "background:#160d1f;"
                + "border:1px solid #352044;"
                + "padding:18px;"
                + "border-radius:14px;"
                + "}"
        );

        html.append(
                ".dado strong{"
                + "display:block;"
                + "color:#a855f7;"
                + "font-size:12px;"
                + "margin-bottom:7px;"
                + "}"
        );

        html.append(
                ".dado span{"
                + "color:#e4dde9;"
                + "}"
        );

        html.append(
                ".dado-bio{grid-column:1/-1;}"
        );

        html.append(
                ".avaliacoes{margin-top:45px;}"
        );

        html.append(
                ".avaliacao-card{"
                + "display:flex;"
                + "gap:20px;"
                + "background:linear-gradient(145deg,#1d1226,#140c18);"
                + "border:1px solid #352044;"
                + "padding:20px;"
                + "border-radius:16px;"
                + "margin-top:16px;"
                + "}"
        );

        html.append(
                ".capa-avaliacao{"
                + "width:105px;"
                + "height:145px;"
                + "object-fit:cover;"
                + "border-radius:9px;"
                + "flex-shrink:0;"
                + "}"
        );

        html.append(
                ".sem-capa-avaliacao{"
                + "width:105px;"
                + "height:145px;"
                + "border-radius:9px;"
                + "background:#251532;"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:center;"
                + "color:#888;"
                + "}"
        );

        html.append(
                ".texto-avaliacao{flex:1;}"
        );

        html.append(
                ".texto-avaliacao h3{"
                + "font-size:20px;"
                + "margin:0 0 8px;"
                + "color:white;"
                + "}"
        );

        html.append(
                ".estrelas-perfil{"
                + "color:#c084fc;"
                + "font-size:21px;"
                + "letter-spacing:2px;"
                + "}"
        );

        html.append(
                ".texto-avaliacao p{"
                + "color:#aaa;"
                + "line-height:1.55;"
                + "}"
        );

        html.append(
                ".sem-avaliacoes{"
                + "background:#160d1f;"
                + "border:1px dashed #4a2d5e;"
                + "padding:35px;"
                + "border-radius:15px;"
                + "text-align:center;"
                + "color:#888;"
                + "}"
        );

        html.append(
                "@media(max-width:700px){"
                + ".perfil-container{padding:10px;}"
                + ".perfil-card{padding:22px;margin-top:-45px;}"
                + ".perfil-capa{height:150px;}"
                + ".foto-wrapper{margin-top:-85px;}"
                + ".foto-perfil{width:140px;height:140px;}"
                + ".estatisticas{grid-template-columns:1fr;}"
                + ".dados-perfil{grid-template-columns:1fr;}"
                + ".dado-bio{grid-column:auto;}"
                + ".avaliacao-card{flex-direction:column;}"
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

        html.append("<a href='index.html'>Início</a>");
        html.append("<a href='jogos'>Jogos</a>");
        html.append("<a href='biblioteca'>Biblioteca</a>");
        html.append(
                "<a href='buscar-usuarios.html'>Buscar usuários</a>"
        );
        html.append("<a href='perfil'>Meu Perfil</a>");
        html.append("<a href='logout'>Sair</a>");

        html.append("</nav>");
        html.append("</header>");

        // =====================================================
        // PERFIL
        // =====================================================

        html.append("<main class='perfil-container'>");

        html.append("<div class='perfil-capa'></div>");

        html.append("<div class='perfil-card'>");

        html.append("<section class='perfil-topo'>");

        html.append("<div class='foto-wrapper'>");

        if (!caminhoFoto.isEmpty()) {

            html.append(
                    "<img class='foto-perfil' "
                    + "src='" + escapar(caminhoFoto) + "' "
                    + "alt='Foto de perfil'>"
            );

        } else {

            html.append(
                    "<div class='foto-perfil sem-foto'>"
                    + "👤"
                    + "</div>"
            );
        }

        html.append("</div>");

        html.append(
                "<h2 class='nome-perfil'>"
                + escapar(usuario.getNome())
                + "</h2>"
        );

        html.append(
                "<div class='username-perfil'>@"
                + escapar(usuario.getUsername())
                + "</div>"
        );

        if (usuario.getBio() != null &&
                !usuario.getBio().trim().isEmpty()) {

            html.append(
                    "<p class='bio-destaque'>"
                    + escapar(usuario.getBio())
                    + "</p>"
            );
        }

        html.append(
                "<a class='botao-editar' "
                + "href='editar-perfil'>"
                + "✏️ Editar perfil"
                + "</a>"
        );

        html.append("</section>");

        // =====================================================
        // CONTAR AVALIAÇÕES
        // =====================================================

        int quantidadeAvaliacoes = 0;

        try {

            Connection conexao =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conexao.prepareStatement(
                            "SELECT COUNT(*) "
                            + "FROM avaliacao "
                            + "WHERE id_usuario = ?"
                    );

            stmt.setInt(1, idUsuario);

            ResultSet rs =
                    stmt.executeQuery();

            if (rs.next()) {

                quantidadeAvaliacoes =
                        rs.getInt(1);
            }

            rs.close();
            stmt.close();
            conexao.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        // =====================================================
        // ESTATÍSTICAS
        // =====================================================

        html.append("<section class='estatisticas'>");

        html.append(
                "<div class='estatistica'>"
                + "<span class='numero'>"
                + quantidadeAvaliacoes
                + "</span>"
                + "<span class='label'>Avaliações</span>"
                + "</div>"
        );

        html.append(
                "<div class='estatistica'>"
                + "<span class='numero'>🎮</span>"
                + "<span class='label'>Jogador</span>"
                + "</div>"
        );

        html.append(
                "<div class='estatistica'>"
                + "<span class='numero'>⭐</span>"
                + "<span class='label'>Reviews</span>"
                + "</div>"
        );

        html.append("</section>");

        // =====================================================
        // DADOS
        // =====================================================

        html.append("<section class='dados-perfil'>");

        html.append(
                "<div class='dado'>"
                + "<strong>📧 E-mail</strong>"
                + "<span>"
                + escapar(usuario.getEmail())
                + "</span>"
                + "</div>"
        );

        html.append(
                "<div class='dado'>"
                + "<strong>🌎 País</strong>"
                + "<span>"
                + (
                        usuario.getPais() != null &&
                        !usuario.getPais().trim().isEmpty()
                        ? escapar(usuario.getPais())
                        : "Não informado"
                )
                + "</span>"
                + "</div>"
        );

        html.append(
                "<div class='dado'>"
                + "<strong>🎮 Plataforma favorita</strong>"
                + "<span>"
                + (
                        usuario.getPlataformaFavorita() != null &&
                        !usuario.getPlataformaFavorita().trim().isEmpty()
                        ? escapar(
                                usuario.getPlataformaFavorita()
                        )
                        : "Não informada"
                )
                + "</span>"
                + "</div>"
        );

        html.append(
                "<div class='dado dado-bio'>"
                + "<strong>📝 Sobre mim</strong>"
                + "<span>"
                + (
                        usuario.getBio() != null &&
                        !usuario.getBio().trim().isEmpty()
                        ? escapar(usuario.getBio())
                        : "Você ainda não adicionou uma biografia."
                )
                + "</span>"
                + "</div>"
        );

        html.append("</section>");

        // =====================================================
        // AVALIAÇÕES
        // =====================================================

        html.append("<section class='avaliacoes'>");

        html.append(
                "<h2>⭐ Minhas avaliações</h2>"
        );

        try {

            Connection conexao =
                    Conexao.conectar();

            String sql =
                    "SELECT "
                    + "jogo.titulo, "
                    + "jogo.capa, "
                    + "avaliacao.nota, "
                    + "avaliacao.comentario "
                    + "FROM avaliacao "
                    + "INNER JOIN jogo "
                    + "ON avaliacao.id_jogo = jogo.id "
                    + "WHERE avaliacao.id_usuario = ? "
                    + "ORDER BY avaliacao.data_avaliacao DESC";

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(1, idUsuario);

            ResultSet resultado =
                    stmt.executeQuery();

            boolean possuiAvaliacao =
                    false;

            while (resultado.next()) {

                possuiAvaliacao = true;

                String titulo =
                        resultado.getString("titulo");

                String capa =
                        resultado.getString("capa");

                double nota =
                        resultado.getDouble("nota");

                String comentario =
                        resultado.getString("comentario");

                html.append(
                        "<article class='avaliacao-card'>"
                );

                if (capa != null &&
                        !capa.trim().isEmpty()) {

                    String caminhoCapa =
                            capa.trim();

                    if (!caminhoCapa.startsWith("http://") &&
                        !caminhoCapa.startsWith("https://")) {

                        while (
                                caminhoCapa.startsWith("/")
                        ) {

                            caminhoCapa =
                                    caminhoCapa.substring(1);
                        }

                        caminhoCapa =
                                request.getContextPath()
                                + "/"
                                + caminhoCapa;
                    }

                    html.append(
                            "<img class='capa-avaliacao' "
                            + "src='" + escapar(caminhoCapa) + "' "
                            + "alt='Capa'>"
                    );

                } else {

                    html.append(
                            "<div class='sem-capa-avaliacao'>"
                            + "🎮"
                            + "</div>"
                    );
                }

                html.append(
                        "<div class='texto-avaliacao'>"
                );

                html.append(
                        "<h3>"
                        + escapar(titulo)
                        + "</h3>"
                );

                html.append(
                        "<div class='estrelas-perfil'>"
                );

                int estrelas =
                        (int) Math.round(nota);

                if (estrelas < 0) estrelas = 0;
                if (estrelas > 5) estrelas = 5;

                for (int i = 1; i <= 5; i++) {

                    html.append(
                            i <= estrelas
                            ? "★"
                            : "☆"
                    );
                }

                html.append("</div>");

                html.append(
                        "<p><strong>Nota:</strong> "
                        + nota
                        + "/5</p>"
                );

                html.append(
                        "<p><strong>Resenha:</strong><br>"
                );

                if (comentario != null &&
                        !comentario.trim().isEmpty()) {

                    html.append(
                            escapar(comentario)
                    );

                } else {

                    html.append(
                            "Sem resenha."
                    );
                }

                html.append("</p>");

                html.append("</div>");

                html.append("</article>");
            }

            if (!possuiAvaliacao) {

                html.append(
                        "<div class='sem-avaliacoes'>"
                        + "🎮<br><br>"
                        + "Você ainda não avaliou nenhum jogo."
                        + "</div>"
                );
            }

            resultado.close();
            stmt.close();
            conexao.close();

        } catch (Exception e) {

            e.printStackTrace();

            html.append(
                    "<div class='sem-avaliacoes'>"
                    + "Não foi possível carregar suas avaliações."
                    + "</div>"
            );
        }

        html.append("</section>");

        html.append("</div>");
        html.append("</main>");

        html.append("</body>");
        html.append("</html>");

        response.getWriter().println(
                html.toString()
        );
    }

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

