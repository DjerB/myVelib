package myVelibCLUI;

import myVelib.exceptions.FullStationException;
import myVelib.exceptions.MaxBikeException;
import myVelib.exceptions.MaxRideException;
import myVelib.exceptions.OccupiedException;
import myVelib.exceptions.OfflineException;
import myVelib.station.Station;
import myVelib.system.*;
import myVelib.stationSorting.*;

import java.util.List;
import java.util.Map.Entry;

import myVelib.userAndCard.*;
import myVelib.utilities.Position;
import myVelib.utilities.PrintConsole;
import myVelib.utilities.Time;
import myVelib.utilities.typeBike;

/**
 * Processing is the brain of the CLUI.It contains all the methods 
 * that execute the command passed in Interface. Each method checks the 
 * contents of commands and call the appropriate method in myVelin core.
 * @author Ahmed Djermani
 *
 */
public class Processing {
	
	private static final String errorMessage = "You entered an incorrect type of argument. Please retry";
	private static final String errorNullNetwork = "No network has this name. Please retry";
	private static final String userIdError = "userId must be an integer";
	private static final String stationIdError = "stationId must be an integer";
	private static final String typeOfBikeError = "typeOfBike must be 'electrical' or 'mechanical'";
	private static final String timeError = "time must be an integer";
	private static final String policyError = "The policy must be leastusedstationsorter or mostusedstationsorter";
	private static final String strategyError = "The strategy must be shortestpath, fastestpath, preservationofuniformity, avoidplus or preferplus";
	private static final String destinationXError = "destinationX must be an integer";
	private static final String destinationYError = "destinationY must be an integer";
	
	public static void reset() {
		Network.resetSystem();
	}
	
	public static void setup1param(String[] args) throws NullPointerException, FullStationException {
		
		String name = args[1];
		
		
		new Network(name);
		
	}
	
	public static void setup5params(String[] args) throws FullStationException {
		
		String name = new String();
		int nbStations = 0;
		int nbSlots = 0;
		int sideArea = 0;
		int nbBikes = 0;
		boolean correctSetup = true;
		String message;
		
		name = args[1];
		
		try {
			nbStations = Integer.parseInt(args[2]);
		} catch(Exception e) {
			message = "nbStations must be an integer";
			System.out.println(message);
			PrintConsole.updateScreenMessage(message);
			correctSetup = false;
		}
		try {
			nbSlots = Integer.parseInt(args[3]);
		} catch(Exception e) {
			message = "nbSlots must be an integer";
			System.out.println(message);
			PrintConsole.updateScreenMessage(message);
			correctSetup = false;
		}
		try {
			sideArea = Integer.parseInt(args[4]);
		} catch(Exception e) {
			message = "sideArea must be an integer";
			System.out.println(message);
			PrintConsole.updateScreenMessage(message);
			correctSetup = false;
		}
		try {
			nbBikes = Integer.parseInt(args[5]);
		} catch(Exception e) {
			message = "nbBikes must be an integer";
			System.out.println(message);
			PrintConsole.updateScreenMessage(message);
			correctSetup = false;
		}
		
		if (correctSetup) {
			new Network(name, nbStations, nbSlots, sideArea, nbBikes);
		}
		else {
			errorMessage();
		}
		
	}
	
