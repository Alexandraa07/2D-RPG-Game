package PaooGame.Entities;

import PaooGame.Game;
import PaooGame.Graphics.Assets;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/*! \class Goblin
    \brief Clasa care reprezinta inamicul de tip goblin.

    Goblinul sta pe loc si, cand detecteaza jucatorul, arunca boomeranguri
    in directia acestuia. Boomerangul se intoarce la goblin dupa ce parcurge
    o distanta fixa. La moarte se activeaza animatia de moarte.
 */
public class Goblin extends Enemy {

    private float attackSpeed;   /// Viteza de deplasare a boomerangului (pixeli/frame).
    private int shootCoolDown;   /// Numarul de frame-uri de asteptare intre doua atacuri.
    private int currentCooldown;  /// Cooldown-ul curent, decrementat la fiecare frame.
    protected int direction;    /// Directia in care priveste goblinul (0=sus, 1=stanga, 2=jos, 3=dreapta).

    private List<Boomerang> boomerangs = new ArrayList<>(); /// Lista boomerangurilor active aruncate de goblin.

    private int animFrame = 0;       /// Frame-ul curent al animatiei.
    private int animTimer = 0;        /// Contor de timp pentru avansarea animatiei.
    private static final int ANIM_SPEED = 8; /// Numarul de frame-uri intre doua cadre de animatie.

    private boolean dying = false;        /// Indica daca goblinul este in animatia de moarte.
    private int deathFrame = 0;         /// Frame-ul curent al animatiei de moarte.
    private int deathTimer = 0;       ///Contor de timp pentru animatia de moarte.
    private static final int DEATH_ANIM_SPEED = 20;  /// Numarul de frame-uri intre doua cadre din animatia de moarte.

    /*! \fn public Goblin(Game game, float x, float y)
        \brief Constructorul clasei Goblin.

        Initializeaza goblinul cu statisticile de baza (viata, damage,raza de detectie) si parametrii de atac (viteza boomerang, cooldown).

        \param game Referinta la instanta principala a jocului.
        \param x    Coordonata X initiala (in pixeli).
        \param y    Coordonata Y initiala (in pixeli).
     */
    public Goblin(Game game, float x, float y) {
        super(game, x, y, 64, 64);
        this.life          = 150;
        this.maxLife       = 150;
        this.speed         = 0;
        this.damage        = 1;
        this.detection     = 300;
        this.attackSpeed   = 6.0f;
        this.currentCooldown = 0;
        this.shootCoolDown = 80;
    }

    /*! \fn public void Update()
        \brief Actualizeaza logica goblinului la fiecare frame.

        Gestioneaza animatia de moarte, detectia jucatorului, directia
        de privire, atacul cu boomerang si coliziunea boomerangurilor cu jucatorul.
     */
    @Override
    public void Update() {

        /// Daca goblinul moare, rulam animatia de moarte
        if (dying) {
            deathTimer++;
            if (deathTimer >= DEATH_ANIM_SPEED) {
                deathTimer = 0;
                deathFrame++;
                if (deathFrame >= Assets.dead_goblin.length) {
                    alive = false;
                }
            }
            return;
        }

        if (currentCooldown > 0) {
            currentCooldown--;
        }

        Player player = game.getPlayer();

        if (playerDetection(player)) {
            /// Calculam directia spre jucator pentru a alege animatia corecta
            float dx = player.getX() - x;
            float dy = player.getY() - y;

            if (Math.abs(dx) > Math.abs(dy)) {
                if (dx > 0) {
                    direction = 3; /// Jucatorul e la dreapta
                } else {
                    direction = 1; /// Jucatorul e la stanga
                }
            } else {
                if (dy > 0) {
                    direction = 2; /// Jucatorul e jos
                } else {
                    direction = 0; /// Jucatorul e sus
                }
            }

            /// Avansam animatia de atac
            animTimer++;
            if (animTimer >= ANIM_SPEED) {
                animTimer = 0;
                animFrame++;
                if (animFrame >= 2) {
                    animFrame = 0;
                }
            }

            /// Atacam daca cooldown-ul a expirat
            if (currentCooldown == 0) {
                Attack(player);
            }

        } else {
            /// Jucatorul nu e detectat, goblinul sta pe loc
            animFrame = 0;
            animTimer = 0;
        }

        /// Actualizam fiecare boomerang activ
        for (int i = 0; i < boomerangs.size(); i++) {
            Boomerang b = boomerangs.get(i);
            b.Update();

            /// Verificam coliziunea boomerangului cu jucatorul
            if (b.canHit() && b.getBounds().intersects(player.getBounds())) {
                player.takeDamage(1);
                b.registerHit();
            }

            /// Eliminam boomerangurile care au revenit la goblin
            if (!b.isActive()) {
                boomerangs.remove(i);
                i--;
            }
        }
    }

