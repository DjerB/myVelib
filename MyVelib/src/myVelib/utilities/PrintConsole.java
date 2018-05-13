package myVelib.utilities;

import myVelibGUI.ClientApplication;

public class PrintConsole {

	private static String screenMessage = "";
	
	public static void updateScreenMessage(String message) {
		
		if (!message.equals(screenMessage)) {
			screenMessage = message + "\n";
		}
		ClientApplication.update();
				
	}
	
	public static String getMessage() { return screenMessage; }
}
