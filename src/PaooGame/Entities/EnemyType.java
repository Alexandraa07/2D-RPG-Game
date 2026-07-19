package PaooGame.Entities;

/*! \enum EnemyType
    \brief Enumeratie care defineste tipurile de inamici disponibili in joc.

    Folosita impreuna cu EnemyFactory pentru a crea inamicul dorit.
 */
public enum EnemyType {
    ARCHER,    /// Inamic de tip arcas, patruleaza si trage sageti.
    GOBLIN,    /// Inamic de tip goblin, arunca boomerangul.
    VRAJITOR,  /// Inamic de tip vrajitor, arunca carti magice
    PROTECTOR  /// Protectori ai piedestalelor
}