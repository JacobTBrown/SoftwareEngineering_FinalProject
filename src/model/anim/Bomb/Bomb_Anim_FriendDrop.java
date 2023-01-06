package model.anim.Bomb;

import controller.Main;
import model.helper.AnimStrategy;
import model.npo.weapons.Bomb;

public class Bomb_Anim_FriendDrop implements AnimStrategy {
    Bomb ct;
    public Bomb_Anim_FriendDrop(Bomb context) {
        this.ct = context;
    }

    @Override
    public void animate(double deltaTime) {
        double rad = Math.atan2(Main.win.getHeight() - ct. location.y, 1);
        ct.location.y += Bomb.UNIT_MOVE * Math.sin(rad);
    }
}