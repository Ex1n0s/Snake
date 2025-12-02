import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame gameFrame = new JFrame("snake");
        gameFrame.add(new GamePanel());
        gameFrame.setTitle("Snake");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setResizable(false);
        gameFrame.pack();
        gameFrame.setVisible(true);
        gameFrame.setLocationRelativeTo(null);
    }
}
