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

public class ShortestPathRide extends RideFactory {

	public ShortestPathRide() {}
	
	/**
	 * The ride is created so as to minimize the riding distance.
	 * It computes all the possible paths and keeps the shortest one.
	 * @param start
	 * @param destination
	 * @param type
	 * @author Ahmed Djermani
	 * 
	 */


	public static Ride createRide(Position start, Position destination, typeBike type, Network network) {
		
		double min = Double.POSITIVE_INFINITY;
		int id1;
		int id2;
		int idStartStation = 0;
		int idEndStation = 0;
		double distance;
		
		for (Entry<Integer, Station> idStation : network.getStations().entrySet()) {
			
			id1 = idStation.getKey();
			Station station1 = idStation.getValue();
			
			for (Entry<Integer, Station> idStation2 : network.getStations().entrySet()) {
				
				id2 = idStation2.getKey();
				Station station2 = idStation2.getValue();
				
				if (id1 != id2) {
					distance = getTotalDistance(start, station1.getPosition(), station2.getPosition(), destination) ;
					
					if (distance < min) {
						min = distance;
						idStartStation = id1;
						idEndStation = id2;
					}
				}
			}
		}
		
		Ride ride  = new Ride(network.getStations().get(idStartStation), network.getStations().get(idEndStation));
		return ride;
		
		
		
		
	}
	
	public static double getTotalDistance(Position position1, Position position2, Position position3, Position position4) {
		
		double distance = position1.getDistance(position2) + position2.getDistance(position3) + position3.getDistance(position4);
		return distance;
	}

	
	public static Ride createRide(Station startStation, Bike bike) {
		// TODO Auto-generated method stub
		return null;
	}


}
