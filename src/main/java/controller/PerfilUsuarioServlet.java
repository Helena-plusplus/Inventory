package controller;

import dao.Conexao;
import dao.UsuarioDAO;
import model.Usuario;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/perfil-usuario")
public class PerfilUsuarioServlet extends HttpServlet {

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

            response.sendRedirect(
                    "buscar-usuarios"
            );

            return;
        }

        int idPerfil;

        try {

            idPerfil =
                    Integer.parseInt(
                            idTexto.trim()
                    );

        } catch (Exception e) {

            response.sendRedirect(
                    "buscar-usuarios"
            );

            return;
        }

        Usuario usuarioLogado =
                (Usuario) sessao.getAttribute(
                        "usuario"
                );

        int idLogado =
                usuarioLogado.getId();

        UsuarioDAO dao =
                new UsuarioDAO();

        Usuario perfil =
                dao.buscarPorId(
                        idPerfil
                );

        if (perfil == null) {

            response.sendRedirect(
                    "buscar-usuarios"
            );

            return;
        }

        // =====================================================
        // EMBLEMA ESPECIAL
        // =====================================================

        boolean jogadorEspecial =
                perfil.getEmail() != null
                &&
                perfil.getEmail()
                        .trim()
                        .equalsIgnoreCase(
                                "rebecarodriguesduarte2@gmail.com"
                        );

        // =====================================================
        // SEGUIR
        // =====================================================

        boolean mesmoUsuario =
                idLogado == idPerfil;

        boolean seguindo =
                false;

        if (!mesmoUsuario) {

            seguindo =
                    dao.seguindo(
                            idLogado,
                            idPerfil
                    );
        }

        int totalSeguidores =
                dao.contarSeguidores(
                        idPerfil
                );

        int totalSeguindo =
                dao.contarSeguindo(
                        idPerfil
                );

        ArrayList<Usuario> seguidores =
                dao.listarSeguidores(
                        idPerfil
                );

        ArrayList<Usuario> seguindoLista =
                dao.listarSeguindo(
                        idPerfil
                );

        ArrayList<String[]> favoritos =
                carregarFavoritos(
                        idPerfil
                );

        ArrayList<String[]> listas =
                carregarListas(
                        idPerfil
                );

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
                + "content='width=device-width, "
                + "initial-scale=1.0'>"
        );

        html.append(
                "<title>"
                + escapar(perfil.getNome())
                + " - Inventory</title>"
        );

        html.append(
                "<link rel='stylesheet' "
                + "href='style.css'>"
        );

        html.append("<style>");

        html.append(
                "body{"
                + "margin:0;"
                + "background:"
                + "radial-gradient("
                + "circle at top,#35105f,"
                + "#160b22 45%,#09060d);"
                + "min-height:100vh;"
                + "color:white;"
                + "}"
        );

        html.append(
                ".perfil-container{"
                + "max-width:1100px;"
                + "margin:45px auto;"
                + "padding:20px;"
                + "}"
        );

        html.append(
                ".perfil-box{"
                + "background:"
                + "linear-gradient("
                + "145deg,#21142c,#140b1b);"
                + "border:1px solid #4b2464;"
                + "border-radius:24px;"
                + "padding:35px;"
                + "}"
        );

        html.append(
                ".topo{"
                + "text-align:center;"
                + "padding-bottom:30px;"
                + "border-bottom:1px solid #382043;"
                + "}"
        );

        html.append(
                ".foto{"
                + "width:155px;"
                + "height:155px;"
                + "object-fit:cover;"
                + "border-radius:50%;"
                + "border:5px solid #7c3aed;"
                + "}"
        );

        html.append(
                ".sem-foto{"
                + "width:155px;"
                + "height:155px;"
                + "margin:auto;"
                + "border-radius:50%;"
                + "background:#24142e;"
                + "border:5px solid #7c3aed;"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:center;"
                + "color:#888;"
                + "}"
        );

        html.append(
                ".nome-area{"
                + "display:flex;"
                + "justify-content:center;"
                + "align-items:center;"
                + "gap:14px;"
                + "flex-wrap:wrap;"
                + "margin-top:18px;"
                + "}"
        );

        html.append(
                ".nome{"
                + "font-size:32px;"
                + "margin:0;"
                + "}"
        );

        html.append(
                ".username{"
                + "color:#b98be8;"
                + "margin-top:8px;"
                + "}"
        );

        html.append(
                ".bio{"
                + "max-width:700px;"
                + "margin:17px auto;"
                + "color:#cfc7d3;"
                + "line-height:1.6;"
                + "}"
        );

        // =====================================================
        // EMBLEMA
        // =====================================================

        html.append(
                ".emblema-my-love{"
                + "display:inline-flex;"
                + "align-items:center;"
                + "gap:8px;"
                + "padding:10px 18px;"
                + "border-radius:30px;"
                + "background:"
                + "linear-gradient("
                + "135deg,#351344,#6d2b8c,#9d4edd);"
                + "border:2px solid #d8a4f7;"
                + "color:#ffe8ff;"
                + "font-size:15px;"
                + "font-weight:bold;"
                + "box-shadow:"
                + "0 0 15px rgba(216,164,247,.3);"
                + "}"
        );

        html.append(
                ".coracao-my-love{"
                + "font-size:27px;"
                + "color:#ffd6ff;"
                + "}"
        );

        // =====================================================
        // ESTATISTICAS
        // =====================================================

        html.append(
                ".estatisticas{"
                + "display:flex;"
                + "justify-content:center;"
                + "gap:15px;"
                + "margin-top:25px;"
                + "flex-wrap:wrap;"
                + "}"
        );

        html.append(
                ".estatistica{"
                + "min-width:120px;"
                + "padding:16px 25px;"
                + "background:#160d1e;"
                + "border:1px solid #392348;"
                + "border-radius:13px;"
                + "text-align:center;"
                + "}"
        );

        html.append(
                ".estatistica strong{"
                + "display:block;"
                + "font-size:24px;"
                + "color:#c084fc;"
                + "}"
        );

        html.append(
                ".estatistica span{"
                + "font-size:12px;"
                + "color:#999;"
                + "}"
        );

        // =====================================================
        // BOTAO
        // =====================================================

        html.append(
                ".botao-seguir{"
                + "margin-top:22px;"
                + "padding:13px 38px;"
                + "border:none;"
                + "border-radius:10px;"
                + "background:"
                + "linear-gradient("
                + "135deg,#7c3aed,#a855f7);"
                + "color:white;"
                + "font-weight:bold;"
                + "cursor:pointer;"
                + "}"
        );

        html.append(
                ".botao-seguindo{"
                + "background:#29183a;"
                + "border:1px solid #8b5cf6;"
                + "}"
        );

        // =====================================================
        // SECOES
        // =====================================================

        html.append(
                ".secao{"
                + "margin-top:40px;"
                + "}"
        );

        html.append(
                ".titulo{"
                + "font-size:24px;"
                + "margin-bottom:8px;"
                + "}"
        );

        html.append(
                ".linha{"
                + "width:55px;"
                + "height:3px;"
                + "background:"
                + "linear-gradient("
                + "90deg,#8b2be2,#c084fc);"
                + "border-radius:10px;"
                + "margin-bottom:22px;"
                + "}"
        );

        // =====================================================
        // FAVORITOS
        // =====================================================

        html.append(
                ".favoritos{"
                + "display:grid;"
                + "grid-template-columns:"
                + "repeat(5,1fr);"
                + "gap:16px;"
                + "}"
        );

        html.append(
                ".favorito{"
                + "background:#160d1e;"
                + "border:1px solid #372145;"
                + "border-radius:14px;"
                + "padding:10px;"
                + "}"
        );

        html.append(
                ".favorito-capa{"
                + "width:100%;"
                + "height:220px;"
                + "object-fit:cover;"
                + "display:block;"
                + "border-radius:9px;"
                + "}"
        );

        html.append(
                ".favorito-titulo{"
                + "font-size:14px;"
                + "font-weight:bold;"
                + "margin-top:9px;"
                + "}"
        );

        // =====================================================
        // LISTAS
        // =====================================================

        html.append(
                ".lista-perfil{"
                + "background:#160d1e;"
                + "border:1px solid #392348;"
                + "border-radius:16px;"
                + "padding:20px;"
                + "margin-bottom:20px;"
                + "}"
        );

        html.append(
                ".nome-lista{"
                + "font-size:21px;"
                + "font-weight:bold;"
                + "margin-bottom:18px;"
                + "}"
        );

        html.append(
                ".jogos-lista{"
                + "display:grid;"
                + "grid-template-columns:"
                + "repeat(auto-fill,minmax(145px,1fr));"
                + "gap:14px;"
                + "}"
        );

        html.append(
                ".jogo-lista{"
                + "background:#21142c;"
                + "border:1px solid #35203f;"
                + "border-radius:12px;"
                + "padding:8px;"
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
                ".nome-jogo{"
                + "font-size:13px;"
                + "font-weight:bold;"
                + "margin-top:8px;"
                + "}"
        );

        // =====================================================
        // USUARIOS
        // =====================================================

        html.append(
                ".usuarios{"
                + "display:grid;"
                + "grid-template-columns:"
                + "repeat(auto-fill,minmax(230px,1fr));"
                + "gap:12px;"
                + "}"
        );

        html.append(
                ".card-usuario{"
                + "display:flex;"
                + "align-items:center;"
                + "gap:12px;"
                + "padding:13px;"
                + "background:#160d1e;"
                + "border:1px solid #34203f;"
                + "border-radius:13px;"
                + "color:white;"
                + "text-decoration:none;"
                + "}"
        );

        html.append(
                ".mini-foto{"
                + "width:52px;"
                + "height:52px;"
                + "border-radius:50%;"
                + "object-fit:cover;"
                + "border:2px solid #7c3aed;"
                + "}"
        );

        html.append(
                ".mini-sem-foto{"
                + "width:52px;"
                + "height:52px;"
                + "border-radius:50%;"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:center;"
                + "background:#281733;"
                + "color:#888;"
                + "font-size:10px;"
                + "}"
        );

        html.append(
                ".info strong{"
                + "display:block;"
                + "}"
        );

        html.append(
                ".info span{"
                + "font-size:12px;"
                + "color:#a855f7;"
                + "}"
        );

        html.append(
                ".vazio{"
                + "padding:25px;"
                + "background:#160d1e;"
                + "border:1px dashed #493254;"
                + "border-radius:13px;"
                + "text-align:center;"
                + "color:#8f8496;"
                + "}"
        );

        html.append(
                "@media(max-width:850px){"
                + ".favoritos{"
                + "grid-template-columns:repeat(3,1fr);"
                + "}"
                + "}"
        );

        html.append(
                "@media(max-width:600px){"
                + ".perfil-container{"
                + "padding:10px;"
                + "}"
                + ".perfil-box{"
                + "padding:22px 16px;"
                + "}"
                + ".favoritos{"
                + "grid-template-columns:repeat(2,1fr);"
                + "}"
                + ".nome{"
                + "font-size:27px;"
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
        // MAIN
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
                "<section class='topo'>"
        );

        String caminhoFoto =
                prepararFoto(
                        request,
                        perfil.getFoto()
                );

        if (caminhoFoto != null) {

            html.append(
                    "<img "
                    + "class='foto' "
                    + "src='"
                    + escapar(caminhoFoto)
                    + "' "
                    + "alt='Foto de "
                    + escapar(perfil.getNome())
                    + "'>"
            );

        } else {

            html.append(
                    "<div class='sem-foto'>"
                    + "Sem foto"
                    + "</div>"
            );
        }

        // =====================================================
        // NOME
        // =====================================================

        html.append(
                "<div class='nome-area'>"
        );

        html.append(
                "<h2 class='nome'>"
                + escapar(perfil.getNome())
                + "</h2>"
        );

        if (jogadorEspecial) {

            html.append(
                    "<span class='emblema-my-love'>"
                    + "<span class='coracao-my-love'>♡</span>"
                    + " My Love"
                    + "</span>"
            );
        }

        html.append(
                "</div>"
        );

        html.append(
                "<div class='username'>"
                + "@"
                + escapar(
                        perfil.getUsername()
                  )
                + "</div>"
        );

        if (perfil.getBio() != null &&
                !perfil.getBio().trim().isEmpty()) {

            html.append(
                    "<div class='bio'>"
                    + escapar(perfil.getBio())
                    + "</div>"
            );
        }

        // =====================================================
        // ESTATISTICAS
        // =====================================================

        html.append(
                "<div class='estatisticas'>"
        );

        html.append(
                "<div class='estatistica'>"
                + "<strong>"
                + totalSeguidores
                + "</strong>"
                + "<span>Seguidores</span>"
                + "</div>"
        );

        html.append(
                "<div class='estatistica'>"
                + "<strong>"
                + totalSeguindo
                + "</strong>"
                + "<span>Seguindo</span>"
                + "</div>"
        );

        html.append(
                "</div>"
        );

        // =====================================================
        // SEGUIR
        // =====================================================

        if (!mesmoUsuario) {

            html.append(
                    "<form "
                    + "method='POST' "
                    + "action='seguir'>"
            );

            html.append(
                    "<input "
                    + "type='hidden' "
                    + "name='idUsuario' "
                    + "value='"
                    + idPerfil
                    + "'>"
            );

            if (seguindo) {

                html.append(
                        "<input "
                        + "type='hidden' "
                        + "name='acao' "
                        + "value='deixar'>"
                );

                html.append(
                        "<button "
                        + "type='submit' "
                        + "class='botao-seguir "
                        + "botao-seguindo'>"
                        + "Seguindo"
                        + "</button>"
                );

            } else {

                html.append(
                        "<input "
                        + "type='hidden' "
                        + "name='acao' "
                        + "value='seguir'>"
                );

                html.append(
                        "<button "
                        + "type='submit' "
                        + "class='botao-seguir'>"
                        + "Seguir"
                        + "</button>"
                );
            }

            html.append("</form>");
        }

        html.append(
                "</section>"
        );

        // =====================================================
        // FAVORITOS
        // =====================================================

        html.append(
                "<section class='secao'>"
        );

        html.append(
                "<h2 class='titulo'>Favoritos</h2>"
        );

        html.append(
                "<div class='linha'></div>"
        );

        if (favoritos.isEmpty()) {

            html.append(
                    "<div class='vazio'>"
                    + "Este usuário ainda não selecionou favoritos."
                    + "</div>"
            );

        } else {

            html.append(
                    "<div class='favoritos'>"
            );

            for (String[] favorito :
                    favoritos) {

                String titulo =
                        favorito[1];

                String capa =
                        favorito[3];

                html.append(
                        "<div class='favorito'>"
                );

                String caminhoCapa =
                        prepararCapa(
                                request,
                                capa
                        );

                if (caminhoCapa != null) {

                    html.append(
                            "<img "
                            + "class='favorito-capa' "
                            + "src='"
                            + escapar(caminhoCapa)
                            + "' "
                            + "alt='"
                            + escapar(titulo)
                            + "'>"
                    );

                } else {

                    html.append(
                            "<div "
                            + "class='favorito-capa' "
                            + "style='display:flex;"
                            + "align-items:center;"
                            + "justify-content:center;"
                            + "color:#777;'>"
                            + "Sem capa"
                            + "</div>"
                    );
                }

                html.append(
                        "<div class='favorito-titulo'>"
                        + escapar(titulo)
                        + "</div>"
                );

                html.append(
                        "</div>"
                );
            }

            html.append("</div>");
        }

        html.append("</section>");

        // =====================================================
        // LISTAS
        // =====================================================

        html.append(
                "<section class='secao'>"
        );

        html.append(
                "<h2 class='titulo'>Listas</h2>"
        );

        html.append(
                "<div class='linha'></div>"
        );

        if (listas.isEmpty()) {

            html.append(
                    "<div class='vazio'>"
                    + "Este usuário ainda não criou nenhuma lista."
                    + "</div>"
            );

        } else {

            for (String[] lista :
                    listas) {

                int idLista =
                        Integer.parseInt(
                                lista[0]
                        );

                String nomeLista =
                        lista[1];

                html.append(
                        "<div class='lista-perfil'>"
                );

                html.append(
                        "<div class='nome-lista'>"
                        + escapar(nomeLista)
                        + "</div>"
                );

                ArrayList<String[]> jogos =
                        carregarJogosLista(
                                idLista
                        );

                if (jogos.isEmpty()) {

                    html.append(
                            "<div class='vazio'>"
                            + "Esta lista ainda não possui jogos."
                            + "</div>"
                    );

                } else {

                    html.append(
                            "<div class='jogos-lista'>"
                    );

                    for (String[] jogo :
                            jogos) {

                        String titulo =
                                jogo[1];

                        String capa =
                                jogo[2];

                        html.append(
                                "<div "
                                + "class='jogo-lista'>"
                        );

                        String caminho =
                                prepararCapa(
                                        request,
                                        capa
                                );

                        if (caminho != null) {

                            html.append(
                                    "<img "
                                    + "class='capa-lista' "
                                    + "src='"
                                    + escapar(caminho)
                                    + "' "
                                    + "alt='"
                                    + escapar(titulo)
                                    + "'>"
                            );

                        } else {

                            html.append(
                                    "<div "
                                    + "class='capa-lista' "
                                    + "style='display:flex;"
                                    + "align-items:center;"
                                    + "justify-content:center;"
                                    + "color:#777;'>"
                                    + "Sem capa"
                                    + "</div>"
                            );
                        }

                        html.append(
                                "<div class='nome-jogo'>"
                                + escapar(titulo)
                                + "</div>"
                        );

                        html.append("</div>");
                    }

                    html.append("</div>");
                }

                html.append("</div>");
            }
        }

        html.append("</section>");

        // =====================================================
        // SEGUIDORES
        // =====================================================

        html.append(
                "<section class='secao'>"
        );

        html.append(
                "<h2 class='titulo'>Seguidores</h2>"
        );

        html.append(
                "<div class='linha'></div>"
        );

        if (seguidores.isEmpty()) {

            html.append(
                    "<div class='vazio'>"
                    + "Este usuário ainda não possui seguidores."
                    + "</div>"
            );

        } else {

            html.append(
                    "<div class='usuarios'>"
            );

            for (Usuario u :
                    seguidores) {

                html.append(
                        cardUsuario(
                                request,
                                u
                        )
                );
            }

            html.append(
                    "</div>"
            );
        }

        html.append("</section>");

        // =====================================================
        // SEGUINDO
        // =====================================================

        html.append(
                "<section class='secao'>"
        );

        html.append(
                "<h2 class='titulo'>Seguindo</h2>"
        );

        html.append(
                "<div class='linha'></div>"
        );

        if (seguindoLista.isEmpty()) {

            html.append(
                    "<div class='vazio'>"
                    + "Este usuário ainda não segue ninguém."
                    + "</div>"
            );

        } else {

            html.append(
                    "<div class='usuarios'>"
            );

            for (Usuario u :
                    seguindoLista) {

                html.append(
                        cardUsuario(
                                request,
                                u
                        )
                );
            }

            html.append(
                    "</div>"
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
    // LISTAS
    // =====================================================

    private ArrayList<String[]> carregarListas(
            int idUsuario) {

        ArrayList<String[]> listas =
                new ArrayList<String[]>();

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            conexao =
                    Conexao.conectar();

            if (conexao == null) {
                return listas;
            }

            String criarTabela =
                    "CREATE TABLE IF NOT EXISTS lista ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "id_usuario INTEGER NOT NULL,"
                    + "nome TEXT NOT NULL,"
                    + "data_criacao TEXT "
                    + "DEFAULT CURRENT_TIMESTAMP"
                    + ")";

            PreparedStatement tabela =
                    conexao.prepareStatement(
                            criarTabela
                    );

            tabela.executeUpdate();
            tabela.close();

            String sql =
                    "SELECT id, nome "
                    + "FROM lista "
                    + "WHERE id_usuario = ? "
                    + "ORDER BY id DESC";

            stmt =
                    conexao.prepareStatement(
                            sql
                    );

            stmt.setInt(
                    1,
                    idUsuario
            );

            rs =
                    stmt.executeQuery();

            while (rs.next()) {

                listas.add(
                        new String[]{
                            String.valueOf(
                                    rs.getInt("id")
                            ),
                            rs.getString("nome")
                        }
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (stmt != null) {
                    stmt.close();
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

        return listas;
    }

    // =====================================================
    // JOGOS DA LISTA
    // =====================================================

    private ArrayList<String[]> carregarJogosLista(
            int idLista) {

        ArrayList<String[]> jogos =
                new ArrayList<String[]>();

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            conexao =
                    Conexao.conectar();

            if (conexao == null) {
                return jogos;
            }

            String sql =
                    "SELECT "
                    + "j.id, "
                    + "j.titulo, "
                    + "j.capa "
                    + "FROM lista_jogo lj "
                    + "INNER JOIN jogo j "
                    + "ON j.id = lj.id_jogo "
                    + "INNER JOIN lista l "
                    + "ON l.id = lj.id_lista "
                    + "WHERE l.id = ? "
                    + "ORDER BY lj.id ASC";

            stmt =
                    conexao.prepareStatement(
                            sql
                    );

            stmt.setInt(
                    1,
                    idLista
            );

            rs =
                    stmt.executeQuery();

            while (rs.next()) {

                jogos.add(
                        new String[]{
                            String.valueOf(
                                    rs.getInt("id")
                            ),
                            rs.getString("titulo"),
                            rs.getString("capa")
                        }
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (stmt != null) {
                    stmt.close();
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

        return jogos;
    }

    // =====================================================
    // FAVORITOS
    // =====================================================

    private ArrayList<String[]> carregarFavoritos(
            int idUsuario) {

        ArrayList<String[]> favoritos =
                new ArrayList<String[]>();

        Connection conexao = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            conexao =
                    Conexao.conectar();

            if (conexao == null) {
                return favoritos;
            }

            String criarTabela =
                    "CREATE TABLE IF NOT EXISTS favorito ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "id_usuario INTEGER NOT NULL,"
                    + "id_jogo INTEGER NOT NULL,"
                    + "data_adicionado TEXT "
                    + "DEFAULT CURRENT_TIMESTAMP,"
                    + "UNIQUE(id_usuario,id_jogo)"
                    + ")";

            PreparedStatement tabela =
                    conexao.prepareStatement(
                            criarTabela
                    );

            tabela.executeUpdate();
            tabela.close();

            String sql =
                    "SELECT "
                    + "j.id, "
                    + "j.titulo, "
                    + "j.genero, "
                    + "j.capa "
                    + "FROM favorito f "
                    + "INNER JOIN jogo j "
                    + "ON j.id = f.id_jogo "
                    + "WHERE f.id_usuario = ? "
                    + "ORDER BY f.data_adicionado ASC "
                    + "LIMIT 5";

            stmt =
                    conexao.prepareStatement(
                            sql
                    );

            stmt.setInt(
                    1,
                    idUsuario
            );

            rs =
                    stmt.executeQuery();

            while (rs.next()) {

                favoritos.add(
                        new String[]{
                            String.valueOf(
                                    rs.getInt("id")
                            ),
                            rs.getString("titulo"),
                            rs.getString("genero"),
                            rs.getString("capa")
                        }
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (stmt != null) {
                    stmt.close();
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

        return favoritos;
    }

    // =====================================================
    // FOTO
    // =====================================================

    private String prepararFoto(
            HttpServletRequest request,
            String foto)
            throws IOException {

        if (foto == null ||
                foto.trim().isEmpty()) {

            return null;
        }

        String caminho =
                foto.trim();

        if (caminho.startsWith("http://")
                ||
                caminho.startsWith("https://")) {

            return caminho;
        }

        while (
                caminho.startsWith("/")
        ) {

            caminho =
                    caminho.substring(1);
        }

        return
                request.getContextPath()
                + "/foto-perfil?arquivo="
                + URLEncoder.encode(
                        caminho,
                        "UTF-8"
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
        }

        if (caminho.matches("\\d+")) {

            return
                    "https://shared.cloudflare.steamstatic.com/"
                    + "store_item_assets/steam/apps/"
                    + caminho
                    + "/library_600x900_2x.jpg";
        }

        Pattern pattern =
                Pattern.compile(
                        "/apps/(\\d+)"
                );

        Matcher matcher =
                pattern.matcher(caminho);

        if (matcher.find()) {

            return
                    "https://shared.cloudflare.steamstatic.com/"
                    + "store_item_assets/steam/apps/"
                    + matcher.group(1)
                    + "/library_600x900_2x.jpg";
        }

        if (caminho.startsWith("http://")
                ||
                caminho.startsWith("https://")) {

            return caminho;
        }

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
    // CARD USUARIO
    // =====================================================

    private String cardUsuario(
            HttpServletRequest request,
            Usuario usuario)
            throws IOException {

        StringBuilder html =
                new StringBuilder();

        html.append(
                "<a "
                + "class='card-usuario' "
                + "href='perfil-usuario?id="
                + usuario.getId()
                + "'>"
        );

        String foto =
                prepararFoto(
                        request,
                        usuario.getFoto()
                );

        if (foto != null) {

            html.append(
                    "<img "
                    + "class='mini-foto' "
                    + "src='"
                    + escapar(foto)
                    + "'>"
            );

        } else {

            html.append(
                    "<div class='mini-sem-foto'>"
                    + "Sem foto"
                    + "</div>"
            );
        }

        html.append(
                "<div class='info'>"
                + "<strong>"
                + escapar(usuario.getNome())
                + "</strong>"
                + "<span>@"
                + escapar(usuario.getUsername())
                + "</span>"
                + "</div>"
        );

        html.append("</a>");

        return html.toString();
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