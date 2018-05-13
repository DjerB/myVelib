
package tests;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import myVelib.bike.ElectricalBike;
import myVelib.bike.MechanicalBike;
import myVelib.exceptions.FullStationException;
import myVelib.ridePlanning.AvoidPlusRide;
import myVelib.ride.*;
import myVelib.station.Station;
import myVelib.stationFactory.PlusStationFactory;
import myVelib.stationFactory.StandardStationFactory;
import myVelib.userAndCard.NoCard;
import myVelib.userAndCard.User;
import myVelib.utilities.Position;
import myVelib.utilities.typeBike;
import myVelib.system.*;

public class AvoidPlusRideTest {

	Station[] stations=new Station[5]; 
	User u;
	AvoidPlusRide rideMaker;
	Network network;
	
	@Before
	public void init() throws NullPointerException, FullStationException {
		
		network = new Network();
		u = new User("Patrick", new Position(750, 1750), new NoCard(), network);
	
		PlusStationFactory plusFactory=new PlusStationFactory();
		StandardStationFactory standardFactory=new StandardStationFactory();
		
		stations[0] = plusFactory.createStation(new Position(540, 3000));
		stations[1] = plusFactory.createStation(new Position(1000, 1000));
		stations[2] = standardFactory.createStation(new Position(500, 3000));			
		stations[3] = standardFactory.createStation(new Position(3500, 1500));
		stations[4] = standardFactory.createStation(new Position(4000, 4000));
		
		for (Station s : stations) {
			for(int k=0;k<5;k++) s.addSlot();
			try{s.initializeElecBike(new ElectricalBike());
			s.initializeMechaBike(new MechanicalBike());
			network.addStation(s);
			}
			catch(FullStationException e) {}
		}
	}
	
	
	@Test
	public void createRideTest() {
		Position destination = new Position(1000, 3000);
		Ride ride = AvoidPlusRide.createRide(u.getPosition(), destination, typeBike.Electrical, u.getNetwork());
		
		assertEquals(ride.getStartStation().getId(),stations[1].getId());
		assertEquals(ride.getEndStation().getId(),stations[2].getId());
		
	}

}
