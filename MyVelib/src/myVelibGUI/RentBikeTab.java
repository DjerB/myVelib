package myVelibGUI;

import java.awt.BorderLayout;
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
import myVelib.station.Station;
import myVelib.system.Network;
import myVelib.userAndCard.User;
import myVelibCLUI.Interface;

/**
 * Tab that provides the application with the possibility of renting
 * a bike at a given station.
 * @author Ahmed Djermani
 *
 */

public class RentBikeTab extends JPanel {
	
	private Box box = Box.createVerticalBox();

	private Dimension boxDim = new Dimension(200, 200);
	
	private JPanel userPanel = new JPanel();
	private JLabel userText = new JLabel("Select the user ID");
	private static JComboBox<String> userList = new JComboBox<String>();
	
	private JPanel stationPanel = new JPanel();
	private JLabel stationText = new JLabel("Select the station ID");
	private static JComboBox<String> stationList = new JComboBox<String>();
	
	private JPanel networkPanel = new JPanel();
	private JLabel networkText = new JLabel("Select the network");
	private static JComboBox<String> networkList = new JComboBox<String>();

	private JPanel typeBikePanel = new JPanel();
	private JLabel typeBikeText = new JLabel("Select a type of bike");
	private JComboBox<String> typeBikeList = new JComboBox<String>();
	
	private JTextField timeField = new JTextField("Enter a number of minutes");
	
	private JButton rentButton = new JButton("Rent Bike");
	
	public RentBikeTab() {
		
		networkList.addItemListener(new UpdateUser());
		networkList.addItemListener(new UpdateStation());
		networkPanel.add(networkText);
		networkPanel.add(networkList);
		
		userPanel.add(userText);
		userPanel.add(userList);

		stationPanel.add(stationText);
		stationPanel.add(stationList);
		typeBikeList.addItem("electrical");
		typeBikeList.addItem("mechanical");
		typeBikePanel.add(typeBikeText);
		typeBikePanel.add(typeBikeList);
		timeField.addFocusListener(new PlaceHolder());
		rentButton.addActionListener(new RentBike());
		box.add(networkPanel);
		box.add(userPanel);
		box.add(stationPanel);
		box.add(typeBikePanel);
		box.add(timeField);
		box.add(rentButton);
		box.setMinimumSize(new Dimension(400, 400));
		box.setBorder(new TitledBorder(new EtchedBorder(), "Rent Bike"));
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

	
	private static void updateStationList(Network network) {
		
		stationList.removeAllItems();
		
		for (Entry<Integer, Station> idStation : network.getStations().entrySet()) {
			String id = "" + idStation.getKey();
			stationList.addItem(id);
		}
	}
	
	private static void updateUserList(Network network) {
		
		userList.removeAllItems();
		for (Entry<Integer, User> idUser : network.getUsers().entrySet()) {
			String id = "" + idUser.getKey();
			userList.addItem(id);
		}
		
	}
	
	public static void resetStationList() {
		stationList.removeAllItems();
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
	
	class UpdateStation implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent event) {
			try {
				String networkName = networkList.getSelectedItem().toString();
				if (networkName != null) {
					updateStationList(Network.getNetworks().get(networkName));
				}
			} catch(Exception e) {}
			
			
		}
		
	}
	
	class RentBike implements ActionListener {
		
		
		String commandLine = "";

		@Override
		public void actionPerformed(ActionEvent e) {
			
			String time = timeField.getText();
			String typeBike = typeBikeList.getSelectedItem().toString();
			String networkName = networkList.getSelectedItem().toString();
			String stationId = stationList.getSelectedItem().toString();
			String userId = userList.getSelectedItem().toString();
			
			commandLine = "rentBike " + userId + " " + stationId + " " + networkName + " " + typeBike + " " + time;
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
	
	class PlaceHolder implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
			
			if (timeField.getText().equals("Enter a number of minutes")) {
				timeField.setText("");
			}
			
		}

		@Override
		public void focusLost(FocusEvent e) {
			
			if (timeField.getText().equals("")) {
				timeField.setText("Enter a number of minutes");
			}
			
		}
		
		
	}
}
