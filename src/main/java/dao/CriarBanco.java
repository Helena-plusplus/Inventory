package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class CriarBanco {

    public static void criarTabela() {

        Connection conexao = null;
        Statement stmt = null;

        try {

            conexao = Conexao.conectar();

            if (conexao == null) {

                System.out.println(
                        "ERRO: não foi possível conectar ao banco."
                );

                return;
            }

            stmt = conexao.createStatement();

            // =====================================================
            // USUARIO
            // =====================================================

            stmt.execute(
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
                    + ")"
            );

            // =====================================================
            // USERNAME
            // =====================================================

            boolean usernameExiste = false;

            ResultSet colunasUsuario =
                    stmt.executeQuery(
                            "PRAGMA table_info(usuario)"
                    );

            while (colunasUsuario.next()) {

                if ("username".equalsIgnoreCase(
                        colunasUsuario.getString("name")
                )) {

                    usernameExiste = true;
                    break;
                }
            }

            colunasUsuario.close();

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
            // JOGO
            // =====================================================

            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS jogo ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "titulo TEXT NOT NULL,"
                    + "descricao TEXT,"
                    + "genero TEXT,"
                    + "plataforma TEXT,"
                    + "ano_lancamento INTEGER,"
                    + "capa TEXT"
                    + ")"
            );

            // =====================================================
            // SEGUIDOR
            // =====================================================

            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS seguidor ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "id_seguidor INTEGER NOT NULL,"
                    + "id_seguido INTEGER NOT NULL,"
                    + "data_seguida TEXT DEFAULT CURRENT_TIMESTAMP,"
                    + "UNIQUE(id_seguidor, id_seguido),"
                    + "FOREIGN KEY(id_seguidor) REFERENCES usuario(id),"
                    + "FOREIGN KEY(id_seguido) REFERENCES usuario(id)"
                    + ")"
            );

            // =====================================================
            // BIBLIOTECA
            // =====================================================

            stmt.execute(
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
                    + ")"
            );

            adicionarColunaSeNaoExistir(
                    conexao,
                    "biblioteca",
                    "horas_jogadas",
                    "REAL DEFAULT 0"
            );

            // =====================================================
            // AVALIACAO
            // =====================================================

            stmt.execute(
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
                    + ")"
            );

            adicionarColunaSeNaoExistir(
                    conexao,
                    "avaliacao",
                    "horas_jogadas",
                    "REAL DEFAULT 0"
            );

            // =====================================================
            // ADICIONAR NOVOS JOGOS
            // =====================================================

            adicionarNovosJogos(conexao);

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "BANCO DO INVENTORY ATUALIZADO!"
            );

            System.out.println(
                    "========================================"
            );

        } catch (Exception e) {

            System.out.println(
                    "ERRO AO ATUALIZAR O BANCO:"
            );

            e.printStackTrace();

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

    // =========================================================
    // GARANTIR COLUNA
    // =========================================================

    private static void adicionarColunaSeNaoExistir(
            Connection conexao,
            String tabela,
            String coluna,
            String tipo)
            throws Exception {

        Statement stmt =
                conexao.createStatement();

        ResultSet rs =
                stmt.executeQuery(
                        "PRAGMA table_info("
                        + tabela
                        + ")"
                );

        boolean existe = false;

        while (rs.next()) {

            if (coluna.equalsIgnoreCase(
                    rs.getString("name")
            )) {

                existe = true;
                break;
            }
        }

        rs.close();

        if (!existe) {

            stmt.execute(
                    "ALTER TABLE "
                    + tabela
                    + " ADD COLUMN "
                    + coluna
                    + " "
                    + tipo
            );
        }

        stmt.close();
    }

    // =========================================================
    // NOVOS JOGOS
    // =========================================================

    private static void adicionarNovosJogos(
            Connection conexao)
            throws Exception {

        /*
         * [0] Titulo
         * [1] Descricao
         * [2] Genero
         * [3] Plataforma
         * [4] Ano
         * [5] Steam App ID
         */

        String[][] jogos = {

            // =================================================
            // 1
            // =================================================

            {
                "Portal",
                "Resolva enigmas usando portais.",
                "Puzzle / Aventura",
                "PC",
                "2007",
                "400"
            },

            {
                "Half-Life",
                "O início da jornada de Gordon Freeman.",
                "Tiro / Ação",
                "PC",
                "1998",
                "70"
            },

            {
                "Half-Life: Alyx",
                "Uma aventura no universo de Half-Life.",
                "Tiro / Ação",
                "PC",
                "2020",
                "546560"
            },

            {
                "Team Fortress 2",
                "Batalhas entre equipes com diferentes classes.",
                "Tiro / Multiplayer",
                "PC",
                "2007",
                "440"
            },

            {
                "Garry's Mod",
                "Crie suas próprias experiências.",
                "Sandbox",
                "PC",
                "2006",
                "4000"
            },

            {
                "A Plague Tale: Innocence",
                "Uma jornada de sobrevivência na França medieval.",
                "Aventura / Ação",
                "PC / PlayStation / Xbox",
                "2019",
                "752590"
            },

            {
                "A Plague Tale: Requiem",
                "Amicia e Hugo continuam sua jornada.",
                "Aventura / Ação",
                "PC / PlayStation / Xbox",
                "2022",
                "1182900"
            },

            {
                "Metro 2033 Redux",
                "Sobreviva nos túneis de Moscou.",
                "Tiro / Terror",
                "PC / PlayStation / Xbox",
                "2014",
                "286690"
            },

            {
                "Metro: Last Light Redux",
                "Continue a luta pela sobrevivência.",
                "Tiro / Ação",
                "PC / PlayStation / Xbox",
                "2014",
                "287390"
            },

            {
                "Metro Exodus",
                "Atravesse uma Rússia devastada.",
                "Tiro / Ação",
                "PC / PlayStation / Xbox",
                "2019",
                "412020"
            },

            {
                "BioShock Remastered",
                "Explore a cidade submarina de Rapture.",
                "Tiro / Aventura",
                "PC",
                "2016",
                "409710"
            },

            {
                "BioShock 2 Remastered",
                "Retorne à cidade de Rapture.",
                "Tiro / Aventura",
                "PC",
                "2016",
                "409720"
            },

            {
                "BioShock Infinite",
                "Explore a cidade flutuante de Columbia.",
                "Tiro / Aventura",
                "PC / PlayStation / Xbox",
                "2013",
                "8870"
            },

            {
                "Borderlands 2",
                "Atire, saqueie e explore Pandora.",
                "Tiro / RPG",
                "PC / PlayStation / Xbox",
                "2012",
                "49520"
            },

            {
                "Borderlands 3",
                "Uma nova aventura de loot e ação.",
                "Tiro / RPG",
                "PC / PlayStation / Xbox",
                "2019",
                "397540"
            },

            {
                "Tiny Tina's Wonderlands",
                "Uma aventura de fantasia cheia de humor.",
                "Tiro / RPG",
                "PC / PlayStation / Xbox",
                "2022",
                "1286680"
            },

            {
                "DOOM 3",
                "Enfrente criaturas em Marte.",
                "Tiro / Terror",
                "PC",
                "2004",
                "9050"
            },

            {
                "Quake",
                "Um clássico dos jogos de tiro.",
                "Tiro / Ação",
                "PC",
                "1996",
                "2310"
            },

            {
                "Quake II",
                "Combates contra forças alienígenas.",
                "Tiro / Ação",
                "PC",
                "1997",
                "2320"
            },

            {
                "Wolfenstein: The New Order",
                "Lute contra um regime em uma realidade alternativa.",
                "Tiro / Ação",
                "PC / PlayStation / Xbox",
                "2014",
                "201810"
            },

            {
                "Wolfenstein II: The New Colossus",
                "Continue a resistência.",
                "Tiro / Ação",
                "PC / PlayStation / Xbox",
                "2017",
                "612880"
            },

            {
                "Dishonored 2",
                "Use poderes e furtividade em Karnaca.",
                "Ação / Furtividade",
                "PC / PlayStation / Xbox",
                "2016",
                "403640"
            },

            {
                "Death of the Outsider",
                "Uma missão no universo Dishonored.",
                "Ação / Furtividade",
                "PC / PlayStation / Xbox",
                "2017",
                "614570"
            },

            {
                "The Evil Within",
                "Um detetive enfrenta acontecimentos sobrenaturais.",
                "Terror / Ação",
                "PC / PlayStation / Xbox",
                "2014",
                "268050"
            },

            {
                "The Evil Within 2",
                "Sebastian retorna para resgatar sua filha.",
                "Terror / Ação",
                "PC / PlayStation / Xbox",
                "2017",
                "601430"
            },

            // =================================================
            // 26
            // =================================================

            {
                "The Wolf Among Us",
                "Uma investigação em uma cidade fantástica.",
                "Aventura",
                "PC / PlayStation / Xbox",
                "2013",
                "250320"
            },

            {
                "Tales from the Borderlands",
                "Uma aventura narrativa em Pandora.",
                "Aventura",
                "PC / PlayStation / Xbox",
                "2014",
                "330830"
            },

            {
                "Batman: The Telltale Series",
                "Uma história interativa do Batman.",
                "Aventura",
                "PC / PlayStation / Xbox",
                "2016",
                "498240"
            },

            {
                "The Walking Dead: Season Two",
                "Continuação da aventura de Clementine.",
                "Aventura",
                "PC / PlayStation / Xbox",
                "2013",
                "261030"
            },

            {
                "The Walking Dead: A New Frontier",
                "Uma nova história de The Walking Dead.",
                "Aventura",
                "PC / PlayStation / Xbox",
                "2016",
                "536220"
            },

            {
                "The Walking Dead: The Final Season",
                "A última jornada de Clementine.",
                "Aventura",
                "PC / PlayStation / Xbox",
                "2018",
                "866800"
            },

            {
                "Sherlock Holmes Chapter One",
                "Investigue mistérios como Sherlock Holmes.",
                "Aventura / Investigação",
                "PC / PlayStation / Xbox",
                "2021",
                "1137300"
            },

            {
                "Sherlock Holmes: Crimes and Punishments",
                "Resolva vários casos.",
                "Investigação / Aventura",
                "PC",
                "2014",
                "241260"
            },

            {
                "L.A. Noire",
                "Investigue crimes em Los Angeles.",
                "Ação / Investigação",
                "PC / PlayStation / Xbox",
                "2011",
                "110800"
            },

            {
                "Sleeping Dogs: Definitive Edition",
                "Um policial infiltrado em Hong Kong.",
                "Ação / Mundo Aberto",
                "PC / PlayStation / Xbox",
                "2014",
                "307690"
            },

            {
                "Just Cause 3",
                "Cause caos em uma enorme ilha.",
                "Ação / Mundo Aberto",
                "PC / PlayStation / Xbox",
                "2015",
                "225540"
            },

            {
                "Just Cause 4",
                "Enfrente uma organização militar.",
                "Ação / Mundo Aberto",
                "PC / PlayStation / Xbox",
                "2018",
                "517630"
            },

            {
                "Mad Max",
                "Sobreviva em um deserto pós-apocalíptico.",
                "Ação / Mundo Aberto",
                "PC / PlayStation / Xbox",
                "2015",
                "234140"
            },

            {
                "Watch Dogs",
                "Use tecnologia para controlar Chicago.",
                "Ação / Mundo Aberto",
                "PC / PlayStation / Xbox",
                "2014",
                "243470"
            },

            {
                "Watch Dogs 2",
                "Hackeie São Francisco.",
                "Ação / Mundo Aberto",
                "PC / PlayStation / Xbox",
                "2016",
                "447040"
            },

            {
                "Watch Dogs: Legion",
                "Monte uma resistência em Londres.",
                "Ação / Mundo Aberto",
                "PC / PlayStation / Xbox",
                "2020",
                "2231380"
            },

            {
                "Saints Row: The Third",
                "Uma gangue domina Steelport.",
                "Ação / Mundo Aberto",
                "PC / PlayStation / Xbox",
                "2011",
                "55230"
            },

            {
                "Saints Row IV",
                "O presidente enfrenta alienígenas.",
                "Ação / Mundo Aberto",
                "PC / PlayStation / Xbox",
                "2013",
                "206420"
            },

            {
                "Saints Row",
                "Recomece a história dos Saints.",
                "Ação / Mundo Aberto",
                "PC / PlayStation / Xbox",
                "2022",
                "742420"
            },

            {
                "Prototype",
                "Use poderes especiais em Manhattan.",
                "Ação",
                "PC",
                "2009",
                "10150"
            },

            {
                "Prototype 2",
                "Uma nova guerra biológica começa.",
                "Ação",
                "PC",
                "2012",
                "115320"
            },

            {
                "Middle-earth: Shadow of Mordor",
                "Explore Mordor e enfrente seus inimigos.",
                "Ação / RPG",
                "PC / PlayStation / Xbox",
                "2014",
                "241930"
            },

            {
                "Middle-earth: Shadow of War",
                "Conquiste fortalezas em Mordor.",
                "Ação / RPG",
                "PC / PlayStation / Xbox",
                "2017",
                "356190"
            },

            // =================================================
            // 50
            // =================================================

            {
                "Ryse: Son of Rome",
                "Lute como um soldado romano.",
                "Ação",
                "PC / Xbox",
                "2013",
                "302510"
            },

            {
                "Mirror's Edge",
                "Corra pelos telhados de uma cidade futurista.",
                "Ação / Plataforma",
                "PC",
                "2008",
                "17410"
            },

            {
                "Mirror's Edge Catalyst",
                "Faith retorna em uma nova aventura.",
                "Ação / Plataforma",
                "PC / PlayStation / Xbox",
                "2016",
                "1233570"
            },

            {
                "Titanfall 2",
                "Piloto e titã lutam juntos.",
                "Tiro / Ação",
                "PC / PlayStation / Xbox",
                "2016",
                "1237970"
            },

            {
                "Star Wars Battlefront II",
                "Grandes batalhas de Star Wars.",
                "Tiro / Multiplayer",
                "PC / PlayStation / Xbox",
                "2017",
                "1237950"
            },

            {
                "Jedi Knight: Jedi Academy",
                "Treine como Jedi.",
                "Ação",
                "PC",
                "2003",
                "6020"
            },

            {
                "Star Wars: Knights of the Old Republic",
                "Um RPG clássico de Star Wars.",
                "RPG",
                "PC",
                "2003",
                "32370"
            },

            {
                "Star Wars: Knights of the Old Republic II",
                "Uma nova aventura RPG de Star Wars.",
                "RPG",
                "PC",
                "2004",
                "208580"
            },

            {
                "The Outer Worlds",
                "Explore uma colônia espacial.",
                "RPG / Ação",
                "PC / PlayStation / Xbox",
                "2019",
                "578650"
            },

            {
                "Wasteland 3",
                "RPG tático em um mundo congelado.",
                "RPG / Estratégia",
                "PC / PlayStation / Xbox",
                "2020",
                "719040"
            },

            {
                "Wasteland 2: Director's Cut",
                "RPG pós-apocalíptico.",
                "RPG",
                "PC / PlayStation / Xbox",
                "2014",
                "404330"
            },

            {
                "Bastion",
                "RPG de ação narrado.",
                "RPG / Ação",
                "PC / Nintendo",
                "2011",
                "107100"
            },

            {
                "Transistor",
                "Uma aventura futurista.",
                "RPG / Ação",
                "PC",
                "2014",
                "237930"
            },

            {
                "Pyre",
                "Um grupo busca sua liberdade.",
                "RPG",
                "PC",
                "2017",
                "462770"
            },

            {
                "The Banner Saga",
                "Uma aventura estratégica viking.",
                "RPG / Estratégia",
                "PC / PlayStation / Xbox / Nintendo",
                "2014",
                "237990"
            },

            {
                "The Banner Saga 2",
                "Continue a jornada dos vikings.",
                "RPG / Estratégia",
                "PC / PlayStation / Xbox / Nintendo",
                "2016",
                "281640"
            },

            {
                "The Banner Saga 3",
                "Conclusão da trilogia.",
                "RPG / Estratégia",
                "PC / PlayStation / Xbox / Nintendo",
                "2018",
                "485460"
            },

            {
                "South Park: The Stick of Truth",
                "Uma aventura RPG em South Park.",
                "RPG / Aventura",
                "PC / PlayStation / Xbox",
                "2014",
                "213670"
            },

            {
                "South Park: The Fractured but Whole",
                "Uma nova aventura RPG.",
                "RPG",
                "PC / PlayStation / Xbox",
                "2017",
                "488790"
            },

            {
                "Dragon Age: Origins",
                "Um RPG clássico de fantasia.",
                "RPG",
                "PC",
                "2009",
                "47810"
            },

            {
                "Dragon Age II",
                "A história de Hawke.",
                "RPG",
                "PC",
                "2011",
                "12370"
            },

            {
                "Mass Effect",
                "Comande a Normandy.",
                "RPG / Ação",
                "PC",
                "2007",
                "17460"
            },

            {
                "Mass Effect 2",
                "Monte sua equipe.",
                "RPG / Ação",
                "PC",
                "2010",
                "24980"
            },

            {
                "Mass Effect 3",
                "A galáxia enfrenta uma grande ameaça.",
                "RPG / Ação",
                "PC",
                "2012",
                "12370"
            },

            {
                "The Witcher 2: Assassins of Kings",
                "Geralt enfrenta intrigas políticas.",
                "RPG",
                "PC",
                "2011",
                "20920"
            },

            // =================================================
            // 76
            // =================================================

            {
                "Dragon's Dogma: Dark Arisen",
                "RPG de fantasia e combate.",
                "RPG / Ação",
                "PC",
                "2016",
                "367500"
            },

            {
                "Kingdoms of Amalur: Re-Reckoning",
                "Uma grande aventura de fantasia.",
                "RPG / Ação",
                "PC / PlayStation / Xbox",
                "2020",
                "1041720"
            },

            {
                "GreedFall",
                "Explore uma ilha cheia de magia.",
                "RPG",
                "PC / PlayStation / Xbox",
                "2019",
                "606880"
            },

            {
                "Vampyr",
                "Um médico enfrenta uma nova vida.",
                "RPG / Ação",
                "PC / PlayStation / Xbox",
                "2018",
                "427290"
            },

            {
                "Elex",
                "Explore o mundo de Magalan.",
                "RPG",
                "PC / PlayStation / Xbox",
                "2017",
                "411300"
            },

            {
                "Elex II",
                "Retorne ao mundo de Magalan.",
                "RPG",
                "PC / PlayStation / Xbox",
                "2022",
                "900040"
            },

            {
                "The Technomancer",
                "RPG de ficção científica em Marte.",
                "RPG / Ação",
                "PC",
                "2016",
                "338390"
            },

            {
                "Torment: Tides of Numenera",
                "Um RPG narrativo futurista.",
                "RPG",
                "PC",
                "2017",
                "272270"
            },

            {
                "Pillars of Eternity",
                "RPG clássico de fantasia.",
                "RPG",
                "PC",
                "2015",
                "291650"
            },

            {
                "Tyranny",
                "O mal já venceu.",
                "RPG",
                "PC",
                "2016",
                "362960"
            },

            {
                "Pathfinder: Wrath of the Righteous",
                "Uma grande aventura de RPG.",
                "RPG",
                "PC / PlayStation / Xbox",
                "2021",
                "1184370"
            },

            {
                "Pillars of Eternity II: Deadfire",
                "Navegue por um mundo de fantasia.",
                "RPG",
                "PC",
                "2018",
                "560130"
            },

            {
                "Darkest Dungeon",
                "Envie heróis para masmorras perigosas.",
                "RPG / Estratégia",
                "PC / PlayStation / Xbox / Nintendo",
                "2016",
                "262060"
            },

            {
                "Darkest Dungeon II",
                "Uma nova jornada sombria.",
                "RPG / Estratégia",
                "PC",
                "2023",
                "1940340"
            },

            {
                "XCOM: Enemy Unknown",
                "Comande a defesa da humanidade.",
                "Estratégia",
                "PC / PlayStation / Xbox",
                "2012",
                "200510"
            },

            {
                "XCOM: Enemy Within",
                "Uma expansão de XCOM.",
                "Estratégia",
                "PC",
                "2013",
                "225340"
            },

            {
                "Civilization V",
                "Construa uma civilização.",
                "Estratégia",
                "PC",
                "2010",
                "8930"
            },

            {
                "Age of Empires III: Definitive Edition",
                "Construa impérios.",
                "Estratégia",
                "PC",
                "2020",
                "933110"
            },

            {
                "Age of Mythology: Extended Edition",
                "Estratégia baseada em mitologias.",
                "Estratégia",
                "PC",
                "2014",
                "266840"
            },

            {
                "Command & Conquer Remastered Collection",
                "Dois clássicos da estratégia.",
                "Estratégia",
                "PC",
                "2020",
                "1213210"
            },

            {
                "Company of Heroes 2",
                "Estratégia durante a Segunda Guerra.",
                "Estratégia",
                "PC",
                "2013",
                "231430"
            },

            {
                "Company of Heroes 3",
                "Novas campanhas históricas.",
                "Estratégia",
                "PC",
                "2023",
                "1677280"
            },

            {
                "Frostpunk",
                "Administre uma cidade congelada.",
                "Estratégia / Simulação",
                "PC / PlayStation / Xbox",
                "2018",
                "323190"
            },

            {
                "Frostpunk 2",
                "Administre uma sociedade congelada.",
                "Estratégia / Simulação",
                "PC / PlayStation / Xbox",
                "2024",
                "1601580"
            },

            // =================================================
            // 101
            // =================================================

            {
                "This War of Mine",
                "Sobreviva como civil durante uma guerra.",
                "Sobrevivência / Estratégia",
                "PC / PlayStation / Xbox / Nintendo",
                "2014",
                "282070"
            },

            {
                "They Are Billions",
                "Defenda sua colônia de hordas.",
                "Estratégia / Sobrevivência",
                "PC / PlayStation / Xbox",
                "2019",
                "644930"
            },

            {
                "Northgard",
                "Conquiste territórios em um mundo viking.",
                "Estratégia",
                "PC / PlayStation / Xbox / Nintendo",
                "2018",
                "466560"
            },

            {
                "Into the Breach",
                "Defenda cidades de criaturas gigantes.",
                "Estratégia",
                "PC / Nintendo",
                "2018",
                "590380"
            },

            {
                "FTL: Faster Than Light",
                "Comande uma nave em uma galáxia perigosa.",
                "Estratégia / Roguelike",
                "PC",
                "2012",
                "212680"
            },

            {
                "The Binding of Isaac: Rebirth",
                "Explore masmorras cheias de perigos.",
                "Roguelike / Ação",
                "PC / PlayStation / Xbox / Nintendo",
                "2014",
                "250900"
            },

            {
                "Nuclear Throne",
                "Tiro roguelike em um mundo pós-apocalíptico.",
                "Roguelike / Ação",
                "PC",
                "2015",
                "242680"
            },

            {
                "The Messenger",
                "Uma aventura de plataforma retrô.",
                "Plataforma",
                "PC / Nintendo",
                "2018",
                "764790"
            },

            {
                "Shovel Knight: Treasure Trove",
                "Aventura de plataforma clássica.",
                "Plataforma",
                "PC / PlayStation / Xbox / Nintendo",
                "2014",
                "250760"
            },

            {
                "A Hat in Time",
                "Uma garota explora mundos coloridos.",
                "Plataforma / Aventura",
                "PC / PlayStation / Xbox / Nintendo",
                "2017",
                "253230"
            },

            {
                "Psychonauts",
                "Uma aventura psicodélica.",
                "Plataforma / Aventura",
                "PC",
                "2005",
                "3830"
            },

            {
                "Rayman Legends",
                "Uma aventura de plataforma.",
                "Plataforma",
                "PC / PlayStation / Xbox / Nintendo",
                "2013",
                "242550"
            },

            {
                "New Super Lucky's Tale",
                "Uma aventura de plataforma.",
                "Plataforma",
                "PC / Nintendo",
                "2019",
                "855740"
            },

            {
                "Brothers: A Tale of Two Sons",
                "Dois irmãos partem em uma jornada.",
                "Aventura",
                "PC / PlayStation / Xbox / Nintendo",
                "2013",
                "225080"
            },

            {
                "Journey",
                "Uma jornada pelo deserto.",
                "Aventura",
                "PC / PlayStation",
                "2019",
                "638230"
            },

            {
                "ABZU",
                "Explore as profundezas do oceano.",
                "Aventura",
                "PC / PlayStation / Xbox / Nintendo",
                "2016",
                "384190"
            },

            {
                "Sable",
                "Explore um planeta cheio de ruínas.",
                "Aventura",
                "PC / Xbox / PlayStation",
                "2021",
                "757310"
            },

            {
                "Eastshade",
                "Explore uma ilha e pinte paisagens.",
                "Aventura",
                "PC / PlayStation / Xbox",
                "2019",
                "967050"
            },

            {
                "The Forgotten City",
                "Investigue um mistério em uma cidade antiga.",
                "Aventura / Investigação",
                "PC / PlayStation / Xbox / Nintendo",
                "2021",
                "874260"
            },

            {
                "Return of the Obra Dinn",
                "Descubra o destino da tripulação.",
                "Investigação / Puzzle",
                "PC / Nintendo",
                "2018",
                "653530"
            },

            // =================================================
            // 121
            // =================================================

            {
                "The Case of the Golden Idol",
                "Resolva crimes por dedução.",
                "Investigação / Puzzle",
                "PC / Nintendo",
                "2022",
                "1375240"
            },

            {
                "Her Story",
                "Investigue uma história através de vídeos.",
                "Investigação",
                "PC",
                "2015",
                "368370"
            },

            {
                "Telling Lies",
                "Investigue uma série de gravações.",
                "Investigação",
                "PC / PlayStation / Xbox / Nintendo",
                "2019",
                "762830"
            },

            {
                "Immortality",
                "Descubra o mistério de três filmes.",
                "Aventura / Investigação",
                "PC / Xbox",
                "2022",
                "1350200"
            },

            {
                "The Room",
                "Resolva enigmas em uma sala misteriosa.",
                "Puzzle",
                "PC / Mobile",
                "2014",
                "288160"
            },

            {
                "The Room Two",
                "Continue solucionando enigmas.",
                "Puzzle",
                "PC / Mobile",
                "2016",
                "425580"
            },

            {
                "The Room Three",
                "Mais enigmas e mistérios.",
                "Puzzle",
                "PC / Mobile",
                "2018",
                "456270"
            },

            {
                "The Room 4: Old Sins",
                "Um novo mistério.",
                "Puzzle",
                "PC / Mobile",
                "2018",
                "1361320"
            },

            {
                "A Little to the Left",
                "Organize objetos em puzzles.",
                "Puzzle",
                "PC / Nintendo",
                "2022",
                "1629520"
            },

            {
                "Unpacking",
                "Organize uma vida através de objetos.",
                "Puzzle / Simulação",
                "PC / Nintendo",
                "2021",
                "1135690"
            },

            {
                "Dorfromantik",
                "Monte paisagens em peças hexagonais.",
                "Puzzle / Estratégia",
                "PC / Nintendo",
                "2022",
                "1455840"
            },

            {
                "Mini Metro",
                "Planeje linhas de metrô.",
                "Estratégia / Puzzle",
                "PC / Nintendo",
                "2014",
                "287980"
            },

            {
                "Mini Motorways",
                "Construa redes de estradas.",
                "Estratégia / Puzzle",
                "PC / Nintendo",
                "2021",
                "1127500"
            },

            {
                "Human: Fall Flat",
                "Resolva desafios de física.",
                "Puzzle / Cooperativo",
                "PC / PlayStation / Xbox / Nintendo",
                "2016",
                "477160"
            },

            {
                "Gang Beasts",
                "Lutas multiplayer engraçadas.",
                "Multiplayer / Luta",
                "PC / PlayStation / Xbox / Nintendo",
                "2017",
                "285900"
            },

            {
                "Ultimate Chicken Horse",
                "Construa fases e tente vencê-las.",
                "Plataforma / Multiplayer",
                "PC / PlayStation / Xbox / Nintendo",
                "2016",
                "386940"
            },

            {
                "Castle Crashers",
                "Aventura cooperativa de ação.",
                "Beat 'em Up",
                "PC / PlayStation / Xbox / Nintendo",
                "2008",
                "204360"
            },

            {
                "Streets of Rage 4",
                "O clássico beat 'em up retorna.",
                "Beat 'em Up",
                "PC / PlayStation / Xbox / Nintendo",
                "2020",
                "985890"
            },

            {
                "TMNT: Shredder's Revenge",
                "As Tartarugas Ninja voltam à ação.",
                "Beat 'em Up",
                "PC / PlayStation / Xbox / Nintendo",
                "2022",
                "1367590"
            },

            {
                "Brawlhalla",
                "Lutas multiplayer com vários personagens.",
                "Luta / Multiplayer",
                "PC / PlayStation / Xbox / Nintendo",
                "2017",
                "291550"
            },

            // =================================================
            // 141
            // =================================================

            {
                "Guilty Gear -Strive-",
                "Uma nova geração de lutas.",
                "Luta",
                "PC / PlayStation / Xbox",
                "2021",
                "1384160"
            },

            {
                "Dragon Ball FighterZ",
                "Combates 2D de Dragon Ball.",
                "Luta",
                "PC / PlayStation / Xbox / Nintendo",
                "2018",
                "678950"
            },

            {
                "Injustice 2",
                "Heróis e vilões da DC lutam.",
                "Luta",
                "PC / PlayStation / Xbox",
                "2017",
                "627270"
            },

            {
                "Killer Instinct",
                "Luta clássica.",
                "Luta",
                "PC / Xbox",
                "2013",
                "577940"
            },

            {
                "F1 23",
                "Dispute a temporada de Fórmula 1.",
                "Corrida / Esporte",
                "PC / PlayStation / Xbox",
                "2023",
                "2108330"
            },

            {
                "DiRT Rally 2.0",
                "Rali em pistas desafiadoras.",
                "Corrida",
                "PC / PlayStation / Xbox",
                "2019",
                "690790"
            },

            {
                "GRID Legends",
                "Corridas em diferentes categorias.",
                "Corrida",
                "PC / PlayStation / Xbox",
                "2022",
                "1307710"
            },

            {
                "Burnout Paradise Remastered",
                "Corridas e destruição em mundo aberto.",
                "Corrida",
                "PC / PlayStation / Xbox / Nintendo",
                "2018",
                "1238080"
            },

            {
                "Hot Wheels Unleashed",
                "Corridas com carrinhos Hot Wheels.",
                "Corrida",
                "PC / PlayStation / Xbox / Nintendo",
                "2021",
                "1271700"
            },

            {
                "Sonic Mania",
                "Uma aventura clássica do Sonic.",
                "Plataforma",
                "PC / PlayStation / Xbox / Nintendo",
                "2017",
                "584400"
            },

            {
                "Sonic Adventure 2",
                "Uma aventura clássica do Sonic.",
                "Ação / Plataforma",
                "PC",
                "2001",
                "213610"
            },

            {
                "Crash Bandicoot N. Sane Trilogy",
                "Três aventuras clássicas de Crash.",
                "Plataforma",
                "PC / PlayStation / Xbox / Nintendo",
                "2017",
                "731490"
            },

            {
                "Crash Bandicoot 4: It's About Time",
                "Uma nova aventura de Crash.",
                "Plataforma",
                "PC / PlayStation / Xbox / Nintendo",
                "2020",
                "1378990"
            },

            {
                "Spyro Reignited Trilogy",
                "Três aventuras clássicas do Spyro.",
                "Plataforma / Aventura",
                "PC / PlayStation / Xbox / Nintendo",
                "2018",
                "996580"
            },

            {
                "LEGO Star Wars: The Skywalker Saga",
                "Reviva os nove filmes de Star Wars.",
                "Ação / Aventura",
                "PC / PlayStation / Xbox / Nintendo",
                "2022",
                "920210"
            },

            {
                "LEGO Marvel Super Heroes",
                "Heróis da Marvel em LEGO.",
                "Ação / Aventura",
                "PC / PlayStation / Xbox / Nintendo",
                "2013",
                "249130"
            },

            {
                "LEGO City Undercover",
                "Explore uma cidade LEGO.",
                "Ação / Aventura",
                "PC / PlayStation / Xbox / Nintendo",
                "2017",
                "578330"
            },

            {
                "LEGO Batman 3: Beyond Gotham",
                "Batman e heróis da DC.",
                "Ação / Aventura",
                "PC / PlayStation / Xbox / Nintendo",
                "2014",
                "313690"
            },

            {
                "LEGO Jurassic World",
                "Aventura baseada em Jurassic Park.",
                "Ação / Aventura",
                "PC / PlayStation / Xbox / Nintendo",
                "2015",
                "352400"
            },

            // =================================================
            // 161
            // =================================================

            {
                "SnowRunner",
                "Transporte veículos por terrenos difíceis.",
                "Simulação",
                "PC / PlayStation / Xbox / Nintendo",
                "2020",
                "1465360"
            },

            {
                "MudRunner",
                "Dirija veículos pesados por terrenos extremos.",
                "Simulação",
                "PC / PlayStation / Xbox / Nintendo",
                "2017",
                "675010"
            },

            {
                "Car Mechanic Simulator 2021",
                "Monte e conserte veículos.",
                "Simulação",
                "PC / PlayStation / Xbox",
                "2021",
                "1190000"
            },

            {
                "Automation - The Car Company Tycoon Game",
                "Crie sua própria montadora.",
                "Simulação",
                "PC",
                "2015",
                "293760"
            },

            {
                "American Truck Simulator",
                "Dirija caminhões pelos Estados Unidos.",
                "Simulação",
                "PC",
                "2016",
                "270880"
            },

            {
                "Euro Truck Simulator 2",
                "Dirija caminhões pela Europa.",
                "Simulação",
                "PC",
                "2012",
                "227300"
            },

            {
                "House Flipper 2",
                "Reforme e venda casas.",
                "Simulação",
                "PC / PlayStation / Xbox",
                "2023",
                "1190970"
            },

            {
                "PowerWash Simulator",
                "Limpe vários lugares.",
                "Simulação",
                "PC / PlayStation / Xbox / Nintendo",
                "2022",
                "1290000"
            },

            {
                "Planet Zoo",
                "Construa e administre um zoológico.",
                "Simulação",
                "PC",
                "2019",
                "703080"
            },

            {
                "Planet Coaster",
                "Crie seu parque de diversões.",
                "Simulação",
                "PC / PlayStation / Xbox",
                "2016",
                "493340"
            },

            {
                "Two Point Hospital",
                "Administre um hospital.",
                "Simulação",
                "PC / PlayStation / Xbox / Nintendo",
                "2018",
                "535930"
            },

            {
                "Two Point Campus",
                "Administre uma universidade.",
                "Simulação",
                "PC / PlayStation / Xbox / Nintendo",
                "2022",
                "1649080"
            },

            {
                "The Long Dark",
                "Sobreviva ao frio extremo.",
                "Sobrevivência",
                "PC / PlayStation / Xbox / Nintendo",
                "2017",
                "305620"
            },

            {
                "Project Zomboid",
                "Sobreviva durante um apocalipse.",
                "Sobrevivência",
                "PC",
                "2013",
                "108600"
            },

            {
                "7 Days to Die",
                "Sobreviva em um mundo devastado.",
                "Sobrevivência",
                "PC / PlayStation / Xbox",
                "2013",
                "251570"
            },

            {
                "ARK: Survival Evolved",
                "Sobreviva entre dinossauros.",
                "Sobrevivência",
                "PC / PlayStation / Xbox / Nintendo",
                "2017",
                "346110"
            },

            {
                "ARK: Survival Ascended",
                "Uma nova versão de sobrevivência com dinossauros.",
                "Sobrevivência",
                "PC / PlayStation / Xbox",
                "2023",
                "2399830"
            },

            {
                "Rust",
                "Sobreviva e construa sua base.",
                "Sobrevivência",
                "PC / PlayStation / Xbox",
                "2018",
                "252490"
            },

            {
                "DayZ",
                "Sobreviva em um mundo pós-apocalíptico.",
                "Sobrevivência",
                "PC / PlayStation / Xbox",
                "2013",
                "221100"
            },

            // =================================================
            // 180
            // =================================================

            {
                "Raft",
                "Sobreviva em alto-mar.",
                "Sobrevivência",
                "PC",
                "2022",
                "648800"
            },

            {
                "Valheim",
                "Explore um mundo inspirado na mitologia nórdica.",
                "Sobrevivência / RPG",
                "PC / Xbox",
                "2021",
                "892970"
            },

            {
                "V Rising",
                "Torne-se um vampiro e construa seu castelo.",
                "Sobrevivência / RPG",
                "PC / PlayStation",
                "2022",
                "1604030"
            },

            {
                "Grounded",
                "Sobreviva sendo pequeno em um quintal.",
                "Sobrevivência",
                "PC / Xbox / PlayStation / Nintendo",
                "2022",
                "962130"
            },

            {
                "Don't Starve Together",
                "Sobreviva com seus amigos.",
                "Sobrevivência / Multiplayer",
                "PC / PlayStation / Xbox / Nintendo",
                "2016",
                "322330"
            },

            {
                "Green Hell",
                "Sobreviva na floresta.",
                "Sobrevivência",
                "PC / PlayStation / Xbox / Nintendo",
                "2019",
                "815370"
            },

            {
                "Sons of the Forest",
                "Sobreviva em uma ilha misteriosa.",
                "Sobrevivência",
                "PC",
                "2024",
                "1326470"
            },

            {
                "Phasmophobia",
                "Investigue locais assombrados.",
                "Terror / Multiplayer",
                "PC",
                "2020",
                "739630"
            },

            {
                "Devour",
                "Sobreviva a uma experiência sobrenatural.",
                "Terror / Multiplayer",
                "PC",
                "2021",
                "1274570"
            },

            {
                "Lethal Company",
                "Explore luas perigosas com seus amigos.",
                "Terror / Multiplayer",
                "PC",
                "2023",
                "1966720"
            },

            {
                "Content Warning",
                "Grave vídeos em lugares assustadores.",
                "Terror / Multiplayer",
                "PC",
                "2024",
                "2881650"
            },

            {
                "Dead by Daylight",
                "Sobreviva em partidas multiplayer.",
                "Terror / Multiplayer",
                "PC / PlayStation / Xbox / Nintendo",
                "2016",
                "381210"
            },

            {
                "The Forest",
                "Sobreviva em uma floresta misteriosa.",
                "Sobrevivência",
                "PC / PlayStation",
                "2018",
                "242760"
            },

            {
                "Subnautica: Below Zero",
                "Explore uma região congelada do oceano.",
                "Sobrevivência",
                "PC / PlayStation / Xbox / Nintendo",
                "2021",
                "848450"
            },

            {
                "Astroneer",
                "Explore planetas e construa bases.",
                "Aventura / Sobrevivência",
                "PC / Xbox / PlayStation / Nintendo",
                "2019",
                "361420"
            },

            {
                "Starbound",
                "Explore um universo cheio de planetas.",
                "Sandbox / Aventura",
                "PC",
                "2016",
                "211820"
            },

            {
                "Terraria",
                "Explore, construa e lute.",
                "Sandbox",
                "PC / PlayStation / Xbox / Nintendo",
                "2011",
                "105600"
            },

            {
                "Core Keeper",
                "Explore um mundo subterrâneo.",
                "Sandbox / RPG",
                "PC / Nintendo",
                "2024",
                "1621690"
            },

            {
                "Dome Keeper",
                "Defenda sua base contra criaturas.",
                "Roguelike",
                "PC",
                "2022",
                "1631470"
            },

            // =================================================
            // 200
            // =================================================

            {
                "The Stanley Parable: Ultra Deluxe",
                "Uma aventura cheia de escolhas.",
                "Aventura",
                "PC / PlayStation / Xbox / Nintendo",
                "2022",
                "1703340"
            }

        };

        // =====================================================
        // SQL
        // =====================================================

        String verificarSQL =
                "SELECT id FROM jogo WHERE titulo = ?";

        String inserirSQL =
                "INSERT INTO jogo "
                + "(titulo, descricao, genero, plataforma, "
                + "ano_lancamento, capa) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        String atualizarSQL =
                "UPDATE jogo SET "
                + "descricao = ?, "
                + "genero = ?, "
                + "plataforma = ?, "
                + "ano_lancamento = ?, "
                + "capa = ? "
                + "WHERE titulo = ?";

        PreparedStatement verificar =
                conexao.prepareStatement(
                        verificarSQL
                );

        PreparedStatement inserir =
                conexao.prepareStatement(
                        inserirSQL
                );

        PreparedStatement atualizar =
                conexao.prepareStatement(
                        atualizarSQL
                );

        int adicionados = 0;
        int atualizados = 0;

        for (String[] jogo : jogos) {

            String titulo =
                    jogo[0];

            String capa =
                    "https://cdn.akamai.steamstatic.com/"
                    + "steam/apps/"
                    + jogo[5]
                    + "/library_600x900_2x.jpg";

            // =================================================
            // VERIFICAR SE JÁ EXISTE
            // =================================================

            verificar.setString(
                    1,
                    titulo
            );

            ResultSet resultado =
                    verificar.executeQuery();

            boolean existe =
                    resultado.next();

            resultado.close();

            // =================================================
            // ATUALIZAR
            // =================================================

            if (existe) {

                atualizar.setString(
                        1,
                        jogo[1]
                );

                atualizar.setString(
                        2,
                        jogo[2]
                );

                atualizar.setString(
                        3,
                        jogo[3]
                );

                atualizar.setInt(
                        4,
                        Integer.parseInt(jogo[4])
                );

                atualizar.setString(
                        5,
                        capa
                );

                atualizar.setString(
                        6,
                        titulo
                );

                atualizar.executeUpdate();

                atualizados++;

                System.out.println(
                        "Atualizado: "
                        + titulo
                );

            } else {

                // =============================================
                // INSERIR
                // =============================================

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
                        Integer.parseInt(jogo[4])
                );

                inserir.setString(
                        6,
                        capa
                );

                inserir.executeUpdate();

                adicionados++;

                System.out.println(
                        "Adicionado: "
                        + titulo
                );
            }
        }

        verificar.close();
        inserir.close();
        atualizar.close();

        System.out.println(
                "========================================"
        );

        System.out.println(
                "JOGOS NOVOS ADICIONADOS: "
                + adicionados
        );

        System.out.println(
                "JOGOS ATUALIZADOS: "
                + atualizados
        );

        System.out.println(
                "========================================"
        );
    }
}