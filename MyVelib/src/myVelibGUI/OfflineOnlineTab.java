package myVelibGUI;

import java.awt.BorderLayout;
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
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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
import myVelibGUI.SetupTab.SetupChoice1;
import myVelibGUI.SetupTab.SetupChoice2;

/**
 * Tab that provides the application with the possibility of setting a 
 * station online/offline.
 * @author Ahmed Djermani
 *
 */

public class OfflineOnlineTab extends JPanel {
	
	private Box box = Box.createVerticalBox();
	
	private JPanel networkPanel = new JPanel();
	private JLabel networkText = new JLabel("Select the network");
	private static JComboBox networkList = new JComboBox();
	
	private JPanel stationPanel = new JPanel();
	private JLabel stationText = new JLabel("Select the station ID");
	private static JComboBox stationList = new JComboBox();
	
	private JPanel buttonPanel = new JPanel();
	private ButtonGroup bg = new ButtonGroup();
	private JRadioButton online = new JRadioButton("Online", true);
	private JRadioButton offline = new JRadioButton("Offline", true);
	
	private static JButton setButton = new JButton("Set State");
	
	public OfflineOnlineTab() {
		
		networkList.addItemListener(new UpdateStation());
		networkPanel.add(networkText);
		networkPanel.add(networkList);
		
		stationPanel.add(stationText);
		stationPanel.add(stationList);
		
		online.setSelected(true);
		
		offline.setSelected(false);

		bg.add(online);
		bg.add(offline);
		buttonPanel.add(online);
		buttonPanel.add(offline);
		
		setButton.addActionListener(new Set());
		box.add(networkPanel);
		box.add(stationPanel);
		box.add(buttonPanel);
		box.add(setButton, BorderLayout.CENTER);
		box.setBorder(new TitledBorder(new EtchedBorder(), "Offline/Online"));
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
	
	public static void resetStationList() {
		stationList.removeAllItems();
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
	
	class Set implements ActionListener {
		
		String commandLine = "";
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			String command = (online.isSelected()) ? "online " : "offline ";
			String networkName = networkList.getSelectedItem().toString();
			String stationId = stationList.getSelectedItem().toString();
			
			commandLine = command + networkName + " " + stationId;
			
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
