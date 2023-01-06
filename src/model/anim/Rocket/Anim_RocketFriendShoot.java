package model.anim.Rocket;

import controller.Main;
import model.helper.AnimStrategy;
import model.npo.weapons.Bullet;
import model.npo.weapons.Rocket;

public class Anim_RocketFriendShoot implements AnimStrategy {
    Rocket ct;
    public Anim_RocketFriendShoot(Rocket context) {
        this.ct = context;
    }

    @Override
    public void animate(double deltaTime) {
        double rad = Math.atan2(ct.target.y - (Main.pc.location.y + Main.pc.height / 2),
                ct.target.x - (Main.pc.location.x + Main.pc.width / 2));
        ct.location.x += Rocket.UNIT_MOVE * Math.cos(rad);
        ct.location.y += Rocket.UNIT_MOVE * Math.sin(rad);
    }
}