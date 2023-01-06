package model.npo;

import controller.Main;
import model.GameObject;
import model.PlayerCharacter;
import model.enums.ENUM_OBJ;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class MousePointer extends GameObject {

    public final int SIZE = 15;

    public Line2D line_1, line_2;

    public MousePointer(int x, int y) {
        super(ENUM_OBJ.MOUSE, ID_MOUSE, x, y);
        line_1 = new Line2D.Float((int) location.x - SIZE, (int) location.y,
                (int) location.x + SIZE, (int) location.y);
        line_2 = new Line2D.Float((int) location.x, (int) location.y - SIZE,
                (int) location.x, (int) location.y + SIZE);

        collisionBox = new Rectangle2D.Double(0, 0, 15 , 15);
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(Color.CYAN);
        g2.draw(line_1);
        g2.draw(line_2);
        g2.setColor(Color.GREEN);
        g2.drawOval((int) (location.x - (25/2.0f)), (int) (location.y - (25/2.0f)), 25, 25);
        g2.setColor(Color.YELLOW);
        g2.drawOval((int) (location.x - (15/2.0f)), (int) (location.y - (15/2.0f)), 15, 15);
        g2.setColor(Color.RED);
        g2.drawOval((int) (location.x - (5/2.0f)), (int) (location.y - (5/2.0f)), 5, 5);

        g2.draw(collisionBox);  //TODO: Remove
    }

    public void rotate() {
        PlayerCharacter playerCharacter = (PlayerCharacter) Main.gameData.gameObjects.get(Main.INDEX_PC);
        float tx = playerCharacter.location.x;
        float ty = playerCharacter.location.y;
        double rad = Math.atan2(ty - location.y, tx - location.x);

        float line1_x = (float) (SIZE * Math.cos(rad));
        float line1_y = (float) (SIZE * Math.sin(rad));
        float line2_x = (float) (SIZE * -Math.sin(rad));
        float line2_y = (float) (SIZE * Math.cos(rad));

        line_1.setLine(location.x + line1_x, location.y + line1_y, location.x - line1_x, location.y - line1_y);
        line_2.setLine(location.x + line2_x, location.y + line2_y, location.x - line2_x, location.y - line2_y);
    }

    @Override
    public void update(double deltaTime) {
        collisionBox.setRect(location.x - (width / 2.0), location.y - (height / 2.0),
                width, height);
    }

    @Override
    public Rectangle2D getCollisionBox() {
        return collisionBox;
    }
}
