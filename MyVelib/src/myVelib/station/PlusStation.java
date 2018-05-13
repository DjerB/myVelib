package myVelib.station;

import myVelib.utilities.Position;

/**
 * Overrides the timecredit method to return the correct timecredit according to the station type
 * @author Ahmed Djermani
 *
 */
public class PlusStation implements StationType {

	@Override
	public int giveTimeCredit() {
	
		return 5;
	}

}
