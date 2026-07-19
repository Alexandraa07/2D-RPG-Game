package PaooGame.Input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;

/*! \class MouseManager
    \brief Clasa gestioneaza input-ul de la mouse.

    Clasa implementeaza interfetele MouseListener si MouseMotionListener
    pentru a detecta apasarile butoanelor mouse-ului si miscarea cursorului.
 */
public class MouseManager implements MouseListener, MouseMotionListener
{
    /// Indica daca butonul stang al mouse-ului este apasat.
    private boolean leftPressed;

    /// Coordonatele curente ale cursorului mouse-ului.
    private int mouseX, mouseY;

    /*! \fn public MouseManager()
        \brief Constructorul clasei MouseManager.
     */
    public MouseManager() {}

    /*! \fn public boolean isLeftPressed()
        \brief Verifica daca butonul stang al mouse-ului este apasat.

        \return True daca butonul stang este apasat.
     */
    public boolean isLeftPressed()
    {
        return leftPressed;
    }

    /*! \fn public int getMouseX()
        \brief Returneaza coordonata X a cursorului.

        \return Pozitia X a mouse-ului.
     */
    public int getMouseX()
    {
        return mouseX;
    }

    /*! \fn public int getMouseY()
        \brief Returneaza coordonata Y a cursorului.

        \return Pozitia Y a mouse-ului.
     */
    public int getMouseY()
    {
        return mouseY;
    }

    /*! \fn public void mousePressed(MouseEvent e)
        \brief Detecteaza apasarea unui buton al mouse-ului.

        Daca este apasat butonul stang, variabila leftPressed devine true.

        \param e Evenimentul generat la apasarea mouse-ului.
     */
    @Override
    public void mousePressed(MouseEvent e)
    {
        if(e.getButton() == MouseEvent.BUTTON1)
        {
            leftPressed = true;
        }
    }

    /*! \fn public void mouseReleased(MouseEvent e)
        \brief Detecteaza eliberarea unui buton al mouse-ului.

        Daca este eliberat butonul stang, variabila leftPressed devine false.

        \param e Evenimentul generat la eliberarea mouse-ului.
     */
    @Override
    public void mouseReleased(MouseEvent e)
    {
        if(e.getButton() == MouseEvent.BUTTON1)
        {
            leftPressed = false;
        }
    }

    /*! \fn public void mouseMoved(MouseEvent e)
        \brief Actualizeaza coordonatele cursorului mouse-ului.

        \param e Evenimentul generat la miscarea mouse-ului.
     */
    @Override
    public void mouseMoved(MouseEvent e)
    {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}