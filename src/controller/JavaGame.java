package controller;

import org.newdawn.slick.*;

public class JavaGame extends BasicGame {

    public JavaGame(String title) {
        super(title);
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {

    }

    @Override
    public void update(GameContainer gameContainer, int i) throws SlickException {

    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        graphics.drawString("String", 10, 10);
    }

/**    public static void main(String[] args) {
        try {
            AppGameContainer apc;
            apc = new AppGameContainer(new JavaGame("Javagame"));
            apc.setDisplayMode(640, 480, false);
            apc.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }**/
}
