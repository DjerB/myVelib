package myVelib.bike;


import myVelib.system.Network;
import myVelib.utilities.*;

public class MechanicalBike extends Bike {
	
	public MechanicalBike() {
		super();
	}
	
	public typeBike getTypeBike() {
		return(typeBike.Mechanical);
	}

}
