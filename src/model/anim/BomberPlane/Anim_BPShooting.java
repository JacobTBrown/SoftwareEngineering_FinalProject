package model.anim.BomberPlane;

import controller.Main;
import controller.queues.GameEvent;
import model.anim.FighterPlane.Anim_FPFlying;
import model.helper.AnimStrategy;
import model.npo.enemies.BomberPlane;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.util.Random;

public class Anim_BPShooting implements AnimStrategy {

    BomberPlane context;

    Random ran;

    public Anim_BPShooting(BomberPlane context) {
        this.context = context;

        ran = new Random();
        x2 = ran.nextInt(3);
    }

    int bombCount;
    int x, x2;
    double timer;

    @Override
    public void animate(double deltaTime) {
        if(timer >= 20) {
            GameEvent event = new GameEvent();
            event.source = context;
            event.type = GameEvent.ENEMY_BOMB;
            Main.gameEventQueue.queue.add(event);
            bombCount = 0;
            timer = 0;
        } else {
            timer += deltaTime;
            if(bombCount < x2 && timer >= 20) {
                GameEvent event = new GameEvent();
                event.source = context;
                event.type = GameEvent.ENEMY_BOMB;
                Main.gameEventQueue.queue.add(event);
                bombCount++;
                timer = 0;
            }
        }

        if (Main.pc.location.x > context.location.x)
            x = (int) ((Main.pc.location.x + Main.pc.width/2) - context.location.x);
        else
            x = (int) (context.location.x - (Main.pc.location.x + Main.pc.width / 2));

        if (x > 50) {
            context.animStrategy = new Anim_BPShooting(context);
        }
    }
}
