package myVelib.userAndCard;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Scanner;

import myVelib.system.*;
import myVelib.station.*;
import myVelib.utilities.*;
import myVelib.exceptions.*;
import myVelib.ride.*;
import myVelib.ridePlanning.*;
import myVelib.bike.*;

/**
 * The class User is a central point of the program : it contains the id, the name, the total amount charged,
 * the total riding time, the current ride (if there is one), the card, the current position.
 * @author Ahmed Djermani
 *
 */


public class User extends Thread {
	
	private Position position;
	private String name;
	private int id;
	private Card card;
	private Network network;
	private int nbRides;
	private int totalAmountCharged;
	private int ridingTime;  // riding time in minutes
	private int totalTimeCredit;
	private Ride ride;
	private boolean isRiding; 
	
	public User(String name, Position position, Card card, Network network) {
		
		this.name = name;
		this.position = position;
		this.card = card;
		this.id = UserIdGenerator.getNextUserId();
		this.nbRides = 0;
		this.totalAmountCharged = 0;
		this.ridingTime = 0;
		this.totalTimeCredit = 0;
		this.ride = null;	 
		this.network = network;
		this.network.addUser(this);
		this.isRiding = false;
		PrintConsole.updateScreenMessage("User " + this.name + " created");
	}
	
	public User(String name, Card card, Network network) {
		this.name = name;
		this.card = card;
		this.id = UserIdGenerator.getNextUserId();
		this.nbRides = 0;
		this.totalAmountCharged = 0;
		this.ridingTime = 0;
		this.totalTimeCredit = 0;
		this.ride = null;
		this.network = network;
		this.network.addUser(this);
		this.isRiding = false;
		PrintConsole.updateScreenMessage("User " + this.name + " created");
		
	}
	
	// GETTERS
	
	public Position getPosition() { return this.position; }
	public String getUserName() { return this.name; } 
	public int getUserId() { return this.id; }
	public int getNbRides() { return this.nbRides; }
	public int getTotalAmountCharged() { return this.totalAmountCharged; }
	public int getTotalTimeCredit() { return this.totalTimeCredit; }
	public Ride getRide() { return this.ride; }
	public Network getNetwork() { return this.network; }
	
	public String toString() {
		return "User " + this.id + " (" + this.name + ")" + " (Last Location : " + this.position.toString() + ")"+ ((this.isRiding)? 
				" is riding on bike " + this.ride.getBike().getId() + ".\n" : " is not riding. \n");
	}
	
	public String displayStats() {
		String stats = 
				"User " + this.id + " (" + this.name + ") : \n" 
				+ "Number of rides : " + this.nbRides
				+ " | Total time spent on time : " + this.ridingTime
				+ " | Total amount of charges : " + this.totalAmountCharged
				+ " | TimeCredit earned : " + this.totalTimeCredit + "\n";
		return stats;
	}
	
	// SETTERS
	
	public void setPosition(Position position) {
		this.position = position;
	}
	
	/**
	 * Method rentBike is called when the user reaches a station and rents a bike
	 * @param station
	 * @param type
	 */
	
	public void rentBike(Station station, typeBike type) throws MaxBikeException {
		
		/** 
		* the station passed as an argument tries to release the right bike to the user. If not (there is no bike of the desired type,
		* an exception is raised.
		*/
		this.position = station.getPosition();
		try {
			
			Bike bike = station.releaseBike(type);
			if (this.ride == null) {
				this.planRandomRide(station, bike);
				this.isRiding = true;
				bike.setUser(this);
				}
			else if (this.ride.getBike() == null) {
				this.ride.rentBike(bike);
				this.isRiding = true;
				bike.setUser(this);
			}else {
				throw new MaxBikeException();
			}
		} catch(NotAvailableBikeException e) {
		} catch(MaxBikeException e) {
			e.printErrorMessage();
		}
		
		
	}
	
	public void increaseNbRides() {
		this.nbRides++;
	}

