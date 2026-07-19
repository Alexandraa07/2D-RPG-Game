package PaooGame.Entities;

import PaooGame.Game;
import PaooGame.Graphics.Assets;
import java.awt.*;

/*! \class Thunder
    \brief Clasa reprezinta capcana de tip Fulger.

    Fulger -> Foc
 */
public class Thunder extends Entity {

    /// Damage de dat.
    private int damage;

    /// Cadru animatie fulger.
    private int thunderFrame = 0;

    /// Timer animatie fulger.
    private int thunderTimer = 0;

    /// Status activare fulger.
    private boolean active = true;
    /// Flag coliziune unica per fulger.
    private boolean hitPlayer = false;

    /// Cooldown reaparitie.
    private int cooldown = 0;

    /// Cadru animatie foc.
    private int fireFrame = 0;
    /// Timer animatie foc.
    private int fireTimer = 0;
    /// Status ardere foc.
    private boolean fireBurning = false;
    /// Durata ardere foc.
    private int fireDuration = 0;

    /// Cadre totale fulger.
    private static final int TOTAL_FRAMES = 4;
    /// Viteza animatie fulger.
    private static final int TICKS_PER_FRAME = 4;

    /// Cadre totale foc.
    private static final int FIRE_FRAMES = 8;
    /// Viteza animatie foc.
    private static final int FIRE_TICKS_PER_FRAME = 6;
    /// Durata totala foc.
    private static final int fire_time = 120;
    /// Cooldown total capcana.
    private static final int cooldownFT = 150;

    /*! \fn public Thunder(Game game, float x, float y, int damage)
        \brief Constructorul clasei Thunder.
        \param game Referinta joc.
        \param x Pozitie X.
        \param y Pozitie Y.
        \param damage Damage dat.
     */
    public Thunder(Game game, float x, float y, int damage) {
        super(game, x, y, 64, 256);
        this.damage = damage;
    }

    /*! \fn public void Update()
        \brief Actualizeaza logica si starile capcanei (Fulger -> Foc -> Cooldown).
     */
    @Override
    public void Update() {

        if (!active && !fireBurning) {
            cooldown++;
            if (cooldown >= cooldownFT) {
                active = true;
                cooldown = 0;
                thunderFrame = 0;
                thunderTimer = 0;
                hitPlayer = false;
            }
            return;
        }

        if (fireBurning) {
            fireTimer++;
            fireDuration++;

            if (fireTimer >= FIRE_TICKS_PER_FRAME) {
                fireTimer = 0;
                fireFrame = (fireFrame + 1) % FIRE_FRAMES;
            }

            Player player = game.getPlayer();
            Rectangle fireHitbox = new Rectangle((int)x + 16, (int)y + 210, 64, 40);
            Rectangle playerBounds = new Rectangle((int)player.getX(), (int)player.getY(), 64, 64);

            if (fireHitbox.intersects(playerBounds)) {
                if (fireDuration % 60 == 0) {
                    player.takeDamage(damage);
                }
            }

            if (fireDuration >= fire_time) {
                fireBurning = false;
                fireDuration = 0;
                fireFrame = 0;
                cooldown = 0;
            }
            return;
        }

        thunderTimer++;
        if (thunderTimer >= TICKS_PER_FRAME) {
            thunderTimer = 0;
            thunderFrame++;

            if (thunderFrame >= TOTAL_FRAMES) {
                active = false;
                thunderFrame = 0;
                fireBurning = true;
                fireFrame = 0;
                fireTimer = 0;
                fireDuration = 0;
            }
        }

        Player player = game.getPlayer();
        Rectangle bounds = new Rectangle((int)x + 32, (int)y + 190, 64, 50);
        Rectangle playerBounds = new Rectangle((int)player.getX(), (int)player.getY(), 64, 64);

        if (bounds.intersects(playerBounds) && !hitPlayer) {
            player.takeDamage(damage);
            hitPlayer = true;
        }
    }

    /*! \fn public void Draw(Graphics g)
        \brief Deseneaza fulgerul sau focul de dupa.
        \param g Contextul grafic.
     */
    @Override
    public void Draw(Graphics g) {
        if (active) {
            g.drawImage(Assets.thunder[thunderFrame], (int)x, (int)y, width, height, null);
        } else if (fireBurning) {
            g.drawImage(Assets.fire[fireFrame], (int)x + 16, (int)y + 200, 32, 48, null);
        }
    }

    /*! \fn public boolean isActive()
        \brief Returneaza starea activa a fulgerului.
        \return True/False.
     */
    public boolean isActive() {
        return active;
    }

    /*! \fn public int getLife()
        \brief Returneaza viata entitatii (0 pentru capcane).
        \return 0.
     */
    @Override public int getLife() { return 0; }

    /*! \fn public float getInitialX()
        \brief Returneaza coordonata X initiala.
        \return Pozitia X.
     */
    @Override public float getInitialX() { return x; }

    /*! \fn public float getInitialY()
        \brief Returneaza coordonata Y initiala.
        \return Pozitia Y.
     */
    @Override public float getInitialY() { return y; }
}