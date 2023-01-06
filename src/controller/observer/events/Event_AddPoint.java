package controller.observer.events;

import java.util.EventObject;

public class Event_AddPoint extends EventObject {
    private byte points;
    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public Event_AddPoint(Object source) {
        super(source);

        if (source == "fighter") {
            points = 1;
        } else if (source == "bomber") {
            points = 2;
        } else if (source == "aagun") {
            points = 5;
        }
    }

    public byte getPoints() {
        return points;
    }
}

