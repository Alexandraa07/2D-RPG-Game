package PaooGame.Entities;

import PaooGame.Game;
import PaooGame.Graphics.Assets;
import java.awt.*;

/*! \class Diamond
    \brief Clasa care reprezinta obiectul de tip diamant.

    Diamantul este un obiect colectabil. Cand jucatorul se apropie suficient de el, dispare de pe harta si apare in inventarul jucatorului.
 */
public class Diamond extends Entity {

    private int diamondFrame = 0;  /// Frame-ul curent al animatiei diamantului.
    private int diamondTimer = 0;  /// Contor de timp pentru avansarea animatiei.
    private boolean active = true; /// Starea diamantului: true daca este inca pe harta, false daca a fost colectat.

    /*! \fn public Diamond(Game game, float x, float y)
        \brief Constructorul clasei Diamond.

        Initializeaza diamantul la pozitia specificata.

        \param game Referinta la instanta principala a jocului.
        \param x    Coordonata X initiala (in pixeli).
        \param y    Coordonata Y initiala (in pixeli).
     */
    public Diamond(Game game, float x, float y) {
        super(game, x, y, 32, 32);
    }

    /*! \fn public void Update()
        \brief Actualizeaza logica diamantului la fiecare frame.

        Daca diamantul este activ, verifica daca jucatorul s-a apropiat suficient pentru a-l colecta.
     */
    @Override
    public void Update() {
        if (!active)
            return;
        checkPickUp();
    }

    /*! \fn public void Draw(Graphics g)
        \brief Deseneaza diamantul daca este activ.

        Avanseaza animatia la fiecare 8 frame-uri si deseneaza sprite-ul corespunzator frame-ului curent.

        \param g Contextul grafic pe care se face desenarea.
     */
    @Override
    public void Draw(Graphics g) {
        if (!active) return;

        /// Avansam animatia diamantului
        diamondTimer++;
        if (diamondTimer >= 8) {
            diamondTimer = 0;
            diamondFrame = (diamondFrame + 1) % Assets.diamond.length;
        }

        g.drawImage(Assets.diamond[diamondFrame], (int) x, (int) y, width, height, null);
    }

    /*! \fn public boolean isActive()
        \brief Verifica daca diamantul este inca pe harta.
        \return true daca nu a fost colectat, false altfel.
     */
    public boolean isActive() { return active; }

    /*! \fn public void checkPickUp()
        \brief Verifica daca jucatorul este suficient de aproape pentru a colecta diamantul.

        Calculeaza distanta dintre diamant si jucator.
        Daca distanta este mai mica decat raza de colectare, diamantul devine inactiv.
     */
    public void checkPickUp() {
        double pickUpRange = 50.0; /// Raza maxima de colectare in pixeli.

        /// Calculam distanta dintre diamant si jucator
        double dx = x - game.getPlayer().getX();
        double dy = y - game.getPlayer().getY();
        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist < pickUpRange) {
            /// Jucatorul e suficient de aproape, colectam diamantul
            active = false;
            System.out.println("Diamant colectat");
        }
    }

    /*! \fn public void setActive(boolean active)
        \brief Seteaza starea diamantului.

        Folosita la incarcare din baza de date pentru a restaura
        starea diamantului salvata anterior.

        \param active true daca diamantul trebuie sa apara pe harta, false altfel.
     */
    public void setActive(boolean active) { this.active = active; }

    /*! \fn public void reset()
        \brief Reseteaza diamantul la starea initiala.

        Il face din nou activ si reporneste animatia de la primul frame.
     */
    public void reset() {
        this.active = true;
        this.diamondFrame = 0;
        this.diamondTimer = 0;
    }

    /*! \fn public int getLife()
        \brief Diamantul nu are viata proprie, returneaza intotdeauna 0.
        \return 0.
     */
    @Override
    public int getLife() { return 0; }

    /*! \fn public float getInitialX()
        \brief Returneaza coordonata X a diamantului.
        \return Coordonata X.
     */
    @Override
    public float getInitialX() { return x; }

    /*! \fn public float getInitialY()
        \brief Returneaza coordonata Y a diamantului.
        \return Coordonata Y.
     */
    @Override
    public float getInitialY() { return y; }
}