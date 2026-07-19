package PaooGame.Entities;

import PaooGame.Game;
import PaooGame.Graphics.Assets;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/*! \class Archer
    \brief Clasa care reprezinta inamicul de tip arcas

    Arcasul patruleaza pe verticala si, atunci cand
    detecteaza jucatorul pe orizontala, se oprește si trage sageti
    Atunci cand ramane fara viata, se activeaza animatia de moarte
 */
public class Archer extends Enemy {

    /// Lista sageților trase de arcaș
    private ArrayList<Arrow> arrows = new ArrayList<>();

    /// Viteza (pixeli/frame) a unei sageti
    private float arrowSpeed;

    /// Numarul de frame-uri de asteptare intre două trageri consecutive.
    private int shootCoolDown;

    /// Cooldown-ul curent, decrementat la fiecare frame.
    private int currentCooldown;

    /// Frame-ul curent al animației (atac si mers).
    private int animFrame = 0;

    /// Contor de timp folosit pentru avansarea animației.
    private int animTimer = 0;

    /// Numarul de frame-uri la care se avansează un cadru de animație.
    private static final int ANIM_SPEED = 5;

    /// Indica dacă arcașul a murit sau nu.
    private boolean dying = false;

    /// Frame-ul curent al animației de moarte.
    private int deathFrame = 0;

    /// Contor de timp pentru animatia de moarte.
    private int deathTimer = 0;

    /// Numarul de frame-uri la care se avanseaza un cadru din animatia de moarte.
    private static final int DEATH_ANIM_SPEED = 20;

    /// Coordonata Y de start, folosita pentru patrulare
    private float startY;

    /// Viteza de patrulare pe verticala (pixeli/frame).
    private float patrolSpeed = 1.5f;

    /// Directia curenta de patrulare: true = jos, false = sus.
    private boolean movingDown = true;

    /// Numarul de tile-uri patrulate in fiecare directe
    private static final int PATROL_TILES = 3;

    /// Dimensiunea unui tile în pixeli.
    private static final int TILE = 32;

    /*!  \fn public Archer(Game game, float x, float y)
         \brief Constructor al clasei Archer.

          Inițializeaza arcasul(viata, damage, raza de detectie)
          si parametrii de tragere (viteză sageata, cooldown).

          \param game   Referința la instanța principala a jocului.
          \param x      Coordonata X inițiala (în pixeli).
          \param y      Coordonata Y inițiala (în pixeli).
     */
    public Archer(Game game, float x, float y) {
        super(game, x, y, 64, 64);
        this.maxLife = 100;
        this.life = 100;
        this.damage = 1;
        this.detection = 400;

        this.arrowSpeed = 7.0f;
        this.shootCoolDown = 60;
        this.currentCooldown = 0;
        this.startY = y;
    }


    /*! \fn public void Update()
        \brief Actualizeaza logica arcașului la fiecare frame.

         Gestioneaza animatia de moarte, cooldown-ul de tragere, detectia jucatorului
     */
    @Override
    public void Update() {

        if (dying) {
            deathTimer++;
            if (deathTimer >= DEATH_ANIM_SPEED) {
                deathTimer = 0;
                deathFrame++;
                if (deathFrame >= Assets.dead_Archer.length) {
                    alive = false;
                }
            }
            return;
        }

        if (currentCooldown > 0)
            currentCooldown--;

        Player player = game.getPlayer();

        if (playerDetection(player)) {
            // Jucatorul este detectat, arcasul sta pe loc și ataca.

            animTimer++;
            if (animTimer >= ANIM_SPEED) {
                animTimer = 0;
                animFrame = (animFrame + 1) % Assets.archer_attack.length;
            }

            // Trage doar cand animatia ajunge la frame-ul 6 și cooldown-ul a expirat.
            if (currentCooldown == 0 && animFrame == 6) {
                Attack(player);
            }

        } else {
            // Jucatorul nu este detectat → arcașul patrulează pe verticală.

            if (movingDown) {
                y = y + patrolSpeed;
                if (y >= startY + PATROL_TILES * TILE)
                    movingDown = false;
            } else {
                y = y - patrolSpeed;
                if (y <= startY - PATROL_TILES * TILE)
                    movingDown = true;
            }

            animTimer++;
            if (animTimer >= ANIM_SPEED) {
                animTimer = 0;
                animFrame = (animFrame + 1) % Assets.walk_upArcher.length;
            }
        }

        for (Arrow a : arrows)
            a.Update();

        arrows.removeIf(a -> !a.isActive());

        for (Arrow a : arrows) {
            if (a.isActive() && a.getBounds().intersects(player.getBounds())) {
                player.takeDamage(damage);
            }
        }
    }

