import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;
import javax.swing.Timer;
import java.util.List;

public class Game extends JPanel implements ActionListener {

    private int checkWin = 0; // variable to check whether the player won or lost
    private CircleWin winSpot;
    private RectangleClass bonus;
    private boolean gameOver; // boolean used to switch from the playable game to the end of game panel
    private final int speed = 20; // sets the timer to activate every 20 ms
    private Timer timer;
    protected Circle player;
    private Pie enemy;
    private final int shapeSize = 30;       // every shape object is with size 30px
    protected final int distanceBorder = 30; // the distance between the shapes is 30px

    private int width = 0;
    protected ArrayList<Square> squares;
    private ArrayList<RectangleClass> points;
    private List<Pie> enemies;
    private int pointsCounter; // keeps track of how much points the player earned
    private endGame endGame;

/** double array for the pie shape class. Sets the coordinates of where the shapes will appear.
 * The first value can be random, the second is counted by 30px, as every 30px is a new row
 * . Each shape has size 30px, therefore each new row of the panel is after 30px.*/
    private final int[][] pieLocation = {
            {0, 60}, {450, 60}, {700, 60}, {0, 90}, {450, 90}, {700, 90},
            {100, 120}, {800, 120}, {222, 150}, {555, 150}, {700, 150}, {15, 180},
            {500, 180}, {600, 180}, {320, 210}, {820, 210}, {500, 240}, {50, 270},
            {610, 270}, {860, 270}, {90, 300}, {290, 300}, {490, 300}, {660, 330},
            {180, 360}, {760, 360}, {335, 390}, {0, 420}, {585, 420}, {480, 450},
            {25, 450}, {830, 450}, {415, 480}
    };

/** The maze string initialises all of the shapes, except the pie shapes, and sets their location on the panel.*/
    private String maze
            = "**********************\n"
            +"*?      *        ?*# *\n"
            +"**   *  * * ***  *** *\n"
            +"*? * * ?*?*  * * *?  *\n"
            +"** * * * * *   * **  *\n"
            +"*  *         * * *  **\n"
            +"* ***********  * **  *\n"
            +"*   *        ?*? * * *\n"
            +"* * * * ******** *  ?*\n"
            +"* * * * *        * ***\n"
            +"* * * * *  *******   *\n"
            +"* * * * ?*      ?*** *\n"
            +"* * * * ********     *\n"
            +"* * * *         *** **\n"
            +"* *   *********  ?**?*\n"
            +"*!*?            *    *\n"
            +"**********************\n";

    public Game() {

        gameOver = true;
        int x = distanceBorder;
        int y = distanceBorder;
        squares = new ArrayList<>();
        points = new ArrayList<>();
        Square square;
        pointsCounter = 0;

        /** The for loop checks the length of the string maze, it exhausts the string. For each character it creates a
         * corresponding shape object and adds to its list(if it has one), also updates the x variable. */
        for (int i = 0; i < maze.length(); i++) {

            char temp = maze.charAt(i);

            if (temp == '*') {
                square = new Square(x, y);
                squares.add(square);
                x += shapeSize;

            } else if (temp == ' ') {
                x += shapeSize;
            } else if (temp == '\n') {
                y += shapeSize;

                if (this.width < x) {
                    this.width = x;
                }
                x = distanceBorder;
            } else if (temp == '!') {
                player = new Circle(x, y);
                x += shapeSize;
            } else if (temp == '?') {
                bonus = new RectangleClass(x, y);
                points.add(bonus);
                x += shapeSize;
            } else if (temp == '#') {
                winSpot = new CircleWin(x, y);
                x += shapeSize;
            }
        }

        addKeyListener(new keys());      // adds a new key listener for the class keys
        setFocusable(true);              // the panel will not respond to the key adapter if the focus is not set to true

        launchEnemies();

        timer = new Timer(speed, this); // creates a timer that listens to this panel and uses the speed variable
        timer.start();

        // builds the endGame panel and adds it this panel with visibility set to false
        endGame = new endGame();
        endGame.setVisible(false);
        add(endGame);
    }

    /** endGame class creates a class which provides the player with option to add and check his score */
     class endGame extends JPanel {
        public JLabel endMessage;
        private JLabel scores;
        private TextField playerName;
        private JButton submit;
        private JLabel labelOutput;
        public JLabel highestScores;
        private JButton checkScore;
        private String fileName;

