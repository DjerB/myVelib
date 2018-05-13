package myVelibGUI;

import myVelibCLUI.*;
import myVelib.utilities.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.html.HTMLEditorKit;

import myVelib.station.StationIdGenerator;
import myVelib.system.Network;

/**
 * The Client Application class is the frame of the GUI, it is composed of various components
 * which enables to create commands.
 * @author Ahmed Djermani
 *
 */
public class ClientApplication extends JFrame {
	
	
		String title = "MyVelib Sharing Bike System !";
		JLabel image = new JLabel( new ImageIcon("back.png"));
		private static String message;
		
		private Box container = Box.createVerticalBox();
		 
		private static JLabel state = new JLabel();
		private static JTextPane console = new JTextPane();
		private static JScrollPane scroll = new JScrollPane();
		private Dimension appDim = new Dimension(1000, 800);
		private Dimension consoleDim = new Dimension(400, 200);
		private Dimension tabDim = new Dimension(900, 400);
		private JButton buttonExit = new JButton("Exit");
		private JButton buttonHelp = new JButton("Help");
		private JButton buttonReset = new JButton("Reset");
		private JButton buttonClear = new JButton("Clear");
		private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
		private SetupTab setupTab = new SetupTab();
		private AddUserTab addUserTab = new AddUserTab();
		private ReturnBikeTab returnBikeTab = new ReturnBikeTab();
		private RentBikeTab rentBikeTab = new RentBikeTab();
		private ReturnPlanBikeTab returnPlanBikeTab = new ReturnPlanBikeTab();
		private OfflineOnlineTab offlineOnlineTab = new OfflineOnlineTab();
		private StatisticsTab statsTab = new StatisticsTab();
		private SortTab sortTab = new SortTab();
		private DisplayTab displayTab = new DisplayTab();
		private PlanningTab planningTab = new PlanningTab();
		
		public ClientApplication() {
			
			this.add(image);
			this.setResizable(false);
			this.setSize(appDim);
			this.setTitle(title);
			this.setMinimumSize(appDim);
			this.setContentPane(container);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.pack();
			this.initComponents();
			this.setVisible(true);
		}
		
		private void initComponents() {
			
			// Basic buttons
			
			buttonExit.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);	
				}
				
			});
			
			buttonHelp.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					ClientApplication.update();
					console.setText(Interface.syllabus);
				}
				
			});
			
			buttonReset.addActionListener(new ResetSystem());
			
			buttonClear.addActionListener(new ClearScreen());
			
			// Bottom Panel
			
			JPanel bottomPanel = new JPanel();
			bottomPanel.add(buttonHelp);
			bottomPanel.add(buttonExit);
			bottomPanel.add(buttonReset);
			bottomPanel.add(buttonClear);
			
			// Console Panel to be updated
			
			console.setEditable(false);
			//console.setLineWrap(true);
			console.setMargin(new Insets(10, 10, 10, 10));
			console.setPreferredSize(consoleDim);
			console.setEditable(false);
			//console.setEditorKit(new HTMLEditorKit());
			console.setText("Welcome in MyVelib sharing bike System ! \n"
					+ "Select a command or 'help' if you want more informations and 'exit' "
					+ "to quit the application \n \n***************************************"
					+ "*********************************************************************"
					+ "*****************\n");
			//console.setLayout(new FlowLayout());
			scroll = new JScrollPane(console);
			scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			
			updateState();
			JPanel consolePanel = new JPanel(new BorderLayout());
			consolePanel.setBorder(new TitledBorder(new EtchedBorder(), "Console"));
			consolePanel.add(state, BorderLayout.NORTH);
			consolePanel.add(scroll);
			
			// Decisions/Commands panel
			
			tabbedPane.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					ClientApplication.update();
				}
				
			});
			
			setupTab.setMinimumSize(tabDim);
			addUserTab.setMinimumSize(tabDim);
			returnBikeTab.setMinimumSize(tabDim);
			rentBikeTab.setMinimumSize(tabDim);
			offlineOnlineTab.setMinimumSize(tabDim);
			statsTab.setMinimumSize(tabDim);
			sortTab.setMinimumSize(tabDim);
			displayTab.setMinimumSize(tabDim);
			planningTab.setMinimumSize(tabDim);

			tabbedPane.addTab("Setup", setupTab);
			tabbedPane.addTab("Add User", addUserTab);
			tabbedPane.addTab("Return Bike", returnBikeTab);
			tabbedPane.addTab("Return Plan Bike", returnPlanBikeTab);
			tabbedPane.addTab("Rent Bike", rentBikeTab);
			tabbedPane.addTab("Offline / Online", offlineOnlineTab);
			tabbedPane.addTab("Stats", statsTab);
			tabbedPane.addTab("Sort", sortTab);
			tabbedPane.addTab("Display", displayTab);
			tabbedPane.addTab("Plan Ride", planningTab);
			
			container.add(tabbedPane);
			container.add(consolePanel);
			container.add(bottomPanel, BorderLayout.SOUTH);
			
			this.setContentPane(container);
		}
		
		class ResetSystem implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				Network.resetSystem();
				ReturnBikeTab.resetStationList();
				ReturnBikeTab.resetUserList();
				RentBikeTab.resetStationList();
				RentBikeTab.resetUserList();
				ReturnPlanBikeTab.resetUserList();
				OfflineOnlineTab.resetStationList();
				StatisticsTab.resetStationList();
				StatisticsTab.resetUserList();
				PlanningTab.resetUserList();
				StationIdGenerator.resetCounter();
				console.setText("Welcome in MyVelib sharing bike System ! \n"
						+ "Select a command or 'help' if you want more informations and 'exit' "
						+ "to quit the application \n \n***************************************"
						+ "*********************************************************************"
						+ "*****************\n");
				ClientApplication.update();
			}
		}
		
		class ClearScreen implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				console.setText("");
				
			}
			
		}
		public static void update() {
			updateState();
			if (!PrintConsole.getMessage().equals(message)) {
				message = PrintConsole.getMessage();
				String newMessage = console.getText() + message;
				console.setText(newMessage); 
			}
			AddUserTab.updateNetworkList();
			ReturnBikeTab.updateNetworkList();
			ReturnPlanBikeTab.updateNetworkList();
			RentBikeTab.updateNetworkList();
			OfflineOnlineTab.updateNetworkList();
			StatisticsTab.updateNetworkList();
			SortTab.updateNetworkList();
			DisplayTab.updateNetworkList();
			PlanningTab.updateNetworkList();
		}
		
		private static void updateState() {
			
			String stateMessage = "Nb of networks : " + Network.getNetworks().size();
			state.setText(stateMessage);	
		}
	
		
		public static void main(String[] args) {
			
			ClientApplication app = new ClientApplication();
		}
		
		
}
