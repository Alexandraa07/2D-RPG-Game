package PaooGame;

import PaooGame.Audio.SoundManager;
import PaooGame.DataBase.DatabaseManager;
import PaooGame.Entities.*;
import PaooGame.GameWindow.GameWindow;
import PaooGame.Graphics.Assets;
import PaooGame.Graphics.Camera;
import PaooGame.Input.KeyManager;
import PaooGame.Input.MouseManager;
import PaooGame.Map.Map1;
import PaooGame.Map.Map2;
import PaooGame.Map.Map3;
import PaooGame.States.EndingState;
import PaooGame.States.GameState;
import PaooGame.States.MenuState;
import PaooGame.States.State;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

/*! \class Game
    \brief Clasa principala a intregului proiect. Implementeaza Game-Loop (Update -> Draw).

    Implementeaza interfata Runnable pentru a rula logica jocului intr-un fir de executie separat.
    Game-loop-ul actualizeaza starea jocului si redeseneaza fereastra de 60 de ori pe secunda.

                ------------
                |           |
                |     ------------
    60 times/s  |     |  Update  |  --> actualizeaza variabile, stari, pozitii ale elementelor grafice
        =       |     ------------
     16.7 ms    |           |
                |     ------------
                |     |   Draw   |  --> deseneaza totul pe ecran
                |     ------------
                |           |
                -------------
*/
public class Game implements Runnable {

    private GameWindow     wnd;        /// Fereastra in care se va desena tabla jocului */
    private boolean        runState;   /// Flag ce indica starea firului de executie */
    private Thread         gameThread; /// Referinta catre thread-ul de update si draw */
    private BufferStrategy bs;         /// Strategia de buffering triplu pentru eliminarea flickering-ului */
    private Graphics       g;          /// Referinta catre contextul grafic curent */

    private Map1 map1; /// Harta nivelului 1
    private Map2 map2; /// Harta nivelului 2
    private Map3 map3; /// Harta nivelului 3

    private int currentMap = 1; /// Indexul hartii curente active

    public State menuState; /// Starea meniului principal
    public State gameState;  /// Starea principala a jocului
    public State endingState; /// Starea ecranului de final

    private MouseManager mouseManager; /// Manager pentru input mouse
    private KeyManager keyM;   /// Manager pentru input tastatura
    private Camera camera;  /// Camera care urmareste jucatorul

    private Player player; /// Jucatorul principal

    private ArrayList<Npc>       npcs;       /// Lista NPC-urilor din joc
    private SpiritulPamantului   pamant;     /// Spiritul Pamantului de salvat
    private SpiritulApei         apa;        /// Spiritul Apei de salvat
    private Archer               archer;     /// Inamicul arcas de pe harta 1
    private ArrayList<Protector> protectors; /// Lista protectorilor diamantelor
    private Vrajitor             wizard;     /// Inamicul final de pe harta 3
    private Goblin               goblin;     /// Inamicul goblin de pe harta 2

    private ArrayList<Thunder> thunder; /// Lista fulgerelor de pe harta 3

    private Diamond  diamond1, diamond2, diamond3;    /// Cele 3 diamante magice
    private Piedestal piedestal1, piedestal2, piedestal3; ///Cele 3 piedestale

    private Potion potion; /// Potiunea de pe harta 1

    private DatabaseManager db; ///Manager pentru salvarea si incarcarea jocului

    private SoundManager soundManager; ///Managerul de sunet al jocului

    /*! \fn public Game(String title, int width, int height)
        \brief Constructor de initializare al clasei Game.

        Construieste fereastra jocului si initializeaza conexiunea la baza de date.

        \param title Titlul ferestrei.
        \param width Latimea ferestrei in pixeli.
        \param height Inaltimea ferestrei in pixeli.
    */
    public Game(String title, int width, int height) {
        wnd = new GameWindow(title, width, height);
        db = new DatabaseManager();
        db.connect();
        db.createTable();
        runState = false;
    }

