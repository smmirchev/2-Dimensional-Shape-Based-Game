import java.awt.*;

public class Square extends Shape {

    public Square(int x, int y) {
        super(x, y);
    }

    /** Draws the shape and a second shape, which acts as the border */
    public void drawSquare (Graphics g) {
        drawSquareBorder(g);
        g.setColor(Color.ORANGE);
        g.fillRect(x(), y(), 28, 28);
    }


    public void drawSquareBorder (Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(x(), y(), 30, 30);
    }
}
