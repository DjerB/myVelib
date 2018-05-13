package myVelib.ridePlanning;

import java.util.Map.Entry;


import myVelib.bike.*;
import myVelib.ride.*;
import myVelib.station.*;
import myVelib.utilities.*;
import myVelib.system.*;

/**
 * This class overrides createRide method and returns a initialized ride of the desired policy planning
 * @author Ahmed Djermani
 *
 */

public class AvoidPlusRide extends RideFactory {
	
	
	/**
	 * The ride is created so as to minimize the riding distance and to avoid plus station as the destination station
	 * @param start
	 * @param destination
	 * @param type
	 * @param network
	 * @author Ahmed Djermani
	 * 
	 */
	
	
	public static Ride createRide(Position start, Position destination, typeBike type, Network network) {
		
		double min = Double.POSITIVE_INFINITY;
		int idStartStation = 0;
		int idEndStation = 0;
		int id;
		double distance;
		
		for (Entry<Integer, Station> idStation : network.getStations().entrySet()) {
			
			id = idStation.getKey();
			Station station = idStation.getValue();
			
			if ((type == typeBike.Electrical && station.getNbElectricalBikes() >= 1) || 
					(type == typeBike.Mechanical && station.getNbMechanicalBikes() >= 1)) {
				
				distance = start.getDistance(station.getPosition());
				
				if (distance < min) {
					min = distance;
					idStartStation = id;
				}
			}
		}
		

		min = Double.POSITIVE_INFINITY;
		for (Entry<Integer, Station> idStation : network.getStations().entrySet()) {
			
			id = idStation.getKey();
			Station station = idStation.getValue();
			
			if (station.getNbFreeSlots() >= 1 && !(station.getStationType() instanceof PlusStation)) {   
				
				distance = destination.getDistance(station.getPosition());
				
				if (distance < min) {
					min = distance;
					idEndStation = id;
				}
			}
		}
		
		Ride ride  = new Ride(network.getStations().get(idStartStation), network.getStations().get(idEndStation));
		return ride;
	}

	
	public static Ride createRide(Station startStation, Bike bike) {
		// TODO Auto-generated method stub
		return null;
	}

}
