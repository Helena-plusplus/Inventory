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

        // -----------------------------------------------------
        // BODY
        // -----------------------------------------------------

        html.append(
                "body{"
                + "margin:0;"
                + "background:"
                + "radial-gradient("
                + "circle at 50% 0%,"
                + "#35135a 0%,"
                + "#160b22 35%,"
                + "#0b0710 75%);"
                + "min-height:100vh;"
                + "color:#fff;"
                + "}"
        );

        // -----------------------------------------------------
        // CONTAINER
        // -----------------------------------------------------

        html.append(
                ".perfil-container{"
                + "max-width:1100px;"
                + "margin:45px auto;"
                + "padding:20px;"
                + "}"
        );

        // -----------------------------------------------------
        // CARD
        // -----------------------------------------------------

        html.append(
                ".perfil-box{"
                + "background:"
                + "linear-gradient("
                + "145deg,"
                + "rgba(35,20,48,.98),"
                + "rgba(15,9,22,.98)"
                + ");"
                + "border:1px solid #47225f;"
                + "border-radius:26px;"
                + "overflow:hidden;"
                + "box-shadow:"
                + "0 25px 70px rgba(0,0,0,.45);"
                + "}"
        );

        // -----------------------------------------------------
        // CAPA SUPERIOR
        // -----------------------------------------------------

        html.append(
                ".perfil-capa{"
                + "height:190px;"
                + "background:"
                + "linear-gradient("
                + "135deg,"
                + "#5b21b6,"
                + "#7c3aed,"
                + "#a855f7"
                + ");"
                + "position:relative;"
                + "overflow:hidden;"
                + "}"
        );

        html.append(
                ".perfil-capa:before{"
                + "content:'';"
                + "position:absolute;"
                + "width:350px;"
                + "height:350px;"
                + "border-radius:50%;"
                + "background:rgba(255,255,255,.08);"
                + "right:-100px;"
                + "top:-180px;"
                + "}"
        );

        html.append(
                ".perfil-capa:after{"
                + "content:'';"
                + "position:absolute;"
                + "width:250px;"
                + "height:250px;"
                + "border-radius:50%;"
                + "background:rgba(255,255,255,.05);"
                + "left:-80px;"
                + "bottom:-170px;"
                + "}"
        );

        html.append(
                ".perfil-conteudo{"
                + "padding:0 38px 40px;"
                + "}"
        );

        // -----------------------------------------------------
        // TOPO DO PERFIL
        // -----------------------------------------------------

        html.append(
                ".perfil-topo{"
                + "margin-top:-75px;"
                + "position:relative;"
                + "display:flex;"
                + "align-items:flex-end;"
                + "gap:28px;"
                + "padding-bottom:28px;"
                + "border-bottom:1px solid #382043;"
                + "}"
        );

        html.append(
                ".foto-area{"
                + "flex-shrink:0;"
                + "}"
        );

        // -----------------------------------------------------
        // FOTO
        // -----------------------------------------------------

        html.append(
                ".foto-perfil,.sem-foto{"
                + "width:150px;"
                + "height:150px;"
                + "border-radius:50%;"
                + "border:6px solid #17101f;"
                + "box-shadow:"
                + "0 0 0 3px #8b5cf6,"
                + "0 12px 35px rgba(0,0,0,.45);"
                + "}"
        );

        html.append(
                ".foto-perfil{"
                + "object-fit:cover;"
                + "display:block;"
                + "background:#24152f;"
                + "}"
        );

        html.append(
                ".sem-foto{"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:center;"
                + "background:#24152f;"
                + "color:#8e8297;"
                + "font-size:14px;"
                + "}"
        );

        // -----------------------------------------------------
        // DADOS PRINCIPAIS
        // -----------------------------------------------------

        html.append(
                ".dados-principais{"
                + "flex:1;"
                + "padding-bottom:5px;"
                + "}"
        );

        html.append(
                ".nome-com-emblema{"
                + "display:flex;"
                + "align-items:center;"
                + "gap:10px;"
                + "flex-wrap:wrap;"
                + "}"
        );

        html.append(
                ".nome-perfil{"
                + "margin:0;"
                + "font-size:34px;"
                + "font-weight:700;"
                + "letter-spacing:-.5px;"
                + "}"
        );

        html.append(
                ".username-perfil{"
                + "margin-top:6px;"
                + "color:#b98be8;"
                + "font-size:16px;"
                + "}"
        );

        html.append(
                ".bio-perfil{"
                + "margin-top:16px;"
                + "color:#c3bac9;"
                + "line-height:1.6;"
                + "max-width:650px;"
                + "}"
        );

        // =====================================================
        // EMBLEMA ♡ MY LOVE
        // =====================================================

        html.append(
                ".emblema-player1{"
                + "display:inline-flex;"
                + "align-items:center;"
                + "gap:6px;"
                + "padding:6px 12px;"
                + "border-radius:20px;"
                + "background:"
                + "linear-gradient("
                + "135deg,#3a174d,#6d2b8c"
                + ");"
                + "border:1px solid #b56ee0;"
                + "color:#f1d8fb;"
                + "font-size:12px;"
                + "font-weight:700;"
                + "box-shadow:"
                + "0 0 16px rgba(181,110,224,.2);"
                + "white-space:nowrap;"
                + "}"
        );

        html.append(
                ".coracao-player1{"
                + "font-size:18px;"
                + "color:#f2b6ff;"
                + "line-height:1;"
                + "}"
        );

        // -----------------------------------------------------
        // BOTÃO EDITAR
        // -----------------------------------------------------

        html.append(
                ".botao-editar{"
                + "display:inline-block;"
                + "margin-top:18px;"
                + "padding:11px 20px;"
                + "background:#25152f;"
                + "border:1px solid #6d28d9;"
                + "border-radius:10px;"
                + "color:#fff;"
                + "text-decoration:none;"
                + "font-weight:600;"
                + "transition:.25s;"
                + "}"
        );

        html.append(
                ".botao-editar:hover{"
                + "background:#6d28d9;"
                + "transform:translateY(-2px);"
                + "}"
        );

        // -----------------------------------------------------
        // DADOS
        // -----------------------------------------------------

        html.append(
                ".dados-perfil{"
                + "display:grid;"
                + "grid-template-columns:"
                + "repeat(3,1fr);"
                + "gap:14px;"
                + "margin-top:28px;"
                + "}"
        );

        html.append(
                ".dado{"
                + "background:#160d1e;"
                + "border:1px solid #30203a;"
                + "border-radius:14px;"
                + "padding:17px;"
                + "transition:.25s;"
                + "}"
        );

        html.append(
                ".dado:hover{"
                + "border-color:#6d28d9;"
                + "transform:translateY(-2px);"
                + "}"
        );

        html.append(
                ".dado strong{"
                + "display:block;"
                + "font-size:11px;"
                + "text-transform:uppercase;"
                + "letter-spacing:1px;"
                + "color:#a855f7;"
                + "margin-bottom:7px;"
                + "}"
        );

        html.append(
                ".dado span{"
                + "display:block;"
                + "color:#ddd;"
                + "font-size:14px;"
                + "line-height:1.5;"
                + "}"
        );

        html.append(
                ".dado-bio{"
                + "grid-column:1/-1;"
                + "}"
        );

        // -----------------------------------------------------
        // SEÇÕES
        // -----------------------------------------------------

        html.append(
                ".secao{"
                + "margin-top:42px;"
                + "}"
        );

        html.append(
                ".titulo-secao{"
                + "font-size:25px;"
                + "margin:0;"
                + "color:#fff;"
                + "}"
        );

        html.append(
                ".linha-secao{"
                + "width:48px;"
                + "height:3px;"
                + "background:#8b5cf6;"
                + "border-radius:10px;"
                + "margin-top:10px;"
                + "margin-bottom:22px;"
                + "}"
        );

        // -----------------------------------------------------
        // FAVORITOS
        // -----------------------------------------------------

        html.append(
                ".favoritos-grid{"
                + "display:grid;"
                + "grid-template-columns:"
                + "repeat(5,1fr);"
                + "gap:16px;"
                + "}"
        );

        html.append(
                ".favorito-card{"
                + "background:#160d1e;"
                + "border:1px solid #30203a;"
                + "border-radius:15px;"
                + "padding:10px;"
                + "text-align:center;"
                + "transition:.25s;"
                + "}"
        );

        html.append(
                ".favorito-card:hover{"
                + "transform:translateY(-6px);"
                + "border-color:#8b5cf6;"
                + "box-shadow:"
                + "0 12px 25px rgba(124,58,237,.2);"
                + "}"
        );

        html.append(
                ".capa-favorito{"
                + "width:100%;"
                + "height:210px;"
                + "object-fit:cover;"
                + "border-radius:10px;"
                + "display:block;"
                + "}"
        );

        html.append(
                ".sem-capa-favorito{"
                + "width:100%;"
                + "height:210px;"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:center;"
                + "background:"
                + "linear-gradient(135deg,#21152d,#3d2059);"
                + "border-radius:10px;"
                + "color:#8f8298;"
                + "font-size:12px;"
                + "}"
        );

        html.append(
                ".favorito-card h3{"
                + "font-size:14px;"
                + "color:#fff;"
                + "margin:12px 4px 8px;"
                + "min-height:38px;"
                + "line-height:1.3;"
                + "}"
        );

        html.append(
                ".genero-favorito{"
                + "font-size:11px;"
                + "color:#a855f7;"
                + "margin-bottom:12px;"
                + "min-height:28px;"
                + "}"
        );

        html.append(
                ".botao-remover-favorito{"
                + "width:100%;"
                + "padding:8px;"
                + "background:#21142a;"
                + "border:1px solid #42204e;"
                + "border-radius:8px;"
                + "color:#aaa;"
                + "cursor:pointer;"
                + "transition:.2s;"
                + "}"
        );

        html.append(
                ".botao-remover-favorito:hover{"
                + "background:#3b183f;"
                + "border-color:#7c3aed;"
                + "color:#fff;"
                + "}"
        );

        html.append(
                ".nenhum-favorito{"
                + "grid-column:1/-1;"
                + "border:1px dashed #4a3158;"
                + "border-radius:14px;"
                + "padding:35px;"
                + "text-align:center;"
                + "color:#807687;"
                + "}"
        );

        // -----------------------------------------------------
        // AVALIAÇÕES
        // -----------------------------------------------------

        html.append(
                ".avaliacao-card{"
                + "display:flex;"
                + "gap:22px;"
                + "background:#160d1e;"
                + "border:1px solid #30203a;"
                + "border-radius:15px;"
                + "padding:18px;"
                + "margin-bottom:15px;"
                + "transition:.25s;"
                + "}"
        );

        html.append(
                ".avaliacao-card:hover{"
                + "border-color:#6d28d9;"
                + "}"
        );

        html.append(
                ".capa-avaliacao{"
                + "width:90px;"
                + "height:125px;"
                + "object-fit:cover;"
                + "border-radius:8px;"
                + "flex-shrink:0;"
                + "}"
        );

        html.append(
                ".sem-capa-avaliacao{"
                + "width:90px;"
                + "height:125px;"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:center;"
                + "background:#24152f;"
                + "border-radius:8px;"
                + "color:#777;"
                + "font-size:11px;"
                + "flex-shrink:0;"
                + "}"
        );

        html.append(
                ".texto-avaliacao{"
                + "flex:1;"
                + "}"
        );

        html.append(
                ".texto-avaliacao h3{"
                + "margin:0 0 10px;"
                + "font-size:18px;"
                + "color:#fff;"
                + "}"
        );

        html.append(
                ".estrelas-perfil{"
                + "color:#c084fc;"
                + "font-size:21px;"
                + "letter-spacing:2px;"
                + "margin-bottom:8px;"
                + "}"
        );

        html.append(
                ".nota-texto{"
                + "font-size:12px;"
                + "color:#9f91a7;"
                + "}"
        );

        html.append(
                ".texto-avaliacao p{"
                + "color:#b9aebe;"
                + "line-height:1.6;"
                + "margin:10px 0 0;"
                + "}"
        );

        html.append(
                ".sem-avaliacoes{"
                + "padding:35px;"
                + "text-align:center;"
                + "background:#160d1e;"
                + "border:1px dashed #4a3158;"
                + "border-radius:14px;"
                + "color:#807687;"
                + "}"
        );

        // -----------------------------------------------------
        // RESPONSIVO
        // -----------------------------------------------------

        html.append(
                "@media(max-width:900px){"

                + ".favoritos-grid{"
                + "grid-template-columns:"
                + "repeat(3,1fr);"
                + "}"

                + ".dados-perfil{"
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

                + ".perfil-capa{"
                + "height:140px;"
                + "}"

                + ".perfil-conteudo{"
                + "padding:0 18px 25px;"
                + "}"

                + ".perfil-topo{"
                + "margin-top:-60px;"
                + "display:block;"
                + "text-align:center;"
                + "}"

                + ".foto-area{"
                + "display:flex;"
                + "justify-content:center;"
                + "}"

                + ".foto-perfil,.sem-foto{"
                + "width:120px;"
                + "height:120px;"
                + "}"

                + ".nome-com-emblema{"
                + "justify-content:center;"
                + "margin-top:16px;"
                + "}"

                + ".nome-perfil{"
                + "font-size:27px;"
                + "}"

                + ".dados-principais{"
                + "padding-bottom:0;"
                + "}"

                + ".dados-perfil{"
                + "grid-template-columns:1fr;"
                + "}"

                + ".dado-bio{"
                + "grid-column:auto;"
                + "}"

                + ".favoritos-grid{"
                + "grid-template-columns:repeat(2,1fr);"
                + "}"

                + ".capa-favorito,.sem-capa-favorito{"
                + "height:190px;"
                + "}"

                + ".avaliacao-card{"
                + "flex-direction:column;"
                + "}"

                + ".capa-avaliacao,.sem-capa-avaliacao{"
                + "width:120px;"
                + "height:165px;"
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
        // PERFIL
        // =====================================================

        html.append(
                "<main class='perfil-container'>"
        );

        html.append(
                "<div class='perfil-box'>"
        );

        html.append(
                "<div class='perfil-capa'></div>"
        );

        html.append(
                "<div class='perfil-conteudo'>"
        );

        // =====================================================
        // TOPO
        // =====================================================

        html.append(
                "<section class='perfil-topo'>"
        );

        html.append(
                "<div class='foto-area'>"
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

            if (fotoLimpa.startsWith("http://")
                    ||
                    fotoLimpa.startsWith("https://")) {

                caminhoFoto =
                        fotoLimpa;

            } else {

                while (
                        fotoLimpa.startsWith("/")
                ) {

                    fotoLimpa =
                            fotoLimpa.substring(1);
                }

                caminhoFoto =
                        request.getContextPath()
                        + "/foto-perfil?arquivo="
                        + java.net.URLEncoder.encode(
                                fotoLimpa,
                                "UTF-8"
                        );
            }
        }

        if (!caminhoFoto.isEmpty()) {

            html.append(
                    "<img "
                    + "class='foto-perfil' "
                    + "src='"
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
        // DADOS PRINCIPAIS
        // =====================================================

        html.append(
                "<div class='dados-principais'>"
        );

        String emailUsuario =
                usuario.getEmail();

        boolean jogadorEspecial =
                emailUsuario != null
                &&
                emailUsuario.trim()
                        .equalsIgnoreCase(
                                "rebecarodriguesduarte2@gmail.com"
                        );

        html.append(
                "<div class='nome-com-emblema'>"
        );

        html.append(
                "<h2 class='nome-perfil'>"
                + escapar(usuario.getNome())
                + "</h2>"
        );

        if (jogadorEspecial) {

            html.append(
                    "<span class='emblema-player1'>"
                    + "<span class='coracao-player1'>♡</span>"
                    + " My Love"
                    + "</span>"
            );
        }

        html.append(
                "</div>"
        );

        html.append(
                "<div class='username-perfil'>"
                + "@"
                + escapar(usuario.getUsername())
                + "</div>"
        );

        String bio =
                usuario.getBio();

        if (bio != null &&
                !bio.trim().isEmpty()) {

            html.append(
                    "<div class='bio-perfil'>"
                    + escapar(bio)
                    + "</div>"
            );
        }

        html.append(
                "<a "
                + "class='botao-editar' "
                + "href='editar-perfil'>"
                + "Editar perfil"
                + "</a>"
        );

        html.append("</div>");

        html.append("</section>");

        // =====================================================
        // INFORMAÇÕES
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
                + valorOuPadrao(
                        usuario.getPais(),
                        "Não informado"
                  )
                + "</span>"
                + "</div>"
        );

        html.append(
                "<div class='dado'>"
                + "<strong>Plataforma favorita</strong>"
                + "<span>"
                + valorOuPadrao(
                        usuario.getPlataformaFavorita(),
                        "Não informado"
                  )
                + "</span>"
                + "</div>"
        );

        html.append(
                "<div class='dado dado-bio'>"
                + "<strong>Sobre</strong>"
                + "<span>"
                + valorOuPadrao(
                        usuario.getBio(),
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
                "<section class='secao'>"
        );

        html.append(
                "<h2 class='titulo-secao'>"
                + "Meus favoritos"
                + "</h2>"
        );

        html.append(
                "<div class='linha-secao'></div>"
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
                            + "Imagem indisponível"
                            + "</div>"
                    );

                } else {

                    html.append(
                            "<div "
                            + "class='sem-capa-favorito'>"
                            + "Imagem indisponível"
                            + "</div>"
                    );
                }

                html.append(
                        "<h3>"
                        + escapar(titulo)
                        + "</h3>"
                );

                if (genero != null &&
                        !genero.trim().isEmpty()) {

                    html.append(
                            "<div "
                            + "class='genero-favorito'>"
                            + escapar(genero)
                            + "</div>"
                    );

                } else {

                    html.append(
                            "<div "
                            + "class='genero-favorito'>"
                            + "&nbsp;"
                            + "</div>"
                    );
                }

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

                html.append("</form>");

                html.append("</article>");
            }

            rsFavoritos.close();

            stmtFavoritos.close();

            conexaoFavoritos.close();

            if (quantidadeFavoritos == 0) {

                html.append(
                        "<div "
                        + "class='nenhum-favorito'>"
                        + "Você ainda não escolheu "
                        + "seus jogos favoritos."
                        + "</div>"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            html.append(
                    "<div "
                    + "class='nenhum-favorito'>"
                    + "Não foi possível carregar "
                    + "os favoritos."
                    + "</div>"
            );
        }

        html.append("</div>");

        html.append("</section>");

        // =====================================================
        // AVALIAÇÕES
        // =====================================================

        html.append(
                "<section class='secao'>"
        );

        html.append(
                "<h2 class='titulo-secao'>"
                + "Minhas avaliações"
                + "</h2>"
        );

        html.append(
                "<div class='linha-secao'></div>"
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
                    conexao.prepareStatement(
                            sql
                    );

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
                            + "Sem imagem"
                            + "</div>"
                    );
                }

                html.append(
                        "<div "
                        + "class='texto-avaliacao'>"
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
                        "<div class='nota-texto'>"
                        + "Nota "
                        + nota
                        + " de 5"
                        + "</div>"
                );

                html.append("<p>");

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
                        + "Você ainda não avaliou "
                        + "nenhum jogo."
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

        html.append("</div>");
        html.append("</div>");
        html.append("</main>");

        html.append("</body>");
        html.append("</html>");

        response.getWriter().println(
                html.toString()
        );
    }

    // =====================================================
    // MONTAR CAMINHO DA CAPA
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

        // =================================================
        // STEAM APP ID
        // =================================================

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

        // =================================================
        // SOMENTE NÚMERO
        // =================================================

        if (caminho.matches("\\d+")) {

            return
                    "https://cdn.akamai.steamstatic.com/"
                    + "steam/apps/"
                    + caminho
                    + "/library_600x900_2x.jpg";
        }

        // =================================================
        // URL
        // =================================================

        if (caminho.startsWith(
                "http://")
                ||
                caminho.startsWith(
                "https://")) {

            return caminho;
        }

        // =================================================
        // ARQUIVO LOCAL
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
    // VALOR PADRÃO
    // =====================================================

    private String valorOuPadrao(
            String valor,
            String padrao) {

        if (valor == null ||
                valor.trim().isEmpty()) {

            return escapar(padrao);
        }

        return escapar(valor);
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