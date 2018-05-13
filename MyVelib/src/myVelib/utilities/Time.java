package myVelib.utilities;

import java.util.concurrent.TimeUnit;
import java.util.Date;

/**
 * The class Time handles the computation of each ride's duration.
 * It enables to create a new object that contains the current date when calling the constructor.
 * @author Ahmed Djermani
 *
 */

public class Time {
	
	private static int currentTime = 0;
	private int creationTime;
	
	public Time() {
		super();
		this.creationTime = currentTime;;
	}
	
	public Time(int minutes) {
		this.creationTime = minutes;
	}
	
	public static void updateTime(int minutes) {
		currentTime = minutes;
	}
	
	
	public int getCreationTime() {
		return this.creationTime;
	}
	
	
	
	/**
	 * 
	 * @param time
	 * 			time represents an object Time when calling the method of "this"
	 * @return
	 * 			nbMinutes is the number of minutes between the time parameter and "this"
	 * @author Ahmed Djermani
	 */
	
	public int computeRidingTime(Time time) {
		
		return time.creationTime - this.creationTime;
	
	}
	
	/**
	 * The method indicates whether this is greater ("later") than time
	 * @param time
	 * @return true if this is greater than time
	 */
	public boolean greaterThan(Time time) {
		
		return (this.creationTime >= time.creationTime);
			
	}
	
	public String toString() {
		int minutes = this.creationTime % 60;
		int hours = this.creationTime - minutes;
		
		return hours + "h" + minutes;
	}

}
