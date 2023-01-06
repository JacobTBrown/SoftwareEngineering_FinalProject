package model.npo;

import controller.Main;
import model.GameObject;
import model.enums.ENUM_OBJ;
import model.helper.AssetsManager;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class ParallaxLayer extends GameObject {

    private BufferedImage foreground, background, followFore, followBack;
    int x2, y2;
    int dx, gap;

    public ParallaxLayer(int dx, int gap) {
        super(ENUM_OBJ.BACKGROUND, ID_FIXED);

        this.dx = dx;
        this.gap = gap;

        foreground = Main.assetsManager.getSprite(AssetsManager.SPR_FOREGROUND);
        background = Main.assetsManager.getSprite(AssetsManager.SPR_BACKGROUND);
        followFore = foreground;
        followBack = background;

        width = foreground.getWidth();
        height = foreground.getHeight();

        location.x = 0;
        location.y = 0;
        x2 = y2 = 0;
        y2 = 72;

        collisionBox = new Rectangle2D.Double(0, 0, 0, 0);
    }

    public ParallaxLayer(int dx) {
        this(dx, 0);
    }

    @Override
    public void render(Graphics2D g2) {
        AffineTransform backup = g2.getTransform();
        AffineTransform trans = new AffineTransform();
        trans.scale(2, 2);

        g2.transform(trans);
        g2.drawImage(background, (int) location.x, (int) location.y, null);
        g2.drawImage(followBack, (int) (width + location.x), (int) location.y, null);
        g2.drawImage(foreground, x2, y2, null);
        g2.drawImage(followFore, (width + x2) - 1, y2, null);

        g2.setTransform(backup);
    }

    @Override
    public void update(double deltaTime) {
        location.x = (int) ((location.x - (dx * .7)) % width);
        x2 = ((x2 - (dx)) % width);
    }

    @Override
    public Rectangle2D getCollisionBox() {
        return collisionBox;
    }
}
