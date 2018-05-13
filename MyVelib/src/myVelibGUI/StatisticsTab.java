package myVelibGUI;

import myVelib.exceptions.FullStationException;
import myVelib.exceptions.MaxBikeException;
import myVelib.exceptions.MaxRideException;
import myVelib.exceptions.OccupiedException;
import myVelib.exceptions.OfflineException;
import myVelib.station.Station;
import myVelib.system.*;
import myVelib.userAndCard.User;
import myVelibCLUI.Interface;
import myVelibCLUI.Processing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

/**
 * Tab that provides the application with the possibility of displaying
 * the statistics of a given station and of a given user.
 * @author Ahmed Djermani
 *
 */
public class StatisticsTab extends JPanel {
	
	private JPanel container = new JPanel();
	
	private Box stationStats = Box.createVerticalBox();
	private Box userStats = Box.createVerticalBox();
	
	private JPanel userNetworkPanel = new JPanel();
	private JLabel userNetworkText = new JLabel("Select the network");
	private static JComboBox<String> userNetworkList = new JComboBox<String>();
	private JPanel userPanel = new JPanel();
	private JLabel userText = new JLabel("Select the user ID");
	private static JComboBox<String> userList = new JComboBox<String>();
	private JButton userButton = new JButton("Display user stats");
	
	private JPanel stationNetworkPanel = new JPanel();
	private JLabel stationNetworkText = new JLabel("Select the network");
	private static JComboBox<String> stationNetworkList = new JComboBox<String>();
	private JPanel stationPanel = new JPanel();
	private JLabel stationText = new JLabel("Select the station ID");
	private static JComboBox<String> stationList = new JComboBox<String>();
	private JButton stationButton = new JButton("Display station stats");
	
	public StatisticsTab() {
		
		userNetworkList.addItemListener(new UpdateUser());
		userNetworkPanel.add(userNetworkText);
		userNetworkPanel.add(userNetworkList);
		userPanel.add(userText);
		userPanel.add(userList);

		
		stationNetworkList.addItemListener(new UpdateStation());
		stationNetworkPanel.add(stationNetworkText);
		stationNetworkPanel.add(stationNetworkList);
		stationPanel.add(stationText);
		stationPanel.add(stationList);
		
		userButton.addActionListener(new UserStats());
		userStats.add(userNetworkPanel);
		userStats.add(userPanel);
		userStats.add(userButton);
		userStats.setBorder(new TitledBorder(new EtchedBorder(), "User Stats"));
		userStats.setMinimumSize(new Dimension(100, 200));
		
		stationButton.addActionListener(new StationStats());
		stationStats.add(stationNetworkPanel);
		stationStats.add(stationPanel);
		stationStats.add(stationButton);
		stationStats.setBorder(new TitledBorder(new EtchedBorder(), "Station Stats"));
		stationStats.setMinimumSize(new Dimension(100, 200));

		
		container.add(userStats);
		container.add(stationStats);
		
		this.add(container);
		this.setVisible(true);
		
		
	}
	
	public static void updateNetworkList() {
		
		String networkName;
		userNetworkList.removeAllItems();
		stationNetworkList.removeAllItems();

		for (Entry<String, Network> nameNetwork : Network.getNetworks().entrySet()) {
			
			networkName = nameNetwork.getKey();
			
			userNetworkList.addItem(networkName);
			stationNetworkList.addItem(networkName);
		}
	}
	
	private static void updateStationList(Network network) {
		
		stationList.removeAllItems();
	
		for (Entry<Integer, Station> idStation : network.getStations().entrySet()) {
			String id = "" + idStation.getKey();
			stationList.addItem(id);
		}
		
	}
	
	public static void resetStationList() {
		stationList.removeAllItems();
	}
	
	public static void resetUserList() {
		userList.removeAllItems();
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
				String networkName = userNetworkList.getSelectedItem().toString();
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
				String networkName = stationNetworkList.getSelectedItem().toString();
				if (networkName != null) {
					updateStationList(Network.getNetworks().get(networkName));
				}
			} catch(Exception e) {}
			
			
		}
	}
	
	class UserStats implements ActionListener {

		String commandLine = "";
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			String networkName = userNetworkList.getSelectedItem().toString();
			String userId = userList.getSelectedItem().toString();
			
			commandLine = "displayUser " + networkName + " " + userId;
			
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
	
	class StationStats implements ActionListener {

		String commandLine = "";
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			String networkName = stationNetworkList.getSelectedItem().toString();
			String stationId = stationList.getSelectedItem().toString();
			
			commandLine = "displayStation " + networkName + " " + stationId;
			
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
}
