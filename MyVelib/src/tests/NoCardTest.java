package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import myVelib.bike.Bike;
import myVelib.bike.ElectricalBike;
import myVelib.bike.MechanicalBike;
import myVelib.userAndCard.NoCard;

public class NoCardTest {

	Bike elec;
	Bike mech;
	NoCard card;
	
	@Before
	public void init() {
		elec=new ElectricalBike();
		mech=new MechanicalBike();
		card=new NoCard();
	}
	
	
	@Test
	public void computePriceTest() {
		int duration=125;
		assertEquals(card.computePrice(duration,elec),6);	
		assertEquals(card.computePrice(duration,mech),3);			
	}
	@Test
	public void computePriceTestShort() {
		int duration=35;
		assertEquals(card.computePrice(duration,elec),2);
		assertEquals(card.computePrice(duration,mech),1);
	}

	@Test
	public void computePriceTestRound() { //test for a duration exactly 2 hours
		int duration=120;
		assertEquals(card.computePrice(duration,elec),4);
		assertEquals(card.computePrice(duration,mech),2);
	}
}