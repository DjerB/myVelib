package myVelib.userAndCard;

import myVelib.bike.*;

/**
 * The interface Card declares the common methods to compute ride price and add time credit when arriving to a plus station
 * @author bachir
 *
 */
public interface Card {
	
	public void addTimeCredit(int timeCredit);
	public int computePrice(int duration, Bike bike);
	
}
