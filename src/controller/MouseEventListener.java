package controller;

import controller.queues.InputEvent;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseEventListener extends MouseAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
        if(e != null) {
            InputEvent inputEvent = new InputEvent();
            inputEvent.event = e;
            inputEvent.type = InputEvent.MOUSE_PRESSED;
            Main.playerInputEventQueue.queue.add(inputEvent);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(e != null) {
            InputEvent inputEvent = new InputEvent();
            inputEvent.event = e;
            inputEvent.type = InputEvent.MOUSE_DRAGGED;
            Main.playerInputEventQueue.queue.add(inputEvent);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(e != null) {
            InputEvent inputEvent = new InputEvent();
            inputEvent.event = e;
            inputEvent.type = InputEvent.MOUSE_MOVED;
            Main.playerInputEventQueue.queue.add(inputEvent);
        }
    }

}
