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
import javax.servlet.http.HttpSession;
import model.Usuario;

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

        String imagem = "";

        if (foto != null && !foto.isEmpty()) {

            imagem =
                    "<div class='foto-perfil'>"
                    + "<img src='imagens/"
                    + foto
                    + "' alt='Foto de perfil'>"
                    + "</div>";

        } else {

            imagem =
                    "<div class='foto-perfil'>"
                    + "<div class='sem-foto'>"
                    + "Sem foto"
                    + "</div>"
                    + "</div>";
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
                "<title>Perfil - GameBoxd</title>"
        );

        html.append(
                "<link rel='stylesheet' href='style.css'>"
        );

        // =========================
        // CSS
        // =========================

        html.append("<style>");

        html.append(
                ".perfil-container {"
                + "max-width:1000px;"
                + "margin:40px auto;"
                + "padding:20px;"
                + "}"
        );

        html.append(
                ".perfil-box {"
                + "background:#202830;"
                + "padding:30px;"
                + "border-radius:12px;"
                + "text-align:center;"
                + "}"
        );

        html.append(
                ".foto-perfil {"
                + "display:flex;"
                + "justify-content:center;"
                + "margin:20px;"
                + "}"
        );

        html.append(
                ".foto-perfil img {"
                + "width:150px;"
                + "height:150px;"
                + "object-fit:cover;"
                + "border-radius:50%;"
                + "}"
        );

        html.append(
                ".sem-foto {"
                + "width:150px;"
                + "height:150px;"
                + "border-radius:50%;"
                + "background:#444;"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:center;"
                + "color:#aaa;"
                + "}"
        );

        html.append(
                ".dados-perfil {"
                + "text-align:left;"
                + "max-width:600px;"
                + "margin:auto;"
                + "}"
        );

        html.append(
                ".avaliacoes {"
                + "margin-top:40px;"
                + "text-align:left;"
                + "}"
        );

        html.append(
                ".avaliacao-card {"
                + "display:flex;"
                + "gap:20px;"
                + "background:#14181c;"
                + "padding:20px;"
                + "border-radius:10px;"
                + "margin-top:20px;"
                + "}"
        );

        html.append(
                ".capa-avaliacao {"
                + "width:100px;"
                + "height:140px;"
                + "object-fit:cover;"
                + "border-radius:6px;"
                + "flex-shrink:0;"
                + "}"
        );

        html.append(
                ".sem-capa-avaliacao {"
                + "width:100px;"
                + "height:140px;"
                + "background:#333;"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:center;"
                + "border-radius:6px;"
                + "color:#999;"
                + "flex-shrink:0;"
                + "}"
        );

        html.append(
                ".estrelas-perfil {"
                + "color:#ffd700;"
                + "font-size:20px;"
                + "letter-spacing:2px;"
                + "}"
        );

        html.append(
                ".texto-avaliacao {"
                + "flex:1;"
                + "}"
        );

        html.append(
                ".sem-avaliacoes {"
                + "background:#14181c;"
                + "padding:30px;"
                + "border-radius:10px;"
                + "text-align:center;"
                + "color:#aaa;"
                + "}"
        );

        html.append("</style>");

        html.append("</head>");

        html.append("<body>");

        // =========================
        // HEADER
        // =========================

        html.append("<header>");

        html.append("<h1>GameBoxd</h1>");

        html.append("<nav>");

        html.append(
                "<a href='index.html'>Início</a>"
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
                "<a href='logout'>Sair</a>"
        );

        html.append("</nav>");

        html.append("</header>");

        // =========================
        // PERFIL
        // =========================

        html.append(
                "<main class='perfil-container'>"
        );

        html.append(
                "<div class='perfil-box'>"
        );

        html.append("<h2>Meu Perfil</h2>");

        html.append(imagem);

        html.append(
                "<div class='dados-perfil'>"
        );

        html.append(
                "<p><strong>Nome:</strong> "
        );

        html.append(usuario.getNome());

        html.append("</p>");

        html.append(
                "<p><strong>E-mail:</strong> "
        );

        html.append(usuario.getEmail());

        html.append("</p>");

        html.append(
                "<p><strong>País:</strong> "
        );

        html.append(usuario.getPais());

        html.append("</p>");

        html.append(
                "<p><strong>Plataforma:</strong> "
        );

        html.append(
                usuario.getPlataformaFavorita()
        );

        html.append("</p>");

        html.append(
                "<p><strong>Bio:</strong> "
        );

        html.append(usuario.getBio());

        html.append("</p>");

        html.append("</div>");

        // =========================
        // AVALIAÇÕES
        // =========================

        html.append(
                "<section class='avaliacoes'>"
        );

        html.append(
                "<h2>⭐ Minhas avaliações</h2>"
        );

        try {

            Connection conexao =
                    Conexao.conectar();

            String sql =
                    "SELECT "
                    + "avaliacao.id_jogo, "
                    + "jogo.titulo, "
                    + "jogo.capa, "
                    + "avaliacao.nota, "
                    + "avaliacao.comentario, "
                    + "avaliacao.horas_jogadas "
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

                double horas =
                        resultado.getDouble("horas_jogadas");

                html.append(
                        "<div class='avaliacao-card'>"
                );

                // =========================
                // CAPA
                // =========================

                if (capa != null &&
                        !capa.trim().isEmpty()) {

                    html.append(
                            "<img "
                            + "class='capa-avaliacao' "
                            + "src='"
                            + capa
                            + "' "
                            + "alt='Capa do jogo'>"
                    );

                } else {

                    html.append(
                            "<div class='sem-capa-avaliacao'>"
                            + "Sem capa"
                            + "</div>"
                    );
                }

                // =========================
                // TEXTO
                // =========================

                html.append(
                        "<div class='texto-avaliacao'>"
                );

                html.append("<h3>");

                html.append(titulo);

                html.append("</h3>");

                // ESTRELAS

                html.append(
                        "<div class='estrelas-perfil'>"
                );

                int estrelas =
                        (int) nota;

                for (int i = 1; i <= 5; i++) {

                    if (i <= estrelas) {

                        html.append("★");

                    } else {

                        html.append("☆");
                    }
                }

                html.append("</div>");

                html.append(
                        "<p><strong>Nota:</strong> "
                );

                html.append(nota);

                html.append("/5</p>");

                // RESENHA

                html.append("<p>");

                if (comentario != null &&
                        !comentario.isEmpty()) {

                    html.append(
                            comentario
                    );

                } else {

                    html.append(
                            "Sem resenha."
                    );
                }

                html.append("</p>");

                html.append(
                        "<p><strong>⏱️ Horas jogadas:</strong> " +
                        horas +
                        "</p>"
                );

                html.append(
                        "<a class='botao-avaliar' " +
                        "href='avaliar?id=" +
                        resultado.getInt("id_jogo") +
                        "'>" +
                        "✏️ Editar avaliação" +
                        "</a>"
                );

                html.append("</div>");

                html.append("</div>");
            }

            if (!possuiAvaliacao) {

                html.append(
                        "<div class='sem-avaliacoes'>"
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
}