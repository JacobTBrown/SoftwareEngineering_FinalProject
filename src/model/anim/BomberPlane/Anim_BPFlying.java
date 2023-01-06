package model.anim.BomberPlane;

import controller.Main;
import model.PlayerCharacter;
import model.helper.AnimStrategy;
import model.npo.enemies.BomberPlane;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

public class Anim_BPFlying implements AnimStrategy {

    BomberPlane context;

    public Anim_BPFlying(BomberPlane context) {
        this.context = context;
    }

    int x;

    @Override
    public void animate(double deltaTime) {
        if (Main.pc.location.x > context.location.x)
            x = (int) ((Main.pc.location.x + Main.pc.width/2) - context.location.x);
        else
            x = (int) (context.location.x - (Main.pc.location.x + Main.pc.width / 2));

        if (x < 150) {
            context.animStrategy = new Anim_BPShooting(context);
        }
    }
}
