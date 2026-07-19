package PaooGame.Entities;

import PaooGame.Game;
import java.awt.*;

/*! \class Character
    \brief Clasa abstracta de baza pentru toate entitatile cu viata din joc.

    Extinde Entity si adauga proprietati comune precum viata, viteza
    si pozitia initiala. Este mostenita de Player, Enemy, NPC si celelalte 2 spirite.
 */
public abstract class Character extends Entity {

    protected int life;  /// Viata curenta a personajului.
    protected float speed;  /// Viteza de deplasare a personajului.
    protected float initialX; /// Coordonata X initiala, folosita la reset.
    protected float initialY; /// Coordonata Y initiala, folosita la reset.

    /*! \fn public Character(Game game, float x, float y, int width, int height)
        \brief Constructorul clasei Character.

        Initializeaza pozitia si retine coordonatele initiale pentru reset.

        \param game Referinta la instanta principala a jocului.
        \param x     Coordonata X initiala (in pixeli).
        \param y     Coordonata Y initiala (in pixeli).
        \param width  Latimea personajului (in pixeli).
        \param height Inaltimea personajului (in pixeli).
     */
    public Character(Game game, float x, float y, int width, int height) {
        super(game, x, y, width, height);
        this.initialX = x;
        this.initialY = y;
    }

    /*! \fn public void reset()
        \brief Reseteaza pozitia personajului la coordonatele initiale.

        Apelata cand moare jucatorul, pentru a repune toti inamicii si NPC-urile la locul lor de start.
     */
    public void reset() {
        this.x = initialX;
        this.y = initialY;
    }

    /*! \fn public abstract void Update()
        \brief Actualizeaza logica personajului la fiecare frame.
     */
    @Override
    public abstract void Update();

    /*! \fn public abstract void Draw(Graphics g)
        \brief Deseneaza Caracterul pe ecran.

        \param g Contextul grafic pe care se realizeaza desenarea.
     */
    @Override
    public abstract void Draw(Graphics g);

    /*! \fn public void setLife(int life)
        \brief Seteaza viata curenta a Caracterului.

        \param life Noua valoare a vietii.
     */
    public void setLife(int life) {
        this.life = life;
    }

    /*! \fn public abstract int getLife()
        \brief Returneaza viata curenta a caracterului.

        \return Viata curenta.
     */
    @Override
    public abstract int getLife();

    /*! \fn public float getInitialX()
        \brief Returneaza coordonata X initiala a caracterului.
        \return Coordonata X de start.
     */
    @Override
    public float getInitialX() { return initialX; }

    /*! \fn public float getInitialY()
        \brief Returneaza coordonata Y initiala a caracterului.
        \return Coordonata Y de start.
     */
    @Override
    public float getInitialY() { return initialY; }
}