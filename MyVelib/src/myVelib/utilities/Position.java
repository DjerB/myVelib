package myVelib.utilities;

import java.util.Random;

/**
 * This  class represents the GPS coordinates (longitude and latitude).
 * The constructor takes 6 arguments : the degrees, the degree minutes and the degree seconds for latitude and longitude.
 * Finally and foremost, it provides with a function that computes the distance between two points.
 * @author Ahmed Djermani
 *
 */

public class Position {
	
	private double x;
	private double y;
	
	// CONSTRUCTOR
	
	public Position(double x, double y) {
		
		this.x = x;
		this.y = y;
		
	}
	
	// GETTERS
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
 
	
	
	/**
	 * Compute distance between this position and a position parameter
	 * A geometric formula is used to compute the distance between 2 points at the surface of the Earth.
	 * 
	 * @param position
	 * 			the position parameter is passed as an argument to compute the distance in meters
	 * 			between "this" and position
	 * @return
	 * 			the return value is the distance in meters that separates both points
	 */
	
	public double getDistance(Position position) {
		
		double distance = Math.sqrt(Math.pow((position.x - this.x), 2) + Math.pow((position.y - this.y), 2));
		return distance;

	}
	
	public String toString() {
		return "at coordinates " + "(" + this.x + ", " + this.y + ")";
	}
}
