import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {

    private JPanel gamePanel;

    public Main() {

        setTitle("Registration number: 1601216");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel = new Game();


        setLayout(new BorderLayout());
        add(gamePanel, BorderLayout.CENTER);


    }

    public static void main(String[] args) {

        Main game = new Main();
        game.setVisible(true);
    }
}
