package ViewControl;


/**
 * Class for representation of the control desk
 */

import Model.ControlDesk;
import Model.Lane;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

public class ControlDeskView implements ActionListener, Observer {

    private JButton addParty, finished, assign, query;
    private JFrame win;
    private JList partyList;
    private int maxMembers; // 	The maximum  number of members in a party
    private ControlDesk controlDesk;

    /**
     * Displays a GUI representation of the ControlDesk
     */
    public ControlDeskView(ControlDesk controlDesk, int maxMembers) {

        this.controlDesk = controlDesk;
        this.maxMembers = maxMembers;
        int numLanes = controlDesk.getNumLanes();

        win = new JFrame("Control Desk");
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);

        JPanel colPanel = new JPanel();
        colPanel.setLayout(new BorderLayout());

        // Controls Panel
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new GridLayout(3, 1));
        controlsPanel.setBorder(new TitledBorder("Controls"));

        query = ViewUtils.createButton("Query Window", controlsPanel, this);
        addParty = ViewUtils.createButton("Add Party", controlsPanel, this);
        assign = ViewUtils.createButton("Assign Lanes", controlsPanel, this);
        finished = ViewUtils.createButton("Finished", controlsPanel, this);


        // Lane Status Panel
        JPanel laneStatusPanel = new JPanel();
        laneStatusPanel.setLayout(new GridLayout(numLanes, 1));
        laneStatusPanel.setBorder(new TitledBorder("Lane Status"));

        HashSet lanes = controlDesk.getLanes();
        Iterator it = lanes.iterator();
        int laneCount = 0;
        while (it.hasNext()) {
            Lane curLane = (Lane) it.next();
            LaneStatusView laneStat = new LaneStatusView(curLane, (laneCount + 1));
            curLane.addObserver(laneStat);
            JPanel lanePanel = laneStat.showLane();
            lanePanel.setBorder(new TitledBorder("Lane" + ++laneCount));
            laneStatusPanel.add(lanePanel);
        }

        // Party Queue Panel
        JPanel partyPanel = new JPanel();
        partyPanel.setLayout(new FlowLayout());
        partyPanel.setBorder(new TitledBorder("Party Queue"));

        Vector empty = new Vector();
        empty.add("(Empty)");

        partyList = new JList(empty);
        partyList.setFixedCellWidth(120);
        partyList.setVisibleRowCount(10);
        JScrollPane partyPane = new JScrollPane(partyList);
        partyPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        partyPanel.add(partyPane);

        // Clean up main panel
        colPanel.add(controlsPanel, "East");
        colPanel.add(laneStatusPanel, "Center");
        colPanel.add(partyPanel, "West");

        win.getContentPane().add("Center", colPanel);

        win.pack();

        /* Close program when this window closes */
        win.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // Center Window on Screen
        Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        win.setLocation(
                ((screenSize.width) / 2) - ((win.getSize().width) / 2),
                ((screenSize.height) / 2) - ((win.getSize().height) / 2));
        win.setVisible(true);
    }

    /**
     * Handler for actionEvents
     *
     * @param e the ActionEvent that triggered the handler
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(addParty)) {
            AddPartyView addPartyWin = new AddPartyView(this, maxMembers);
        }
        if (e.getSource().equals(assign)) {
            controlDesk.assignLane();
        }
        if (e.getSource().equals(finished)) {
            win.setVisible(false);
            EntryPointView view = new EntryPointView();
        }
        if (e.getSource().equals(query)) {
            QueryView queryView = new QueryView();
        }
    }

    /**
     * Receive a new party from andPartyView.
     *
     * @param addPartyView the AddPartyView that is providing a new party
     */
    public void updateAddParty(AddPartyView addPartyView) {
        controlDesk.addPartyQueue(addPartyView.getParty());
    }

    @Override
    public void update(Observable o, Object arg) {
        ControlDesk cd;
        try {
            cd = (ControlDesk) o;
        } catch (Exception e) {
            return;
        }
        partyList.setListData(((Vector) cd.getPartyQueue()));
    }
}