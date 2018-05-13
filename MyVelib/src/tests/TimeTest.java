package tests;

import org.junit.Test;

import myVelib.utilities.*;

public class TimeTest {

	/*@Test
	public void computeTheRightRidingTimeBetweenDepartureAndArrival() {
		
		Time t1 = new Time();
		try {
			Thread.sleep(62000);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		Time t2 = new Time();
		
		org.junit.Assert.assertEquals(2, t1.computeRidingTime(t2));
	}*/
	
	@Test
	public void determinesTheOrderRelationBetween2times() {
		
		Time t1 = new Time();
		
		Time.updateTime(20);
		
		Time t2 = new Time();
		
		org.junit.Assert.assertEquals(t1.greaterThan(t2), false);
		
	}

}
