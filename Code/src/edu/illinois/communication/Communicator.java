package edu.illinois.communication;

import edu.illinois.classinterfaces.ConnectionManager;
import edu.illinois.digitalstickynotes.MainActivity;

public class Communicator {
	
	@SuppressWarnings("unused")			//TODO: remove this.
	private ConnectionManager connectionManager;
	
	public boolean tryAuthenticate(String email, String password) {
		//TODO: now only try to authenticate against the main database, need to add
		// support for authentication against local server.
		
		
		return true;
	}
	
	public Communicator() {
		this.connectionManager = MainActivity.connectionManager;
	}
}