package myVelib.system;



public class NetworkIdGenerator {
	
	private static NetworkIdGenerator instance = null;
	static int id;
	
	private NetworkIdGenerator() {}
	
	public static NetworkIdGenerator getInstance() {
		if (instance == null) {
			instance = new NetworkIdGenerator();
		}
		return instance;
	}
	
	public static int getNextNetworkId() {
		return id++;
	}
	
	public static void reset() {
		id = 0;
	}
}
