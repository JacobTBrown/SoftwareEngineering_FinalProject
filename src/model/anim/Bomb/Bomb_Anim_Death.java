package model.anim.Bomb;

import model.helper.AnimStrategy;
import model.npo.weapons.Bomb;

import java.awt.*;

public class Bomb_Anim_Death implements AnimStrategy {
    Bomb ct;
    public Bomb_Anim_Death(Bomb context) { this.ct = context; }

    @Override
    public void animate(double deltaTime) {

        if(ct.size % 5 == 0)
            ct.color = Color.RED;
        else if (ct.size % 10 == 0)
            ct.color = Color.GRAY;
        else ct.color = Color.DARK_GRAY;
        ++ct.size;

        if (ct.size >= Bomb.MAX_SIZE) {
            ct.done = true;
        }
    }
}
