package myVelibGUI;

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
import myVelibGUI.ReturnBikeTab.PlaceHolder;
import myVelibGUI.ReturnBikeTab.ReturnBike;
import myVelibGUI.ReturnBikeTab.UpdateStation;
import myVelibGUI.ReturnBikeTab.UpdateUser;

/**
 * Tab that provides the application with the possibility of returning
 * a bike at the station calculated by the ride plan maker.
 * @author Ahmed Djermani
 *
 */

public class ReturnPlanBikeTab extends JPanel {

	private Box box = Box.createVerticalBox();
	private JPanel container = new JPanel(new BorderLayout());

	private Dimension boxDim = new Dimension(400, 400);
	
	private JPanel userPanel = new JPanel();
	private JLabel userText = new JLabel("Select the user ID");
	private static JComboBox<String> userList = new JComboBox<String>();
	
	private JPanel networkPanel = new JPanel();
	private JLabel networkText = new JLabel("Select the network");
	private static JComboBox<String> networkList = new JComboBox<String>();

	private JTextField timeField = new JTextField("Enter a number of minutes");
	
	private JButton returnButton = new JButton("Return Bike");
	
	public ReturnPlanBikeTab() {
		
		networkList.addItemListener(new UpdateUser());
		networkPanel.add(networkText);
		networkPanel.add(networkList);
		
		userPanel.add(userText);
		userPanel.add(userList);
		
		returnButton.addActionListener(new ReturnBike());
		timeField.addFocusListener(new PlaceHolder());
		box.add(networkPanel, Component.LEFT_ALIGNMENT);
		box.add(userPanel, Component.LEFT_ALIGNMENT);
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
	
	
	
	class ReturnBike implements ActionListener {

		String commandLine = "";
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			String time = timeField.getText();
			String networkName = networkList.getSelectedItem().toString();
			String userId = userList.getSelectedItem().toString();
			
			commandLine = "returnPlanBike " + userId + " " + time + " " + networkName;
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

