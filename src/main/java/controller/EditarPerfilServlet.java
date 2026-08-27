
package controller;

import dao.Conexao;
import model.Usuario;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@WebServlet("/editar-perfil")

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 10 * 1024 * 1024
)

public class EditarPerfilServlet extends HttpServlet {

    // =====================================================
    // PASTA DAS FOTOS
    // =====================================================

    private static final String PASTA_FOTOS;

    static {

        String sistema =
                System.getProperty("os.name")
                        .toLowerCase();

        if (sistema.contains("win")) {

            PASTA_FOTOS =
                    "C:\\GameBoxdUploads\\data\\perfil";

        } else {

            PASTA_FOTOS =
                    "/app/data/perfil";
        }
    }

    // =====================================================
    // GET - ABRIR PÁGINA
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

        Usuario usuario =
                (Usuario) sessao.getAttribute("usuario");

        response.setContentType(
                "text/html;charset=UTF-8"
        );

        String nome =
                usuario.getNome();

        String username =
                usuario.getUsername();

        String email =
                usuario.getEmail();

        String bio =
                usuario.getBio();

        String pais =
                usuario.getPais();

        String plataforma =
                usuario.getPlataformaFavorita();

        String foto =
                usuario.getFoto();

        if (nome == null) nome = "";
        if (username == null) username = "";
        if (email == null) email = "";
        if (bio == null) bio = "";
        if (pais == null) pais = "";
        if (plataforma == null) plataforma = "";
        if (foto == null) foto = "";

        String caminhoFoto = "";

        if (!foto.trim().isEmpty()) {

            if (foto.startsWith("http://") ||
                    foto.startsWith("https://")) {

                caminhoFoto = foto;

            } else {

                caminhoFoto =
                        request.getContextPath()
                        + "/foto-perfil?arquivo="
                        + java.net.URLEncoder.encode(
                                foto,
                                "UTF-8"
                        );
            }
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
                "<meta name='viewport' "
                + "content='width=device-width, initial-scale=1.0'>"
        );

        html.append(
                "<title>Editar Perfil - Inventory</title>"
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
                ".editar-container{"
                + "max-width:750px;"
                + "margin:45px auto;"
                + "padding:20px;"
                + "}"
        );

        html.append(
                ".editar-card{"
                + "background:linear-gradient(145deg,#21142c,#140b1b);"
                + "border:1px solid #54256f;"
                + "border-radius:22px;"
                + "padding:40px;"
                + "box-shadow:0 20px 50px rgba(0,0,0,.4);"
                + "}"
        );

        html.append(
                ".titulo-editar{"
                + "text-align:center;"
                + "margin-bottom:30px;"
                + "}"
        );

        html.append(
                ".titulo-editar h2{"
                + "font-size:34px;"
                + "margin-bottom:8px;"
                + "}"
        );

        html.append(
                ".titulo-editar p{"
                + "color:#aaa;"
                + "}"
        );

        // =====================================================
        // FOTO
        // =====================================================

        html.append(
                ".foto-area{"
                + "text-align:center;"
                + "margin-bottom:35px;"
                + "}"
        );

        html.append(
                ".foto-preview{"
                + "width:160px;"
                + "height:160px;"
                + "object-fit:cover;"
                + "border-radius:50%;"
                + "border:5px solid #7c3aed;"
                + "box-shadow:0 0 30px rgba(124,58,237,.4);"
                + "margin-bottom:15px;"
                + "}"
        );

        html.append(
                ".sem-foto{"
                + "width:160px;"
                + "height:160px;"
                + "margin:0 auto 15px;"
                + "border-radius:50%;"
                + "background:#24152f;"
                + "border:5px solid #7c3aed;"
                + "display:flex;"
                + "align-items:center;"
                + "justify-content:center;"
                + "font-size:45px;"
                + "}"
        );

        html.append(
                ".input-foto{"
                + "display:none;"
                + "}"
        );

        html.append(
                ".botao-foto{"
                + "display:inline-block;"
                + "padding:11px 20px;"
                + "background:linear-gradient(135deg,#7c3aed,#9333ea);"
                + "color:white;"
                + "border-radius:9px;"
                + "cursor:pointer;"
                + "font-weight:bold;"
                + "}"
        );

        html.append(
                ".botao-foto:hover{"
                + "transform:translateY(-2px);"
                + "box-shadow:0 8px 20px rgba(124,58,237,.3);"
                + "}"
        );

        html.append(
                ".ajuda-foto{"
                + "color:#82758d;"
                + "font-size:12px;"
                + "margin-top:10px;"
                + "}"
        );

