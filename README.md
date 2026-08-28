# 2D RPG Game (Java)

A 2D tile-based role-playing game built in Java for managing game loops, entity movement, and local state persistence. The project focuses on clean Object-Oriented Programming (OOP), design patterns, and safe local persistence powered by the SQLite library.

---

## Key Features

- Game Loop & State Management: Dynamic state switching (MenuState, GameState, InstructionState, StoryState, EndingState) handled cleanly via OOP design patterns.
- Persistent Storage: Game state and player progress are saved and loaded from disk using SQLite (`DataBaseManager`).
- Tilemap Rendering & Camera: Multi-layered 2D map parsing (`BaseMap`, `Map1`, `Map2`, `Map3`) with custom camera tracking (`Camera`) and tile engines (`Tiles`).
- Physics & Entities: Factory pattern implementation (`EnemyFactory`) for dynamic entity generation, collision detection, and projectile mechanics (Archer, FireBall, MagicAtack, Arrow).
- Audio Integration: Dedicated sound management system (`SoundManager`) for background music.

---

## Technologies Used

- Language: Java
- Database: SQLite (JDBC Driver)
- Build System: IDE Project (IntelliJ IDEA / Eclipse)
- Libraries & Frameworks: Java Swing & AWT (Graphics, Input)
- Concepts: Object-Oriented Programming, Design Patterns, Data Structures

---

## Project Architecture

- Audio – Handles game background audio (`SoundManager`).
- DataBase – Handles SQLite database I/O operations for progress persistence (`DataBaseManager`).
- Entities – Core game objects, inheritance hierarchy (`Character`, `Player`, `Enemy`), projectiles, and creation patterns (`EnemyFactory`).
- Graphics & Camera – Handles sprite sheet slicing (`SpriteSheet`), image loading (`ImageLoader`), assets, and viewport bounds (`Camera`).
- Map & Tiles – Modular map loading (`BaseMap`, `MapLayer`) and custom tile behaviors (`WaterTile`, `GrassTile`, `TreeTile`).
- States – Application workflow and menu machine (`GameState`, `MenuState`, `StoryState`, `EndingState`).

---

## How to Build and Run

### Prerequisites

- A Java Development Kit (JDK) installed
- IntelliJ IDEA or Eclipse IDE
- SQLite JDBC library (included in `lib/`)

### Build Steps

1. Clone the repository
git clone https://github.com/Alexandraa07/2D-RPG-Game.git

2. Open in your IDE
Open the project folder in IntelliJ IDEA or Eclipse.

3. Configure build path
Ensure the JAR file from the `lib/` directory (`sqlite-jdbc`) is added to the project libraries.

4. Run the executable
Locate `Main.java` inside `src/PaooGame/` and click **Run**.

---

## How to Use it

1. Launch: Start the application via `Main.java`.

2. Main Menu: Navigate using the key bindings to Start a New Game or view instructions.

3. Interactive Gameplay:
   - Movement & Combat: Use key bindings to navigate maps, cast spells (FireBall, MagicAtack), and interact with entities.
   - Save / Exit: Save your current progress directly to the local SQLite database before closing.
