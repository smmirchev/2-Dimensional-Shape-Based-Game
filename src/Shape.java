import java.awt.*;

/** Abstract class used to control the different shapes. Integers x and y are used for determining the location of the objects.*/
public abstract class Shape {
    protected int x;
    protected int y;

    /** Constructor takes the x and y parameters */
    public Shape(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /** Returns x in accordance to the location */
    public void ReturnX(int x) {

        this.x = x;
    }

    /** Returns y in accordance to the location */
    public void ReturnY(int y) {

        this.y = y;
    }

    /** Returns the x value*/
    public int x() {

        return x;
    }

    /** Returns the y value*/
    public int y() {

        return y;
    }

    /** Returns the x and y values, with the same location the shape(player) is located*/
    public boolean leftPoints(Shape shape) {
        return x() == shape.x() && y() == shape.y();
    }
    public boolean rightPoints(Shape shape) {
        return x() == shape.x() && y() == shape.y();
    }
    public boolean upPoints(Shape shape) {
        return x() == shape.x() && y() == shape.y();
    }
    public boolean downPoints(Shape shape) {
        return x() == shape.x() && y() == shape.y();
    }

    /** Returns the location of the shape(player) -30 for the corresponding method*/
    public boolean left (Shape shape) {

        return x() - 30 == shape.x() && y() == shape.y();
    }

    public boolean right (Shape shape) {

        return x() + 30 == shape.x() && y() == shape.y();
    }

    public boolean up (Shape shape) {

        return y() - 30 == shape.y() && x() == shape.x();
    }

    public boolean down (Shape shape) {

        return y() + 30 == shape.y() && x() == shape.x();
    }

    /**  Creates a rectangle around the shapes. The actual size of the shapes is 30 by 30, width of 15 is used for better optimization of dodging the enemy pies*/
    public Rectangle getBounds() {
        return new Rectangle(x(), y(), 15, 30);
    }

}
