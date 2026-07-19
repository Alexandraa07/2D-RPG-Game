package PaooGame.Graphics;

import PaooGame.Audio.SoundManager;
import PaooGame.Tiles.*;
import java.awt.image.BufferedImage;

/*! \class Assets
    \brief Clasa care se ocupă cu încărcarea și gestionarea tuturor resurselor vizuale din joc.
 */
public class Assets {

    ///Resurse pentru constructia hartilor, personaje, obiecte(Tiles)
    ///Tile-uri simple (o singura imagine)
    public static BufferedImage water, grass, pod, piatra, geam, usa1, usa2;

    ///Tile-uri compuse(mai multe bucați)
    public static BufferedImage[] drum = new BufferedImage[9];
    public static BufferedImage[] margine = new BufferedImage[4];
    public static BufferedImage[] cerc = new BufferedImage[8];
    public static BufferedImage[] copac = new BufferedImage[8];
    public static BufferedImage[] frunze = new BufferedImage[20];
    public static BufferedImage[] perete = new BufferedImage[5];
    public static BufferedImage[] acoperis = new BufferedImage[4];


    ///Pentru imagine fundal
    public static BufferedImage menuBackground;
    public static BufferedImage storyBackground;

    ///Pentru player Luma
    public static BufferedImage[] luma_front = new BufferedImage[9];
    public static BufferedImage[] luma_back = new BufferedImage[9];
    public static BufferedImage[] luma_left = new BufferedImage[9];
    public static BufferedImage[] luma_right = new BufferedImage[9];


    public static BufferedImage[] luma_hurt = new BufferedImage[6];
    public static BufferedImage[] luma_attack_right = new BufferedImage[7];
    public static BufferedImage[] luma_attack_left = new BufferedImage[7];
    public static BufferedImage[] luma_attack_up = new BufferedImage[7];
    public static BufferedImage[] luma_attack_down = new BufferedImage[7];

    public static BufferedImage[] fireball_luma = new BufferedImage[4];


    ///Pentru meniu secundar
    public static BufferedImage heart;
    public static BufferedImage potion;

    ///Pentru NPC
    public static BufferedImage npc1, npc2;


    ///Pentru celelalte spirite
    public static BufferedImage pamant1, pamant2;
    public static BufferedImage apa1, apa2;

    ///Pentru sageata si arcas
    public static BufferedImage arrow;
    public static BufferedImage[] archer_attack = new BufferedImage[8];
    public static BufferedImage[] walk_upArcher = new BufferedImage[9];
    public static BufferedImage[] walk_downArcher = new BufferedImage[9];
    public static BufferedImage[] dead_Archer = new BufferedImage[6];

    ///Pentru wizard
    public static BufferedImage[] wizard_back = new BufferedImage[9];
    public static BufferedImage[] wizard_right = new BufferedImage[9];
    public static BufferedImage[] wizard_left = new BufferedImage[9];
    public static BufferedImage[] wizard_front = new BufferedImage[9];
    public static BufferedImage[] dead_Wizard = new BufferedImage[6];
    public static BufferedImage[] w_attack_back = new BufferedImage[7];
    public static BufferedImage[] w_attack_right = new BufferedImage[7];
    public static BufferedImage[] w_attack_left = new BufferedImage[7];
    public static BufferedImage[] w_attack_front = new BufferedImage[7];

    public static BufferedImage[] magic = new BufferedImage[5];

    ///Pentru capcaun
    public static BufferedImage[] goblin_back = new BufferedImage[2];  // Sus
    public static BufferedImage[] goblin_left = new BufferedImage[2];  // Stânga
    public static BufferedImage[] goblin_front = new BufferedImage[2]; // Jos
    public static BufferedImage[] goblin_right = new BufferedImage[2]; // Dreapta
    public static BufferedImage[] dead_goblin = new BufferedImage[6];

    ///Pentru boomerang
    public static BufferedImage[] boomerang_rotation = new BufferedImage[8];

