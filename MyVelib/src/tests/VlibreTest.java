package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import myVelib.bike.Bike;
import myVelib.bike.ElectricalBike;
import myVelib.bike.MechanicalBike;
import myVelib.userAndCard.Vlibre;

public class VlibreTest {

	Bike elec;
	Bike mech;
	Vlibre v;
	
	@Before
	public void init() {
		elec=new ElectricalBike();
		mech=new MechanicalBike();
		v=new Vlibre();
	}
	
	
	@Test
	public void computePriceTest() {
		int duration=125;
		assertEquals(v.computePrice(duration,elec),5);
		assertEquals(v.computePrice(duration,mech),2);

		
	}
	@Test
	public void computePriceTestShort() {
		int duration=35;
		assertEquals(v.computePrice(duration,elec),1);
		assertEquals(v.computePrice(duration,mech),0);
	}

	@Test
	public void computePriceTestRound() { //test for a duration exactly 2 hours
		int duration=120;
		assertEquals(v.computePrice(duration,elec),3);
		assertEquals(v.computePrice(duration,mech),1);		
	}
	
	
	// same tests but with credit on the card
	@Test
	public void computePriceTestCredit() {
		v.addTimeCredit(30);
		int duration=125;
		assertEquals(v.computePrice(duration,elec),3);
		assertEquals(v.getTimeCredit(),25);
	}
	@Test
	public void computePriceTestShortCredit() {
		v.addTimeCredit(30);
		int duration=35;
		assertEquals(v.computePrice(duration,elec),1);
		assertEquals(v.getTimeCredit(),30);

	}

	@Test
	public void computePriceTestRoundCredit() { 
		v.addTimeCredit(30);
		int duration=120;
		assertEquals(v.computePrice(duration,elec),3);
		assertEquals(v.getTimeCredit(),30);
	}
	
	//same tests but with mechanical bike
	@Test
	public void computePriceTestCreditMech() {
		v.addTimeCredit(30);
		int duration=125;
		assertEquals(v.computePrice(duration,mech),1);
		assertEquals(v.getTimeCredit(),25);
	}
	@Test
	public void computePriceTestShortCreditMech() {
		v.addTimeCredit(30);
		int duration=35;
		assertEquals(v.computePrice(duration,mech),0);
		assertEquals(v.getTimeCredit(),30);

	}

	@Test
	public void computePriceTestRoundCreditMech() { 
		v.addTimeCredit(30);
		int duration=120;
		assertEquals(v.computePrice(duration,mech),1);
		assertEquals(v.getTimeCredit(),30);
	}


}
