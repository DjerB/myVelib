package myVelib.bike;

/**
 * The generator is used to compute automatically the next user id when calling the constructor
 * @author Ahmed Djermani
 *
 */
public class BikeIdGenerator {
	
	private static BikeIdGenerator instance = null;
	static int id;
	
	private BikeIdGenerator() {}
	
	public static BikeIdGenerator getInstance() {
		if (instance == null) {
			instance = new BikeIdGenerator();
		}
		return instance;
	}
	
	public static int getNextBikeId() {
		return id++;
	}
}
