package ViewControl;

import Model.Pinsetter;
import Model.PinsetterEvent;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

/**
 *  constructs a prototype PinSetter GUI
 *
 */
public class PinsetterView implements Observer {

    private Vector pinVect = new Vector ( );
    private JPanel firstRoll;
    private JPanel secondRoll;
	public Container cpanel;

	private Image getScaledImage(JPanel label, String emo) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(String.format("Unit2/resources/%s.png", emo)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img.getScaledInstance(30, 60, Image.SCALE_SMOOTH); // please check once giving error
	}
	private JFrame frame;
    
    /**
     * Constructs a Pin Setter GUI displaying which roll it is with
     * yellow boxes along the top (1 box for first roll, 2 boxes for second)
     * and displays the pins as numbers in this format:
     *
     *                7   8   9   10
     *                  4   5   6
     *                    2   3
     *                      1
     *
     */
    public PinsetterView ( Pinsetter ps, int laneNum ) {
		ps.addObserver(this);
		frame = new JFrame ( "Lane " + laneNum + ":" );
		
		cpanel = frame.getContentPane ( );
		
		JPanel pins = new JPanel ( );
		
		pins.setLayout ( new GridLayout ( 4, 7 ) );
		
		//********************Top of GUI indicates first or second roll
		
		JPanel top = new JPanel ( );
		
		firstRoll = new JPanel ( );
		firstRoll.setBackground( Color.yellow );
		
		secondRoll = new JPanel ( );
		secondRoll.setBackground ( Color.black );
		
		top.add ( firstRoll, BorderLayout.WEST );
		
		top.add ( secondRoll, BorderLayout.EAST );
		
		//******************************************************************
		
		//**********************Grid of the pins**************************
		
		
		JPanel one = new JPanel ();
		JLabel oneL = new JLabel ( "1" );
		oneL.setIcon(new ImageIcon(getScaledImage(one,
				"pin")));
		one.add (oneL);
		JPanel two = new JPanel ();
		JLabel twoL = new JLabel ( "2" );
		twoL.setIcon(new ImageIcon(getScaledImage(two,
				"pin")));
		two.add (twoL);
		JPanel three = new JPanel ();
		JLabel threeL = new JLabel ( "3" );
		threeL.setIcon(new ImageIcon(getScaledImage(three,
				"pin")));
		three.add (threeL);
		JPanel four = new JPanel ();
		JLabel fourL = new JLabel ( "4" );
		fourL.setIcon(new ImageIcon(getScaledImage(four,
				"pin")));
		four.add (fourL);
		JPanel five = new JPanel ();
		JLabel fiveL = new JLabel ( "5" );
		fiveL.setIcon(new ImageIcon(getScaledImage(five,
				"pin")));
		five.add (fiveL);
		JPanel six = new JPanel ();
		JLabel sixL = new JLabel ( "6" );
		sixL.setIcon(new ImageIcon(getScaledImage(six,
				"pin")));
		six.add (sixL);
		JPanel seven = new JPanel ();
		JLabel sevenL = new JLabel ( "7" );
		sevenL.setIcon(new ImageIcon(getScaledImage(seven,
				"pin")));
		seven.add (sevenL);
		JPanel eight = new JPanel ();
		JLabel eightL = new JLabel ( "8" );
		eightL.setIcon(new ImageIcon(getScaledImage(eight,
				"pin")));
		eight.add (eightL);
		JPanel nine = new JPanel ();
		JLabel nineL = new JLabel ( "9" );
		nineL.setIcon(new ImageIcon(getScaledImage(nine,
				"pin")));
		nine.add (nineL);
		JPanel ten = new JPanel ();
		JLabel tenL = new JLabel ( "10" );
		tenL.setIcon(new ImageIcon(getScaledImage(ten,
				"pin")));
		ten.add (tenL);
		
		//This Vector will keep references to the pin labels to show
		//which ones have fallen.
		
		pinVect.add ( oneL );
		pinVect.add ( twoL );
		pinVect.add ( threeL );
		pinVect.add ( fourL );
		pinVect.add ( fiveL );
		pinVect.add ( sixL );
		pinVect.add ( sevenL );
		pinVect.add ( eightL );
		pinVect.add ( nineL );
		pinVect.add ( tenL );	
		
		
		//******************************Fourth Row**************
		
		pins.add ( seven );
		pins.add ( new JPanel ( ) );
		pins.add ( eight );
		pins.add ( new JPanel ( ) );
		pins.add ( nine );
		pins.add ( new JPanel ( ) );
		pins.add ( ten );
		
		//*****************************Third Row***********
			
		pins.add ( new JPanel ( ) );
		pins.add ( four );
		pins.add ( new JPanel ( ) );
		pins.add ( five );
		pins.add ( new JPanel ( ) );
		pins.add ( six );
		
		//*****************************Second Row**************
	 
		pins.add ( new JPanel ( ) );
		pins.add ( new JPanel ( ) );
		pins.add ( new JPanel ( ) );
		pins.add ( two );
		pins.add ( new JPanel ( ) );
		pins.add ( three );
		pins.add ( new JPanel ( ) );
		pins.add ( new JPanel ( ) );
		
		//******************************First Row*****************
		
		pins.add ( new JPanel ( ) );
		pins.add ( new JPanel ( ) );
		pins.add ( new JPanel ( ) );
		pins.add ( one );
		pins.add ( new JPanel ( ) );
		pins.add ( new JPanel ( ) );
		pins.add ( new JPanel ( ) );
		//*********************************************************
		
		top.setBackground ( Color.black );
		
		cpanel.add ( top, BorderLayout.NORTH );
		
		pins.setBackground ( Color.black );
		pins.setForeground ( Color.yellow );
		
		cpanel.add ( pins, BorderLayout.CENTER );
		
		frame.pack();
	}
    
    public void show() {
    	frame.show();
    }

    public void hide() {
    	frame.hide();
    }

	@Override
	public void update(Observable o, Object arg) {
		PinsetterEvent pe = (PinsetterEvent)arg;
		if ( !(pe.isFoulCommited()) ) {
	    	JLabel tempPin = new JLabel ( );
	    	for ( int c = 0; c < 10; c++ ) {
				boolean pin = pe.pinKnockedDown ( c );
				tempPin = (JLabel)pinVect.get ( c );
				if ( pin ) {
		    		tempPin.setForeground ( Color.lightGray );
				}
	    	}
    	}
		if ( pe.getThrowNumber() == 1 ) {
	   		 secondRoll.setBackground ( Color.yellow );
		}
		if ( pe.pinsDownOnThisThrow() == -1) {
			for ( int i = 0; i != 10; i++){
				((JLabel)pinVect.get(i)).setForeground(Color.black);
			}
			secondRoll.setBackground( Color.black);
		}
	}
}