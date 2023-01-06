package controller.queues;

import model.GameObject;

import java.util.EventObject;

public class GameEvent {
    public static final int ENEMY_BULLET = 0;
    public static final int ENEMY_BOMB = 1;
    public static final int ENEMY_ROCKET = 2;
    public static final int ENEMY_CREATE = 4;
    public static final int ENEMY_DESTROY = 5;

    /**Add score*/
    public static final int ADD_SCORE = 10;
    /**Subtract score*/
    public static final int SUB_SCORE = 11;

    public EventObject event;
    public GameObject source;
    public int type;
}
