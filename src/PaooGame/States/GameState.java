package PaooGame.States;

import PaooGame.Entities.*;
import PaooGame.Game;
import PaooGame.Graphics.Assets;

import java.awt.*;
import java.awt.geom.AffineTransform;

/*! \class GameState
    \brief Starea principala a jocului

    Deseneaza harta in doua etape: straturile de sub jucator, apoi jucatorul,
    apoi straturile de deasupra jucatorului. Gestioneaza logica de update
    pentru toate entitatile si tranzitiile intre harti.
*/
public class GameState extends State {

    private Game game; /// Referinta catre obiectul principal al jocului */

    private static final int DOOR_FIRST_MAPX = 2784;   /// Coordonata X a portii spre harta 2
    private static final int DOOR_FIRST_MAPY = 1408;   /// Coordonata Y a portii spre harta 2
    private static final int DOOR_SECOND_MAPX = 3776;  /// Coordonata X a portii spre harta 3
    private static final int DOOR_SECOND_MAPY = 2240;  /// Coordonata Y a portii spre harta 3
    private static final int TILE_SIZE = 32;    /// Dimensiunea unui tile in pixeli
    private static final int CHARACTER_SIZE = 64;   /// Dimensiunea personajului in pixeli

    /*! \fn public GameState(Game game)
        \brief Constructor care retine referinta catre Game.
        \param game Obiectul principal al jocului.
    */
    public GameState(Game game) {
        this.game = game;
    }

    /*! \fn public void Update()
        \brief Actualizeaza logica intregii scene de joc.

        Actualizeaza jucatorul, NPC-urile, inamicii, obiectele colectabile,
        piedestalele, camera si verifica tranzitiile intre harti.
    */
    @Override
    public void Update() {

        game.getPlayer().Update();
        checkMapTransition(game.getPlayer());
        Player player = game.getPlayer();

        for (Npc npc : game.getNpcs()) {
            if (npc.getMapId() == game.getCurrentMap()) {
                npc.Update();
            }
        }

        game.getPamant().Update();
        game.getApa().Update();

        if (game.getCurrentMap() == 1) {
            game.getArcher().Update();
            if (!game.getArcher().isAlive() && !game.getPamant().isInstructionRead()) {
                game.getPamant().setIsActive(true);
            }
            game.getPotion().Update();
        }

        if (game.getCurrentMap() == 2) {
            game.getGoblin().Update();
            if (!game.getGoblin().isAlive() && !game.getApa().isInstructionRead()) {
                game.getApa().setIsActive(true);
            }
            game.getDiamond1().Update();
            game.getDiamond2().Update();
            game.getDiamond3().Update();
        }

        if (game.getCurrentMap() == 3) {
            for (Protector protector : game.getProtectors()) {
                protector.Update();
            }
            if (game.allPedestalsReady()) {
                game.getWizard().Update();
            }
            for (int i = 0; i < game.getThunder().size(); i++) {
                Thunder t = game.getThunder().get(i);
                t.Update();
            }
            game.getPiedestal1().Update();
            game.getPiedestal2().Update();
            game.getPiedestal3().Update();
            if (game.getKeyManager().place) {
                game.getPiedestal1().tryPlaceDiamond();
                game.getPiedestal2().tryPlaceDiamond();
                game.getPiedestal3().tryPlaceDiamond();
            }
        }

        // Setam dimensiunile hartii in functie de nivel
        int mapWidth = 0, mapHeight = 0;
        if (game.getCurrentMap() == 1) {
            mapWidth = 90 * 32;
            mapHeight = 60 * 32;
        } else if (game.getCurrentMap() == 2 || game.getCurrentMap() == 3) {
            mapHeight = 80 * 32;
            mapWidth = 120 * 32;
        }

        // Limiteaza jucatorul sa nu iasa din harta
        if (player.getX() < 0) player.setX(0);
        if (player.getY() < 0) player.setY(0);
        if (player.getX() + CHARACTER_SIZE > mapWidth)  player.setX(mapWidth - CHARACTER_SIZE);
        if (player.getY() + CHARACTER_SIZE > mapHeight) player.setY(mapHeight - CHARACTER_SIZE);

        // Calcul pozitie camera
        float zoom = game.getCamera().getZoom();
        float cameraX = player.getX() + TILE_SIZE - (1280 / zoom) / 2f;
        float cameraY = player.getY() + TILE_SIZE - (720 / zoom) / 2f;

        if (cameraX < 0) cameraX = 0;
        if (cameraY < 0) cameraY = 0;
        if (cameraX > mapWidth  - (1280 / zoom)) cameraX = mapWidth  - (1280 / zoom);
        if (cameraY > mapHeight - (720  / zoom)) cameraY = mapHeight - (720  / zoom);

        game.getCamera().setPosition(cameraX, cameraY);
    }

