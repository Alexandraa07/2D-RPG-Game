package PaooGame.Map;

import java.awt.*;

/*! \class Map1
    \brief Reprezinta harta 1 a jocului, compusa din mai multe straturi (layere).

    Straturile sunt desenate in ordine:
    mai intai cele aflate sub jucator (iarba, apa, drum, coliziuni),
    apoi entitatile jocului,
    iar la final straturile aflate deasupra jucatorului (frunze).
 */
public class Map1 extends BaseMap {

    /// Layer-ul ce contine apa.
    private MapLayer water;

    /// Layer-ul ce contine iarba.
    private MapLayer grass;

    /// Layer-ul ce contine drumurile.
    private MapLayer road;

    /// Layere desenate peste jucator.
    private MapLayer leaf1, leaf2, leaf3;

    /*! \fn public Map1()
        \brief Constructorul clasei Map1.

        Incarca toate layerele hartii din fisierele CSV corespunzatoare.
     */
    public Map1() {

        water = new MapLayer("/maps/MAPA_DE_TEST_water.csv", 90, 60);
        grass = new MapLayer("/maps/MAPA_DE_TEST_grass.csv", 90, 60);
        road  = new MapLayer("/maps/MAPA_DE_TEST_drum.csv", 90, 60);

        /// Layer-ul utilizat pentru coliziuni.
        collision = new MapLayer("/maps/MAPA_DE_TEST_Collision.csv", 90, 60);

        /// Layere desenate peste jucator.
        leaf1 = new MapLayer("/maps/MAPA_DE_TEST_OverPlayer1.csv", 90, 60);
        leaf2 = new MapLayer("/maps/MAPA_DE_TEST_OverPlayer2.csv", 90, 60);
        leaf3 = new MapLayer("/maps/MAPA_DE_TEST_OverPlayer3.csv", 90, 60);
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