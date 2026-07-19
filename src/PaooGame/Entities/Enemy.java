package PaooGame.Entities;

import PaooGame.Game;
import java.awt.*;

/*! \class Enemy
    \brief Clasa abstracta de baza pentru toti inamicii din joc.

    Extinde Character si adauga logica comuna inamicilor:
    detectarea jucatorului, primirea de damage, atacul si resetul.
    Fiecare inamic concret (Archer, Goblin, Vrajitor) mosteneste aceasta clasa.
 */
public abstract class Enemy extends Character {

    protected int detection; ///< Distanta maxima la care inamicul detecteaza jucatorul (in pixeli).
    protected int damage;    ///< Damage-ul aplicat jucatorului la atac.
    protected boolean alive; ///< Starea inamicului: true daca este in viata, false daca a murit.
    protected int maxLife;   ///< Viata maxima a inamicului, folosita la reset.

    /*! \fn public Enemy(Game game, float x, float y, int width, int height)
        \brief Constructorul clasei Enemy.

        Initializeaza inamicul la pozitia specificata si il marcheaza ca fiind in viata.

        \param game   Referinta la instanta principala a jocului.
        \param x      Coordonata X initiala (in pixeli).
        \param y      Coordonata Y initiala (in pixeli).
        \param width  Latimea inamicului (in pixeli).
        \param height Inaltimea inamicului (in pixeli).
     */
    public Enemy(Game game, float x, float y, int width, int height) {
        super(game, x, y, width, height);
        this.alive    = true;
        this.initialX = x;
        this.initialY = y;
    }

    /*! \fn public abstract void takeDamage(int d)
    \brief Aplica damage inamicului.

    \param d Valoarea damage-ului de aplicat.
 */
    public abstract void takeDamage(int d);

    /*! \fn public abstract boolean playerDetection(Player player)
        \brief Verifica daca jucatorul se afla in raza de detectie a inamicului.

        \param player Referinta la jucatorul verificat.
        \return true daca jucatorul este detectat, false altfel.
     */
    public abstract boolean playerDetection(Player player);

    /*! \fn public boolean isAlive()
        \brief Verifica daca inamicul este in viata.
        \return true daca inamicul este in viata, false altfel.
     */
    public boolean isAlive() { return alive; }

    /*! \fn public Rectangle getBounds()
        \brief Returneaza dreptunghiul de coliziune al inamicului.
        \return Un obiect Rectangle cu pozitia si dimensiunile inamicului.
     */
    public Rectangle getBounds() { return new Rectangle((int) x, (int) y, width, height); }

    /*! \fn public abstract void Update()
        \brief Actualizeaza logica inamicului la fiecare frame.

        Implementata diferit de fiecare subclasa.
     */
    @Override
    public abstract void Update();

    /*! \fn public abstract void Draw(Graphics g)
        \brief Deseneaza inamicul pe ecran.

        Implementata diferit de fiecare subclasa.

        \param g Contextul grafic pe care se face desenarea.
     */
    @Override
    public abstract void Draw(Graphics g);

    /*! \fn public abstract void Attack(Entity target)
        \brief Logica de atac a inamicului asupra unei tinte.

        Implementata diferit de fiecare subclasa.

        \param target Entitatea tinta (jucatorul).
     */
    public abstract void Attack(Entity target);

    /*! \fn public void reset()
        \brief Reseteaza inamicul la starea initiala.

        Reface pozitia, viata maxima si il marcheaza din nou ca fiind in viata.
        Apelata cand moare jucatorul pentru a reinitializa nivelul.
     */
    public void reset() {
        this.x     = initialX;
        this.y     = initialY;
        this.life  = maxLife;
        this.alive = true;
    }

    /*! \fn public int getLife()
        \brief Returneaza viata curenta a inamicului.
        \return Viata curenta.
     */
    @Override
    public int getLife() { return life; }
}