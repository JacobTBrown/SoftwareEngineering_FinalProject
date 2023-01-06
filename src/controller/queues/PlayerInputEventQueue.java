package controller.queues;

import controller.Main;
import model.PlayerCharacter;
import model.npo.weapons.Bomb;
import model.npo.weapons.Bullet;
import model.npo.MousePointer;
import model.npo.weapons.Rocket;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

public class PlayerInputEventQueue {

    public LinkedList<InputEvent> queue;
    PlayerCharacter pc;

    public PlayerInputEventQueue() {
        queue = new LinkedList<>();
    }

    public void processInputEvents() {
        while(!queue.isEmpty() && queue != null) {
            InputEvent inputEvent = queue.removeFirst();
            if (inputEvent != null) {
                switch (inputEvent.type) {
                    case InputEvent.MOUSE_PRESSED:
                        MouseEvent e = (MouseEvent) inputEvent.event;
                        if (e.getButton() == MouseEvent.BUTTON1) {           //Left click
                            if(Main.pc.canShoot) {
                                Bullet m = new Bullet();
                                Main.gameData.gameObjects.add(m);

                                Main.pc.gunTimer = 0;
                                Main.pc.canShoot = false;
                            }
                        } else if (e.getButton() == MouseEvent.BUTTON3) {   //Right click
                            if (Main.pc.canRocket) {
                                MouseEvent me = (MouseEvent) inputEvent.event;
                                int tx = me.getX();
                                int ty = me.getY();

                                Rocket r = new Rocket(tx, ty);
                                Main.gameData.gameObjects.add(r);

                                Main.pc.rocketTimer = 0;
                                Main.pc.canRocket = false;
                            }
                        }
                        break;
                    case InputEvent.MOUSE_DRAGGED:
                    case InputEvent.MOUSE_MOVED:
                        MousePointer mp = (MousePointer) Main.gameData.gameObjects.get(Main.INDEX_MOUSE_POINTER);
                        MouseEvent me = (MouseEvent) inputEvent.event;
                        mp.location.x = me.getX();
                        mp.location.y = me.getY();

                        mp.rotate();
                        break;
                    case InputEvent.KEY_PRESSED:
                        KeyEvent ke = (KeyEvent) inputEvent.event;
                        int k = ke.getKeyCode();

                        switch(k) {
                            case KeyEvent.VK_W:
                            case KeyEvent.VK_UP:
                                Main.pc.state = PlayerCharacter.STATE_ACCELERATE_UP;
                                Main.pc.isGoingUp = true;
                                break;
                            case KeyEvent.VK_S:
                            case KeyEvent.VK_DOWN:
                                Main.pc.state = PlayerCharacter.STATE_ACCELERATE_DOWN;
                                Main.pc.isGoingDown = true;
                                break;
                            case KeyEvent.VK_SPACE:
                                if(Main.pc.canBomb) {
                                    Bomb b = new Bomb();
                                    Main.gameData.gameObjects.add(b);

                                    Main.pc.bombTimer = 0;
                                    Main.pc.canBomb = false;
                                }
                                break;
                        }
                        break;
                    case InputEvent.KEY_RELEASED:
                        ke = (KeyEvent) inputEvent.event;
                        k = ke.getKeyCode();

                        switch(k) {
                            case KeyEvent.VK_W:
                            case KeyEvent.VK_UP:
                                Main.pc.state = PlayerCharacter.STATE_DECELERATE_UP;
                                Main.pc.isGoingUp = false;
                                Main.pc.delta = Main.pc.angle;
                                break;
                            case KeyEvent.VK_S:
                            case KeyEvent.VK_DOWN:
                                Main.pc.state = PlayerCharacter.STATE_DECELERATE_DOWN;
                                Main.pc.isGoingDown = false;
                                Main.pc.delta = Main.pc.angle;
                                break;
                        }
                        break;
                    default:
                        //System.out.println("DEFAULT CLAUSE: INPUT");
                        break;
                }
            }
        }
    }

    public void clear() {
        queue.clear();
    }
}
