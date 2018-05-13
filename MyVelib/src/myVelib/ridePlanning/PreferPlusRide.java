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

public class PreferPlusRide extends RideFactory {
	
	public PreferPlusRide() {}
	
	/**
	 * Creates a ride meeting with the classical requirements of proximity with the user and the destination,
	 * prefering a plus station as the end station.
	 * @param start
	 * @param destination
	 * @param type
	 * @author Ahmed Djermani
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
		
		
		/*
		 * We compute distances for Standard Stations on the first hand and distances of Plus Stations
		 * on the other hand.
		 * When ending the loop, we have an id for the closest Standard Station and an id for the closest 
		 * Plus Station : if the distance for the Plus is not greater than 110% the minimum for the Standard
		 * one, then the Plus Station is picked. Else, we keep the Standard Station.
		 */
		
		min = Double.POSITIVE_INFINITY;
		double minPlus = Double.POSITIVE_INFINITY;
		int idPlusStation = 0;
		for (Entry<Integer, Station> idStation : network.getStations().entrySet()) {
			
			id = idStation.getKey();
			Station station = idStation.getValue();
			
			if (station.getNbFreeSlots() >= 1) {   // DEPEND DE L'INTERFACE STATIONTYPE CREEE DANS LE PACKAGE STATION
				
				distance = destination.getDistance(station.getPosition()); // 
				
				if (distance <= min * 1.1 && station.getStationType() instanceof PlusStation) {  // the plus station is selected if its distance to the destination is not greater than 110% the current minimal distance
					minPlus = distance;
					idPlusStation = id;
				}
				else if (distance <= min) {
					min = distance;
					idEndStation = id;
				}
			}
		}
		
		if (minPlus <= min * 1.1) {
			idEndStation = idPlusStation;
		}
		
		Ride ride  = new Ride(network.getStations().get(idStartStation), network.getStations().get(idEndStation));
		return ride;
	}


	public static Ride createRide(Station startStation, Bike bike) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