	public static void addUser3Params(String[] args) {
		
		//addUser <userName,cardType, velibnetworkName>
		String userName = args[1];
		String typeCard = new String();
		Card card = null;
		String networkName = args[3];
		boolean canBeProcessed = true;
		String message;
	
		try {
			 typeCard = args[2].toLowerCase();
		} catch(Exception e ) {
			message = "typeCard must be a string";
			System.out.println(message);
			PrintConsole.updateScreenMessage(message);
			canBeProcessed = false;
		}
		
		
		if (canBeProcessed && checkNetworkName(networkName)) {
			
			boolean cardDefined = true;
			typeCard = typeCard.toLowerCase();  // Type of Card in command line is not case sensitive
			
			switch(typeCard) {
			
			case "none" : 
				card = Network.getNetworks().get(networkName).noCardFactory.createCard();
				break;
			
			case "vlibre" :
				card = Network.getNetworks().get(networkName).vlibreFactory.createCard();
				break; 
			
			case "vmax" :
				card = Network.getNetworks().get(networkName).vmaxFactory.createCard();
				break;
				
			default :
				message = "The cardType you entered doesn't match with any type of card";
				System.out.println(message);
				PrintConsole.updateScreenMessage(message);
				cardDefined = false;
				break;
			}
			
			if (cardDefined) {
				Position userPosition = Network.getNetworks().get(networkName).getRandomPosition();
				Network.getNetworks().get(networkName).addUser(new User(userName, userPosition, card, Network.getNetworks().get(networkName)));
			}
			else {
				errorMessage();
			}
			
		}
		
	}
	
	
	public static void online2params(String[] args) {
		
		boolean canBeProcessed = true;
		int stationId = 0;
		String networkName = args[1];
		
		
		try {
			stationId = Integer.parseInt(args[2]);
			if (canBeProcessed && checkNetworkName(networkName)) {
				Network.getNetworks().get(networkName).getStations().get(stationId).setOnline(true);
			} else {
				System.out.println(errorMessage);
			}
		} catch(Exception e) {
			System.out.println("stationId must be an integer");
			canBeProcessed = false;
		}
	}
	
	public static void offline2params(String[] args) {
		
		boolean canBeProcessed = true;
		int stationId = 0;
		String networkName = args[1];
		
		
		try {
			stationId = Integer.parseInt(args[2]);
			if (canBeProcessed && checkNetworkName(networkName)) {
				Network.getNetworks().get(networkName).getStations().get(stationId).setOnline(false);
			} else {
				System.out.println(errorMessage);
			}
		} catch(Exception e) {
			System.out.println("stationId must be an integer");
			canBeProcessed = false;
		}
	
	}

	public static void renBike5params(String[] args) throws MaxBikeException {
		
		
		if (checkUserId(args[1]) && checkStationId(args[2]) && checkNetworkName(args[3]) && checkTypeBike(args[4]) && checkTime(args[5])) {
			
			Time.updateTime(Integer.parseInt(args[5]));
			int userId = Integer.parseInt(args[1]);
			int stationId = Integer.parseInt(args[2]);
			
			String type = args[4].toLowerCase();

			try {
				Network.getNetworks().get(args[3]).getUser(userId).rentBike(Network.getNetworks().get(args[3]).getStations().get(stationId), 
					(type == "electrical") ? typeBike.Electrical : typeBike.Mechanical);
			} catch(NullPointerException e) {}
		}
		
		else {
			errorMessage();
		}
	}
	
	public static void returnBike4params(String[] args) throws MaxBikeException, OccupiedException, OfflineException, FullStationException {
		
		if (checkUserId(args[1]) && checkStationId(args[2]) && checkNetworkName(args[4]) && checkTime(args[3])) {
			
			int userId = Integer.parseInt(args[1]);
			int stationId = Integer.parseInt(args[2]);
			
			Time.updateTime(Integer.parseInt(args[3]));
			
			try {
				Network.getNetworks().get(args[4]).getStations().get(stationId).getFreeSlot().receiveBike(
					Network.getNetworks().get(args[4]).getUser(userId).getRide().getBike(), Network.getNetworks().get(args[4]));
			} catch(NullPointerException e) {
				
			}
		}
		
		else {
			errorMessage();
		}
	}
	
	public static void returnPlanBike3params(String[] args) throws MaxBikeException, OccupiedException, OfflineException, FullStationException {
		
		if (checkUserId(args[1]) && checkNetworkName(args[3]) && checkTime(args[2])) {
			
			int userId = Integer.parseInt(args[1]);
			int stationId = Network.getNetworks().get(args[3]).getUser(userId).getRide().getEndStation().getId();
			
			Time.updateTime(Integer.parseInt(args[2]));
			
			try {
				Network.getNetworks().get(args[3]).getStations().get(stationId).getFreeSlot().receiveBike(
					Network.getNetworks().get(args[3]).getUser(userId).getRide().getBike(), Network.getNetworks().get(args[3]));
			} catch(NullPointerException e) {
				
			}
		}
		
		else {
			errorMessage();
		}
	}
		
		
	public static void diplayStation2params(String[] args) {
		
		if (checkStationId(args[2]) && checkNetworkName(args[1])) {
			
			int stationId = Integer.parseInt(args[2]);
			String networkName = args[1];
			
			Network.getNetworks().get(networkName).printStationStats(stationId);

		}
		else {
			errorMessage();
		}
	}
	
