package myVelib.ridePlanning;


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

public class RandomRide extends RideFactory {
	
	public RandomRide() {}

	/**
	 * The following method is used only by the Random class : method createRide has been overloaded
	 * to fit with the requirements of an unplanned ride. It is the case when the user simply gets to a station and 
	 * rent a bike : we do not know the destination he wants to reach; thus only the start station and the type of 
	 * bike are known, we simply create a basic ride through the appropriate constructor.
	 * @param startStation
	 * @param bike
	 * @return
	 */

	
	public static Ride createRide(Station startStation, Bike bike) {
		
		Ride ride = new Ride(startStation, bike);
		return ride;
	
	}
	
	
	// The following method is not used in the case of an unplanned riding, giving the absence of a predefined destination
	
	public static Ride createRide(Position start, Position destination, typeBike type) {
		
	
		return null;
	}

}
