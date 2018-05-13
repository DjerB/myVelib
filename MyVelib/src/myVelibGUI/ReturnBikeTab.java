package myVelibGUI;

import myVelib.exceptions.FullStationException;
import myVelib.exceptions.MaxBikeException;
import myVelib.exceptions.MaxRideException;
import myVelib.exceptions.OccupiedException;
import myVelib.exceptions.OfflineException;
import myVelib.station.Station;
import myVelib.userAndCard.*;
import myVelib.system.*;
import myVelibCLUI.Interface;
import myVelibCLUI.Processing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import myVelibGUI.SetupTab.Setup;
import myVelibGUI.SetupTab.SetupChoice1;
import myVelibGUI.SetupTab.SetupChoice2;

/**
 * Tab that provides the application with the possibility of returning
 * a bike at a given station.
 * @author Ahmed Djermani
 *
 */

public class ReturnBikeTab extends JPanel {
	
	private Box box = Box.createVerticalBox();
	private JPanel container = new JPanel(new BorderLayout());

	private Dimension boxDim = new Dimension(400, 400);
	
	private JPanel userPanel = new JPanel();
	private JLabel userText = new JLabel("Select the user ID");
	private static JComboBox<String> userList = new JComboBox<String>();
	
	private JPanel stationPanel = new JPanel();
	private JLabel stationText = new JLabel("Select the station ID");
	private static JComboBox<String> stationList = new JComboBox<String>();
	
	private JPanel networkPanel = new JPanel();
	private JLabel networkText = new JLabel("Select the network");
	private static JComboBox<String> networkList = new JComboBox<String>();

	private JTextField timeField = new JTextField("Enter a number of minutes");
	
	private JButton returnButton = new JButton("Return Bike");
	
	public ReturnBikeTab() {
		
		networkList.addItemListener(new UpdateUser());
		networkList.addItemListener(new UpdateStation());
		networkPanel.add(networkText);
		networkPanel.add(networkList);
		
		userPanel.add(userText);
		userPanel.add(userList);
		stationPanel.add(stationText);
		stationPanel.add(stationList);
		
		returnButton.addActionListener(new ReturnBike());
		timeField.addFocusListener(new PlaceHolder());
		box.add(networkPanel, Component.LEFT_ALIGNMENT);
		box.add(userPanel, Component.LEFT_ALIGNMENT);
		box.add(stationPanel, Component.LEFT_ALIGNMENT);
		box.add(timeField, Component.LEFT_ALIGNMENT);
		box.add(returnButton, Component.LEFT_ALIGNMENT);
		box.setBorder(new TitledBorder(new EtchedBorder(), "ReturnBike menu"));
		box.setMinimumSize(boxDim);
		
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
	
	public static void resetStationList() {
		stationList.removeAllItems();
	}
	
	public static void resetUserList() {
		userList.removeAllItems();
	}
	
	
	private static void updateStationList(Network network) {
		
		stationList.removeAllItems();
		
		for (Entry<Integer, Station> idStation : network.getStations().entrySet()) {
			String id = "" + idStation.getValue().getId();
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
	
	class ReturnBike implements ActionListener {

		String commandLine = "";
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			String time = timeField.getText();
			String networkName = networkList.getSelectedItem().toString();
			String stationId = stationList.getSelectedItem().toString();
			String userId = userList.getSelectedItem().toString();
			
			commandLine = "returnBike " + userId + " " + stationId + " " + time + " " + networkName;
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
