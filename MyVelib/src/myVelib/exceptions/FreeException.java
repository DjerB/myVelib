package myVelib.exceptions;

public class FreeException extends Exception {
	
	public FreeException() {
		System.out.println("This device is already free");
	}
	 
}
