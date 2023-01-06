package model.npo.weapons;

import controller.Main;
import model.GameObject;
import model.anim.Rocket.Anim_RocketDeath;
import model.anim.Rocket.Anim_RocketEnemyShoot;
import model.anim.Rocket.Anim_RocketFriendShoot;
import model.enums.ENUM_OBJ;
import model.npo.enemies.AAGun;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Rocket extends GameObject {

    public static final int UNIT_MOVE = 7;
    public static final int INIT_SIZE = 5;
    public static final int MAX_SIZE = 35;

    public int size = INIT_SIZE;

    public Point2D.Float target;   //where mouse was pressed;
    public Color color;

    public AAGun enemyShooter;

    public Rocket(int tx, int ty) {
        super(ENUM_OBJ.ROCKET, ID_FRIEND);
        color = Color.MAGENTA;

        target = new Point2D.Float(tx, ty);

        location.x = Main.pc.location.x + Main.pc.width / 2;
        location.y = Main.pc.location.y + Main.pc.height / 2;
        animStrategy = new Anim_RocketFriendShoot(this);

        collisionBox = new Rectangle2D.Double(location.x, location.y, size, size);
    }

    public Rocket(AAGun enemy) {
        super(ENUM_OBJ.ROCKET, ID_ENEMY);
        color = Color.MAGENTA;

        enemyShooter = enemy;

        target = new Point2D.Float(enemy.tx, enemy.ty);

        location.x = (int) enemy.gun.getX2() - size;
        location.y = (int) (enemy.gun.getY2() - size / 2.0);
        animStrategy = new Anim_RocketEnemyShoot(this);

        collisionBox = new Rectangle2D.Double(location.x, location.y, size, size);
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(1));
        g2.fillOval((int)super.location.x - size / 2, (int)super.location.y - size / 2, size , size);
    }

    @Override
    public void update(double deltaTime) {
        animStrategy.animate(deltaTime);

        if (hitCount > 0) {
            animStrategy = new Anim_RocketDeath(this);
            collisionBox = null;
        } else if (location.x > Main.win.canvas.getWidth() || location.x < 0 || location.y > Main.win.canvas.getHeight() || location.y < 0) {
            done = true;
            collisionBox = null;
        }

        if (collisionBox != null) {
            collisionBox = new Rectangle2D.Double(location.x, location.y, size, size);
        }
    }

    @Override
    public Rectangle2D getCollisionBox() {
        return collisionBox;
    }
}
