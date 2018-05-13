package myVelibCLUI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import myVelib.exceptions.FullStationException;
import myVelib.exceptions.MaxBikeException;
import myVelib.exceptions.MaxRideException;
import myVelib.exceptions.OccupiedException;
import myVelib.exceptions.OfflineException;
import myVelib.utilities.PrintConsole;

/**
 * Interface is the console provider of the CLUI. It contains all the description,
 * name and number of arguments of commands. Its main method receives command runtest
 * and checks the conformity of commands before sending to Processing.
 * @author Ahmed Djermani
 *
 */
public class Interface { 
	
	// Exit command
	private static final String EXIT_COMMAND = "exit";
	
	private static final String DESCRIPTION_EXIT = 
			"<b>exit</b> : exits the application \n";
	
	// Help command
		private static final String HELP_COMMAND = "help";
		
		private static final String DESCRIPTION_HELP = 
				"help : opens the syllabus \n";
	
	// Setup commands
	private static final String SETUP_COMMAND = "setup";
	
	private static final int NBR1_PARAM_SETUP = 1;
	private static final String DESCRIPTION_NBR1_SETUP =
			"<b>setup <velibnetworkName></b>: to create a myVelib network with given name and\n" + 
			"consisting of 10 stations each of which has 10 parking slots and such that stations\n" + 
			"are arranged on a square grid whose of side 4km and initially populated with a 75%\n" + 
			"bikes randomly distributed over the 10 stations";
	
	private static final int NBR2_PARAM_SETUP = 5;
	private static final String DESCRIPTION_NBR2_SETUP =
			"setup <name> <nstations> <nslots> <sidearea> <nbikes>: to create a myVelib network\n" + 
			"with given name and consisting of nstations stations each of which has nslots\n" + 
			"parking slots and such that stations are arranged on a square grid whose of side\n" + 
			"sidearea and initially populated with a nbikes bikes randomly distributed over the\n" + 
			"nstations stations";
	
	private static final String DESCRIPTION_SETUP_COMMAND = 
			"setup : used to instantiate a new network \n"
			+ DESCRIPTION_NBR1_SETUP + "\n" + DESCRIPTION_NBR2_SETUP + "\n";
	
	// AddUser command
	private static final String ADDUSER_COMMAND = "addUser";
	
	private static final int NBR1_PARAM_ADDUSER = 3;
	private static final String DESCRIPTION_NBR1_ADDUSER = 
			"addUser <userName,cardType, velibnetworkName> : to add a user with name\n" + 
			"userName and card cardType (i.e. ‘‘none’’ if the user has no card, Vlibre or Vmax otherwise) to a myVelib network\n" + 
			"velibnetworkName";
	
	private static final String DESCRIPTION_ADDUSER_COMMAND = 
			"addUser : used to add a new user \n"
			+ DESCRIPTION_NBR1_ADDUSER + "\n";
	
	// Offline/Online commands
	
	private static final String ONLINE_COMMAND = "online";
	
	private static final int NBR1_PARAM_ONLINE = 2;
	private static final String DESCRIPTION_NBR1_ONLINE = 
			"online <velibnetworkName, stationID> : to put online the station stationID of\n" + 
			"the myVelib network velibnetworkName";
	
	private static final String DESCRIPTION_ONLINE_COMMAND =
			"online : enables to put online a station \n"
			+ DESCRIPTION_NBR1_ONLINE + "\n";
	
private static final String OFFLINE_COMMAND = "offline";
	
	private static final int NBR1_PARAM_OFFLINE = 2;
	private static final String DESCRIPTION_NBR1_OFFLINE = 
			"offline <velibnetworkName, stationID> : to put offline the station stationID\n" + 
			"of the myVelib network velibnetworkName";
	
	private static final String DESCRIPTION_OFFLINE_COMMAND =
			"offline : enables to put offline a station \n"
			+ DESCRIPTION_NBR1_ONLINE + "\n";
	
	// RentBike/ReturnBike commands
	
	private static final String RENTBIKE_COMMAND = "rentBike";
	
	private static final int NBR1_PARAM_RENTBIKE = 5;
	private static final String DESCRIPTION_NBR1_RENTBIKE = 
			"rentBike <userID, stationID, networkName, typeBike, time> : to let the user userID renting a bike from station\n" + 
			"stationID (if no bikes are available should behave accordingly)";
	
	private static final String DESCRIPTION_RENTBIKE_COMMAND =
			"rentBike : enables to rent a bike \n"
			+ DESCRIPTION_NBR1_RENTBIKE + "\n";
	
