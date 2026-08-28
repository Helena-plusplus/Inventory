package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class CriarBanco {

    public static void criarTabela() {

        try {

            Connection conexao =
                    Conexao.conectar();

            if (conexao == null) {

                System.out.println(
                        "Não foi possível conectar ao banco."
                );

                return;
            }

            Statement stmt =
                    conexao.createStatement();

            // =====================================================
            // TABELA USUARIO
            // =====================================================

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
                    + "plataforma_favorita TEXT"
                    + ")";

            stmt.execute(tabelaUsuario);

            // =====================================================
            // VERIFICAR USERNAME
            // =====================================================

            boolean usernameExiste = false;

            ResultSet colunasUsuario =
                    stmt.executeQuery(
                            "PRAGMA table_info(usuario)"
                    );

            while (colunasUsuario.next()) {

                String nomeColuna =
                        colunasUsuario.getString("name");

                if ("username".equalsIgnoreCase(
                        nomeColuna
                )) {

                    usernameExiste = true;

                    break;
                }
            }

            colunasUsuario.close();

            // =====================================================
            // CRIAR USERNAME
            // =====================================================

            if (!usernameExiste) {

                stmt.execute(
                        "ALTER TABLE usuario "
                        + "ADD COLUMN username TEXT"
                );
            }

            stmt.execute(
                    "UPDATE usuario "
                    + "SET username = 'usuario' || id "
                    + "WHERE username IS NULL "
                    + "OR username = ''"
            );

            stmt.execute(
                    "CREATE UNIQUE INDEX IF NOT EXISTS "
                    + "idx_usuario_username "
                    + "ON usuario(username)"
            );

            // =====================================================
            // TABELA JOGO
            // =====================================================

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

            // =====================================================
            // TABELA SEGUIDOR
            // =====================================================

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

            // =====================================================
            // TABELA BIBLIOTECA
            // =====================================================

            String tabelaBiblioteca =
                    "CREATE TABLE IF NOT EXISTS biblioteca ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "id_usuario INTEGER NOT NULL,"
                    + "id_jogo INTEGER NOT NULL,"
                    + "status TEXT DEFAULT 'quero jogar',"
                    + "data_adicionado TEXT DEFAULT CURRENT_TIMESTAMP,"
                    + "horas_jogadas REAL DEFAULT 0,"
                    + "UNIQUE(id_usuario, id_jogo),"
                    + "FOREIGN KEY(id_usuario) REFERENCES usuario(id),"
                    + "FOREIGN KEY(id_jogo) REFERENCES jogo(id)"
                    + ")";

            stmt.execute(tabelaBiblioteca);

            // =====================================================
            // GARANTIR HORAS NA BIBLIOTECA
            // =====================================================

            boolean horasBibliotecaExiste = false;

            ResultSet colunasBiblioteca =
                    stmt.executeQuery(
                            "PRAGMA table_info(biblioteca)"
                    );

            while (colunasBiblioteca.next()) {

                String nomeColuna =
                        colunasBiblioteca.getString("name");

                if ("horas_jogadas".equalsIgnoreCase(
                        nomeColuna
                )) {

                    horasBibliotecaExiste = true;

                    break;
                }
            }

            colunasBiblioteca.close();

            if (!horasBibliotecaExiste) {

                stmt.execute(
                        "ALTER TABLE biblioteca "
                        + "ADD COLUMN horas_jogadas REAL DEFAULT 0"
                );
            }

            // =====================================================
            // TABELA AVALIACAO
            // =====================================================

            String tabelaAvaliacao =
                    "CREATE TABLE IF NOT EXISTS avaliacao ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "id_usuario INTEGER NOT NULL,"
                    + "id_jogo INTEGER NOT NULL,"
                    + "nota REAL NOT NULL,"
                    + "comentario TEXT,"
                    + "horas_jogadas REAL DEFAULT 0,"
                    + "data_avaliacao TEXT DEFAULT CURRENT_TIMESTAMP,"
                    + "FOREIGN KEY(id_usuario) REFERENCES usuario(id),"
                    + "FOREIGN KEY(id_jogo) REFERENCES jogo(id),"
                    + "UNIQUE(id_usuario, id_jogo)"
                    + ")";

            stmt.execute(tabelaAvaliacao);

            // =====================================================
            // GARANTIR HORAS NA AVALIACAO
            // =====================================================

            boolean horasAvaliacaoExiste = false;

            ResultSet colunasAvaliacao =
                    stmt.executeQuery(
                            "PRAGMA table_info(avaliacao)"
                    );

            while (colunasAvaliacao.next()) {

                String nomeColuna =
                        colunasAvaliacao.getString("name");

                if ("horas_jogadas".equalsIgnoreCase(
                        nomeColuna
                )) {

                    horasAvaliacaoExiste = true;

                    break;
                }
            }

            colunasAvaliacao.close();

            if (!horasAvaliacaoExiste) {

                stmt.execute(
                        "ALTER TABLE avaliacao "
                        + "ADD COLUMN horas_jogadas REAL DEFAULT 0"
                );
            }

            // =====================================================
            // ADICIONAR JOGOS
            // =====================================================

            adicionarJogos(stmt);

            stmt.close();
            conexao.close();

            System.out.println(
                    "================================="
            );

            System.out.println(
                    "BANCO DO INVENTORY ATUALIZADO!"
            );

            System.out.println(
                    "JOGOS VERIFICADOS/ADICIONADOS!"
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

    // =========================================================
    // ADICIONAR JOGOS
    // =========================================================

    private static void adicionarJogos(
            Statement stmt) throws Exception {

        String[][] jogos = {

            // =================================================
            // JOGOS EXISTENTES
            // =================================================

            {
                "Resident Evil 4",
                "Terror e ação com Leon S. Kennedy.",
                "Terror / Ação",
                "PlayStation / Xbox / PC",
                "2023",
                "2050650"
            },

            {
                "The Last of Us Part I",
                "Uma jornada em um mundo pós-apocalíptico.",
                "Ação / Aventura",
                "PlayStation / PC",
                "2022",
                "1888930"
            },

            {
                "God of War Ragnarök",
                "Kratos e Atreus enfrentam o destino dos deuses.",
                "Ação / Aventura",
                "PlayStation / PC",
                "2022",
                "2322010"
            },

            {
                "Minecraft",
                "Explore, construa e sobreviva em um mundo de blocos.",
                "Sandbox",
                "PC / PlayStation / Xbox / Nintendo",
                "2011",
                "Minecraft"
            },

            {
                "Red Dead Redemption 2",
                "Uma grande aventura no Velho Oeste.",
                "Ação / Aventura",
                "PlayStation / Xbox / PC",
                "2018",
                "1174180"
            },

            {
                "Grand Theft Auto V",
                "Acompanhe três criminosos em Los Santos.",
                "Ação / Mundo Aberto",
                "PlayStation / Xbox / PC",
                "2013",
                "271590"
            },

            {
                "Silent Hill 2",
                "Uma jornada assustadora pela cidade de Silent Hill.",
                "Terror",
                "PlayStation / Xbox / PC",
                "2024",
                "2124490"
            },

            {
                "Elden Ring",
                "Explore um enorme mundo de fantasia e desafios.",
                "RPG / Ação",
                "PlayStation / Xbox / PC",
                "2022",
                "1245620"
            },

            {
                "Resident Evil Village",
                "Ethan Winters enfrenta novos horrores.",
                "Terror / Ação",
                "PlayStation / Xbox / PC",
                "2021",
                "1196590"
            },

            {
                "The Witcher 3",
                "Geralt procura por sua filha adotiva.",
                "RPG / Aventura",
                "PlayStation / Xbox / PC / Nintendo",
                "2015",
                "292030"
            },

            {
                "Cyberpunk 2077",
                "Explore Night City em um futuro tecnológico.",
                "RPG / Ação",
                "PlayStation / Xbox / PC",
                "2020",
                "1091500"
            },

            {
                "Marvel's Spider-Man 2",
                "Peter Parker e Miles Morales protegem Nova York.",
                "Ação / Aventura",
                "PlayStation / PC",
                "2023",
                "2651280"
            },

            // =================================================
            // NOVOS JOGOS
            // =================================================

            {
                "God of War",
                "Kratos inicia uma nova jornada ao lado de Atreus.",
                "Ação / Aventura",
                "PlayStation / PC",
                "2018",
                "1593500"
            },

            {
                "Ghost of Tsushima",
                "Um samurai luta para proteger sua ilha.",
                "Ação / Aventura",
                "PlayStation / PC",
                "2020",
                "2215430"
            },

            {
                "Horizon Zero Dawn",
                "Explore um mundo dominado por máquinas.",
                "RPG / Ação",
                "PlayStation / PC",
                "2017",
                "1151640"
            },

            {
                "Horizon Forbidden West",
                "Aloy enfrenta novas ameaças em um mundo aberto.",
                "RPG / Ação",
                "PlayStation / PC",
                "2022",
                "2420110"
            },

            {
                "Marvel's Spider-Man Remastered",
                "Peter Parker protege Nova York.",
                "Ação / Aventura",
                "PlayStation / PC",
                "2018",
                "1817070"
            },

            {
                "Marvel's Spider-Man: Miles Morales",
                "Miles Morales assume o papel de Homem-Aranha.",
                "Ação / Aventura",
                "PlayStation / PC",
                "2020",
                "1817190"
            },

            {
                "Uncharted: Legacy of Thieves Collection",
                "Duas grandes aventuras de Uncharted.",
                "Ação / Aventura",
                "PlayStation / PC",
                "2022",
                "1659420"
            },

            {
                "Days Gone",
                "Sobreviva em um mundo devastado.",
                "Ação / Aventura",
                "PlayStation / PC",
                "2019",
                "1259420"
            },

            {
                "Dark Souls Remastered",
                "Um RPG sombrio cheio de desafios.",
                "RPG / Ação",
                "PlayStation / Xbox / PC / Nintendo",
                "2018",
                "570940"
            },

            {
                "Dark Souls II",
                "Uma nova jornada por um reino amaldiçoado.",
                "RPG / Ação",
                "PlayStation / Xbox / PC",
                "2014",
                "236430"
            },

            {
                "Dark Souls III",
                "A batalha pelo destino de um mundo em ruínas.",
                "RPG / Ação",
                "PlayStation / Xbox / PC",
                "2016",
                "374320"
            },

            {
                "Sekiro: Shadows Die Twice",
                "Um shinobi busca vingança no Japão feudal.",
                "Ação / Aventura",
                "PlayStation / Xbox / PC",
                "2019",
                "814380"
            },

            {
                "Lies of P",
                "Uma aventura sombria inspirada em Pinóquio.",
                "RPG / Ação",
                "PlayStation / Xbox / PC",
                "2023",
                "1627720"
            },

            {
                "Black Myth: Wukong",
                "Uma aventura inspirada na mitologia chinesa.",
                "RPG / Ação",
                "PlayStation / PC",
                "2024",
                "2358720"
            },

            {
                "Dragon's Dogma 2",
                "Uma aventura de fantasia em mundo aberto.",
                "RPG / Ação",
                "PlayStation / Xbox / PC",
                "2024",
                "2054970"
            },

            {
                "Baldur's Gate 3",
                "Uma grande aventura de RPG.",
                "RPG",
                "PlayStation / Xbox / PC",
                "2023",
                "1086940"
            },

            {
                "Divinity: Original Sin 2",
                "Monte seu grupo e enfrente uma grande aventura.",
                "RPG",
                "PlayStation / Xbox / PC / Nintendo",
                "2017",
                "435150"
            },

            {
                "Persona 5 Royal",
                "Estudantes vivem uma vida dupla.",
                "RPG",
                "PlayStation / Xbox / PC / Nintendo",
                "2019",
                "1687950"
            },

            {
                "Persona 3 Reload",
                "Uma nova versão de Persona 3.",
                "RPG",
                "PlayStation / Xbox / PC",
                "2024",
                "2161700"
            },

            {
                "Persona 4 Golden",
                "Investigue acontecimentos misteriosos.",
                "RPG",
                "PlayStation / PC / Xbox",
                "2012",
                "1113000"
            },

            {
                "Final Fantasy VII Remake Intergrade",
                "Uma aventura moderna em Midgar.",
                "RPG / Ação",
                "PlayStation / PC",
                "2020",
                "1462040"
            },

            {
                "Final Fantasy VII Rebirth",
                "Cloud e seus companheiros deixam Midgar.",
                "RPG / Aventura",
                "PlayStation / PC",
                "2024",
                "2909400"
            },

            {
                "Final Fantasy XVI",
                "Uma fantasia sombria centrada em Clive.",
                "RPG / Ação",
                "PlayStation / PC",
                "2023",
                "2515020"
            },

            {
                "Final Fantasy XV",
                "Noctis e seus amigos viajam pelo mundo.",
                "RPG / Ação",
                "PlayStation / Xbox / PC",
                "2016",
                "637650"
            },

            {
                "Kingdom Hearts III",
                "Sora viaja por vários mundos.",
                "RPG / Ação",
                "PlayStation / Xbox / PC",
                "2019",
                "2552450"
            },

            {
                "NieR:Automata",
                "Androides enfrentam máquinas em um mundo em guerra.",
                "RPG / Ação",
                "PlayStation / Xbox / PC / Nintendo",
                "2017",
                "524220"
            },

            {
                "Monster Hunter: World",
                "Caçe monstros enormes.",
                "RPG / Ação",
                "PlayStation / Xbox / PC",
                "2018",
                "582010"
            },

            {
                "Monster Hunter Rise",
                "Enfrente monstros em novas caçadas.",
                "RPG / Ação",
                "PlayStation / Xbox / PC / Nintendo",
                "2021",
                "1446780"
            },

            {
                "Helldivers 2",
                "Combata ameaças alienígenas em missões cooperativas.",
                "Ação / Multiplayer",
                "PlayStation / PC",
                "2024",
                "553850"
            },

            {
                "The Last of Us Part II Remastered",
                "A continuação da jornada de Ellie.",
                "Ação / Aventura",
                "PlayStation / PC",
                "2024",
                "2531310"
            },

            {
                "Detroit: Become Human",
                "Suas escolhas alteram a história.",
                "Aventura",
                "PlayStation / PC",
                "2018",
                "1222140"
            },

            {
                "Heavy Rain",
                "Um suspense baseado em escolhas.",
                "Aventura",
                "PlayStation / PC",
                "2010",
                "960910"
            },

            {
                "Until Dawn",
                "Um grupo precisa sobreviver a uma noite assustadora.",
                "Terror / Aventura",
                "PlayStation / PC",
                "2015",
                "2172010"
            },

            {
                "The Quarry",
                "Uma noite de acampamento vira um pesadelo.",
                "Terror / Aventura",
                "PlayStation / Xbox / PC",
                "2022",
                "1577120"
            },

            {
                "Outlast",
                "Sobreviva em um hospital abandonado.",
                "Terror",
                "PlayStation / Xbox / PC / Nintendo",
                "2013",
                "238320"
            },

            {
                "Outlast 2",
                "Uma investigação leva a um lugar assustador.",
                "Terror",
                "PlayStation / Xbox / PC / Nintendo",
                "2017",
                "414700"
            },

            {
                "Amnesia: The Dark Descent",
                "Explore um castelo sombrio.",
                "Terror",
                "PC / PlayStation / Xbox",
                "2010",
                "57300"
            },

            {
                "Alien: Isolation",
                "Sobreviva em uma estação espacial.",
                "Terror / Ação",
                "PlayStation / Xbox / PC / Nintendo",
                "2014",
                "214490"
            },

            {
                "Dead Space",
                "Explore uma nave espacial infestada.",
                "Terror / Ação",
                "PlayStation / Xbox / PC",
                "2023",
                "1693980"
            },

            {
                "Dead Space 2",
                "Isaac enfrenta novos perigos espaciais.",
                "Terror / Ação",
                "PlayStation / Xbox / PC",
                "2011",
                "47780"
            },

            {
                "Alan Wake 2",
                "Uma investigação sobrenatural mergulha em uma realidade estranha.",
                "Terror / Ação",
                "PlayStation / Xbox / PC",
                "2023",
                "1085550"
            },

            {
                "Control",
                "Explore uma agência cheia de fenômenos sobrenaturais.",
                "Ação / Aventura",
                "PlayStation / Xbox / PC",
                "2019",
                "870780"
            },

            {
                "Death Stranding",
                "Conecte pessoas em um mundo devastado.",
                "Ação / Aventura",
                "PlayStation / PC",
                "2019",
                "1190460"
            },

            {
                "Red Dead Redemption",
                "John Marston enfrenta seu passado.",
                "Ação / Aventura",
                "PlayStation / Xbox / PC",
                "2010",
                "2668510"
            },

            {
                "Grand Theft Auto IV",
                "Niko Bellic chega a Liberty City.",
                "Ação / Mundo Aberto",
                "PlayStation / Xbox / PC",
                "2008",
                "12210"
            },

            {
                "Grand Theft Auto: San Andreas",
                "CJ retorna para Los Santos.",
                "Ação / Mundo Aberto",
                "PlayStation / Xbox / PC",
                "2004",
                "12120"
            },

            {
                "Grand Theft Auto: Vice City",
                "Explore uma cidade inspirada nos anos 1980.",
                "Ação / Mundo Aberto",
                "PlayStation / Xbox / PC",
                "2002",
                "12110"
            },

            {
                "Grand Theft Auto III",
                "Explore Liberty City.",
                "Ação / Mundo Aberto",
                "PlayStation / Xbox / PC",
                "2001",
                "12100"
            },

            {
                "Bully: Scholarship Edition",
                "Viva um ano escolar cheio de confusões.",
                "Ação / Aventura",
                "PC / Xbox",
                "2006",
                "12200"
            },

            {
                "Mafia: Definitive Edition",
                "Uma história de crime organizada.",
                "Ação / Aventura",
                "PlayStation / Xbox / PC",
                "2020",
                "1030840"
            },

            {
                "Mafia II",
                "Vito tenta construir sua vida no crime.",
                "Ação / Aventura",
                "PlayStation / Xbox / PC",
                "2010",
                "50130"
            },

            {
                "Assassin's Creed II",
                "Ezio inicia sua jornada.",
                "Ação / Aventura",
                "PlayStation / Xbox / PC",
                "2009",
                "33230"
            },

            {
                "Assassin's Creed IV Black Flag",
                "Viva a era dos piratas.",
                "Ação / Aventura",
                "PlayStation / Xbox / PC",
                "2013",
                "242050"
            },

            {
                "Assassin's Creed Origins",
                "Descubra as origens dos Assassinos.",
                "RPG / Ação",
                "PlayStation / Xbox / PC",
                "2017",
                "582160"
            },

            {
                "Assassin's Creed Odyssey",
                "Explore a Grécia Antiga.",
                "RPG / Ação",
                "PlayStation / Xbox / PC",
                "2018",
                "812140"
            },

            {
                "Assassin's Creed Valhalla",
                "Viva como um guerreiro viking.",
                "RPG / Ação",
                "PlayStation / Xbox / PC",
                "2020",
                "2208920"
            },

            {
                "Far Cry 3",
                "Sobreviva em uma ilha perigosa.",
                "Ação / Tiro",
                "PlayStation / Xbox / PC",
                "2012",
                "220240"
            },

            {
                "Far Cry 4",
                "Enfrente um regime em uma região montanhosa.",
                "Ação / Tiro",
                "PlayStation / Xbox / PC",
                "2014",
                "298110"
            },

            {
                "Far Cry 5",
                "Enfrente um grupo extremista em Montana.",
                "Ação / Tiro",
                "PlayStation / Xbox / PC",
                "2018",
                "552520"
            },

            {
                "Dying Light",
                "Sobreviva em uma cidade infestada.",
                "Ação / Terror",
                "PlayStation / Xbox / PC",
                "2015",
                "239140"
            },

            {
                "Dying Light 2 Stay Human",
                "Explore uma cidade pós-apocalíptica.",
                "Ação / RPG",
                "PlayStation / Xbox / PC",
                "2022",
                "534380"
            },

            {
                "Life is Strange",
                "Uma estudante descobre poderes especiais.",
                "Aventura",
                "PlayStation / Xbox / PC / Nintendo",
                "2015",
                "319630"
            },

            {
                "Life is Strange: True Colors",
                "Alex Chen descobre sua habilidade.",
                "Aventura",
                "PlayStation / Xbox / PC / Nintendo",
                "2021",
                "936790"
            },

            {
                "Stray",
                "Um gato explora uma cidade futurista.",
                "Aventura",
                "PlayStation / Xbox / PC / Nintendo",
                "2022",
                "1332010"
            },

            {
                "It Takes Two",
                "Dois jogadores precisam cooperar.",
                "Aventura / Cooperativo",
                "PlayStation / Xbox / PC / Nintendo",
                "2021",
                "1426210"
            },

            {
                "A Way Out",
                "Dois presos precisam escapar.",
                "Ação / Aventura",
                "PlayStation / Xbox / PC",
                "2018",
                "1222700"
            },

            {
                "Unravel Two",
                "Uma aventura cooperativa.",
                "Plataforma / Aventura",
                "PlayStation / Xbox / PC / Nintendo",
                "2018",
                "1225560"
            },

            {
                "Hogwarts Legacy",
                "Explore o mundo mágico de Hogwarts.",
                "RPG / Aventura",
                "PlayStation / Xbox / PC / Nintendo",
                "2023",
                "990080"
            },

            {
                "Star Wars Jedi: Fallen Order",
                "Um Jedi tenta sobreviver após a queda da Ordem.",
                "Ação / Aventura",
                "PlayStation / Xbox / PC",
                "2019",
                "1172380"
            },

            {
                "Star Wars Jedi: Survivor",
                "Cal Kestis continua sua jornada.",
                "Ação / Aventura",
                "PlayStation / Xbox / PC",
                "2023",
                "1774580"
            },

            {
                "Marvel's Guardians of the Galaxy",
                "Os Guardiões vivem uma aventura espacial.",
                "Ação / Aventura",
                "PlayStation / Xbox / PC",
                "2021",
                "1088850"
            },

            {
                "Batman: Arkham Asylum GOTY",
                "Batman enfrenta seus inimigos no Arkham.",
                "Ação / Aventura",
                "PlayStation / Xbox / PC",
                "2009",
                "35140"
            },

            {
                "Batman: Arkham City GOTY",
                "Batman enfrenta uma cidade-prisão.",
                "Ação / Aventura",
                "PlayStation / Xbox / PC",
                "2011",
                "200260"
            },

            {
                "Batman: Arkham Knight",
                "Batman enfrenta o misterioso Cavaleiro.",
                "Ação / Aventura",
                "PlayStation / Xbox / PC",
                "2015",
                "208650"
            },

            {
                "DOOM",
                "Enfrente ameaças em uma estação espacial.",
                "Ação / Tiro",
                "PlayStation / Xbox / PC / Nintendo",
                "2016",
                "379720"
            },

            {
                "DOOM Eternal",
                "O Slayer retorna para outra batalha.",
                "Ação / Tiro",
                "PlayStation / Xbox / PC / Nintendo",
                "2020",
                "782330"
            },

            {
                "Mortal Kombat 11",
                "Combates em um torneio intenso.",
                "Luta",
                "PlayStation / Xbox / PC / Nintendo",
                "2019",
                "976310"
            },

            {
                "Street Fighter 6",
                "Uma nova geração de lutas.",
                "Luta",
                "PlayStation / Xbox / PC",
                "2023",
                "1364780"
            },

            {
                "TEKKEN 8",
                "A nova geração da franquia de luta.",
                "Luta",
                "PlayStation / Xbox / PC",
                "2024",
                "1778820"
            },

            {
                "EA SPORTS FC 24",
                "Monte seu time e dispute partidas.",
                "Esporte",
                "PlayStation / Xbox / PC / Nintendo",
                "2023",
                "2195250"
            },

            {
                "Rocket League",
                "Futebol com carros.",
                "Esporte / Multiplayer",
                "PlayStation / Xbox / PC / Nintendo",
                "2015",
                "252950"
            },

            {
                "Apex Legends",
                "Heróis competem em batalhas.",
                "Battle Royale / Ação",
                "PlayStation / Xbox / PC / Nintendo",
                "2019",
                "1172470"
            },

            {
                "Call of Duty: Modern Warfare II",
                "Uma campanha militar intensa.",
                "Ação / Tiro",
                "PlayStation / Xbox / PC",
                "2022",
                "1938090"
            },

            {
                "Call of Duty: Black Ops",
                "Uma campanha ambientada na Guerra Fria.",
                "Ação / Tiro",
                "PlayStation / Xbox / PC",
                "2010",
                "42700"
            },

            {
                "Battlefield 1",
                "Participe de batalhas históricas.",
                "Ação / Tiro",
                "PlayStation / Xbox / PC",
                "2016",
                "1238840"
            },

            {
                "Overwatch 2",
                "Heróis competem em partidas por equipes.",
                "Ação / Multiplayer",
                "PlayStation / Xbox / PC / Nintendo",
                "2022",
                "2357570"
            },

            {
                "Counter-Strike 2",
                "Partidas competitivas entre equipes.",
                "Ação / Estratégia",
                "PC",
                "2023",
                "730"
            },

            {
                "Among Us",
                "Descubra quem está sabotando a tripulação.",
                "Multiplayer / Dedução",
                "PC / Android / iOS / Nintendo",
                "2018",
                "945360"
            },

            {
                "Terraria",
                "Explore e construa em um mundo 2D.",
                "Sandbox / Aventura",
                "PC / PlayStation / Xbox / Nintendo",
                "2011",
                "105600"
            },

            {
                "Stardew Valley",
                "Construa uma nova vida em uma fazenda.",
                "Simulação / RPG",
                "PC / PlayStation / Xbox / Nintendo",
                "2016",
                "413150"
            },

            {
                "The Sims 4",
                "Crie personagens e controle suas vidas.",
                "Simulação",
                "PC / PlayStation / Xbox",
                "2014",
                "1222670"
            },

            {
                "Cities: Skylines",
                "Construa e administre uma cidade.",
                "Simulação / Estratégia",
                "PC / PlayStation / Xbox / Nintendo",
                "2015",
                "255710"
            },

            {
                "Subnautica",
                "Explore um planeta oceânico alienígena.",
                "Sobrevivência / Aventura",
                "PlayStation / Xbox / PC / Nintendo",
                "2018",
                "264710"
            },

            {
                "No Man's Sky",
                "Explore um universo enorme.",
                "Aventura / Sobrevivência",
                "PlayStation / Xbox / PC / Nintendo",
                "2016",
                "275850"
            },

            {
                "Sea of Thieves",
                "Viva aventuras piratas.",
                "Ação / Aventura / Multiplayer",
                "Xbox / PC / PlayStation",
                "2018",
                "1172620"
            },

            {
                "Palworld",
                "Explore um mundo aberto cheio de criaturas.",
                "Sobrevivência / Ação",
                "Xbox / PC",
                "2024",
                "1623730"
            },

            {
                "Sonic Frontiers",
                "Sonic explora ilhas abertas.",
                "Plataforma / Ação",
                "PlayStation / Xbox / PC / Nintendo",
                "2022",
                "1237320"
            },

            {
                "Cuphead",
                "Uma aventura inspirada em desenhos clássicos.",
                "Plataforma / Ação",
                "PlayStation / Xbox / PC / Nintendo",
                "2017",
                "268910"
            },

            {
                "Hades",
                "Tente escapar do submundo.",
                "Roguelike / Ação",
                "PlayStation / Xbox / PC / Nintendo",
                "2020",
                "1145360"
            },

            {
                "Hollow Knight",
                "Explore um reino subterrâneo.",
                "Metroidvania / Ação",
                "PlayStation / Xbox / PC / Nintendo",
                "2017",
                "367520"
            }

        };

        // =========================================================
        // INSERIR JOGOS
        // =========================================================

        String verificarSQL =
                "SELECT id FROM jogo WHERE titulo = ?";

        String inserirSQL =
                "INSERT INTO jogo "
                + "(titulo, descricao, genero, plataforma, "
                + "ano_lancamento, capa) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement verificar =
                stmt.getConnection()
                        .prepareStatement(
                                verificarSQL
                        );

        PreparedStatement inserir =
                stmt.getConnection()
                        .prepareStatement(
                                inserirSQL
                        );

        for (String[] jogo : jogos) {

            String titulo =
                    jogo[0];

            // ----------------------------------------------
            // VERIFICAR DUPLICADO
            // ----------------------------------------------

            verificar.setString(
                    1,
                    titulo
            );

            ResultSet resultado =
                    verificar.executeQuery();

            boolean existe =
                    resultado.next();

            resultado.close();

            if (!existe) {

                // ------------------------------------------
                // MONTAR URL DA CAPA
                // ------------------------------------------

                String capa;

                if (jogo[5].equals("Minecraft")) {

                    capa =
                            "https://cdn.cloudflare.steamstatic.com/steam/apps/322330/library_600x900_2x.jpg";

                } else {

                    capa =
                            "https://cdn.cloudflare.steamstatic.com/steam/apps/"
                            + jogo[5]
                            + "/library_600x900_2x.jpg";
                }

                // ------------------------------------------
                // INSERIR
                // ------------------------------------------

                inserir.setString(
                        1,
                        jogo[0]
                );

                inserir.setString(
                        2,
                        jogo[1]
                );

                inserir.setString(
                        3,
                        jogo[2]
                );

                inserir.setString(
                        4,
                        jogo[3]
                );

                inserir.setInt(
                        5,
                        Integer.parseInt(
                                jogo[4]
                        )
                );

                inserir.setString(
                        6,
                        capa
                );

                inserir.executeUpdate();

                System.out.println(
                        "Jogo adicionado: "
                        + titulo
                );
            }
        }

        verificar.close();
        inserir.close();
    }
}