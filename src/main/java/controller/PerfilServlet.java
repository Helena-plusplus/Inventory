package controller;

import dao.Conexao;
import dao.UsuarioDAO;
import model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/perfil")
public class PerfilServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sessao = request.getSession(false);

        if (sessao == null ||
                sessao.getAttribute("usuario") == null) {

            response.sendRedirect("login.html");
            return;
        }

        try {

            Usuario usuarioSessao =
                    (Usuario) sessao.getAttribute("usuario");

            int idUsuario =
                    usuarioSessao.getId();

            Usuario usuario =
                    new UsuarioDAO().buscarPorId(idUsuario);

            if (usuario == null) {

                response.sendRedirect("login.html");
                return;
            }

            UsuarioDAO dao =
                    new UsuarioDAO();

            int seguidores =
                    dao.contarSeguidores(idUsuario);

            int seguindo =
                    dao.contarSeguindo(idUsuario);

            boolean usuarioEspecial =
                    usuario.getEmail() != null &&
                    usuario.getEmail().equalsIgnoreCase(
                            "rebecarodriguesduarte2@gmail.com"
                    );

            List<Usuario> listaSeguidores =
                    dao.listarSeguidores(idUsuario);

            List<Usuario> listaSeguindo =
                    dao.listarSeguindo(idUsuario);

            List<JogoFavorito> favoritos =
                    carregarFavoritos(idUsuario);

            List<ListaJogo> listas =
                    carregarListas(idUsuario);

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
                    "<title>Meu Perfil - GameBoxd</title>"
            );

            html.append(
                    "<link rel='stylesheet' href='style.css'>"
            );

            html.append("<style>");

            html.append(
                    ".perfil-container {" +
                    "max-width:1000px;" +
                    "margin:40px auto;" +
                    "padding:20px;" +
                    "}"
            );

            html.append(
                    ".perfil-topo {" +
                    "background:#202830;" +
                    "padding:30px;" +
                    "border-radius:14px;" +
                    "text-align:center;" +
                    "}"
            );

            html.append(
                    ".foto-perfil {" +
                    "width:120px;" +
                    "height:120px;" +
                    "border-radius:50%;" +
                    "object-fit:cover;" +
                    "border:3px solid #6300c0;" +
                    "}"
            );

            html.append(
                    ".nome-perfil {" +
                    "font-size:28px;" +
                    "margin:15px 0 5px;" +
                    "}"
            );

            html.append(
                    ".username {" +
                    "color:#aaa;" +
                    "margin-bottom:10px;" +
                    "}"
            );

            html.append(
                    ".bio {" +
                    "color:#ddd;" +
                    "margin:15px auto;" +
                    "max-width:600px;" +
                    "}"
            );

            html.append(
                    ".emblema {" +
                    "display:inline-block;" +
                    "margin-top:10px;" +
                    "padding:7px 14px;" +
                    "border-radius:20px;" +
                    "background:#6300c0;" +
                    "color:white;" +
                    "font-weight:bold;" +
                    "}"
            );

            html.append(
                    ".estatisticas {" +
                    "display:flex;" +
                    "justify-content:center;" +
                    "gap:50px;" +
                    "margin-top:25px;" +
                    "flex-wrap:wrap;" +
                    "}"
            );

            html.append(
                    ".estatistica {" +
                    "text-align:center;" +
                    "cursor:pointer;" +
                    "}"
            );

            html.append(
                    ".numero {" +
                    "font-size:24px;" +
                    "font-weight:bold;" +
                    "color:white;" +
                    "}"
            );

            html.append(
                    ".texto-estatistica {" +
                    "color:#aaa;" +
                    "font-size:14px;" +
                    "}"
            );

            html.append(
                    ".secao {" +
                    "background:#202830;" +
                    "padding:25px;" +
                    "border-radius:14px;" +
                    "margin-top:25px;" +
                    "}"
            );

            html.append(
                    ".secao h2 {" +
                    "margin-top:0;" +
                    "}"
            );

            html.append(
                    ".jogos-grid {" +
                    "display:grid;" +
                    "grid-template-columns:" +
                    "repeat(auto-fill,minmax(160px,1fr));" +
                    "gap:20px;" +
                    "}"
            );

            html.append(
                    ".jogo-card {" +
                    "background:#14181c;" +
                    "padding:12px;" +
                    "border-radius:10px;" +
                    "text-align:center;" +
                    "}"
            );

            html.append(
                    ".jogo-capa {" +
                    "width:100%;" +
                    "height:220px;" +
                    "object-fit:cover;" +
                    "border-radius:8px;" +
                    "}"
            );

            html.append(
                    ".nome-jogo {" +
                    "margin-top:10px;" +
                    "font-weight:bold;" +
                    "color:white;" +
                    "}"
            );

            html.append(
                    ".lista-box {" +
                    "background:#14181c;" +
                    "padding:18px;" +
                    "border-radius:10px;" +
                    "margin-bottom:20px;" +
                    "}"
            );

            html.append(
                    ".lista-titulo {" +
                    "font-size:20px;" +
                    "font-weight:bold;" +
                    "margin-bottom:15px;" +
                    "}"
            );

            html.append(
                    ".botoes-perfil {" +
                    "display:flex;" +
                    "justify-content:center;" +
                    "gap:10px;" +
                    "margin-top:20px;" +
                    "flex-wrap:wrap;" +
                    "}"
            );

            html.append(
                    ".botao-perfil {" +
                    "display:inline-block;" +
                    "padding:10px 18px;" +
                    "background:#6300c0;" +
                    "color:white;" +
                    "text-decoration:none;" +
                    "border-radius:7px;" +
                    "font-weight:bold;" +
                    "}"
            );

            html.append(
                    ".botao-perfil:hover {" +
                    "background:#7d00ef;" +
                    "}"
            );

            html.append(
                    ".usuario-lista {" +
                    "display:flex;" +
                    "align-items:center;" +
                    "gap:12px;" +
                    "padding:10px 0;" +
                    "border-bottom:1px solid #333;" +
                    "}"
            );

            html.append(
                    ".foto-mini {" +
                    "width:45px;" +
                    "height:45px;" +
                    "border-radius:50%;" +
                    "object-fit:cover;" +
                    "}"
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
                    "<a href='jogos'>Jogos</a>"
            );

            html.append(
                    "<a href='biblioteca'>Biblioteca</a>"
            );

            html.append(
                    "<a href='buscar-usuarios.html'>Buscar usuários</a>"
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

            // =========================
            // PERFIL
            // =========================

            html.append(
                    "<main class='perfil-container'>"
            );

            html.append(
                    "<section class='perfil-topo'>"
            );

            String foto =
                    prepararFoto(
                            usuario.getFoto(),
                            request
                    );

            if (foto != null &&
                    !foto.isEmpty()) {

                html.append(
                        "<img " +
                        "class='foto-perfil' " +
                        "src='" +
                        foto +
                        "' " +
                        "alt='Foto de perfil'>"
                );

            } else {

                html.append(
                        "<div style='font-size:70px;'>👤</div>"
                );
            }

            html.append(
                    "<div class='nome-perfil'>"
            );

            html.append(
                    escaparHtml(
                            usuario.getNome()
                    )
            );

            html.append("</div>");

            html.append(
                    "<div class='username'>@"
                    +
                    escaparHtml(
                            usuario.getNome()
                    )
                    +
                    "</div>"
            );

            if (usuario.getBio() != null &&
                    !usuario.getBio().trim().isEmpty()) {

                html.append(
                        "<div class='bio'>"
                );

                html.append(
                        escaparHtml(
                                usuario.getBio()
                        )
                );

                html.append("</div>");
            }

            if (usuarioEspecial) {

                html.append(
                        "<div class='emblema'>"
                        + "♡ My Love"
                        + "</div>"
                );
            }

            html.append(
                    "<div class='estatisticas'>"
            );

            html.append(
                    "<div class='estatistica'>"
            );

            html.append(
                    "<div class='numero'>"
                    + seguidores +
                    "</div>"
            );

            html.append(
                    "<div class='texto-estatistica'>"
                    + "Seguidores"
                    + "</div>"
            );

            html.append("</div>");

            html.append(
                    "<div class='estatistica'>"
            );

            html.append(
                    "<div class='numero'>"
                    + seguindo +
                    "</div>"
            );

            html.append(
                    "<div class='texto-estatistica'>"
                    + "Seguindo"
                    + "</div>"
            );

            html.append("</div>");

            html.append("</div>");

            html.append(
                    "<div class='botoes-perfil'>"
            );

            html.append(
                    "<a class='botao-perfil' " +
                    "href='buscar-usuarios.html'>"
                    + "Buscar usuários"
                    + "</a>"
            );

            html.append(
                    "<a class='botao-perfil' " +
                    "href='listas'>"
                    + "Minhas listas"
                    + "</a>"
            );

            html.append("</div>");

            html.append("</section>");

            // =========================
            // FAVORITOS
            // =========================

            html.append(
                    "<section class='secao'>"
            );

            html.append(
                    "<h2>❤️ Favoritos</h2>"
            );

            if (favoritos.isEmpty()) {

                html.append(
                        "<p>Nenhum jogo favorito ainda.</p>"
                );

            } else {

                html.append(
                        "<div class='jogos-grid'>"
                );

                for (JogoFavorito jogo :
                        favoritos) {

                    html.append(
                            "<div class='jogo-card'>"
                    );

                    String capaJogo =
                            prepararCapa(
                                    jogo.capa,
                                    request
                            );

                    if (capaJogo != null &&
                            !capaJogo.isEmpty()) {

                        html.append(
                                "<img " +
                                "class='jogo-capa' " +
                                "src='" +
                                capaJogo +
                                "' " +
                                "alt='Capa do jogo'>"
                        );
                    }

                    html.append(
                            "<div class='nome-jogo'>"
                    );

                    html.append(
                            escaparHtml(
                                    jogo.titulo
                            )
                    );

                    html.append("</div>");

                    html.append("</div>");
                }

                html.append("</div>");
            }

            html.append("</section>");

            // =========================
            // LISTAS
            // =========================

            html.append(
                    "<section class='secao'>"
            );

            html.append(
                    "<h2>📚 Minhas listas</h2>"
            );

            if (listas.isEmpty()) {

                html.append(
                        "<p>Você ainda não criou nenhuma lista.</p>"
                );

            } else {

                for (ListaJogo lista :
                        listas) {

                    html.append(
                            "<div class='lista-box'>"
                    );

                    html.append(
                            "<div class='lista-titulo'>"
                    );

                    html.append(
                            escaparHtml(
                                    lista.nome
                            )
                    );

                    html.append("</div>");

                    if (lista.jogos.isEmpty()) {

                        html.append(
                                "<p>Essa lista está vazia.</p>"
                        );

                    } else {

                        html.append(
                                "<div class='jogos-grid'>"
                        );

                        for (JogoFavorito jogo :
                                lista.jogos) {

                            html.append(
                                    "<div class='jogo-card'>"
                            );

                            String capaJogo =
                                    prepararCapa(
                                            jogo.capa,
                                            request
                                    );

                            if (capaJogo != null &&
                                    !capaJogo.isEmpty()) {

                                html.append(
                                        "<img " +
                                        "class='jogo-capa' " +
                                        "src='" +
                                        capaJogo +
                                        "' " +
                                        "alt='Capa do jogo'>"
                                );
                            }

                            html.append(
                                    "<div class='nome-jogo'>"
                            );

                            html.append(
                                    escaparHtml(
                                            jogo.titulo
                                    )
                            );

                            html.append("</div>");

                            html.append("</div>");
                        }

                        html.append("</div>");
                    }

                    html.append("</div>");
                }
            }

            html.append("</section>");

            // =========================
            // SEGUIDORES
            // =========================

            html.append(
                    "<section class='secao'>"
            );

            html.append(
                    "<h2>👥 Seguidores</h2>"
            );

            if (listaSeguidores.isEmpty()) {

                html.append(
                        "<p>Você ainda não tem seguidores.</p>"
                );

            } else {

                for (Usuario u :
                        listaSeguidores) {

                    html.append(
                            "<div class='usuario-lista'>"
                    );

                    String fotoMini =
                            prepararFoto(
                                    u.getFoto(),
                                    request
                            );

                    if (fotoMini != null &&
                            !fotoMini.isEmpty()) {

                        html.append(
                                "<img " +
                                "class='foto-mini' " +
                                "src='" +
                                fotoMini +
                                "' " +
                                "alt='Foto'>"
                        );
                    }

                    html.append(
                            "<a href='perfil-usuario?id="
                            +
                            u.getId()
                            +
                            "' style='color:white;text-decoration:none;'>"
                    );

                    html.append(
                            escaparHtml(
                                    u.getNome()
                            )
                    );

                    html.append("</a>");

                    html.append("</div>");
                }
            }

            html.append("</section>");

            // =========================
            // SEGUINDO
            // =========================

            html.append(
                    "<section class='secao'>"
            );

            html.append(
                    "<h2>➕ Seguindo</h2>"
            );

            if (listaSeguindo.isEmpty()) {

                html.append(
                        "<p>Você ainda não segue ninguém.</p>"
                );

            } else {

                for (Usuario u :
                        listaSeguindo) {

                    html.append(
                            "<div class='usuario-lista'>"
                    );

                    String fotoMini =
                            prepararFoto(
                                    u.getFoto(),
                                    request
                            );

                    if (fotoMini != null &&
                            !fotoMini.isEmpty()) {

                        html.append(
                                "<img " +
                                "class='foto-mini' " +
                                "src='" +
                                fotoMini +
                                "' " +
                                "alt='Foto'>"
                        );
                    }

                    html.append(
                            "<a href='perfil-usuario?id="
                            +
                            u.getId()
                            +
                            "' style='color:white;text-decoration:none;'>"
                    );

                    html.append(
                            escaparHtml(
                                    u.getNome()
                            )
                    );

                    html.append("</a>");

                    html.append("</div>");
                }
            }

            html.append("</section>");

            html.append("</main>");

            html.append("</body>");

            html.append("</html>");

            response.getWriter().println(
                    html.toString()
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect("index.html");
        }
    }

    // =====================================================
    // FAVORITOS
    // =====================================================

    private List<JogoFavorito> carregarFavoritos(
            int idUsuario)
            throws Exception {

        List<JogoFavorito> lista =
                new ArrayList<>();

        Connection conexao =
                Conexao.conectar();

        String sql =
                "SELECT j.id, j.titulo, j.capa " +
                "FROM favorito f " +
                "INNER JOIN jogo j ON j.id = f.id_jogo " +
                "WHERE f.id_usuario = ? " +
                "ORDER BY f.id DESC";

        PreparedStatement stmt =
                conexao.prepareStatement(sql);

        stmt.setInt(1, idUsuario);

        ResultSet rs =
                stmt.executeQuery();

        while (rs.next()) {

            JogoFavorito jogo =
                    new JogoFavorito();

            jogo.id =
                    rs.getInt("id");

            jogo.titulo =
                    rs.getString("titulo");

            jogo.capa =
                    rs.getString("capa");

            lista.add(jogo);
        }

        rs.close();
        stmt.close();
        conexao.close();

        return lista;
    }

    // =====================================================
    // LISTAS
    // =====================================================

    private List<ListaJogo> carregarListas(
            int idUsuario)
            throws Exception {

        List<ListaJogo> listas =
                new ArrayList<>();

        Connection conexao =
                Conexao.conectar();

        String sql =
                "SELECT id, nome " +
                "FROM lista " +
                "WHERE id_usuario = ? " +
                "ORDER BY id DESC";

        PreparedStatement stmt =
                conexao.prepareStatement(sql);

        stmt.setInt(1, idUsuario);

        ResultSet rs =
                stmt.executeQuery();

        while (rs.next()) {

            ListaJogo lista =
                    new ListaJogo();

            lista.id =
                    rs.getInt("id");

            lista.nome =
                    rs.getString("nome");

            lista.jogos =
                    carregarJogosLista(
                            lista.id
                    );

            listas.add(lista);
        }

        rs.close();
        stmt.close();
        conexao.close();

        return listas;
    }

    // =====================================================
    // JOGOS DA LISTA
    // =====================================================

    private List<JogoFavorito> carregarJogosLista(
            int idLista)
            throws Exception {

        List<JogoFavorito> jogos =
                new ArrayList<>();

        Connection conexao =
                Conexao.conectar();

        String sql =
                "SELECT j.id, j.titulo, j.capa " +
                "FROM lista_jogo lj " +
                "INNER JOIN jogo j " +
                "ON j.id = lj.id_jogo " +
                "WHERE lj.id_lista = ? " +
                "ORDER BY lj.id ASC";

        PreparedStatement stmt =
                conexao.prepareStatement(sql);

        stmt.setInt(1, idLista);

        ResultSet rs =
                stmt.executeQuery();

        while (rs.next()) {

            JogoFavorito jogo =
                    new JogoFavorito();

            jogo.id =
                    rs.getInt("id");

            jogo.titulo =
                    rs.getString("titulo");

            jogo.capa =
                    rs.getString("capa");

            jogos.add(jogo);
        }

        rs.close();
        stmt.close();
        conexao.close();

        return jogos;
    }

    // =====================================================
    // CORREÇÃO DAS CAPAS
    // =====================================================

    private String prepararCapa(
            String caminho,
            HttpServletRequest request) {

        if (caminho == null ||
                caminho.trim().isEmpty()) {

            return "";
        }

        caminho =
                caminho.trim();

        // URL completa: NÃO ALTERAR
        if (caminho.startsWith("http://") ||
                caminho.startsWith("https://")) {

            return caminho;
        }

        // Markdown: [texto](URL)
        if (caminho.startsWith("[") &&
                caminho.contains("](") &&
                caminho.endsWith(")")) {

            int inicio =
                    caminho.indexOf("](") + 2;

            int fim =
                    caminho.lastIndexOf(")");

            if (inicio < fim) {

                String url =
                        caminho.substring(
                                inicio,
                                fim
                        );

                if (url.startsWith("http://") ||
                        url.startsWith("https://")) {

                    return url;
                }
            }
        }

        // Caso seja apenas ID do jogo
        if (caminho.matches("\\d+")) {

            return
                    "https://cdn.akamai.steamstatic.com/" +
                    "steam/apps/" +
                    caminho +
                    "/library_600x900_2x.jpg";
        }

        while (caminho.startsWith("/")) {

            caminho =
                    caminho.substring(1);
        }

        return
                request.getContextPath()
                + "/"
                + caminho;
    }

    // =====================================================
    // CORREÇÃO DA FOTO
    // =====================================================

    private String prepararFoto(
            String foto,
            HttpServletRequest request) {

        if (foto == null ||
                foto.trim().isEmpty()) {

            return "";
        }

        foto =
                foto.trim();

        if (foto.startsWith("http://") ||
                foto.startsWith("https://")) {

            return foto;
        }

        if (foto.startsWith("/")) {

            return
                    request.getContextPath()
                    + foto;
        }

        return
                request.getContextPath()
                +
                "/foto-perfil?arquivo="
                +
                foto;
    }

    // =====================================================
    // ESCAPAR HTML
    // =====================================================

    private String escaparHtml(
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

    // =====================================================
    // CLASSES AUXILIARES
    // =====================================================

    private static class JogoFavorito {

        int id;
        String titulo;
        String capa;
    }

    private static class ListaJogo {

        int id;
        String nome;
        List<JogoFavorito> jogos =
                new ArrayList<>();
    }
}