package PaooGame.Entities;

import PaooGame.Game;
import PaooGame.Graphics.Assets;
import java.awt.*;

/*! \class Boomerang
    \brief Clasa care reprezinta proiectilul de tip boomerang al goblinului.

    Boomerangul zboara intr-o directie pentru un numar fix de frame-uri,dupa care se intoarce automat la goblin.
    Poate lovi jucatorul o singura data pe parcursul zborului.
 */
public class Boomerang extends Entity {

    private float vX, vY;   ///Viteza initiala pe X si Y (pixeli/frame).
    private int damage;   /// Damage-ul aplicat jucatorului la lovire.
    private boolean active;  /// Verifica daca boomerangul este activ
    private boolean returning;  /// Verifica daca boomerangul s-a intors spre goblin
    private boolean hasHit;  /// Verifica daca boomerangul a lovit jucatorul

    private int animFrame = 0;      /// Frame-ul curent al animatiei de rotatie.
    private int animTimer = 0;  /// Contor de timp pentru avansarea animatiei.
    private static final int ANIM_SPEED = 5; /// Numarul de frame-uri.

    private int flyTimer = 0;       /// Contor de timp cat zboara inainte.
    private static final int FLY_TIME = 25; /// Numarul de frame-uri cat zboara inainte sa se intoarca.

    private Goblin owner; /// Referinta la goblinul care a aruncat boomerangul.

    /*! \fn public Boomerang(Game game, float x, float y, float velX, float velY, int damage, Goblin owner)
        \brief Constructorul clasei Boomerang.

        Initializeaza boomerangul cu pozitia, viteza, damage-ul si ownerul sau.

        \param game   Referinta la instanta principala a jocului.
        \param x      Coordonata X initiala (in pixeli).
        \param y      Coordonata Y initiala (in pixeli).
        \param velX   Viteza pe axa X (pixeli/frame).
        \param velY   Viteza pe axa Y (pixeli/frame).
        \param damage Damage-ul aplicat la lovire.
        \param owner  Referinta la goblinul care a aruncat boommerangul.
     */
    public Boomerang(Game game, float x, float y, float velX, float velY, int damage, Goblin owner) {
        super(game, x, y, 32, 32);
        this.vX = velX;
        this.vY = velY;
        this.damage = damage;
        this.owner = owner;
        this.active = true;
        this.returning = false;
        this.hasHit = false;
    }

    /*! \fn public void Update()
        \brief Actualizeaza logica boomerangului la fiecare frame.

        Gestioneaza animatia de rotatie, miscarea inainte, intoarcerea
        spre goblin si dezactivarea la impact sau la iesirea din ecran.
     */
    @Override
    public void Update() {
        if (!active) return;

        /// Avansam animatia de rotatie
        animTimer++;
        if (animTimer >= ANIM_SPEED) {
            animTimer = 0;
            animFrame++;
            if (animFrame >= 8) {
                animFrame = 0;
            }
        }

        if (!returning) {
            /// Boomerangul zboara inainte in directia initiala
            x += vX;
            y += vY;
            flyTimer++;

            /// Dupa FLY_TIME frame-uri, incepe sa se intoarca
            if (flyTimer >= FLY_TIME) {
                returning = true;
            }
        } else {
            /// Calculam directia spre centrul goblinului
            float dx = (owner.getX() + owner.getWidth() / 2) - (x + width / 2);
            float dy = (owner.getY() + owner.getHeight() / 2) - (y + height / 2);
            float dist = (float) Math.sqrt(dx * dx + dy * dy);

            if (dist < 15) {
                /// A ajuns la goblin, il dezactivam
                active = false;
            } else {
                /// Se misca spre goblin cu viteza fixa de intoarcere
                x = x + (dx / dist) * 8.0f;
                y = y + (dy / dist) * 8.0f;
            }
        }

        /// Dezactivam boomerangul daca a iesit complet din zona jocului
        if (x < -5000 || x > 5000 || y < -5000 || y > 5000){
            active = false;
        }
    }

    /*! \fn public void Draw(Graphics g)
        \brief Deseneaza boomerangul daca este activ.

        \param g Contextul grafic pe care se face desenul.
     */
    @Override
    public void Draw(Graphics g) {
        if (!active) return;

        g.drawImage(Assets.boomerang_rotation[animFrame], (int) x, (int) y, width, height, null);
    }

    /*! \fn public boolean isActive()
        \brief Verifica daca boomerangul este activ
        \return true daca este activ, false altfel.
     */
    public boolean isActive() { return active; }

    /*! \fn public boolean canHit()
        \brief Verifica daca boomerangul poate lovi jucatorul.

        Returneaza true doar daca este activ si nu a mai lovit pe nimeni.
     */
    public boolean canHit() { return active && !hasHit; }

    /*! \fn public void registerHit()
        \brief Marcheaza faptul ca boomerangul a lovit deja jucatorul.

        Dupa aceasta apelare, canHit() va returna false.
     */
    public void registerHit() { hasHit = true; }

    /*! \fn public void deactivate()
        \brief Dezactiveaza bumerangul imediat.
     */

    public int getDamage() { return damage; }

    /*! \fn public int getLife()
        \brief Bumerangul nu are viata proprie, returneaza intotdeauna 0.
        \return 0.
     */

    @Override
    public int getLife() { return 0; }

    /*! \fn public float getInitialX()
        \brief Returneaza coordonata X curenta (folosita ca pozitie initiala).
        \return Coordonata X.
     */
    @Override
    public float getInitialX() { return x; }

    /*! \fn public float getInitialY()
        \brief Returneaza coordonata Y curenta (folosita ca pozitie initiala).
        \return Coordonata Y.
     */
    @Override
    public float getInitialY() { return y; }

    /*! \fn public Rectangle getBounds()
        \brief Returneaza dreptunghiul de coliziune al boomerangului.
        \return Un obiect Rectangle cu pozitia si dimensiunile boomerangului.
     */
    public Rectangle getBounds() { return new Rectangle((int) x, (int) y, width, height); }
}