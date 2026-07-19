package PaooGame.Tiles;

import java.awt.image.BufferedImage;

/*! \class LeafTile
    \brief Abstractizeaza notiunea de dala de tip frunza.
 */
public class LeafTile extends Tile{
    public LeafTile(BufferedImage img, int id) {
        super(img, id);
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}