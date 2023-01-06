package controller;

import controller.queues.GameEventQueue;
import controller.queues.PlayerInputEventQueue;
import model.GameObject;
import model.PlayerCharacter;
import model.Tiles.Tile;
import model.helper.AssetsManager;
import model.helper.GameData;
import model.npo.MousePointer;
import model.npo.ParallaxLayer;
import model.npo.Text;
import view.Window;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Main
{
    public static Window win;
    public static GameData gameData;
    public static AssetsManager assetsManager;
    public static PlayerInputEventQueue playerInputEventQueue;
    public static GameEventQueue gameEventQueue;
    public static boolean running;

    public static int INDEX_MOUSE_POINTER = 1;  // gameData.fixedobject
    public static int INDEX_PC = 2;

    public static int FPS = 60; // frames per second
    public static int TILE_SIZE = 16;

    public static PlayerCharacter pc;

    public static boolean dead = false;

   public static void main(String[] args) {
        win = new Window();
        win.init();
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setVisible(true);

        playerInputEventQueue = new PlayerInputEventQueue();
        assetsManager = new AssetsManager();
        gameData = new GameData();
        gameEventQueue = new GameEventQueue();

        startScreen();
        initGame();
        gameLoop();
    }

    static void startScreen() {
        // show initial message on canvas
        Font font = new Font("Courier New", Font.BOLD, 40);
        for (int i = 0; i < win.screenWidth / Main.TILE_SIZE; i++) {
            for (int j = 0; j < win.screenHeight / Main.TILE_SIZE; j++) {
                gameData.gameObjects.add(new Tile(assetsManager, i, j));
            }
        }
        gameData.gameObjects.add(new Text("Press Start Button", Main.win.getWidth()/2 - 225, Main.win.getHeight()/2 - 40, Color.YELLOW, font));

        while(!running) {
            Main.win.canvas.render();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // finish when running == true
    }

    public static void restartGame() {
        playerInputEventQueue.clear();
        gameEventQueue.clear();

        gameData.setScore(0);

        initGame();
    }

    public static void initGame() {
        gameData.destroyAll();
        gameData.gameObjects.add(new ParallaxLayer(4));
        gameData.gameObjects.add(new MousePointer(0, 0));

        pc = new PlayerCharacter(100, Main.win.getHeight() / 2);
        gameData.gameObjects.add(pc);

        Random ran = new Random();
        gameData.addAAGunWithListener(Main.win.canvas.getWidth() + 500 + ran.nextInt(1000),
                Main.win.canvas.getHeight() - 60);
        gameData.addFighterWithListener(Main.win.canvas.getWidth() + ran.nextInt(700),
                100 + ran.nextInt(550));
        short rY = (short) ran.nextInt(300);
        int y = (int) Main.pc.location.y - rY;
        if (y < 0) y = Main.assetsManager.getSprite(AssetsManager.SPR_BOMBER_PLANE).getHeight();
        else y = (int) Main.pc.location.y - rY;
        gameData.addBomberWithListener(Main.win.canvas.getWidth() + ran.nextInt(1000), y);
    }

    public static long timeSpent;

    static void gameLoop() {
        running = true;

        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0 / FPS;
        double delta = 0;
        double deltaTime = 0;
        int frames = 0;
        int updates = 0;
        //game loop
        while(running) {
            long currTime = System.nanoTime();
            delta += (currTime - lastTime) / ns;
            deltaTime = delta;
            lastTime = currTime;

            long startTime = System.currentTimeMillis();
            while (delta >= 1) {
                playerInputEventQueue.processInputEvents();
                gameEventQueue.processInputEvents();
                processCollision();
                //System.out.println("Delta: " + deltaTime);    //TODO Remove
                gameData.update(deltaTime);
                updates++;
                delta--;
            }

            win.canvas.render();
            frames++;

            long endTime = System.currentTimeMillis();

            timeSpent = endTime - startTime;
            long sleepTime = (long) (1000.0 / FPS - timeSpent);

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                Main.win.setTitle("FPS: " + frames + ", UPS: " + updates);
                frames = 0;
                updates = 0;
            }

            if(pc.hp <= 0) {
                dead = true;
                win.startButton.setText("RESTART");

                gameData.destroyAll();
                Font font = new Font("Courier New", Font.BOLD, 40);
                gameData.gameObjects.add(new Text("You DIED!", Main.win.getWidth()/2 - 225, Main.win.getHeight()/2 - 40, Color.YELLOW, font));
                gameData.gameObjects.add(new Text("HIGH SCORE: " + gameData.getScore(), Main.win.getWidth() / 2 - 225, Main.win.getHeight() / 2 - 80, Color.RED, font));
            }

            while(dead) {
                Main.win.canvas.render();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try {
                if (sleepTime > 0) Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static void processCollision() {
        for (int i = 3; i < Main.gameData.gameObjects.size(); i++) {
            var object = Main.gameData.gameObjects.get(i);
            if (object.id == GameObject.ID_ENEMY
                    && (object.location.x - object.width / 2) <= (pc.location.x + pc.width / 2) + 100
                    && (object.location.x - object.width / 2) >= (pc.location.x + pc.width / 2) - 100
                    && !object.done && object.getCollisionBox() != null) {
                if (pc.collideWith(object)) {
                    --pc.hp;
                    Main.win.hpField.setText("" + pc.hp);
                    ++object.hitCount;
                }
            }
            if (object.id == GameObject.ID_FRIEND) {
                for (var enemy : Main.gameData.gameObjects) {
                    if (enemy.id == GameObject.ID_ENEMY
                            && (object.location.x - object.width / 2) <= (enemy.location.x - enemy.width / 2) + 100
                            && (object.location.x - object.width / 2) >= (enemy.location.x - enemy.width / 2) - 100
                            && !object.done && !enemy.done && object.getCollisionBox() != null && enemy.getCollisionBox() != null) {
                        if (object.collideWith(enemy)) {
                            ++object.hitCount;
                            ++enemy.hitCount;
                        }
                    }
                }
            }
        }
    }
}
