package PaooGame.Input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*! \class KeyManager
    \brief Clasa gestioneaza input-ul de la tastatura.

    Clasa implementeaza interfata KeyListener si retine
    starea tastelor utilizate pentru miscarea jucatorului,
    atac si interactiuni.
 */
public class KeyManager implements KeyListener {

    /// Vector ce retine starea fiecarei taste.
    private boolean[] keys;

    /// Taste pentru miscare si interactiuni.
    public boolean up, down, left, right, message, esc, place;

    /// Taste pentru atac pe diferite directii.
    public boolean atack_left, atack_right, atack_up, atack_down;

    /*! \fn public KeyManager()
        \brief Constructorul clasei KeyManager.

        Initializeaza vectorul de taste.
     */
    public KeyManager() {
        keys = new boolean[256];
    }

    /*! \fn public void Update()
        \brief Actualizeaza starea tastelor utilizate in joc.

        Verifica tastele apasate si actualizeaza variabilele
        folosite pentru miscare, atac si interactiuni.
     */
    public void Update() {

        /// Tastele W, A, S, D controleaza miscarea.
        up = keys[KeyEvent.VK_W];
        down = keys[KeyEvent.VK_S];
        left = keys[KeyEvent.VK_A];
        right = keys[KeyEvent.VK_D];

        /// Tasta M afiseaza mesaje
        message = keys[KeyEvent.VK_M];

        /// Sagetile controleaza atacul.
        atack_left = keys[KeyEvent.VK_LEFT];
        atack_right = keys[KeyEvent.VK_RIGHT];
        atack_up = keys[KeyEvent.VK_UP];
        atack_down = keys[KeyEvent.VK_DOWN];

        /// Tasta ESC se ocupa de salvarea jocului
        esc = keys[KeyEvent.VK_ESCAPE];

        /// Tasta E este folosita pentru plasarea diamantelor
        place = keys[KeyEvent.VK_E];
    }

    /*! \fn public void keyPressed(KeyEvent e)
        \brief Marcheaza o tasta ca fiind apasata.

        \param e Evenimentul generat la apasarea tastei.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    /*! \fn public void keyReleased(KeyEvent e)
        \brief Marcheaza o tasta ca fiind eliberata.

        \param e Evenimentul generat la eliberarea tastei.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    /*! \fn public void keyTyped(KeyEvent e)
        \brief Metoda apelata la tastarea unui caracter.

        \param e Evenimentul generat la tastarea caracterului.
     */
    @Override
    public void keyTyped(KeyEvent e) {}
}