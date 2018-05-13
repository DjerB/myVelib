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

public class FastestPathRide extends RideFactory {
	
	public FastestPathRide() {}
	
	/**
	 * The ride is created so as to minimize the riding duration.
	 * It computes all the possible paths and keep the fastest one.
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
		double time;
		int walkingSpeed =  4;      		// Expressed in kph
		int cyclingSpeed = (type == typeBike.Electrical)? 20 : 15;
		
		/*
		// Generating each path between user and start stations and keeping the id of the closest station in terms of time
		
		for (Entry<Integer, Station> idStation : MyVelib.getStations().entrySet()) {
			
			id = idStation.getKey();
			Station station = idStation.getValue();
			
			if ((type == typeBike.Electrical && station.getNbElectricalBikes() >= 1) || 
			   (type == typeBike.Mechanical && station.getNbMechanicalBikes() >= 1)) {
				
				time = start.getDistance(station.getPosition()) / walkingSpeed;
				
				if (time < min) {
					min = time;
					idStartStation = id;
				}
			}
			
		}
		
		// Generating each path between destination and end station and keeping the id of the closest station in terms of time
		
		min = Double.POSITIVE_INFINITY;
		for (Entry<Integer, Station> idStation : MyVelib.getStations().entrySet()) {
			
			id = idStation.getKey();
			Station station = idStation.getValue();
			if (station.getNbFreeSlots() >= 1) {
				time  = (MyVelib.getStations().get(idStartStation).getPosition().getDistance(station.getPosition()) / cyclingSpeed)
						+ (station.getPosition().getDistance(destination) / walkingSpeed);
				
				if (time < min) {
					min = time;
					idEndStation = id;
				}
			}
			
			
		}*/
		
		
		min = Double.POSITIVE_INFINITY;
		for (Entry<Integer, Station> idStation : network.getStations().entrySet()) {
			
			id1 = idStation.getKey();
			Station station1 = idStation.getValue();
			
			for (Entry<Integer, Station> idStation2 : network.getStations().entrySet()) {
				
				id2 = idStation2.getKey();
				Station station2 = idStation2.getValue();
				
				if (id1 != id2) {
					
					time = getTotalTime(start, station1.getPosition(), station2.getPosition(), destination, walkingSpeed, cyclingSpeed);
					
					if (time < min) {
						min = time;
						idStartStation = id1;
						idEndStation = id2;
					}
				}

			}
		}
		
		Ride ride  = new Ride(network.getStations().get(idStartStation), network.getStations().get(idEndStation));
		return ride;
	}

	public static double getTotalTime(Position position1, Position position2, Position position3, Position position4, int walking, int riding) {
		
		double time = position1.getDistance(position2) / walking
					+ position2.getDistance(position3) / riding
					+ position3.getDistance(position4) / walking;
		return time;
	}
	
	public static Ride createRide(Station startStation, Bike bike) {
		// TODO Auto-generated method stub
		return null;
	}

}
