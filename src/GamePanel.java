import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    private int APPLE_SIZE = 20,SEGMENT_SIZE = 10;
    private Timer gameTimer;
    private int directionX = 0, directionY = 1;
    private int velocity = 5;
    private List<Point> snake;
    private List<Point> storicoPosizioni;
    private List<Point> apples;

    public GamePanel(){
        setBackground(Color.black);
        setPreferredSize(new Dimension(640,640));
        setOpaque(true);

        snake = new ArrayList<>();
        storicoPosizioni = new ArrayList<>();
        apples = new ArrayList<>();

        int x = 100;
        for (int i = 0; i < 7; i++){
            snake.add(new Point(x - 20 * i,100));
        }
        spawnApple(3);

        setFocusable(true);
        addKeyListener(new InputHandler());

        gameTimer = new Timer(16,this);
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
        snake.get(0).x += velocity * directionX;
        snake.get(0).y += velocity * directionY;
        storicoPosizioni.add(0,new Point(snake.get(0).x,snake.get(0).y));

        for(int i = 1; i < snake.size(); i++){
            int index = i * 3;
            if(index < storicoPosizioni.size()){
                Point puntoStorico = storicoPosizioni.get(index);
                snake.get(i).x = puntoStorico.x;
                snake.get(i).y = puntoStorico.y;
            } else {
                Point vecchioPunto = storicoPosizioni.get(storicoPosizioni.size() - 1);
                snake.get(i).x = vecchioPunto.x;
                snake.get(i).y = vecchioPunto.y;
            }
        }
        int maxStoria = snake.size() * 3;
        if(storicoPosizioni.size() > maxStoria){
            storicoPosizioni.remove(storicoPosizioni.size() - 1);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Draw dei segmenti del serpente
        for (int i = 0; i < snake.size(); i++) {
            Point point = snake.get(i);
            if(i == 0){
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.GREEN);
            }
            g.fillRect(point.x,point.y,SEGMENT_SIZE,SEGMENT_SIZE);
            //g.fillRoundRect(point.x, point.y,20,20,20,20);
        }
        for (int i = 0; i < apples.size(); i++) {
            Point point = apples.get(i);
            g.fillRoundRect(point.x, point.y,APPLE_SIZE,APPLE_SIZE,20,20);
        }
    }

    private void spawnApple(int n){
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            int x = random.nextInt(640);
            int y = random.nextInt(640);
            apples.add(new Point(x,y));
        }
    }

    public void checkCollisions(){
        for (int i = 0; i < apples.size(); i++) {
            Point headPosition = snake.getFirst();
            Point applePosition = apples.get(i);
            if(headPosition.equals(applePosition)){
                apples.remove(i);
                i--;
            }
        }
    }

    private class InputHandler extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            switch (key){
                case KeyEvent.VK_W:
                    if(directionY != 1){
                        directionX = 0;
                        directionY = -1;
                    }
                    break;
                case KeyEvent.VK_S:
                    if(directionY != -1){
                        directionX = 0;
                        directionY = 1;
                    }
                    break;
                case KeyEvent.VK_D:
                    if(directionX != -1){
                        directionY = 0;
                        directionX = 1;
                    }
                    break;
                case KeyEvent.VK_A:
                    if (directionX != 1) {
                        directionY = 0;
                        directionX = -1;
                    }
                default:
                    break;
            }
        }
    }
}
