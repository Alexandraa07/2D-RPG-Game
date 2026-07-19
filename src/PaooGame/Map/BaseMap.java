package PaooGame.Map;

import PaooGame.Tiles.Tile;

import java.awt.*;

/*! \class BaseMap
    \brief Clasa abstracta ce defineste structura de baza a unei harti.

    Clasa gestioneaza layer-ul de coliziune si ofera metode
    abstracte pentru desenarea elementelor aflate sub si peste entitati.
 */
public abstract class BaseMap {

    /// Layer-ul utilizat pentru verificarea coliziunilor.
    public MapLayer collision;

    /*! \fn public boolean isPlaceBlocked(float x, float y, int width, int height)
        \brief Verifica daca o pozitie este blocata de un tile solid.

        Se calculeaza cele patru colturi ale entitatii si se verifica
        daca acestea se suprapun peste tile-uri marcate ca solide.

        \param x Coordonata X a entitatii.
        \param y Coordonata Y a entitatii.
        \param width Latimea entitatii.
        \param height Inaltimea entitatii.

        \return True daca exista coliziune cu un tile solid.
     */
    public boolean isPlaceBlocked(float x, float y, int width, int height) {

        int TILE_SIZE = Tile.TILE_WIDTH;

        /// Calculul coordonatelor tile-urilor ocupate de colturi.
        int left   = (int) x / TILE_SIZE;
        int right  = (int)(x + width - 1) / TILE_SIZE;
        int top    = (int) y / TILE_SIZE;
        int bottom = (int)(y + height - 1) / TILE_SIZE;

        /// Verifica daca unul dintre colturi intersecteaza un tile blocat.
        return (collision.isTileBlocked(left, top) || collision.isTileBlocked(right, top) || collision.isTileBlocked(left, bottom) || collision.isTileBlocked(right, bottom));
    }

    /*! \fn public abstract void drawUnder(Graphics g)
        \brief Deseneaza layer-ele aflate sub entitati.

        \param g Contextul grafic utilizat pentru desenare.
     */
    public abstract void drawUnder(Graphics g);

    /*! \fn public abstract void drawOver(Graphics g)
        \brief Deseneaza layer-ele aflate peste entitati.

        \param g Contextul grafic utilizat pentru desenare.
     */
    public abstract void drawOver(Graphics g);
}