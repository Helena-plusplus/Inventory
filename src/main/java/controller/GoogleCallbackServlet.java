package controller;

import dao.CriarBanco;
import dao.Conexao;
import model.Usuario;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import java.nio.charset.StandardCharsets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/google-callback")
public class GoogleCallbackServlet extends HttpServlet {

    private static final String CLIENT_ID =
            System.getenv("GOOGLE_CLIENT_ID");

    private static final String CLIENT_SECRET =
            System.getenv("GOOGLE_CLIENT_SECRET");

    private static final String REDIRECT_URI =
            System.getenv("GOOGLE_REDIRECT_URI");

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            System.out.println("==============================");
            System.out.println("GOOGLE CALLBACK FOI CHAMADO");
            System.out.println("==============================");

            if (CLIENT_ID == null ||
                    CLIENT_ID.trim().isEmpty()) {

                System.out.println(
                        "ERRO: GOOGLE_CLIENT_ID não configurado."
                );

                response.sendRedirect(
                        "login.html?erro=config_google"
                );

                return;
            }

            if (CLIENT_SECRET == null ||
                    CLIENT_SECRET.trim().isEmpty()) {

                System.out.println(
                        "ERRO: GOOGLE_CLIENT_SECRET não configurado."
                );

                response.sendRedirect(
                        "login.html?erro=config_google"
                );

                return;
            }

            if (REDIRECT_URI == null ||
                    REDIRECT_URI.trim().isEmpty()) {

                System.out.println(
                        "ERRO: GOOGLE_REDIRECT_URI não configurado."
                );

                response.sendRedirect(
                        "login.html?erro=config_google"
                );

                return;
            }

            String code =
                    request.getParameter("code");

            String state =
                    request.getParameter("state");

            String erro =
                    request.getParameter("error");

            if (erro != null) {

                response.sendRedirect(
                        "login.html?erro=google"
                );

                return;
            }

            if (code == null ||
                    code.trim().isEmpty()) {

                response.sendRedirect(
                        "login.html?erro=sem_codigo"
                );

                return;
            }

            HttpSession sessao =
                    request.getSession(false);

            if (sessao == null) {

                response.sendRedirect(
                        "login.html?erro=sessao"
                );

                return;
            }

            String stateSalvo =
                    (String) sessao.getAttribute(
                            "google_oauth_state"
                    );

            if (stateSalvo == null ||
                    !stateSalvo.equals(state)) {

                response.sendError(
                        HttpServletResponse.SC_FORBIDDEN,
                        "State inválido."
                );

                return;
            }

            sessao.removeAttribute(
                    "google_oauth_state"
            );

            // =================================================
            // TROCAR CODE POR ACCESS TOKEN
            // =================================================

            String dadosToken =
                    "code="
                    + URLEncoder.encode(
                            code,
                            "UTF-8"
                    )
                    + "&client_id="
                    + URLEncoder.encode(
                            CLIENT_ID,
                            "UTF-8"
                    )
                    + "&client_secret="
                    + URLEncoder.encode(
                            CLIENT_SECRET,
                            "UTF-8"
                    )
                    + "&redirect_uri="
                    + URLEncoder.encode(
                            REDIRECT_URI,
                            "UTF-8"
                    )
                    + "&grant_type=authorization_code";

            URL urlToken =
                    new URL(
                            "https://oauth2.googleapis.com/token"
                    );

            HttpURLConnection conexaoToken =
                    (HttpURLConnection)
                            urlToken.openConnection();

            conexaoToken.setRequestMethod("POST");

            conexaoToken.setDoOutput(true);

            conexaoToken.setRequestProperty(
                    "Content-Type",
                    "application/x-www-form-urlencoded"
            );

            try (
                    OutputStream saida =
                            conexaoToken.getOutputStream()
            ) {

                saida.write(
                        dadosToken.getBytes(
                                StandardCharsets.UTF_8
                        )
                );
            }

            int codigoResposta =
                    conexaoToken.getResponseCode();

            InputStream entrada;

