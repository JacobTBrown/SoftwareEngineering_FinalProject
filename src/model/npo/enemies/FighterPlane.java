package model.npo.enemies;

import controller.Main;
import controller.observer.Observer;
import controller.observer.Subject;
import model.GameObject;
import model.anim.FighterPlane.Anim_FPDeath;
import model.anim.FighterPlane.Anim_FPFlying;
import model.helper.AssetsManager;
import model.enums.ENUM_OBJ;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.util.ArrayList;

public class FighterPlane extends GameObject implements Subject {

    public static int UNIT_MOVE = 5;
    public static int UNIT_MOVE_FALLING = 5;

    public boolean outOfBounds;

    ArrayList<Observer> listeners = new ArrayList<>();

    public FighterPlane(int x, int y) {
        super(ENUM_OBJ.FIGHTER, ID_ENEMY, x, y);
        if(y < 0) {
            location.y = y * -1;
        }
        outOfBounds = false;
        bufferImg = Main.assetsManager.getSprite(AssetsManager.SPR_FIGHTER_PLANE);

        AffineTransform at = AffineTransform.getScaleInstance(1, 1);
        at.scale(1.5, 1.5);
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        bufferImg = op.filter(bufferImg, null);

        width = bufferImg.getWidth();
        height = bufferImg.getHeight();

        animStrategy = new Anim_FPFlying(this);

        collisionBox = new Rectangle2D.Double(location.x, location.y, width, height);
    }


    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(bufferImg, (int) location.x - width / 2,(int) location.y - height / 2, null);
    }

    @Override
    public void update(double deltaTime) {
        if (hitCount > 0) {
            animStrategy = new Anim_FPDeath(this);
            collisionBox = null;
        } else if (location.x + width <= 0) {
            listeners.get(0).eventReceived();
            done = true;
            collisionBox = null;
        }

        animStrategy.animate(deltaTime);
        if (collisionBox != null) {
            collisionBox.setRect(location.x - (width / 2.0), location.y - (height / 2.0),
                    width, height);
        }

        if (done && outOfBounds) {
            notifyEvent();
        }
    }

    @Override
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