        public endGame() {
            endMessage = new JLabel();
            fileName = "src/fileScores.txt";   // the file path of the text file
            scores  = new JLabel(" ");
            playerName = new TextField(15);
            submit = new JButton("Add Name To Records");
            labelOutput = new JLabel();
            highestScores = new JLabel();
            checkScore = new JButton("Show My Top 10");

            setLayout(new GridBagLayout());
            GridBagConstraints gc = new GridBagConstraints();

            gc.gridx = 0;
            gc.gridy = 0;
            add(endMessage, gc);
            ///// Row 2 ////////
            gc.gridx = 0;
            gc.gridy = 1;
            add(scores, gc);
            ///// Row 3 ////////
            gc.gridx = 0;
            gc.gridy = 2;
            add(playerName, gc);
            ////////////////////////
            gc.gridx = 1;
            gc.gridy = 2;
            add(submit, gc);
            ////////////////////////
            gc.gridx = 2;
            gc.gridy = 2;
            add(checkScore, gc);
            ///// Row 4 ////////
            gc.gridx = 0;
            gc.gridy = 3;
            add(labelOutput, gc);
            ///// Row 5 ////////
            gc.gridx = 0;
            gc.gridy = 4;
            add(highestScores, gc);

            /** When pressed the submit button will check if the name contains only letters and is a single word, if not it will set the label text to appropriate message.
             * The button will check the game outcome and show the win or lost message. It will try to open a text file, write the name along with the game score, separated
             * by a single white space and disable the button, so that a player can enter their score only once.*/
            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (playerName.getText().matches("[a-zA-Z]+")) {
                        if (!playerName.getText().matches("\\s+")) {
                            if(checkWin == 0) {
                                endMessage.setText("Game Over");
                            } else {
                                endMessage.setText("CONGRATULATIONS YOU WON!");
                            }

                            try {
                                BufferedWriter writer;
                                 writer = new BufferedWriter(new FileWriter(fileName, true));
                                writer.append(playerName.getText() + " " + pointsCounter);
                                writer.newLine();
                                writer.close();
                            } catch (IOException ex) {
                                labelOutput.setText("File not found");
                            }

                            scores.setText("Your score is: " + pointsCounter);
                            submit.setEnabled(false);
                        } else {
                            labelOutput.setText("Write a single word");
                        }
                    }else {
                        labelOutput.setText("Enter a valid name");
                    }
                }
            });

            /** The checkScore button will also check of the name entered is valid. If it is, it reads the file line by line, if any of the lines starts with the text in
             * the text field, it will remove the name part, so that only the int part remains, it will parse it to an int and add the values to a list. The list is then sorted,
             * and a copy of the list is added to a string builder. As a JLabel can only accept String as its value, before the text is set to the string builder,
             * the string builder uses the toString() method.*/
            checkScore.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    List<Integer> theList = new ArrayList<Integer>();
                    List<Integer> highest10 = new ArrayList<Integer>();
                    StringBuilder build;

                    String line;

                    if (playerName.getText().matches("[a-zA-Z]+")) {
                        if (!playerName.getText().matches("\\s+")) {
                            labelOutput.setText(playerName.getText() + ",  your top 10 highest scores are:");

                            try {
                                BufferedReader br;
                                br = new BufferedReader(new FileReader(fileName));
                                while((line = br.readLine()) != null) {

                                    if (line.startsWith(playerName.getText())) {
                                        String intValue = line.replaceAll("[^0-9]", "");
                                        int result = Integer.parseInt(intValue);

                                        theList.add(result);
                                        Collections.sort(theList);

                                        highest10 =  theList.subList(Math.max(theList.size() - 10, 0), theList.size());
                                    }
                                }
                                List<Integer> newList = new ArrayList<Integer>(highest10);

                                String whiteSpace = " ";
                                build = new StringBuilder();
                                for(Integer i : newList) {
                                    build.append(whiteSpace);
                                    build.append(i);
                                }

                                highestScores.setText(build.toString());
                                br.close();
                            } catch (IOException er) {
                                System.out.println("Error occurred");
                            }

                        } else {
                            labelOutput.setText("Write a single word");
                        }
                    } else {
                        labelOutput.setText("Enter a valid name");
                    }
                }
            });
        }
    }

