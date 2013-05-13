package edu.illinois.digitalstickynotes;

import edu.illinois.communication.Communicator;
import android.app.Application;

/**
 * The application.
 * 
 * @author tianyiw
 */
public class TheApplication extends Application {
	
	private Communicator communicator;
	
	public Communicator getCommunicator() {
		return communicator;
	}
	
	public void setCommunicator(Communicator communicator) {
		this.communicator = communicator;
	}
}