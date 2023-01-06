package controller.observer;

import controller.Main;
import controller.observer.events.Event_AddPoint;
import controller.queues.GameEvent;

public class Observer_BPAddPoint implements Observer {
    @Override
    public void eventReceived() {
        GameEvent event = new GameEvent();
        event.event = new Event_AddPoint("bomber");
        event.type = GameEvent.ADD_SCORE;
        Main.gameEventQueue.queue.add(event);
    }
}
