package tests;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import myVelib.bike.ElectricalBike;
import myVelib.bike.MechanicalBike;
import myVelib.exceptions.FullStationException;
import myVelib.ride.Ride;
import myVelib.ridePlanning.PreservationOfUniformityRide;
import myVelib.station.Station;
import myVelib.stationFactory.PlusStationFactory;
import myVelib.stationFactory.StandardStationFactory;
import myVelib.system.Network;
import myVelib.userAndCard.NoCard;
import myVelib.userAndCard.User;
import myVelib.utilities.Position;
import myVelib.utilities.typeBike;

public class PreservationOfUniformityRideTest {

	Station[] stations=new Station[5];
	User u;
	PreservationOfUniformityRide rideMaker;
	Network network;

	@Before
	public void init() throws NullPointerException, FullStationException {
		
		network = new Network();
		u=new User("Patrick",new Position(520, 2000),new NoCard(), network);
	
		PlusStationFactory plusFactory=new PlusStationFactory();
		StandardStationFactory standardFactory=new StandardStationFactory();
		
		stations[0] = plusFactory.createStation(new Position(500, 3000));
		stations[1] = plusFactory.createStation(new Position(1000, 1000));
		stations[2] = standardFactory.createStation(new Position(520, 3000));			
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
		try{stations[0].initializeElecBike(new ElectricalBike());}
		catch(FullStationException e) {}
	}
	
	
	
	@Test
	public void createRideTest() {
		Position destination=new Position(4000, 3000);
		Ride ride = PreservationOfUniformityRide.createRide(u.getPosition(),destination,typeBike.Electrical, u.getNetwork());
		
		assertEquals(ride.getStartStation().getId(),stations[0].getId()); //stations[0] has more bikes of wanted kind than stations[2], which is closer
		assertEquals(ride.getEndStation().getId(),stations[4].getId());
		
	}

}
