package tests;

import org.junit.Test;

import myVelib.utilities.*;

public class PositionTest {

	@Test
	public void computeTheRightDistanceBetween2GPSLocations() {
		
		Position p1 = new Position(3000, 1200);
		Position p2 = new Position(2, 400);
		
		double distance = p1.getDistance(p2);
		
		org.junit.Assert.assertEquals(3102,  (int)distance);
	}

}
