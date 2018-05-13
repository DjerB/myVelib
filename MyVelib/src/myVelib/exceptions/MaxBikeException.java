package myVelib.exceptions;

public class MaxBikeException extends Exception {
	
	public MaxBikeException() {
		super();
	}
	
	public void printErrorMessage() {
		System.out.println("A user can handle only one bike at once.");

	}

}
