package myVelib.userAndCard;


import myVelib.bike.*;

/**
 * NoCard class is used when the user has no card and has to pay the max charges
 * @author Ahmed Djermani
 *
 */

public class NoCard implements Card {

	/**
	 * addTimeCredit cannot be called by a NoCard instance, then has no content
	 */
	@Override
	public void addTimeCredit(int timeCredit) {}

	/**
	 * We compute the price according to the ride duration 
	 * @param duration
	 * 			duration of the ride has been computed and sent for computing the price
	 * @param
	 * 			the type of the bike has the impact over the price of the ride
	 * @return 
	 * 			the price is returned
	 * @author Ahmed Djermani
	 */
	
	@Override
	public int computePrice(int duration, Bike bike) {
		
		int unite = (boolean) (bike instanceof MechanicalBike) ? 1 : 2;    // Definition of the price per hour
		int hours = duration / 60;
		
		if ((duration%60) > 0) {            // if duration exceeds a integer number of hours, then extra minutes are charged as an extra hour
			hours += 1;
		}
		
		int price = unite * hours;
		return price;
	}

}
