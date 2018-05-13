package tests;



import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import myVelib.system.*;
import myVelib.bikeFactory.*;
import myVelib.stationFactory.*;
import myVelib.station.*;
import myVelib.bike.*;
import myVelib.userAndCard.*;
import myVelib.utilities.*;
import myVelib.cardFactory.*;
import myVelib.exceptions.*;

public class StationTest {
	
	Network network;
	Station standard;
	Station plus;
	User user;
	Bike bike;
	
	@Before
	public void init() throws NullPointerException, FullStationException {
		
		network = new Network();
		Time.updateTime(0);
		Position plusPosition = new Position(1000, 250);
		Position standardPosition = new Position(2300, 2700);
		Position userPosition = new Position(2300, 2700);
		
		PlusStationFactory plusFactory = new PlusStationFactory();
		StandardStationFactory standardFactory = new StandardStationFactory();
		VmaxFactory vmaxFactory = new VmaxFactory();
		MechanicalFactory mechaFactory = new MechanicalFactory();

		
		
		StationIdGenerator.resetCounter();
		
		plus = plusFactory.createStation(plusPosition);
		standard = standardFactory.createStation(standardPosition);
		network.addStation(plus);
		network.addStation(standard);
		plus.addSlot();
		plus.addSlot();
		standard.addSlot();

		bike = mechaFactory.createBike();
		
		try{plus.initializeBike(bike);} catch(Exception e) {}
		
		Vmax card = vmaxFactory.createCard();
		user = new User("Jules", userPosition, card, network);

	}

	@Test
	public synchronized void hasTheRightStationId() {
		org.junit.Assert.assertEquals(plus.getId(), 1);
		org.junit.Assert.assertEquals(standard.getId(), 2);
	}
	
	@Test
	public void ReleaseBikeWhenBikeAvailable() throws MaxBikeException, FullStationException {

		
		org.junit.Assert.assertEquals(plus.getNbFreeSlots(), 1);
		org.junit.Assert.assertEquals(plus.getNbMechanicalBikes(), 1);
		
		user.rentBike(plus, typeBike.Mechanical);
		
		org.junit.Assert.assertEquals(plus.getNbMechanicalBikes(), 0);
		
	}
	
	@Test
	public void ReleaseBikeWhenNoBikeAvailable() throws MaxBikeException, FullStationException {
		
		user.rentBike(plus, typeBike.Electrical);
		assertNull(user.getRide());
		
	}
	
	@Test
	public void computeTheRightNumberOfRentsBetweenTwoTimes() {	
		
		Time t1 = new Time();
	try {
			user.rentBike(plus, typeBike.Mechanical);
			Time.updateTime(1);
			standard.getSlots().get(0).receiveBike(bike, network);
			Time.updateTime(2);
			user.rentBike(standard, typeBike.Mechanical);
			Time.updateTime(3);
			plus.getSlots().get(0).receiveBike(bike, network);
			Time.updateTime(4);
			user.rentBike(plus, typeBike.Mechanical);
		}
	catch(Exception e) {System.out.println(e);}
		
		Time t2 = new Time();
		
		try {
			org.junit.Assert.assertEquals(plus.getTerminal().nbRents(t1, t2), 2);
			org.junit.Assert.assertEquals(standard.getTerminal().nbRents(t1, t2), 1);
		}
		catch(MissingDataException e) {System.out.println(e);}
	}
	
	@Test
	public void computeTheRightNumberOfReturnsBetweenTwoTimes() throws MaxBikeException, InterruptedException, OccupiedException, OfflineException, MissingDataException, FullStationException {
		
		
		Time t1 = new Time();
		
		user.rentBike(plus, typeBike.Mechanical);
		System.out.println(user.getRide().getBike());
		Time.updateTime(1);
		standard.getSlots().get(0).receiveBike(bike, network);
		Time.updateTime(2);
		user.rentBike(standard, typeBike.Mechanical);
		Time.updateTime(3); 
		plus.getSlots().get(0).receiveBike(bike, network);
		Time.updateTime(4);
		user.rentBike(plus, typeBike.Mechanical);
		
		Time t2 = new Time();
		
		org.junit.Assert.assertEquals(plus.getTerminal().nbReturns(t1, t2), 1);
		org.junit.Assert.assertEquals(standard.getTerminal().nbReturns(t1, t2), 1);

	}
	
	@Test(expected = OccupiedException.class)
	public void raiseAnErrorWhenTheSlotCannotReceiveABike() throws MaxBikeException, InterruptedException, OccupiedException, OfflineException, FullStationException {
	
		

		standard.getSlots().get(0).setState(SlotState.occupied);		
		user.rentBike(plus, typeBike.Electrical);
		standard.getSlots().get(0).receiveBike(bike, network);
		
	}

	
	@Test
	public void giveTheRightNbOfBikes() throws MaxBikeException, InterruptedException, OccupiedException, OfflineException, FullStationException {
		
		MechanicalFactory mechaFactory=new MechanicalFactory();
		ElectricalFactory elecFactory=new ElectricalFactory();
		MechanicalBike bike1 = mechaFactory.createBike();
		MechanicalBike bike2 = mechaFactory.createBike();
		ElectricalBike bike3=elecFactory.createBike();
		
		plus.addSlot();
		plus.addSlot();
		standard.addSlot();
		
		plus.initializeMechaBike(bike1);
		plus.initializeMechaBike(bike2);
		standard.initializeBike(bike3);

		org.junit.Assert.assertEquals(plus.getNbMechanicalBikes(), 3);
		org.junit.Assert.assertEquals(standard.getNbElectricalBikes(), 1);

		synchronized(this) {
			user.rentBike(plus, typeBike.Mechanical);
			Time.updateTime(1);
			standard.getFreeSlot().receiveBike(user.getRide().getBike(), network);
			org.junit.Assert.assertEquals(standard.getNbMechanicalBikes(), 1);
			org.junit.Assert.assertEquals(plus.getNbMechanicalBikes(), 2);
			Time.updateTime(2);
			user.rentBike(standard, typeBike.Electrical);
			Time.updateTime(3); 
			org.junit.Assert.assertEquals(standard.getNbElectricalBikes(), 0);
		}
		
		
		
		
		
	}
	
	@Test
	public void getOfflineWhenSlotsAreOffLine() throws FullStationException {
		
		Position plusPosition = new Position(300, 4000);
		PlusStationFactory plusFactory = new PlusStationFactory();
		Station plus = plusFactory.createStation(plusPosition);
		
		org.junit.Assert.assertEquals(plus.isOnline(), true);

		plus.addSlot();
		plus.addSlot();
		plus.getFreeSlot().setState(SlotState.offline);
		plus.getFreeSlot().setState(SlotState.offline);

		
		org.junit.Assert.assertEquals(plus.isOnline(), false);
		
	}
	
	@Test
	public void getOfflineWhenTerminalIsOffline() {
		
		Position plusPosition = new Position(100, 3600);
		PlusStationFactory plusFactory = new PlusStationFactory();
		Station plus = plusFactory.createStation(plusPosition);
		
		org.junit.Assert.assertEquals(plus.isOnline(), true);
		
		plus.getTerminal().setOnline(false);
		
		org.junit.Assert.assertEquals(plus.isOnline(), false);

		
	}
	
	
	
}