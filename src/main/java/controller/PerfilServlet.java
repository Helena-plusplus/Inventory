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

        // =====================================================
        // FOTO
        // =====================================================

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

        // =====================================================
        // ESTATÍSTICAS
        // =====================================================

        int quantidadeAvaliacoes = 0;
        int quantidadeJogosZerados = 0;
        double totalHoras = 0;
        double mediaNotas = 0;

        try {

            Connection conexao =
                    Conexao.conectar();

            // ---------------------------------------------
            // AVALIAÇÕES
            // ---------------------------------------------

            PreparedStatement stmtAvaliacoes =
                    conexao.prepareStatement(
                            "SELECT COUNT(*), " +
                            "COALESCE(AVG(nota), 0) " +
                            "FROM avaliacao " +
                            "WHERE id_usuario = ?"
                    );

            stmtAvaliacoes.setInt(
                    1,
                    idUsuario
            );

            ResultSet rsAvaliacoes =
                    stmtAvaliacoes.executeQuery();

            if (rsAvaliacoes.next()) {

                quantidadeAvaliacoes =
                        rsAvaliacoes.getInt(1);

                mediaNotas =
                        rsAvaliacoes.getDouble(2);
            }

            rsAvaliacoes.close();
            stmtAvaliacoes.close();

            // ---------------------------------------------
            // JOGOS ZERADOS + HORAS
            // ---------------------------------------------

            PreparedStatement stmtHoras =
                    conexao.prepareStatement(
                            "SELECT COUNT(*), " +
                            "COALESCE(SUM(horas_jogadas), 0) " +
                            "FROM biblioteca " +
                            "WHERE id_usuario = ? " +
                            "AND status = 'zerado'"
                    );

            stmtHoras.setInt(
                    1,
                    idUsuario
            );

            ResultSet rsHoras =
                    stmtHoras.executeQuery();

            if (rsHoras.next()) {

                quantidadeJogosZerados =
                        rsHoras.getInt(1);

                totalHoras =
                        rsHoras.getDouble(2);
            }

            rsHoras.close();
            stmtHoras.close();

            conexao.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

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
                "<meta name='viewport' " +
                "content='width=device-width, initial-scale=1.0'>"
        );

        html.append(
                "<title>Meu Perfil - Inventory</title>"
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
                "body {"
                + "background:"
                + "radial-gradient(circle at 20% 0%,#55208a 0%,transparent 28%),"
                + "radial-gradient(circle at 90% 20%,#32115c 0%,transparent 25%),"
                + "linear-gradient(135deg,#09060d,#120b18 45%,#09060d);"
                + "min-height:100vh;"
                + "}"
        );

        html.append(
                ".perfil-container {"
                + "max-width:1100px;"
                + "margin:45px auto 80px;"
                + "padding:20px;"
                + "}"
        );

        // =====================================================
        // CAPA
        // =====================================================

        html.append(
                ".perfil-banner {"
                + "height:230px;"
                + "border-radius:26px;"
                + "position:relative;"
                + "overflow:hidden;"
                + "background:"
                + "linear-gradient(135deg,#3b1260,#7c3aed 45%,#c084fc);"
                + "box-shadow:0 25px 60px rgba(0,0,0,.45);"
                + "}"
        );

        html.append(
                ".perfil-banner:before {"
                + "content:'';"
                + "position:absolute;"
                + "width:400px;"
                + "height:400px;"
                + "background:rgba(255,255,255,.08);"
                + "border-radius:50%;"
                + "top:-280px;"
                + "left:-80px;"
                + "}"
        );

        html.append(
                ".perfil-banner:after {"
                + "content:'INVENTORY';"
                + "position:absolute;"
                + "right:30px;"
                + "bottom:18px;"
                + "font-family:'Orbitron',sans-serif;"
                + "font-size:48px;"
                + "font-weight:900;"
                + "letter-spacing:5px;"
                + "color:rgba(255,255,255,.09);"
                + "}"
        );

        // =====================================================
        // CARD
        // =====================================================

        html.append(
                ".perfil-card {"
                + "position:relative;"
                + "margin-top:-80px;"
                + "background:linear-gradient(145deg,rgba(32,19,45,.98),rgba(14,9,19,.99));"
                + "border:1px solid #4a2860;"
                + "border-radius:26px;"
                + "padding:35px;"
                + "box-shadow:0 25px 70px rgba(0,0,0,.5);"
                + "}"
        );

        // =====================================================
        // TOPO
        // =====================================================

        html.append(
                ".perfil-topo {"
                + "text-align:center;"
                + "}"
        );

        html.append(
                ".foto-wrapper {"
                + "display:flex;"
                + "justify-content:center;"
                + "margin-top:-125px;"
                + "position:relative;"
                + "z-index:2;"
                + "}"
        );

        html.append(
                ".foto-perfil {"
                + "width:190px;"
                + "height:190px;"
                + "border-radius:50%;"
                + "object-fit:cover;"
                + "background:#251532;"
                + "border:7px solid #17101f;"
                + "box-shadow:"
                + "0 0 0 5px #a855f7,"
                + "0 0 45px rgba(168,85,247,.55);"
                + "}"
        );

        html.append(
                ".sem-foto {"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:center;"
                + "font-size:55px;"
                + "color:#aa9bb8;"
                + "}"
        );

        html.append(
                ".nome-perfil {"
                + "font-size:34px;"
                + "margin:20px 0 4px;"
                + "color:white;"
                + "}"
        );

        html.append(
                ".username-perfil {"
                + "color:#c084fc;"
                + "font-weight:bold;"
                + "font-size:16px;"
                + "}"
        );

        html.append(
                ".bio {"
                + "max-width:650px;"
                + "margin:18px auto;"
                + "line-height:1.6;"
                + "color:#bcb1c5;"
                + "}"
        );

        // =====================================================
        // BOTÃO EDITAR
        // =====================================================

        html.append(
                ".botao-editar {"
                + "display:inline-block;"
                + "margin-top:10px;"
                + "padding:13px 24px;"
                + "border-radius:11px;"
                + "background:linear-gradient(135deg,#7c3aed,#a855f7);"
                + "color:white;"
                + "text-decoration:none;"
                + "font-weight:bold;"
                + "transition:.25s;"
                + "}"
        );

        html.append(
                ".botao-editar:hover {"
                + "transform:translateY(-3px);"
                + "box-shadow:0 12px 30px rgba(124,58,237,.35);"
                + "}"
        );

        // =====================================================
        // ESTATÍSTICAS
        // =====================================================

        html.append(
                ".estatisticas {"
                + "display:grid;"
                + "grid-template-columns:repeat(4,1fr);"
                + "gap:15px;"
                + "margin:38px 0;"
                + "}"
        );

        html.append(
                ".estatistica {"
                + "position:relative;"
                + "background:linear-gradient(145deg,#1b1026,#120b18);"
                + "border:1px solid #39234b;"
                + "border-radius:16px;"
                + "padding:22px;"
                + "text-align:center;"
                + "overflow:hidden;"
                + "transition:.25s;"
                + "}"
        );

        html.append(
                ".estatistica:hover {"
                + "transform:translateY(-4px);"
                + "border-color:#8b5cf6;"
                + "box-shadow:0 12px 30px rgba(124,58,237,.15);"
                + "}"
        );

        html.append(
                ".estatistica:before {"
                + "content:'';"
                + "position:absolute;"
                + "width:80px;"
                + "height:80px;"
                + "border-radius:50%;"
                + "background:#8b5cf6;"
                + "filter:blur(45px);"
                + "opacity:.08;"
                + "top:-40px;"
                + "right:-20px;"
                + "}"
        );

        html.append(
                ".estatistica-icone {"
                + "font-size:23px;"
                + "margin-bottom:7px;"
                + "}"
        );

        html.append(
                ".estatistica-numero {"
                + "display:block;"
                + "font-size:29px;"
                + "font-weight:800;"
                + "color:#d8b4fe;"
                + "}"
        );

        html.append(
                ".estatistica-label {"
                + "display:block;"
                + "color:#8f8399;"
                + "font-size:12px;"
                + "margin-top:5px;"
                + "text-transform:uppercase;"
                + "letter-spacing:1px;"
                + "}"
        );

        // =====================================================
        // HORAS DESTAQUE
        // =====================================================

        html.append(
                ".horas-destaque {"
                + "background:"
                + "linear-gradient(135deg,#28123c,#160d20);"
                + "border:1px solid #66358b;"
                + "border-radius:18px;"
                + "padding:25px;"
                + "margin-bottom:30px;"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:space-between;"
                + "gap:20px;"
                + "box-shadow:0 10px 35px rgba(124,58,237,.12);"
                + "}"
        );

        html.append(
                ".horas-texto h3 {"
                + "margin:0 0 5px;"
                + "color:white;"
                + "font-size:20px;"
                + "}"
        );

        html.append(
                ".horas-texto p {"
                + "margin:0;"
                + "color:#96889f;"
                + "}"
        );

        html.append(
                ".horas-total {"
                + "font-family:'Orbitron',sans-serif;"
                + "font-size:33px;"
                + "font-weight:bold;"
                + "color:#c084fc;"
                + "text-shadow:0 0 18px rgba(192,132,252,.25);"
                + "white-space:nowrap;"
                + "}"
        );

        // =====================================================
        // INFORMAÇÕES
        // =====================================================

        html.append(
                ".secao-titulo {"
                + "margin:35px 0 18px;"
                + "font-size:25px;"
                + "color:white;"
                + "}"
        );

        html.append(
                ".linha-roxa {"
                + "width:65px;"
                + "height:3px;"
                + "margin-top:8px;"
                + "background:linear-gradient(90deg,#7c3aed,#c084fc);"
                + "border-radius:10px;"
                + "}"
        );

        html.append(
                ".dados-perfil {"
                + "display:grid;"
                + "grid-template-columns:repeat(2,1fr);"
                + "gap:15px;"
                + "}"
        );

        html.append(
                ".dado {"
                + "background:#150c1d;"
                + "border:1px solid #34203f;"
                + "border-radius:15px;"
                + "padding:19px;"
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
                + "font-size:12px;"
                + "color:#a855f7;"
                + "text-transform:uppercase;"
                + "letter-spacing:.6px;"
                + "margin-bottom:7px;"
                + "}"
        );

        html.append(
                ".dado span {"
                + "color:#e5dfea;"
                + "}"
        );

        html.append(
                ".dado-bio {"
                + "grid-column:1/-1;"
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
                ".avaliacao-card {"
                + "display:flex;"
                + "gap:20px;"
                + "background:linear-gradient(145deg,#1b1023,#120a16);"
                + "border:1px solid #34203f;"
                + "border-radius:17px;"
                + "padding:20px;"
                + "margin-top:15px;"
                + "transition:.25s;"
                + "}"
        );

        html.append(
                ".avaliacao-card:hover {"
                + "border-color:#7c3aed;"
                + "transform:translateY(-3px);"
                + "box-shadow:0 12px 30px rgba(0,0,0,.3);"
                + "}"
        );

        html.append(
                ".capa-avaliacao {"
                + "width:110px;"
                + "height:150px;"
                + "object-fit:cover;"
                + "border-radius:10px;"
                + "flex-shrink:0;"
                + "}"
        );

        html.append(
                ".sem-capa-avaliacao {"
                + "width:110px;"
                + "height:150px;"
                + "border-radius:10px;"
                + "background:#24152f;"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:center;"
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
                + "margin:0 0 8px;"
                + "color:white;"
                + "font-size:21px;"
                + "}"
        );

        html.append(
                ".nota-pill {"
                + "display:inline-block;"
                + "padding:5px 10px;"
                + "background:#2a1738;"
                + "border-radius:20px;"
                + "color:#d8b4fe;"
                + "font-size:12px;"
                + "font-weight:bold;"
                + "margin-bottom:8px;"
                + "}"
        );

        html.append(
                ".estrelas-perfil {"
                + "color:#c084fc;"
                + "font-size:22px;"
                + "letter-spacing:2px;"
                + "margin-bottom:8px;"
                + "}"
        );

        html.append(
                ".texto-avaliacao p {"
                + "color:#aaa;"
                + "line-height:1.6;"
                + "}"
        );

        html.append(
                ".sem-avaliacoes {"
                + "background:#150c1d;"
                + "border:1px dashed #4d2b60;"
                + "padding:35px;"
                + "border-radius:15px;"
                + "text-align:center;"
                + "color:#8f8399;"
                + "}"
        );

        // =====================================================
        // RESPONSIVO
        // =====================================================

        html.append(
                "@media(max-width:800px){"
                + ".estatisticas{grid-template-columns:repeat(2,1fr);}"
                + ".dados-perfil{grid-template-columns:1fr;}"
                + ".dado-bio{grid-column:auto;}"
                + ".perfil-banner{height:180px;}"
                + ".perfil-card{margin-top:-60px;padding:25px;}"
                + ".foto-wrapper{margin-top:-105px;}"
                + ".foto-perfil{width:160px;height:160px;}"
                + "}"
        );

        html.append(
                "@media(max-width:550px){"
                + ".perfil-container{padding:10px;margin-top:25px;}"
                + ".estatisticas{grid-template-columns:1fr 1fr;gap:10px;}"
                + ".estatistica{padding:15px 8px;}"
                + ".estatistica-numero{font-size:22px;}"
                + ".horas-destaque{flex-direction:column;align-items:flex-start;}"
                + ".horas-total{font-size:27px;}"
                + ".avaliacao-card{flex-direction:column;}"
                + ".capa-avaliacao,.sem-capa-avaliacao{width:140px;height:190px;}"
                + ".nome-perfil{font-size:28px;}"
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
        // PERFIL
        // =====================================================

        html.append(
                "<main class='perfil-container'>"
        );

        html.append(
                "<div class='perfil-banner'></div>"
        );

        html.append(
                "<div class='perfil-card'>"
        );

        html.append(
                "<section class='perfil-topo'>"
        );

        html.append(
                "<div class='foto-wrapper'>"
        );

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
                    "<p class='bio'>"
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
        // ESTATÍSTICAS
        // =====================================================

        html.append(
                "<section class='estatisticas'>"
        );

        html.append(
                "<div class='estatistica'>"
                + "<div class='estatistica-icone'>🎮</div>"
                + "<span class='estatistica-numero'>"
                + quantidadeJogosZerados
                + "</span>"
                + "<span class='estatistica-label'>Jogos zerados</span>"
                + "</div>"
        );

        html.append(
                "<div class='estatistica'>"
                + "<div class='estatistica-icone'>⏱️</div>"
                + "<span class='estatistica-numero'>"
                + formatarNumero(totalHoras)
                + "</span>"
                + "<span class='estatistica-label'>Horas jogadas</span>"
                + "</div>"
        );

        html.append(
                "<div class='estatistica'>"
                + "<div class='estatistica-icone'>⭐</div>"
                + "<span class='estatistica-numero'>"
                + formatarNumero(mediaNotas)
                + "</span>"
                + "<span class='estatistica-label'>Média</span>"
                + "</div>"
        );

        html.append(
                "<div class='estatistica'>"
                + "<div class='estatistica-icone'>📝</div>"
                + "<span class='estatistica-numero'>"
                + quantidadeAvaliacoes
                + "</span>"
                + "<span class='estatistica-label'>Avaliações</span>"
                + "</div>"
        );

        html.append("</section>");

        // =====================================================
        // HORAS EM DESTAQUE
        // =====================================================

        html.append(
                "<section class='horas-destaque'>"
                + "<div class='horas-texto'>"
                + "<h3>⏱️ Seu tempo no mundo dos jogos</h3>"
                + "<p>Total acumulado das suas horas jogadas.</p>"
                + "</div>"
                + "<div class='horas-total'>"
                + formatarNumero(totalHoras)
                + " h"
                + "</div>"
                + "</section>"
        );

        // =====================================================
        // INFORMAÇÕES
        // =====================================================

        html.append(
                "<section>"
        );

        html.append(
                "<h2 class='secao-titulo'>"
                + "👾 Informações"
                + "<div class='linha-roxa'></div>"
                + "</h2>"
        );

        html.append(
                "<div class='dados-perfil'>"
        );

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

        html.append("</div>");

        html.append("</section>");

        // =====================================================
        // AVALIAÇÕES
        // =====================================================

        html.append(
                "<section class='avaliacoes'>"
        );

        html.append(
                "<h2 class='secao-titulo'>"
                + "⭐ Minhas avaliações"
                + "<div class='linha-roxa'></div>"
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

                possuiAvaliacao = true;

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
                        "<article class='avaliacao-card'>"
                );

                // -----------------------------------------
                // CAPA
                // -----------------------------------------

                if (capa != null &&
                        !capa.trim().isEmpty()) {

                    String caminhoCapa =
                            capa.trim();

                    if (!caminhoCapa.startsWith(
                            "http://")
                            &&
                            !caminhoCapa.startsWith(
                            "https://")) {

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
                            "<img "
                            + "class='capa-avaliacao' "
                            + "src='"
                            + escapar(caminhoCapa)
                            + "' "
                            + "alt='Capa'>"
                    );

                } else {

                    html.append(
                            "<div class='sem-capa-avaliacao'>"
                            + "🎮"
                            + "</div>"
                    );
                }

                // -----------------------------------------
                // TEXTO
                // -----------------------------------------

                html.append(
                        "<div class='texto-avaliacao'>"
                );

                html.append(
                        "<h3>"
                        + escapar(titulo)
                        + "</h3>"
                );

                html.append(
                        "<span class='nota-pill'>"
                        + "Nota "
                        + nota
                        + "/5"
                        + "</span>"
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

                html.append(
                        "</div>"
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

    // =====================================================
    // FORMATA NÚMEROS
    // =====================================================

    private String formatarNumero(
            double numero) {

        if (numero == Math.floor(numero)) {

            return String.valueOf(
                    (int) numero
            );
        }

        return String.format(
                java.util.Locale.US,
                "%.1f",
                numero
        );
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