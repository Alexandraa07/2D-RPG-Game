package PaooGame;

//import PaooGame.GameWindow.GameWindow;

public class Main
{
    public static void main(String[] args)
    {
        System.setProperty("sun.java2d.uiScale", "1.0");
        Game paooGame = new Game("PaooGame", 1280, 720);
        paooGame.StartGame();
    }
}