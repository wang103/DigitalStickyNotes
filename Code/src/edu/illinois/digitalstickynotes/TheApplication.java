package edu.illinois.digitalstickynotes;

import edu.illinois.communication.Communicator;
import edu.illinois.data.UserInformation;
import android.app.Application;

/**
 * The application.
 * 
 * @author tianyiw
 */
public class TheApplication extends Application {
	
	private UserInformation userInfo;
	private Communicator communicator;
	
	public UserInformation getUserInfo() {
		return userInfo;
	}
	
	public void setUserInfo(UserInformation userInfo) {
		this.userInfo = userInfo;
	}
	
	public Communicator getCommunicator() {
		return communicator;
	}
	
	public void setCommunicator(Communicator communicator) {
		this.communicator = communicator;
	}
}