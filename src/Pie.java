import java.awt.*;

public class Pie extends Shape {

    public Pie(int x, int y) {
        super(x, y);
    }

    /** Draws the shape */
    public void drawTriangle(Graphics g) {
        g.setColor(Color.RED);
        g.fillArc(x(), y(), 30, 30,90 , 180);
    }

    /** relocate()  subtracts 1 each time it is called from x. x is set to 900 and acts as the width of the application.
     * In other words: each time the timer is called(speed), it will subtract 1 from 900, when 0 reached it will reset, as the shapes are repainted*/
    public void relocate() {

        if (x < 0) {
            x = 900;  // width of lifespan of enemies
        }

        x = x - 1;
    }
}
