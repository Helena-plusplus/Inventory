package controller;

import dao.Conexao;
import model.Usuario;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/editar-perfil")
public class EditarPerfilServlet extends HttpServlet {

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

        String html =
                "<!DOCTYPE html>" +
                "<html lang='pt-BR'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<title>Editar perfil - Inventory</title>" +
                "<link rel='stylesheet' href='style.css'>" +
                "</head>" +

                "<body>" +

                "<header>" +
                "<h1>Inventory</h1>" +
                "</header>" +

                "<main class='avaliacao-container'>" +

                "<h2>Editar perfil</h2>" +

                "<form method='POST' action='editar-perfil'>" +

                "<label>Nome</label>" +
                "<input type='text' name='nome' value='" +
                usuario.getNome() +
                "' required>" +

                "<br><br>" +

                "<label>Username</label>" +
                "<input type='text' name='username' value='" +
                usuario.getUsername() +
                "' required>" +

                "<br><br>" +

                "<label>Bio</label>" +
                "<textarea name='bio'>" +
                (usuario.getBio() == null
                        ? ""
                        : usuario.getBio()) +
                "</textarea>" +

                "<br><br>" +

                "<label>País</label>" +
                "<input type='text' name='pais' value='" +
                (usuario.getPais() == null
                        ? ""
                        : usuario.getPais()) +
                "'>" +

                "<br><br>" +

                "<label>Plataforma favorita</label>" +
                "<input type='text' name='plataforma_favorita' value='" +
                (usuario.getPlataformaFavorita() == null
                        ? ""
                        : usuario.getPlataformaFavorita()) +
                "'>" +

                "<br><br>" +

                "<button type='submit'>Salvar alterações</button>" +

                "</form>" +

                "</main>" +

                "</body>" +
                "</html>";

        response.getWriter().println(html);
    }

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

        try {

            Usuario usuario =
                    (Usuario) sessao.getAttribute("usuario");

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

            Connection conexao =
                    Conexao.conectar();

            String sql =
                    "UPDATE usuario SET " +
                    "nome = ?, " +
                    "username = ?, " +
                    "bio = ?, " +
                    "pais = ?, " +
                    "plataforma_favorita = ? " +
                    "WHERE id = ?";

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setString(1, nome);
            stmt.setString(2, username);
            stmt.setString(3, bio);
            stmt.setString(4, pais);
            stmt.setString(5, plataforma);
            stmt.setInt(6, usuario.getId());

            stmt.executeUpdate();

            stmt.close();
            conexao.close();

            // Atualiza a sessão
            usuario.setNome(nome);
            usuario.setUsername(username);
            usuario.setBio(bio);
            usuario.setPais(pais);
            usuario.setPlataformaFavorita(plataforma);

            sessao.setAttribute(
                    "usuario",
                    usuario
            );

            response.sendRedirect("perfil");

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect("perfil");
        }
    }
}