    /*! \fn public void Draw(Graphics g)
        \brief Deseneaza goblinul si elementele sale asociate.

        Alege sprite-ul potrivit in functie de directia de privire,
        deseneaza boomerangurile active si bara de viata.

        \param g Contextul grafic pe care se realizeaza desenarea.
     */
    @Override
    public void Draw(Graphics g) {

        /// Daca moare, afisam animatia de moarte
        if (dying) {
            g.drawImage(Assets.dead_goblin[Math.min(deathFrame, Assets.dead_goblin.length - 1)],
                    (int) x, (int) y, width, height, null);
            return;
        }

        /// Alegem sprite-ul in functie de directia curenta
        BufferedImage currentFrame;
        switch (direction) {
            case 0:
                currentFrame = Assets.goblin_back[animFrame];
                break;
            case 1:
                currentFrame = Assets.goblin_left[animFrame];
                break;
            case 3:
                currentFrame = Assets.goblin_right[animFrame];
                break;
            case 2:
            default:
                currentFrame = Assets.goblin_front[animFrame];
                break;
        }

        g.drawImage(currentFrame, (int) x, (int) y, width, height, null);

        /// Desenam boomerangurile active
        for (Boomerang b : boomerangs) {
            b.Draw(g);
        }

        /// Bara de viata
        g.setColor(Color.RED);
        g.fillRect((int) x, (int) y - 10, width, 5);

        g.setColor(Color.GREEN);
        int barWidth = (int) ((life / (float) maxLife) * width);
        g.fillRect((int) x, (int) y - 10, barWidth, 5);
    }

    /*! \fn public void Attack(Entity target)
        \brief Creeaza si lanseaza un boomerang in directia tintei.

        Calculeaza directia spre tinta si porneste boomerangul
        din centrul goblinului. Reseteaza cooldown-ul dupa atac.

        \param target Entitatea tinta (jucatorul).
     */
    @Override
    public void Attack(Entity target) {
        if (dying || !alive) return;

        /// Calculam directia spre jucator
        float dx   = target.getX() - x;
        float dy   = target.getY() - y;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        if (dist == 0)
            return;

        float vX = (dx / dist) * attackSpeed;
        float vY = (dy / dist) * attackSpeed;

        /// Cream boomerangul si il adaugam in lista
        Boomerang b = new Boomerang(game, x + width / 4, y + height / 4, vX, vY, damage, this);
        boomerangs.add(b);

        currentCooldown = shootCoolDown;
    }

    /*! \fn public void takeDamage(int d)
        \brief Aplica damage goblinului si declanseaza moartea daca viata ajunge la 0.

        Daca goblinul este deja in animatia de moarte, apelul este ignorat.

        \param d Valoarea damage-ului de aplicat.
     */
    @Override
    public void takeDamage(int d) {
        if (dying) return;

        life = life - d;

        if (life <= 0) {
            life = 0;
            dying = true;
            deathFrame = 0;
            deathTimer = 0;
        }
    }

    /*! \fn public boolean playerDetection(Player player)
        \brief Verifica daca jucatorul se afla in raza de detectie a goblinului.

        Detectia are loc daca distanta pe ambele axe este mai mica decat raza detection.

        \param player Referinta la jucatorul verificat.
        \return true daca jucatorul este detectat, false altfel.
     */
    @Override
    public boolean playerDetection(Player player) {
        float diffX = Math.abs((x + width  / 2f) - (player.getX() + player.width  / 2f));
        float diffY = Math.abs((y + height / 2f) - (player.getY() + player.height / 2f));

        return diffX <= detection && diffY <= detection;
    }

    /*! \fn public void reset()
        \brief Reseteaza goblinul la starea initiala.

        Reinitializeaza animatiile, cooldown-ul, directia si lista de boomeranguri.
        Apelata cand moare jucatorul pentru a reinitializa nivelul.
     */
    @Override
    public void reset() {
        super.reset();
        this.dying         = false;
        this.deathFrame    = 0;
        this.deathTimer    = 0;
        this.animFrame     = 0;
        this.animTimer     = 0;
        this.currentCooldown = 0;
        this.direction     = 2;
        this.maxLife = 150;
        this.life = 150;
        this.boomerangs.clear();
    }
}