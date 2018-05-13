package myVelib.exceptions;

public class MissingDataException extends Exception {

	public MissingDataException() {
		System.out.println("We lack data to compute station stats !");
	}
}
