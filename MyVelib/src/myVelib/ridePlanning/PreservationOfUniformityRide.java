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

public class PreservationOfUniformityRide extends RideFactory {
	
	public PreservationOfUniformityRide() {}
	/**
	 * The ride is created so as to preserve as good as possible the uniformity of the number of bikes in each station.
	 * The comments below explain how the computation is being done
	 * @param start
	 * @param destination
	 * @param type
	 * @author Ahmed Djermani
	 * 
	 */
	
	

	public static Ride createRide(Position start, Position destination, typeBike type, Network network) {
		
		double min = Double.POSITIVE_INFINITY;
		int nbBikes = 0;
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
					nbBikes = (type == typeBike.Electrical) ? station.getNbElectricalBikes() : station.getNbMechanicalBikes();
					idStartStation = id;
				}
			}
		}
		
	for (Entry<Integer, Station> idStation : network.getStations().entrySet()) {
			
			id = idStation.getKey();
			Station station = idStation.getValue();
			
			if ((type == typeBike.Electrical && station.getNbElectricalBikes() >= 1) || 
					(type == typeBike.Mechanical && station.getNbMechanicalBikes() >= 1)) {
				
				distance = start.getDistance(station.getPosition());
				
				/* if a station is no more than 105% of min distance away from the user and has more wanted type bikes, then it is selected.
				 * We arbitrary decided to pick up the station that has the largest number of available bikes.
				 */
				
				if ((distance <= 1.05 * min && type == typeBike.Electrical && station.getNbElectricalBikes() > nbBikes) ||       
				   (distance <= 1.05 * min && type == typeBike.Mechanical && station.getNbMechanicalBikes() > nbBikes)) {
					
					nbBikes = (type == typeBike.Electrical) ? station.getNbElectricalBikes() : station.getNbMechanicalBikes();
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
		int nbFreeSlots = 0;
		double minPlus = Double.POSITIVE_INFINITY;
		int idPlusStation = 0;
		
		for (Entry<Integer, Station> idStation : network.getStations().entrySet()) {
			
			id = idStation.getKey();
			Station station = idStation.getValue();
			
			if (station.getNbFreeSlots() >= 1) {   // DEPEND DE L'INTERFACE STATIONTYPE CREEE DANS LE PACKAGE STATION
				
				distance = destination.getDistance(station.getPosition()); // 
				
				if (distance < min) {
					min = distance;
					nbFreeSlots = station.getNbFreeSlots();
					idEndStation = id;
				}
			}
		}
		
		/* As for the start station, we look for stations whose distance are not more than 105% the distance between destination point and
		 * the closest station. We pick up the one that has the larger number of free slots.
		 */
		
		for (Entry<Integer, Station> idStation : network.getStations().entrySet()) {
			
			id = idStation.getKey();
			Station station = idStation.getValue();
			
			if (station.getNbFreeSlots() >= 1) {
				
				distance = destination.getDistance(station.getPosition());
				
				if (distance <= 1.05 * min && station.getNbFreeSlots() > nbFreeSlots) {
					
					idEndStation = id;
					nbFreeSlots = station.getNbFreeSlots();
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