            if (codigoResposta >= 200 &&
                    codigoResposta < 300) {

                entrada =
                        conexaoToken.getInputStream();

            } else {

                entrada =
                        conexaoToken.getErrorStream();
            }

            String respostaToken =
                    lerResposta(entrada);

            conexaoToken.disconnect();

            if (codigoResposta < 200 ||
                    codigoResposta >= 300) {

                System.out.println(
                        "ERRO TOKEN GOOGLE:"
                );

                System.out.println(
                        respostaToken
                );

                response.sendRedirect(
                        "login.html?erro=token"
                );

                return;
            }

            JsonObject tokenJson =
                    JsonParser.parseString(
                            respostaToken
                    ).getAsJsonObject();

            if (!tokenJson.has("access_token")) {

                response.sendRedirect(
                        "login.html?erro=token"
                );

                return;
            }

            String accessToken =
                    tokenJson.get(
                            "access_token"
                    ).getAsString();

            // =================================================
            // PEGAR DADOS DO USUÁRIO GOOGLE
            // =================================================

            URL urlUsuario =
                    new URL(
                            "https://openidconnect.googleapis.com/v1/userinfo"
                    );

            HttpURLConnection conexaoUsuario =
                    (HttpURLConnection)
                            urlUsuario.openConnection();

            conexaoUsuario.setRequestMethod("GET");

            conexaoUsuario.setRequestProperty(
                    "Authorization",
                    "Bearer " + accessToken
            );

            int codigoUsuario =
                    conexaoUsuario.getResponseCode();

            InputStream entradaUsuario;

            if (codigoUsuario >= 200 &&
                    codigoUsuario < 300) {

                entradaUsuario =
                        conexaoUsuario.getInputStream();

            } else {

                entradaUsuario =
                        conexaoUsuario.getErrorStream();
            }

            String respostaUsuario =
                    lerResposta(
                            entradaUsuario
                    );

            conexaoUsuario.disconnect();

            if (codigoUsuario < 200 ||
                    codigoUsuario >= 300) {

                System.out.println(
                        "ERRO AO PEGAR USUARIO GOOGLE:"
                );

                System.out.println(
                        respostaUsuario
                );

                response.sendRedirect(
                        "login.html?erro=usuario_google"
                );

                return;
            }

            JsonObject dados =
                    JsonParser.parseString(
                            respostaUsuario
                    ).getAsJsonObject();

            String nome =
                    dados.has("name")
                    ? dados.get("name").getAsString()
                    : "Usuário Google";

            String email =
                    dados.has("email")
                    ? dados.get("email").getAsString()
                    : null;

            String foto =
                    dados.has("picture")
                    ? dados.get("picture").getAsString()
                    : "";

            if (email == null ||
                    email.trim().isEmpty()) {

                response.sendRedirect(
                        "login.html?erro=sem_email"
                );

                return;
            }

            // =================================================
            // CRIAR / VERIFICAR BANCO
            // =================================================

            CriarBanco.criarTabela();

            // =================================================
            // PROCURAR USUARIO
            // =================================================

            Usuario usuario =
                    buscarUsuarioPorEmail(email);

            if (usuario == null) {

                usuario =
                        criarUsuarioGoogle(
                                nome,
                                email,
                                foto
                        );
            }

            if (usuario == null) {

                response.sendRedirect(
                        "login.html?erro=criar_usuario"
                );

                return;
            }

            // =================================================
            // LOGIN
            // =================================================

            HttpSession novaSessao =
                    request.getSession(true);

            novaSessao.setAttribute(
                    "usuario",
                    usuario
            );

            System.out.println(
                    "LOGIN GOOGLE REALIZADO!"
            );

