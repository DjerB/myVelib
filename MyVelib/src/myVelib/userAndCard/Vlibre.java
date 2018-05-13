package myVelib.userAndCard;

import myVelib.bike.*;

/**
 * Vlibre class represents the card Vlibre and its rules for computing prices
 * @author Ahmed Djermani
 *
 */
public class Vlibre implements Card {
	
	/**
	 * timeCredit is the amount of credit in minutes the user has to reduce the cost of his ride
	 */
	private int timeCredit;
	
	public Vlibre() { this.timeCredit = 0; }
	
	// GETTER
	
	public int getTimeCredit() {
		return this.timeCredit;
	}
	
	// SETTER
	
	public void addTimeCredit(int timeCredit) {
		this.timeCredit += timeCredit;
	}
	
	
	/**
	 * We compute the price according to the ride duration : if consuming time credit makes the ride cheaper, then it is used;
	 * Otherwise, wasting the time credit would be totally useless.
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
		int unite = (boolean) (bike instanceof MechanicalBike) ? 1 : 2; // Definition of the price per hour
		int price = 0;
		
		int minutes = duration%60;
		int hours;

		if ((minutes - this.timeCredit) <= 0) {              // If time credit is useful, the we deduce it from the minutes
			this.timeCredit -= minutes;
			duration -= minutes;
			hours = (int) (duration / 60);
			System.out.println(hours);
			price = (unite - 1) + (hours - 1) * unite;     // price for the first hour,  plus price per unite of hour is applied
		}
		else {                                              // If not useful, don't waste time credit
			hours = (int)(duration / 60) + 1;
			price = (unite - 1) + (hours - 1) * unite;
		}
		
		return price;
	}
}
