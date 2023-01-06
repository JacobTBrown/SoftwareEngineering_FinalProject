package model.anim.Rocket;

import controller.Main;
import model.helper.AnimStrategy;
import model.npo.weapons.Bullet;
import model.npo.weapons.Rocket;

public class Anim_RocketEnemyShoot implements AnimStrategy {
    Rocket ct;

    double x, y;
    double rad;

    public Anim_RocketEnemyShoot(Rocket context) {
        this.ct = context;
        x = ct.enemyShooter.gun.getX2();
        y = ct.enemyShooter.gun.getY2();

        rad = Math.atan2(ct.target.y - y,
                ct.target.x - x);
    }

    @Override
    public void animate(double deltaTime) {
        ct.location.x += Rocket.UNIT_MOVE * Math.cos(rad) - Rocket.UNIT_MOVE;
        ct.location.y += Rocket.UNIT_MOVE * Math.sin(rad);
    }
}