/** The draw method creates an array list for each shape. It adds the shape(s) to their list, iterates through the array lists and draws the shapes.  */
    private void draw(Graphics g) {

        ArrayList<Square> square = new ArrayList<>();
        square.addAll(squares);

        ArrayList<Circle> circle = new ArrayList<>();
        circle.add(player);

        ArrayList<RectangleClass> rect = new ArrayList<>();
        rect.addAll(points);

        ArrayList<CircleWin> circleWin = new ArrayList<>();
        circleWin.add(winSpot);

        for (int i = 0; i < circleWin.size(); i++) {
            CircleWin circleWinObject = circleWin.get(i);
            circleWinObject.drawCircleWin(g);
        }

        for (int i = 0; i < rect.size(); i++) {

            RectangleClass rectangleObject = rect.get(i);
            rectangleObject.drawRectangle(g);
        }

        for (int i = 0; i < square.size(); i++) {

            Square squareObject = square.get(i);
            squareObject.drawSquare(g);
        }

        for (int i = 0; i < circle.size(); i++) {

            Circle circleObject = circle.get(i);
            circleObject.drawCircle(g);
        }

        for (int i = 0; i < enemies.size(); i++) {
            Pie triangleObject = enemies.get(i);
            triangleObject.drawTriangle(g);
        }
    }

    /** The paintComponent method draws the draw method. When the gameOver boolean is set to false, the panel is replaced with the endGame class. */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameOver) {
            draw(g);
        }
        else {
            endGame.setVisible(true);
        }
    }

    /** extraPoints checks if the player(circle object) will collide with a rectangleClass object(the score points fields). If collision happens,
     * the player location is the same as the location of the point(this is checked when the left/right/up/downPoints() method is called,
     * returns true if this is the case and the particular point is removed from its container.  */
    private boolean extraPoints (Shape shape, String side) {
        if (side.equals("left")) {
            for (int i = 0; i < points.size(); i++) {
              bonus = points.get(i);
              if (shape.leftPoints(bonus)) {
                  points.remove(i);
                  return true;
              }
            }
            return false;
        }
        if (side.equals("right")) {
            for (int i = 0; i < points.size(); i++) {
                bonus = points.get(i);
                if (shape.rightPoints(bonus)) {
                    points.remove(i);
                    return true;
                }
            }
            return false;
        }
        if (side.equals("up")) {
            for (int i = 0; i < points.size(); i++) {
                bonus = points.get(i);
                if (shape.upPoints(bonus)) {
                    points.remove(i);
                    return true;
                }
            }
            return false;
        }
        if (side.equals("down")) {
            for (int i = 0; i < points.size(); i++) {
                bonus = points.get(i);
                if (shape.downPoints(bonus)) {
                    points.remove(i);
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /** Clash check if the player collides with a square object. If true the player position will be moved back to its original position.  */
    private boolean clash (Shape shape, String side) {

        if (side.equals("left")) {
            for (int i = 0; i < squares.size(); i++) {
                Square square = squares.get(i);
                if (shape.left(square)) return true;
            }
            return false;
        }

        if (side.equals("right")) {
            for (int i = 0; i < squares.size(); i++) {
                Square square = squares.get(i);
                if (shape.right(square)) return true;
            }
            return false;
        }

        if (side.equals("up")) {
            for (int i = 0; i < squares.size(); i++) {
                Square square = squares.get(i);
                if (shape.up(square)) return true;
            }
            return false;
        }

        if (side.equals("down")) {
            for (int i = 0; i < squares.size(); i++) {
                Square square = squares.get(i);
                if (shape.down(square)) return true;
            }
            return false;
        }
        return false;
    }
/** Method in the key adapter keys class. Check which arrow is pressed and moves the player 30px to the location the arrow is pointing to.
 * It checks for collision and handles it, either with the clash() or extraPoints() method. In the case of extraPoints() collision it increments the pointsCounter int by 10.*/
    public void keyboard(KeyEvent e) {

        if (e.getKeyCode() == 37) {    // left arrow
            if (clash(player, "left")) return;
            player.move(-distanceBorder, 0);
            if (extraPoints(player,"left")) {
                pointsCounter += 10;
            }

        }

        if (e.getKeyCode() == 39) {   // right arrow
            if (clash(player, "right")) return;
            player.move(distanceBorder, 0);
            if (extraPoints(player,"right")) {
                pointsCounter += 10;
            }
        }

        if (e.getKeyCode() == 38) {   /// up arrow
            if (clash(player, "up")) return;
            player.move(0, -distanceBorder);
            if (extraPoints(player,"up")) {
                pointsCounter += 10;
            }

        }

        if (e.getKeyCode() == 40) {    // down arrow
            if (clash(player, "down")) return;
            player.move(0, distanceBorder);
            if (extraPoints(player,"down")) {
                pointsCounter += 10;
            }

        }
    }
/** The method draws an enemy(pie shape) for every value in the pieLocation double array.*/
    public void launchEnemies() {

        enemies = new ArrayList<>();

        for (int[] i : pieLocation) {
            enemies.add(new Pie(i[0], i[1]));
        }
    }

/** The timer in the constructor will call this action performed method every speed ms and call the methods in it.*/
    @Override
    public void actionPerformed(ActionEvent e) {
        stillPlaying();
        enemiesIncoming();
        winIntersection();
        intersection();
        repaint();
    }

    /** The method iterates through the enemies collection and calls the relocate() method found in the Pie class, which changes the location of the pies.*/
    private void enemiesIncoming() {

        for (int i = 0; i < enemies.size(); i++) {

            enemy  = enemies.get(i);
            enemy.relocate();
        }
    }

    /** Stops the timer if game over */
    public void stillPlaying() {

        if(!gameOver) {
            timer.stop();
        }
    }

    /** If the player intersects with the CircleWin object, the game is over and checkWin value is changed to 1(the end game message will change) */
    public void winIntersection() {

        Rectangle leftCollision = player.getBounds();

        Rectangle rightCollision = winSpot.getBounds();

        if (leftCollision.intersects(rightCollision)) {
            checkWin = 1;
            gameOver = false;
        }
    }

    /** If an enemy pie intersects the boundary of the player, the game will be over.*/
    public void intersection() {

        Rectangle leftCollision = player.getBounds();

        for (Pie enemy : enemies) {

            Rectangle rightCollision = enemy.getBounds();

            if (leftCollision.intersects(rightCollision)) {
                gameOver = false;
            }
        }
    }
    /** The keys class implements the keyboard() method and repaints it each time a keyboard arrow is pressed*/
    private class keys extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            keyboard(e);
        repaint();
        }
    }
}

