package myVelib.exceptions;

public class NoDestinationAssignedException extends Exception {
	
	public NoDestinationAssignedException() {
		super();
	}
	
	public void printErrorMessage() {
		System.out.println("No destination station is assigned");
	}

}
