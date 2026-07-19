package PaooGame.Entities;

import PaooGame.Game;
import PaooGame.Graphics.Assets;
import java.awt.*;

/*! \class MagicAtack
    \brief Clasa care reprezinta proiectilul magic al vrajitorului.

    Vraja se misca pe o singura axa (sus, jos, stanga, dreapta) si dispare
    dupa ce parcurge distanta maxima sau dupa ce loveste jucatorul.
 */
public class MagicAtack extends Entity {

    private float startX, startY;       /// Coordonatele de start, folosite pentru calculul distantei parcurse.
    private float dx, dy;               /// Directia de deplasare pe X sau Y (nu simultan).
    private float speed = 10f;          /// Viteza de deplasare (pixeli/frame).
    private int distancelimit = 7 * 32; /// Distanta maxima parcursa inainte sa dispara (7 tile-uri).
    private boolean active = true;      /// Starea vrajii: true daca este inca in zbor, false daca a lovit sau a expirat.
    private int damage;                 /// Damage-ul aplicat jucatorului la lovire.

    private int magicFrame = 0; /// Frame-ul curent al animatiei magice.
    private int magicTimer = 0; /// Contor de timp pentru avansarea animatiei.

    /*! \fn public MagicAtack(Game game, float x, float y, float dx, float dy, int damage)
        \brief Constructorul clasei MagicAtack.

        Initializeaza vraja cu pozitia de start, directia de deplasare si damage-ul.

        \param game   Referinta la instanta principala a jocului.
        \param x      Coordonata X initiala (in pixeli).
        \param y      Coordonata Y initiala (in pixeli).
        \param dx     Componenta X a directiei (1 = dreapta, -1 = stanga, 0 = nu se misca pe X).
        \param dy     Componenta Y a directiei (1 = jos, -1 = sus, 0 = nu se misca pe Y).
        \param damage Damage-ul aplicat jucatorului la impact.
     */
    public MagicAtack(Game game, float x, float y, float dx, float dy, int damage) {
        super(game, x, y, 24, 24);
        this.startX = x;
        this.startY = y;
        this.dx = dx;
        this.dy = dy;
        this.damage = damage;
    }

    /*! \fn public void Update()
        \brief Actualizeaza logica vrajii la fiecare frame.

        Arunca vraja pe axa activa, verifica daca a depasit distanta maxima
        si verifica coliziunea cu jucatorul.
     */
    @Override
    public void Update() {
        if (!active) return;

        /// Miscam vraja in directia data
        x = x + dx * speed;
        y = y + dy * speed;

        /// Verificam daca vraja a depasit distanta maxima pe axa pe care se misca
        if (Math.abs(x - startX) > distancelimit || Math.abs(y - startY) > distancelimit) {
            active = false;
            return;
        }

        /// Verificam coliziunea cu jucatorul
        Player player = game.getPlayer();
        if (getBounds().intersects(player.getBounds())) {
            player.takeDamage(damage);
            active = false;
        }
    }

    /*! \fn public void Draw(Graphics g)
        \brief Deseneaza vraja animata daca este activa.

        Avanseaza animatia la fiecare 4 frame-uri si deseneaza
        sprite-ul corespunzator frame-ului curent.

        \param g Contextul grafic pe care se realizeaza desenarea.
     */
    @Override
    public void Draw(Graphics g) {
        if (!active) return;

        /// Avansam animatia magica
        magicTimer++;
        if (magicTimer >= 4) {
            magicTimer = 0;
            magicFrame = (magicFrame + 1) % Assets.magic.length;
        }

        g.drawImage(Assets.magic[magicFrame], (int) x, (int) y, width, height, null);
    }

    /*! \fn public boolean isActive()
        \brief Verifica daca vraja este inca activa in scena.
        \return true daca este activa, false daca a lovit sau a expirat.
     */
    public boolean isActive() { return active; }

    /*! \fn public Rectangle getBounds()
        \brief Returneaza dreptunghiul de coliziune al vrajii.
        \return Un obiect Rectangle cu pozitia si dimensiunile vrajii.
     */
    public Rectangle getBounds() { return new Rectangle((int) x, (int) y, width, height); }

    /*! \fn public int getLife()
        \brief Vraja nu are viata proprie, returneaza intotdeauna 0.
        \return 0.
     */
    @Override
    public int getLife() { return 0; }

    /*! \fn public float getInitialX()
        \brief Returneaza coordonata X de start a vrajii.
        \return Coordonata X initiala.
     */
    @Override
    public float getInitialX() { return startX; }

    /*! \fn public float getInitialY()
        \brief Returneaza coordonata Y de start a vrajii.
        \return Coordonata Y initiala.
     */
    @Override
    public float getInitialY() { return startY; }
}