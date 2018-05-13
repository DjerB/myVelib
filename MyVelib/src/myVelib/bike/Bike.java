package myVelib.bike;

import myVelib.system.Network;
import myVelib.userAndCard.*;
import myVelib.utilities.*;

public abstract class Bike {
	
	protected int id;
	protected int userId;
	
	public abstract typeBike getTypeBike();
	
	public Bike() {
		this.id = BikeIdGenerator.getNextBikeId();
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getUserId() {
		return this.userId;
	}
	
	public void setUser(User user) {
		this.userId = user.getUserId();
	}
	
	public User getUser(Network network) {
		return network.getUser(userId);
	}
	
	public String toString() {
		return("Bike : "+" "+id);
	}
}