	private static final String RETURNBIKE_COMMAND = "returnBike";
	
	private static final int NBR1_PARAM_RETURNBIKE = 4;
	private static final String DESCRIPTION_NBR1_RETURNBIKE = 
			"returnBike <userID, stationID, time, networkName> : to let the user userID returning a bike\n" + 
			"to station stationID at a given instant of time time (if no parking bay is available\n" + 
			"should behave accordingly). This command should display the cost of the rent";
	
	private static final String DESCRIPTION_RETURNBIKE_COMMAND =
			"returnBike : enables to return a bike \n"
			+ DESCRIPTION_NBR1_RETURNBIKE + "\n";
	
	private static final String RETURNPLANBIKE_COMMAND = "returnPlanBike";
	
	private static final int NBR1_PARAM_RETURNPLANBIKE = 3;
	private static final String DESCRIPTION_NBR1_RETURNPLANBIKE = 
			"returnBike <userID, time, networkName> : to let the user userID returning a bike\n" + 
			"to planned station at a given instant of time time (if no parking bay is available\n" + 
			"should behave accordingly). This command should display the cost of the rent";
	
	private static final String DESCRIPTION_RETURNPLANBIKE_COMMAND =
			"returnBike : enables to return a bike to planned station \n"
			+ DESCRIPTION_NBR1_RETURNBIKE + "\n";
	
	// DisplayStation commands
	
	private static final String DISPLAYSTATION_COMMAND = "displayStation";
	
	private static final int NBR1_PARAM_DISPLAYSTATION = 2;
	private static final String DESCRIPTION_NBR1_DISPLAYSTATION = 
			"displayStation<velibnetworkName, stationID> : to display the statistics (as of\n" + 
			"Section 2.4) of station stationID of a myVelib network velibnetwork.";

	
	private static final String DESCRIPTION_DISPLAYSTATION_COMMAND =
			"displayStation : displays the statistics of stations \n"
			+ DESCRIPTION_NBR1_DISPLAYSTATION + "\n";
	
	// DisplayUser commands
	
		private static final String DISPLAYUSER_COMMAND = "displayUser";
		
		private static final int NBR1_PARAM_DISPLAYUSER = 2;
		private static final String DESCRIPTION_NBR1_DISPLAYUSER = 
				"displayUser<velibnetworkName, userID> : to display the statistics (as of Section\n" + 
				"2.4) of user stationID of a myVelib network velibnetwork.";
		
		private static final String DESCRIPTION_DISPLAYUSER_COMMAND =
				"displayUser : displays the statistics of users \n"
				+ DESCRIPTION_NBR1_DISPLAYUSER+ "\n";
		
	// SortStation command
	
	private static final String SORTSTATION_COMMAND = "sortStation";
	
	private static final int NBR1_PARAM_SORTSTATION = 2;
	private static final String DESCRIPTION_NBR1_SORTSTATION = 
			"sortStation<velibnetworkName, sortpolicy> : to display the stations in increasing\n" + 
			"order w.r.t. to the sorting policy (as of Section 2.4) of user sortpolicy.";
	
	private static final String DESCRIPTION_SORTSTATION_COMMAND =
			"sortStation : enables to compute a sorting of stations \n"
			+ DESCRIPTION_NBR1_SORTSTATION + "\n";
	
	// Display command
	
	private static final String DISPLAY_COMMAND = "display";
	
	private static final int NBR1_PARAM_DISPLAY = 1;
	private static final String DESCRIPTION_NBR1_DISPLAY = 
			"display <velibnetworkName>: to display the entire status (stations, parking bays,\n" + 
			"users) of an a myVelib network velibnetworkName.";
	
	private static final String DESCRIPTION_DISPLAY_COMMAND =
			"display : enables to display the current state \n"
			+ DESCRIPTION_NBR1_DISPLAY + "\n";
	
	// Planning ride policies commands
	
	private static final String PLANRIDE_COMMAND = "planRide";
	
	private static final int NBR1_PARAM_PLANRIDE = 6;
	private static final String DESCRIPTION_NBR1_PLANRIDE = 
			"planRide <networkName, userID, destinationX, destinationY, typeOfBike, Strategy> : to plan a ride for a specific \n"
			+ "user following a given stategy and a given type of bike : Electrical or Mechanical. \n"
			+ "the strategy must be one the following : ShortestPath, FastestPath, AvoidPlus, PreferPlus, \n"
			+ "PreservationOfUniformity.";
	