        // =====================================================
        // CAMPOS
        // =====================================================

        html.append(
                ".campo{"
                + "margin-bottom:20px;"
                + "}"
        );

        html.append(
                ".campo label{"
                + "display:block;"
                + "margin-bottom:8px;"
                + "font-weight:bold;"
                + "color:#ddd;"
                + "}"
        );

        html.append(
                ".campo input,"
                + ".campo textarea,"
                + ".campo select{"
                + "width:100%;"
                + "box-sizing:border-box;"
                + "padding:13px 15px;"
                + "background:#100b15;"
                + "border:1px solid #47305a;"
                + "border-radius:9px;"
                + "color:white;"
                + "font-size:15px;"
                + "outline:none;"
                + "}"
        );

        html.append(
                ".campo input:focus,"
                + ".campo textarea:focus,"
                + ".campo select:focus{"
                + "border-color:#a855f7;"
                + "box-shadow:0 0 0 3px rgba(168,85,247,.12);"
                + "}"
        );

        html.append(
                ".campo textarea{"
                + "min-height:130px;"
                + "resize:vertical;"
                + "font-family:Arial,sans-serif;"
                + "}"
        );

        // =====================================================
        // BOTÕES
        // =====================================================

        html.append(
                ".botoes{"
                + "display:flex;"
                + "gap:12px;"
                + "margin-top:30px;"
                + "}"
        );

        html.append(
                ".botao-salvar,"
                + ".botao-cancelar{"
                + "flex:1;"
                + "padding:14px;"
                + "border-radius:9px;"
                + "font-weight:bold;"
                + "font-size:15px;"
                + "text-align:center;"
                + "text-decoration:none;"
                + "cursor:pointer;"
                + "}"
        );

        html.append(
                ".botao-salvar{"
                + "border:none;"
                + "background:linear-gradient(135deg,#7c3aed,#a855f7);"
                + "color:white;"
                + "}"
        );

        html.append(
                ".botao-cancelar{"
                + "background:#261d30;"
                + "border:1px solid #493653;"
                + "color:#ddd;"
                + "}"
        );

