package myVelib.ride;

/**
 * The generator is used to compute automatically the next user id when calling the constructor
 * @author Ahmed Djermani
 *
 */

public class RideIdGenerator {
	
	private static RideIdGenerator instance = null;
	static int id;
	
	private RideIdGenerator() {}
	
	public static RideIdGenerator getInstance() {
		if (instance == null) {
			instance = new RideIdGenerator();
		}
		return instance;
	}
	
	public static int getNextRideId() {
		return id++;
	}

}
