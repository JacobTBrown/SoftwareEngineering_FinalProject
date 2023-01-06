package model.Tiles;

import controller.Main;
import model.GameObject;
import model.helper.AssetsManager;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class Tile extends GameObject {
    public Tile(AssetsManager assets, int x, int y) {
        super(x * Main.TILE_SIZE, y * Main.TILE_SIZE);
        super.id = GameObject.ID_BACKGROUND;

        Random rand = new Random();

        switch(rand.nextInt(4)) {
            case 0:
                bufferImg = assets.getSprite(AssetsManager.TILE_CARPET_UPLEFT);
                break;
            case 1:
                bufferImg = assets.getSprite(AssetsManager.TILE_CARPET_UPRIGHT);
                break;
            case 2:
                bufferImg = assets.getSprite(AssetsManager.TILE_CARPET_DOWNLEFT);
                break;
            case 3:
                bufferImg = assets.getSprite(AssetsManager.TILE_CARPET_DOWNRIGHT);
                break;
        }

        width = bufferImg.getWidth();
        height = bufferImg.getHeight();
    }

    /**
     * Takes an x and y integer and multiplies it by TILE_SIZE.
     * @param x tile position x
     * @param y tile position y
     */
    public void setTileLocation(int x, int y) {
        setLocation(x * Main.TILE_SIZE, y * Main.TILE_SIZE);
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(bufferImg, (int) location.x, (int) location.y, null);
    }

    @Override
    public void update(double deltaTime) {

    }

    @Override
    public Rectangle2D getCollisionBox() {
        return null;
    }
}
