package PaooGame.Entities;

import PaooGame.Game;
import PaooGame.Graphics.Assets;

import java.awt.*;
import java.awt.event.KeyEvent;

/*! \class SpiritulApei
    \brief Clasa reprezinta o entitate de tip Spirit al Apei.

    Acest spirit este o entitate care nu ataca(asemnanator NPC). Trebuie sa fie salvata
    pentru a duce jocul la bun sfarsit.
 */
public class SpiritulApei extends Character
{
    /// Contor utilizat pentru alternarea cadrelor de animatie.
    private int contor = 0;

    /// Status care indica daca interactiunea cu spiritul este activa (se afiseaza casuta de dialog).
    private boolean active = false;

    /// Textul  pe care spiritul il afiseaza pentru jucator
    private String instructions;

    /// Status care retine daca jucatorul a citit si a inchis deja mesajul oferit de spirit.
    private boolean instructionRead = false;

    /*! \fn public SpiritulApei(Game game, float x, float y, String m)
        \brief Constructorul clasei SpiritulApei.

        Initializeaza pozitia spiritului pe harta si asociaza mesajul de instructiuni primit ca parametru.

     */
    public SpiritulApei(Game game, float x, float y, String m) {
        super(game, x, y, 64, 64);
        this.instructions = m;
    }

    /*! \fn public void Update()
        \brief Actualizeaza logica spiritului

        Gestioneaza resetarea contorului de animatie si verifica daca jucatorul a apasat
        tasta de interactiune pentru a inchide casuta de dialog.
     */
    @Override
    public void Update() {
        contor++;
        if (contor >= 60) {
            contor = 0;
        }
        if (active && game.getKeyManager().message) {
            active = false;
            instructionRead = true;
        }
    }

    /*! \fn public void Draw(Graphics g)
        \brief Deseneaza spiritul pe ecran.

        Alterneaza cele doua imagini (`apa1` si `apa2`) la un interval de 30 de frame-uri.

        \param g Contextul grafic pe care se face desenarea.
     */
    @Override
    public void Draw(Graphics g) {
        if (contor < 30) {
            g.drawImage(Assets.apa1, (int) x, (int) y, width, height, null);
        } else {
            g.drawImage(Assets.apa2, (int) x, (int) y, width, height, null);
        }
    }

    /*! \fn public void reset()
        \brief Reseteaza starea spiritului la configuratia initiala.
     */
    @Override
    public void reset() {
        super.reset();
        this.contor = 0;
        this.active = false;
        this.instructionRead = false;
    }

    /*! \fn public boolean isActive()
        \brief Verifica daca dialogul cu spiritul este activ in acest moment.
        \return True daca dialogul este deschis, altfel False.
     */
    public boolean isActive() {
        return active;
    }

    /*! \fn public String getInstructions()
        \brief Returneaza textul de instructiuni al spiritului.
        \return String-ul cu mesajul de dialog.
     */
    public String getInstructions() {
        return instructions;
    }

    /*! \fn public void setIsActive(boolean active)
        \brief Modifica statusul dialogului (pornit/oprit).
        \param active Noua stare a ferestrei de dialog.
     */
    public void setIsActive(boolean active){
        this.active = active;
    }

    /*! \fn public boolean isInstructionRead()
        \brief Verifica daca jucatorul a finalizat de citit instructiunile spiritului.
        \return True daca mesajul a fost citit, altfel fals
     */
    public boolean isInstructionRead() {
        return instructionRead;
    }

    /*! \fn public int getLife()
        \brief Returneaza viata spiritului.Vvaloarea este intotdeauna 0.
        \return Valoarea fixa 0.
     */
    @Override
    public int getLife() {
        return 0;
    }

    /*! \fn public float getInitialX()
        \brief Returneaza coordonata X initiala de spawn a spiritului.
        \return Pozitia pe axa X.
     */
    @Override
    public float getInitialX() {
        return x;
    }

    /*! \fn public float getInitialY()
        \brief Returneaza coordonata Y initiala de spawn a spiritului.
        \return Pozitia pe axa Y.
     */
    @Override
    public float getInitialY() {
        return y;
    }
}