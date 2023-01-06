package model.npo.enemies;

import controller.Main;
import controller.observer.Observer;
import controller.observer.Subject;
import model.GameObject;
import model.anim.AAGun.Anim_AAGunMove;
import model.helper.AssetsManager;
import model.enums.ENUM_OBJ;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

public class AAGun extends GameObject implements Subject {

    public static int UNIT_MOVE = 8;

    public static final int STATE_IDLE = 0;
    public static final int STATE_SHOOTING = 1;
    public static final int STATE_DYING = 2;
    public static final int STATE_DONE = 3;

    ArrayList<Observer> listeners = new ArrayList<>();

    public boolean outOfBounds;

    private Random timer;
    private int timerCount;

    public final int GUN_LEN = 40;
    public final int TURRET_SIZE = 25;
    public Rectangle2D turret;
    public Line2D gun;

    public boolean doneShooting = false;

    public AAGun(int x, int y) {
        super(ENUM_OBJ.AAGUN, ID_ENEMY, x, y);
        outOfBounds = false;

        bufferImg = Main.assetsManager.getSprite(AssetsManager.SPR_AA_GUN);

        width = bufferImg.getWidth();
        height = bufferImg.getHeight();

        animStrategy = new Anim_AAGunMove(this);

        collisionBox = new Rectangle2D.Double(location.x, location.y, width, height);

        timer = new Random();
        timerCount = timer.nextInt(2000);

        turret = new Rectangle2D.Float(location.x, location.y, TURRET_SIZE, TURRET_SIZE + 10);
        gun = new Line2D.Float(x, y, 3, y - GUN_LEN);
    }


    @Override
    public void render(Graphics2D g2) {
        g2.setColor(Color.BLUE);
        g2.setStroke(new BasicStroke(6));
        g2.draw(gun);
        g2.draw(turret);
    }

    public float tx, ty;

    @Override
    public void update(double deltaTime) {
        tx = Main.pc.location.x + 400;
        ty = Main.pc.location.y + 100;
        double rad = Math.atan2(ty - super.location.y, tx - super.location.x);
        float gun_x = (float) (GUN_LEN * Math.cos(rad));
        float gun_y = (float) (GUN_LEN * Math.sin(rad));

        if (hitCount > 0) {
            outOfBounds = false;
            done = true;
            collisionBox = null;
            notifyEvent();
        } else if (location.x < timerCount * -1) {
            collisionBox = null;
            listeners.get(0).eventReceived();
            done = true;
        }

        animStrategy.animate(deltaTime);

        location.x -= AAGun.UNIT_MOVE;

        gun.setLine(location.x, location.y, location.x + gun_x, location.y + gun_y);
        turret.setRect( location.x - TURRET_SIZE / 2, location.y - (TURRET_SIZE + 10) / 4, TURRET_SIZE, TURRET_SIZE + 10);

        if (collisionBox != null) {
            collisionBox.setRect(location.x - (width / 2.0), location.y - (height / 2.0),
                    width, height);
        }

        if (done && outOfBounds) {
            notifyEvent();
        }
    }

    public Rectangle2D getCollisionBox() {
        return collisionBox;
    }

    @Override
    public void attachListener(Observer o) {
        listeners.add(o);
    }

    @Override
    public void detachListener(Observer o) {
        listeners.remove(o);
    }

    @Override
    public void notifyEvent() {
        for (var o: listeners) {
            o.eventReceived();
        }
    }
}
