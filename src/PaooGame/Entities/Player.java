package PaooGame.Entities;

import PaooGame.Game;
import PaooGame.Graphics.Assets;
import PaooGame.States.GameState;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/*! \class Player
    \brief Clasa care reprezinta jucatorul principal.

    Jucatorul se poate deplasa pe harta, poate ataca folosind proiectile,
    isi poate regenera viata si poate utiliza o potiune speciala.
    Atunci cand viata ajunge la 0 se activeaza animatia de moarte.
 */
public class Player extends Character {

    /// Frame-ul curent al animatiei de mers
    private int frame;

    /// Contor folosit pentru animatii
    private int time;

    /// Lista proiectilelor lansate de jucator
    private List<FireBall> fireballs = new ArrayList<>();

    /// Verifica daca jucatorul ataca
    private boolean attacking = false;

    /// Frame-ul curent al animatiei de atac
    private int attackFrame = 0;

    /// Timer pentru animatia de atac
    private int attackTimer = 0;

    /// Folosit pentru a preveni atacul continuu pe fiecare frame
    /// (trebuie sa apesi de fiecare data pe tasta de atac pentru a trimite UN SINGUR proiectil)
    private boolean attackPressed = false;

    /// Viteza animatiei de atac
    private static final int ATTACK_SPEED = 3;

    /// Numarul de frame-uri ale animatiei de atac
    private static final int ATTACK_FRAMES = 7;

    /// Timer pentru regenerarea vietii
    private int regenTimer = 0;

    /// Timpul trecut de la ultima lovitura primita
    private int timeSinceHit = 0;

    /// Intervalul necesar pentru regenerarea vietii
    private static final int REGEN_INTERVAL = 300;

    /// Timp minim fara damage pentru regenerare
    private static final int HIT_COOLDOWN = 180;

    /// Viata maxima a jucatorului
    private int maxLife = 3;

    /// Verifica daca jucatorul are potiune
    private boolean arePotiune = false;

    /// Directia curenta de mers
    private String dir = "right";

    /// Directia ultimului atac
    private String attackDir = "right";

    /// Verifica daca jucatorul moare
    private boolean dying = false;

    /// Frame-ul curent al animatiei de moarte
    private int deathFrame = 0;

    /// Timer pentru animatia de moarte
    private int deathTimer = 0;

    /// Viteza animatiei de moarte
    private static final int DEATH_ANIM_SPEED = 20;

    /*! \fn public Player(Game game, float x, float y)
        \brief Constructorul clasei Player.

        Initializeaza jucatorul cu viata, viteza si pozitia initiala.

        \param game Referinta catre joc.
        \param x Coordonata initiala pe axa X.
        \param y Coordonata initiala pe axa Y.
     */
    public Player(Game game, float x, float y) {
        super(game, x, y, 64, 64);

        this.life = 3;
        this.speed = 7.0f;
        this.frame = 0;
        this.time = 0;
    }

    /*! \fn public void Update()
        \brief Actualizeaza logica jucatorului(miscare, coliziune etc.)
     */
    @Override
    public void Update() {

        if (dying) {

            deathTimer++;
            if (deathTimer >= DEATH_ANIM_SPEED) {
                deathTimer = 0;
                deathFrame++;
                if (deathFrame >= Assets.luma_hurt.length) {
                    life = 0;
                    ((GameState)game.gameState).handlePlayerDeath();
                }
            }
            return;
        }

        float newX = x;
        float newY = y;

        // Miscare sus
        if (game.getKeyManager().up) {
            newY = newY - speed;
            dir = "back";
        }

        // Miscare jos
        if (game.getKeyManager().down) {
            newY = newY + speed;
            dir = "front";
        }

        // Miscare stanga
        if (game.getKeyManager().left) {
            newX = newX - speed;
            dir = "left";
        }

        // Miscare dreapta
        if (game.getKeyManager().right) {
            newX = newX + speed;
            dir = "right";
        }

        boolean blocked;

        // Verificare coliziuni in functie de harta curenta
        if (game.getCurrentMap() == 1)
            blocked = game.getMap1().isPlaceBlocked(newX, newY, width, height);

        else if (game.getCurrentMap() == 2)
            blocked = game.getMap2().isPlaceBlocked(newX, newY, width, height);

        else
            blocked = game.getMap3().isPlaceBlocked(newX, newY, width, height);

        // Actualizare pozitie daca nu exista coliziuni
        if (!blocked) {
            x = newX;
            y = newY;
        }

        // Regenerare viata
        timeSinceHit++;
        if (life < maxLife && timeSinceHit >= HIT_COOLDOWN) {
            regenTimer++;
            if (regenTimer >= REGEN_INTERVAL) {
                regenTimer = 0;
                life++;
                System.out.println("Regenerare! life=" + life);
            }
        } else if (life >= maxLife) {
            regenTimer = 0;
        }

        // Resetare atac
        if (!game.getKeyManager().atack_left && !game.getKeyManager().atack_right && !game.getKeyManager().atack_up && !game.getKeyManager().atack_down) {
            attackPressed = false;
        }

        float dirX = 0;
        float dirY = 0;

        // Atac stanga
        if (game.getKeyManager().atack_left && !attackPressed) {

            attackPressed = true;
            dirX = -1;
            attackDir = "left";
        }

        // Atac dreapta
        else if (game.getKeyManager().atack_right && !attackPressed) {

            attackPressed = true;
            dirX = 1;
            attackDir = "right";
        }

        // Atac jos
        else if (game.getKeyManager().atack_down && !attackPressed) {

            attackPressed = true;
            dirY = 1;
            attackDir = "front";
        }

        // Atac sus
        else if (game.getKeyManager().atack_up && !attackPressed) {

            attackPressed = true;
            dirY = -1;
            attackDir = "back";
        }

        // Creeaza proiectil
        if (dirX != 0 || dirY != 0) {
            fireballs.add(new FireBall(game, x + width / 2f, y + height / 2f, dirX, dirY)
            );
        }

        // Actualizare proiectile
        for (FireBall fire : fireballs)
            fire.Update();

        // Eliminare proiectile inactive
        fireballs.removeIf(fire -> !fire.isActive());
    }

