package PaooGame.Entities;

import PaooGame.Game;
import PaooGame.Graphics.Assets;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/*! \class Protector
    \brief Clasa care reprezinta inamicul de tip Protector (paznicul diamantelor de la nivelul 3).

    Protectorul este asemanator cu inamicul Arcas.
    Protectorul ramane pe loc si supravegheaza zona diamantelor. Atunci cand detecteaza
    jucatorul pe axa orizontala, isi orienteaza atacul spre el si incepe sa traga sageti.
 */
public class Protector extends Enemy {

    /// Lista de sageti active proiectate de catre protector.
    private ArrayList<Arrow> arrows = new ArrayList<>();

    /// Viteza de deplasare (pixeli/frame) pe orizontala a sagetilor.
    private float arrowSpeed;

    /// Intervalul de timp minim necesar intre doua atacuri consecutive.
    private int shootCoolDown;

    /// Cooldown-ul curent al atacului, decrementat la fiecare cadru.
    private int currentCooldown;

    /// Cadrul curent utilizat pentru animatiile de atac.
    private int animFrame = 0;

    /// Contor de timp pentru incrementarea cadrelor de animatie.
    private int animTimer = 0;

    /// Viteza de rulare a animatiei standard (numarul de frame-uri per cadru).
    private static final int ANIM_SPEED = 5;

    /// Starea de moarte a protectorului (true daca viata ajunge la 0).
    private boolean dying = false;

    /// Cadrul curent din animatia de moarte.
    private int deathFrame = 0;

    /// Contor de timp pentru animatia de moarte.
    private int deathTimer = 0;

    /// Viteza de rulare a animatiei de moarte.
    private static final int DEATH_ANIM_SPEED = 20;

    /*! \fn public Protector(Game game, float x, float y)
        \brief Constructorul clasei Protector.

        Configureaza atributele de baza mostenite din clasa Enemy (viata, damage, raza de detectie)
        si seteaza parametrii initiali pentru comportamentul proiectilelor.

        \param game Referinta catre managerul principal al jocului.
        \param x Coordonata initiala pe axa X.
        \param y Coordonata initiala pe axa Y.
     */
    public Protector(Game game, float x, float y) {
        super(game, x, y, 64, 64);
        this.maxLife = 100;
        this.life = 100;
        this.damage = 1;
        this.detection = 300;
        this.arrowSpeed = 9.0f;
        this.shootCoolDown = 40;
        this.currentCooldown = 0;
    }

    /*! \fn public void Update()
        \brief Actualizeaza logica si starile protectorului pentru cadrul curent.

        Se ocupa de animatia de moarte, actualizarea timpului de asteptare pentru atac,
        detectarea jucatorului, actualizarea sagetilor, cat si de aplicarea damage-ului.
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
            animTimer++;
            if (animTimer >= ANIM_SPEED) {
                animTimer = 0;
                animFrame = (animFrame + 1) % Assets.archer_attack.length;
            }

            if (currentCooldown == 0 && animFrame == 6) {
                Attack(player);
            }
        } else {
            animTimer++;
            if (animTimer >= ANIM_SPEED) {
                animTimer = 0;
                animFrame = (animFrame + 1) % Assets.walk_downArcher.length;
            }
        }

        for (Arrow a : arrows) {
            a.Update();
        }
        arrows.removeIf(a -> !a.isActive());

        for (Arrow a : arrows) {
            if (a.isActive() && a.getBounds().intersects(player.getBounds())) {
                player.takeDamage(damage);
            }
        }
    }

    /*! \fn public void Draw(Graphics g)
        \brief Deseneaza protectorul

        Selecteaza sprite-ul corect (moarte, atac sau repaus). Daca jucatorul se afla in partea dreapta,
        aplica un algoritm de desenare inversa pentru a oglindi caracterul. Deseneaza sagetile si bara de viata.

        \param g Contextul grafic pe care se deseneaza.
     */
    @Override
    public void Draw(Graphics g) {
        BufferedImage currentImage;
        boolean flip = false;
        Player player = game.getPlayer();

        if (dying) {
            currentImage = Assets.dead_Archer[Math.min(deathFrame, Assets.dead_Archer.length - 1)];
        } else if (playerDetection(player)) {
            currentImage = Assets.archer_attack[animFrame % Assets.archer_attack.length];
            if (player.getX() > x) {
                flip = true;
            }
        } else {
            currentImage = Assets.walk_downArcher[animFrame % Assets.walk_downArcher.length];
        }

        if (flip) {
            g.drawImage(currentImage, (int) x + width, (int) y, -width, height, null);
        } else {
            g.drawImage(currentImage, (int) x, (int) y, width, height, null);
        }

        for (Arrow a : arrows) a.Draw(g);

        g.setColor(Color.RED);
        g.fillRect((int) x, (int) y - 10, width, 5);
        g.setColor(Color.GREEN);
        int bar = (int) ((life / (float) maxLife) * width);
        g.fillRect((int) x, (int) y - 10, bar, 5);
    }

    /*! \fn public void Attack(Entity target)
        \brief Lanseaza o sageata catre entitatea tinta (jucatorul).

        Determina directia vectorului de viteza `vX` (pozitiv sau negativ) prin compararea pozitiei inamicului
        cu cea a tintei si instantiaza obiectul in lista de proiectile, resetand timpul de cooldown.

        \param target Obiectul de tip Entity atacat de catre protector.
     */
    @Override
    public void Attack(Entity target) {
        float vX;
        if(target.getX() > x){
            vX= arrowSpeed;
        } else {
            vX = -arrowSpeed;
        }
        arrows.add(new Arrow(game, x, y, vX, 0, damage));
        currentCooldown = shootCoolDown;
    }

    /*! \fn public void takeDamage(int damage)
        \brief Scade din viata inamicului si verifica daca ramane fara viata.

        Daca viata scade sub sau este egala cu 0, porneste animatia de moarte.

        \param damage Damage-ul primit
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

    /*! \fn public boolean playerDetection(Player player)
        \brief Verifica daca jucatorul se afla in apropiere pe orizontala.

        Calculul se bazeaza pe valoarea absoluta a diferentei dintre centrul protectorului si cel al jucatorului.

        \param player Referinta catre obiectul jucatorului.
        \return True daca jucatorul este detectat in aceeasi arie orizontala (Y <= 40) si in limita campului vizual
     */
    public boolean playerDetection(Player player) {
        float diffX = Math.abs((x + width / 2f) - (player.getX() + player.width / 2f));
        float diffY = Math.abs((y + height / 2f) - (player.getY() + player.height / 2f));
        return diffY <= 40 && diffX <= detection;
    }

    /*! \fn public void reset()
        \brief readuce inamicul la configuratia sa initiala de spawn.

        Curata complet lista de sageti si reinitializeaza parametrii animatiilor si ai starilor de atac/moarte.
     */
    @Override
    public void reset() {
        super.reset();
        this.dying = false;
        this.deathFrame = 0;
        this.deathTimer = 0;
        this.animFrame = 0;
        this.animTimer = 0;
        this.currentCooldown = 0;
        this.life = 100;
        this.maxLife = 100;
        this.arrows.clear();
    }
}