package controller;

import dao.CriarBanco;
import dao.UsuarioDAO;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Usuario;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Cria/verifica as tabelas do banco
        CriarBanco.criarTabela();

        request.setCharacterEncoding("UTF-8");

        String email =
                request.getParameter("email");

        String senha =
                request.getParameter("senha");

        System.out.println("==============================");
        System.out.println("LOGIN FOI CHAMADO");
        System.out.println("E-mail: " + email);
        System.out.println("==============================");

        UsuarioDAO dao =
                new UsuarioDAO();

        Usuario usuario =
                dao.login(email, senha);

        if (usuario != null) {

            System.out.println(
                    "LOGIN REALIZADO COM SUCESSO!"
            );

            // Cria a sessão
            HttpSession sessao =
                    request.getSession(true);

            // Salva o usuário na sessão
            sessao.setAttribute(
                    "usuario",
                    usuario
            );

            // Vai para a página inicial
            response.sendRedirect("home");

        } else {

            System.out.println(
                    "E-MAIL OU SENHA INCORRETOS!"
            );

            response.setContentType(
                    "text/html;charset=UTF-8"
            );

            response.getWriter().println(

                    "<!DOCTYPE html>"

                    + "<html lang='pt-BR'>"

                    + "<head>"

                    + "<meta charset='UTF-8'>"

                    + "<title>Erro no Login</title>"

                    + "</head>"

                    + "<body>"

                    + "<h2>E-mail ou senha incorretos!</h2>"

                    + "<a href='login.html'>"
                    + "Voltar para o login"
                    + "</a>"

                    + "</body>"

                    + "</html>"
            );
        }
    }
}