package PaooGame.Map;

import PaooGame.Tiles.Tile;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/*! \class MapLayer
    \brief Clasa gestioneaza un singur strat.

    Clasa incarca un fisier CSV ce contine ID-uri de tile-uri
    si deseneaza stratul corespunzator pe ecran.
 */
public class MapLayer {

    /// Dimensiunile hartii in tile-uri.
    private int width, height;

    /// Matrice ce retine ID-ul fiecarui tile.
    private int[][] tiles;

    /*! \fn public MapLayer(String path, int width, int height)
        \brief Constructorul clasei MapLayer.

        Initializeaza dimensiunile layer-ului si incarca datele din fisierul CSV.

        \param path Calea catre fisierul CSV.
        \param width Latimea hartii in tile-uri.
        \param height Inaltimea hartii in tile-uri.
     */
    public MapLayer(String path, int width, int height) {

        this.width = width;
        this.height = height;

        /// Aloca matricea de tile-uri.
        tiles = new int[width][height];

        /// Incarca layer-ul din fisier.
        loadWorld(path);
    }

    /*! \fn private void loadWorld(String path)
        \brief Incarca datele layer-ului dintr-un fisier CSV.

        Fiecare valoare din CSV reprezinta ID-ul unui tile.

        \param path Calea catre fisierul CSV.
     */
    private void loadWorld(String path) {

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(MapLayer.class.getResourceAsStream(path)));
            for (int y = 0; y < height; y++) {

                /// Citeste un rand din fisier.
                String line = br.readLine();

                /// Opreste citirea daca fisierul s-a terminat.
                if (line == null)
                    break;

                /// Separa valorile folosind virgula.
                String[] tokens = line.split(",");

                for (int x = 0; x < tokens.length; x++) {
                    /// Converteste valoarea in ID numeric.
                    tiles[x][y] = Integer.parseInt(tokens[x].trim());
                }
            }
            br.close();
        } catch (Exception e) {
            /// Afiseaza eroarea daca fisierul nu poate fi incarcat.
            e.printStackTrace();
        }
    }

    /*! \fn public void draw(Graphics g)
        \brief Deseneaza toate tile-urile layer-ului.

        Fiecare tile valid este desenat la pozitia corespunzatoare pe harta.

        \param g Contextul grafic utilizat pentru desenare.
     */
    public void draw(Graphics g) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int id = tiles[x][y];
                /// Verifica daca ID-ul corespunde unui tile valid.
                if (id >= 0 && id < Tile.tiles.length && Tile.tiles[id] != null) {
                    Tile.tiles[id].Draw(g, x * Tile.TILE_WIDTH, y * Tile.TILE_HEIGHT);
                }
            }
        }
    }

    /*! \fn public boolean isTileBlocked(int x, int y)
        \brief Verifica daca un tile este blocat.

        Un tile este considerat blocat daca coordonatele sunt in afara hartii sau tile-ul exista si este marcat ca solid.

        \param x Coordonata tile-ului pe axa X.
        \param y Coordonata tile-ului pe axa Y.

        \return True daca tile-ul este blocat.
     */
    public boolean isTileBlocked(int x, int y) {

        /// Verifica limitele hartii.
        if (x < 0 || y < 0 || x >= width || y >= height)
            return true;
        int id = tiles[x][y];
        /// Tile inexistent sau gol.
        if (id <= 0 || id >= Tile.tiles.length || Tile.tiles[id] == null)
            return false;
        /// Returneaza starea tile-ului.
        return Tile.tiles[id].isSolid();
    }
}