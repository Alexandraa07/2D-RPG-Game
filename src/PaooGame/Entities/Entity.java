package PaooGame.Entities;

import PaooGame.Game;
import java.awt.*;

/*! \class Entity
    \brief Clasa abstracta de baza pentru toate obiectele din joc.

    Defineste proprietatile comune (pozitie, dimensiuni, referinta la joc)
    si metodele abstracte pe care fiecare entitate concreta trebuie sa le implementeze.
    Este mostenita de Character, Diamond, Arrow, Boomerang etc.
 */
public abstract class Entity {

    protected float x;   /// Pozitia curenta pe axa X in lume (in pixeli).
    protected float y;   /// Pozitia curenta pe axa Y in lume (in pixeli).
    public int width;    /// Latimea entitatii (in pixeli).
    public int height;   /// Inaltimea entitatii (in pixeli).
    protected Game game; /// Referinta la instanta principala a jocului.

    /*! \fn public Entity(Game game, float x, float y, int width, int height)
        \brief Constructor care initializeaza pozitia si dimensiunile entitatii.

        \param game   Referinta la instanta principala a jocului.
        \param x      Pozitia initiala pe axa X (in pixeli).
        \param y      Pozitia initiala pe axa Y (in pixeli).
        \param width  Latimea entitatii (in pixeli).
        \param height Inaltimea entitatii (in pixeli).
     */
    public Entity(Game game, float x, float y, int width, int height) {
        this.game   = game;
        this.x      = x;
        this.y      = y;
        this.width  = width;
        this.height = height;
    }

    /*! \fn public Entity(Game game)
        \brief Constructor care initializeaza doar referinta la joc.

        \param game Referinta la instanta principala a jocului.
     */
    public Entity(Game game) {
        this.game = game;
    }

    /*! \fn public abstract void Update()
        \brief Actualizeaza logica si miscarea entitatii la fiecare frame.

        Implementata diferit de fiecare subclasa.
     */
    public abstract void Update();

    /*! \fn public abstract void Draw(Graphics g)
        \brief Deseneaza entitatea pe ecran.

        Implementata diferit de fiecare subclasa.

        \param g Contextul grafic folosit pentru randare.
     */
    public abstract void Draw(Graphics g);

    /*! \fn public float getX()
        \brief Returneaza pozitia curenta pe axa X.
        \return Coordonata X (in pixeli).
     */
    public float getX() { return x; }

    /*! \fn public float getY()
        \brief Returneaza pozitia curenta pe axa Y.
        \return Coordonata Y (in pixeli).
     */
    public float getY() { return y; }

    /*! \fn public void setX(float x)
        \brief Seteaza pozitia pe axa X.
        \param x Noua valoare a coordonatei X (in pixeli).
     */
    public void setX(float x) { this.x = x; }

    /*! \fn public void setY(float y)
        \brief Seteaza pozitia pe axa Y.
        \param y Noua valoare a coordonatei Y (in pixeli).
     */
    public void setY(float y) { this.y = y; }

    /*! \fn public int getWidth()
        \brief Returneaza latimea entitatii.
        \return Latimea (in pixeli).
     */
    public int getWidth() { return width; }

    /*! \fn public int getHeight()
        \brief Returneaza inaltimea entitatii.
        \return Inaltimea (in pixeli).
     */
    public int getHeight() { return height; }

    /*! \fn public abstract int getLife()
        \brief Returneaza viata curenta a entitatii.
     */
    public abstract int getLife();

    /*! \fn public abstract float getInitialX()
        \brief Returneaza coordonata X initiala a entitatii.
        \return Coordonata X de start (in pixeli).
     */
    public abstract float getInitialX();

    /*! \fn public abstract float getInitialY()
        \brief Returneaza coordonata Y initiala a entitatii.
        \return Coordonata Y de start (in pixeli).
     */
    public abstract float getInitialY();
}