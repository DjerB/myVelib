package myVelib.ride;

import java.util.Map.Entry;

import myVelib.bike.*;
import myVelib.station.*;
import myVelib.userAndCard.*;
import myVelib.utilities.*;
import myVelib.system.*;

/**
 * Ride class contains the informations of the ride : destination, departure, id, price, startime and bike.
 * Depending on the policy we choose, elements might not be instanciated at first. 
 * Constructor is called only by Ride factories.
 * @author Ahmed Djermani
 *
 */
public class Ride {
	
	private int id;
	private Station startStation;
	private Station endStation;
	private int price = 0; 
	/**
	 * enables to compute the ride duration when finishing the ride
	 */
	private Time startTime = new Time();
	private Bike bike;
	
	public Ride(Station startStation, Bike bike) {                    // If no policy has been declared --> Random Ride maker
		
		this.id = RideIdGenerator.getNextRideId();
		this.startStation = startStation;
		this.endStation = null;
		this.bike = bike;
	}
	
	public Ride(Station startStation, Station endStation) {
		
		this.id = RideIdGenerator.getNextRideId();
		this.endStation = endStation;
		this.startStation = startStation;
		this.bike = null;
		this.startTime = null;
	}
	
	// GETTERS
	
	public int getId() {
		return this.id;
	}
	
	public int getPrice() {
		return this.price;
	}
	
	public Bike getBike() {
		return this.bike;
	}
	
	public Station getStartStation() {
		return this.startStation;
	}
	
	public Station getEndStation() {
		return this.endStation;
	}
	
	// SETTERS
	
	public void setEndStation(Station station) {
		this.endStation = station;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}
	
	/**
	 * This method retrieves the duration of the ride from the Time class.
	 * @return
	 * @author Ahmed Djermani
	 */
	public int getDuration() {
		return  this.startTime.computeRidingTime(new Time());
	}
	
	public String toString() {
		String message = " ride from station " + this.startStation.getId() + ((this.endStation != null) ? (" to  station " +
				this.endStation.getId() + ".") : "") + " at " + ((this.startTime != null) ? 
						this.startTime.getCreationTime() + " minutes." : "time non defined yet (not reached the start station yet).");
		return message;
	}
	
	
	/**
	 * Method called to update a planned ride when destination station gets offline 
	 */
	public void updateDestinationStation(Network network) {
		
		double min = Double.POSITIVE_INFINITY;
		double distance;
		int idEndStation = 0;
		
		for (Entry<Integer, Station> idStation : network.getStations().entrySet()) {
			
			id = idStation.getKey();
			Station station = idStation.getValue();
			
			/**
			 * We're looking for the closest free station to the station that got offline.
			 * Obviously, we have not to consider the station itself in the algorithm.
			 */
			
			if (station.getNbFreeSlots() >= 1 && station.isOnline() == true) {   
				
				distance = this.endStation.getPosition().getDistance(station.getPosition());
				
				if (distance < min) {
					idEndStation = id;
					min = distance;
				}
			}
		}
		
		this.endStation = network.getStations().get(idEndStation);
	}
	
	// Method called for a planned ride when the user gets to the station and rents the desired bike
	
	public void rentBike(Bike bike) {
		if (this.bike == null) {
			this.bike = bike;
			this.startTime = new Time();
		}
		else {
			String message = "The user has already taken a bike";
			System.out.println(message);
			PrintConsole.updateScreenMessage(message);
		}
	}

}
