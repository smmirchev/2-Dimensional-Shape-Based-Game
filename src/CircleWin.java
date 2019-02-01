import java.awt.*;

public class CircleWin extends Shape {

    public CircleWin(int x, int y) {
        super(x, y);
    }

    /** Draws the shape and a second shape, which acts as the border */
    public void drawCircleWin(Graphics g) {
        drawCircleWinBorder(g);
        g.setColor(Color.BLUE);
        g.fillOval(x(), y(), 28, 28);
    }

    public void drawCircleWinBorder(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillOval(x(), y(), 30, 30);
    }
}