    ///Pentru diamante, fulgere, foc
    public static BufferedImage[] diamond = new BufferedImage[8];
    public static BufferedImage[] thunder = new BufferedImage[8];
    public static BufferedImage[] fire = new BufferedImage[8];

    ///Pentru piedestale diamante nivel 3
    public static BufferedImage[] piedestal_activ = new BufferedImage[8];
    public static BufferedImage piedestal;

    public static SoundManager soundManager;

    /*! \fn public static void Init()
        \brief Incarca fișierele de textura din resurse(spritesheets) si decupeaza
        imaginile corespunzatoare pentru fiecare variabila din clasa.
     */

    public static void Init() {

        SpriteSheet sheet = new SpriteSheet(ImageLoader.LoadImage("/textures/Tileset_mai_mic.png"));

        water = sheet.crop(1, 0);
        grass = sheet.crop(2, 0);
        pod = sheet.crop(7, 0);
        piatra = sheet.crop(8, 0);

        int j = 0;
        for (int x = 3; x <= 5; x++)
            for (int y = 1; y <= 3; y++) {
                drum[j] = sheet.crop(x, y);
                j++;
            }

        for (int i = 0; i < 4; i++) {
            margine[i] = sheet.crop(3 + i, 0);
        }

        int[][] cercPoz = {{0, 1}, {1, 1}, {2, 1}, {2, 2}, {2, 3}, {1, 3}, {0, 3}, {0, 2}};
        for (int i = 0; i < 8; i++) {
            cerc[i] = sheet.crop(cercPoz[i][0], cercPoz[i][1]);
        }

        int[][] copacPos = {{0, 6}, {0, 7}, {1, 4}, {1, 5}, {1, 6}, {1, 7}, {2, 6}, {2, 7}};
        for (int i = 0; i < 8; i++) {
            copac[i] = sheet.crop(copacPos[i][0], copacPos[i][1]);
        }


        int fi = 0;
        for (int x = 3; x <= 6; x++)
            for (int y = 4; y <= 8; y++) {
                frunze[fi++] = sheet.crop(x, y);
            }

        for (int i = 0; i < 5; i++) {
            perete[i] = sheet.crop(i, 9);
        }

        geam = sheet.crop(5, 9);
        acoperis[0] = sheet.crop(6, 9);
        acoperis[1] = sheet.crop(7, 9);
        acoperis[2] = sheet.crop(10, 9);
        acoperis[3] = sheet.crop(11, 9);
        usa1 = sheet.crop(12, 9);
        usa2 = sheet.crop(13, 9);

        registerTiles();

        menuBackground = ImageLoader.LoadImage("/textures/fundalBun.png");
        storyBackground = ImageLoader.LoadImage("/textures/fundal_poveste.png");

        BufferedImage sheetImage = ImageLoader.LoadImage("/textures/luma/walk.png");

        sheet = new SpriteSheet(sheetImage);

        for(int i = 0; i < 9; i++) {
            luma_back[i]  = sheet.crop2(i, 0, 64, 64);
            luma_left[i] = sheet.crop2(i, 1, 64, 64);
            luma_front[i] = sheet.crop2(i, 2, 64, 64);
            luma_right[i] = sheet.crop2(i, 3, 64, 64);
        }

        SpriteSheet hurt = new SpriteSheet(ImageLoader.LoadImage("/textures/luma/hurt.png"));
        for (int i = 0; i < 6; i++) {
            luma_hurt[i] = hurt.crop2(i, 0, 64, 64);
        }

        SpriteSheet atackLumaSheet = new SpriteSheet(ImageLoader.LoadImage("/textures/luma/luma_atack.png"));
        for (int i = 0; i < 7; i++) {
            luma_attack_right[i] = atackLumaSheet.crop2(i, 3, 64, 64);
            luma_attack_left[i] = atackLumaSheet.crop2(i, 1, 64, 64);
            luma_attack_up[i] = atackLumaSheet.crop2(i, 0, 64, 64);
            luma_attack_down[i] = atackLumaSheet.crop2(i, 2, 64, 64);

        }

        SpriteSheet fireSheet = new SpriteSheet(ImageLoader.LoadImage("/textures/luma/luma_fireball.png"));
        for (int i = 0; i < 4; i++) {
            fireball_luma[i] = fireSheet.crop2(i, 0, 32, 32);

        }

        heart = ImageLoader.LoadImage("/textures/heart.png");
        potion = ImageLoader.LoadImage("/textures/LavaPotion.png");

        SpriteSheet npc = new SpriteSheet(ImageLoader.LoadImage("/textures/idle.png"));
        npc1 = npc.crop2(0, 2, 64, 64);
        npc2 = npc.crop2(1, 2, 64, 64);

        SpriteSheet pamant = new SpriteSheet(ImageLoader.LoadImage("/textures/idle_pamant.png"));
        pamant1 = pamant.crop2(0, 2, 64, 64);
        pamant2 = pamant.crop2(1, 2, 64, 64);

        SpriteSheet apa = new SpriteSheet(ImageLoader.LoadImage("/textures/idle_apa.png"));
        apa1 = apa.crop2(0, 2, 64, 64);
        apa2 = apa.crop2(1, 2, 64, 64);

        SpriteSheet a = new SpriteSheet(ImageLoader.LoadImage("/textures/arcas/arrow.png"));
        arrow = a.crop2(0, 1, 64, 64);
        SpriteSheet arcasSheet = new SpriteSheet(ImageLoader.LoadImage("/textures/arcas/shoot.png"));
        for (int i = 0; i < 8; i++) {
            archer_attack[i] = arcasSheet.crop2(i, 1, 64, 64);
        }

        SpriteSheet walk = new SpriteSheet(ImageLoader.LoadImage("/textures/arcas/walk.png"));
        for (int i = 0; i < 9; i++) {
            walk_upArcher[i] = walk.crop2(i, 0, 64, 64);
            walk_downArcher[i] = walk.crop2(i, 2, 64, 64);
        }

        SpriteSheet dead = new SpriteSheet(ImageLoader.LoadImage("/textures/arcas/dead.png"));
        for (int i = 0; i < 6; i++) {
            dead_Archer[i] = dead.crop2(i, 0, 64, 64);
        }

        SpriteSheet moves = new SpriteSheet(ImageLoader.LoadImage("/textures/vrajitor/ipostaze.png"));
        for (int i = 0; i < 9; i++) {
            wizard_back[i] = moves.crop2(i, 8, 64, 64);
            wizard_left[i] = moves.crop2(i, 9, 64, 64);
            wizard_front[i] = moves.crop2(i, 10, 64, 64);
            wizard_right[i] = moves.crop2(i, 11, 64, 64);
        }

        for (int i = 0; i < 6; i++) {
            dead_Wizard[i] = moves.crop2(i, 20, 64, 64);
        }

        for (int i = 0; i < 7; i++) {
            w_attack_back[i] = moves.crop2(i, 0, 64, 64);
            w_attack_left[i] = moves.crop2(i, 1, 64, 64);
            w_attack_front[i] = moves.crop2(i, 2, 64, 64);
            w_attack_right[i] = moves.crop2(i, 3, 64, 64);
        }

        SpriteSheet magie = new SpriteSheet(ImageLoader.LoadImage("/textures/vrajitor/magie.png"));
        for (int i = 0; i < 5; i++) {
            magic[i] = magie.crop2(0, i, 128, 128);
        }

        SpriteSheet goblinSheet = new SpriteSheet(ImageLoader.LoadImage("/textures/capcaun/combat.png"));
        for (int i = 0; i < 2; i++) {

            goblin_back[i] = goblinSheet.crop2(i, 0, 64, 64);
            goblin_left[i] = goblinSheet.crop2(i, 1, 64, 64);
            goblin_front[i] = goblinSheet.crop2(i, 2, 64, 64);
            goblin_right[i] = goblinSheet.crop2(i, 3, 64, 64);
        }

        SpriteSheet deadG = new SpriteSheet(ImageLoader.LoadImage("/textures/capcaun/hurt.png"));
        for (int i = 0; i < 6; i++) {
            dead_goblin[i] = deadG.crop2(i, 0, 64, 64);
        }

        SpriteSheet boomerangSheet = new SpriteSheet(ImageLoader.LoadImage("/textures/capcaun/small_boomerang.png"));
        for (int i = 0; i < 8; i++) {
            boomerang_rotation[i] = boomerangSheet.crop2(i, 0, 16, 16);
        }

        SpriteSheet diamondSheet = new SpriteSheet(ImageLoader.LoadImage("/textures/diamond.png"));
        for(int i = 0; i < 8; i++){
            diamond[i] = diamondSheet.crop2(i, 0, 32, 32);
        }

        SpriteSheet piedestalSheet = new SpriteSheet(ImageLoader.LoadImage("/textures/piedestal.png"));
        for(int i = 0; i < 8; i ++){
            piedestal_activ[i] = piedestalSheet.crop2(i + 1, 0, 32, 40);
        }
        piedestal = piedestalSheet.crop2(0, 0, 32, 40);

        SpriteSheet thunderSheet = new SpriteSheet(ImageLoader.LoadImage("/textures/thunder.png"));
        for(int i = 0; i < 8; i++){
            thunder[i] = thunderSheet.crop2(i, 0, 64, 256);
        }

        SpriteSheet FireSheet = new SpriteSheet(ImageLoader.LoadImage("/textures/Fire.png"));
        for(int i = 0; i < 8; i++){
            fire[i] = FireSheet.crop2(i, 0, 32, 48);
        }

        soundManager = new SoundManager();
        soundManager.playMusic("/audio/Fundal.wav");

    }

