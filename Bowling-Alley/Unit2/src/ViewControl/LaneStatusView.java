package ViewControl;

import Model.Bowler;
import Model.Lane;
import Model.Pinsetter;
import Model.PinsetterEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

public class LaneStatusView implements ActionListener, Observer {

	private JPanel jp;
	private JLabel curBowler, pinsDown;
	private JButton viewLane;
	private JButton viewPinSetter, maintenance;
	private OverallLaneView olv;
	private PinsetterView psv;
	private LaneView lv;
	private Lane lane;
	int laneNum;



	public LaneStatusView(Lane lane, int laneNum ) {

		this.lane = lane;
		this.laneNum = laneNum;
		Pinsetter ps = lane.getPinsetter();
		ps.addObserver(this);
		olv = new OverallLaneView(lane, laneNum);

		jp = new JPanel();
		jp.setLayout(new FlowLayout());
		JLabel cLabel = new JLabel( "Now Bowling: " );
		curBowler = new JLabel( "(no one)" );
		JLabel fLabel = new JLabel( "Foul: " );
		JLabel pdLabel = new JLabel( "Pins Down: " );
		pinsDown = new JLabel( "0" );

		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		Insets buttonMargin = new Insets(4, 4, 4, 4);

		viewLane = new JButton("View Lane");
		JPanel viewLanePanel = new JPanel();
		viewLanePanel.setLayout(new FlowLayout());
		viewLane.addActionListener(this);
		viewLanePanel.add(viewLane);



		maintenance = new JButton("Pause/Resume");
		maintenance.setBackground(Color.GREEN);
		maintenance.setOpaque(true);
		JPanel maintenancePanel = new JPanel();
		maintenancePanel.setLayout(new FlowLayout());
		maintenance.addActionListener(this);
		maintenancePanel.add(maintenance);

		viewLane.setEnabled( false );


		buttonPanel.add(viewLanePanel);

		buttonPanel.add(maintenancePanel);

		jp.add( cLabel );
		jp.add( curBowler );
		jp.add( pdLabel );
		jp.add( pinsDown );
		jp.add(buttonPanel);
	}

	public JPanel showLane() {
		return jp;
	}

	public void actionPerformed( ActionEvent e ) {
		if (lane.isPartyAssigned()) {
			if (e.getSource().equals(viewLane)) {
				lane.unPauseGame();
				olv.toggle();

			}
		if (e.getSource().equals(maintenance)) {
			if ( lane.isGameIsHalted() ) {
				lane.unPauseGame();
				maintenance.setBackground( Color.GREEN );
			}
			else{
				lane.pauseGame();
				maintenance.setBackground( Color.RED );
			}
		}
	}}

	@Override
	public void update(Observable o, Object arg) {
		
		if(o instanceof Pinsetter){
			PinsetterEvent pe = (PinsetterEvent)arg;
			pinsDown.setText( ( new Integer(pe.totalPinsDown()) ).toString() );
		}
		else if(o instanceof Lane){
			Lane le = (Lane)o;
			if(lane.isGameFinished() && lane.isPartyAssigned()){ // Start end of game routine
				EndGamePrompt egp = new EndGamePrompt( ((Bowler) le.getParty().getMembers().get(0)).getNickName() + "'s Party", le.getWinner());
				int result = egp.getResult();
				egp.distroy();
				egp = null;	
				if (result == 1) {					// yes, want to play again
					lane.resetScores();
					lane.resetBowlerIterator();
				}
				else if (result == 2) {// no, dont want to play another game
					Vector printVector;	
					EndGameReport egr = new EndGameReport( ((Bowler)le.getParty().getMembers().get(0)).getNickName() + "'s Party", le.getParty());
					printVector = egr.getResult();
					olv.getframe().dispose(); // destrory the combine newly added frame
					Iterator scoreIt = le.getParty().getMembers().iterator();
					lane.clearLane();
					int myIndex = 0;
					while (scoreIt.hasNext()){
						Bowler thisBowler = (Bowler)scoreIt.next();
						ScoreReport sr = new ScoreReport( thisBowler, lane.getFinalScores()[myIndex++], lane.getGameNumber() );
						sr.sendEmail(thisBowler.getEmail());
						Iterator printIt = printVector.iterator();
						while (printIt.hasNext()){
							if (thisBowler.getNick() == (String)printIt.next()){
								System.out.println("Printing " + thisBowler.getNick());
								sr.sendPrintout();
							}
						}
					}
				}
			}				
			curBowler.setText(le.getCurrentThrower().getNickName());
			if ( le.isGameIsHalted() ) {
				maintenance.setBackground( Color.RED );
			}	
			if ( lane.isPartyAssigned() == false ) {
				viewLane.setEnabled( false );
				//viewPinSetter.setEnabled( false );
			} else {
				viewLane.setEnabled( true );
				//viewPinSetter.setEnabled( true );
			}
		}
	}	
}