    /*! \fn private void InitGame()
        \brief Initializeaza toate elementele jocului.

        Construieste fereastra, incarca asseturile, instantiaza hartile,
        jucatorul, NPC-urile, inamicii, obiectele si starile jocului.
    */
    private void InitGame() {
        wnd.BuildGameWindow();

        camera = new Camera(0, 0, 1280, 720);

        Assets.Init();

        map1 = new Map1();
        map2 = new Map2();
        map3 = new Map3();

        player = new Player(this, 384, 1856);

        npcs = new ArrayList<>();
        npcs.add(new Npc(this, 500,  1750, "Mergi spre nord și salvează Spiritul Pământului, este în pericol!", 1));
        npcs.add(new Npc(this, 128,  1952, "Colectează toate cele trei diamante și salvează Spiritul Apei!", 2));
        npcs.add(new Npc(this, 96,   1120, "Pune diamantele pe piedestal și învinge Vrăjitorul!", 3));

        pamant = new SpiritulPamantului(this, 2484, 470,  "Îți mulțumesc că m-ai salvat, pădurea îți este recunoscătoare");
        apa= new SpiritulApei (this, 3360, 992,  "Îți mulțumesc că m-ai salvat, pădurea îți este recunoscătoare");

        diamond1 = new Diamond(this, 1792, 2496);
        diamond2 = new Diamond(this,  864, 1248);
        diamond3 = new Diamond(this, 2784, 1504);

        piedestal1 = new Piedestal(this, 1728, 2496, 1);
        piedestal2 = new Piedestal(this, 3744, 1216, 2);
        piedestal3 = new Piedestal(this, 1792,   32, 3);

        potion = new Potion(this, 1216, 128);

        archer = (Archer) EnemyFactory.create(EnemyType.ARCHER,   this, 2292,    530);
        wizard = (Vrajitor) EnemyFactory.create(EnemyType.VRAJITOR,  this, 1440.0f, 1024.0f);
        goblin = (Goblin)EnemyFactory.create(EnemyType.GOBLIN,    this, 3167.0f, 957.0f);

        thunder = new ArrayList<>();
        thunder.add(new Thunder(this, 1280, 1184, 1));
        thunder.add(new Thunder(this, 1440, 1312, 1));
        thunder.add(new Thunder(this, 1568, 1216, 1));
        thunder.add(new Thunder(this, 1824, 1216, 1));
        thunder.add(new Thunder(this, 1920,  960, 1));
        thunder.add(new Thunder(this, 1920, 1472, 1));
        thunder.add(new Thunder(this, 1664, 1024, 1));

        protectors = new ArrayList<>();
        protectors.add((Protector) EnemyFactory.create(EnemyType.PROTECTOR, this, 1984,  96));
        protectors.add((Protector) EnemyFactory.create(EnemyType.PROTECTOR, this, 1568, 128));
        protectors.add((Protector) EnemyFactory.create(EnemyType.PROTECTOR, this, 1920, 2368));
        protectors.add((Protector) EnemyFactory.create(EnemyType.PROTECTOR, this, 1472, 2464));
        protectors.add((Protector) EnemyFactory.create(EnemyType.PROTECTOR, this, 3616, 1216));

        menuState= new MenuState(this);
        gameState = new GameState(this);
        endingState = new EndingState(this);
        State.setState(menuState);

        mouseManager = new MouseManager();
        wnd.GetCanvas().addMouseListener(mouseManager);
        wnd.GetCanvas().addMouseMotionListener(mouseManager);

        keyM = new KeyManager();
        wnd.getWndFrame().addKeyListener(keyM);
        wnd.GetCanvas().addKeyListener(keyM);
        wnd.GetCanvas().setFocusable(true);
        wnd.GetCanvas().requestFocus();
    }

    /*! \fn public void run()
        \brief Functia executata de thread-ul jocului.

        Initializeaza jocul si ruleaza game-loop-ul la 60 de frame-uri pe secunda.
    */
    @Override
    public void run() {
        InitGame();

        long oldTime = System.nanoTime();
        long curentTime;

        final int    framesPerSecond = 60;
        final double timeFrame       = 1000000000.0 / framesPerSecond;

        while (runState) {
            curentTime = System.nanoTime();
            if ((curentTime - oldTime) > timeFrame) {
                Update();
                Draw();
                oldTime = curentTime;
            }
        }
    }

