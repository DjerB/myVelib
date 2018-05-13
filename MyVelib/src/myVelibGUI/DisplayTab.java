package myVelibGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import myVelib.exceptions.FullStationException;
import myVelib.exceptions.MaxBikeException;
import myVelib.exceptions.MaxRideException;
import myVelib.exceptions.OccupiedException;
import myVelib.exceptions.OfflineException;
import myVelib.system.Network;
import myVelibCLUI.Interface;

/**
 * Tab that provides the application with the possibility of displaying
 * the status of a network.
 * @author Ahmed Djermani
 *
 */

public class DisplayTab extends JPanel {
	
	private Box box = Box.createVerticalBox();
	
	private JPanel networkPanel = new JPanel();
	private JLabel networkText = new JLabel("Select the network");
	private static JComboBox<String> networkList = new JComboBox<String>();
	
	private JButton displayButton = new JButton("Display");
	
	
	public DisplayTab() {
		
		networkPanel.add(networkText);
		networkPanel.add(networkList);
		
		displayButton.addActionListener(new Display());
		box.add(networkPanel);
		box.add(displayButton);
		box.setBorder(new TitledBorder(new EtchedBorder(), "Display"));
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
	
	class Display implements ActionListener {

		String commandLine = "";
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			String networkName = networkList.getSelectedItem().toString();
			
			commandLine = "display " + networkName;
			
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
