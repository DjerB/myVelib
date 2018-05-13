package myVelib.system;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import myVelib.bike.*;
import myVelib.ride.*;
import myVelib.station.*;
import myVelib.stationFactory.*;
import myVelib.bikeFactory.*;
import myVelib.exceptions.FullStationException;
import myVelib.userAndCard.*;
import myVelib.utilities.*;
import myVelib.cardFactory.*;

/**
 * MyVelib is the central database that contains all of the objects and methods to add new objects
 * @author Ahmed Djermani
 *
 */

public class Network {
	
	private static Map<String, Network> networks = new HashMap<String, Network>();
	private int id;
	private String name;
	private int sideArea;
	private Map<Integer, Station> stations = new HashMap<Integer, Station>();
	private Map<Integer, Ride> rides = new HashMap<Integer, Ride>();
	private Map<Integer, ElectricalBike> electricalBikes = new HashMap<Integer, ElectricalBike>();
	private Map<Integer, MechanicalBike> mechanicalBikes = new HashMap<Integer, MechanicalBike>();
	private Map<Integer, User> users = new HashMap<Integer,User>();
	private Time beginningTime;
	
	public StandardStationFactory standardFactory = new StandardStationFactory();
	public PlusStationFactory plusFactory = new PlusStationFactory();
	public ElectricalFactory elecFactory = new ElectricalFactory();
	public MechanicalFactory mechaFactory = new MechanicalFactory();
	public NoCardFactory noCardFactory = new NoCardFactory();
	public VlibreFactory vlibreFactory = new VlibreFactory();
	public VmaxFactory vmaxFactory = new VmaxFactory();

	public static void addNetwork(Network network) {
		networks.put(network.getName(), network);
	}
	
	public Network() {
		Network.addNetwork(this);
	}
	
	public Network(String name) throws NullPointerException, FullStationException {
		
		synchronized(this) {
			this.id = NetworkIdGenerator.getNextNetworkId();
			this.beginningTime = new Time();
			Position position;
			int nbStations = 10;
			int nbBikes = 75;
			int nbSlots = 10;
			int sideArea = 4000;
			int nbPlusStations = (int) (nbStations * 0.2);
			int nbStandardStations = nbStations - nbPlusStations;
			int nbElecBikes = (int) (nbBikes * 0.3);
			int nbMechaBikes = nbBikes - nbElecBikes;
			int extraElecBikes = nbElecBikes % nbStations;
			int nbElecBikesPerStation = (nbElecBikes - extraElecBikes) / nbStations;
			int extraMechaBikes = nbMechaBikes % nbStations;
			int nbMechaBikesPerStation = (nbMechaBikes - extraMechaBikes) / nbStations;
			
			this.name = name;
			this.sideArea = sideArea;
			
			synchronized(this) {
				for (int i = 0; i < nbStandardStations; i++) {
					position = this.getRandomPosition();
					Station station = this.standardFactory.createStation(position);
					for (int k = 0; k < nbSlots; k++) {
						station.addSlot();
					}
					for (int k = 0; k < nbElecBikesPerStation; k++) {
						station.initializeElecBike(elecFactory.createBike());
					}
					for (int k = 0; k < nbMechaBikesPerStation; k++) {
						station.initializeMechaBike(mechaFactory.createBike());
					}
					this.addStation(station);
				}
				for (int i = 0; i < nbPlusStations; i++) {
					position = this.getRandomPosition();
					Station station = this.plusFactory.createStation(position);
					for (int k = 0; k < nbSlots; k++) {
						station.addSlot();
					}
					for (int k = 0; k < nbElecBikesPerStation; k++) {
						station.initializeElecBike(elecFactory.createBike());
					}
					for (int k = 0; k < nbMechaBikesPerStation; k++) {
						station.initializeMechaBike(mechaFactory.createBike());
					}
					this.addStation(station);
				}
			}
			
			for (int i = 1; i <= extraElecBikes; i++) {
				try {
					this.getStations().get(i).initializeElecBike(elecFactory.createBike());
				} catch(NullPointerException e) {
					
				}
			}
			for (int i = nbStations; i > nbStations - extraMechaBikes; i--) {
				try {
					this.getStations().get(i).initializeMechaBike(mechaFactory.createBike());
				} catch(NullPointerException e) {
					
				}
			}
			
			Network.addNetwork(this);
		}
	}
	
	
	public Network(String name, int nbStations, int nbSlots, int sideArea, int nbBikes) throws FullStationException, NullPointerException {
		
		synchronized(this) {
			this.id = NetworkIdGenerator.getNextNetworkId();
			this.beginningTime = new Time();
			Position position;
			int nbPlusStations = (int) (nbStations * 0.2);
			int nbStandardStations = nbStations - nbPlusStations;
			int nbElecBikes = (int) (nbBikes * 0.3);
			int nbMechaBikes = nbBikes - nbElecBikes;
			int extraElecBikes = nbElecBikes % nbStations;
			int nbElecBikesPerStation = (nbElecBikes - extraElecBikes) / nbStations;
			int extraMechaBikes = nbMechaBikes % nbStations;
			int nbMechaBikesPerStation = (nbMechaBikes - extraMechaBikes) / nbStations;
			
			this.name = name;
			this.sideArea = sideArea;
			
			synchronized(this) {
				for (int i = 0; i < nbStandardStations; i++) {
					position = this.getRandomPosition();
					Station station = this.standardFactory.createStation(position);
					for (int k = 0; k < nbSlots; k++) {
						station.addSlot();
					}
					for (int k = 0; k < nbElecBikesPerStation; k++) {
						station.initializeElecBike(elecFactory.createBike());
					}
					for (int k = 0; k < nbMechaBikesPerStation; k++) {
						station.initializeMechaBike(mechaFactory.createBike());
					}
					this.addStation(station);
				}
				for (int i = 0; i < nbPlusStations; i++) {
					position = this.getRandomPosition();
					Station station = this.plusFactory.createStation(position);
					for (int k = 0; k < nbSlots; k++) {
						station.addSlot();
					}
					for (int k = 0; k < nbElecBikesPerStation; k++) {
						station.initializeElecBike(elecFactory.createBike());
					}
					for (int k = 0; k < nbMechaBikesPerStation; k++) {
						station.initializeMechaBike(mechaFactory.createBike());
					}
					this.addStation(station);
				}
			}
			for (int i = 1; i <= extraElecBikes; i++) {
				try {
					this.getStations().get(i).initializeElecBike(elecFactory.createBike());
				} catch(NullPointerException e) {
					
				}
			}
			for (int i = nbStations; i > nbStations - extraMechaBikes; i--) {
				try {
					this.getStations().get(i).initializeMechaBike(mechaFactory.createBike());
				} catch(NullPointerException e) {
					
				}
			}
			
			Network.addNetwork(this);
		}
	}
	
