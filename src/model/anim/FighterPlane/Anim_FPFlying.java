package model.anim.FighterPlane;

import controller.Main;
import model.PlayerCharacter;
import model.helper.AnimStrategy;
import model.npo.enemies.FighterPlane;

import java.util.Random;

public class Anim_FPFlying implements AnimStrategy {

    FighterPlane context;

    public Anim_FPFlying(FighterPlane context) {
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

        x = (int) ((context.location.x - context.width/2) - (Main.pc.location.x));
        if (Main.pc.location.y > context.location.y)
            y = (int) (Main.pc.location.y - context.location.y);
        else
            y = (int) (context.location.y - Main.pc.location.y);
        if (x <= 1200 && (y < 35 && y > 0)) {
            context.animStrategy = new Anim_FPShooting(context);
        }

        context.location.x -= FighterPlane.UNIT_MOVE;
    }
}
