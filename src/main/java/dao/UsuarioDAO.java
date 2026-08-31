package dao;

import model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;

public class UsuarioDAO {

    // =====================================================
    // CADASTRAR USUARIO
    // =====================================================

    public boolean cadastrar(Usuario usuario) {

        String sql =
                "INSERT INTO usuario "
                + "(nome, username, email, senha, foto, bio, "
                + "data_nascimento, pais, plataforma_favorita) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conexao = null;
        PreparedStatement stmt = null;

        try {

            conexao = Conexao.conectar();

            if (conexao == null) {

                System.out.println(
                        "ERRO: conexão com banco é NULL!"
                );

                return false;
            }

            stmt =
                    conexao.prepareStatement(sql);

            stmt.setString(
                    1,
                    usuario.getNome()
            );

            stmt.setString(
                    2,
                    usuario.getUsername()
            );

            stmt.setString(
                    3,
                    usuario.getEmail()
            );

            stmt.setString(
                    4,
                    usuario.getSenha()
            );

            stmt.setString(
                    5,
                    usuario.getFoto()
            );

            stmt.setString(
                    6,
                    usuario.getBio()
            );

            stmt.setString(
                    7,
                    usuario.getDataNascimento()
            );

            stmt.setString(
                    8,
                    usuario.getPais()
            );

            stmt.setString(
                    9,
                    usuario.getPlataformaFavorita()
            );

            int resultado =
                    stmt.executeUpdate();

            return resultado > 0;

        } catch (Exception e) {

            System.out.println(
                    "=============================="
            );

            System.out.println(
                    "ERRO AO CADASTRAR:"
            );

            System.out.println(
                    "TIPO: "
                    + e.getClass().getName()
            );

            System.out.println(
                    "MENSAGEM: "
                    + e.getMessage()
            );

            System.out.println(
                    "=============================="
            );

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

    // =====================================================
    // LOGIN
    // =====================================================

    public Usuario login(
            String email,
            String senha) {

        String sql =
                "SELECT * FROM usuario "
                + "WHERE email = ? "
                + "AND senha = ?";

        try {

            Connection conexao =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setString(
                    1,
                    email
            );

            stmt.setString(
                    2,
                    senha
            );

            ResultSet resultado =
                    stmt.executeQuery();

            if (resultado.next()) {

                Usuario usuario =
                        criarUsuario(
                                resultado
                        );

                resultado.close();
                stmt.close();
                conexao.close();

                return usuario;
            }

            resultado.close();
            stmt.close();
            conexao.close();

        } catch (Exception e) {

            System.out.println(
                    "=============================="
            );

            System.out.println(
                    "ERRO NO LOGIN:"
            );

            System.out.println(
                    e.getMessage()
            );

            e.printStackTrace();

            System.out.println(
                    "=============================="
            );
        }

        return null;
    }

    // =====================================================
    // BUSCAR POR ID
    // =====================================================

    public Usuario buscarPorId(int id) {

        String sql =
                "SELECT * FROM usuario "
                + "WHERE id = ?";

        try {

            Connection conexao =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(
                    1,
                    id
            );

            ResultSet resultado =
                    stmt.executeQuery();

            if (resultado.next()) {

                Usuario usuario =
                        criarUsuario(
                                resultado
                        );

                resultado.close();
                stmt.close();
                conexao.close();

                return usuario;
            }

            resultado.close();
            stmt.close();
            conexao.close();

        } catch (Exception e) {

            System.out.println(
                    "ERRO AO BUSCAR USUARIO:"
            );

            e.printStackTrace();
        }

        return null;
    }

    // =====================================================
    // BUSCAR POR USERNAME
    // =====================================================

    public Usuario buscarPorUsername(
            String username) {

        String sql =
                "SELECT * FROM usuario "
                + "WHERE username = ?";

        try {

            Connection conexao =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setString(
                    1,
                    username
            );

            ResultSet resultado =
                    stmt.executeQuery();

            if (resultado.next()) {

                Usuario usuario =
                        criarUsuario(
                                resultado
                        );

                resultado.close();
                stmt.close();
                conexao.close();

                return usuario;
            }

            resultado.close();
            stmt.close();
            conexao.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    // =====================================================
    // BUSCAR POR EMAIL
    // =====================================================

    public Usuario buscarPorEmail(
            String email) {

        String sql =
                "SELECT * FROM usuario "
                + "WHERE email = ?";

        try {

            Connection conexao =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setString(
                    1,
                    email
            );

            ResultSet resultado =
                    stmt.executeQuery();

            if (resultado.next()) {

                Usuario usuario =
                        criarUsuario(
                                resultado
                        );

                resultado.close();
                stmt.close();
                conexao.close();

                return usuario;
            }

            resultado.close();
            stmt.close();
            conexao.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }

    // =====================================================
    // BUSCAR USERNAMES PARCIAL
    // =====================================================

    public ArrayList<Usuario>
            buscarPorUsernameParcial(
                    String username) {

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
                        criarUsuario(
                                resultado
                        );

                usuarios.add(
                        usuario
                );
            }

            resultado.close();
            stmt.close();
            conexao.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return usuarios;
    }

    // =====================================================
    // BUSCAR POR NOME
    // =====================================================

    public ArrayList<Usuario>
            buscarPorNome(
                    String nome) {

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
                        criarUsuario(
                                resultado
                        );

                usuarios.add(
                        usuario
                );
            }

            resultado.close();
            stmt.close();
            conexao.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return usuarios;
    }

    // =====================================================
    // LISTAR
    // =====================================================

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
                        criarUsuario(
                                resultado
                        );

                usuarios.add(
                        usuario
                );
            }

            resultado.close();
            stmt.close();
            conexao.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return usuarios;
    }

    // =====================================================
    // ATUALIZAR
    // =====================================================

    public boolean atualizar(
            Usuario usuario) {

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

            stmt.setString(
                    1,
                    usuario.getNome()
            );

            stmt.setString(
                    2,
                    usuario.getUsername()
            );

            stmt.setString(
                    3,
                    usuario.getEmail()
            );

            stmt.setString(
                    4,
                    usuario.getSenha()
            );

            stmt.setString(
                    5,
                    usuario.getFoto()
            );

            stmt.setString(
                    6,
                    usuario.getBio()
            );

            stmt.setString(
                    7,
                    usuario.getDataNascimento()
            );

            stmt.setString(
                    8,
                    usuario.getPais()
            );

            stmt.setString(
                    9,
                    usuario.getPlataformaFavorita()
            );

            stmt.setInt(
                    10,
                    usuario.getId()
            );

            int resultado =
                    stmt.executeUpdate();

            stmt.close();
            conexao.close();

            return resultado > 0;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // EXCLUIR
    // =====================================================

    public boolean excluir(int id) {

        String sql =
                "DELETE FROM usuario "
                + "WHERE id = ?";

        try {

            Connection conexao =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setInt(
                    1,
                    id
            );

            int resultado =
                    stmt.executeUpdate();

            stmt.close();
            conexao.close();

            return resultado > 0;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // SALVAR CADASTRO PENDENTE
    // =====================================================

    public boolean salvarCadastroPendente(
            Usuario usuario,
            String codigo,
            String expiraEm) {

        String sql =
                "INSERT INTO cadastro_pendente "
                + "(nome, username, email, senha, foto, bio, "
                + "data_nascimento, pais, plataforma_favorita, "
                + "codigo, expira_em) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                + "ON CONFLICT(email) DO UPDATE SET "
                + "nome = excluded.nome, "
                + "username = excluded.username, "
                + "senha = excluded.senha, "
                + "foto = excluded.foto, "
                + "bio = excluded.bio, "
                + "data_nascimento = excluded.data_nascimento, "
                + "pais = excluded.pais, "
                + "plataforma_favorita = excluded.plataforma_favorita, "
                + "codigo = excluded.codigo, "
                + "expira_em = excluded.expira_em";

        try {

            Connection conexao =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setString(
                    1,
                    usuario.getNome()
            );

            stmt.setString(
                    2,
                    usuario.getUsername()
            );

            stmt.setString(
                    3,
                    usuario.getEmail()
            );

            stmt.setString(
                    4,
                    usuario.getSenha()
            );

            stmt.setString(
                    5,
                    usuario.getFoto()
            );

            stmt.setString(
                    6,
                    usuario.getBio()
            );

            stmt.setString(
                    7,
                    usuario.getDataNascimento()
            );

            stmt.setString(
                    8,
                    usuario.getPais()
            );

            stmt.setString(
                    9,
                    usuario.getPlataformaFavorita()
            );

            stmt.setString(
                    10,
                    codigo
            );

            stmt.setString(
                    11,
                    expiraEm
            );

            int resultado =
                    stmt.executeUpdate();

            stmt.close();
            conexao.close();

            return resultado > 0;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // VERIFICAR PENDENTE
    // =====================================================

    public boolean existeCadastroPendente(
            String email) {

        String sql =
                "SELECT id "
                + "FROM cadastro_pendente "
                + "WHERE email = ?";

        try {

            Connection conexao =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conexao.prepareStatement(sql);

            stmt.setString(
                    1,
                    email
            );

            ResultSet rs =
                    stmt.executeQuery();

            boolean existe =
                    rs.next();

            rs.close();
            stmt.close();
            conexao.close();

            return existe;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    // =====================================================
    // CONFIRMAR EMAIL
    // =====================================================

    public Usuario confirmarEmail(
            String email,
            String codigo) {

        String sql =
                "SELECT * "
                + "FROM cadastro_pendente "
                + "WHERE email = ? "
                + "AND codigo = ? "
                + "AND expira_em > CURRENT_TIMESTAMP";

        Connection conexao = null;

        try {

            conexao =
                    Conexao.conectar();

            PreparedStatement stmt =
                    conexao.prepareStatement(
                            sql
                    );

            stmt.setString(
                    1,
                    email
            );

            stmt.setString(
                    2,
                    codigo
            );

            ResultSet rs =
                    stmt.executeQuery();

            if (!rs.next()) {

                rs.close();
                stmt.close();
                conexao.close();

                return null;
            }

            Usuario usuario =
                    new Usuario();

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
                    rs.getString(
                            "data_nascimento"
                    )
            );

            usuario.setPais(
                    rs.getString("pais")
            );

            usuario.setPlataformaFavorita(
                    rs.getString(
                            "plataforma_favorita"
                    )
            );

            // =============================================
            // CRIAR CONTA DEFINITIVA
            // =============================================

            String inserir =
                    "INSERT INTO usuario "
                    + "(nome, username, email, senha, foto, bio, "
                    + "data_nascimento, pais, plataforma_favorita) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement stmtInserir =
                    conexao.prepareStatement(
                            inserir,
                            java.sql.Statement.RETURN_GENERATED_KEYS
                    );

            stmtInserir.setString(
                    1,
                    usuario.getNome()
            );

            stmtInserir.setString(
                    2,
                    usuario.getUsername()
            );

            stmtInserir.setString(
                    3,
                    usuario.getEmail()
            );

            stmtInserir.setString(
                    4,
                    usuario.getSenha()
            );

            stmtInserir.setString(
                    5,
                    usuario.getFoto()
            );

            stmtInserir.setString(
                    6,
                    usuario.getBio()
            );

            stmtInserir.setString(
                    7,
                    usuario.getDataNascimento()
            );

            stmtInserir.setString(
                    8,
                    usuario.getPais()
            );

            stmtInserir.setString(
                    9,
                    usuario.getPlataformaFavorita()
            );

            stmtInserir.executeUpdate();

            ResultSet chaves =
                    stmtInserir.getGeneratedKeys();

            if (chaves.next()) {

                usuario.setId(
                        chaves.getInt(1)
                );
            }

            chaves.close();
            stmtInserir.close();

            // =============================================
            // APAGAR PENDENTE
            // =============================================

            String apagar =
                    "DELETE FROM cadastro_pendente "
                    + "WHERE email = ?";

            PreparedStatement stmtApagar =
                    conexao.prepareStatement(
                            apagar
                    );

            stmtApagar.setString(
                    1,
                    email
            );

            stmtApagar.executeUpdate();

            stmtApagar.close();

            rs.close();
            stmt.close();
            conexao.close();

            return usuario;

        } catch (Exception e) {

            e.printStackTrace();

            try {

                if (conexao != null) {
                    conexao.close();
                }

            } catch (Exception erro) {

                erro.printStackTrace();
            }

            return null;
        }
    }

    // =====================================================
    // CRIAR USUARIO
    // =====================================================

    private Usuario criarUsuario(
            ResultSet resultado)
            throws Exception {

        Usuario usuario =
                new Usuario();

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
                resultado.getString(
                        "data_nascimento"
                )
        );

        usuario.setPais(
                resultado.getString("pais")
        );

        usuario.setPlataformaFavorita(
                resultado.getString(
                        "plataforma_favorita"
                )
        );

        return usuario;
    }
}