    /*! \fn public void Draw(Graphics g)
        \brief Deseneaza jucatorul pe ecran(animatia de mers, atac, moarte, proiectile).
        \param g Contextul grafic.
     */
    @Override
    public void Draw(Graphics g) {

        if (game.getKeyManager() == null)
            return;

        // Animatie moarte
        if (dying) {
            g.drawImage(Assets.luma_hurt[Math.min(deathFrame, Assets.luma_hurt.length - 1)], (int)x, (int)y, width, height, null);
            return;
        }

        time++;
        if (time >= 8) {
            time = 0;
            frame = (frame + 1) % 9;
        }

        // Pornire animatie atac
        if ((game.getKeyManager().atack_left || game.getKeyManager().atack_right || game.getKeyManager().atack_up || game.getKeyManager().atack_down) && !attacking) {
            attacking = true;
            attackFrame = 0;
            attackTimer = 0;
        }

        // Animatie atac
        if (attacking) {
            attackTimer++;
            if (attackTimer >= ATTACK_SPEED) {
                attackTimer = 0;
                attackFrame++;
                if (attackFrame >= ATTACK_FRAMES) {
                    attackFrame = 0;
                    attacking = false;
                }
            }

            if (attacking) {
                BufferedImage frame;
                switch (attackDir) {
                    case "left":
                        frame = Assets.luma_attack_left[attackFrame];
                        break;
                    case "back":
                        frame = Assets.luma_attack_up[attackFrame];
                        break;
                    case "front":
                        frame = Assets.luma_attack_down[attackFrame];
                        break;
                    default:
                        frame = Assets.luma_attack_right[attackFrame];
                        break;
                }

                g.drawImage(frame, (int)x, (int)y, width, height, null);
            }
            for (FireBall fb : fireballs)
                fb.Draw(g);
            return;
        }

        // Animatie mers in functie de tasta apasata
        BufferedImage imageToDraw = Assets.luma_front[0];
        if (game.getKeyManager().up)
            imageToDraw = Assets.luma_back[frame];
        else if (game.getKeyManager().down)
            imageToDraw = Assets.luma_front[frame];
        else if (game.getKeyManager().left)
            imageToDraw = Assets.luma_left[frame];
        else if (game.getKeyManager().right)
            imageToDraw = Assets.luma_right[frame];
        g.drawImage(imageToDraw, (int)x, (int)y, width, height, null);

        for (FireBall fb : fireballs)
            fb.Draw(g);
    }

    @Override
    public int getLife() {
        return life;
    }

    /*! \fn public void takeDamage(int damage)
        \brief Scade viata jucatorului.

        Daca viata ajunge la 0 se activeaza animatia de moarte.
     */
    public void takeDamage(int damage) {

        if (dying || life == 0)
            return;

        life = life - damage;

        timeSinceHit = 0;
        if (life <= 0) {
            life = 0;
            dying = true;
            deathFrame = 0;
            deathTimer = 0;
        }
    }

    /*! \fn public Rectangle getBounds()
        \brief Returneaza hitbox-ul jucatorului.

        \return Dreptunghiul de coliziune.
     */
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }

    /*! \fn public void reset()
        \brief Reseteaza jucatorul la starea initiala.

        Reinitializeaza viata, animatiile si pozitia initiala.
     */
    public void reset() {

        this.maxLife = 3;
        this.arePotiune = false;
        this.life = 3;
        this.dying = false;
        this.deathFrame = 0;
        this.deathTimer = 0;
        this.attacking = false;
        this.x = 384;
        this.y = 1856;
    }

    /*! \fn public void activeazaPotiune()
        \brief Activeaza efectul potiunei.

        Daca jucatorul are potiunea: viata maxima devine 5, viata curenta devine 5
     */

    public void activeazaPotiune() {

        if (this.arePotiune) {
            this.maxLife = 5;
            this.life = 5;
            this.arePotiune = false;
            System.out.println("Piedestal activat! Luma are potiune");
        } else {
            this.maxLife = 3;
            this.life = 3;
        }
    }

    /*! \fn public void setArePotiune(boolean status)
        \brief Seteaza daca jucatorul detine sau nu o potiune.
        \param status Valoarea booleana a statusului potiunii.
     */
    public void setArePotiune(boolean status) {
        this.arePotiune = status;
    }

    /*! \fn public int getHealth()
    \brief Returneaza viata curenta a jucatorului.
    \return Viata curenta ca intreg.
*/
    public int getHealth() {
        return life;
    }

    /*! \fn public boolean isDying()
        \brief Returneaza daca jucatorul este in animatia de moarte.
        \return true daca jucatorul moare, false altfel.
    */
    public boolean isDying() {
        return dying;
    }

    /*! \fn public boolean hasPotion()
        \brief Returneaza daca jucatorul detine o potiune.
        \return true daca jucatorul are potiune, false altfel.
    */
    public boolean hasPotion() {
        return arePotiune;
    }

    /*! \fn public void setDying(boolean dying)
        \brief Seteaza starea de moarte a jucatorului.
        \param dying true daca jucatorul intra in animatia de moarte, false altfel.
    */
    public void setDying(boolean dying) {
        this.dying = dying;
    }
}