package model.anim.BomberPlane;

import controller.Main;
import model.helper.AnimStrategy;
import model.npo.enemies.BomberPlane;

public class Anim_BPDeath implements AnimStrategy {

    BomberPlane context;

    public Anim_BPDeath(BomberPlane context) {
        this.context = context;
    }

    @Override
    public void animate(double deltaTime) {
        //TODO: Animate BomberPlane death
        if (context.location.y >= Main.win.canvas.height)
            context.done = true;

        context.location.y += BomberPlane.UNIT_MOVE_FALLING;
    }
}
