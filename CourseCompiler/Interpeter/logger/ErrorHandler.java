/**
 * the file is the main project error handle
 * if it cann't deal with the error, than throw it
 */
package logger;

import java.util.logging.Logger;

public class ErrorHandler {

	public static void error(int line, String message){
		report(line, "", message);
	}

	public static void report(int line, String where, String message){
		Logger.getLogger(ErrorHandler.class.getName()).severe("[line " + line + "] Error" + where + ": " + message);
	}
}