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

@WebServlet("/perfil")
public class PerfilServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // =====================================================
        // SESSÃO
        // =====================================================

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

        // =====================================================
        // HTML
        // =====================================================

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
                + "content='width=device-width, "
                + "initial-scale=1.0'>"
        );

        html.append(
                "<title>Meu Perfil - Inventory</title>"
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
                "body {"
                + "background:"
                + "radial-gradient("
                + "circle at top,"
                + "#32134d,"
                + "#12091b 55%,"
                + "#09050d"
                + ");"
                + "min-height:100vh;"
                + "}"
        );

        html.append(
                ".perfil-container {"
                + "max-width:1050px;"
                + "margin:45px auto;"
                + "padding:20px;"
                + "}"
        );

        html.append(
                ".perfil-box {"
                + "background:"
                + "linear-gradient("
                + "145deg,#21142c,#140b1b"
                + ");"
                + "border:1px solid #45245d;"
                + "border-radius:22px;"
                + "padding:35px;"
                + "box-shadow:"
                + "0 15px 50px rgba(0,0,0,.4);"
                + "}"
        );

        // =====================================================
        // TOPO
        // =====================================================

        html.append(
                ".perfil-topo {"
                + "text-align:center;"
                + "padding-bottom:30px;"
                + "border-bottom:1px solid #382043;"
                + "}"
        );

        html.append(
                ".titulo-perfil {"
                + "font-size:36px;"
                + "margin-bottom:20px;"
                + "background:"
                + "linear-gradient("
                + "90deg,#a855f7,#c084fc,#7c3aed"
                + ");"
                + "-webkit-background-clip:text;"
                + "-webkit-text-fill-color:transparent;"
                + "}"
        );

        // =====================================================
        // FOTO
        // =====================================================

        html.append(
                ".foto-perfil {"
                + "display:flex;"
                + "justify-content:center;"
                + "margin:20px 0;"
                + "}"
        );

        html.append(
                ".foto-perfil img,"
                + ".sem-foto {"
                + "width:170px;"
                + "height:170px;"
                + "border-radius:50%;"
                + "object-fit:cover;"
                + "border:5px solid #7c3aed;"
                + "box-shadow:"
                + "0 0 35px rgba(124,58,237,.45);"
                + "}"
        );

        html.append(
                ".sem-foto {"
                + "background:#251532;"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:center;"
                + "color:#aaa;"
                + "font-size:15px;"
                + "}"
        );

        // =====================================================
        // NOME
        // =====================================================

        html.append(
                ".nome-perfil {"
                + "font-size:28px;"
                + "margin:10px 0 3px;"
                + "color:white;"
                + "}"
        );

        html.append(
                ".username-perfil {"
                + "color:#a855f7;"
                + "font-size:16px;"
                + "}"
        );

        // =====================================================
        // BOTÃO EDITAR
        // =====================================================

        html.append(
                ".botao-editar {"
                + "display:inline-block;"
                + "margin-top:20px;"
                + "padding:12px 24px;"
                + "background:"
                + "linear-gradient("
                + "135deg,#7c3aed,#9333ea"
                + ");"
                + "color:white;"
                + "text-decoration:none;"
                + "border-radius:10px;"
                + "font-weight:bold;"
                + "transition:.25s;"
                + "}"
        );

        html.append(
                ".botao-editar:hover {"
                + "transform:translateY(-3px);"
                + "box-shadow:"
                + "0 10px 25px rgba(124,58,237,.35);"
                + "}"
        );

        // =====================================================
        // DADOS
        // =====================================================

        html.append(
                ".dados-perfil {"
                + "display:grid;"
                + "grid-template-columns:repeat(2,1fr);"
                + "gap:15px;"
                + "margin-top:30px;"
                + "}"
        );

        html.append(
                ".dado {"
                + "background:#160d1e;"
                + "border:1px solid #352044;"
                + "padding:17px;"
                + "border-radius:12px;"
                + "transition:.25s;"
                + "}"
        );

        html.append(
                ".dado:hover {"
                + "border-color:#7c3aed;"
                + "transform:translateY(-2px);"
                + "}"
        );

        html.append(
                ".dado strong {"
                + "display:block;"
                + "color:#a855f7;"
                + "font-size:13px;"
                + "margin-bottom:5px;"
                + "}"
        );

        html.append(
                ".dado span {"
                + "color:#ddd;"
                + "}"
        );

        html.append(
                ".dado-bio {"
                + "grid-column:1 / -1;"
                + "}"
        );

        // =====================================================
        // FAVORITOS
        // =====================================================

        html.append(
                ".favoritos-perfil {"
                + "margin-top:45px;"
                + "padding-top:35px;"
                + "border-top:1px solid #382043;"
                + "}"
        );

        html.append(
                ".titulo-favoritos {"
                + "font-size:28px;"
                + "color:white;"
                + "margin:0;"
                + "}"
        );

        html.append(
                ".descricao-favoritos {"
                + "color:#999;"
                + "margin:8px 0 25px;"
                + "}"
        );

        html.append(
                ".favoritos-grid {"
                + "display:grid;"
                + "grid-template-columns:"
                + "repeat(5,1fr);"
                + "gap:15px;"
                + "}"
        );

        html.append(
                ".favorito-card {"
                + "background:#160d1e;"
                + "border:1px solid #45245d;"
                + "border-radius:14px;"
                + "padding:10px;"
                + "text-align:center;"
                + "transition:.25s;"
                + "}"
        );

        html.append(
                ".favorito-card:hover {"
                + "transform:translateY(-5px);"
                + "border-color:#8b5cf6;"
                + "box-shadow:"
                + "0 10px 25px rgba(124,58,237,.25);"
                + "}"
        );

        html.append(
                ".capa-favorito {"
                + "width:100%;"
                + "height:210px;"
                + "object-fit:cover;"
                + "border-radius:9px;"
                + "display:block;"
                + "}"
        );

        html.append(
                ".sem-capa-favorito {"
                + "width:100%;"
                + "height:210px;"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:center;"
                + "background:#251532;"
                + "border-radius:9px;"
                + "font-size:35px;"
                + "color:#777;"
                + "}"
        );

        html.append(
                ".favorito-card h3 {"
                + "color:#fff;"
                + "font-size:15px;"
                + "margin:12px 4px 7px;"
                + "min-height:40px;"
                + "}"
        );

        html.append(
                ".genero-favorito {"
                + "color:#a855f7;"
                + "font-size:12px;"
                + "min-height:28px;"
                + "}"
        );

        html.append(
                ".botao-remover-favorito {"
                + "width:100%;"
                + "padding:8px;"
                + "border:1px solid #6d28d9;"
                + "border-radius:8px;"
                + "background:#241434;"
                + "color:#ddd;"
                + "cursor:pointer;"
                + "font-weight:bold;"
                + "transition:.2s;"
                + "}"
        );

        html.append(
                ".botao-remover-favorito:hover {"
                + "background:#6d28d9;"
                + "color:white;"
                + "}"
        );

        html.append(
                ".nenhum-favorito {"
                + "grid-column:1/-1;"
                + "padding:35px;"
                + "text-align:center;"
                + "background:#160d1e;"
                + "border:1px solid #352044;"
                + "border-radius:12px;"
                + "color:#888;"
                + "}"
        );

        // =====================================================
        // AVALIAÇÕES
        // =====================================================

        html.append(
                ".avaliacoes {"
                + "margin-top:45px;"
                + "}"
        );

        html.append(
                ".titulo-avaliacoes {"
                + "font-size:25px;"
                + "color:white;"
                + "margin-bottom:20px;"
                + "}"
        );

        html.append(
                ".avaliacao-card {"
                + "display:flex;"
                + "gap:20px;"
                + "background:#160d1e;"
                + "border:1px solid #352044;"
                + "padding:20px;"
                + "border-radius:15px;"
                + "margin-top:15px;"
                + "transition:.25s;"
                + "}"
        );

        html.append(
                ".avaliacao-card:hover {"
                + "border-color:#7c3aed;"
                + "transform:translateY(-3px);"
                + "}"
        );

        html.append(
                ".capa-avaliacao {"
                + "width:100px;"
                + "height:140px;"
                + "object-fit:cover;"
                + "border-radius:8px;"
                + "flex-shrink:0;"
                + "}"
        );

        html.append(
                ".sem-capa-avaliacao {"
                + "width:100px;"
                + "height:140px;"
                + "background:#251532;"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:center;"
                + "border-radius:8px;"
                + "color:#888;"
                + "flex-shrink:0;"
                + "}"
        );

        html.append(
                ".texto-avaliacao {"
                + "flex:1;"
                + "}"
        );

        html.append(
                ".texto-avaliacao h3 {"
                + "color:white;"
                + "margin-top:0;"
                + "}"
        );

        html.append(
                ".estrelas-perfil {"
                + "color:#c084fc;"
                + "font-size:22px;"
                + "letter-spacing:2px;"
                + "}"
        );

        html.append(
                ".texto-avaliacao p {"
                + "color:#aaa;"
                + "line-height:1.5;"
                + "}"
        );

        html.append(
                ".sem-avaliacoes {"
                + "background:#160d1e;"
                + "border:1px solid #352044;"
                + "padding:30px;"
                + "border-radius:12px;"
                + "text-align:center;"
                + "color:#888;"
                + "}"
        );

        // =====================================================
        // RESPONSIVO
        // =====================================================

        html.append(
                "@media(max-width:800px){"
                + ".favoritos-grid{"
                + "grid-template-columns:"
                + "repeat(2,1fr);"
                + "}"
                + "}"
        );

        html.append(
                "@media(max-width:650px){"

                + ".perfil-container{"
                + "padding:10px;"
                + "margin:25px auto;"
                + "}"

                + ".perfil-box{"
                + "padding:20px;"
                + "}"

                + ".dados-perfil{"
                + "grid-template-columns:1fr;"
                + "}"

                + ".dado-bio{"
                + "grid-column:auto;"
                + "}"

                + ".avaliacao-card{"
                + "flex-direction:column;"
                + "}"

                + ".capa-avaliacao,"
                + ".sem-capa-avaliacao{"
                + "width:130px;"
                + "height:180px;"
                + "}"

                + ".favoritos-grid{"
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
        // INÍCIO DO PERFIL
        // =====================================================

        html.append(
                "<main class='perfil-container'>"
        );

        html.append(
                "<div class='perfil-box'>"
        );

        // =====================================================
        // TOPO
        // =====================================================

        html.append(
                "<section class='perfil-topo'>"
        );

        html.append(
                "<h2 class='titulo-perfil'>"
                + "Meu Perfil"
                + "</h2>"
        );

        // =====================================================
        // FOTO
        // =====================================================

        String foto =
                usuario.getFoto();

        String caminhoFoto =
                "";

        if (foto != null &&
                !foto.trim().isEmpty()) {

            String fotoLimpa =
                    foto.trim();

            if (fotoLimpa.startsWith(
                    "http://")
                    ||
                    fotoLimpa.startsWith(
                    "https://")) {

                caminhoFoto =
                        fotoLimpa;

            } else {

                while (
                        fotoLimpa.startsWith("/")
                ) {

                    fotoLimpa =
                            fotoLimpa.substring(1);
                }

                if (
                        fotoLimpa.startsWith(
                                "imagens/"
                        )
                ) {

                    caminhoFoto =
                            request.getContextPath()
                            + "/"
                            + fotoLimpa;

                } else {

                    caminhoFoto =
                            request.getContextPath()
                            + "/imagens/"
                            + fotoLimpa;
                }
            }
        }

        html.append(
                "<div class='foto-perfil'>"
        );

        if (!caminhoFoto.isEmpty()) {

            html.append(
                    "<img src='"
                    + escapar(caminhoFoto)
                    + "' "
                    + "alt='Foto de perfil'>"
            );

        } else {

            html.append(
                    "<div class='sem-foto'>"
                    + "Sem foto"
                    + "</div>"
            );
        }

        html.append("</div>");

        // =====================================================
        // NOME
        // =====================================================

        html.append(
                "<h3 class='nome-perfil'>"
                + escapar(usuario.getNome())
                + "</h3>"
        );

        html.append(
                "<div class='username-perfil'>"
                + "@"
                + escapar(usuario.getUsername())
                + "</div>"
        );

        // =====================================================
        // EDITAR
        // =====================================================

        html.append(
                "<a class='botao-editar' "
                + "href='editar-perfil'>"
                + "✏️ Editar perfil"
                + "</a>"
        );

        html.append("</section>");

        // =====================================================
        // DADOS
        // =====================================================

        html.append(
                "<section class='dados-perfil'>"
        );

        html.append(
                "<div class='dado'>"
                + "<strong>E-mail</strong>"
                + "<span>"
                + escapar(usuario.getEmail())
                + "</span>"
                + "</div>"
        );

        html.append(
                "<div class='dado'>"
                + "<strong>País</strong>"
                + "<span>"
                + escapar(usuario.getPais())
                + "</span>"
                + "</div>"
        );

        html.append(
                "<div class='dado'>"
                + "<strong>Plataforma favorita</strong>"
                + "<span>"
                + escapar(
                        usuario.getPlataformaFavorita()
                  )
                + "</span>"
                + "</div>"
        );

        html.append(
                "<div class='dado dado-bio'>"
                + "<strong>Biografia</strong>"
                + "<span>"
                + (
                    usuario.getBio() != null
                    &&
                    !usuario.getBio()
                            .trim()
                            .isEmpty()
                    ?
                    escapar(usuario.getBio())
                    :
                    "Nenhuma biografia adicionada."
                  )
                + "</span>"
                + "</div>"
        );

        html.append("</section>");

        // =====================================================
        // FAVORITOS
        // =====================================================

        html.append(
                "<section class='favoritos-perfil'>"
        );

        html.append(
                "<h2 class='titulo-favoritos'>"
                + "⭐ Meus favoritos"
                + "</h2>"
        );

        html.append(
                "<p class='descricao-favoritos'>"
                + "Escolha até 5 jogos para aparecer aqui."
                + "</p>"
        );

        html.append(
                "<div class='favoritos-grid'>"
        );

        try {

            Connection conexaoFavoritos =
                    Conexao.conectar();

            String sqlFavoritos =
                    "SELECT "
                    + "j.id, "
                    + "j.titulo, "
                    + "j.genero, "
                    + "j.capa "
                    + "FROM favorito f "
                    + "INNER JOIN jogo j "
                    + "ON f.id_jogo = j.id "
                    + "WHERE f.id_usuario = ? "
                    + "ORDER BY f.data_adicionado "
                    + "LIMIT 5";

            PreparedStatement stmtFavoritos =
                    conexaoFavoritos.prepareStatement(
                            sqlFavoritos
                    );

            stmtFavoritos.setInt(
                    1,
                    idUsuario
            );

            ResultSet rsFavoritos =
                    stmtFavoritos.executeQuery();

            int quantidadeFavoritos =
                    0;

            while (rsFavoritos.next()) {

                quantidadeFavoritos++;

                int idJogo =
                        rsFavoritos.getInt("id");

                String titulo =
                        rsFavoritos.getString(
                                "titulo"
                        );

                String genero =
                        rsFavoritos.getString(
                                "genero"
                        );

                String capa =
                        rsFavoritos.getString(
                                "capa"
                        );

                html.append(
                        "<article class='favorito-card'>"
                );

                // =================================================
                // CAPA
                // =================================================

                String caminhoCapa =
                        montarCaminhoCapa(
                                request,
                                capa
                        );

                if (caminhoCapa != null &&
                        !caminhoCapa.isEmpty()) {

                    html.append(
                            "<img "
                            + "class='capa-favorito' "
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
                            + "\""
                            + ">"
                    );

                    html.append(
                            "<div "
                            + "class='sem-capa-favorito' "
                            + "style='display:none;'>"
                            + "🎮"
                            + "</div>"
                    );

                } else {

                    html.append(
                            "<div "
                            + "class='sem-capa-favorito'>"
                            + "🎮"
                            + "</div>"
                    );
                }

                // =================================================
                // TITULO
                // =================================================

                html.append(
                        "<h3>"
                        + escapar(titulo)
                        + "</h3>"
                );

                // =================================================
                // GENERO
                // =================================================

                if (genero != null &&
                        !genero.trim().isEmpty()) {

                    html.append(
                            "<p class='genero-favorito'>"
                            + escapar(genero)
                            + "</p>"
                    );
                }

                // =================================================
                // REMOVER
                // =================================================

                html.append(
                        "<form "
                        + "method='POST' "
                        + "action='favorito'>"
                );

                html.append(
                        "<input "
                        + "type='hidden' "
                        + "name='idJogo' "
                        + "value='"
                        + idJogo
                        + "'>"
                );

                html.append(
                        "<button "
                        + "type='submit' "
                        + "class='botao-remover-favorito'>"
                        + "Remover"
                        + "</button>"
                );

                html.append(
                        "</form>"
                );

                html.append(
                        "</article>"
                );
            }

            rsFavoritos.close();

            stmtFavoritos.close();

            conexaoFavoritos.close();

            // =================================================
            // NENHUM FAVORITO
            // =================================================

            if (quantidadeFavoritos == 0) {

                html.append(
                        "<div "
                        + "class='nenhum-favorito'>"
                        + "Você ainda não escolheu "
                        + "seus favoritos."
                        + "</div>"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            html.append(
                    "<div "
                    + "class='nenhum-favorito'>"
                    + "Não foi possível carregar "
                    + "seus favoritos."
                    + "</div>"
            );
        }

        html.append("</div>");

        html.append("</section>");

        // =====================================================
        // AVALIAÇÕES
        // =====================================================

        html.append(
                "<section class='avaliacoes'>"
        );

        html.append(
                "<h2 class='titulo-avaliacoes'>"
                + "⭐ Minhas avaliações"
                + "</h2>"
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

            stmt.setInt(
                    1,
                    idUsuario
            );

            ResultSet resultado =
                    stmt.executeQuery();

            boolean possuiAvaliacao =
                    false;

            while (resultado.next()) {

                possuiAvaliacao =
                        true;

                String titulo =
                        resultado.getString(
                                "titulo"
                        );

                String capa =
                        resultado.getString(
                                "capa"
                        );

                double nota =
                        resultado.getDouble(
                                "nota"
                        );

                String comentario =
                        resultado.getString(
                                "comentario"
                        );

                html.append(
                        "<article "
                        + "class='avaliacao-card'>"
                );

                String caminhoCapa =
                        montarCaminhoCapa(
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
                            + "'>"
                    );

                } else {

                    html.append(
                            "<div "
                            + "class='sem-capa-avaliacao'>"
                            + "Sem capa"
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
                        "<div "
                        + "class='estrelas-perfil'>"
                );

                int estrelas =
                        (int) Math.round(nota);

                if (estrelas < 0) {
                    estrelas = 0;
                }

                if (estrelas > 5) {
                    estrelas = 5;
                }

                for (int i = 1; i <= 5; i++) {

                    html.append(
                            i <= estrelas
                            ? "★"
                            : "☆"
                    );
                }

                html.append(
                        "</div>"
                );

                html.append(
                        "<p>"
                        + "<strong>Nota:</strong> "
                        + nota
                        + "/5"
                        + "</p>"
                );

                html.append(
                        "<p>"
                        + "<strong>Resenha:</strong><br>"
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

                html.append(
                        "</p>"
                );

                html.append(
                        "</div>"
                );

                html.append(
                        "</article>"
                );
            }

            if (!possuiAvaliacao) {

                html.append(
                        "<div "
                        + "class='sem-avaliacoes'>"
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
                    "<div "
                    + "class='sem-avaliacoes'>"
                    + "Não foi possível carregar "
                    + "suas avaliações."
                    + "</div>"
            );
        }

        html.append("</section>");

        // =====================================================
        // FECHAMENTO
        // =====================================================

        html.append("</div>");

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

    private String montarCaminhoCapa(
            HttpServletRequest request,
            String capa) {

        if (capa == null ||
                capa.trim().isEmpty()) {

            return null;
        }

        String caminho =
                capa.trim();

        /*
         * Se já for uma URL da Steam,
         * reconstrói usando o CDN que
         * funcionou no catálogo.
         */

        Pattern pattern =
                Pattern.compile(
                        "/apps/(\\d+)"
                );

        Matcher matcher =
                pattern.matcher(caminho);

        if (matcher.find()) {

            String appId =
                    matcher.group(1);

            return
                    "https://cdn.akamai.steamstatic.com/"
                    + "steam/apps/"
                    + appId
                    + "/library_600x900_2x.jpg";
        }

        /*
         * Se for somente um App ID.
         */

        if (caminho.matches("\\d+")) {

            return
                    "https://cdn.akamai.steamstatic.com/"
                    + "steam/apps/"
                    + caminho
                    + "/library_600x900_2x.jpg";
        }

        /*
         * Se for uma URL normal ou caminho local,
         * mantém o valor.
         */

        if (caminho.startsWith("http://") ||
                caminho.startsWith("https://")) {

            return caminho;
        }

        while (caminho.startsWith("/")) {

            caminho =
                    caminho.substring(1);
        }

        return request.getContextPath()
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