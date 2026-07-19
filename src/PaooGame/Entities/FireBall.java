package PaooGame.Entities;

import PaooGame.Game;
import PaooGame.Graphics.Assets;
import java.awt.*;

/*! \class FireBall
    \brief Clasa care reprezinta proiectilul de tip minge de foc al jucatorului.

    Mingea de foc se misca intr-o directie data, se dezactiveaza dupa
    ce parcurge distanta maxima sau dupa ce loveste un inamic.
 */
public class FireBall extends Entity {

    private float startX;        /// Coordonata X de start, folosita pentru a calcula distanta parcursa.
    private float speed = 10.0f;   /// Viteza de deplasare (pixeli/frame).
    private int distanceLimit = 7 * 32; /// Distanta maxima parcursa inainte sa dispara (7 tile-uri).
    private boolean active = true;   /// Starea mingii de foc: true daca este inca in zbor, false daca a lovit sau a expirat.

    private int fireFrame = 0; /// Frame-ul curent al animatiei de foc.
    private int fireTimer = 0; /// Contor de timp pentru avansarea animatiei.

    private float dirX, dirY; /// Directia de deplasare normalizata pe X si Y.

    /*! \fn public FireBall(Game game, float x, float y, float dirX, float dirY)
        \brief Constructorul clasei FireBall.

        Initializeaza mingea de foc cu pozitia de start si directia de deplasare.
     */
    public FireBall(Game game, float x, float y, float dirX, float dirY) {
        super(game, x, y, 24, 24);
        this.startX = x;
        this.dirX   = dirX;
        this.dirY   = dirY;
    }

    /*! \fn public void Update()
        \brief Actualizeaza logica mingii de foc la fiecare frame.

        Muta mingea de foc in directia specificata, verifica daca a depasit
        distanta maxima si verifica coliziunea cu inamicii.
     */
    @Override
    public void Update() {
        if (!active) return;

        /// Miscam mingea de foc in directia data
        x = x + dirX * speed;
        y = y + dirY * speed;

        /// Verificam daca a parcurs distanta maxima (7 tile-uri)
        if (Math.abs(x - startX) > distanceLimit)
        {
            active = false;
            return;
        }

        /// Verificam coliziunea cu fiecare inamic in viata
        for (Enemy enemy : game.getEnemies()) {
            if (enemy.isAlive() && getBounds().intersects(enemy.getBounds())) {
                enemy.takeDamage(10);
                active = false;
                return;
            }
        }
    }

    /*! \fn public void Draw(Graphics g)
        \brief Deseneaza mingea de foc animata daca este activa.

        Avanseaza animatia la fiecare 4 frame-uri si deseneaza
        sprite-ul corespunzator frame-ului curent.

        \param g Contextul grafic pe care se face desenarea.
     */
    @Override
    public void Draw(Graphics g) {
        if (!active) return;

        /// Avansam animatia de foc
        fireTimer++;
        if (fireTimer >= 4) {
            fireTimer = 0;
            fireFrame = (fireFrame + 1) % Assets.fireball_luma.length;
        }

        g.drawImage(Assets.fireball_luma[fireFrame], (int) x, (int) y, width, height, null);
    }

    /*! \fn public boolean isActive()
        \brief Verifica daca mingea de foc este inca activa.
        \return true daca este activa, false daca a lovit sau a expirat.
     */
    public boolean isActive() { return active; }

    /*! \fn public int getLife()
        \brief Mingea de foc nu are viata proprie, returneaza intotdeauna 0.
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

    /*! \fn public Rectangle getBounds()
        \brief Returneaza dreptunghiul de coliziune al mingii de foc.
        \return Un obiect Rectangle cu pozitia si dimensiunile mingii de foc.
     */
    public Rectangle getBounds() { return new Rectangle((int) x, (int) y, width, height); }
}