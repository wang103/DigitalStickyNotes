package edu.illinois.data;

import java.util.ArrayList;

import edu.illinois.communication.Communicator;
import edu.illinois.digitalstickynotes.TheApplication;

import android.app.Activity;

/**
 * Various information of the client user.
 * 
 * @author Tianyi Wang
 */
public class UserInformation {
	
	private Activity activity;
	
	private User user;
	private String accessToken;
	private ArrayList<User> friendsList;
	
	/**
	 * Add a friend to the friends list.
	 * 
	 * @param user the user to add as friend.
	 */
	public void addFriend(User user) {
		friendsList.add(user);
	}
	
	/**
	 * Constructor.
	 */
	public UserInformation(Activity activity) {
		this.activity = activity;
		this.user = new User();
		this.friendsList = new ArrayList<User>();
	}
	
	public User getUser() {
		return user;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
		
		// Update user information.
		if (accessToken != null) {
			Communicator communicator = ((TheApplication)(activity.getApplication())).getCommunicator();
			communicator.setUserInfo(user);
		}
	}
}