    /*! \fn private static void registerTiles()
        \brief Inregistreaza tile-urile, asociind imaginile decupate cu ID-uri unice.
     */
    private static void registerTiles() {
        new WaterTile(water,  1);
        new GrassTile(grass,  2);
        new RoadTile(piatra,  8);
        new RoadTile(pod,     7);

        int[] drumId = {19,35,51,20,36,52,21,37,53};
        for (int i = 0; i < 9; i++)
        {
            new RoadTile(drum[i], drumId[i]);
        }

        for (int i=0; i<4; i++)
        {
            new RoadTile(margine[i], 3+i);
        }

        int[] cercId = {16,17,18,34,50,49,48,32};
        for (int i = 0; i < 8; i++)
        {
            new RoadTile(cerc[i], cercId[i]);
        }

        int[] copacId = {96,112,65,81,97,113,98,114};
        for (int i = 0; i < 8; i++)
        {
            new TreeTile(copac[i], copacId[i]);
        }

        new LeafTile(frunze[0], 67);
        new LeafTile(frunze[1], 83);
        new LeafTile(frunze[2], 99);
        new LeafTile(frunze[3], 115);
        new LeafTile(frunze[4], 131);

        new LeafTile(frunze[5], 68);
        new LeafTile(frunze[6], 84);
        new LeafTile(frunze[7], 100);
        new LeafTile(frunze[8], 116);
        new LeafTile(frunze[9], 132);

        new LeafTile(frunze[10], 69);
        new LeafTile(frunze[11], 85);
        new LeafTile(frunze[12], 101);
        new LeafTile(frunze[13], 117);
        new LeafTile(frunze[14], 133);

        new LeafTile(frunze[15], 70);
        new LeafTile(frunze[16], 86);
        new LeafTile(frunze[17], 102);
        new LeafTile(frunze[18], 118);
        new LeafTile(frunze[19], 134);

        for (int i=0; i<5; i++)
        {
            new TreeTile(perete[i], 144+i);
        }

        new TreeTile(geam,        149);
        new TreeTile(acoperis[0], 150);
        new TreeTile(acoperis[1], 151);
        new TreeTile(acoperis[2], 154);
        new TreeTile(acoperis[3], 155);
        new TreeTile(usa1,        156);
        new TreeTile(usa2,        157);

    }
}