	public static void displayUser2params(String[] args) {
		
		if (checkUserId(args[2]) && checkNetworkName(args[1])) {
			
			int userId = Integer.parseInt(args[2]);
			String networkName = args[1];
			
			Network.getNetworks().get(networkName).printUserStats(userId);
		}
		else {
			errorMessage();
		}
	}
	
	public static void sortStation2params(String[] args) {
		
		if (checkNetworkName(args[1]) && checkSortPolicy(args[2])) {
			
			String networkName = args[1];
			String policy = args[2].toLowerCase();
			
			if (policy.equals("leastusedstationsorter")) {
				LeastUsedStationSorter sorter = new LeastUsedStationSorter(Network.getNetworks().get(networkName));
				List<Station> stations = sorter.sort();
				for (Station station : stations) {
					PrintConsole.updateScreenMessage(station.toString());
					Interface.newOutput += station.toString();
					System.out.println(station.toString());
				}
			}
			else {
				MostUsedStationSorter sorter = new MostUsedStationSorter(Network.getNetworks().get(networkName));
				List<Station> stations = sorter.sort();
				for (Station station : stations) {
					PrintConsole.updateScreenMessage(station.toString());
					Interface.newOutput += station.toString();
					System.out.println(station.toString());
				}
			}
		}
		else {
			errorMessage();
		}
		
	}
	
	public static void display1param(String[] args) {
		
		if (checkNetworkName(args[1])) {
			
			String networkName = args[1];
			String status = "";
			
			for (Entry<Integer, Station> idStation : Network.getNetworks().get(networkName).getStations().entrySet()) {
				
				Station station = idStation.getValue();
				PrintConsole.updateScreenMessage(station.toString());
				Interface.newOutput += station.toString();
				System.out.println(station.toString());
				for (Station.Slot slot : station.getSlots()) {
					PrintConsole.updateScreenMessage(slot.toString());
					Interface.newOutput += slot.toString();
					System.out.println(slot.toString());
				}
			}
			
			for (Entry<Integer, User> idUser : Network.getNetworks().get(networkName).getUsers().entrySet()) {
				
				User user = idUser.getValue();
				PrintConsole.updateScreenMessage(user.toString());
				Interface.newOutput += user.toString();
				System.out.println(user.toString());
			}
			
		} else {
			errorMessage();
		}
	}
	
	public static void planRide5params(String[] args) throws MaxRideException {
		
		if (checkNetworkName(args[1]) && checkUserId(args[2]) && checkDestinationX(args[3]) && checkDestinationY(args[4])
				&& checkTypeBike(args[5]) && checkStrategy(args[6])) {
			
			String networkName = args[1];
			int userId = Integer.parseInt(args[2]);
			Position destination = new Position(Integer.parseInt(args[3]), Integer.parseInt(args[4]));
			String strategy = args[6].toLowerCase();
			String type = args[5].toLowerCase();
			typeBike bikeType = typeBike.Electrical;
			
			if (type == "mechanical") {
				bikeType = typeBike.Mechanical;
			}
			switch(strategy) {
				
			case "shortestpath" :
				Network.getNetworks().get(networkName).getUser(userId).planShortestPath(destination, bikeType);
				break;
			
			case "fastestpath" :
				Network.getNetworks().get(networkName).getUser(userId).planFastestPath(destination, bikeType);
				break;
			
			case "avoidplus" :
				Network.getNetworks().get(networkName).getUser(userId).planAvoidPlusStation(destination, bikeType);;
				break;
			
			case "preferplus" :
				Network.getNetworks().get(networkName).getUser(userId).planPreferPlusStation(destination, bikeType);;
				break;
				
			case "preservationofuniformity" :
				Network.getNetworks().get(networkName).getUser(userId).planPreservationOfUniformity(destination, bikeType);
				break;
			}
			
		}
		else {
			errorMessage();
		}
	}
		
		
	// Utilities methods 
	
