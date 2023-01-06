package model.npo.weapons;

import controller.Main;
import controller.observer.Observer;
import controller.observer.Subject;
import model.GameObject;
import model.PlayerCharacter;
import model.anim.Bomb.Bomb_Anim_Death;
import model.anim.Bomb.Bomb_Anim_EnemyDrop;
import model.anim.Bomb.Bomb_Anim_FriendDrop;
import model.enums.ENUM_OBJ;
import model.npo.enemies.BomberPlane;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Bomb extends GameObject implements Subject {

    public static final int UNIT_MOVE = 6;
    public static final int INIT_SIZE = 6;
    public static final int MAX_SIZE = 40;

    public int size = INIT_SIZE;

    public Color color;

    public Bomb() {
        super(ENUM_OBJ.BOMB, ID_FRIEND);
        color = Color.DARK_GRAY;

        PlayerCharacter pc = (PlayerCharacter) Main.gameData.gameObjects.get(Main.INDEX_PC);
        location.x = pc.location.x + pc.width / 2;
        location.y = pc.location.y + pc.height;
        animStrategy = new Bomb_Anim_FriendDrop(this);

        collisionBox = new Rectangle2D.Double(location.x, location.y, size, size);
    }

    public Bomb(BomberPlane enemy) {
        super(ENUM_OBJ.BOMB, ID_ENEMY);
        color = Color.DARK_GRAY;

        location.x = enemy.location.x - size;
        location.y = (int) (enemy.location.y - size / 2.0);
        animStrategy = new Bomb_Anim_EnemyDrop(this);

        collisionBox = new Rectangle2D.Double(location.x, location.y, size, size);
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(1));
        g2.fillOval((int) (super.location.x - size / 2.0), (int) (super.location.y - size / 2.0), size , size);
        //g2.draw(collisionBox);
    }

    @Override
    public void update(double deltaTime) {
        animStrategy.animate(deltaTime);

        if (hitCount > 0) {
            animStrategy = new Bomb_Anim_Death(this);
            collisionBox = null;
        } else if (location.y > Main.win.canvas.getHeight() - width / 2) {
            animStrategy = new Bomb_Anim_Death(this);
            collisionBox = null;
        }

        if (collisionBox != null) {
            collisionBox.setRect(location.x - (size / 2.0), location.y - (size / 2.0),
                    size, size);
        }
    }

    @Override
    public Rectangle2D getCollisionBox() {
        return collisionBox;
    }

    @Override
    public void attachListener(Observer o) {

    }

    @Override
    public void detachListener(Observer o) {

    }

    @Override
    public void notifyEvent() {

    }
}