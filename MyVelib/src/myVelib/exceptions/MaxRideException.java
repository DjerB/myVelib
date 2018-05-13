package myVelib.exceptions;

public class MaxRideException extends Exception {
	
	public MaxRideException() {
		super();
	}
	
	public void printErrorMessage() {
		System.out.println("You only can handle one ride at once.");
	}

}
