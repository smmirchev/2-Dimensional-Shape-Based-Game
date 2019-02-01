import java.awt.*;

public class Circle extends Shape {

    public Circle(int x, int y) {
        super(x, y);
    }


    /** The move method used to relocate the player */
    public void move(int x, int y) {

        int xAxis = x() + x;
        int yAxis = y() + y;

        ReturnX(xAxis);
        ReturnY(yAxis);
    }
/** Draws the shape and a second shape, which acts as the border */
    public void drawCircle(Graphics g) {
        drawCircleBorder(g);
        g.setColor(Color.GREEN);
        g.fillOval(x(), y(), 28, 28);
    }

    public void drawCircleBorder(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillOval(x(), y(), 30, 30);
    }
}


