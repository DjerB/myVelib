package myVelib.exceptions;

public class OfflineException extends Exception {
	
	public OfflineException() {
		System.out.println("This device is offline !");
	}

}