            response.sendRedirect(
                    "home"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect(
                    "login.html?erro=google"
            );
        }
    }

    // =========================================================
    // LER RESPOSTA
    // =========================================================

    private String lerResposta(
            InputStream entrada)
            throws IOException {

        if (entrada == null) {

            return "";
        }

        StringBuilder resultado =
                new StringBuilder();

        try (
                BufferedReader leitor =
                        new BufferedReader(
                                new InputStreamReader(
                                        entrada,
                                        StandardCharsets.UTF_8
                                )
                        )
        ) {

            String linha;

            while (
                    (linha = leitor.readLine()) != null
            ) {

                resultado.append(linha);
            }
        }

        return resultado.toString();
    }

    // =========================================================
    // BUSCAR USUARIO
    // =========================================================

    private Usuario buscarUsuarioPorEmail(
            String email)
            throws Exception {

        Connection conexao =
                Conexao.conectar();

        if (conexao == null) {

            throw new Exception(
                    "Não foi possível conectar ao banco."
            );
        }

        String sql =
                "SELECT * "
                + "FROM usuario "
                + "WHERE email = ?";

        PreparedStatement stmt =
                conexao.prepareStatement(sql);

        stmt.setString(
                1,
                email
        );

        ResultSet rs =
                stmt.executeQuery();

        Usuario usuario = null;

        if (rs.next()) {

            usuario =
                    new Usuario();

            usuario.setId(
                    rs.getInt("id")
            );

            usuario.setNome(
                    rs.getString("nome")
            );

            usuario.setUsername(
                    rs.getString("username")
            );

            usuario.setEmail(
                    rs.getString("email")
            );

            usuario.setSenha(
                    rs.getString("senha")
            );

            usuario.setFoto(
                    rs.getString("foto")
            );

            usuario.setBio(
                    rs.getString("bio")
            );

            usuario.setDataNascimento(
                    rs.getString("data_nascimento")
            );

            usuario.setPais(
                    rs.getString("pais")
            );

            usuario.setPlataformaFavorita(
                    rs.getString(
                            "plataforma_favorita"
                    )
            );
        }

        rs.close();
        stmt.close();
        conexao.close();

        return usuario;
    }

    // =========================================================
    // CRIAR USUARIO GOOGLE
    // =========================================================

    private Usuario criarUsuarioGoogle(
            String nome,
            String email,
            String foto)
            throws Exception {

        Connection conexao =
                Conexao.conectar();

        if (conexao == null) {

            throw new Exception(
                    "Não foi possível conectar ao banco."
            );
        }

        String usernameBase =
                nome
                        .toLowerCase()
                        .replaceAll(
                                "[^a-z0-9]",
                                ""
                        );

        if (usernameBase.isEmpty()) {

            usernameBase =
                    "googleuser";
        }

        String username =
                usernameBase;

        int contador = 1;

        while (
                usernameExiste(
                        conexao,
                        username
                )
        ) {

            username =
                    usernameBase
                    + contador;

            contador++;
        }

        String sql =
                "INSERT INTO usuario "
                + "(nome, username, email, senha, foto) "
                + "VALUES (?, ?, ?, ?, ?)";

        PreparedStatement stmt =
                conexao.prepareStatement(
                        sql,
                        java.sql.Statement.RETURN_GENERATED_KEYS
                );

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
                email
        );

        stmt.setString(
                4,
                "GOOGLE_LOGIN"
        );

        stmt.setString(
                5,
                foto
        );

        stmt.executeUpdate();

        ResultSet chaves =
                stmt.getGeneratedKeys();

        int id = 0;

        if (chaves.next()) {

            id =
                    chaves.getInt(1);
        }

        chaves.close();
        stmt.close();
        conexao.close();

        Usuario usuario =
                new Usuario();

        usuario.setId(id);
        usuario.setNome(nome);
        usuario.setUsername(username);
        usuario.setEmail(email);
        usuario.setSenha("GOOGLE_LOGIN");
        usuario.setFoto(foto);

        return usuario;
    }

    // =========================================================
    // VERIFICAR USERNAME
    // =========================================================

    private boolean usernameExiste(
            Connection conexao,
            String username)
            throws Exception {

        String sql =
                "SELECT id "
                + "FROM usuario "
                + "WHERE username = ?";

        PreparedStatement stmt =
                conexao.prepareStatement(sql);

        stmt.setString(
                1,
                username
        );

        ResultSet rs =
                stmt.executeQuery();

        boolean existe =
                rs.next();

        rs.close();
        stmt.close();

        return existe;
    }
}