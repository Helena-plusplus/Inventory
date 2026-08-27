
package controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/foto-perfil")
public class FotoPerfilServlet extends HttpServlet {

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
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String arquivo =
                request.getParameter("arquivo");

        if (arquivo == null ||
                arquivo.trim().isEmpty()) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND
            );

            return;
        }

        // Impede acesso a caminhos externos
        arquivo =
                new File(arquivo)
                        .getName();

        String nomeMinusculo =
                arquivo.toLowerCase();

        // Aceitar somente imagens
        if (!nomeMinusculo.endsWith(".jpg") &&
            !nomeMinusculo.endsWith(".jpeg") &&
            !nomeMinusculo.endsWith(".png") &&
            !nomeMinusculo.endsWith(".webp")) {

            response.sendError(
                    HttpServletResponse.SC_FORBIDDEN
            );

            return;
        }

        File arquivoFoto =
                new File(
                        PASTA_FOTOS,
                        arquivo
                );

        if (!arquivoFoto.exists() ||
                !arquivoFoto.isFile()) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND
            );

            return;
        }

        String tipo =
                getServletContext()
                        .getMimeType(
                                arquivoFoto.getName()
                        );

        if (tipo == null) {

            tipo =
                    "application/octet-stream";
        }

        response.setContentType(tipo);

        response.setContentLengthLong(
                arquivoFoto.length()
        );

        try (
                OutputStream saida =
                        response.getOutputStream()
        ) {

            Files.copy(
                    arquivoFoto.toPath(),
                    saida
            );
        }
    }
}