	private static final String DESCRIPTION_PLANRIDE_COMMAND = 
			"planRide : enables to plan ride following policies \n"
			+ DESCRIPTION_NBR1_PLANRIDE + "\n";
	
	// Runtest command
	
	private static final String RUNTEST_COMMAND = "runtest";
	private static final int NBR1_PARAM_RUNTEST = 1;
	private static final String DESCRIPTION_NBR1_RUNTEST =
			"runtest <fileName.txt> : run the test scenario file.";
		
	private static final String RUNTEST_ERROR = "Wrong command : please check file name.";
				
	public static final String syllabus = DESCRIPTION_NBR1_RUNTEST
		+ DESCRIPTION_EXIT 
		+ DESCRIPTION_HELP
		+ DESCRIPTION_SETUP_COMMAND
		+ DESCRIPTION_ADDUSER_COMMAND
		+ DESCRIPTION_ONLINE_COMMAND
		+ DESCRIPTION_OFFLINE_COMMAND
		+ DESCRIPTION_RENTBIKE_COMMAND
		+ DESCRIPTION_RETURNBIKE_COMMAND
		+ DESCRIPTION_RETURNPLANBIKE_COMMAND
		+ DESCRIPTION_DISPLAYSTATION_COMMAND
		+ DESCRIPTION_DISPLAYUSER_COMMAND
		+ DESCRIPTION_SORTSTATION_COMMAND
		+ DESCRIPTION_DISPLAY_COMMAND
		+ DESCRIPTION_PLANRIDE_COMMAND;
	
	
	public static String newOutput ="";
	public static boolean quit = false;
	public static void main(String[] args) throws FullStationException, MaxBikeException, OccupiedException, OfflineException, MaxRideException, InterruptedException {
		
		System.out.println("Welcome in MyVelib sharing bike System !");
		System.out.println("Write a command or 'help' if you want more informations and 'exit' to quit the application");
		System.out.println("*******************************************************************************************");
		runTestFile("my_velib.ini");
		Scanner sc = new Scanner(System.in);
		String line;
		
		while (quit == false) {
			
			line = sc.nextLine();
			process(line);
			
		}
		sc.close();
	}
	
