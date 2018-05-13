package myVelib.userAndCard;


/**
 * The generator is used to compute automatically the next user id when calling the constructor
 * @author Ahmed Djermani
 *
 */

public class UserIdGenerator {
	
	private static UserIdGenerator instance = null;
	static int id;
	
	private UserIdGenerator() {}
	
	public static UserIdGenerator getInstance() {
		if (instance == null) {
			instance = new UserIdGenerator();
		}
		return instance;
	}
	
	public static int getNextUserId() {
		id++;
		return id;
	}
	
	public static void reset() {
		id = 0;
	}

}
