package PaooGame.Tiles;

import java.awt.image.BufferedImage;

/*! \class TreeTile
    \brief Abstractizeaza notiunea de dala de tip tree.
 */
public class TreeTile extends Tile
{
    /*! \fn public TreeTile(int id)
        \brief Constructorul de initializare al clasei

        \param id Id-ul dalei util in desenarea hartii.
     */
    public TreeTile(BufferedImage img, int id) {
        super(img, id); // Aici pasăm imaginea (care poate fi null la început) și ID-ul
    }

    /*! \fn public boolean IsSolid()
        \brief Suprascrie metoda IsSolid() din clasa de baza in sensul ca va fi luat in calcul in caz de coliziune.
     */
    @Override
    public boolean isSolid()
    {
        return true;
    }
}