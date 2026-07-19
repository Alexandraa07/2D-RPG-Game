package PaooGame.Entities;

import PaooGame.Game;
import PaooGame.Graphics.Assets;
import java.awt.*;

/*! \class Piedestal
    \brief Clasa care reprezinta un piedestal pentru diamante.

    Piedestalul poate primi un diamant colectat de jucator.
    Dupa plasarea diamantului, piedestalul devine activ si isi
    porneste animatia. Cand toate piedestalele sunt completate,
    este activata potiunea speciala a jucatorului.
 */
public class Piedestal extends Entity {

    private boolean active = false;   /// Indica daca piedestalul este activ.
    private boolean hasDiamond = false; /// Indica daca diamantul a fost plasat.
    private int diamondSlot;      /// Specifica diamantul asociat piedestalului.

    private int diamondFrame = 0; /// Cadrul curent al animatiei diamantului.
    private int diamondTimer = 0; /// Timer pentru animatia diamantului.

    private int animFrame = 0; /// Cadrul curent al animatiei piedestalului.
    private int animTimer = 0; /// Timer pentru animatia piedestalului.

    private static final int ANIM_SPEED = 5;        /// Viteza animatiei piedestalului.
    private static final double INTERACT_RANGE = 60.0; /// Distanta maxima de interactiune cu jucatorul.

    /*! \fn public Piedestal(Game game, float x, float y, int diamondSlot)
        \brief Constructorul clasei Piedestal.

        Initializeaza piedestalul cu pozitia sa si diamantul asociat.

        \param game Referinta la instanta principala a jocului.
        \param x Coordonata X initiala.
        \param y Coordonata Y initiala.
        \param diamondSlot Slotul diamantului asociat acestui piedestal.
     */
    public Piedestal(Game game, float x, float y, int diamondSlot) {
        super(game, x, y, 32, 40);
        this.diamondSlot = diamondSlot;
    }

    /*! \fn public void Update()
        \brief Actualizeaza animatiile piedestalului si diamantului.

        Controleaza animatia diamantului plasat si animatia
        piedestalului activ.
     */
    @Override
    public void Update() {

        /// Actualizam animatia diamantului
        if (hasDiamond) {
            diamondTimer++;

            if (diamondTimer >= 8) {
                diamondTimer = 0;
                diamondFrame = (diamondFrame + 1) % Assets.diamond.length;
            }
        }

        /// Actualizam animatia piedestalului activ
        if (active) {
            animTimer++;

            if (animTimer >= ANIM_SPEED) {
                animTimer = 0;
                animFrame = (animFrame + 1) % 8;
            }
        }
    }

    /*! \fn public void Draw(Graphics g)
        \brief Deseneaza piedestalul si diamantul.

        Afiseaza mesajul de interactiune daca jucatorul este aproape
        si are diamantul necesar.

        \param g Contextul grafic utilizat pentru desenare.
     */
    @Override
    public void Draw(Graphics g) {

        /// Afisam mesajul de interactiune
        if (!hasDiamond && playerDetection() && isDiamondCollected()) {
            drawText(g);
        }

        /// Desenam piedestalul
        if (active) {
            g.drawImage(Assets.piedestal_activ[animFrame], (int) x, (int) y, width, height, null);
        } else {
            g.drawImage(Assets.piedestal, (int) x, (int) y, width, height, null);
        }

        /// Desenam diamantul daca exista
        if (hasDiamond) {
            g.drawImage(Assets.diamond[diamondFrame], (int) x, (int) y - 30, 32, 32, null);
        }
    }

    /*! \fn public void tryPlaceDiamond()
        \brief Incearca sa plaseze diamantul pe piedestal.

        Daca jucatorul este suficient de aproape si a colectat
        diamantul corespunzator, acesta este plasat pe piedestal cu tasta E.
     */
    public void tryPlaceDiamond() {

        if (!hasDiamond && playerDetection() && isDiamondCollected()) {

            hasDiamond = true;
            active = true;

            /// Verificam daca toate piedestalele sunt completate
            if (piedestalFull()) {

                System.out.println("Toate cele 3 diamante sunt pe piedestal!");

                /// Activam potiunea speciala
                game.getPlayer().activeazaPotiune();
            }
        }
    }

    /*! \fn private boolean playerDetection()
        \brief Verifica daca jucatorul este aproape de piedestal.
     */
    private boolean playerDetection() {

        double dx = x - game.getPlayer().getX();
        double dy = y - game.getPlayer().getY();

        return Math.sqrt(dx * dx + dy * dy) < INTERACT_RANGE;
    }

    /*! \fn private boolean isDiamondCollected()
        \brief Verifica daca diamantul asociat a fost colectat.
     */
    private boolean isDiamondCollected() {

        switch (diamondSlot) {

            case 1:
                return !game.getDiamond1().isActive();

            case 2:
                return !game.getDiamond2().isActive();

            case 3:
                return !game.getDiamond3().isActive();

            default:
                return false;
        }
    }

    /*! \fn private void drawText(Graphics g)
        \brief Afiseaza mesajul de interactiune.

        \param g Contextul grafic utilizat pentru desenare.
     */
    private void drawText(Graphics g) {

        String msg = "Apasa [E] pentru a pune diamantul!";

        int boxW = 220, boxH = 25;
        int boxX = (int) x - boxW / 2 + (width / 2);
        int boxY = (int) y - 50;

        g.setColor(new Color(0, 0, 0, 180));
        g.fillRoundRect(boxX, boxY, boxW, boxH, 10, 10);

        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 11));

        g.drawString(msg, boxX + 10, boxY + 17);
    }

    /*! \fn private boolean piedestalFull()
        \brief Verifica daca toate piedestalele au diamante.

        \return true daca toate cele 3 piedestale sunt completate.
     */
    private boolean piedestalFull() {

        return game.getPiedestal1().hasDiamond() && game.getPiedestal2().hasDiamond() && game.getPiedestal3().hasDiamond();
    }

    /*! \fn public boolean hasDiamond()
        \brief Verifica daca piedestalul contine un diamant.

        \return true daca diamantul este plasat.
     */
    public boolean hasDiamond() {
        return hasDiamond;
    }

    /*! \fn public boolean isActive()
        \brief Verifica daca piedestalul este activ.

        \ true daca piedestalul este activ.
     */
    public boolean isActive() {
        return active;
    }

    /*! \fn public int getLife()
        \brief Piedestalul nu are viata proprie.
     */
    @Override
    public int getLife() {
        return 0;
    }

    /*! \fn public float getInitialX()
        \brief Returneaza coordonata initiala X.

     */
    @Override
    public float getInitialX() {
        return x;
    }

    /*! \fn public float getInitialY()
        \brief Returneaza coordonata initiala Y.

        \return Coordonata initiala Y.
     */
    @Override
    public float getInitialY() {
        return y;
    }

    /*! \fn public void reset()
        \brief Reseteaza piedestalul la starea initiala.
     */
    public void reset() {

        this.active = false;
        this.hasDiamond = false;

        this.diamondFrame = 0;
        this.diamondTimer = 0;

        this.animFrame = 0;
        this.animTimer = 0;
    }
}