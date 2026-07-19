package PaooGame.States;

import java.awt.*;
import PaooGame.Graphics.Assets;
import PaooGame.Game;

/*! \class MenuState
    \brief Starea meniului principal al jocului.

    Afiseaza butoanele de Start, Load, Instructions si Exit.
*/
public class MenuState extends State {

    private Game game; /// Referinta catre obiectul principal al jocului

    private Color baseColor  = new Color(20, 40, 30, 200);  /// Culoarea de baza a butoanelor
    private Color hoverColor = new Color(40, 90, 60, 230);  /// Culoarea butoanelor la hover
    private Color gold       = new Color(218, 165, 32);     /// Culoare gold pentru titlu si contur
    private Color textLight  = new Color(238, 230, 217);    /// Culoare deschisa pentru textul butoanelor

    /*! \fn public MenuState(Game g)
        \brief Constructor care retine referinta catre Game.
        \param g Obiectul principal al jocului.
    */
    public MenuState(Game g) {
        this.game = g;
    }

    /*! \fn public void Update()
        \brief Actualizeaza logica meniului.

        Detecteaza click-urile pe butoane si tranzitioneaza catre
        starea corespunzatoare (joc nou, incarcare, instructiuni, iesire).
    */
    @Override
    public void Update() {
        if (game.getMouseManager().isLeftPressed()) {
            int x = game.getMouseManager().getMouseX();
            int y = game.getMouseManager().getMouseY();

            /// Buton Start
            if (isMouseOver(x, y, 180, 340, 280, 60)) {
                System.out.println("Luma pornește aventura!");
                State.setState(new StoryState(game));
            }

            /// Buton Load
            if (isMouseOver(x, y, 180, 420, 280, 60)) {
                System.out.println("Loading game");
                game.getDataBaseManager().loadGame(
                        game.getPlayer(), game.getArcher(), game.getGoblin(),
                        game.getWizard(), game, game.getPotion(),
                        game.getDiamond1(), game.getDiamond2(), game.getDiamond3()
                );
                State.setState(game.gameState);
            }

            /// Buton Instructions
            if (isMouseOver(x, y, 180, 500, 280, 60)) {
                System.out.println("Deschidere instrucțiuni joc...");
                State.setState(new InstructionState(game));
            }

            /// Buton Exit
            if (isMouseOver(x, y, 180, 580, 280, 60)) {
                System.exit(0);
            }
        }
    }

    /*! \fn private boolean isMouseOver(int mx, int my, int x, int y, int width, int height)
        \brief Verifica daca cursorul mouse-ului se afla peste o zona drept.
        \param mx Coordonata X a mouse-ului.
        \param my Coordonata Y a mouse-ului.
        \param x Coordonata X a zonei.
        \param y Coordonata Y a zonei.
        \param width Latimea zonei.
        \param height Inaltimea zonei.
        \return true daca mouse-ul este in zona, false altfel.
    */
    private boolean isMouseOver(int mx, int my, int x, int y, int width, int height) {
        return (mx >= x && mx <= x + width && my >= y && my <= y + height);
    }

    /*! \fn public void Draw(Graphics g)
        \brief Deseneaza meniul principal.

        Afiseaza fundalul, titlul si cele 4 butoane de navigare.

        \param g Contextul grafic.
    */
    @Override
    public void Draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        /// Fundal
        if (Assets.menuBackground != null) {
            g2.drawImage(Assets.menuBackground, 0, 0, 1280, 720, null);
        }

        /// Gradient semi-transparent peste fundal
        GradientPaint gp = new GradientPaint(0, 0, new Color(0, 0, 0, 180), 600, 0, new Color(0, 0, 0, 0));
        g2.setPaint(gp);
        g2.fillRect(0, 0, 800, 720);

        /// Titlu
        Title(g2, 320, 280);

        /// Butoane cu detectare
        int mouseX = game.getMouseManager().getMouseX();
        int mouseY = game.getMouseManager().getMouseY();

