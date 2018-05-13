package myVelib.bikeFactory;

import myVelib.bike.*;

/**
 * Automatically create an electrical bike through its method
 * @author Ahmed Djermani
 *
 */
public class ElectricalFactory extends BikeFactory {

	@Override
	public ElectricalBike createBike() {
		ElectricalBike elec = new ElectricalBike();
		return elec;
	}

}
