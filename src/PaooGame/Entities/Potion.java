package PaooGame.Entities;

import PaooGame.Game;
import PaooGame.Graphics.Assets;
import java.awt.*;

/*! \class Potion
    \brief Clasa care reprezinta un obiect de tip potiune din joc.

    Potion este o entitate colectabila de catre jucator. Atunci cand jucatorul
    se afla in raza de colectare, jucatorul o colecteaza
 */
public class Potion extends Entity {

    /// Indica daca potiunea este inca pe harta si poate fi colectata (true) sau a fost deja luata (false).
    private boolean active = true;

    /*! \fn public Potion(Game game, float x, float y)
        \brief Constructor al clasei Potion.

        Initializeaza potiunea la pozitia specificata.

        \param game   Referinta la instanta principala a jocului.
        \param x   Coordonata X initiala (in pixeli).
        \param y    Coordonata Y initiala (in pixeli).
     */
    public Potion(Game game, float x, float y) {
        super(game, x, y, 32, 32);
    }

    /*! \fn public void Update()
        \brief Actualizeaza logica potiunii la fiecare frame.

        Verifica daca potiunea este inca activa pe harta, apeleaza metoda de verificare a colectarii de catre jucator.
     */
    @Override
    public void Update() {
        //verificam daca inca nu a fost colectat
        if (!active)
            return;
        checkPickUp();
    }

    /*! \fn public void Draw(Graphics g)
        \brief Deseneaza potiunea pe ecran.

        Daca potiunea este activa (nu a fost colectata), deseneaza sprite-ul
        corespunzator la coordonatele potrivite.

        \param g Contextul grafic unde se face desenarea.
     */
    @Override
    public void Draw(Graphics g) {
        if (active) {
            g.drawImage(Assets.potion, (int) x, (int) y, width, height, null);
        }
    }

    /*! \fn public boolean isActive()
        \brief Returneaza starea de activitate a potiunii.

        \return True daca potiunea se afla inca pe harta.
     */
    public boolean isActive() {
        return active;
    }

    /*! \fn public void checkPickUp()
        \brief Verifica daca jucatorul este suficient de aproape pentru a colecta potiunea.

        Calculeaza distanta dintre centrul/pozitia potiunii si jucator.
        Daca distanta este mai mica decat raza de colectare (50 pixeli), potiunea devine
        inactiva, iar jucatorul primeste potiunea in inventar.
     */
    public void checkPickUp() {
        double pickUpRange = 50.0;
        //calculam distanta potiune-personaj
        double dx = x - game.getPlayer().getX();
        double dy = y - game.getPlayer().getY();

        double dist = (float) Math.sqrt(dx * dx + dy * dy);
        if (dist < pickUpRange) {
            active = false; //dispare potiunea de pe harta
            game.getPlayer().setArePotiune(true);
            System.out.println("Potiune colectata");
        }
    }

    /*! \fn public int getLife()
        \brief Returneaza viata entitatii.

        Potiunea fiind un obiect de colectat nu are viata

        \return Valoarea 0, deoarece potiunea nu are viata.
     */
    @Override public int getLife()       { return 0; }  // nu are viata

    /*! \fn public float getInitialX()
        \brief Returneaza coordonata X initiala a potiunii.

        \return Pozitia X curenta/initiala.
     */
    @Override public float getInitialX() { return x; }

    /*! \fn public float getInitialY()
        \brief Returneaza coordonata Y initiala a potiunii.

        \return Pozitia Y curenta/initiala.
     */
    @Override public float getInitialY() { return y; }

    /*! \fn public void setActive(boolean active)
        \brief Seteaza manual starea de activitate a potiunii.

        \param active Noua stare a potiunii (true pentru activa, false pentru colectata).
     */
    public void setActive(boolean active) { this.active = active; }

    /*! \fn public void reset()
        \brief Reseteaza potiunea la starea initiala.

        Face potiunea din nou activa si vizibila pe harta (de exemplu, la repornirea nivelului).
     */
    public void reset()
    {
        this.active= true;
    }
}