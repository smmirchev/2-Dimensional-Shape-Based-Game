import java.awt.*;

public class RectangleClass extends Shape {

    public RectangleClass(int x, int y) {
        super(x, y);
    }

    /** Draws the shape and a second shape, which acts as the border */
    public void drawRectangle(Graphics g) {
        drawRectangleBorder(g);
        g.setColor(Color.PINK);
        g.fillRect(x(), y(), 28, 28);
    }

    public void drawRectangleBorder(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(x(), y(), 30, 30);
    }
}
