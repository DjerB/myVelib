package myVelibGUI;



import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import myVelib.exceptions.FullStationException;
import myVelib.system.Network;
import myVelibCLUI.Processing;

/**
 * Tab that provides the application with the possibility of creating
 * a new network.
 * @author Ahmed Djermani
 *
 */

public class SetupTab extends JPanel {
	
	private Box box = Box.createHorizontalBox();
	private JPanel container = new JPanel(new BorderLayout());
	
	private JPanel buttonPanel = new JPanel();
	private ButtonGroup bg = new ButtonGroup();
	private Box setupLeft = Box.createVerticalBox();
	private Box setupRight = Box.createVerticalBox();
	private Dimension boxDim = new Dimension(200, 200);
	
	/*private JRadioButton param1 = new JRadioButton("1 parameter", true);
	private JRadioButton param5 = new JRadioButton("5 parameters", true);
	
	private JTextField networkNameLeft = new JTextField("Enter the name of the network", 20);
	private JButton setupButtonLeft = new JButton("Setup network");
	
	private JTextField networkNameRight = new JTextField("Enter the name of the network", 20);
	private JTextField nbStations = new JTextField("Enter the number of stations", 2);
	private JTextField nbSlots = new JTextField("Enter the number of slots per station", 2);
	private JTextField sideArea = new JTextField("Enter the size of square grid", 5);
	private JTextField nbBikes = new JTextField("Enter the total number of bikes", 2);*/
	
	private JRadioButton param1 = new JRadioButton("1 parameter", true);
	private JRadioButton param5 = new JRadioButton("5 parameters", true);
	
	private JTextField networkNameLeft = new JTextField("Velib", 20);
	private JButton setupButtonLeft = new JButton("Setup network");
	
	private JTextField networkNameRight = new JTextField("Velib", 20);
	private JTextField nbStations = new JTextField("10", 2);
	private JTextField nbSlots = new JTextField("10", 2);
	private JTextField sideArea = new JTextField("4000", 5);
	private JTextField nbBikes = new JTextField("75", 2);
	
	private JButton setupButtonRight = new JButton("Setup network");
	
	public SetupTab() {
				
		// Button Group
		
		param1.setSelected(true);
		param1.addItemListener(new SetupChoice1());
		
		param5.setSelected(false);
		param5.addItemListener(new SetupChoice2());

		bg.add(param1);
		bg.add(param5);
		buttonPanel.add(param1);
		buttonPanel.add(param5);
		
		// SETUP left
		
		networkNameLeft.addFocusListener(new PlaceHolderNameLeft());
		networkNameLeft.setPreferredSize(new Dimension(150, 50));
		setupButtonLeft.addActionListener(new Setup());
		setupLeft.add(networkNameLeft);
		setupLeft.add(setupButtonLeft);
		setupLeft.setSize(boxDim);
		setupLeft.setBorder(new TitledBorder(new EtchedBorder(), "1 param setup"));
		
		// SETUP right

		networkNameRight.addFocusListener(new PlaceHolderNameRight());
		setupButtonRight.addActionListener(new Setup());
		setupRight.add(networkNameRight);
		nbStations.addFocusListener(new PlaceHolderNbStations());
		setupRight.add(nbStations);
		nbSlots.addFocusListener(new PlaceHolderNbSlots());
		setupRight.add(nbSlots);
		sideArea.addFocusListener(new PlaceHolderSideArea());
		setupRight.add(sideArea);
		nbBikes.addFocusListener(new PlaceHolderNbBikes());
		setupRight.add(nbBikes);
		setupRight.add(setupButtonRight);
		setupRight.setSize(boxDim);
		setupRight.setBorder(new TitledBorder(new EtchedBorder(), "5 param setup"));
		
		setBoxEnabled(setupRight, false);
		
		
		// Tab creation
		container.add(buttonPanel, BorderLayout.NORTH);
		box.add(setupLeft);
		box.add(setupRight);
		container.add(box, BorderLayout.SOUTH);
		this.add(container);
		this.setVisible(true);
	}
	
	
	class SetupChoice1 implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			
			if (e.getStateChange() == ItemEvent.SELECTED) {
				setBoxEnabled(setupRight, false);
				setBoxEnabled(setupLeft, true);
			}
		}
	
	}
	
	class SetupChoice2 implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			
			if (e.getStateChange() == ItemEvent.SELECTED) {
				setBoxEnabled(setupRight, true);
				setBoxEnabled(setupLeft, false);
			}
			
		}
		
	}
	
	private void setBoxEnabled(Box box, boolean isEnabled) {
		box.setEnabled(isEnabled);
		
		Component[] components = box.getComponents();
		
		for (int i= 0; i < components.length; i++) {
			components[i].setEnabled(isEnabled);
		}
	}
	
	
	class Setup implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (e.getSource() == setupButtonLeft) {
				
				String[] args = new String[2];
				args[0] = "setup";
				args[1] = networkNameLeft.getText();
				
				try {
					Processing.setup1param(args);
				} catch (NullPointerException | FullStationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			else if (e.getSource() == setupButtonRight) {
				
				String[] args = new String[6];
				
				args[0] = "setup";
				args[1] = networkNameRight.getText();
				args[2] = nbStations.getText();
				args[3] = nbSlots.getText();
				args[4] = sideArea.getText();
				args[5] = nbBikes.getText();
				
				try {
					Processing.setup5params(args);
				} catch (FullStationException e1) {
					e1.printStackTrace();
				}
			}
			
			ClientApplication.update();
			
		}
	}
	
	class PlaceHolderNameLeft implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
			
			if (networkNameLeft.getText().equals("Enter the the name of the network")) {
				networkNameLeft.setText("");}
		}

		@Override
		public void focusLost(FocusEvent e) {
			
			if (networkNameLeft.getText().equals("")) {
				networkNameLeft.setText("Enter the the name of the network");
			}
		}
	}
	
	class PlaceHolderNameRight implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
			
			if (networkNameRight.getText().equals("Enter the the name of the network")) {
			networkNameRight.setText("");}
		}

		@Override
		public void focusLost(FocusEvent e) {
			
			if (networkNameRight.getText().equals("")) {
				networkNameRight.setText("Enter the the name of the network");
			}
		}
	}
	
	class PlaceHolderNbStations implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
			
			if (nbStations.getText().equals("Enter the number of stations")) {
				nbStations.setText("");
			}
		}

		@Override
		public void focusLost(FocusEvent e) {
			
			if (nbStations.getText().equals("")) {
				nbStations.setText("Enter the number of stations");
			}
		}
	}
	
	class PlaceHolderNbSlots implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
			
			if (nbSlots.getText().equals("Enter the number of slots per station")) {
				nbSlots.setText("");
			}
			
		}

		@Override
		public void focusLost(FocusEvent e) {
			
			if (nbSlots.getText().equals("")) {
				nbSlots.setText("Enter the number of slots per station");
			}
			
		}
	}
	
	class PlaceHolderSideArea implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
			
			if (sideArea.getText().equals("Enter the size of square grid")) {
			sideArea.setText("");}
			
		}

		@Override
		public void focusLost(FocusEvent e) {
			
			if (sideArea.getText().equals("")) {
				sideArea.setText("Enter the size of square grid");
			}
			
		}
	}

	class PlaceHolderNbBikes implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
			
			if (nbBikes.getText().equals("Enter the total number of bikes")) {
				nbBikes.setText("");
			}
			
		}

		@Override
		public void focusLost(FocusEvent e) {
			
			if (nbBikes.getText().equals("")) {
				nbBikes.setText("Enter the total number of bikes");
			}
			
		}
	}
}
