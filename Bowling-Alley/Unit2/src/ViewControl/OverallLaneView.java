package ViewControl;

import Model.Lane;

import javax.swing.*;
import java.awt.*;

public class OverallLaneView {
    private LaneView laneView;
    private PinsetterView pinSetterView;
    private JFrame frame;
    private Container cpanel;
    private boolean visible;

    public JFrame getframe()
    {
        return frame;
    }

    public OverallLaneView(Lane lane, int laneNum){
        this.laneView = new LaneView(lane, laneNum);
        this.pinSetterView = new PinsetterView(lane.getPinsetter(),laneNum);
        this.visible = false;
        frame = new JFrame("Lane " + laneNum + ":");
        cpanel = frame.getContentPane();
        cpanel.setLayout(new GridLayout(2, 1));
        cpanel.add(this.laneView.cpanel);
        cpanel.add(this.pinSetterView.cpanel);


        //ps.addObserver(this.pinSetterView);
        lane.addObserver(this.laneView);
        //ps.subscribe(this.pinSetterView);  ///check observer pattern
        //lane.subscribe(this.laneView);

        frame.pack();
    }

    public void toggle() {
        if (!this.visible) {
            frame.pack();
            frame.setVisible(true);
        }
        else{

            frame.setVisible(false);
        }
    }
}
