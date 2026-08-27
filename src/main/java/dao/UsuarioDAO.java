package dao;

import model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class UsuarioDAO {

    // =========================
    // CADASTRAR USUARIO
    // =========================

    public boolean cadastrar(Usuario usuario) {

        String sql = "INSERT INTO usuario "
                + "(nome, username, email, senha, foto, bio, data_nascimento, pais, plataforma_favorita) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conexao = null;
        PreparedStatement stmt = null;

        try {

            conexao = Conexao.conectar();

            if (conexao == null) {
                System.out.println("ERRO: conexão com banco é NULL!");
                return false;
            }

            stmt = conexao.prepareStatement(sql);

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getUsername());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getSenha());
            stmt.setString(5, usuario.getFoto());
            stmt.setString(6, usuario.getBio());
            stmt.setString(7, usuario.getDataNascimento());
            stmt.setString(8, usuario.getPais());
            stmt.setString(9, usuario.getPlataformaFavorita());

            int resultado = stmt.executeUpdate();

            return resultado > 0;

        } catch (Exception e) {

            System.out.println("==============================");
            System.out.println("ERRO AO CADASTRAR:");
            System.out.println("TIPO: " + e.getClass().getName());
            System.out.println("MENSAGEM: " + e.getMessage());
            System.out.println("==============================");

            e.printStackTrace();

            return false;

        } finally {

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
    }


    // =========================
    // LOGIN
    // =========================

    public Usuario login(String email, String senha) {

        String sql = "SELECT * FROM usuario "
                + "WHERE email = ? AND senha = ?";

        try {

            Connection conexao =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setString(1, email);
            stmt.setString(2, senha);

            ResultSet resultado =
                    stmt.executeQuery();

            if (resultado.next()) {

                Usuario usuario =
                        criarUsuario(resultado);

                resultado.close();
                stmt.close();
                conexao.close();

                return usuario;
            }

            resultado.close();
            stmt.close();
            conexao.close();

        } catch (Exception e) {

            System.out.println("==============================");
            System.out.println("ERRO NO LOGIN:");
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("==============================");
        }

        return null;
    }


    // =========================
    // BUSCAR POR ID
    // =========================

    public Usuario buscarPorId(int id) {

        String sql =
                "SELECT * FROM usuario WHERE id = ?";

        try {

            Connection conexao =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(1, id);

            ResultSet resultado =
                    stmt.executeQuery();

            if (resultado.next()) {

                Usuario usuario =
                        criarUsuario(resultado);

                resultado.close();
                stmt.close();
                conexao.close();

                return usuario;
            }

            resultado.close();
            stmt.close();
            conexao.close();

        } catch (Exception e) {

            System.out.println("==============================");
            System.out.println("ERRO AO BUSCAR USUARIO:");
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("==============================");
        }

        return null;
    }


    // =========================
    // BUSCAR EXATAMENTE POR USERNAME
    // =========================

    public Usuario buscarPorUsername(String username) {

        String sql =
                "SELECT * FROM usuario "
                + "WHERE username = ?";

        try {

            Connection conexao =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setString(1, username);

            ResultSet resultado =
                    stmt.executeQuery();

            if (resultado.next()) {

                Usuario usuario =
                        criarUsuario(resultado);

                resultado.close();
                stmt.close();
                conexao.close();

                return usuario;
            }

            resultado.close();
            stmt.close();
            conexao.close();

        } catch (Exception e) {

            System.out.println("==============================");
            System.out.println("ERRO AO BUSCAR USERNAME:");
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.out.println("==============================");
        }

        return null;
    }


    // =========================
    // BUSCAR USERNAMES
    // =========================

    public ArrayList<Usuario> buscarPorUsernameParcial(String username) {

        ArrayList<Usuario> usuarios =
                new ArrayList<Usuario>();

        String sql =
                "SELECT * FROM usuario "
                + "WHERE username LIKE ? "
                + "ORDER BY username";

        try {

            Connection conexao =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setString(
                    1,
                    "%" + username + "%"
            );

            ResultSet resultado =
                    stmt.executeQuery();

            while (resultado.next()) {

                Usuario usuario =
                        criarUsuario(resultado);

                usuarios.add(usuario);
            }

            resultado.close();
            stmt.close();
            conexao.close();

        } catch (Exception e) {

            System.out.println("==============================");
            System.out.println("ERRO AO BUSCAR USERNAMES:");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return usuarios;
    }


    // =========================
    // BUSCAR POR NOME
    // =========================

    public ArrayList<Usuario> buscarPorNome(String nome) {

        ArrayList<Usuario> usuarios =
                new ArrayList<Usuario>();

        String sql =
                "SELECT * FROM usuario "
                + "WHERE nome LIKE ? "
                + "ORDER BY nome";

        try {

            Connection conexao =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setString(
                    1,
                    "%" + nome + "%"
            );

            ResultSet resultado =
                    stmt.executeQuery();

            while (resultado.next()) {

                Usuario usuario =
                        criarUsuario(resultado);

                usuarios.add(usuario);
            }

            resultado.close();
            stmt.close();
            conexao.close();

        } catch (Exception e) {

            System.out.println("==============================");
            System.out.println("ERRO AO BUSCAR NOMES:");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return usuarios;
    }


    // =========================
    // LISTAR USUARIOS
    // =========================

    public ArrayList<Usuario> listar() {

        ArrayList<Usuario> usuarios =
                new ArrayList<Usuario>();

        String sql =
                "SELECT * FROM usuario";

        try {

            Connection conexao =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            ResultSet resultado =
                    stmt.executeQuery();

            while (resultado.next()) {

                Usuario usuario =
                        criarUsuario(resultado);

                usuarios.add(usuario);
            }

            resultado.close();
            stmt.close();
            conexao.close();

        } catch (Exception e) {

            System.out.println("==============================");
            System.out.println("ERRO AO LISTAR USUARIOS:");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return usuarios;
    }


    // =========================
    // ATUALIZAR USUARIO
    // =========================

    public boolean atualizar(Usuario usuario) {

        String sql =
                "UPDATE usuario SET "
                + "nome = ?, "
                + "username = ?, "
                + "email = ?, "
                + "senha = ?, "
                + "foto = ?, "
                + "bio = ?, "
                + "data_nascimento = ?, "
                + "pais = ?, "
                + "plataforma_favorita = ? "
                + "WHERE id = ?";

        try {

            Connection conexao =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getUsername());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getSenha());
            stmt.setString(5, usuario.getFoto());
            stmt.setString(6, usuario.getBio());
            stmt.setString(7, usuario.getDataNascimento());
            stmt.setString(8, usuario.getPais());
            stmt.setString(9, usuario.getPlataformaFavorita());
            stmt.setInt(10, usuario.getId());

            int resultado =
                    stmt.executeUpdate();

            stmt.close();
            conexao.close();

            return resultado > 0;

        } catch (Exception e) {

            System.out.println("==============================");
            System.out.println("ERRO AO ATUALIZAR USUARIO:");
            System.out.println(e.getMessage());
            e.printStackTrace();

            return false;
        }
    }


    // =========================
    // EXCLUIR USUARIO
    // =========================

    public boolean excluir(int id) {

        String sql =
                "DELETE FROM usuario WHERE id = ?";

        try {

            Connection conexao =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(1, id);

            int resultado =
                    stmt.executeUpdate();

            stmt.close();
            conexao.close();

            return resultado > 0;

        } catch (Exception e) {

            System.out.println("==============================");
            System.out.println("ERRO AO EXCLUIR USUARIO:");
            System.out.println(e.getMessage());
            e.printStackTrace();

            return false;
        }
    }


    // =========================
    // CRIAR OBJETO USUARIO
    // =========================

    private Usuario criarUsuario(ResultSet resultado)
            throws Exception {

        Usuario usuario = new Usuario();

        usuario.setId(
                resultado.getInt("id")
        );

        usuario.setNome(
                resultado.getString("nome")
        );

        usuario.setUsername(
                resultado.getString("username")
        );

        usuario.setEmail(
                resultado.getString("email")
        );

        usuario.setSenha(
                resultado.getString("senha")
        );

        usuario.setFoto(
                resultado.getString("foto")
        );

        usuario.setBio(
                resultado.getString("bio")
        );

        usuario.setDataNascimento(
                resultado.getString("data_nascimento")
        );

        usuario.setPais(
                resultado.getString("pais")
        );

        usuario.setPlataformaFavorita(
                resultado.getString("plataforma_favorita")
        );

        return usuario;
    }
}