package myVelib.station;


/**
 * The generator is used to compute automatically the next user id when calling the constructor
 * @author Ahmed Djermani
 *
 */
public class StationIdGenerator {
	
	private static StationIdGenerator instance = null;
	static int id = 0;
	
	private StationIdGenerator() {}
	
	public static StationIdGenerator getInstance() {
		if (instance == null) {
			instance = new StationIdGenerator();
		}
		return instance;
	}
	
	public static int getNextStationId() {
		id++;
		return id;
	}
	
	public static void resetCounter() {
		id = 0;
	}


}
