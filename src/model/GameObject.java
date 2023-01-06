package model;

import model.helper.AnimStrategy;
import model.enums.ENUM_OBJ;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public abstract class GameObject {

    public static final byte ID_PC = 0;
    public static final byte ID_MOUSE = 1;
    public static final byte ID_BACKGROUND = 2;
    public static final byte ID_FIXED = 3;
    public static final byte ID_FRIEND = 4;
    public static final byte ID_ENEMY = 5;

    public BufferedImage bufferImg;
    public AnimStrategy animStrategy;
    protected Rectangle2D collisionBox;

    public Point2D.Float location;
    public boolean done = false;
    public int hitCount;
    public byte id;
    public ENUM_OBJ name;

    public int width, height;

    public GameObject(ENUM_OBJ name, byte id, float x, float y) {
        this(x, y);
        this.id = id;
        this.name = name;
    }

    public GameObject(ENUM_OBJ name, byte id) {
        this();
        this.id = id;
        this.name = name;
    }

    public GameObject(float x, float y) {
        location = new Point2D.Float(x, y);
    }

    public GameObject() {
        this(0, 0);
    }

    public void setLocation(float x, float y) {
        location.x = x;
        location.y = y;
    }

    public boolean collideWith(GameObject o) {
        return this.getCollisionBox().intersects(o.getCollisionBox());
    }

    public abstract void render(Graphics2D g2);
    public abstract void update(double deltaTime);
    public abstract Rectangle2D getCollisionBox();
}
