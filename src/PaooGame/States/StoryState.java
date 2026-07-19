package PaooGame.States;

import PaooGame.Game;
import PaooGame.Graphics.Assets;

import java.awt.*;

/*! \class StoryState
    \brief Starea care afiseaza povestea introductiva a jocului.

    Este prezentata povestea jocului inainte de inceperea aventurii.
    La click oriunde pe ecran, se face tranzitia catre GameState.
*/
public class StoryState extends State {

    private Game game; /// Referinta catre obiectul principal al jocului

    private static final Color TEXT = new Color(60, 40, 20); /*!< Culoarea textului narativ */
    private static final Color HINT = new Color(60, 40, 20); /*!< Culoarea textului de hint */

    /// Liniile de text ale povestii introductive
    private final String[] lines = {
            "Undeva, departe , se întindea Pădurea Magică —",
            "cel mai viu loc din lume. Copacii atingeau norii, spiritele",
            "trăiau în pace și armonie.",
            " ",
            "Într-o zi, au apărut răufacatorii care au pus stapanire ",
            "pe intreaga padure, spiritele fiind capturate,",
            "s-a dezlantuit haosul ",
            " ",
            "A rămas doar un spirit — Luma.",
            "Ajut-o să găsească diamantele magice, să elibereze",
            "spiritele capturate si sa salveze ",
            "ce a mai rămas din pădurea magică!"
    };

    private boolean wasPressed = true; /// Previne tranzitia imediata la prima afisare */

    /*! \fn public StoryState(Game game)
        \brief Constructor care retine referinta catre Game.
        \param game Obiectul principal al jocului.
    */
    public StoryState(Game game) {
        this.game = game;
    }

    /*! \fn public void Update()
        \brief Actualizeaza logica starii de poveste.

        Detecteaza click-ul de mouse si face tranzitia catre GameState.
    */
    @Override
    public void Update() {
        boolean pressing = game.getMouseManager().isLeftPressed();
        if (pressing && !wasPressed) {
            State.setState(game.gameState);
        }
        if (!pressing) wasPressed = false;
    }

    /*! \fn public void Draw(Graphics g)
        \brief Deseneaza ecranul de poveste introductiva.

        Afiseaza fundalul, liniile de poveste si continuarea.

        \param g Contextul grafic.
    */
    @Override
    public void Draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        /// Fundal poveste
        if (Assets.storyBackground != null) {
            g2.drawImage(Assets.storyBackground, 0, 0, 1280, 720, null);
        }

        int W  = game.getWnd().GetWndWidth();
        int H  = game.getWnd().GetWndHeight();
        int cx = W / 2;

        /// Linii text narativ
        g2.setFont(new Font("Georgia", Font.ITALIC, 20));
        g2.setColor(TEXT);
        int startY = H / 2 - 200;
        for (int i = 0; i < lines.length; i++) {
            drawCentered(g2, lines[i], cx, startY + i * 36);
        }

        /// Continuare
        g2.setFont(new Font("Georgia", Font.ITALIC, 16));
        g2.setColor(HINT);
        drawCentered(g2, "[ click pentru a incepe jocul ]", cx, H - 130);
    }

    /*! \fn private void drawCentered(Graphics2D g2, String text, int cx, int y)
        \brief Deseneaza un text centrat orizontal pe ecran.
        \param g2 Contextul grafic 2D.
        \param text Textul de desenat.
        \param cx Centrul orizontal al ecranului.
        \param y Coordonata Y la care se deseneaza textul.
    */
    private void drawCentered(Graphics2D g2, String text, int cx, int y) {
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, cx - fm.stringWidth(text) / 2, y);
    }
}