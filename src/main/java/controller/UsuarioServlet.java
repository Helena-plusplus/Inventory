package controller;

import dao.CriarBanco;
import dao.UsuarioDAO;
import model.Usuario;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@MultipartConfig
@WebServlet("/cadastro")
public class UsuarioServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {

        System.out.println("=================================");
        System.out.println("USUARIOSERVLET INICIADO");
        System.out.println("=================================");

        CriarBanco.criarTabela();
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("=================================");
        System.out.println("SERVLET CADASTRO FOI CHAMADO");
        System.out.println("=================================");

        request.setCharacterEncoding("UTF-8");

        // =========================
        // DADOS DO FORMULARIO
        // =========================

        String nome =
                request.getParameter("nome");

        String username =
                request.getParameter("username");

        String email =
                request.getParameter("email");

        String senha =
                request.getParameter("senha");

        String dataNascimento =
                request.getParameter("dataNascimento");

        String pais =
                request.getParameter("pais");

        String plataforma =
                request.getParameter("plataforma");

        String bio =
                request.getParameter("bio");

        System.out.println("Nome: " + nome);
        System.out.println("Username: " + username);
        System.out.println("Email: " + email);

        // =========================
        // FOTO
        // =========================

        Part arquivoFoto =
                request.getPart("foto");

        String nomeFoto = null;

        if (arquivoFoto != null &&
                arquivoFoto.getSize() > 0) {

            String nomeOriginal =
                    arquivoFoto.getSubmittedFileName();

            if (nomeOriginal == null ||
                    nomeOriginal.isEmpty()) {

                nomeOriginal = "foto.jpg";
            }

            nomeFoto =
                    System.currentTimeMillis()
                    + "_" + nomeOriginal;

            String caminho =
                    getServletContext()
                    .getRealPath("/imagens");

            File pasta =
                    new File(caminho);

            if (!pasta.exists()) {
                pasta.mkdirs();
            }

            File arquivo =
                    new File(pasta, nomeFoto);

            arquivoFoto.write(
                    arquivo.getAbsolutePath()
            );

            System.out.println(
                    "Foto salva em: "
                    + arquivo.getAbsolutePath()
            );

        } else {

            System.out.println(
                    "Nenhuma foto foi enviada."
            );
        }

        // =========================
        // CRIAR OBJETO
        // =========================

        Usuario usuario =
                new Usuario();

        usuario.setNome(nome);
        usuario.setUsername(username);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setFoto(nomeFoto);
        usuario.setBio(bio);
        usuario.setDataNascimento(dataNascimento);
        usuario.setPais(pais);
        usuario.setPlataformaFavorita(plataforma);

        // =========================
        // CADASTRAR
        // =========================

        UsuarioDAO usuarioDAO =
                new UsuarioDAO();

        boolean cadastrado =
                usuarioDAO.cadastrar(usuario);

        // =========================
        // RESULTADO
        // =========================

        response.setContentType(
                "text/html;charset=UTF-8"
        );

        if (cadastrado) {

            System.out.println(
                    "USUARIO CADASTRADO COM SUCESSO!"
            );

            response.sendRedirect(
                    "login.html"
            );

        } else {

            System.out.println(
                    "ERRO AO CADASTRAR USUARIO!"
            );

            response.getWriter().println(

                    "<!DOCTYPE html>"

                    + "<html lang='pt-BR'>"

                    + "<head>"

                    + "<meta charset='UTF-8'>"

                    + "<title>Erro</title>"

                    + "</head>"

                    + "<body>"

                    + "<h2>Erro ao cadastrar usuário.</h2>"

                    + "<p>"
                    + "O nome de usuário ou e-mail "
                    + "pode já estar cadastrado."
                    + "</p>"

                    + "<a href='cadastro.html'>"
                    + "Voltar para cadastro"
                    + "</a>"

                    + "</body>"

                    + "</html>"
            );
        }
    }
}