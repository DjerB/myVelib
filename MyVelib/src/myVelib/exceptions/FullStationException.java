package myVelib.exceptions;

public class FullStationException extends Exception {
	
	public FullStationException() {
		System.out.println("No room : The station has no empty slot");
	}
}