	/**
	 * method called by a station when the user's bike is received by one of its slots.
	 * Station sends the given time credit to the user class that computes the final price, given the duration provided
	 * by the ride class and the type of bike.
	 * @param timeCredit depends on the type of station the user is dealing with
	 * @author Ahmed Djermani
	 */
	public void returnBike(int timeCredit, Station station) {
		
		int duration = this.ride.getDuration();
		int price;
		
		// We use a synchronized block to be sure that timeCredit is added before computing the final cost
		 
		synchronized(this) {
			this.card.addTimeCredit(timeCredit);
			price = this.card.computePrice(duration, this.ride.getBike());
			System.out.println(this.name + "(Id " + this.id + ") returns bike : " + price + " euros"
					+ " charged, " + duration + " minutes.");
			PrintConsole.updateScreenMessage(this.name + "(Id " + this.id + ") returns bike : " + price + " euros"
					+ " charged, " + duration + " minutes.");
		}

		this.totalAmountCharged += price;
		this.ridingTime += duration;
		this.ride.setPrice(price);
		this.ride.setEndStation(station);
		this.setPosition(station.getPosition());
		this.ride = null;
		this.isRiding = false;
				
	}
	
	// Each planning Ride policy is contained in a different method
	
	public void planToString(String typeRide) {
		if (ride != null) {
			String message = this.name + " plans "+ typeRide + this.ride.toString();
			System.out.println(message);
			PrintConsole.updateScreenMessage(message);
		}
	}
	
	public void planShortestPath(Position destination, typeBike type) throws MaxRideException {
		
		try {
			if (this.ride != null) {
				throw new MaxRideException();
			}
			else {
				this.ride = ShortestPathRide.createRide(this.position, destination, type, this.network);
				this.network.addRide(this.ride);
				this.ride.getEndStation().attach(this);
				this.planToString("shortest path");
				this.increaseNbRides();
			}
		} catch (MaxRideException e) {
			e.printErrorMessage();
		}
		
	
	}
	
	public void planFastestPath(Position destination, typeBike type) throws MaxRideException {
		
		try {
			if (this.ride != null) {
				throw new MaxRideException();
			}
			else {
				this.ride = FastestPathRide.createRide(this.position, destination, type, this.network);
				this.network.addRide(this.ride);
				this.ride.getEndStation().attach(this);
				this.planToString("fastest path");
				this.increaseNbRides();

			}
		} catch (MaxRideException e) {
			e.printErrorMessage();
		}
		
	}

	public void planPreferPlusStation(Position destination, typeBike type) throws MaxRideException {
		
		try {
			if (this.ride != null) {
				throw new MaxRideException();
			}
			else {
				this.ride = PreferPlusRide.createRide(this.position, destination, type, this.network);
				this.network.addRide(this.ride);
				this.ride.getEndStation().attach(this);
				this.planToString("prefer plus station");
				this.increaseNbRides();

			}
		} catch (MaxRideException e) {
			e.printErrorMessage();
		}
		
	}
	
	public void planAvoidPlusStation(Position destination, typeBike type) throws MaxRideException {
		
		try {
			if (this.ride != null) {
				throw new MaxRideException();
			}
			else {
				this.ride = AvoidPlusRide.createRide(this.position, destination, type, this.network);
				this.network.addRide(this.ride);
				this.ride.getEndStation().attach(this);
				this.planToString("avoid plus station");
				this.increaseNbRides();

			}
		} catch (MaxRideException e) {
			e.printErrorMessage();
		}

	}
	
	public void planPreservationOfUniformity(Position destination, typeBike type) throws MaxRideException {
		
		try {
			if (this.ride != null) {
				throw new MaxRideException();
			}
			else {
				this.ride = PreservationOfUniformityRide.createRide(this.position, destination, type, this.network);
				this.network.addRide(this.ride);
				this.ride.getEndStation().attach(this);
				this.planToString("preservation of uniformity");
				this.increaseNbRides();

			}
		} catch (MaxRideException e) {
			e.printErrorMessage();
		}
		
	}
	
	// planRandomRide doesn't throw exception because the test (ride == null) is done before calling it in the rentBike method
	
	public void planRandomRide(Station station, Bike bike) {
		
		this.ride = RandomRide.createRide(station, bike);
		this.network.addRide(this.ride);
		this.planToString("random");
		this.increaseNbRides();
		
	}
	
	
	 
