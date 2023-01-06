package model.npo.weapons;

import controller.Main;
import model.GameObject;
import model.PlayerCharacter;
import model.anim.Bullet.Anim_BulletDeath;
import model.anim.Bullet.Anim_BulletEnemyShoot;
import model.anim.Bullet.Anim_BulletFriendShoot;
import model.enums.ENUM_OBJ;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Bullet extends GameObject {

    public static final int UNIT_MOVE = 10;
    public static final int INIT_SIZE = 6;
    public static final int MAX_SIZE = 35;

    public int size = INIT_SIZE;

    public Color color;

    public Bullet() {
        super(ENUM_OBJ.BULLET, ID_FRIEND);
        color = Color.RED;

        PlayerCharacter pc = (PlayerCharacter) Main.gameData.gameObjects.get(Main.INDEX_PC);
        location.x = pc.location.x + pc.width - 5;
        location.y = pc.location.y + pc.height / 2 + 2;
        animStrategy = new Anim_BulletFriendShoot(this);

        collisionBox = new Rectangle2D.Double(location.x, location.y, size, size);
    }

    public Bullet(GameObject enemy) {
        super(ENUM_OBJ.BULLET, ID_ENEMY);
        color = Color.ORANGE;

        location.x = enemy.location.x - (enemy.width / 2);
        location.y = (int) (enemy.location.y - size / 2.0);
        animStrategy = new Anim_BulletEnemyShoot(this);

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
            animStrategy = new Anim_BulletDeath(this);
            collisionBox = null;
        } else if (location.x > Main.win.canvas.getWidth() || location.x < 0) {
            done = true;
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
}