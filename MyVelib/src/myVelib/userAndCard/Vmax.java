package myVelib.userAndCard;

import myVelib.bike.*;

/**
 * Vmax class represents the card Vmax and its rules for computing prices
 * @author Ahmed Djermani
 *
 */

public class Vmax implements Card {
	
	private int timeCredit;
	
	public Vmax() { this.timeCredit = 0; }
	
	// GETTER
	
	public int getTimeCredit() {
		return this.timeCredit;
	}
	
	
	@Override
	public void addTimeCredit(int timeCredit) {
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
		
		int unite = 1;                                      // 1 euro per hour, not matter the bike that has been used
		int price = 0;
		
		int minutes = duration%60;
		int hours = 0;
                                   
		hours = (duration / 60);
		if (minutes > 0) {
			hours++;
		}
		price = (unite - 1) + (hours -1) * unite;
		
		return price;
	}

}
