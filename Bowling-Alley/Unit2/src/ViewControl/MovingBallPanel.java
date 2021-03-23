package ViewControl;



import javax.swing.*;
import java.awt.*;
public class MovingBallPanel extends JPanel {
    // Container box's width and height
    private static final int BOX_WIDTH = 530;
    private static final int BOX_HEIGHT = 70;

    // Ball's properties
    private float ballRadius = 20; // Ball's radius
    private float ballX = 20; // Ball's center (x, y)
    private float ballY = 35;
    private float ballSpeedX = 20;   // Ball's speed for x and y
    private float ballSpeedY = 2;
    private Thread gameThread;

    private static final int UPDATE_RATE = 60; // Number of refresh per second

    /** Constructor to create the UI components and init game objects. */
    public MovingBallPanel() {
        this.setPreferredSize(new Dimension(BOX_WIDTH, BOX_HEIGHT));

        // Start the ball bouncing (in its own thread)
        gameThread = new Thread() {
            public void run() {
                while (true) { // Execute one update step
                    // Calculate the ball's new position
                    ballX += ballSpeedX;
                    if (ballX - ballRadius < 0) {
                        ballSpeedX = -ballSpeedX; // Reflect along normal
                        ballX = ballRadius; // Re-position the ball at the edge
                    } else if (ballX + ballRadius > BOX_WIDTH) {
                        ballSpeedX = -ballSpeedX;
                        ballX = BOX_WIDTH - ballRadius;
                    }
                    // Refresh the display
                    repaint(); // Callback paintComponent()
                    // Delay for timing control and give other threads a chance
                    try {
                        Thread.sleep(1000 / UPDATE_RATE);  // milliseconds
                    } catch (InterruptedException ex) { }
                }
            }
        };
        gameThread.start();  // Callback run()
    }

    /** Custom rendering codes for drawing the JPanel */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);    // Paint background

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, BOX_WIDTH, BOX_HEIGHT);

        g.setColor(Color.BLUE);
       g.fillOval((int) (ballX - ballRadius), (int) (ballY - ballRadius),
               (int)(2 * ballRadius), (int)(2 * ballRadius));
        //g.drawLine(40,60,40,0);
        g.setColor(Color.BLACK);
        g.drawString("0", 510, 10);
        g.drawString("1", 485, 10);
        g.drawString("2", 460, 10);
        g.drawString("3", 435, 10);
        g.drawString("4", 410, 10);
        g.drawString("5", 385, 10);
        g.drawString("6", 360, 10);
        g.drawString("7", 335, 10);
        g.drawString("8", 310, 10);
        g.drawString("9", 285, 10);
        g.drawString("10", 260, 10);
        g.drawString("9", 235, 10);
        g.drawString("8", 210, 10);
        g.drawString("7", 185, 10);
        g.drawString("6", 160, 10);
        g.drawString("5", 135, 10);
        g.drawString("4", 110, 10);
        g.drawString("3", 85, 10);
        g.drawString("2", 60, 10);
        g.drawString("1", 35, 10);
        g.drawString("0", 10, 10);
    }

    public void startBall() {
        gameThread.start();

    }

    public int stopBall() {
        gameThread.interrupt();
        int currX = (int)ballX;
        if (currX > 260) {
            currX -= (currX - 260)*2;
        }
        int point = 10 - ((int)(260 - currX)/25);
        System.out.println("Points: " + point);
        return point;
    }
    /** main program (entry point) */
    /*
    public static void main(String[] args) {
        // Run GUI in the Event Dispatcher Thread (EDT) instead of main thread.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Set up main window (using Swing's Jframe)
                JFrame frame = new JFrame("A Bouncing Ball");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setContentPane(new MovingBallPanel());
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
     */
}