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
 * Tab that provides the application with the possibility of sorting
 * stations of a given network.
 * @author Ahmed Djermani
 *
 */

public class SortTab extends JPanel {
	
	private Box container = Box.createVerticalBox();
	
	private JPanel networkPanel = new JPanel();
	private JLabel networkText = new JLabel("Select the network");
	private static JComboBox<String> networkList = new JComboBox<String>();
	
	private JPanel policyPanel = new JPanel();
	private JLabel policyText = new JLabel("Select a policy for sorting");
	private static JComboBox<String> policyList = new JComboBox<String>();
	
	private JButton sortButton = new JButton("Sort");
	
	public SortTab() {
		
		networkPanel.add(networkText);
		networkPanel.add(networkList);
		
		policyList.addItem("leastusedstationsorter");
		policyList.addItem("mostusedstationsorter");
		policyPanel.add(policyText);
		policyPanel.add(policyList);
		
		sortButton.addActionListener(new Sort());;
		container.add(networkPanel);
		container.add(policyPanel);
		container.add(sortButton);
		container.setBorder(new TitledBorder(new EtchedBorder(), "Sort"));
		this.add(container);
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
	
	class Sort implements ActionListener {

		String commandLine = "";
		@Override
		public void actionPerformed(ActionEvent e) {
			
			String networkName = networkList.getSelectedItem().toString();
			String policy = policyList.getSelectedItem().toString();
			
			commandLine = "sortStation " + networkName + " " + policy;
			
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
