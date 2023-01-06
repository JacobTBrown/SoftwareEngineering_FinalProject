package model.anim.FighterPlane;

import controller.Main;
import controller.queues.GameEvent;
import model.PlayerCharacter;
import model.helper.AnimStrategy;
import model.npo.enemies.FighterPlane;

import java.util.Random;

public class Anim_FPShooting implements AnimStrategy {

    FighterPlane context;

    public Anim_FPShooting(FighterPlane context) {
        this.context = context;
        rand = new Random();
        randInt = rand.nextInt(250);
        prevX = context.location.x;
    }

    Random rand;
    double prevX;
    int randInt;
    int shotCount;
    int y;
    int i = 0;

    double timer;

    @Override
    public void animate(double deltaTime) {
        if(timer >= 60) {
            shotCount = rand.nextInt(4);
            GameEvent event = new GameEvent();
            event.source = context;
            event.type = GameEvent.ENEMY_BULLET;
            Main.gameEventQueue.queue.add(event);
            i = 0;
            timer = 0;
        } else {
            timer += deltaTime;
            if(i < shotCount && timer >= 7) {
                GameEvent event = new GameEvent();
                event.source = context;
                event.type = GameEvent.ENEMY_BULLET;
                Main.gameEventQueue.queue.add(event);
                i++;
                timer = 0;
            }
        }

        y = (int) (Main.pc.location.y - context.location.y);
        if (y < 0) y *= -1;

        if (y > 35) {
            context.animStrategy = new Anim_FPFlying(context);
        }

        context.location.x -= FighterPlane.UNIT_MOVE;
    }
}