	public synchronized static void runTestFile(String fileName) {
		
		Processing.reset();
		fileName = "eval/" + fileName;
		String outputName = fileName.split("\\.")[0] + "Output.txt";
		
		FileReader file = null;
		BufferedReader reader = null;
		String output = "";
		
		try {
			file = new FileReader(fileName);
			reader = new BufferedReader(file); 
			String line = "";
			File fileWrite = new File(outputName);
			fileWrite.createNewFile();
			FileWriter writer = new FileWriter(fileWrite);
			
			
			while ((line = reader.readLine()) != null) {
				output = process(line);
				writer.write(output);
				writer.flush();
			}
			

			writer.close();
		} catch(Exception e) {
			
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {}
			}
			if (file != null) {
				try {
					file.close();
				} catch(IOException e) {}
			}
		}
		
	}
	
	public static String process(String line) throws FullStationException, MaxBikeException, OccupiedException, OfflineException, MaxRideException, InterruptedException {
		
		String WRONG_NBR_OF_ARGS = "Wrong number of arguments : please retry";
		String WRONG_COMMAND = "This command doesn't exist : please retry. Please mind that the system is case sensitive";
		String[] args = line.split(" ");
		/*for (String s : args) {
			System.out.println(s);
		}*/
		int nbWords = args.length;
		int nbParams = nbWords - 1;
		newOutput = "";
		boolean toChange = true;
		
		if (nbWords >= 1) {

			String command = args[0];
			
			switch(command) {
			
			case "runtest":
				String fileName = args[1];
				
				try {
				
				if (fileName.split("\\.")[1].equals("txt")) {
					runTestFile(fileName);}
				}
				catch(Exception e) {
					System.out.println(RUNTEST_ERROR);
				}
				break;
			case HELP_COMMAND:
				System.out.println(syllabus);
				newOutput = syllabus;
				Thread.sleep(1000);
				break;
			
			case EXIT_COMMAND:
				System.out.println("System has been shut down. Goodbye");
				newOutput = "System has been shut down";
				quit = true;
				Thread.sleep(1000);
				break;
			
			case SETUP_COMMAND:
				if (nbParams == NBR1_PARAM_SETUP) {
					Processing.setup1param(args);
					Thread.sleep(1000);
				}
				else if (nbParams == NBR2_PARAM_SETUP) {
					Processing.setup5params(args);
					Thread.sleep(1000);
				}
				else {
					System.out.println(WRONG_NBR_OF_ARGS);
					newOutput = WRONG_NBR_OF_ARGS;
				}
				break;
			
			case ADDUSER_COMMAND:
				if (nbParams == NBR1_PARAM_ADDUSER) {
					Processing.addUser3Params(args);
					Thread.sleep(1000);
				}
				else {
					System.out.println(WRONG_NBR_OF_ARGS);
					newOutput = WRONG_NBR_OF_ARGS;
				}
				break;
			
			case ONLINE_COMMAND:
				if (nbParams == NBR1_PARAM_ONLINE) {
					Processing.online2params(args);
					Thread.sleep(1000);
				}
				else {
					System.out.println(WRONG_NBR_OF_ARGS);
					newOutput = WRONG_NBR_OF_ARGS;
				}
				break;
			
			case OFFLINE_COMMAND:
				if (nbParams == NBR1_PARAM_OFFLINE) {
					Processing.offline2params(args);
					Thread.sleep(1000);
				}
				else {
					System.out.println(WRONG_NBR_OF_ARGS);
					newOutput = WRONG_NBR_OF_ARGS;
				}
				break;
			
			case RENTBIKE_COMMAND:
				if (nbParams == NBR1_PARAM_RENTBIKE) {
					Processing.renBike5params(args);
					Thread.sleep(1000);
				}
				else {
					System.out.println(WRONG_NBR_OF_ARGS);
					newOutput = WRONG_NBR_OF_ARGS;
				}
				break;
			
			case RETURNBIKE_COMMAND:
				if (nbParams == NBR1_PARAM_RETURNBIKE) {
					Processing.returnBike4params(args);
					Thread.sleep(1000);
				}
				else {
					System.out.println(WRONG_NBR_OF_ARGS);
					newOutput = WRONG_NBR_OF_ARGS;
				}
				break;
			
			case RETURNPLANBIKE_COMMAND:
				if (nbParams == NBR1_PARAM_RETURNPLANBIKE) {
					Processing.returnPlanBike3params(args);
					Thread.sleep(1000);
				}
				else {
					System.out.println(WRONG_NBR_OF_ARGS);
					newOutput = WRONG_NBR_OF_ARGS;
				}
				break;
				
			case DISPLAYSTATION_COMMAND:
				if (nbParams == NBR1_PARAM_DISPLAYSTATION) {
					Processing.diplayStation2params(args);
					Thread.sleep(1000);
				}
				else {
					System.out.println(WRONG_NBR_OF_ARGS);
					newOutput = WRONG_NBR_OF_ARGS;
				}
				break;
			
			case DISPLAYUSER_COMMAND:
				if (nbParams == NBR1_PARAM_DISPLAYUSER) {
					Processing.displayUser2params(args);
					Thread.sleep(1000);
				}
				else {
					System.out.println(WRONG_NBR_OF_ARGS);
					newOutput = WRONG_NBR_OF_ARGS;
				}
				break;
			
			case SORTSTATION_COMMAND:
				if (nbParams == NBR1_PARAM_SORTSTATION) {
					Processing.sortStation2params(args);
					toChange = false;
					Thread.sleep(1000);
				}
				else {
					System.out.println(WRONG_NBR_OF_ARGS);
					newOutput = WRONG_NBR_OF_ARGS;
				}
				break;
				
			case DISPLAY_COMMAND:
				if (nbParams == NBR1_PARAM_DISPLAY) {
					Processing.display1param(args);
					toChange = false;
					Thread.sleep(1000);
				}
				else {
					System.out.println(WRONG_NBR_OF_ARGS);
					newOutput = WRONG_NBR_OF_ARGS;
				}
				break;
			
			case PLANRIDE_COMMAND:
				if (nbParams == NBR1_PARAM_PLANRIDE) {
					Processing.planRide5params(args);
					Thread.sleep(1000);
				}
				else {
					System.out.println(WRONG_NBR_OF_ARGS);
					newOutput = WRONG_NBR_OF_ARGS;
				}
				break;
			
			default:
				System.out.println(WRONG_COMMAND);
				newOutput = WRONG_COMMAND;
				break;
			}
		}
		else {
			System.out.println("Empty line");
			newOutput = "Empty line";
		}
		if (newOutput.equals("") && toChange) {
			newOutput = PrintConsole.getMessage();
		}
		return newOutput;
		
	}
	
	public static void updateOutput(String message) {
		
	}
	
}

