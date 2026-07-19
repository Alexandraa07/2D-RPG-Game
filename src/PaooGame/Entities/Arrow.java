package PaooGame.Entities;

import PaooGame.Game;
import PaooGame.Graphics.Assets;
import java.awt.*;

/*! \class Arrow
    \brief Clasa care reprezinta proiectilul de tip sageata al arcasului si al protectorilor.

    Sageata se misca pe orizontala si se dezactiveaza cand loveste jucatorul sau iese din limitele hartii curente.
 */
public class Arrow extends Entity {

    private float vitezaX; /// Viteza de deplasare pe axa X (pixeli/frame).
    private float vitezaY; /// Viteza de deplasare pe axa Y (pixeli/frame).
    private int damage;    /// Damage-ul aplicat jucatorului la lovire.
    private boolean active; /// Starea sagetii: true daca este inca in zbor, false daca a lovit sau a iesit din harta.

    /*! \fn public Arrow(Game game, float x, float y, float vitezaX, float vitezaY, int damage)
        \brief Constructorul clasei Arrow.

        Initializeaza sageata cu pozitia de start, viteza si damage-ul.

        \param game    Referinta la instanta principala a jocului.
        \param x       Coordonata X initiala (in pixeli).
        \param y       Coordonata Y initiala (in pixeli).
        \param vitezaX Viteza pe axa X (pixeli/frame).
        \param vitezaY Viteza pe axa Y (pixeli/frame).
        \param damage  Damage-ul aplicat jucatorului la impact.
     */
    public Arrow(Game game, float x, float y, float vitezaX, float vitezaY, int damage) {
        super(game, x, y, 64, 64);
        this.vitezaX = vitezaX;
        this.vitezaY = vitezaY;
        this.damage  = damage;
        this.active  = true;
    }

    /*! \fn public void Update()
        \brief Actualizeaza logica sagetii la fiecare frame.

        Muta sageata pe harta, verifica coliziunea cu jucatorul si o dezactiveaza daca iese din limitele hartii curente.
     */
    @Override
    public void Update() {
        if (!active) return;

        /// Miscam sageata pe harta
        x = x + vitezaX;
        y = y + vitezaY;

        /// Verificam coliziunea cu jucatorul
        Player player = game.getPlayer();
        if (getBounds().intersects(player.getBounds())) {
            player.takeDamage(damage);
            active = false;
        }

        /// Stabilim dimensiunile hartii in functie de harta curenta
        /// Harta 1: 90x60 tile-uri, Harta 3: 120x80 tile-uri
        int mapWidth  = 90 * 32;
        int mapHeight = 60 * 32;

        if (game.getCurrentMap() == 3) {
            mapWidth  = 120 * 32;
            mapHeight = 80  * 32;
        }

        /// Dezactivam sageata daca a iesit din limitele hartii
        if (x < 0 || x > mapWidth || y < 0 || y > mapHeight) {
            active = false;
        }
    }

    /*! \fn public void Draw(Graphics g)
        \brief Deseneaza sageata pe ecran daca este activa.

        \param g Contextul grafic pe care se face desenarea.
     */
    @Override
    public void Draw(Graphics g) {
        if (!active) return;

        g.drawImage(Assets.arrow, (int) x, (int) y, width, height, null);
    }

    /*! \fn public int getLife()
        \brief Sageata nu are viata proprie, returneaza intotdeauna 0.
        \return 0.
     */
    @Override
    public int getLife() { return 0; }

    /*! \fn public float getInitialX()
        \brief Returneaza coordonata X curenta.
        \return Coordonata X.
     */
    @Override
    public float getInitialX() { return x; }

    /*! \fn public float getInitialY()
        \brief Returneaza coordonata Y curenta.
        \return Coordonata Y.
     */
    @Override
    public float getInitialY() { return y; }

    /*! \fn public boolean isActive()
        \brief Verifica daca sageata este inca activa
        \return true daca sageata zboara, false daca a lovit sau a iesit din harta.
     */
    public boolean isActive() { return active; }

    /*! \fn public Rectangle getBounds()
        \brief Returneaza dreptunghiul de coliziune al sagetii.
        \return Un obiect Rectangle cu pozitia si dimensiunile sagetii.
     */
    public Rectangle getBounds() { return new Rectangle((int) x, (int) y, width, height); }
}