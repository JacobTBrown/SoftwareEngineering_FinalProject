package model.anim.AAGun;

import controller.Main;
import model.PlayerCharacter;
import model.helper.AnimStrategy;
import model.npo.enemies.AAGun;

public class Anim_AAGunMove implements AnimStrategy {

    AAGun context;

    public Anim_AAGunMove(AAGun context) {
        this.context = context;
    }

    int x, y;

    @Override
    public void animate(double deltaTime) {
        if (context.location.x + context.width/2 > Main.win.canvas.getWidth()) {
            context.outOfBounds = true;
        } else if (context.location.x - context.width/2 <= 0){
            context.outOfBounds = false;
        }

        x = (int) ((context.location.x - context.TURRET_SIZE / 2) - (Main.pc.location.x));
        if (x <= 1600 && !context.doneShooting) {
            context.animStrategy = new Anim_AAGunShoot(context);
        }
    }
}
