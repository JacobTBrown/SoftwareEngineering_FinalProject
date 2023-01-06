package controller.observer.events;

import controller.Main;
import model.PlayerCharacter;
import model.helper.AssetsManager;

import java.util.EventObject;
import java.util.Random;

public class Event_CreateNewEnemy extends EventObject {
    private int x;
    private int y;
    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public Event_CreateNewEnemy(Object source) {
        super(source);

        if (source == "fighter") {
            Random rand = new Random();
            int n = Main.win.canvas.getWidth();
            this.x = (int) ((x + n) * 1.1);

            double p;

            n = rand.nextInt(200) + 1;
            p = rand.nextDouble() * 3 + .1;
            this.y = (int) ((y + n) * p);
        } else if (source == "bomber") {
            Random rand = new Random();
            int rX = rand.nextInt(1000);
            int rY = rand.nextInt(300);
            y = (int) Main.pc.location.y - rY;
            if (y < 0) y = Main.assetsManager.getSprite(AssetsManager.SPR_BOMBER_PLANE).getHeight();
            else y = (int) Main.pc.location.y - rY;

            x = Main.win.canvas.getWidth() + rX;
        } else if (source == "aagun") {
            Random ran = new Random();
            int r = ran.nextInt(1000);
            x = Main.win.canvas.getWidth() + r;
            y = Main.win.canvas.getHeight() - 60;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