    /*! \fn public void Draw(Graphics g)
        \brief Deseneaza scena de joc: harta sub jucator, jucatorul, harta peste jucator.
        \param g Contextul grafic.
    */
    @Override
    public void Draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform oldTransform = g2d.getTransform();
        game.getCamera().apply(g2d);

        if (game.getCurrentMap() == 1)
            drawMap1(g2d);
        else if (game.getCurrentMap() == 2)
            drawMap2(g2d);
        else
            drawMap3(g2d);

        g2d.setTransform(oldTransform);
        DrawMeniuSecundar(g);
        DrawText(g);
        ThankYouMessage(g);

        // Afisare ecran Game Over la moartea jucatorului pe harta 3
        if (game.getCurrentMap() == 3 && game.getPlayer().isDying()) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, game.getWnd().GetWndWidth(), game.getWnd().GetWndHeight());
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 72));
            String text = "GAME OVER";
            int textWidth = g.getFontMetrics().stringWidth(text);
            g.drawString(text, (game.getWnd().GetWndWidth() - textWidth) / 2, 360);
        }

        // Salvare si iesire la apasarea ESC
        if (game.getKeyManager().esc) {
            game.getDataBaseManager().saveGame(game.getPlayer(), game.getArcher(), game.getGoblin(), game.getWizard(), game.getCurrentMap(), game.getPotion(), game.getDiamond1(), game.getDiamond2(), game.getDiamond3());
            System.exit(0);
        }
    }

    /*! \fn private void drawMap1(Graphics2D g2d)
        \brief Deseneaza toate entitatile de pe harta 1.

        Deseneaza straturile hartii, jucatorul, NPC-urile, arcasul,
        potiunea si spiritul Pamantului (daca arcasul a fost invins).

        \param g2d Contextul grafic 2D.
    */
    private void drawMap1(Graphics2D g2d) {
        game.getMap1().drawUnder(g2d);
        game.getPlayer().Draw(g2d);

        for (Npc npc : game.getNpcs()) {
            if (npc.getMapId() == 1)
                npc.Draw(g2d);
        }

        game.getArcher().Draw(g2d);
        game.getPotion().Draw(g2d);

        if (!game.getArcher().isAlive()) {
            game.getPamant().Draw(g2d);
        }

        game.getMap1().drawOver(g2d);
    }

    /*! \fn private void drawMap2(Graphics2D g2d)
        \brief Deseneaza toate entitatile de pe harta 2.

        Deseneaza straturile hartii, jucatorul, NPC-urile, goblinul,
        diamantele si spiritul Apei (daca goblinul a fost invins).

        \param g2d Contextul grafic 2D.
    */
    private void drawMap2(Graphics2D g2d) {
        game.getMap2().drawUnder(g2d);
        game.getPlayer().Draw(g2d);

        for (Npc npc : game.getNpcs()) {
            if (npc.getMapId() == 2)
                npc.Draw(g2d);
        }

        game.getGoblin().Draw(g2d);

        if (!game.getGoblin().isAlive()) {
            game.getApa().Draw(g2d);
        }

        game.getDiamond1().Draw(g2d);
        game.getDiamond2().Draw(g2d);
        game.getDiamond3().Draw(g2d);

        game.getMap2().drawOver(g2d);
    }

    /*! \fn private void drawMap3(Graphics2D g2d)
        \brief Deseneaza toate entitatile de pe harta 3.

        Deseneaza straturile hartii, jucatorul, NPC-urile, piedestalele,
        vrăjitorul (daca toate piedestalele sunt activate), protectorii
        si fulgerele. Verifica si conditia de castig a jocului.

        \param g2d Contextul grafic 2D.
    */
    private void drawMap3(Graphics2D g2d) {
        game.getMap3().drawUnder(g2d);
        game.getPlayer().Draw(g2d);

        for (Npc npc : game.getNpcs()) {
            if (npc.getMapId() == 3)
                npc.Draw(g2d);
        }

        game.getPiedestal1().Draw(g2d);
        game.getPiedestal2().Draw(g2d);
        game.getPiedestal3().Draw(g2d);

        if (game.allPedestalsReady()) {
            game.getWizard().Draw(g2d);
        }

        // Verificare conditie de castig
        if (!game.getWizard().isAlive() && game.allPedestalsReady()) {
            State.setState(game.endingState);
        }

        for (Protector protector : game.getProtectors()) {
            protector.Draw(g2d);
        }

        for (Thunder t : game.getThunder()) {
            t.Draw(g2d);
        }

        game.getMap3().drawOver(g2d);
    }

    /*! \fn private void DrawMeniuSecundar(Graphics g)
        \brief Deseneaza "inventarul": potiune, diamante colectate si viata jucatorului.
        \param g Contextul grafic.
    */
    private void DrawMeniuSecundar(Graphics g) {

        if (game.getCurrentMap() >= 1) {
            if (!game.getPotion().isActive() && game.getPlayer().hasPotion()) {
                g.drawImage(Assets.potion, 10, 10, 50, 50, null);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 17));
                g.drawString("Super Power!", 75, 45);
            }
            else
            {
                g.setColor(new Color(0, 0, 0, 120));
                g.fillRect(10, 10, 50, 50);
            }
        }

        if (game.getCurrentMap() >= 2) {
            // Diamond 1
            if (!game.getDiamond1().isActive() && !game.getPiedestal1().hasDiamond()) {
                g.drawImage(Assets.diamond[0], 20, 70, 50, 50, null);
            } else {
                g.setColor(new Color(0, 0, 0, 120));
                g.fillRect(20, 70, 50, 50);
            }

            // Diamond 2
            if (!game.getDiamond2().isActive() && !game.getPiedestal2().hasDiamond()) {
                g.drawImage(Assets.diamond[0], 90, 70, 50, 50, null);
            } else {
                g.setColor(new Color(0, 0, 0, 120));
                g.fillRect(90, 70, 50, 50);
            }

            // Diamond 3
            if (!game.getDiamond3().isActive() && !game.getPiedestal3().hasDiamond()) {
                g.drawImage(Assets.diamond[0], 160, 70, 50, 50, null);
            } else {
                g.setColor(new Color(0, 0, 0, 120));
                g.fillRect(160, 70, 50, 50);
            }
        }

        // Desenare inimi pentru viata curenta
        int currentHealth = game.getPlayer().getLife();
        for (int i = 0; i < currentHealth; i++) {
            g.drawImage(Assets.heart, 1040 + (i * 40), 20, 30, 30, null);
        }
    }

    /*! \fn private void DrawText(Graphics g)
        \brief Deseneaza caseta de dialog pentru NPC-ul activ de pe harta curenta.
        \param g Contextul grafic.
    */
    private void DrawText(Graphics g) {
        for (Npc npc : game.getNpcs()) {
            if (npc.getMapId() == game.getCurrentMap() && npc.isActive()) {
                int screenW = game.getWnd().GetWndWidth();
                int screenH = game.getWnd().GetWndHeight();
                int boxW = 600, boxH = 120;
                int boxX = (screenW - boxW) / 2;
                int boxY = screenH - boxH - 70;

                g.setColor(new Color(0, 0, 0, 200));
                g.fillRoundRect(boxX, boxY, boxW, boxH, 25, 25);
                g.setColor(Color.WHITE);
                g.drawRoundRect(boxX, boxY, boxW, boxH, 25, 25);

                g.setFont(new Font("Arial", Font.BOLD, 18));
                g.setColor(Color.WHITE);
                g.drawString(npc.getInstructions(), boxX + 30, boxY + 50);

                g.setFont(new Font("Arial", Font.ITALIC, 13));
                g.setColor(Color.YELLOW);
                g.drawString("Apasa [M] pentru a incepe aventura...", boxX + 30, boxY + 90);
            }
        }
    }

    /*! \fn private void checkMapTransition(Player player)
        \brief Verifica daca jucatorul a ajuns la poarta de tranzitie si schimba harta.

        Pe harta 1 verifica poarta spre harta 2.
        Pe harta 2 verifica poarta spre harta 3 (doar daca diamantele au fost colectate).

        \param player Referinta catre jucator.
    */
    private void checkMapTransition(Player player) {
        int X = (int) player.getX();
        int Y = (int) player.getY();

        if (game.getCurrentMap() == 1) {
            if (X + CHARACTER_SIZE > DOOR_FIRST_MAPX && X < DOOR_FIRST_MAPX + TILE_SIZE &&
                    Y + CHARACTER_SIZE > DOOR_FIRST_MAPY && Y < DOOR_FIRST_MAPY + CHARACTER_SIZE) {
                game.setCurrentMap(2);
                player.setX(32);
                player.setY(2112);
            }
        } else if (game.getCurrentMap() == 2) {
            if (game.checkReady()) {
                if (X + CHARACTER_SIZE > DOOR_SECOND_MAPX && X < DOOR_SECOND_MAPX + TILE_SIZE &&
                        Y + CHARACTER_SIZE > DOOR_SECOND_MAPY && Y < DOOR_SECOND_MAPY + CHARACTER_SIZE) {
                    game.setCurrentMap(3);
                    player.setX(32);
                    player.setY(1216);
                }
            }
        }
    }

    /*! \fn public void handlePlayerDeath()
        \brief Gestioneaza moartea jucatorului.

        Daca jucatorul moare pe harta 3, jocul se reseteaza complet si
        revine la meniu. Pe hartile 1 si 2 se reia doar nivelul curent.
    */
    public void handlePlayerDeath() {
        if (game.getCurrentMap() == 3) {
            game.setCurrentMap(1);
            game.getPlayer().reset();

            for (Enemy e : game.getEnemies()) {
                e.reset();
            }

            game.getPamant().reset();
            game.getApa().reset();
            for (Npc npc : game.getNpcs()) {
                npc.reset();
            }
            game.getArcher().reset();
            game.getGoblin().reset();
            for (Protector protector : game.getProtectors()) {
                protector.reset();
            }
            game.getPotion().reset();
            game.getDiamond1().reset();
            game.getDiamond2().reset();
            game.getDiamond3().reset();
            game.getPiedestal1().reset();
            game.getPiedestal2().reset();
            game.getPiedestal3().reset();

            System.out.println("Game Over! Revenire la meniu.");
            State.setState(game.menuState);
        } else {
            restartLevel();
        }
    }

    /*! \fn private void restartLevel()
        \brief Reia nivelul curent (harta 1 sau harta 2) dupa moartea jucatorului.

        Reseteaza viata jucatorului, pozitia sa initiala pe harta curenta
        si starea tuturor entitatilor relevante pentru acel nivel.
    */
    private void restartLevel() {
        Player p = game.getPlayer();
        p.setLife(3);
        p.setDying(false);

        if (game.getCurrentMap() == 1) {
            p.setX(384);
            p.setY(1856);
            game.getArcher().reset();
            for (Npc npc : game.getNpcs()) {
                npc.reset();
            }
            game.getPamant().reset();
            game.getPotion().reset();
        } else if (game.getCurrentMap() == 2) {
            p.setX(32);
            p.setY(2112);
            game.getGoblin().reset();
            for (Npc npc : game.getNpcs()) {
                npc.reset();
            }
            game.getApa().reset();
            game.getDiamond1().reset();
            game.getDiamond2().reset();
            game.getDiamond3().reset();
            game.getPiedestal1().reset();
            game.getPiedestal2().reset();
            game.getPiedestal3().reset();
        }
    }

    /*! \fn private void ThankYouMessage(Graphics g)
        \brief Afiseaza mesajul de multumire al spiritelor dupa salvarea lor.

        Apare dupa ce spiritul Pamantului (harta 1) sau spiritul Apei (harta 2)
        a fost eliberat.

        \param g Contextul grafic.
    */
    private void ThankYouMessage(Graphics g) {
        if (game.getApa().isActive() || game.getPamant().isActive()) {
            int screenW = game.getWnd().GetWndWidth();
            int screenH = game.getWnd().GetWndHeight();
            int boxW = 600, boxH = 120;
            int boxX = (screenW - boxW) / 2;
            int boxY = screenH - boxH - 70;

            g.setColor(new Color(0, 0, 0, 200));
            g.fillRoundRect(boxX, boxY, boxW, boxH, 25, 25);
            g.setColor(Color.WHITE);
            g.drawRoundRect(boxX, boxY, boxW, boxH, 25, 25);

            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.setColor(Color.WHITE);
            if (game.getCurrentMap() == 1) {
                g.drawString(game.getPamant().getInstructions(), boxX + 30, boxY + 50);
            }
            if (game.getCurrentMap() == 2) {
                g.drawString(game.getApa().getInstructions(), boxX + 30, boxY + 50);
            }

            g.setFont(new Font("Arial", Font.ITALIC, 13));
            g.setColor(Color.YELLOW);
            g.drawString("Apasa [M] pentru a continua aventura!", boxX + 30, boxY + 90);
        }
    }
}