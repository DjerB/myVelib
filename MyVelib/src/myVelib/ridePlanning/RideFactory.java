package myVelib.ridePlanning;

import myVelib.ride.*;
import myVelib.station.*;
import myVelib.utilities.*;
import myVelib.system.*;
import myVelib.bike.*;

/**
 * This class is the root of the abstract ride factory that enables to creates the appropriate ride relative 
 * to each policy.
 * Each ride factory inherits from this class.
 * @author Ahmed Djermani
 *
 */
public abstract class RideFactory {
	
	
	
	public static Ride createRide(Position start, Position destination, typeBike type, Network network) { return null; }
	public static Ride createRide(Station startStation, Bike bike) { return null; }
	

}
