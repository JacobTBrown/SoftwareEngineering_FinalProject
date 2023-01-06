package model.npo.enemies;

import controller.Main;
import controller.observer.Observer;
import controller.observer.Subject;
import model.GameObject;
import model.anim.BomberPlane.Anim_BPDeath;
import model.anim.BomberPlane.Anim_BPFlying;
import model.helper.AssetsManager;
import model.enums.ENUM_OBJ;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.util.ArrayList;

public class BomberPlane extends GameObject implements Subject {

    public static int UNIT_MOVE = 3;
    public static int UNIT_MOVE_FALLING = 2;

    public boolean flag = false;
    public boolean outOfBounds;
    public boolean canBounce = true;

    ArrayList<Observer> listeners = new ArrayList<>();

    public BomberPlane(int x, int y) {
        super(ENUM_OBJ.BOMBER, GameObject.ID_ENEMY, x, y);
        bufferImg = Main.assetsManager.getSprite(AssetsManager.SPR_BOMBER_PLANE);

        AffineTransform at = AffineTransform.getScaleInstance(1, 1);
        at.scale(1.5, 1.5);
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        bufferImg = op.filter(bufferImg, null);

        width = bufferImg.getWidth();
        height = bufferImg.getHeight();
        animStrategy = new Anim_BPFlying(this);

        collisionBox = new Rectangle2D.Double(location.x, location.y, width, height);
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(bufferImg, (int) location.x - width/2,(int) location.y - height/2, null);
    }

    @Override
    public void update(double deltaTime) {
        location.x -= BomberPlane.UNIT_MOVE;

        if (hitCount > 0) {
            animStrategy = new Anim_BPDeath(this);
            collisionBox = null;
            canBounce = false;
        }

        if(location.x + width <= 0) {
            done = true;
            listeners.get(0).eventReceived();
        }

        animStrategy.animate(deltaTime);

        if(collisionBox != null) {
            collisionBox.setRect(location.x - (width / 2.0), location.y - (height / 2.0),
                    width, height);
        }

        if (done && !canBounce) {
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
        listeners.trimToSize();
    }

    @Override
    public void detachListener(Observer o) {
        listeners.remove(o);
        listeners.trimToSize();
    }

    @Override
    public void notifyEvent() {
        for (var o: listeners) {
            o.eventReceived();
        }
    }
}
