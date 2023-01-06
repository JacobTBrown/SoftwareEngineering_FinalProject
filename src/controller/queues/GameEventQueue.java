package controller.queues;

import controller.Main;
import controller.observer.events.Event_AddPoint;
import controller.observer.events.Event_CreateNewEnemy;
import model.npo.enemies.AAGun;
import model.npo.enemies.BomberPlane;

import java.util.LinkedList;

public class GameEventQueue {

    public LinkedList<GameEvent> queue = new LinkedList<>();

    public void processInputEvents() {
        while(!queue.isEmpty()) {
            GameEvent gameEvent = queue.removeFirst();

            switch(gameEvent.type) {
                case GameEvent.ENEMY_BULLET:
                    Main.gameData.addEnemyBullet(gameEvent.source);
                    break;
                case GameEvent.ENEMY_BOMB:
                    Main.gameData.addEnemyBomb((BomberPlane) gameEvent.source);
                    break;
                case GameEvent.ENEMY_ROCKET:
                    Main.gameData.addEnemyRocket((AAGun) gameEvent.source, 0, 0);
                    break;
                case GameEvent.ENEMY_CREATE:
                    Event_CreateNewEnemy ue = (Event_CreateNewEnemy) gameEvent.event;

                    switch(gameEvent.source.name) {
                        case FIGHTER:
                            Main.gameData.addFighterWithListener(ue.getX(), ue.getY());
                            break;
                        case BOMBER:
                            Main.gameData.addBomberWithListener(ue.getX(), ue.getY());
                            break;
                        case AAGUN:
                            Main.gameData.addAAGunWithListener(ue.getX(), ue.getY());
                            break;
                        default:
                            continue;
                    }
                    break;
                case GameEvent.ADD_SCORE:
                    Event_AddPoint ap = (Event_AddPoint) gameEvent.event;
                    Main.gameData.addToScore(ap.getPoints());
                    int score = Main.gameData.getScore();
                    Main.win.scoreField.setText("" + score);
                    break;
                default:
                    //System.out.println("DEFAULT CLAUSE: GAME");
                    break;
            }
        }
    }

    public void clear() {
        queue.clear();
    }
}
