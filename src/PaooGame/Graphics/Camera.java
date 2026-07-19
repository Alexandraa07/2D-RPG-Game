package PaooGame.Graphics;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

/*! \class public class Camera
    \brief Clasa care gestioneaza camera jocului.

    Camera controleaza portiunea din lume afisata pe ecran
    si aplica transformari de translatie si zoom asupra contextului grafic.
 */
public class Camera {

    /// Coordonatele camerei in lumea jocului.
    private float x, y;

    /// Dimensiunile ferestrei jocului.
    private int width, height;

    /// Factorul de zoom aplicat camerei.
    private float zoom = 2.0f;

    /*! \fn public Camera(float x, float y, int width, int height)
        \brief Constructorul clasei Camera.

        Initializeaza pozitia camerei si dimensiunile ferestrei jocului.

        \param x Coordonata initiala X a camerei.
        \param y Coordonata initiala Y a camerei.
        \param width Latimea ferestrei jocului.
        \param height Inaltimea ferestrei jocului.
     */
    public Camera(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /*! \fn public void apply(Graphics2D g2d)
        \brief Aplica transformarile camerei asupra contextului grafic.

        Se aplica mai intai zoom-ul, apoi translatarea scenei
        pentru a afisa portiunea dorita din lume.

        \param g2d Contextul grafic asupra caruia se aplica transformarea.
     */
    public void apply(Graphics2D g2d) {
        AffineTransform transform = new AffineTransform();

        transform.scale(zoom, zoom);
        transform.translate(-x, -y);

        g2d.setTransform(transform);
    }

    /*! \fn public void setPosition(float x, float y)
        \brief Modifica pozitia camerei in lume.

        \param x Noua coordonata X a camerei.
        \param y Noua coordonata Y a camerei.
     */
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /*! \fn public float getZoom()
        \brief Returneaza factorul de zoom al camerei.

        \return Valoarea curenta a zoom-ului.
     */
    public float getZoom() {
        return zoom;
    }

    /*! \fn public float getX()
        \brief Returneaza coordonata X a camerei.

        \return Pozitia X a camerei.
     */
    public float getX() {
        return x;
    }

    /*! \fn public float getY()
        \brief Returneaza coordonata Y a camerei.

        \return Pozitia Y a camerei.
     */
    public float getY() {
        return y;
    }
}