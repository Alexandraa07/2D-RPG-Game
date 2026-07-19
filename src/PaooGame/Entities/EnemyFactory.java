package PaooGame.Entities;

import PaooGame.Game;

/*! \class EnemyFactory
    \brief Clasa care implementeaza pattern-ul Factory pentru crearea inamicilor.

    In loc sa cream inamicii direct cu new Archer(), new Goblin() etc.,
    folosim aceasta clasa care decide ce tip de inamic sa creeze
    in functie de parametrul primit.
 */
public class EnemyFactory {

    /*! \fn public static Enemy create(EnemyType type, Game game, float x, float y)
        \brief Creeaza si returneaza un inamic de tipul specificat.

        \param type Tipul inamicului de creat (ARCHER, GOBLIN, VRAJITOR sau PROTECTOR).
        \param game Referinta la instanta principala a jocului.
        \param x    Coordonata X initiala a inamicului (in pixeli).
        \param y  Coordonata Y initiala a inamicului (in pixeli).
        \return    O instanta de Enemy corespunzatoare tipului cerut.
     */
    public static Enemy create(EnemyType type, Game game, float x, float y) {

        /// Cream arcasul
        if (type == EnemyType.ARCHER) {
            return new Archer(game, x, y);
            /// Cream goblinul
        } else if (type == EnemyType.GOBLIN) {
            return new Goblin(game, x, y);
            /// Cream vrajitorul
        } else if (type == EnemyType.VRAJITOR) {
            return new Vrajitor(game, x, y);
            /// Implicit cream protectorul
        } else {
            return new Protector(game, x, y);
        }
    }
}