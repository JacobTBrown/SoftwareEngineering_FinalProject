package model.anim.AAGun;

import model.helper.AnimStrategy;
import model.npo.enemies.AAGun;

public class Anim_AAGunDeath implements AnimStrategy {

    AAGun context;

    public Anim_AAGunDeath(AAGun context) {
        this.context = context;
    }

    @Override
    public void animate(double deltaTime) {
        //TODO: Animate AAGun death
    }
}
