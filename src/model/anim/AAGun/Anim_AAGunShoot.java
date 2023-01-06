package model.anim.AAGun;

import controller.Main;
import controller.queues.GameEvent;
import model.anim.FighterPlane.Anim_FPFlying;
import model.helper.AnimStrategy;
import model.npo.enemies.AAGun;

public class Anim_AAGunShoot implements AnimStrategy {

    AAGun context;

    public Anim_AAGunShoot(AAGun context) {
        this.context = context;
        x = 0;
        rocketCount = 0;
        timer = 0;
    }

    int x;
    int rocketCount;
    double timer;

    @Override
    public void animate(double deltaTime) {
        if (context.location.x + context.width/2 > Main.win.canvas.getWidth()) {
            context.outOfBounds = true;
        } else if (context.location.x - context.width/2 <= 0){
            context.outOfBounds = false;
        }

        if(timer >= 100) {
            GameEvent event = new GameEvent();
            event.source = context;
            event.type = GameEvent.ENEMY_ROCKET;
            Main.gameEventQueue.queue.add(event);
            rocketCount = 0;
            timer = 0;
        } else {
            timer += deltaTime;
            if(rocketCount < 2 && timer >= 12) {
                GameEvent event = new GameEvent();
                event.source = context;
                event.type = GameEvent.ENEMY_ROCKET;
                Main.gameEventQueue.queue.add(event);
                rocketCount++;
                timer = 0;
            }
        }

        if (context.location.x <= Main.pc.location.x + 300) {
            context.doneShooting = true;
            context.animStrategy = new Anim_AAGunMove(context);
        }
    }
}
