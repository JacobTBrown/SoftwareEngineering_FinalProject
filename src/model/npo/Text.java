package model.npo;

import model.GameObject;
import model.enums.ENUM_OBJ;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Text extends GameObject {

    private String text;
    private Color color;
    private Font font;

    public Text(String text, int x, int y, Color color, Font font) {
        super(ENUM_OBJ.TEXT, ID_FIXED, x, y);
        this.text = text;
        this.color = color;
        this.font = font;

        collisionBox = new Rectangle2D.Double(0, 0, 0, 0);
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setFont(font);
        g2.setColor(color);
        g2.drawString(text, (int) location.x, (int) location.y);
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public Rectangle2D getCollisionBox() {
        return collisionBox;
    }
}
