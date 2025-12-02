import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Timer gameTimer;
    private int directionX = 0, directionY = 1;
    private int velocity = 5;
    private boolean[] keys = new boolean[KeyEvent.KEY_LAST];
    private List<Point> snake;
    private List<Point> storicoPosizioni;

    public GamePanel(){
        setBackground(Color.black);
        setPreferredSize(new Dimension(800,800));
        setOpaque(true);

        snake = new ArrayList<>();
        storicoPosizioni = new ArrayList<>();
        snake.add(new Point(100,100));
        snake.add(new Point(75,100));
        snake.add(new Point(50,100));
        snake.add(new Point(25,100));
        snake.add(new Point(0,100));
        snake.add(new Point(-25,100));
        snake.add(new Point(-50,100));
        setFocusable(true);
        addKeyListener(this);

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
        if(keys[KeyEvent.VK_W]){
            directionX = 0;
            directionY = -1;
        } else if (keys[KeyEvent.VK_S]) {
            directionX = 0;
            directionY = 1;
        } else if (keys[KeyEvent.VK_D]) {
            directionY = 0;
            directionX = 1;
        } else if (keys[KeyEvent.VK_A]) {
            directionY = 0;
            directionX = -1;
        }

        moveSnake();

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
        for (int i = 0; i < snake.size(); i++) {
            Point point = snake.get(i);
            if(i == 0){
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.GREEN);
            }

            g.fillRoundRect(point.x, point.y,20,20,20,20);
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if(key < keys.length){
            keys[key] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if(key < keys.length){
            keys[key] = false;
        }
    }
}
