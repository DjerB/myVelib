package myVelibGUI;

import myVelib.station.Station;
import myVelib.system.*;
import myVelibCLUI.Processing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.FocusListener;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
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

import myVelibGUI.SetupTab.SetupChoice1;
import myVelibGUI.SetupTab.SetupChoice2;

/**
 * Tab that provides the application with the possibility of adding
 * a new user to a network.
 * @author Ahmed Djermani
 *
 */
public class AddUserTab extends JPanel {

	private Box box = Box.createVerticalBox();
	private JButton addButton = new JButton("Add User");
	private JPanel cardPanel = new JPanel();
	private JPanel networkPanel = new JPanel();
	private JTextField userName = new JTextField("Enter the user name");
	private JLabel cardText = new JLabel("Choose the type of card");
	private JComboBox<String> cardList = new JComboBox<String>();		
	private JLabel networkText = new JLabel("Choose the network");
	private static JComboBox<String> networkList = new JComboBox<String>();
	
	public AddUserTab() {
		
		userName.addFocusListener(new PlaceHolder());
		cardList.addItem("none");
		cardList.addItem("Vlibre");
		cardList.addItem("Vmax");
		cardPanel.add(cardText);
		cardPanel.add(cardList);

		
		networkPanel.add(networkText);
		networkPanel.add(networkList);
		
		addButton.addActionListener(new AddUser());
		box.add(userName);
		box.add(cardPanel);
		box.add(networkPanel);
		box.add(addButton);
		
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
	
	class AddUser implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (e.getSource() == addButton) {
				String[] args = new String[4];
			
			
				args[0] = "addUser";
				args[1] = userName.getText();
				args[2] = cardList.getSelectedItem().toString();
				args[3] = networkList.getSelectedItem().toString();
				
				Processing.addUser3Params(args);
				ClientApplication.update();
			}
		}
		
	}
	
	class PlaceHolder implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
			
			if (userName.getText().equals("Enter the user name")) {
				userName.setText("");
			}
			
		}

		@Override
		public void focusLost(FocusEvent e) {
			
			if (userName.getText().equals("")) {
				userName.setText("Enter the user name");
			}
			
		}
		
		
	}
}