    /*! \fn public void Draw(Graphics g)
        \brief Desenează arcașul și elementele sale asociate.

        Alege sprite-ul potrivit pentru starea curenta (moarte, atac, mers sus/jos),
        deseneaza toate sagețile si bara de viața a inamicului

        \param g Contextul grafic unde se realizeaza desenarea.
     */
    @Override
    public void Draw(Graphics g) {

        if (dying) {
            g.drawImage(Assets.dead_Archer[Math.min(deathFrame, Assets.dead_Archer.length - 1)], (int) x, (int) y, width, height, null);
            return;
        }

        if (playerDetection(game.getPlayer())) {
            g.drawImage(Assets.archer_attack[animFrame % Assets.archer_attack.length], (int) x, (int) y, width, height, null);
        } else {
            if (movingDown) {
                g.drawImage(Assets.walk_downArcher[animFrame % Assets.walk_downArcher.length], (int) x, (int) y, width, height, null);
            } else {
                g.drawImage(Assets.walk_upArcher[animFrame % Assets.walk_upArcher.length], (int) x, (int) y, width, height, null);
            }
        }

        for (Arrow a : arrows)
            a.Draw(g);

        // Bara de viata: initial verde, iar cand primeste damage ii ramane in fundal linia rosie de viata
        g.setColor(Color.RED);
        g.fillRect((int) x, (int) y - 10, width, 5);

        g.setColor(Color.GREEN);
        int bar = (int) ((life / (float) maxLife) * width);
        g.fillRect((int) x, (int) y - 10, bar, 5);
    }

    /*! \fn public void Attack(Entity target)
        \brief Creeaza si lanseaza o sageata în directia tintei.

         Sageata se deplasează pe orizontală (vX = 0).
         Directia (stanga/dreapta) este determinata prin compararea pozitiilor X.
         După tragere, cooldown-ul este resetat la valoarea de shootCoolDown.

         \param target Entitatea tintă (jucatorul).
     */
    @Override
    public void Attack(Entity target) {
        float vX;
        if(target.getX() > x) {
            vX = arrowSpeed;
        } else {
            vX = -arrowSpeed;
        }

        arrows.add(new Arrow(game, x, y, vX, 0, damage));
        currentCooldown = shootCoolDown;
    }

    /*! \public void takeDamage
        \brief Verifica daca arcasul este mort, iar daca nu isi ia damage si i se declanseaza moartea dacă viata ajunge la 0.

        \param damage Valoarea damage-ului
     */
    @Override
    public void takeDamage(int damage) {
        if (dying || !alive) return;

        life = life - damage;

        if (life <= 0) {
            life = 0;
            dying = true;
            deathFrame = 0;
            deathTimer = 0;
        }
    }

    /*! \public boolean playerDetection
        \brief Verifica dacă arcasul detecteaza jucatorul

         Detectia are loc daca diferența pe Y dintre centrele celor două entitati este ≤ 30 pixeli
         si diferenta pe X este detection

         \param player Referința la jucator
         \return Este true daca jucatorul este în campul vizual al arcasului
     */
    public boolean playerDetection(Player player) {
        float diffX = Math.abs((x + width / 2f) - (player.getX() + player.width / 2f));
        float diffY = Math.abs((y + height / 2f) - (player.getY() + player.height / 2f));

        return diffY <= 30 && diffX <= detection;
    }

    /*! \public void reset
        \brief Reseteaza arcasul la starea inițială.

         Apelam reset() in clasa GameState in momentul in care moare jucatorul
         Reinițializeaza toti parametrii: animatii, cooldown, directie de patrulare și lista de sageți
     */
    @Override
    public void reset() {
        super.reset();
        this.dying = false;
        this.maxLife = 100;
        this.life = 100;
        this.deathFrame = 0;
        this.deathTimer = 0;
        this.animFrame = 0;
        this.animTimer = 0;
        this.currentCooldown = 0;
        this.startY = initialY;
        this.movingDown = true;
        this.arrows.clear();
    }
}