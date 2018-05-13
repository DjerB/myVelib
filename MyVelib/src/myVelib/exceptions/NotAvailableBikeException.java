package myVelib.exceptions;

public class NotAvailableBikeException extends Exception {
	
	public NotAvailableBikeException() {
		System.out.println("The type of bike you're asking for is not available !");
	}
}
