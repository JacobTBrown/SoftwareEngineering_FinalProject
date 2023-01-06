package controller;

import controller.queues.InputEvent;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyEventListener extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
        InputEvent inputEvent = new InputEvent();
        inputEvent.event = e;
        inputEvent.type = InputEvent.KEY_PRESSED;
        Main.playerInputEventQueue.queue.add(inputEvent);
     }

    @Override
    public void keyReleased(KeyEvent e) {
        InputEvent inputEvent = new InputEvent();
        inputEvent.event = e;
        inputEvent.type = InputEvent.KEY_RELEASED;
        Main.playerInputEventQueue.queue.add(inputEvent);
    }
}