    /*! \fn public synchronized void StartGame()
        \brief Creaza si porneste firul de executie al jocului.

        Metoda este synchronized pentru a preveni porniri multiple simultane.
    */
    public synchronized void StartGame() {
        if (!runState) {
            runState   = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    /*! \fn public synchronized void StopGame()
        \brief Opreste firul de executie al jocului.

        Metoda este synchronized pentru a preveni opriri multiple simultane.
    */
    public synchronized void StopGame() {
        if (runState) {
            runState = false;
            try {
                gameThread.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    /*! \fn private void Update()
        \brief Actualizeaza starea elementelor din joc.

    */
    private void Update() {
        keyM.Update();
        if (State.getState() != null) {
            State.getState().Update();
        }
    }

    /*! \fn private void Draw()
        \brief Deseneaza elementele grafice in fereastra.
    */
    private void Draw() {
        bs = wnd.GetCanvas().getBufferStrategy();
        if (bs == null) {
            try {
                wnd.GetCanvas().createBufferStrategy(3);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        g = bs.getDrawGraphics();
        g.clearRect(0, 0, wnd.GetWndWidth(), wnd.GetWndHeight());

        if (State.getState() != null) {
            State.getState().Draw(g);
        }

        bs.show();
        g.dispose();
    }

    /*! \fn public boolean checkReady()
        \brief Verifica daca toate diamantele au fost colectate.
    */
    public boolean checkReady() {
        return !getDiamond1().isActive() && !getDiamond2().isActive() && !getDiamond3().isActive();
    }

    /*! \fn public boolean allPedestalsReady()
        \brief Verifica daca toate piedestalele au un diamant plasat.
        \return true daca toate 3 piedestale sunt activate, false altfel.
    */
    public boolean allPedestalsReady() {
        return piedestal1.hasDiamond() && piedestal2.hasDiamond() && piedestal3.hasDiamond();
    }

    public MouseManager getMouseManager()
    { return mouseManager; }

    public Map1 getMap1()
    { return map1; }

    public Map2 getMap2()
    { return map2; }

    public Map3 getMap3()
    { return map3; }

    public Camera getCamera()
    { return camera; }

    public KeyManager getKeyManager()
    { return keyM; }


    public Player getPlayer()
    { return player; }

    public void setCurrentMap(int map)
    { this.currentMap = map; }

    public int getCurrentMap()
    { return currentMap; }

    public ArrayList<Npc> getNpcs()
    { return npcs; }

    public GameWindow getWnd()
    { return wnd; }

    public SpiritulPamantului getPamant()
    { return pamant; }

    public SpiritulApei getApa()
    { return apa; }

    public Archer getArcher()
    { return archer; }

    public Vrajitor getWizard()
    { return wizard; }

    public ArrayList<Protector> getProtectors()
    { return protectors; }

    public ArrayList<Thunder> getThunder()
    { return thunder; }

    public Goblin getGoblin()
    { return goblin; }

    public ArrayList<Enemy> getEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();
        if (archer    != null && archer.isAlive())
            enemies.add(archer);
        if (wizard    != null && wizard.isAlive())
            enemies.add(wizard);
        if (goblin    != null && goblin.isAlive())
            enemies.add(goblin);
        if (protectors != null) enemies.addAll(protectors);
        return enemies;
    }

    public Diamond getDiamond1()
    { return diamond1; }

    public Diamond getDiamond2()
    { return diamond2; }

    public Diamond getDiamond3()
    { return diamond3; }

    public Piedestal getPiedestal1()
    { return piedestal1; }

    public Piedestal getPiedestal2()
    { return piedestal2; }

    public Piedestal getPiedestal3()
    { return piedestal3; }

    public Potion getPotion()
    { return potion; }

    public DatabaseManager getDataBaseManager()
    { return db; }
}