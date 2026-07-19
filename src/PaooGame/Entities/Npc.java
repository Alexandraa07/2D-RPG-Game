package PaooGame.Entities;

import PaooGame.Game;
import PaooGame.Graphics.Assets;
import java.awt.*;

/*! \class Npc
    \brief Clasa care reprezinta NPC

    NPC-ul afiseaza un mesaj de instructiuni la inceputul nivelului.
    Are o animatie simpla de doua cadre si dispare dupa ce jucatorul
    apasa tasta de confirmare a mesajului.
 */
public class Npc extends Character {

    private int contor = 0;       /// Contor pentru alternarea cadrelor de animatie.
    private boolean active = true; /// Starea NPC-ului: true daca mesajul nu a fost confirmat inca.
    private String instructions;   /// Mesajul de instructiuni afisat jucatorului.
    private int mapId;             /// Id-ul hartii pe care apare NPC-ul.

    /*! \fn public Npc(Game game, float x, float y, String m, int mapId)
        \brief Constructorul clasei Npc.

        Initializeaza NPC-ul cu pozitia, mesajul de afisat si harta pe care apare.

        \param game  Referinta la instanta principala a jocului.
        \param x     Coordonata X initiala (in pixeli).
        \param y     Coordonata Y initiala (in pixeli).
        \param m     Mesajul de instructiuni afisat jucatorului.
        \param mapId Id-ul hartii pe care este plasat NPC-ul.
     */
    public Npc(Game game, float x, float y, String m, int mapId) {
        super(game, x, y, 64, 64);
        this.instructions = m;
        this.mapId        = mapId;
    }

    /*! \fn public void Update()
        \brief Actualizeaza logica NPC-ului la fiecare frame.

        Avanseaza contorul de animatie si dezactiveaza NPC-ul
        daca jucatorul a apasat tasta de confirmare a mesajului.
     */
    @Override
    public void Update() {
        /// Avansam contorul de animatie si il resetam la 60
        contor++;
        if (contor >= 60) {
            contor = 0;
        }

        /// Daca jucatorul a confirmat mesajul, NPC-ul dispare
        if (active && game.getKeyManager().message) {
            active = false;
        }
    }

    /*! \fn public void Draw(Graphics g)
        \brief Deseneaza NPC-ul daca este activ.

        Alterneaza intre doua sprite-uri.

        \param g Contextul grafic pe care se realizeaza desenarea.
     */
    @Override
    public void Draw(Graphics g) {
        if (!active) return;

        /// Alternăm intre cele doua cadre ale animatiei
        if (contor < 30) {
            g.drawImage(Assets.npc1, (int) x, (int) y, width, height, null);
        } else {
            g.drawImage(Assets.npc2, (int) x, (int) y, width, height, null);
        }
    }

    /*! \fn public boolean isActive()
        \brief Verifica daca NPC-ul este inca activ (mesajul nu a fost confirmat).
        \return true daca NPC-ul este vizibil, false daca mesajul a fost confirmat.
     */
    public boolean isActive() { return active; }

    /*! \fn public String getInstructions()
        \brief Returneaza mesajul de instructiuni al NPC-ului.
        \return Instructiunea
     */
    public String getInstructions() { return instructions; }

    /*! \fn public int getMapId()
        \brief Returneaza id-ul hartii pe care este plasat NPC-ul.
        \return Id-ul hartii.
     */
    public int getMapId() { return mapId; }

    /*! \fn public void reset()
        \brief Reseteaza NPC-ul la starea initiala.
     */
    @Override
    public void reset() {
        super.reset();
        this.active = true;
        this.contor = 0;
    }

    /*! \fn public int getLife()
        \brief NPC-ul nu are viata proprie, returneaza intotdeauna 0.
     */
    @Override
    public int getLife() { return 0; }

    /*! \fn public float getInitialX()
        \brief Returneaza coordonata X initiala a NPC-ului.
     */
    @Override
    public float getInitialX() { return initialX; }

    /*! \fn public float getInitialY()
        \brief Returneaza coordonata Y initiala a NPC-ului.
     */
    @Override
    public float getInitialY() { return initialY; }
}