package controller.observer;

import controller.Main;
import controller.observer.events.Event_CreateNewEnemy;
import controller.queues.GameEvent;
import model.GameObject;

public class Observer_BPAddNew implements Observer {
    GameObject creator;

    public Observer_BPAddNew(GameObject creator) {
        this.creator = creator;
    }

    @Override
    public void eventReceived() {
        GameEvent event = new GameEvent();
        event.event = new Event_CreateNewEnemy("bomber");
        event.type = GameEvent.ENEMY_CREATE;
        event.source = creator;
        Main.gameEventQueue.queue.add(event);
    }
}
