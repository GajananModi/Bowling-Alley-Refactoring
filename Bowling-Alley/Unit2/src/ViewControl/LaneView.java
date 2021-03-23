package ViewControl;
/*
 *  constructs a prototype Lane View
 *
 */

import Model.Bowler;
import Model.Lane;
import Model.Party;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

public class LaneView implements ActionListener, Observer {

    private boolean initDone = false;
    JFrame frame;
    public Container cpanel;
    Vector bowlers;
    JPanel[][] balls;
    JLabel[][] ballLabel;
    JLabel[][] emoji;
    JPanel[][] scores;
    JLabel[][] scoreLabel;
    JPanel[][] ballGrid;
    JPanel[] pins;
    JButton maintenance,throwBall,finish;
    MovingBallPanel bouncingBallSample;
    Lane lane;

    public LaneView(Lane lane, int laneNum) {

        this.lane = lane;
        frame = new JFrame("Lane " + laneNum + ":");
        cpanel = frame.getContentPane();
        cpanel.setLayout(new BorderLayout());
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.setVisible(false);
            }
        });
        cpanel.add(new JPanel());
        return;
    }

    public void show() {
        frame.setVisible(true);
    }

    public void hide() {
        frame.setVisible(false);
    }

    private JPanel makeFrame(Party party) {
        bowlers = party.getMembers();
        int numBowlers = bowlers.size();
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));
        balls = new JPanel[numBowlers][30];
        ballLabel = new JLabel[numBowlers][30];
        emoji = new JLabel[numBowlers][30];
        scores = new JPanel[numBowlers][14];
        scoreLabel = new JLabel[numBowlers][14];
        ballGrid = new JPanel[numBowlers][14];
        pins = new JPanel[numBowlers];

        for (int i = 0; i != numBowlers; i++) {
            for (int j = 0; j != 30; j++) {
                ballLabel[i][j] = new JLabel(" ");
                emoji[i][j] = new JLabel(" ");
                balls[i][j] = new JPanel();
                balls[i][j].setBorder(
                        BorderFactory.createLineBorder(Color.BLACK));
                balls[i][j].add(ballLabel[i][j]);
            }
        }

        for (int i = 0; i != numBowlers; i++) {
            for (int j = 0; j != 9; j++) {
                ballGrid[i][j] = new JPanel();
                ballGrid[i][j].setLayout(new GridLayout(0, 3));
                ballGrid[i][j].add(new JLabel("  "), BorderLayout.EAST);
                ballGrid[i][j].add(balls[i][2 * j], BorderLayout.EAST);
                ballGrid[i][j].add(balls[i][2 * j + 1], BorderLayout.EAST);
                ballGrid[i][j].add(new JLabel("  "), BorderLayout.EAST);
                ballGrid[i][j].add(emoji[i][2 * j], BorderLayout.EAST);
                ballGrid[i][j].add(emoji[i][2 * j + 1], BorderLayout.EAST);
            }
            int j = 9;
            ballGrid[i][j] = new JPanel();
            ballGrid[i][j].setLayout(new GridLayout(0, 3));
            ballGrid[i][j].add(balls[i][2 * j]);
            ballGrid[i][j].add(balls[i][2 * j + 1]);
            ballGrid[i][j].add(balls[i][2 * j + 2]);
            ballGrid[i][j].add(emoji[i][2 * j]);
            ballGrid[i][j].add(emoji[i][2 * j + 1]);
            ballGrid[i][j].add(emoji[i][2 * j + 2]);
            j = 10;
            ballGrid[i][j] = new JPanel();
            ballGrid[i][j].setLayout(new GridLayout(0, 3));
//			ballGrid[i][j].add(balls[i][2 * j]);
            ballGrid[i][j].add(new JLabel("  "), BorderLayout.EAST);
            ballGrid[i][j].add(new JLabel("  "), BorderLayout.EAST);
            ballGrid[i][j].add(balls[i][2 * j + 1], BorderLayout.EAST);
            ballGrid[i][j].add(new JLabel("  "), BorderLayout.EAST);
            ballGrid[i][j].add(new JLabel("  "), BorderLayout.EAST);
            ballGrid[i][j].add(emoji[i][2 * j + 1], BorderLayout.EAST);
//			ballGrid[i][j].add(balls[i][2 * j + 2]);
            for (j = 11; j != 14; j++) {
                ballGrid[i][j] = new JPanel();
                ballGrid[i][j].setLayout(new GridLayout(0, 3));
                ballGrid[i][j].add(new JLabel("  "), BorderLayout.EAST);
                ballGrid[i][j].add(balls[i][2 * j], BorderLayout.EAST);
                ballGrid[i][j].add(balls[i][2 * j + 1], BorderLayout.EAST);
                ballGrid[i][j].add(new JLabel("  "), BorderLayout.EAST);
                ballGrid[i][j].add(emoji[i][2 * j], BorderLayout.EAST);
                ballGrid[i][j].add(emoji[i][2 * j + 1], BorderLayout.EAST);
            }
//			ballGrid[i][j].add(balls[i][2 * j + 2]);
        }

        for (int i = 0; i != numBowlers; i++) {
            pins[i] = new JPanel();
            pins[i].setBorder(
                    BorderFactory.createTitledBorder(
                            ((Bowler) bowlers.get(i)).getNick()));
            pins[i].setLayout(new GridLayout(0, 14));
            for (int k = 0; k != 14; k++) {
                scores[i][k] = new JPanel();
                scoreLabel[i][k] = new JLabel("  ", SwingConstants.CENTER);
                scores[i][k].setBorder(
                        BorderFactory.createLineBorder(Color.BLACK));
                scores[i][k].setLayout(new GridLayout(0, 1));
                scores[i][k].add(ballGrid[i][k], BorderLayout.EAST);
                scores[i][k].add(scoreLabel[i][k], BorderLayout.SOUTH);
                pins[i].add(scores[i][k], BorderLayout.EAST);
            }
            panel.add(pins[i]);
        }
        panel.add(getBallPanel());
        initDone = true;
        return panel;
    }
    private JPanel getBallPanel() {
        bouncingBallSample = new MovingBallPanel();
        return bouncingBallSample;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(maintenance)) {
            lane.pauseGame();
        }
        if (e.getSource().equals(throwBall)) {
            int point = bouncingBallSample.stopBall();
            throwBall.setEnabled(false);
            lane.throwBall(point);
        }
        if (e.getSource().equals(finish)) {

            frame.dispose();
            //lane.publish();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(o instanceof Lane)) {
            return;
        }
        Lane le = (Lane) o;

        if (lane.isPartyAssigned() && !lane.isGameFinished()) {

            if (!initDone) {
                cpanel.removeAll();
                cpanel.add(makeFrame(le.getParty()), "Center");
            }

            int numBowlers = le.getParty().getMembers().size();

            if (le.getFrameNumber() == 0 && le.getBall() == 0 && le.getBowlIndex() == 0) {
                System.out.println("Making the frame.");
                cpanel.removeAll();
                cpanel.add(makeFrame(le.getParty()), "Center");
                // Button Panel
                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new FlowLayout());

                Insets buttonMargin = new Insets(4, 4, 4, 4);

                maintenance = new JButton("Pause/Resume");
                JPanel maintenancePanel = new JPanel();
                maintenancePanel.setLayout(new FlowLayout());
                maintenance.addActionListener(this);
                maintenancePanel.add(maintenance);

                buttonPanel.add(maintenancePanel);
// Added Throw and finish

                throwBall = new JButton("Throw");
                JPanel tPanel = new JPanel();
                tPanel.setLayout(new FlowLayout());
                throwBall.addActionListener(this);
                tPanel.add(throwBall);
                buttonPanel.add(tPanel);

                finish = new JButton("Finish");
                JPanel fpanel = new JPanel();
                fpanel.setLayout(new FlowLayout());
                finish.addActionListener(this);
                fpanel.add(finish);

                buttonPanel.add(fpanel);


                cpanel.add(buttonPanel, "South");

                frame.pack();
                cpanel.setVisible(true);
            }

            int[][] lescores = le.getCumulScores();
            HashMap scores = le.getScores();
            this.bowlers = le.getParty().getMembers();
            for (int k = 0; k < numBowlers; k++) {
                for (int i = 0; i <= le.getFrameNumber() - 1; i++) {
                    if (lescores[k][i] != 0) {
                        this.scoreLabel[k][i].setText((new Integer(lescores[k][i])).toString());
                    }
                }
                for (int i = 0; i < 28; i++) {
                    if (((int[]) (scores.get(bowlers.get(k))))[i] != -1) {
                        if (((int[]) scores.get(bowlers.get(k)))[i] == 10 && (i % 2 == 0 || i == 19 || i == 21)) {
                            ballLabel[k][i].setText("X");
                            emoji[k][i].setIcon(new ImageIcon(getScaledImage(emoji[k][i], "great")));
                        } else if (i > 0 && ((int[]) scores.get(bowlers.get(k)))[i] + ((int[]) scores.get(bowlers.get(k)))[i - 1] == 10 && i % 2 == 1) {
                            ballLabel[k][i].setText("/");
                            emoji[k][i].setIcon(new ImageIcon(getScaledImage(emoji[k][i], "good")));
                        } else if (((int[]) scores.get(bowlers.get(k)))[i] == -2) {
                            ballLabel[k][i].setText("F");
                        } else {
                            ballLabel[k][i].setText((new Integer(((int[]) scores.get(bowlers.get(k)))[i])).toString());
                            emoji[k][i].setIcon(new ImageIcon(getScaledImage(emoji[k][i], "okay")));
                        }
                    }
                }
            }
            throwBall.setEnabled(true);
        } else {
            initDone = false;
        }

    }

    private Image getScaledImage(JLabel label, String emo) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(String.format("Unit2/resources/%s.png", emo)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img.getScaledInstance(20, 20, Image.SCALE_SMOOTH); // please check once giving error
    }
}