	/**
	 * Method called when the destination station gets offline : the observer can either change the destination or do nothing
	 * It checks that the answer has the correct type and compute the new end station if needed.
	 * @throws NotAnAnswerException
	 * @throws NoDestinationAssignedException
	 * @author Ahmed Djermani
	 */
	public void updateRide() throws NotAnAnswerException, NoDestinationAssignedException {
		
		/*Scanner sc = new Scanner(System.in);
		char answer;
		System.out.println("Your destination station got offline. Do you want to choose the closest available station ? (Y/N)");
		
		// While the user doesn't enter a right answer : Y or N, a exception is raised and he is asked to retry
		
		boolean loop = true;
		
		while (loop) {
			
			try {
				
				answer = sc.nextLine().charAt(0);

				if(answer != 'Y' && answer != 'N') {
					throw new NotAnAnswerException();
				}
				else if (answer == 'Y') {
					this.ride.updateDestinationStation();
					System.out.println("Destination has been recomputed");
					loop = false;
				}
				else {
					loop = false;
					throw new NoDestinationAssignedException();
					
				}
				
			} catch (NotAnAnswerException e) {
				e.printErrorMessage();
			} catch (NoDestinationAssignedException e) {
				e.printErrorMessage();
			}
		}
		
		sc.close();*/
		
		String offlineMessage = "Your destination station got offline.";
		System.out.println(offlineMessage);
		PrintConsole.updateScreenMessage(offlineMessage);
		this.ride.updateDestinationStation(this.network);
		String recomputedMessage = "Destination has been recomputed";
		System.out.println(recomputedMessage);
		PrintConsole.updateScreenMessage(recomputedMessage);
		
	}
	
	/**
	 * start method creates random test scenarios to test the entire system.
	 * The user makes it choice randomly and plans, rents a bike and returns bike.
	 * 
	 */

	@Override
	public void start() {
		
		int random = getZeroOrOne();
		int nbStations = this.network.getStations().size();
		int stationId = (int) (Math.random() * nbStations) + 1;
		
		if (random == 0) {    // The user plans a ride through the application
			
			Position destination = getRandomPosition();
			
			int randomPolicy = (int) (Math.random() * 4);
			int r = getZeroOrOne();
			typeBike randomType = (r == 0)? typeBike.Electrical : typeBike.Mechanical;
			
			switch(randomPolicy) {
				
			case 0 : 
				try {
					this.planAvoidPlusStation(destination, randomType);
				} catch (MaxRideException e) {
					e.printErrorMessage();
				}
				break;
			
			case 1 : 
				try {
					this.planPreferPlusStation(destination, randomType);
				} catch (MaxRideException e) {
					e.printErrorMessage();
				}
				break;
				
			case 2 : 
				try {
					this.planFastestPath(destination, randomType);
				} catch (MaxRideException e) {
					e.printErrorMessage();
				}
				
				break;
			
			case 3 : 
				try {
					this.planShortestPath(destination, randomType);
				} catch (MaxRideException e) {
					e.printErrorMessage();
				}
				
				break;
			
			case 4 : 
				try {
					this.planPreservationOfUniformity(destination, randomType);
				} catch (MaxRideException e) {
					e.printErrorMessage();
				}
				
				break;
			
			default : break;
			}
			
			Time.updateTime(15);
			
			// The user rents the bike at the station
			
			try {
				this.rentBike(this.ride.getStartStation(), randomType);
			} catch (MaxBikeException e1) {
				e1.printErrorMessage();
			}
			
			Time.updateTime(20);
			// Then retrieves it at a random station
			
			stationId = (int) (Math.random() * nbStations) + 1;
			try {
				this.network.getStations().get(stationId).getFreeSlot().receiveBike(this.ride.getBike(), this.network);
			} catch (OccupiedException | OfflineException | FullStationException e) {
			}
			
			

			
		}
		
		else {    // The user tries to rent a bike at a random station
			
			
			if (stationId > nbStations) { stationId--; }
			
			int r = getZeroOrOne();
			typeBike randomType = (r == 0)? typeBike.Electrical : typeBike.Mechanical;
			try {
				this.rentBike(this.network.getStations().get(stationId), randomType);
			} catch (MaxBikeException e) {
				e.printErrorMessage();
			}
			
			// Then retrieves it at a random station
			
			stationId = (int) (Math.random() * nbStations) + 1;
			try {
				this.network.getStations().get(stationId).getFreeSlot().receiveBike(this.ride.getBike(), this.network);
			} catch (OccupiedException | OfflineException | FullStationException e) {
			}
			
		}
		
	}
	
	private int getZeroOrOne() {
		double random = Math.random();
		return (random > 0.5)? 0 : 1;
	}
	
	private Position getRandomPosition() {
		
		int rangeMax = 4000;
		int rangeMin = 0;
		Random r = new Random();
		double randomX = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		double randomY = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		
		Position position = new Position(randomX, randomY);
		return position;
	}
	
	
	
}
