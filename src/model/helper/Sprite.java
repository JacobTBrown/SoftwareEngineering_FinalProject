package model.helper;

import controller.Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Sprite {
    private static BufferedImage spriteSheet;
    private static BufferedImage sprite;

    public static BufferedImage loadSprite(String file) {
        sprite = null;

        try {
            sprite = ImageIO.read(new File("assets/" + file + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sprite;
    }

    public static BufferedImage getSprite() {
        return sprite;
    }

    public static BufferedImage getSpriteSheet(BufferedImage spriteSheet, int xGrid, int yGrid) {
        if(spriteSheet == null) {
            return null;
        }

        return spriteSheet.getSubimage(xGrid * Main.TILE_SIZE, yGrid * Main.TILE_SIZE,
            Main.TILE_SIZE, Main.TILE_SIZE);
    }
}
