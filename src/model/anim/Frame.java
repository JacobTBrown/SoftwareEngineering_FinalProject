package model.anim;

import java.awt.image.BufferedImage;

public class Frame {

    private BufferedImage sprite;
    private int duration;

    public Frame(BufferedImage frame, int duration) {
        this.sprite = frame;
        this.duration = duration;
    }

    public BufferedImage getFrame() {
        return sprite;
    }

    public void setFrame(BufferedImage frame) {
        this.sprite = frame;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
