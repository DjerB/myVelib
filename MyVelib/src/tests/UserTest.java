package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import myVelib.bike.Bike;
import myVelib.bike.ElectricalBike;
import myVelib.bike.MechanicalBike;
import myVelib.exceptions.FullStationException;
import myVelib.exceptions.MaxBikeException;
import myVelib.exceptions.MaxRideException;
import myVelib.exceptions.NoDestinationAssignedException;
import myVelib.exceptions.NotAnAnswerException;
import myVelib.exceptions.OccupiedException;
import myVelib.exceptions.OfflineException;
import myVelib.station.PlusStation;
import myVelib.station.Station;
import myVelib.station.StationIdGenerator;
import myVelib.stationFactory.PlusStationFactory;
import myVelib.stationFactory.StandardStationFactory;
import myVelib.system.Network;
import myVelib.userAndCard.NoCard;
import myVelib.userAndCard.User;
import myVelib.utilities.Position;
import myVelib.utilities.typeBike;



public class UserTest {
	
	Station[] stations = new Station[5];
	User user;
	Bike elec;
	
	Network network;
	
	@Before
	public void init() throws NullPointerException, FullStationException {
		
		network = new Network();
		
		user = new User("Patrick",new Position(520, 2000),new NoCard(), network);
		
		StationIdGenerator.resetCounter();
		PlusStationFactory plusFactory = new PlusStationFactory();
		StandardStationFactory standardFactory = new StandardStationFactory();
		
		stations[0] = plusFactory.createStation(new Position(540, 3000));
		stations[1] = plusFactory.createStation(new Position(800, 1000));
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
	public void rentBikeTest() throws MaxBikeException, OccupiedException, OfflineException, FullStationException {
		user.rentBike(stations[0], typeBike.Electrical);
		assertTrue(user.getRide().getBike() instanceof ElectricalBike);
		stations[0].getFreeSlot().receiveBike(user.getRide().getBike(), network);
	}
	
	@Test public void returnBikeTest() throws MaxBikeException {
		
		int timeCredit=30;
		user.rentBike(stations[0], typeBike.Electrical);
		user.returnBike(timeCredit, stations[0]);
		assertNull(user.getRide());
	}
	
	@Test
	public void updateRideTest() {
		Position destination = new Position(3800, 100);
		try{
			user.planShortestPath(destination, typeBike.Electrical); 
			System.out.println(user.getRide().getEndStation().getId());
		} //destination should be stations[3]
		catch(MaxRideException e) {}
		stations[3].setOnline(false);
		
		System.out.println(user.getRide().getEndStation().getId());
		assertEquals(user.getRide().getEndStation().getId(),stations[4].getId());
	}
	
	

}