        html.append(
                "@media(max-width:600px){"
                + ".editar-card{padding:25px 20px;}"
                + ".botoes{flex-direction:column;}"
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
        // FORMULÁRIO
        // =====================================================

        html.append(
                "<main class='editar-container'>"
        );

        html.append(
                "<div class='editar-card'>"
        );

        html.append(
                "<div class='titulo-editar'>"
                + "<h2>✏️ Editar Perfil</h2>"
                + "<p>Atualize suas informações no Inventory.</p>"
                + "</div>"
        );

        html.append(
                "<form action='editar-perfil' "
                + "method='POST' "
                + "enctype='multipart/form-data'>"
        );

        // =====================================================
        // FOTO
        // =====================================================

        html.append(
                "<div class='foto-area'>"
        );

        if (!caminhoFoto.isEmpty()) {

            html.append(
                    "<img "
                    + "id='fotoPreview' "
                    + "class='foto-preview' "
                    + "src='"
                    + escapar(caminhoFoto)
                    + "' "
                    + "alt='Foto de perfil'>"
            );

        } else {

            html.append(
                    "<div id='fotoPadrao' "
                    + "class='sem-foto'>"
                    + "👤"
                    + "</div>"
            );
        }

        html.append("<br>");

        html.append(
                "<label for='foto' class='botao-foto'>"
                + "📷 Escolher nova foto"
                + "</label>"
        );

        html.append(
                "<input "
                + "class='input-foto' "
                + "type='file' "
                + "id='foto' "
                + "name='foto' "
                + "accept='.jpg,.jpeg,.png,.webp,image/*'>"
        );

        html.append(
                "<div class='ajuda-foto'>"
                + "JPG, JPEG, PNG ou WEBP • Máximo 5 MB"
                + "</div>"
        );

        html.append("</div>");

        // =====================================================
        // NOME
        // =====================================================

        html.append(
                "<div class='campo'>"
                + "<label for='nome'>Nome</label>"
                + "<input "
                + "type='text' "
                + "id='nome' "
                + "name='nome' "
                + "value='"
                + escapar(nome)
                + "' "
                + "required>"
                + "</div>"
        );

        // =====================================================
        // USERNAME
        // =====================================================

        html.append(
                "<div class='campo'>"
                + "<label for='username'>Username</label>"
                + "<input "
                + "type='text' "
                + "id='username' "
                + "name='username' "
                + "value='"
                + escapar(username)
                + "' "
                + "required>"
                + "</div>"
        );

        // =====================================================
        // EMAIL
        // =====================================================

        html.append(
                "<div class='campo'>"
                + "<label>E-mail</label>"
                + "<input "
                + "type='email' "
                + "value='"
                + escapar(email)
                + "' "
                + "disabled>"
                + "</div>"
        );

        // =====================================================
        // BIO
        // =====================================================

        html.append(
                "<div class='campo'>"
                + "<label for='bio'>Bio</label>"
                + "<textarea "
                + "id='bio' "
                + "name='bio' "
                + "placeholder='Fale um pouco sobre você...'>"
                + escapar(bio)
                + "</textarea>"
                + "</div>"
        );

        // =====================================================
        // PAÍS
        // =====================================================

        html.append(
                "<div class='campo'>"
                + "<label for='pais'>País</label>"
                + "<input "
                + "type='text' "
                + "id='pais' "
                + "name='pais' "
                + "value='"
                + escapar(pais)
                + "' "
                + "placeholder='Ex: Brasil'>"
                + "</div>"
        );

        // =====================================================
        // PLATAFORMA
        // =====================================================

        html.append(
                "<div class='campo'>"
                + "<label for='plataforma_favorita'>"
                + "Plataforma favorita"
                + "</label>"
                + "<select "
                + "id='plataforma_favorita' "
                + "name='plataforma_favorita'>"

                + "<option value=''>"
                + "Selecione"
                + "</option>"

                + "<option value='PlayStation' "
                + selecionar(plataforma, "PlayStation")
                + ">PlayStation</option>"

                + "<option value='Xbox' "
                + selecionar(plataforma, "Xbox")
                + ">Xbox</option>"

                + "<option value='PC' "
                + selecionar(plataforma, "PC")
                + ">PC</option>"

                + "<option value='Nintendo' "
                + selecionar(plataforma, "Nintendo")
                + ">Nintendo</option>"

                + "</select>"
                + "</div>"
        );

        // =====================================================
        // BOTÕES
        // =====================================================

        html.append("<div class='botoes'>");

        html.append(
                "<a href='perfil' "
                + "class='botao-cancelar'>"
                + "Cancelar"
                + "</a>"
        );

        html.append(
                "<button "
                + "type='submit' "
                + "class='botao-salvar'>"
                + "💾 Salvar alterações"
                + "</button>"
        );

        html.append("</div>");

        html.append("</form>");

        html.append("</div>");

        html.append("</main>");

        // =====================================================
        // PREVIEW
        // =====================================================

        html.append("<script>");

        html.append(
                "const foto = document.getElementById('foto');"
        );

        html.append(
                "foto.addEventListener('change', function(){"
                + "const arquivo=this.files[0];"
                + "if(!arquivo)return;"
                + "if(arquivo.size>5*1024*1024){"
                + "alert('A foto não pode ter mais de 5 MB.');"
                + "this.value='';"
                + "return;"
                + "}"
                + "const tipos=['image/jpeg','image/png','image/webp'];"
                + "if(!tipos.includes(arquivo.type)){"
                + "alert('Escolha JPG, PNG ou WEBP.');"
                + "this.value='';"
                + "return;"
                + "}"
                + "const leitor=new FileReader();"
                + "leitor.onload=function(e){"
                + "let img=document.getElementById('fotoPreview');"
                + "if(!img){"
                + "const padrao=document.getElementById('fotoPadrao');"
                + "if(padrao){"
                + "padrao.outerHTML="
                + "'<img id=\"fotoPreview\" class=\"foto-preview\" alt=\"Foto de perfil\">';"
                + "}"
                + "img=document.getElementById('fotoPreview');"
                + "}"
                + "if(img)img.src=e.target.result;"
                + "};"
                + "leitor.readAsDataURL(arquivo);"
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
    // POST - SALVAR
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

        Usuario usuario =
                (Usuario) sessao.getAttribute("usuario");

        int idUsuario =
                usuario.getId();

        Connection conexao = null;
        PreparedStatement stmt = null;

        try {

            String nome =
                    request.getParameter("nome");

            String username =
                    request.getParameter("username");

            String bio =
                    request.getParameter("bio");

            String pais =
                    request.getParameter("pais");

            String plataforma =
                    request.getParameter(
                            "plataforma_favorita"
                    );

            if (nome == null) nome = "";
            if (username == null) username = "";
            if (bio == null) bio = "";
            if (pais == null) pais = "";
            if (plataforma == null) plataforma = "";

            String caminhoFoto =
                    usuario.getFoto();

            // =================================================
            // RECEBER FOTO
            // =================================================

            Part arquivo =
                    request.getPart("foto");

            if (arquivo != null &&
                    arquivo.getSize() > 0) {

                String nomeOriginal =
                        arquivo.getSubmittedFileName();

                if (nomeOriginal == null ||
                        nomeOriginal.trim().isEmpty()) {

                    throw new Exception(
                            "Nome da imagem inválido."
                    );
                }

                nomeOriginal =
                        new File(nomeOriginal)
                                .getName();

                String extensao = "";

                int ponto =
                        nomeOriginal.lastIndexOf(".");

                if (ponto >= 0) {

                    extensao =
                            nomeOriginal
                            .substring(ponto)
                            .toLowerCase();
                }

                // =================================================
                // VALIDAR EXTENSÃO
                // =================================================

                if (!extensao.equals(".jpg") &&
                    !extensao.equals(".jpeg") &&
                    !extensao.equals(".png") &&
                    !extensao.equals(".webp")) {

                    response.sendRedirect(
                            "editar-perfil?erro=formato"
                    );

                    return;
                }

                // =================================================
                // CRIAR DIRETÓRIO
                // =================================================

                File diretorio =
                        new File(PASTA_FOTOS);

                if (!diretorio.exists()) {

                    if (!diretorio.mkdirs()) {

                        throw new Exception(
                                "Não foi possível criar a pasta:"
                                + PASTA_FOTOS
                        );
                    }
                }

                // =================================================
                // NOME DO ARQUIVO
                // =================================================

                String novoNome =
                        "perfil_"
                        + idUsuario
                        + "_"
                        + System.currentTimeMillis()
                        + extensao;

                File arquivoFinal =
                        new File(
                                diretorio,
                                novoNome
                        );

                // =================================================
                // SALVAR ARQUIVO
                // =================================================

                arquivo.write(
                        arquivoFinal.getAbsolutePath()
                );

                caminhoFoto =
                        novoNome;

                System.out.println(
                        "================================="
                );

                System.out.println(
                        "FOTO SALVA COM SUCESSO!"
                );

                System.out.println(
                        "CAMINHO:"
                        + arquivoFinal.getAbsolutePath()
                );

                System.out.println(
                        "NOME SALVO NO BANCO:"
                        + caminhoFoto
                );

                System.out.println(
                        "================================="
                );
            }

            // =================================================
            // ATUALIZAR BANCO
            // =================================================

            conexao =
                    Conexao.conectar();

            if (conexao == null) {

                throw new Exception(
                        "Não foi possível conectar ao SQLite."
                );
            }

            String sql =
                    "UPDATE usuario SET "
                    + "nome = ?, "
                    + "username = ?, "
                    + "bio = ?, "
                    + "pais = ?, "
                    + "plataforma_favorita = ?, "
                    + "foto = ? "
                    + "WHERE id = ?";

            stmt =
                    conexao.prepareStatement(sql);

            stmt.setString(
                    1,
                    nome
            );

            stmt.setString(
                    2,
                    username
            );

            stmt.setString(
                    3,
                    bio
            );

            stmt.setString(
                    4,
                    pais
            );

            stmt.setString(
                    5,
                    plataforma
            );

            stmt.setString(
                    6,
                    caminhoFoto
            );

            stmt.setInt(
                    7,
                    idUsuario
            );

            stmt.executeUpdate();

            // =================================================
            // ATUALIZAR SESSÃO
            // =================================================

            usuario.setNome(nome);

            usuario.setUsername(username);

            usuario.setBio(bio);

            usuario.setPais(pais);

            usuario.setPlataformaFavorita(
                    plataforma
            );

            usuario.setFoto(
                    caminhoFoto
            );

            sessao.setAttribute(
                    "usuario",
                    usuario
            );

            // =================================================
            // FINAL
            // =================================================

            stmt.close();
            conexao.close();

            response.sendRedirect(
                    "perfil"
            );

        } catch (Exception e) {

            e.printStackTrace();

            try {

                if (stmt != null) {
                    stmt.close();
                }

                if (conexao != null) {
                    conexao.close();
                }

            } catch (Exception erro) {

                erro.printStackTrace();
            }

            response.sendRedirect(
                    "editar-perfil?erro=1"
            );
        }
    }

    // =====================================================
    // SELECT
    // =====================================================

    private String selecionar(
            String atual,
            String valor) {

        if (atual != null &&
                atual.equals(valor)) {

            return "selected";
        }

        return "";
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

