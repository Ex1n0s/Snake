import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    private int APPLE_SIZE = 20;
    private int SEGMENT_SIZE = 10;
    private int SCREEN_WIDTH = 640;
    private int SCREEN_HEIGHT = 640;
    private Timer gameTimer;
    private int directionX = 0, directionY = 1;
    private int velocity = 10;
    private List<Rectangle> snake;
    private List<Rectangle> apples;
    private int score = 0;

    public GamePanel(){
        setBackground(Color.black);
        setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        setOpaque(true);

        snake = new ArrayList<>();
        apples = new ArrayList<>();

        int x = 100;
        for (int i = 0; i < 4; i++){
            snake.add(new Rectangle(x - SEGMENT_SIZE * i,100,SEGMENT_SIZE,SEGMENT_SIZE));
        }
        spawnApple(3);

        setFocusable(true);
        addKeyListener(new InputHandler());

        gameTimer = new Timer(30,this);
        gameTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Aggiornamento logica
        update();
        repaint();
    }

    private void update(){
        moveSnake();
        checkCollisions();
    }

    private void moveSnake(){
        for(int i = snake.size() - 1; i > 0; i--){
            Rectangle current = snake.get(i);
            Rectangle next = snake.get(i - 1);
            current.setLocation(next.x,next.y);
        }
        Rectangle head = snake.get(0);
        head.x += velocity * directionX;
        head.y += velocity * directionY;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Draw dei segmenti del serpente
        for (int i = 0; i < snake.size(); i++) {
            Rectangle segment = snake.get(i);
            if(i == 0){
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.GREEN);
            }
            g.fillRect(segment.x,segment.y,SEGMENT_SIZE,SEGMENT_SIZE);
        }
        g.setColor(Color.YELLOW);
        for (int i = 0; i < apples.size(); i++) {
            Rectangle apple = apples.get(i);
            g.fillRoundRect(apple.x, apple.y,APPLE_SIZE,APPLE_SIZE,20,20);
        }
    }

    private void spawnApple(int n){
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            int x = random.nextInt(640);
            int y = random.nextInt(640);
            apples.add(new Rectangle(x,y,APPLE_SIZE,APPLE_SIZE));
        }
    }

    public void checkCollisions(){
        Rectangle head = snake.getFirst();
        for (int i = 1; i < snake.size(); i++){
            Rectangle segment = snake.get(i);
            if(head.intersects(segment)){
                gameOver();
                return;
            }
        }

        for (int i = 0; i < apples.size(); i++) {
            Rectangle apple = apples.get(i);
            if(head.intersects(apple)){
                score++;
                apples.remove(i);
                i--;
                snake.add(new Rectangle(-100,-100,SEGMENT_SIZE,SEGMENT_SIZE));
                spawnApple(1);
            }
        }

        if(head.x < 0 || head.x >= SCREEN_WIDTH || head.y < 0 || head.y >= SCREEN_HEIGHT){
            gameOver();
        }

    }

    private void gameOver(){
        gameTimer.stop();
        JOptionPane.showMessageDialog(this,"Game over\npunteggio: " + score);
    }

    private class InputHandler extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if(key == KeyEvent.VK_W && directionY != 1){
                directionX = 0;
                directionY = -1;
            } else if (key == KeyEvent.VK_S && directionY != -1) {
                directionX = 0;
                directionY = 1;
            } else if (key == KeyEvent.VK_D && directionX != -1) {
                directionY = 0;
                directionX = 1;
            } else if (key == KeyEvent.VK_A && directionX != 1) {
                directionY = 0;
                directionX = -1;
            }
        }
    }
}
