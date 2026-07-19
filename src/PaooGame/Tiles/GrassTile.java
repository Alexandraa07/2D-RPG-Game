package PaooGame.Tiles;

import PaooGame.Graphics.Assets;

import java.awt.image.BufferedImage;

/*! \class GrassTile
    \brief Abstractizeaza notiunea de dala de tip iarba.
 */
public class GrassTile extends Tile
{
    /*! \fn public GrassTile(int id)
        \brief Constructorul de initializare al clasei

        \param id Id-ul dalei util in desenarea hartii.
     */
    public GrassTile(BufferedImage img, int id)
    {
        /// Apel al constructorului clasei de baza
        super(img, id);
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}