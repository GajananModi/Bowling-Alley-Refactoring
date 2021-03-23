package ViewControl;

import javax.swing.*;
import java.awt.*;

import static ViewControl.ViewUtils.setComponentProperties;

public class UserLaneView extends JFrame {
    private JLabel msg;
    private Container c;

    public UserLaneView(String username) {
        setTitle("User View Window");
        setBounds(300, 90, 800, 200);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setResizable(false);

        c = getContentPane();
        c.setLayout(null);

        msg = new JLabel(String.format("Sorry %s,Development of User View is in progress. Please login as Admin.", username));
        setComponentProperties(msg, 15, 700, 20, 70, 80);
        c.add(msg);

        setVisible(true);
    }
}
