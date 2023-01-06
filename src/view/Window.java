package view;

import controller.KeyEventListener;
import controller.Main;
import controller.MouseEventListener;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Dimension2D;
import java.util.Random;

public class Window extends JFrame {

    public static int WINDOW_WIDTH  = 100 * Main.TILE_SIZE;
    public static int WINDOW_HEIGHT = 56 * Main.TILE_SIZE;
    public int screenWidth;
    public int screenHeight;

    public MyCanvas canvas;

    public JButton startButton;

    public JLabel scoreField, hpField;

    int fighterLimit;
    int bomberLimit;

    public void init() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = dim.width;
        screenHeight = dim.height;
        canvas = new MyCanvas();

        MouseEventListener mouseListener = new MouseEventListener();
        canvas.addMouseListener(mouseListener);
        canvas.addMouseMotionListener(mouseListener);

        KeyEventListener keyListener = new KeyEventListener();
        canvas.addKeyListener(keyListener);
        canvas.setFocusable(true);

        var cp = getContentPane();
        cp.add(BorderLayout.CENTER, canvas);

        JPanel bottomPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel scorePanel = new JPanel();

        startButton = new JButton("START");
        startButton.setFocusable(false);
        // lambda
        startButton.addActionListener(e -> {
            if(startButton.getText() == "RESTART") {
                Main.dead = false;
                hpField.setText("20");
                scoreField.setText("0");
                startButton.setText("QUIT");
                Main.restartGame();
            }else if (!Main.running) {    // start button
                startButton.setText("QUIT");
                Main.running = true;
            } else {                // quit button
                JOptionPane.showMessageDialog(this, "Your final score is..." + Main.gameData.getScore());
                System.exit(0);
            }
        });
        buttonPanel.add(startButton);

        /*saveButton = new JButton("SAVE");
        saveButton.setFocusable(false);
        saveButton.addActionListener(e -> {
            System.out.println("saving");
        });
        buttonPanel.add(saveButton);

        loadButton = new JButton("LOAD");
        loadButton.setFocusable(false);
        loadButton.addActionListener(e -> {
            System.out.println("loading");
        });
        buttonPanel.add(loadButton);*/

        bottomPanel.add(BorderLayout.CENTER, buttonPanel);

        JLabel hp = new JLabel("HP");
        hp.setFocusable(false);
        scorePanel.add(hp);

        hpField = new JLabel("20");
        hpField.setHorizontalAlignment(JLabel.CENTER);
        hpField.setFocusable(false);
        scorePanel.add(hpField);

        JLabel score = new JLabel("Score");
        score.setFocusable(false);
        scorePanel.add(score);

        scoreField = new JLabel("0");
        scoreField.setHorizontalAlignment(JLabel.CENTER);
        scoreField.setFocusable(false);
        scoreField.addPropertyChangeListener(e -> {
            if(scoreField.getText().compareToIgnoreCase("15") < 0){
                Random ran = new Random();
                if(Main.gameData != null && fighterLimit < 9) {
                    Main.gameData.addFighterWithListener(Main.win.canvas.getWidth() + ran.nextInt(700),
                            200 + ran.nextInt(800));
                    fighterLimit++;
                }
            }
            if (scoreField.getText().compareToIgnoreCase("30") < 0) {
                Random ran = new Random();
                if(Main.gameData != null && bomberLimit < 3) {
                    Main.gameData.addFighterWithListener(Main.win.canvas.getWidth() + ran.nextInt(700),
                            (int) Main.pc.location.y - ran.nextInt(500));
                    bomberLimit++;
                }
            }
        });
        scorePanel.add(scoreField);
        scorePanel.setSize(10, cp.getHeight()-5);

        bottomPanel.add(BorderLayout.WEST, scorePanel);

        cp.add(BorderLayout.SOUTH, bottomPanel);

        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        int x = screenWidth - (100 * Main.TILE_SIZE);
        int y = screenHeight - (56 * Main.TILE_SIZE);
        setLocation(x - x / 2, y - y / 2);
        setTitle("Game Engine");
    }
}
