package PaooGame.Tiles;
import java.awt.image.BufferedImage;

/*! \class RoadTile
    \brief Abstractizeaza notiunea de dala de tip drum.
 */
public class RoadTile extends Tile{
    public RoadTile(BufferedImage img, int id){
        super(img, id);
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}