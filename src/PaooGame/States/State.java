package PaooGame.States;

import java.awt.*;

/*! \class State
    \brief Clasa abstracta de baza pentru starile jocului.

    Gestioneaza starea curenta activa a jocului prin metode statice.
    Fiecare stare concreta (meniu, joc, instructiuni) extinde aceasta clasa
    si implementeaza metodele Update() si Draw().
*/
public abstract class State {

    private static State currentState = null; /// Starea curenta activa a jocului

    /*! \fn public static void setState(State s)
        \brief Seteaza starea curenta a jocului.
        \param s Noua stare care devine activa.
    */
    public static void setState(State s) {
        currentState = s;
    }

    /*! \fn public static State getState()
        \brief Returneaza starea curenta a jocului.
        \return Referinta catre starea curenta.
    */
    public static State getState() {
        return currentState;
    }

    /*! \fn public abstract void Update()
        \brief Actualizeaza logica starii curente.
    */
    public abstract void Update();

    /*! \fn public abstract void Draw(Graphics g)
        \brief Deseneaza continutul starii curente.
        \param g Contextul grafic.
    */
    public abstract void Draw(Graphics g);
}