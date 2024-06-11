package snake_game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 600;
    private static final int UNIT_SIZE = 30;
    private static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT)/UNIT_SIZE;
    private static final int DELAY = 70;
    private final int[] x = new int[GAME_UNITS];
    private final int[] y = new int[GAME_UNITS];

    private int snakeBodyParts = 3;
    int applesEaten;
    int potionX;
    int potionY;
    char direction = 'R'; // default - right

    boolean running = false;
    Timer timer;
    Random random;

    public GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        this.startGame();
    }

    public void startGame() {
        newPotion();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();

    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void draw(Graphics graphics) {
        if (running) {
            for (int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
                graphics.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                graphics.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
            }
            graphics.setColor(Color.CYAN);
            graphics.fillOval(potionX, potionY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < snakeBodyParts; i++) {
                if (i == 0) {
                    graphics.setColor(Color.cyan); // for the head
                    graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    graphics.setColor(Color.yellow); // for the body
                    graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            /*graphics.setColor(Color.red);
            graphics.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics fontMetrics = getFontMetrics(graphics.getFont());
            graphics.drawString("Score: " + applesEaten,
                    (SCREEN_WIDTH - fontMetrics.stringWidth("Score: " + applesEaten))/2,
                    graphics.getFont().getSize());*/
        } else {
            gameOver(graphics);
        }

    }

    public void newPotion() {
        potionX = random.nextInt(SCREEN_WIDTH/UNIT_SIZE) * UNIT_SIZE;
        potionY = random.nextInt(SCREEN_HEIGHT/UNIT_SIZE) * UNIT_SIZE;
    }

    public void move() {
        for (int i = snakeBodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if ((x[0] == potionX) && (y[0] == potionY)) {
            snakeBodyParts++;
            applesEaten++;
            newPotion();
        }
    }

    public void checkCollisions() {
        for (int i = snakeBodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }
        }

        if (x[0] < 0){
            System.out.println("left " + x[0]);
            x[0] = SCREEN_WIDTH;

        } else if (x[0] >= SCREEN_WIDTH){
            System.out.println("right " + x[0]);
            x[0] = 0;

        } else if (y[0] < 0){
            System.out.println("top border " + y[0]);
            y[0] = SCREEN_HEIGHT;

        } else if (y[0] >= SCREEN_HEIGHT) {
            System.out.println("bottom border " + y[0]);
            y[0] = 0;
        }

        if (!running) {
            timer.stop();
        }

    }

    public void gameOver(Graphics graphics) {
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics fontMetrics1 = getFontMetrics(graphics.getFont());
        graphics.drawString("Score: " + applesEaten,
                (SCREEN_WIDTH - fontMetrics1.stringWidth("Score: " + applesEaten))/2,
                graphics.getFont().getSize());

        graphics.setColor(Color.red);
        graphics.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics fontMetrics2 = getFontMetrics(graphics.getFont());
        graphics.drawString("Game Over",
                (SCREEN_WIDTH - fontMetrics2.stringWidth("Game Over"))/2, SCREEN_WIDTH/2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_RIGHT:
                    direction = 'R';
                    break;
                case KeyEvent.VK_LEFT:
                    direction = 'L';
                    break;
                case KeyEvent.VK_UP:
                    direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    direction = 'D';
                    break;
            }
        }
    }
}
