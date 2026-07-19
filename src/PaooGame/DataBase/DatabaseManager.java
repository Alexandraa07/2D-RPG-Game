package PaooGame.DataBase;

import PaooGame.Entities.*;
import PaooGame.Game;
import java.sql.*;

/*! \class DatabaseManager
    \brief Clasa care gestioneaza baza de date a jocului.

    Creeaza tabelele necesare, salveaza si incarca starea jocului
    (jucator, inamici, harta, obiecte)
 */
public class DatabaseManager {

    private Connection c = null;    ///< Conexiunea la baza de date SQLite.
    private Statement stmt = null;  ///< Obiectul folosit pentru executarea query-urilor SQL.

    /*! \fn public void connect()
        \brief Deschide conexiunea la baza de date SQLite.

        Inregistreaza driverul JDBC si creaza conexiunea la fisierul game.db.
        Daca apare o eroare, o afiseaza in consola.
     */
    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:game.db");
            System.out.println("Opened database successfully");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /*! \fn public void createTable()
        \brief Creeaza tabelele din baza de date daca nu exista deja.

        Creeaza doua tabele:
        - PLAYER_SAVE: retine pozitia, viata, harta curenta si obiectele jucatorului.
        - ENEMY_SAVE: retine pozitia si viata fiecarui inamic.
     */
    public void createTable() {
        try {
            stmt = c.createStatement();

            /// Tabela pentru jucator: pozitie, viata, harta, potiune, diamante
            String sql1 = "CREATE TABLE IF NOT EXISTS PLAYER_SAVE " +
                    "(ID INT PRIMARY KEY NOT NULL," +
                    " X_PLAYER REAL NOT NULL, " +
                    " Y_PLAYER REAL NOT NULL, " +
                    " LIFE_PLAYER INT NOT NULL, " +
                    " MAP INT NOT NULL, " +
                    " HAS_POTION INT, " +
                    " DIAMOND1 INT, " +
                    " DIAMOND2 INT, " +
                    " DIAMOND3 INT)";

            /// Tabela pentru inamici: pozitie si viata pentru fiecare inamic
            String sql2 = "CREATE TABLE IF NOT EXISTS ENEMY_SAVE" +
                    "(ID INT PRIMARY KEY NOT NULL," +
                    " X_ARCHER REAL NOT NULL, " +
                    " Y_ARCHER REAL NOT NULL, " +
                    " LIFE_ARCHER INT NOT NULL, " +
                    " X_GOBLIN REAL NOT NULL, " +
                    " Y_GOBLIN REAL NOT NULL, " +
                    " LIFE_GOBLIN INT NOT NULL, " +
                    " X_WIZARD REAL NOT NULL, " +
                    " Y_WIZARD REAL NOT NULL, " +
                    " LIFE_WIZARD INT NOT NULL)";

            stmt.execute(sql1);
            stmt.execute(sql2);
            stmt.close();
            System.out.println("Tables created successfully");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /*! \fn public void saveGame(Player player, Archer arcas, Goblin goblin, Vrajitor vrajitor, int currentMap, Potion potion, Diamond diamond1, Diamond diamond2, Diamond diamond3)
        \brief Salveaza starea curenta a jocului in baza de date.

        Stocheaza pozitia si viata jucatorului, harta curenta, starea potiunii
        si a diamantelor, precum si pozitia si viata fiecarui inamic.
        Foloseste INSERT OR REPLACE pentru a suprascrie save-ul anterior.

        \param player     Referinta la jucator.
        \param arcas      Referinta la inamicul de tip arcas.
        \param goblin     Referinta la inamicul de tip goblin.
        \param vrajitor   Referinta la inamicul de tip vrajitor.
        \param currentMap Indexul hartii curente.
        \param potion     Referinta la obiectul de tip Potion.
        \param diamond1   Referinta la primul diamant.
        \param diamond2   Referinta la al doilea diamant.
        \param diamond3   Referinta la al treilea diamant.
     */
    public void saveGame(Player player, Archer arcas, Goblin goblin, Vrajitor vrajitor, int currentMap, Potion potion, Diamond diamond1, Diamond diamond2, Diamond diamond3) {
        try {
            c.setAutoCommit(false);
            stmt = c.createStatement();

            /// SQLite nu are tip boolean, asa ca salvam 1 pentru true si 0 pentru false

            /// Verificam daca jucatorul are potiunea in inventar
            int hasPotion = 0;
            if (player.hasPotion()) {
                hasPotion = 1;
            }

            /// Verificam daca fiecare diamant este inca pe harta (activ)
            int hasDiamond1 = 0;
            if (diamond1.isActive()) {
                hasDiamond1 = 1;
            }

            int hasDiamond2 = 0;
            if (diamond2.isActive()) {
                hasDiamond2 = 1;
            }

            int hasDiamond3 = 0;
            if (diamond3.isActive()) {
                hasDiamond3 = 1;
            }

            String sql1 = "INSERT OR REPLACE INTO PLAYER_SAVE " +
                    "(ID, X_PLAYER, Y_PLAYER, LIFE_PLAYER, MAP, HAS_POTION, DIAMOND1, DIAMOND2, DIAMOND3) " +
                    "VALUES (1, " + player.getX() + ", " + player.getY() + ", " + player.getHealth() + ", " +
                    currentMap + ", " + hasPotion + ", " + hasDiamond1 + ", " + hasDiamond2 + ", " + hasDiamond3 + ");";

            String sql2 = "INSERT OR REPLACE INTO ENEMY_SAVE " +
                    "(ID, X_ARCHER, Y_ARCHER, LIFE_ARCHER, X_GOBLIN, Y_GOBLIN, LIFE_GOBLIN, X_WIZARD, Y_WIZARD, LIFE_WIZARD) " +
                    "VALUES (1, " + arcas.getX() + ", " + arcas.getY() + ", " + arcas.getLife() + ", " +
                    goblin.getX() + ", " + goblin.getY() + ", " + goblin.getLife() + ", " +
                    vrajitor.getX() + ", " + vrajitor.getY() + ", " + vrajitor.getLife() + ");";

            stmt.executeUpdate(sql1);
            stmt.executeUpdate(sql2);

            c.commit();
            stmt.close();
            System.out.println("Game saved successfully!");
        } catch (Exception e) {
            System.err.println("Save error: " + e.getMessage());
        }
    }

    /*! \fn public void loadGame(Player player, Archer arcas, Goblin goblin, Vrajitor vrajitor, Game game, Potion potion, Diamond diamond1, Diamond diamond2, Diamond diamond3)
        \brief Incarca starea jocului din baza de date.

        Citeste din tabelele PLAYER_SAVE si ENEMY_SAVE si reface
        pozitia, viata si starea obiectelor pentru jucator si inamici.

        \param player    Referinta la jucator.
        \param arcas     Referinta la inamicul de tip arcas.
        \param goblin    Referinta la inamicul de tip goblin.
        \param vrajitor  Referinta la inamicul de tip vrajitor.
        \param game      Referinta la instanta principala a jocului.
        \param potion    Referinta la obiectul de tip Potion.
        \param diamond1  Referinta la primul diamant.
        \param diamond2  Referinta la al doilea diamant.
        \param diamond3  Referinta la al treilea diamant.
     */
    public void loadGame(Player player, Archer arcas, Goblin goblin, Vrajitor vrajitor, Game game, Potion potion, Diamond diamond1, Diamond diamond2, Diamond diamond3) {
        try {
            stmt = c.createStatement();

            /// Citim datele jucatorului din tabela PLAYER_SAVE
            ResultSet rs = stmt.executeQuery("SELECT * FROM PLAYER_SAVE WHERE ID=1;");
            if (rs.next()) {
                player.setX(rs.getFloat("X_PLAYER"));
                player.setY(rs.getFloat("Y_PLAYER"));
                player.setLife(rs.getInt("LIFE_PLAYER"));
                game.setCurrentMap(rs.getInt("MAP"));

                /// Refacem starea potiunii
                if (rs.getInt("HAS_POTION") == 1) {
                    /// Jucatorul are potiunea si o scoatem de pe harta
                    player.setArePotiune(true);
                    potion.setActive(false);
                } else {
                    /// Daca jucatorul nu avea potiunea ramane pe harta
                    player.setArePotiune(false);
                    potion.setActive(true);
                }

                /// Refacem starea fiecarui diamant (1 inseamna ca este pe harta, 0 inseamna ca este luat deja luat)
                if (rs.getInt("DIAMOND1") == 1) {
                    diamond1.setActive(true);
                } else {
                    diamond1.setActive(false);
                }

                if (rs.getInt("DIAMOND2") == 1) {
                    diamond2.setActive(true);
                } else {
                    diamond2.setActive(false);
                }

                if (rs.getInt("DIAMOND3") == 1) {
                    diamond3.setActive(true);
                } else {
                    diamond3.setActive(false);
                }
            }

            /// Citim datele inamicilor din tabela ENEMY_SAVE
            ResultSet rsE = stmt.executeQuery("SELECT * FROM ENEMY_SAVE WHERE ID=1;");
            if (rsE.next()) {
                arcas.setX(rsE.getFloat("X_ARCHER"));
                arcas.setY(rsE.getFloat("Y_ARCHER"));
                arcas.setLife(rsE.getInt("LIFE_ARCHER"));

                goblin.setX(rsE.getFloat("X_GOBLIN"));
                goblin.setY(rsE.getFloat("Y_GOBLIN"));
                goblin.setLife(rsE.getInt("LIFE_GOBLIN"));

                vrajitor.setX(rsE.getFloat("X_WIZARD"));
                vrajitor.setY(rsE.getFloat("Y_WIZARD"));
                vrajitor.setLife(rsE.getInt("LIFE_WIZARD"));
            }

            rs.close();
            rsE.close();
            stmt.close();
            System.out.println("Game loaded successfully!");
        } catch (Exception e) {
            System.err.println("Load error: " + e.getMessage());
        }
        System.out.println("Are potiune: " + game.getPlayer().hasPotion());
    }
}