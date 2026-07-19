package PaooGame.States;

import PaooGame.Game;
import PaooGame.Graphics.Assets;

import java.awt.*;

/*! \class InstructionState
    \brief Starea care afiseaza instructiunile jocului.

    Prezinta controalele si obiectivele jocului.
    La click oriunde pe ecran se revine la meniu.
*/
public class InstructionState extends State {

    private Game game; /// Referinta catre obiectul principal al jocului

    private boolean wasPressed = true; /// Previne tranzitia imediata la prima afisare

    private Color titleColor  = new Color(218, 165, 32);  /// Culoare gold pentru titluri
    private Color textColor   = new Color(238, 230, 217); /// Culoare deschisa pentru text
    private Color accentColor = new Color(135, 211, 124); /// Verde deschis pentru taste

    ///Lista de reguli si instructiuni afisate pe ecran
    private final String[] rules = {
            "PERSONAJ:",
            "  [ W, A, S, D ] - Deplasare Luma",
            "  Săgeți - atac",
            "  [ ESC ] - Salvează jocul și ieși",
            " ",
            "OBIECTIVELE TALE:",
            "1. Învinge inamicii de pe hartă pentru a elibera Spiritele Capturate.",
            "2. Colectează poțiunea (Nivel 1) și cele 3 Diamante Magice (Nivel 2).",
            "3. Plasează diamantele pe piedestale la Nivelul 3 pentru a-l trezi pe Wizard.",
            "4. Învinge Wizard-ul pentru a salva întreaga Pădure Magică!"
    };

    /*! \fn public InstructionState(Game game)
        \brief Constructor care retine referinta catre Game.

        \param game Obiectul principal al jocului.
    */
    public InstructionState(Game game) {
        this.game = game;
    }

    /*! \fn public void Update()
        \brief Actualizeaza logica starii de instructiuni.

        Detecteaza click-ul de mouse si revine la MenuState.
    */
    @Override
    public void Update() {
        boolean pressing = game.getMouseManager().isLeftPressed();
        if (pressing && !wasPressed) {
            State.setState(game.menuState);
        }
        if (!pressing) wasPressed = false;
    }

    /*! \fn public void Draw(Graphics g)
        \brief Deseneaza ecranul de instructiuni.

        Afiseaza fundalul, titlul, liniile de instructiuni si intoarcerea la meniu.

        \param g Contextul grafic.
    */
    @Override
    public void Draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        /// Fundal de meniu
        if (Assets.menuBackground != null) {
            g2.drawImage(Assets.menuBackground, 0, 0, 1280, 720, null);
        }

        /// Strat semi-transparent
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(0, 0, 1280, 720);

        int cx = 1280 / 2;

        /// Titlu
        g2.setFont(new Font("Serif", Font.BOLD, 60));
        g2.setColor(titleColor);
        drawCentered(g2, "CUM SE JOACĂ", cx, 100);

        /// Linie separatoare
        g2.setColor(titleColor);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(cx - 200, 120, cx + 200, 120);

        /// Instructiuni
        int startY = 200;
        for (int i = 0; i < rules.length; i++) {
            String line = rules[i];
            if (line.endsWith(":")) {
                g2.setFont(new Font("Georgia", Font.BOLD, 22));
                g2.setColor(titleColor);
            } else {
                g2.setFont(new Font("Georgia", Font.PLAIN, 19));
                g2.setColor(textColor);
            }
            drawCentered(g2, line, cx, startY + i * 35);
        }

        /// Intoarcere la meniu
        g2.setFont(new Font("Georgia", Font.ITALIC, 16));
        g2.setColor(Color.YELLOW);
        drawCentered(g2, "[ click oriunde pentru a te întoarce la meniu ]", cx, 650);
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