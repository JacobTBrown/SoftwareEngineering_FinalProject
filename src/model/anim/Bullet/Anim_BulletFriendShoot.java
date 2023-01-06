package model.anim.Bullet;

import controller.Main;
import model.helper.AnimStrategy;
import model.npo.weapons.Bullet;

public class Anim_BulletFriendShoot implements AnimStrategy {
    Bullet ct;
    public Anim_BulletFriendShoot(Bullet context) {
        this.ct = context;
    }

    @Override
    public void animate(double deltaTime) {
        double rad = Math.atan2(1, Main.win.getWidth() - ct.location.x);
        ct.location.x += Bullet.UNIT_MOVE * Math.cos(rad);
    }
}