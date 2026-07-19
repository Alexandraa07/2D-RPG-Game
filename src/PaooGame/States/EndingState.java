package PaooGame.States;

import PaooGame.Game;
import PaooGame.Graphics.Assets;

import java.awt.*;

/*! \class EndingState
    \brief Reprezinta starea de final a jocului.

    Aceasta stare afiseaza mesajul de finalizare al jocului
    dupa ce jucatorul salveaza Padurea Magica.
 */
public class EndingState extends State {

    /// Referinta catre instanta principala a jocului.
    private Game game;

    /// Culoarea utilizata pentru textul principal.
    private static final Color TEXT_BROWN = new Color(60, 40, 20);

    /// Culoarea utilizata pentru mesajul de iesire.
    private static final Color HINT_GOLD = new Color(160, 120, 0);

    /// Textul afisat
    private final String[] ending = {
            "Felicitări, Luma!",
            "Ai salvat Pădurea Magică!",
            "Copacii și-au recăpătat strălucirea,",
            "iar spiritele s-au reunit.",
            "Lumea e în siguranță, mulțumită ție."
    };

    /// Retine starea precedenta a click-ului mouse-ului.
    private boolean wasPressed = true;

    /*! \fn public EndingState(Game game)
        \brief Constructorul clasei EndingState.

        \param game Referinta catre instanta principala a jocului.
     */
    public EndingState(Game game) {
        this.game = game;
    }

    /*! \fn public void Update()
        \brief Actualizeaza logica starii de final.

        Detecteaza click-ul mouse-ului pentru revenire la meniul principal.
     */
    @Override
    public void Update() {

        boolean pressing = game.getMouseManager().isLeftPressed();

        /// Revenire la meniu dupa click.
        if (pressing && !wasPressed) {
            State.setState(game.menuState);
        }

        /// Actualizeaza starea click-ului.
        if (!pressing)
            wasPressed = false;
    }

    /*! \fn public void Draw(Graphics g)
        \brief Deseneaza ecranul de final al jocului.

        \param g Contextul grafic utilizat pentru desenare.
     */
    @Override
    public void Draw(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int W = game.getWnd().GetWndWidth();
        int H = game.getWnd().GetWndHeight();

        /// Centrul ecranului pe axa X.
        int cx = W / 2;

        /// Deseneaza fundalul povestii.
        if (Assets.storyBackground != null) {
            g2.drawImage(Assets.storyBackground, 0, 0, 1280, 720, null);
        }

        int startY = H / 2 - 120;

        /// Afiseaza fiecare linie a mesajului de final.
        for (int i = 0; i < ending.length; i++) {
            int y = startY + i * 40;
            /// Primul rand este afisat ca titlu.
            if (i == 0) {
                g2.setFont(new Font("Georgia", Font.BOLD | Font.ITALIC, 32));
            }
            else {
                g2.setFont(new Font("Georgia", Font.ITALIC, 22));
            }

            g2.setColor(TEXT_BROWN);

            drawCentered(g2, ending[i], cx, y);
        }

        /// Afiseaza mesajul pentru revenirea la meniu.
        g2.setFont(new Font("Georgia",
                Font.BOLD | Font.ITALIC, 16));

        g2.setColor(HINT_GOLD);

        drawCentered(g2, "[ click pentru a reveni la meniu ]", cx, H - 150);
    }

    /*! \fn private void drawCentered(Graphics2D g2, String text, int cx, int y)
        \brief Deseneaza un text centrat orizontal.

        Pozitia pe axa X este calculata folosind latimea textului.

        \param g2 Contextul grafic utilizat pentru desenare.
        \param text Textul afisat.
        \param cx Coordonata centrului pe axa X.
        \param y Coordonata pe axa Y.
     */
    private void drawCentered(Graphics2D g2, String text, int cx, int y) {

        FontMetrics fm = g2.getFontMetrics();

        g2.drawString(text, cx - fm.stringWidth(text) / 2, y);
    }
}