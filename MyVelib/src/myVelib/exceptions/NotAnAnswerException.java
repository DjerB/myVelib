package myVelib.exceptions;

public class NotAnAnswerException extends Exception {
	
	public NotAnAnswerException() {
		super();
	}
	
	public void printErrorMessage() {
		System.out.println("Wrong answer : Please enter 'Y' or 'N'.");
	}
}
