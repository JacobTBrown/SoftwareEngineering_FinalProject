package model;

import controller.Main;
import model.helper.AssetsManager;
import model.npo.MousePointer;
import model.enums.ENUM_OBJ;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;

public class PlayerCharacter extends GameObject {

    public static final int STATE_IDLE = 0;
    public static final int STATE_ACCELERATE_UP = 1;
    public static final int STATE_ACCELERATE_DOWN = 2;
    public static final int STATE_DECELERATE_UP = 3;
    public static final int STATE_DECELERATE_DOWN = 4;

    public static final int UNIT_MAXSPD = 12;

    public double delta, angle;
    public boolean isGoingUp, isGoingDown, canShoot, canBomb, canRocket;

    private MousePointer mousePointer;
    public int state;
    public int hp;

    public PlayerCharacter(int x, int y) {
        super(ENUM_OBJ.PC, ID_PC, x, y);
        hp = 20;
        bufferImg = Main.assetsManager.sprites.get(AssetsManager.SPR_PLANE);

        AffineTransform at = AffineTransform.getScaleInstance(1, 1);
        at.scale(1.5, 1.5);
        AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        bufferImg = op.filter(bufferImg, null);

        width = bufferImg.getWidth();
        height = bufferImg.getHeight();

        collisionBox = new Rectangle2D.Double(location.x, location.y, width - 25, height - 25);

        delta = 0.0;
        angle = 0.0;

        rad = rad2 = 0;

        mousePointer = (MousePointer) Main.gameData.gameObjects.get(Main.INDEX_MOUSE_POINTER);
    }

    @Override
    public void render(Graphics2D g2) {
        AffineTransform backup = g2.getTransform();
        AffineTransform trans = AffineTransform.getScaleInstance(1, 1);
        trans.rotate(rad2, location.x + width / 2, location.y + height / 2);

        g2.transform(trans);
        g2.drawImage(bufferImg, (int) location.x, (int) location.y , null);

        g2.setTransform(backup);
    }

    double rad, rad2;
    public double gunTimer, rocketTimer, bombTimer;
    public double speed_y;
    public double acc;

    @Override
    public void update(double deltaTime) {
        float tx = mousePointer.location.x;
        float ty = mousePointer.location.y;
        rad = Math.atan2(ty - (super.location.y + height/2), tx - (super.location.x + (width /2)));

        delta /= 2;
        bombTimer += deltaTime;
        rocketTimer += deltaTime;
        gunTimer += deltaTime;

        if(gunTimer > 35) {
            canShoot = true;
        }
        if (rocketTimer > 60) {
            canRocket = true;
        }
        if (bombTimer > 90) {
            canBomb = true;
        }

        speed_y = Math.sqrt(UNIT_MAXSPD) * (acc / 2);

        if (state == STATE_IDLE) {
            acc = 0.0;
        } else {
            if (acc == 0.0) acc = 0.1;
        }

        if (speed_y > UNIT_MAXSPD) speed_y = UNIT_MAXSPD;
        if (acc > 2.5) acc = 2.5;
        else if (acc <= 0) state = STATE_IDLE;

        if (state == STATE_ACCELERATE_UP) {
            location.y -= speed_y;
            acc += .021 + (deltaTime * .08);
        } else if (state == STATE_ACCELERATE_DOWN) {
            location.y += speed_y;
            acc += .021 + (deltaTime * .08);
        } else if (state == STATE_DECELERATE_UP) {
            location.y -= speed_y;
            acc -= .03 + (deltaTime * .05);
        } else if (state == STATE_DECELERATE_DOWN) {
            location.y += speed_y;
            acc -= .03 + (deltaTime * .05);
        }

        if(isGoingUp) {
            if(angle > 0) angle -= delta;
            else angle = (1.01 + (angle * angle) / 2) * -1;
        } else if (isGoingDown) {
            if(angle < 0) {
                if(delta < 0) angle -= delta;
                else angle += delta;
            }
            else angle = (1.01 + (angle * angle) / 2);
        }
        if(!isGoingDown || !isGoingUp) {
            if(angle > 0) {
                if(delta > 0) angle -= delta;
                else angle -= delta * -1;
            }
            else if(angle < 0) {
                if(delta > 0) angle += delta;
                else angle += delta * -1;
            }
        }
        if (angle <= -70.0) angle = -70.0;
        else if (angle >= 70.0) angle = 70.0;

        //update angle
        rad2 = Math.atan(angle);

        if(location.y <= 0) {
            location.y = 0;
        } else if (location.y >= Main.win.canvas.getHeight() - height) {
            location.y = Main.win.canvas.getHeight() - height;
        }

        if (hp <= 0) {
            done = true;
        }

        collisionBox.setRect(location.x, location.y, width, height);
    }

    @Override
    public Rectangle2D getCollisionBox() {
        return collisionBox;
    }
}
