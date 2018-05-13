package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import myVelib.bike.Bike;
import myVelib.bike.ElectricalBike;
import myVelib.userAndCard.Vmax;

public class VmaxTest {

	Bike b;
	Vmax v;
	
	@Before
	public void init() {
		b=new ElectricalBike();
		v=new Vmax();
	}
	
	
	@Test
	public void computePriceTest() {
		int duration=125;
		assertEquals(v.computePrice(duration,b),2);		
	}
	@Test
	public void computePriceTestShort() {
		int duration=35;
		assertEquals(v.computePrice(duration,b),0);
	}

	@Test
	public void computePriceTestRound() { //test for a duration exactly 2 hours
		int duration=120;
		assertEquals(v.computePrice(duration,b),1);
	}
	
	
	// same tests but with credit on the card
	@Test
	public void computePriceTestCredit() {
		v.addTimeCredit(30);
		int duration=125;
		assertEquals(v.computePrice(duration,b),2);
		
	}
	@Test
	public void computePriceTestShortCredit() {
		v.addTimeCredit(30);
		int duration=35;
		assertEquals(v.computePrice(duration,b),0);
	}

	@Test
	public void computePriceTestRoundCredit() { 
		v.addTimeCredit(30);
		int duration=120;
		assertEquals(v.computePrice(duration,b),1);
	}

}
