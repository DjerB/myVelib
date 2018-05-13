package myVelibGUI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import myVelib.exceptions.FullStationException;
import myVelib.exceptions.MaxBikeException;
import myVelib.exceptions.MaxRideException;
import myVelib.exceptions.OccupiedException;
import myVelib.exceptions.OfflineException;
import myVelib.system.Network;
import myVelib.userAndCard.User;
import myVelibCLUI.Interface;

/**
 * Tab that provides the application with the possibility of planning
 * a ride for a given user.
 * @author Ahmed Djermani
 *
 */

public class PlanningTab extends JPanel {
	
	private Box box = Box.createVerticalBox();
	
	private JPanel networkPanel = new JPanel();
	private JLabel networkText = new JLabel("Select the network");
	private static JComboBox<String> networkList = new JComboBox<String>();
	
	private JPanel userPanel = new JPanel();
	private JLabel userText = new JLabel("Select the user ID");
	private static JComboBox<String> userList = new JComboBox<String>();
	
	
	private JTextField destinationXField = new JTextField("Enter destination X");
	private JTextField destinationYField = new JTextField("Enter destination Y");
	
	private JPanel typeBikePanel = new JPanel();
	private JLabel typeBikeText = new JLabel("Select a type of bike");
	private JComboBox<String> typeBikeList = new JComboBox<String>();
	
	private JPanel strategyPanel = new JPanel();
	private JLabel strategyText = new JLabel("Select a planning policy");
	private JComboBox<String> strategyList = new JComboBox<String>();
	
	private JButton planButton = new JButton("Plan the ride");
	
	public PlanningTab() {
		
		networkList.addItemListener(new UpdateUser());
		networkPanel.add(networkText);
		networkPanel.add(networkList);
		
		userPanel.add(userText);
		userPanel.add(userList);
		
		typeBikeList.addItem("electrical");
		typeBikeList.addItem("mechanical");
		typeBikePanel.add(typeBikeText);
		typeBikePanel.add(typeBikeList);
		
		strategyList.addItem("shortestpath");
		strategyList.addItem("fastestpath");
		strategyList.addItem("preferplus");
		strategyList.addItem("avoidplus");
		strategyList.addItem("preservationofuniformity");
		strategyPanel.add(strategyText);
		strategyPanel.add(strategyList);
		
		destinationXField.addFocusListener(new PlaceHolderX());
		destinationYField.addFocusListener(new PlaceHolderY());
		planButton.addActionListener(new PlanRide());
		box.add(networkPanel);
		box.add(userPanel);
		box.add(destinationXField);
		box.add(destinationYField);
		box.add(typeBikePanel);
		box.add(strategyPanel);
		box.add(planButton);
		box.setMinimumSize(new Dimension(500, 500));
		box.setBorder(new TitledBorder(new EtchedBorder(), "Add user"));
		this.add(box);
		this.setVisible(true);
		
	}
	
	public static void updateNetworkList() {
		
		String networkName;
		networkList.removeAllItems();
		
		for (Entry<String, Network> nameNetwork : Network.getNetworks().entrySet()) {
			
			networkName = nameNetwork.getKey();
			
			networkList.addItem(networkName);
			
		}
	}
	
	private static void updateUserList(Network network) {
		
		userList.removeAllItems();
		
		for (Entry<Integer, User> idUser : network.getUsers().entrySet()) {
			String id = "" + idUser.getKey();
			userList.addItem(id);
		}
	}
	
	public static void resetUserList() {
		userList.removeAllItems();
	}
	
	class UpdateUser implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent event) {
			try {
				String networkName = networkList.getSelectedItem().toString();
				if (networkName != null) {
					updateUserList(Network.getNetworks().get(networkName));
				}
			} catch(Exception e) {}
		}
	}
	
	class PlanRide implements ActionListener {

		String commandLine = "";
		@Override
		public void actionPerformed(ActionEvent e) {
			
			String networkName = networkList.getSelectedItem().toString();
			String userId = userList.getSelectedItem().toString();
			String destinationX = destinationXField.getText();
			String destinationY = destinationYField.getText();
			String typeOfBike = typeBikeList.getSelectedItem().toString();
			String strategy = strategyList.getSelectedItem().toString();
			
			commandLine = "planRide " + networkName + " " + userId + " " + destinationX 
					+ " " + destinationY + " " + typeOfBike + " " + strategy;
			
			
			try {
				Interface.process(commandLine);
			} catch (FullStationException | MaxBikeException | OccupiedException | OfflineException | MaxRideException
					| InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			ClientApplication.update();
			
		}
		
	}
	
	class PlaceHolderX implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
			
			if (destinationXField.getText().equals("Enter destination X")) {
				destinationXField.setText("");
			}
			
		}

		@Override
		public void focusLost(FocusEvent e) {
			
			if (destinationXField.getText().equals("")) {
				destinationXField.setText("Enter destination X");
			}
			
		}
		
	}
	
	class PlaceHolderY implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
			
			if (destinationYField.getText().equals("Enter destination Y")) {
				destinationYField.setText("");
			}
			
		}

		@Override
		public void focusLost(FocusEvent e) {
			
			if (destinationYField.getText().equals("")) {
				destinationYField.setText("Enter destination Y");
			}
			
		}
		
	}
}
