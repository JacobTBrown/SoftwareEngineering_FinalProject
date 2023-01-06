package view;

import controller.Main;

import javax.swing.*;
import java.awt.*;

public class MyCanvas extends JPanel {

    public int width;
    public int height;

    public void render() {
        width = getSize().width;
        height = getSize().height;

        //System.out.println("Canvas Width: " + width + "\n" + "Canvas Height: " + height);

        // off-screen double buffer image
        Image doubleBufferImage = createImage(width, height);
        if (doubleBufferImage == null) {
            System.out.println("Critical error: doubleBufferImage is null");
            System.exit(1);
        }

        // off-screen rendering
        Graphics2D g2OffScreen = (Graphics2D) doubleBufferImage.getGraphics();
        if (g2OffScreen == null) {
            System.out.println("Critical error: g2OffScreen is null");
            System.exit(1);
        }

        // initialize the image buffer
        g2OffScreen.setBackground(Color.WHITE);
        g2OffScreen.clearRect(0,0,width,height);

        // render all game data
        for(var fig: Main.gameData.gameObjects) {
            fig.render(g2OffScreen);
        }
        /*for(var fig: Main.gameData.friendObject) {
            fig.render(g2OffScreen);
        }
        for(var fig: Main.gameData.enemyObject) {
            fig.render(g2OffScreen);
        }*/

        // use active rendering to put the buffer image on screen
        Graphics gOnScreen;
        gOnScreen = this.getGraphics();
        if (gOnScreen != null) {
            // copy offScreen image to onScreen
            gOnScreen.drawImage(doubleBufferImage, 0, 0, null);
        }
        Toolkit.getDefaultToolkit().sync(); // sync the display
        if (gOnScreen != null) {
            gOnScreen.dispose();
        }
        if (g2OffScreen != null) {
            g2OffScreen.dispose();
        }
    }
}