	public void addStation(Station station) {
		stations.put(station.getId(), station);
	}
	
	public void addRide(Ride ride) {
		rides.put(ride.getId(), ride);
	}
	
	public void addElectricalBike(ElectricalBike bike) {
		electricalBikes.put(bike.getId(), bike);
	}
	
	public void addMechanicalBike(MechanicalBike bike) {
		mechanicalBikes.put(bike.getId(), bike);
	}
	
	public void addUser(User user) {
		users.put(user.getUserId(), user);
	}
	
	public static Map<String, Network> getNetworks() { return networks; }
	
	public String getName() { return this.name; }
	
	public int getId() { return this.id; }
	
	public Time getBeginningTime() { return beginningTime; }
	
	public Map<Integer, Station> getStations() { return stations; }
	
	public Map<Integer, ElectricalBike> getElectricalBikes() { return electricalBikes; }
	
	public Map<Integer, MechanicalBike> getMechanicalBikes() { return mechanicalBikes; }

	
	public Map<Integer, User> getUsers() { return users; }
	
	public Map<Integer, Ride> getRides() { return rides; }

	public User getUser(int id) {
		return(users.get(id));
	}
	
	public static void resetSystem() {
		
		for (Entry<String, Network> nameNetwork : networks.entrySet()) {
			nameNetwork.getValue().users = new HashMap<Integer, User>();
			nameNetwork.getValue().stations = new HashMap<Integer, Station>();
			nameNetwork.getValue().electricalBikes = new HashMap<Integer, ElectricalBike>();
			nameNetwork.getValue().mechanicalBikes = new HashMap<Integer, MechanicalBike>();
			nameNetwork.getValue().rides = new HashMap<Integer, Ride>();
			nameNetwork = null;
		}
		networks = new HashMap<String, Network>();
		StationIdGenerator.resetCounter();
		UserIdGenerator.reset();
		NetworkIdGenerator.reset();
	}
	
	/**
	 * Computes a random position on a square grid of given size
	 * @return
	 * @author Ahmed Djermani
	 */
	public Position getRandomPosition() {
		
		double rangeMin = 0;
		double rangeMax = this.sideArea;
		Random r = new Random();
		double randomX = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		double randomY = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		
		Position position = new Position(randomX, randomY);
		return position;
	}
	
	
	// Statistics methods
	
	public void printStationStats(int stationId) {
		String message = this.getStations().get(stationId).displayStats();
		System.out.println(message);
		PrintConsole.updateScreenMessage(message);
	}
	
	public void printStationStats(int stationId, int minutes1, int minutes2) {
		Time t1 = new Time(minutes1);
		Time t2 = new Time(minutes2);
		String message = this.getStations().get(stationId).displayStats(t1, t2);
		System.out.println(message);
		PrintConsole.updateScreenMessage(message);
	}
	
	public void printUserStats(int userId) {
		String message = this.getUser(userId).displayStats();
		System.out.println(message);
		PrintConsole.updateScreenMessage(message);
	}
}
