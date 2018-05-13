package myVelib.bikeFactory;

import myVelib.bike.*;

/**
 * Automatically create an mechanical bike through its method
 * @author Ahmed Djermani
 *
 */
public class MechanicalFactory extends BikeFactory {

	@Override
	public MechanicalBike createBike() {
		MechanicalBike mechanical = new MechanicalBike();
		return mechanical;
	}

}
