package model.anim.FighterPlane;

import controller.Main;
import model.helper.AnimStrategy;
import model.npo.enemies.FighterPlane;

public class Anim_FPDeath implements AnimStrategy {

    FighterPlane context;

    public Anim_FPDeath(FighterPlane context) {
        this.context = context;
    }

    @Override
    public void animate(double deltaTime) {
        if (context.location.y >= Main.win.canvas.height) {
            context.done = true;
            context.outOfBounds = true;
        }

        context.location.x -= FighterPlane.UNIT_MOVE - 1 * deltaTime;
        context.location.y += FighterPlane.UNIT_MOVE_FALLING;
    }
}