	public static boolean checkNetworkName(String networkName) {
		
		boolean canBeProcessed = true;
				
		try {
			Network network = Network.getNetworks().get(networkName);
		} catch(Exception e) {
			System.out.println(errorNullNetwork);
			PrintConsole.updateScreenMessage(errorNullNetwork);
			canBeProcessed = false;
		}
			
		return canBeProcessed;
	}
	
	public static boolean checkUserId(String id) {
			
		boolean canBeProcessed = true;
			
		try {
			int userId = Integer.parseInt(id);
		} catch(Exception e) {
			System.out.println(userIdError);
			PrintConsole.updateScreenMessage(userIdError);
			canBeProcessed = false;
		}
			
		return canBeProcessed;
	}
		
	public static boolean checkStationId(String id) {
			
		boolean canBeProcessed = true;
			
		try {
			int stationId = Integer.parseInt(id);
		} catch(Exception e) {
			System.out.println(stationIdError);
			PrintConsole.updateScreenMessage(stationIdError);
			canBeProcessed = false;
		}
			
		return canBeProcessed;
	}
		
	public static boolean checkTypeBike(String type) {
			
		boolean canBeProcessed = true;
		type = type.toLowerCase();
			
		if (type.equals("electrical") && type.equals("mechanical")) {
			canBeProcessed = false;
			System.out.println(typeOfBikeError);
			PrintConsole.updateScreenMessage(typeOfBikeError);
		}
			
		return canBeProcessed;
			
	}
	
	public static boolean checkTime(String time) {
		
		boolean canBeProcessed = true;
		
		try {
			int duration = Integer.parseInt(time);
		} catch(Exception e) {
			System.out.println(timeError);
			PrintConsole.updateScreenMessage(timeError);
			canBeProcessed = false;
		}
		
		return canBeProcessed;
	}
	
	public static boolean checkSortPolicy(String policy) {
		
		boolean canBeProcessed = true;
		policy = policy.toLowerCase();
		
		if (!policy.equals("leastusedstationsorter") && !policy.equals("mostusedstationsorter")) {
			canBeProcessed = false;
			System.out.println(policyError);
			PrintConsole.updateScreenMessage(policyError);
		}
		
		return canBeProcessed;
	}
	
	public static boolean checkStrategy(String strategy) {
		
		boolean canBeProcessed = true;
		strategy = strategy.toLowerCase();
		
		if (!strategy.equals("shortestpath") && !strategy.equals("fastestpath") && !strategy.equals("preservationofuniformity")
				&& !strategy.equals("avoidplus") && !strategy.equals("preferplus")) {
			canBeProcessed = false;
			System.out.println(strategyError);
			PrintConsole.updateScreenMessage(strategyError);
		}
		
		return canBeProcessed;
	}
	
	
	
	public static boolean checkDestinationX(String x) {
		
		boolean canBeProcessed = true;
		
		try {
			int coordinate = Integer.parseInt(x);
		} catch(Exception e) {
			System.out.println(destinationXError);
			PrintConsole.updateScreenMessage(destinationXError);
			canBeProcessed = false;
		}
		
		return canBeProcessed;
	}

	public static boolean checkDestinationY(String y) {
	
		boolean canBeProcessed = true;
	
		try {
			int coordinate = Integer.parseInt(y);
		} catch(Exception e) {
			System.out.println(destinationYError);
			PrintConsole.updateScreenMessage(destinationYError);
			canBeProcessed = false;
		}
		
		return canBeProcessed;
	}
	
	public static void errorMessage() {
		
		System.out.println(errorMessage);
		PrintConsole.updateScreenMessage(errorMessage);
	}
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
