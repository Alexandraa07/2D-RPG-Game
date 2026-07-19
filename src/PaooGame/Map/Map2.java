package PaooGame.Map;

import java.awt.*;

/*! \class Map2
    \brief Reprezinta harta 2 a jocului, compusa din mai multe straturi (layere).

    Straturile sunt desenate in ordine:
    mai intai cele aflate sub jucator (iarba, apa, drum, coliziuni),
    apoi entitatile jocului,
    iar la final straturile aflate deasupra jucatorului (frunze).
 */
public class Map2 extends BaseMap {

    /// Layer-ul ce contine apa.
    private MapLayer water;

    /// Layer-ul ce contine iarba.
    private MapLayer grass;

    /// Layer-ul ce contine drumurile.
    private MapLayer road;

    /// Layere desenate peste jucator.
    private MapLayer leaf1, leaf2, leaf3;

    /*! \fn public Map2()
        \brief Constructorul clasei Map2.

        Incarca toate layerele hartii din fisierele CSV corespunzatoare.
     */
    public Map2() {

        water = new MapLayer("/maps/Harta2_water.csv", 120, 80);
        grass = new MapLayer("/maps/Harta2_grass.csv", 120, 80);
        road  = new MapLayer("/maps/Harta2_drum.csv", 120, 80);

        /// Layer-ul utilizat pentru coliziuni.
        collision = new MapLayer("/maps/Harta2_Collision.csv", 120, 80);

        /// Layere desenate peste jucator.
        leaf1 = new MapLayer("/maps/Harta2_OverPlayer1.csv", 120, 80);
        leaf2 = new MapLayer("/maps/Harta2_OverPlayer2.csv", 120, 80);
        leaf3 = new MapLayer("/maps/Harta2_OverPlayer3.csv", 120, 80);
    }

    /*! \fn public void drawUnder(Graphics g)
        \brief Deseneaza layerele aflate sub jucator.

        Ordinea desenarii:
        iarba -> apa -> drum -> obiecte solide (coliziuni).

        \param g Contextul grafic utilizat pentru desenare.
     */
    @Override
    public void drawUnder(Graphics g) {

        /// Iarba - fundalul principal al hartii.
        if (grass != null)
            grass.draw(g);

        /// Apa.
        if (water != null)
            water.draw(g);

        /// Drumurile.
        if (road != null)
            road.draw(g);

        /// Trunchiurile copacilor si obiectele solide.
        if (collision != null)
            collision.draw(g);
    }

    /*! \fn public void drawOver(Graphics g)
        \brief Deseneaza layerele aflate deasupra jucatorului.

        Aceste layere contin frunze.

        \param g Contextul grafic utilizat pentru desenare.
     */
    @Override
    public void drawOver(Graphics g) {

        /// Primul strat de frunze.
        if (leaf1 != null)
            leaf1.draw(g);

        /// Al doilea strat de frunze.
        if (leaf2 != null)
            leaf2.draw(g);

        /// Al treilea strat de frunze.
        if (leaf3 != null)
            leaf3.draw(g);
    }
}