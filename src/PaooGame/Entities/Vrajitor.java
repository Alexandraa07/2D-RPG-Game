package PaooGame.Entities;

import PaooGame.Game;
import PaooGame.Graphics.Assets;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/*! \class Vrajitor
    \brief Clasa reprezinta inamicul de tip Vrajitor(inamicul final)
 */
public class Vrajitor extends Enemy {

    /// Lista proiectilelor magice active.
    private List<MagicAtack> magii = new ArrayList<>();
    /// Cooldown curent atac.
    private int attackCooldown = 0;
    /// Interval cooldown atac.
    private int shootCooldown = 50;

    /// Cadru curent animatie.
    private int animFrame = 0;
    /// Timer schimbare cadre animatie.
    private int animTimer = 0;
    /// Viteza rulare animatie.
    private static final int ANIM_SPEED = 5;

    /// Status moarte.
    private boolean dying = false;
    /// Cadru animatie moarte.
    private int deathFrame = 0;
    /// Timer animatie moarte.
    private int deathTimer = 0;
    /// Viteza animatie moarte.
    private static final int DEATH_ANIM_SPEED = 20;

    /// Coordonata X de start.
    private float startX;
    /// Coordonata Y de start.
    private float startY;
    /// Dimensiune tile de joc.
    private static final int TILE = 32;
    /// Latime zona patrulare.
    private static final int PATROL_W = 20 * TILE;
    /// Inaltime zona patrulare.
    private static final int PATROL_H = 13 * TILE;
    /// Viteza deplasare patrulare.
    private float patrolSpeed = 3.0f;

    /// Directiile de patrulare.
    private enum PatrolDir {RIGHT, DOWN, LEFT, UP}
    /// Directia curenta de patrulare.
    private PatrolDir patrolDir = PatrolDir.RIGHT;

    /// Directia de orientare a sprite-ului.
    private String dir = "front";

    /// Timer interval regenerare.
    private int regenTimer=0;
    /// Timp trecut de la ultima lovitura primita.
    private int timeSinceHit=0;
    /// Interval regenerare
    private static final int REGEN_INTERVAL = 180;
    /// Timp fara damage obligatoriu (5 secunde).
    private static final int HIT_COOLDOWN   = 300;
    /// Cantitate viata regenerata per interval.
    private static final int REGEN_AMOUNT   = 25;

    /*! \fn public Vrajitor(Game game, float x, float y)
        \brief Constructorul clasei Vrajitor.
        \param game Referinta joc.
        \param x Pozitie X.
        \param y Pozitie Y.
     */
    public Vrajitor(Game game, float x, float y) {
        super(game, x, y, 64, 64);
        this.life = 300;
        this.maxLife = 300;
        this.damage = 1;
        this.detection = 200;
        this.startX = x;
        this.startY = y;
    }

    /*! \fn public void Update()
        \brief Actualizeaza logica, starile de atac, moartea, patrularea si regenerarea vrajitorului.
     */
    @Override
    public void Update() {

        if (dying) {
            deathTimer++;
            if (deathTimer >= DEATH_ANIM_SPEED) {
                deathTimer = 0;
                deathFrame++;
                if (deathFrame >= Assets.dead_Wizard.length) {
                    alive = false;
                }
            }
            return;
        }

        if (!alive) return;

        if (attackCooldown > 0)
            attackCooldown--;

        Player player = game.getPlayer();

        timeSinceHit++;
        if (life < maxLife && timeSinceHit >= HIT_COOLDOWN) {
            regenTimer++;
            if (regenTimer >= REGEN_INTERVAL) {
                regenTimer = 0;
                life += REGEN_AMOUNT;
                if (life > maxLife) {
                    life = maxLife;
                }
                System.out.println("viata vrajitorului regenerata! HP curent: " + life);
            }
        } else if (life >= maxLife) {
            regenTimer = 0;
        }

        if (playerDetection(player)) {

            float dx = player.getX() - x;
            float dy = player.getY() - y;

            if (Math.abs(dx) > Math.abs(dy)) {
                if(dx > 0)
                    dir = "right";
                else
                    dir = "left";
            } else {
                if(dy > 0)
                    dir = "front";
                else
                    dir = "back";
            }

            if (attackCooldown == 0) {
                Attack(player);
                attackCooldown = shootCooldown;
            }
        }
        else {
            switch (patrolDir) {
                case RIGHT:
                    x = x + patrolSpeed;
                    dir = "right";
                    if (x >= startX + PATROL_W)
                        patrolDir = PatrolDir.DOWN;
                    break;

                case DOWN:
                    y = y + patrolSpeed;
                    dir = "front";
                    if (y >= startY + PATROL_H)
                        patrolDir = PatrolDir.LEFT;
                    break;

                case LEFT:
                    x = x - patrolSpeed;
                    dir = "left";
                    if (x <= startX)
                        patrolDir = PatrolDir.UP;
                    break;

                case UP:
                    y = y - patrolSpeed;
                    dir = "back";
                    if (y <= startY)
                        patrolDir = PatrolDir.RIGHT;
                    break;
            }
        }

        animTimer++;
        if (animTimer >= ANIM_SPEED) {
            animTimer = 0;
            animFrame = (animFrame + 1) % 9;
        }

        for (MagicAtack m : magii)
            m.Update();

        magii.removeIf(m -> !m.isActive());
    }

