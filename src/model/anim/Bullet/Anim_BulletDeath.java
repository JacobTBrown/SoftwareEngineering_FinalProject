package model.anim.Bullet;

import model.helper.AnimStrategy;
import model.npo.weapons.Bullet;

import java.awt.*;

public class Anim_BulletDeath implements AnimStrategy {
    Bullet ct;
    public Anim_BulletDeath(Bullet context) { this.ct = context; }

    @Override
    public void animate(double deltaTime) {
        if(ct.size % 5 == 0)
            ct.color = Color.RED;
        else if (ct.size % 10 == 0)
            ct.color = Color.ORANGE;
        else ct.color = Color.YELLOW;
        ++ct.size;

        if (ct.size >= Bullet.MAX_SIZE) {
            ct.done = true;
        }
    }
}
