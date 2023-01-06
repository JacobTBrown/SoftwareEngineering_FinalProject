package model.anim.Rocket;

import model.helper.AnimStrategy;
import model.npo.weapons.Bullet;
import model.npo.weapons.Rocket;

import java.awt.*;

public class Anim_RocketDeath implements AnimStrategy {
    Rocket ct;
    public Anim_RocketDeath(Rocket context) { this.ct = context; }

    @Override
    public void animate(double deltaTime) {
        if(ct.size % 5 == 0)
            ct.color = Color.RED;
        else if (ct.size % 10 == 0)
            ct.color = Color.MAGENTA;
        else ct.color = Color.PINK;
        ++ct.size;

        if (ct.size >= Rocket.MAX_SIZE) {
            ct.done = true;
        }
    }
}