    /*! \fn public void Draw(Graphics g)
        \brief Deseneaza vrajitorul (in functie de directia de mers), bara de viata si magiile sale.
        \param g Contextul grafic.
     */
    @Override
    public void Draw(Graphics g) {

        if (dying) {
            g.drawImage(Assets.dead_Wizard[Math.min(deathFrame, Assets.dead_Wizard.length - 1)], (int)x, (int)y, width, height, null);
            return;
        }

        if (!alive) return;
        switch (dir) {
            case "back":
                g.drawImage(Assets.wizard_back[animFrame], (int)x, (int)y, width, height, null);
                break;

            case "left":
                g.drawImage(Assets.wizard_left[animFrame], (int)x, (int)y, width, height, null);
                break;

            case "right":
                g.drawImage(Assets.wizard_right[animFrame], (int)x, (int)y, width, height, null);
                break;

            default:
                g.drawImage(Assets.wizard_front[animFrame], (int)x, (int)y, width, height, null);
                break;
        }

        g.setColor(Color.RED);
        g.fillRect((int)x, (int)y - 10, width, 5);

        g.setColor(Color.GREEN);
        int bar = (int)((life / (float)maxLife) * width);
        g.fillRect((int)x, (int)y - 10, bar, 5);

        for (MagicAtack m : magii)
            m.Draw(g);
    }

    /*! \fn public void Attack(Entity target)
        \brief Lanseaza un proiectil magic pe directia unde se afla tinta.
        \param target Tinta atacata (Jucatorul).
     */
    @Override
    public void Attack(Entity target) {

        float dx = target.getX() - x;
        float dy = target.getY() - y;

        float dirX = 0;
        float dirY = 0;

        if (Math.abs(dx) > Math.abs(dy)) {
            if(dx > 0){
                dirX = 1;
            }else {
                dirX = -1;
            }
        } else {
            if(dy > 0){
                dirY = 1;
            } else {
                dirY = -1;
            }
        }

        float cx = x + width / 2f;
        float cy = y + height / 2f;

        magii.add(new MagicAtack(game, cx, cy, dirX, dirY, damage));
    }

    /*! \fn public void takeDamage(int damage)
        \brief Scade viata inamicului, reseteaza timerul de hit si verifica moartea.
        \param damage Damage primit.
     */
    @Override
    public void takeDamage(int damage) {
        if (dying || !alive) return;
        life = life - damage;
        timeSinceHit = 0;
        if (life <= 0) {
            life = 0;
            dying = true;
            deathFrame = 0;
            deathTimer = 0;
        }
    }

    /*! \fn public boolean playerDetection(Player player)
        \brief Verifica daca jucatorul este in raza de detectie a vrajitorului.
        \param player Referinta jucator.
        \return True/False.
     */
    public boolean playerDetection(Player player) {
        float diffX = Math.abs((x + width / 2f) - (player.getX() + player.width / 2f));
        float diffY = Math.abs((y + height / 2f) - (player.getY() + player.height / 2f));
        return diffX <= detection && diffY <= detection;
    }

    /*! \fn public void reset()
        \brief Reseteaza starea, viata, magiile si traseul de patrulare la configuratia de baza.
     */
    @Override
    public void reset() {
        super.reset();
        this.dying = false;
        this.deathFrame = 0;
        this.deathTimer = 0;
        this.animFrame = 0;
        this.animTimer = 0;
        this.attackCooldown = 0;
        this.patrolDir = PatrolDir.RIGHT;
        this.startX = initialX;
        this.startY = initialY;
        this.dir = "front";
        this.magii.clear();
        this.life = 300;
        this.maxLife = 300;
    }
}