        Button(g2, "Start game",180, 340, 280, 60, isMouseOver(mouseX, mouseY, 180, 340, 280, 60));
        Button(g2, "Load game",180, 420, 280, 60, isMouseOver(mouseX, mouseY, 180, 420, 280, 60));
        Button(g2, "Instructions",180, 500, 280, 60, isMouseOver(mouseX, mouseY, 180, 500, 280, 60));
        Button(g2, "Exit", 180, 580, 280, 60, isMouseOver(mouseX, mouseY, 180, 580, 280, 60));
    }

    /*! \fn private void Title(Graphics2D g2, int centerX, int y)
        \brief Deseneaza titlul si subtitlul jocului in meniu.
        \param g2 Contextul grafic 2D.
        \param centerX Centrul orizontal al titlului.
        \param y Coordonata Y a titlului.
    */
    private void Title(Graphics2D g2, int centerX, int y) {
        Font titleFont = new Font("Serif", Font.BOLD, 110);
        g2.setFont(titleFont);

        String titleText = "LUMA";
        FontMetrics fm = g2.getFontMetrics(titleFont);
        int titleX = centerX - (fm.stringWidth(titleText) / 2);

        /// Umbra titlu
        g2.setColor(new Color(0, 0, 0, 150));
        g2.drawString(titleText, titleX + 6, y + 6);

        /// Text principal titlu
        g2.setColor(gold);
        g2.drawString(titleText, titleX, y);

        /// Subtitlu
        Font subFont = new Font("Serif", Font.PLAIN, 30);
        g2.setFont(subFont);
        String subText = "SPIRITUL LUMINII";
        FontMetrics fmSub = g2.getFontMetrics(subFont);
        int subX = centerX - (fmSub.stringWidth(subText) / 2);

        g2.setColor(new Color(224, 192, 151));
        g2.drawString(subText, subX, y + 45);
    }

    /*! \fn private void Button(Graphics2D g2, String text, int x, int y, int w, int h, boolean hovered)
        \brief Deseneaza un buton cu forma de poligon si efect de hover.
        \param g2 Contextul grafic 2D.
        \param text Textul afisat pe buton.
        \param x Coordonata X a butonului.
        \param y Coordonata Y a butonului.
        \param w Latimea butonului.
        \param h Inaltimea butonului.
        \param hovered true daca mouse-ul este peste buton, false altfel.
    */
    private void Button(Graphics2D g2, String text, int x, int y, int w, int h, boolean hovered) {
        /// Forma poligon
        int[] xPoints = {x, x + 15, x + w - 15, x + w, x + w, x + w - 15, x + 15, x};
        int[] yPoints = {y + 15, y, y, y + 15, y + h - 15, y + h, y + h, y + h - 15};
        Polygon shape = new Polygon(xPoints, yPoints, 8);

        /// Fundal buton
        if (hovered) {
            g2.setPaint(new GradientPaint(x, y, hoverColor, x + w, y, baseColor));
        } else {
            g2.setPaint(new GradientPaint(x, y, baseColor, x + w, y, new Color(10, 20, 15, 200)));
        }
        g2.fill(shape);

        /// Contur buton
        g2.setColor(hovered ? Color.WHITE : gold);
        g2.setStroke(new BasicStroke(hovered ? 2.5f : 1.5f));
        g2.draw(shape);

        /// Efect de stralucire la hover
        if (hovered) {
            g2.setColor(new Color(255, 255, 255, 50));
            g2.setStroke(new BasicStroke(5f));
            g2.draw(shape);
        }

        /// Text buton
        g2.setFont(new Font("Georgia", Font.ITALIC | Font.BOLD, 22));
        FontMetrics fm = g2.getFontMetrics();
        int textX = x + (w - fm.stringWidth(text)) / 2;
        int textY = y + (h + fm.getAscent() - fm.getDescent()) / 2;

        /// Umbra text
        g2.setColor(Color.BLACK);
        g2.drawString(text, textX + 1, textY + 1);

        /// Text principal
        g2.setColor(hovered ? Color.WHITE : textLight);
        g2.drawString(text, textX, textY);
    }
}