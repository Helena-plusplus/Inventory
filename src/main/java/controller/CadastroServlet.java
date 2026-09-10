package controller;

import dao.CriarBanco;
import dao.Conexao;
import dao.UsuarioDAO;
import model.Usuario;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/cadastro")

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 5 * 1024 * 1024,
        maxRequestSize = 10 * 1024 * 1024
)

public class CadastroServlet extends HttpServlet {

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

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        System.out.println(
                "================================="
        );

        System.out.println(
                "CADASTRO FOI CHAMADO"
        );

        System.out.println(
                "================================="
        );

        try {

            CriarBanco.criarTabela();

            // =========================================
            // DADOS
            // =========================================

            String nome =
                    valor(request, "nome");

            String username =
                    valor(request, "username");

            String email =
                    valor(request, "email")
                            .toLowerCase();

            String senha =
                    valor(request, "senha");

            String pais =
                    valor(request, "pais");

            String plataforma =
                    valor(request, "plataforma");

            String bio =
                    valor(request, "bio");

            // =========================================
            // VALIDAR
            // =========================================

            if (nome.isEmpty() ||
                    username.isEmpty() ||
                    email.isEmpty() ||
                    senha.isEmpty()) {

                response.sendRedirect(
                        "cadastro.html?erro=campos"
                );

                return;
            }

            // =========================================
            // VALIDAR E-MAIL
            // =========================================

            if (!email.matches(
                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
            )) {

                response.sendRedirect(
                        "cadastro.html?erro=email"
                );

                return;
            }

            UsuarioDAO dao =
                    new UsuarioDAO();

            // =========================================
            // E-MAIL JÁ EXISTE
            // =========================================

            if (dao.buscarPorEmail(email) != null) {

                response.sendRedirect(
                        "cadastro.html?erro=email_existente"
                );

                return;
            }

            // =========================================
            // USERNAME JÁ EXISTE
            // =========================================

            if (!dao.buscarPorUsernameParcial(username)
                    .isEmpty()) {

                boolean usernameExato = false;

                for (Usuario usuario :
                        dao.buscarPorUsernameParcial(
                                username
                        )) {

                    if (usuario.getUsername() != null &&
                            usuario.getUsername()
                                    .equalsIgnoreCase(
                                            username
                                    )) {

                        usernameExato = true;
                        break;
                    }
                }

                if (usernameExato) {

                    response.sendRedirect(
                            "cadastro.html?erro=username"
                    );

                    return;
                }
            }

            // =========================================
            // FOTO
            // =========================================

            String nomeFoto = "";

            Part arquivo =
                    request.getPart("foto");

            if (arquivo != null &&
                    arquivo.getSize() > 0) {

                String nomeOriginal =
                        arquivo.getSubmittedFileName();

                if (nomeOriginal == null ||
                        nomeOriginal.trim().isEmpty()) {

                    response.sendRedirect(
                            "cadastro.html?erro=foto"
                    );

                    return;
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

                if (!extensao.equals(".jpg") &&
                        !extensao.equals(".jpeg") &&
                        !extensao.equals(".png") &&
                        !extensao.equals(".webp")) {

                    response.sendRedirect(
                            "cadastro.html?erro=formato"
                    );

                    return;
                }

                File diretorio =
                        new File(PASTA_FOTOS);

                if (!diretorio.exists()) {

                    if (!diretorio.mkdirs()) {

                        throw new Exception(
                                "Não foi possível criar a pasta de fotos."
                        );
                    }
                }

                nomeFoto =
                        "perfil_pendente_"
                        + System.currentTimeMillis()
                        + extensao;

                File arquivoFinal =
                        new File(
                                diretorio,
                                nomeFoto
                        );

                arquivo.write(
                        arquivoFinal.getAbsolutePath()
                );
            }

            // =========================================
            // CRIAR OBJETO
            // =========================================

            Usuario usuario =
                    new Usuario();

            usuario.setNome(nome);

            usuario.setUsername(username);

            usuario.setEmail(email);

            usuario.setSenha(senha);

            usuario.setFoto(nomeFoto);

            usuario.setBio(bio);

            // sem data de nascimento
            usuario.setDataNascimento("");

            usuario.setPais(pais);

            usuario.setPlataformaFavorita(
                    plataforma
            );

            // =========================================
            // GERAR CÓDIGO
            // =========================================

            Random random =
                    new Random();

            String codigo =
                    String.format(
                            "%06d",
                            random.nextInt(1000000)
                    );

            // =========================================
            // EXPIRAÇÃO
            // 10 MINUTOS
            // =========================================

            String expiraEm =
                    "+10 minutes";

            // =========================================
            // SALVAR PENDENTE
            // =========================================

            boolean salvo =
                    dao.salvarCadastroPendente(
                            usuario,
                            codigo,
                            expiraEm
                    );

            if (!salvo) {

                response.sendRedirect(
                        "cadastro.html?erro=salvar"
                );

                return;
            }

            // =========================================
            // ENVIAR CÓDIGO
            // =========================================

            EmailUtil.enviarCodigo(
                    email,
                    codigo
            );

            // =========================================
            // IR PARA VERIFICAÇÃO
            // =========================================

            response.sendRedirect(
                    "verificar-email.html?email="
                            + java.net.URLEncoder.encode(
                                    email,
                                    "UTF-8"
                            )
            );

        } catch (Exception e) {

            System.out.println(
                    "================================="
            );

            System.out.println(
                    "ERRO NO CADASTRO"
            );

            System.out.println(
                    e.getMessage()
            );

            System.out.println(
                    "================================="
            );

            e.printStackTrace();

            response.sendRedirect(
                    "cadastro.html?erro=servidor"
            );
        }
    }

    private String valor(
            HttpServletRequest request,
            String nome) {

        String valor =
                request.getParameter(nome);

        if (valor == null) {
            return "";
        }

        return valor.trim();
    }
}