package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CriarBanco {


public static void criarTabela() {

    try {

        Connection conexao = Conexao.conectar();

        if (conexao == null) {

            System.out.println(
                    "Não foi possível conectar ao banco."
            );

            return;
        }

        Statement stmt =
                conexao.createStatement();

        // =========================================
        // TABELA USUARIO
        // =========================================

        String tabelaUsuario =
                "CREATE TABLE IF NOT EXISTS usuario ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "nome TEXT NOT NULL,"
                + "username TEXT,"
                + "email TEXT NOT NULL UNIQUE,"
                + "senha TEXT NOT NULL,"
                + "foto TEXT,"
                + "bio TEXT,"
                + "data_nascimento TEXT,"
                + "pais TEXT,"
                + "plataforma_favorita TEXT,"
                + "google_id TEXT"
                + ")";

        stmt.execute(tabelaUsuario);
        try {
    stmt.execute(
        "ALTER TABLE usuario ADD COLUMN google_id TEXT"
    );

    System.out.println("Coluna google_id adicionada!");
} catch (Exception e) {
    // A coluna já existe
}

        // =========================================
        // VERIFICAR USERNAME
        // =========================================

        boolean usernameExiste = false;

        ResultSet colunas =
                stmt.executeQuery(
                        "PRAGMA table_info(usuario)"
                );

        while (colunas.next()) {

            String nomeColuna =
                    colunas.getString("name");

            if ("username".equalsIgnoreCase(
                    nomeColuna)) {

                usernameExiste = true;
                break;
            }
        }

        colunas.close();

        // =========================================
        // ADICIONAR USERNAME
        // CASO NÃO EXISTA
        // =========================================

        if (!usernameExiste) {

            stmt.execute(
                    "ALTER TABLE usuario "
                    + "ADD COLUMN username TEXT"
            );

            System.out.println(
                    "Coluna username adicionada!"
            );
        }

        // =========================================
        // VERIFICAR GOOGLE_ID
        // =========================================

        boolean googleIdExiste = false;

        ResultSet colunasGoogle =
                stmt.executeQuery(
                        "PRAGMA table_info(usuario)"
                );

        while (colunasGoogle.next()) {

            String nomeColuna =
                    colunasGoogle.getString("name");

            if ("google_id".equalsIgnoreCase(
                    nomeColuna)) {

                googleIdExiste = true;
                break;
            }
        }

        colunasGoogle.close();

        // =========================================
        // ADICIONAR GOOGLE_ID
        // CASO NÃO EXISTA
        // =========================================

        if (!googleIdExiste) {

            stmt.execute(
                    "ALTER TABLE usuario "
                    + "ADD COLUMN google_id TEXT"
            );

            System.out.println(
                    "Coluna google_id adicionada!"
            );
        }

        // =========================================
        // USERNAMES ANTIGOS
        // =========================================

        stmt.execute(
                "UPDATE usuario "
                + "SET username = 'usuario' || id "
                + "WHERE username IS NULL "
                + "OR username = ''"
        );

        // =========================================
        // INDICE USERNAME
        // =========================================

        stmt.execute(
                "CREATE UNIQUE INDEX IF NOT EXISTS "
                + "idx_usuario_username "
                + "ON usuario(username)"
        );

        // =========================================
        // INDICE GOOGLE_ID
        // =========================================

        stmt.execute(
                "CREATE UNIQUE INDEX IF NOT EXISTS "
                + "idx_usuario_google_id "
                + "ON usuario(google_id)"
        );

        // =========================================
        // TABELA JOGO
        // =========================================

        String tabelaJogo =
                "CREATE TABLE IF NOT EXISTS jogo ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "titulo TEXT NOT NULL,"
                + "descricao TEXT,"
                + "genero TEXT,"
                + "plataforma TEXT,"
                + "ano_lancamento INTEGER,"
                + "capa TEXT"
                + ")";

        stmt.execute(tabelaJogo);

        // =========================================
        // TABELA SEGUIDOR
        // =========================================

        String tabelaSeguidor =
                "CREATE TABLE IF NOT EXISTS seguidor ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "id_seguidor INTEGER NOT NULL,"
                + "id_seguido INTEGER NOT NULL,"
                + "data_seguida TEXT DEFAULT CURRENT_TIMESTAMP,"
                + "UNIQUE(id_seguidor, id_seguido),"
                + "FOREIGN KEY(id_seguidor) REFERENCES usuario(id),"
                + "FOREIGN KEY(id_seguido) REFERENCES usuario(id)"
                + ")";

        stmt.execute(tabelaSeguidor);

        // =========================================
        // TABELA BIBLIOTECA
        // =========================================

        String tabelaBiblioteca =
                "CREATE TABLE IF NOT EXISTS biblioteca ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "id_usuario INTEGER NOT NULL,"
                + "id_jogo INTEGER NOT NULL,"
                + "status TEXT DEFAULT 'quero jogar',"
                + "horas_jogadas REAL DEFAULT 0,"
                + "data_adicionado TEXT DEFAULT CURRENT_TIMESTAMP,"
                + "UNIQUE(id_usuario, id_jogo),"
                + "FOREIGN KEY(id_usuario) REFERENCES usuario(id),"
                + "FOREIGN KEY(id_jogo) REFERENCES jogo(id)"
                + ")";

        stmt.execute(tabelaBiblioteca);

        // =========================================
        // VERIFICAR HORAS JOGADAS
        // =========================================

        boolean horasExiste = false;

        ResultSet colunasBiblioteca =
                stmt.executeQuery(
                        "PRAGMA table_info(biblioteca)"
                );

        while (colunasBiblioteca.next()) {

            String nomeColuna =
                    colunasBiblioteca.getString("name");

            if ("horas_jogadas".equalsIgnoreCase(
                    nomeColuna)) {

                horasExiste = true;
                break;
            }
        }

        colunasBiblioteca.close();

        // =========================================
        // ADICIONAR HORAS JOGADAS
        // =========================================

        if (!horasExiste) {

            stmt.execute(
                    "ALTER TABLE biblioteca "
                    + "ADD COLUMN horas_jogadas "
                    + "REAL DEFAULT 0"
            );

            System.out.println(
                    "Coluna horas_jogadas adicionada!"
            );
        }

        // =========================================
        // TABELA AVALIACAO
        // =========================================

        String tabelaAvaliacao =
                "CREATE TABLE IF NOT EXISTS avaliacao ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "id_usuario INTEGER NOT NULL,"
                + "id_jogo INTEGER NOT NULL,"
                + "nota REAL NOT NULL,"
                + "comentario TEXT,"
                + "data_avaliacao TEXT DEFAULT CURRENT_TIMESTAMP,"
                + "FOREIGN KEY(id_usuario) REFERENCES usuario(id),"
                + "FOREIGN KEY(id_jogo) REFERENCES jogo(id),"
                + "UNIQUE(id_usuario, id_jogo)"
                + ")";

        stmt.execute(tabelaAvaliacao);

        // =========================================
        // VERIFICAR HORAS NA AVALIACAO
        // =========================================

        boolean horasAvaliacaoExiste = false;

        ResultSet colunasAvaliacao =
                stmt.executeQuery(
                        "PRAGMA table_info(avaliacao)"
                );

        while (colunasAvaliacao.next()) {

            String nomeColuna =
                    colunasAvaliacao.getString("name");

            if ("horas_jogadas".equalsIgnoreCase(
                    nomeColuna)) {

                horasAvaliacaoExiste = true;
                break;
            }
        }

        colunasAvaliacao.close();

        // =========================================
        // ADICIONAR HORAS NA AVALIACAO
        // =========================================

        if (!horasAvaliacaoExiste) {

            stmt.execute(
                    "ALTER TABLE avaliacao "
                    + "ADD COLUMN horas_jogadas "
                    + "REAL DEFAULT 0"
            );

            System.out.println(
                    "Coluna horas_jogadas adicionada "
                    + "na tabela avaliacao!"
            );
        }

        // =========================================
        // ADICIONAR JOGOS
        // =========================================

        adicionarJogos(stmt);

        // =========================================
        // FECHAR
        // =========================================

        stmt.close();

        conexao.close();

        System.out.println(
                "================================="
        );

        System.out.println(
                "Banco do GameBoxd atualizado "
                + "com sucesso!"
        );

        System.out.println(
                "================================="
        );

    } catch (Exception e) {

        System.out.println(
                "ERRO AO ATUALIZAR O BANCO:"
        );

        e.printStackTrace();
    }
}

// =========================================
// ADICIONAR JOGOS
// =========================================

private static void adicionarJogos(
        Statement stmt) throws Exception {

    String[][] jogos = {

        {
            "Resident Evil 4",
            "Terror e ação com Leon S. Kennedy.",
            "Terror / Ação",
            "PlayStation / Xbox / PC",
            "2023",
            "imagensjogos/jogos/resident-evil-4.jpg"
        },

        {
            "The Last of Us Part I",
            "Uma jornada em um mundo pós-apocalíptico.",
            "Ação / Aventura",
            "PlayStation / PC",
            "2022",
            "imagensjogos/jogos/the-last-of-us-part-1.jpg"
        },

        {
            "God of War Ragnarök",
            "Kratos e Atreus enfrentam o destino dos deuses.",
            "Ação / Aventura",
            "PlayStation / PC",
            "2022",
            "imagensjogos/jogos/god-of-war-ragnarok.jpg"
        },

        {
            "Minecraft",
            "Explore, construa e sobreviva em um mundo de blocos.",
            "Sandbox",
            "PC / PlayStation / Xbox / Nintendo",
            "2011",
            "imagensjogos/jogos/minecraft.jpg"
        },

        {
            "Red Dead Redemption 2",
            "Uma grande aventura no Velho Oeste.",
            "Ação / Aventura",
            "PlayStation / Xbox / PC",
            "2018",
            "imagensjogos/jogos/red-dead-redemption-2.jpg"
        },

        {
            "Grand Theft Auto V",
            "Acompanhe três criminosos em Los Santos.",
            "Ação / Mundo Aberto",
            "PlayStation / Xbox / PC",
            "2013",
            "imagensjogos/jogos/gta-v.jpg"
        },

        {
            "Silent Hill 2",
            "Uma jornada assustadora pela cidade de Silent Hill.",
            "Terror",
            "PlayStation / Xbox / PC",
            "2024",
            "imagensjogos/jogos/silent-hill-2.jpg"
        },

        {
            "Elden Ring",
            "Explore um enorme mundo de fantasia e desafios.",
            "RPG / Ação",
            "PlayStation / Xbox / PC",
            "2022",
            "imagensjogos/jogos/elden-ring.jpg"
        },

        {
            "Resident Evil Village",
            "Ethan Winters enfrenta novos horrores.",
            "Terror / Ação",
            "PlayStation / Xbox / PC",
            "2021",
            "imagensjogos/jogos/resident-evil-village.jpg"
        },

        {
            "The Witcher 3",
            "Geralt procura por sua filha adotiva.",
            "RPG / Aventura",
            "PlayStation / Xbox / PC / Nintendo",
            "2015",
            "imagensjogos/jogos/the-witcher-3.jpg"
        },

        {
            "Cyberpunk 2077",
            "Explore Night City em um futuro tecnológico.",
            "RPG / Ação",
            "PlayStation / Xbox / PC",
            "2020",
            "imagensjogos/jogos/cyberpunk-2077.jpg"
        },

        {
            "Marvel's Spider-Man 2",
            "Peter Parker e Miles Morales protegem Nova York.",
            "Ação / Aventura",
            "PlayStation / PC",
            "2023",
            "imagensjogos/jogos/spider-man-2.jpg"
        }
    };

    // =========================================
    // INSERIR OU ATUALIZAR JOGOS
    // =========================================

    for (String[] jogo : jogos) {

        String titulo = jogo[0];

        String verificar =
                "SELECT id FROM jogo "
                + "WHERE titulo = '"
                + titulo.replace("'", "''")
                + "'";

        ResultSet resultado =
                stmt.executeQuery(verificar);

        boolean existe =
                resultado.next();

        resultado.close();

        if (!existe) {

            String sql =
                    "INSERT INTO jogo "
                    + "(titulo, descricao, genero, "
                    + "plataforma, ano_lancamento, capa) "
                    + "VALUES ("
                    + "'"
                    + jogo[0].replace("'", "''")
                    + "',"
                    + "'"
                    + jogo[1].replace("'", "''")
                    + "',"
                    + "'"
                    + jogo[2].replace("'", "''")
                    + "',"
                    + "'"
                    + jogo[3].replace("'", "''")
                    + "',"
                    + jogo[4]
                    + ","
                    + "'"
                    + jogo[5].replace("'", "''")
                    + "'"
                    + ")";

            stmt.executeUpdate(sql);

            System.out.println(
                    "Jogo adicionado: "
                    + titulo
            );

        } else {

            String atualizar =
                    "UPDATE jogo SET capa = '"
                    + jogo[5].replace("'", "''")
                    + "' WHERE titulo = '"
                    + titulo.replace("'", "''")
                    + "'";

            stmt.executeUpdate(atualizar);

            System.out.println(
                    "Capa atualizada: "
                    + titulo
            );
        }
    }
    
}

public static void main(String[] args) {
    criarTabela();
}

}
