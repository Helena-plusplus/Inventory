
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

    // =====================================================
    // GET - ABRIR PÁGINA DE AVALIAÇÃO
    // =====================================================

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

        int idJogo;

        try {

            idJogo =
                    Integer.parseInt(idTexto);

        } catch (Exception e) {

            response.sendRedirect("biblioteca");
            return;
        }

        String titulo = "";
        String capa = "";

        try {

            Connection conexao =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conexao.prepareStatement(
                            "SELECT titulo, capa " +
                            "FROM jogo " +
                            "WHERE id = ?"
                    );

            stmt.setInt(1, idJogo);

            ResultSet rs =
                    stmt.executeQuery();

            if (!rs.next()) {

                rs.close();
                stmt.close();
                conexao.close();

                response.sendRedirect(
                        "biblioteca"
                );

                return;
            }

            titulo =
                    rs.getString("titulo");

            capa =
                    rs.getString("capa");

            rs.close();
            stmt.close();
            conexao.close();

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect(
                    "biblioteca"
            );

            return;
        }

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
                "<meta name='viewport' " +
                "content='width=device-width, initial-scale=1.0'>"
        );

        html.append(
                "<title>Avaliar "
                + escapar(titulo)
                + " - Inventory</title>"
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
                + "background:"
                + "radial-gradient(circle at top,#35105f,#17121f 45%,#0d0b11);"
                + "min-height:100vh;"
                + "}"
        );

        html.append(
                ".avaliacao-container{"
                + "max-width:680px;"
                + "margin:45px auto;"
                + "padding:35px;"
                + "background:linear-gradient(145deg,#21142c,#140b1b);"
                + "border:1px solid #54256f;"
                + "border-radius:22px;"
                + "box-shadow:0 20px 50px rgba(0,0,0,.4);"
                + "text-align:center;"
                + "}"
        );

        html.append(
                ".capa-avaliacao{"
                + "width:210px;"
                + "height:295px;"
                + "object-fit:cover;"
                + "border-radius:12px;"
                + "display:block;"
                + "margin:0 auto 22px;"
                + "box-shadow:0 15px 35px rgba(0,0,0,.4);"
                + "}"
        );

        html.append(
                ".sem-capa{"
                + "width:210px;"
                + "height:295px;"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:center;"
                + "background:#17101f;"
                + "border:1px dashed #56356a;"
                + "border-radius:12px;"
                + "color:#888;"
                + "margin:0 auto 22px;"
                + "}"
        );

        html.append(
                ".titulo-jogo{"
                + "font-size:28px;"
                + "margin-bottom:8px;"
                + "}"
        );

        html.append(
                ".subtitulo{"
                + "color:#aaa;"
                + "margin-bottom:22px;"
                + "}"
        );

        // ESTRELAS

        html.append(
                ".estrelas{"
                + "display:flex;"
                + "justify-content:center;"
                + "gap:6px;"
                + "margin:22px 0 30px;"
                + "}"
        );

        html.append(
                ".estrela{"
                + "font-size:48px;"
                + "color:#554b5e;"
                + "cursor:pointer;"
                + "transition:.2s;"
                + "user-select:none;"
                + "}"
        );

        html.append(
                ".estrela:hover{"
                + "transform:scale(1.1);"
                + "}"
        );

        html.append(
                ".estrela.selecionada{"
                + "color:#c084fc;"
                + "text-shadow:0 0 12px rgba(192,132,252,.35);"
                + "}"
        );

        // CAMPOS

        html.append(
                ".campo-area{"
                + "text-align:left;"
                + "margin-bottom:20px;"
                + "}"
        );

        html.append(
                ".campo-area label{"
                + "display:block;"
                + "margin-bottom:8px;"
                + "color:#ddd;"
                + "font-weight:bold;"
                + "}"
        );

        html.append(
                ".campo-horas{"
                + "width:100%;"
                + "box-sizing:border-box;"
                + "padding:14px;"
                + "background:#100b15;"
                + "color:white;"
                + "border:1px solid #47305a;"
                + "border-radius:9px;"
                + "font-size:15px;"
                + "outline:none;"
                + "}"
        );

        html.append(
                ".campo-horas:focus{"
                + "border-color:#a855f7;"
                + "box-shadow:0 0 0 3px rgba(168,85,247,.12);"
                + "}"
        );

        html.append(
                ".campo-resenha{"
                + "width:100%;"
                + "height:150px;"
                + "box-sizing:border-box;"
                + "padding:14px;"
                + "background:#100b15;"
                + "color:white;"
                + "border:1px solid #47305a;"
                + "border-radius:9px;"
                + "resize:vertical;"
                + "font-family:Arial,sans-serif;"
                + "font-size:15px;"
                + "outline:none;"
                + "}"
        );

        html.append(
                ".campo-resenha:focus{"
                + "border-color:#a855f7;"
                + "box-shadow:0 0 0 3px rgba(168,85,247,.12);"
                + "}"
        );

        // BOTÃO

        html.append(
                ".botao-postar{"
                + "width:100%;"
                + "margin-top:10px;"
                + "padding:14px;"
                + "border:none;"
                + "border-radius:10px;"
                + "background:linear-gradient(135deg,#7c3aed,#a855f7);"
                + "color:white;"
                + "font-weight:bold;"
                + "font-size:15px;"
                + "cursor:pointer;"
                + "transition:.2s;"
                + "}"
        );

        html.append(
                ".botao-postar:hover{"
                + "transform:translateY(-2px);"
                + "box-shadow:0 10px 25px rgba(124,58,237,.3);"
                + "}"
        );

        html.append(
                "@media(max-width:650px){"
                + ".avaliacao-container{"
                + "margin:25px 12px;"
                + "padding:25px 20px;"
                + "}"
                + ".capa-avaliacao,.sem-capa{"
                + "width:170px;"
                + "height:240px;"
                + "}"
                + ".estrela{font-size:40px;}"
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
        // CONTEÚDO
        // =====================================================

        html.append(
                "<main class='avaliacao-container'>"
        );

        // CAPA

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
                    "<img class='capa-avaliacao' "
                    + "src='"
                    + escapar(caminhoCapa)
                    + "' "
                    + "alt='Capa do jogo'>"
            );

        } else {

            html.append(
                    "<div class='sem-capa'>"
                    + "🎮 Sem capa"
                    + "</div>"
            );
        }

        html.append(
                "<h2 class='titulo-jogo'>"
                + escapar(titulo)
                + "</h2>"
        );

        html.append(
                "<p class='subtitulo'>"
                + "Como foi sua experiência com esse jogo?"
                + "</p>"
        );

        // =====================================================
        // FORM
        // =====================================================

        html.append(
                "<form method='POST' "
                + "action='avaliar' "
                + "id='formAvaliacao'>"
        );

        html.append(
                "<input type='hidden' "
                + "name='idJogo' "
                + "value='"
                + idJogo
                + "'>"
        );

        html.append(
                "<input type='hidden' "
                + "name='nota' "
                + "id='nota' "
                + "value=''>"
        );

        // ESTRELAS

        html.append(
                "<p><strong>Sua nota</strong></p>"
        );

        html.append(
                "<div class='estrelas'>"
        );

        for (int i = 1; i <= 5; i++) {

            html.append(
                    "<span "
                    + "class='estrela' "
                    + "data-nota='"
                    + i
                    + "'>★</span>"
            );
        }

        html.append("</div>");

        // =====================================================
        // HORAS
        // =====================================================

        html.append(
                "<div class='campo-area'>"
                + "<label for='horas'>"
                + "⏱️ Horas jogadas"
                + "</label>"
                + "<input "
                + "class='campo-horas' "
                + "type='number' "
                + "id='horas' "
                + "name='horas' "
                + "min='0' "
                + "step='0.1' "
                + "placeholder='Ex: 25.5' "
                + "required>"
                + "</div>"
        );

        // =====================================================
        // RESENHA
        // =====================================================

        html.append(
                "<div class='campo-area'>"
                + "<label for='comentario'>"
                + "📝 Sua resenha"
                + "</label>"
                + "<textarea "
                + "class='campo-resenha' "
                + "id='comentario' "
                + "name='comentario' "
                + "placeholder='Escreva sua opinião sobre o jogo...' "
                + "required></textarea>"
                + "</div>"
        );

        html.append(
                "<button "
                + "class='botao-postar' "
                + "type='submit'>"
                + "💾 Salvar avaliação"
                + "</button>"
        );

        html.append("</form>");

        html.append("</main>");

        // =====================================================
        // JAVASCRIPT
        // =====================================================

        html.append("<script>");

        html.append(
                "var estrelas = "
                + "document.querySelectorAll('.estrela');"
        );

        html.append(
                "var campoNota = "
                + "document.getElementById('nota');"
        );

        html.append(
                "for(var i=0;i<estrelas.length;i++){"
                + "estrelas[i].addEventListener('click',function(){"
                + "var valor=parseInt(this.getAttribute('data-nota'));"
                + "campoNota.value=valor;"
                + "for(var j=0;j<estrelas.length;j++){"
                + "var numero=parseInt("
                + "estrelas[j].getAttribute('data-nota')"
                + ");"
                + "if(numero<=valor){"
                + "estrelas[j].classList.add('selecionada');"
                + "}else{"
                + "estrelas[j].classList.remove('selecionada');"
                + "}"
                + "}"
                + "});"
                + "}"
        );

        html.append(
                "document.getElementById('formAvaliacao')"
                + ".addEventListener('submit',function(event){"
                + "if(campoNota.value===''){"
                + "event.preventDefault();"
                + "alert('Escolha uma nota de 1 a 5 estrelas.');"
                + "}"
                + "});"
        );

        html.append("</script>");

        html.append("</body>");

        html.append("</html>");

        response.getWriter().println(
                html.toString()
        );
    }

    // =====================================================
    // POST - SALVAR AVALIAÇÃO
    // =====================================================

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

        Connection conexao = null;

        try {

            Usuario usuario =
                    (Usuario) sessao.getAttribute("usuario");

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

            String comentario =
                    request.getParameter(
                            "comentario"
                    );

            String horasTexto =
                    request.getParameter(
                            "horas"
                    );

            double horas = 0;

            if (horasTexto != null &&
                    !horasTexto.trim().isEmpty()) {

                horas =
                        Double.parseDouble(
                                horasTexto
                        );
            }

            if (horas < 0) {
                horas = 0;
            }

            if (nota < 1 ||
                    nota > 5) {

                response.sendRedirect(
                        "avaliar?id=" + idJogo
                );

                return;
            }

            conexao =
                    Conexao.conectar();

            conexao.setAutoCommit(false);

            // =================================================
            // AVALIACAO
            // =================================================

            String inserirAvaliacao =
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

            PreparedStatement stmtAvaliacao =
                    conexao.prepareStatement(
                            inserirAvaliacao
                    );

            stmtAvaliacao.setInt(
                    1,
                    idUsuario
            );

            stmtAvaliacao.setInt(
                    2,
                    idJogo
            );

            stmtAvaliacao.setDouble(
                    3,
                    nota
            );

            stmtAvaliacao.setString(
                    4,
                    comentario
            );

            stmtAvaliacao.setDouble(
                    5,
                    horas
            );

            stmtAvaliacao.executeUpdate();

            stmtAvaliacao.close();

            // =================================================
            // BIBLIOTECA
            // =================================================

            String verificarBiblioteca =
                    "SELECT id "
                    + "FROM biblioteca "
                    + "WHERE id_usuario = ? "
                    + "AND id_jogo = ?";

            PreparedStatement stmtBusca =
                    conexao.prepareStatement(
                            verificarBiblioteca
                    );

            stmtBusca.setInt(
                    1,
                    idUsuario
            );

            stmtBusca.setInt(
                    2,
                    idJogo
            );

            ResultSet rs =
                    stmtBusca.executeQuery();

            boolean existeNaBiblioteca =
                    rs.next();

            rs.close();
            stmtBusca.close();

            if (existeNaBiblioteca) {

                String atualizarBiblioteca =
                        "UPDATE biblioteca SET "
                        + "status = 'zerado', "
                        + "horas_jogadas = ? "
                        + "WHERE id_usuario = ? "
                        + "AND id_jogo = ?";

                PreparedStatement stmtUpdate =
                        conexao.prepareStatement(
                                atualizarBiblioteca
                        );

                stmtUpdate.setDouble(
                        1,
                        horas
                );

                stmtUpdate.setInt(
                        2,
                        idUsuario
                );

                stmtUpdate.setInt(
                        3,
                        idJogo
                );

                stmtUpdate.executeUpdate();

                stmtUpdate.close();

            } else {

                String inserirBiblioteca =
                        "INSERT INTO biblioteca "
                        + "(id_usuario, id_jogo, "
                        + "status, horas_jogadas) "
                        + "VALUES (?, ?, 'zerado', ?)";

                PreparedStatement stmtInsert =
                        conexao.prepareStatement(
                                inserirBiblioteca
                        );

                stmtInsert.setInt(
                        1,
                        idUsuario
                );

                stmtInsert.setInt(
                        2,
                        idJogo
                );

                stmtInsert.setDouble(
                        3,
                        horas
                );

                stmtInsert.executeUpdate();

                stmtInsert.close();
            }

            // =================================================
            // CONFIRMAR
            // =================================================

            conexao.commit();

            conexao.setAutoCommit(true);

            System.out.println(
                    "================================="
            );

            System.out.println(
                    "AVALIAÇÃO SALVA"
            );

            System.out.println(
                    "ID USUARIO: "
                    + idUsuario
            );

            System.out.println(
                    "ID JOGO: "
                    + idJogo
            );

            System.out.println(
                    "NOTA: "
                    + nota
            );

            System.out.println(
                    "HORAS JOGADAS: "
                    + horas
            );

            System.out.println(
                    "================================="
            );

            conexao.close();

            response.sendRedirect(
                    "biblioteca#zerados"
            );

        } catch (Exception e) {

            e.printStackTrace();

            try {

                if (conexao != null) {

                    conexao.rollback();

                    conexao.close();
                }

            } catch (Exception erro) {

                erro.printStackTrace();
            }

            response.sendRedirect(
                    "biblioteca"
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

