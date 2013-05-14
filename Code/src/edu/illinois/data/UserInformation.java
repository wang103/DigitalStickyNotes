package edu.illinois.data;

import java.util.ArrayList;

import edu.illinois.communication.Communicator;
import edu.illinois.digitalstickynotes.TheApplication;

import android.app.Activity;
import android.os.AsyncTask;

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
	 * Update the user information.
	 */
	public void updateUserInfo() {
		if (accessToken != null) {
			GetUserInfoTask mTask = new GetUserInfoTask();
			mTask.execute((Void) null);
		}
	}
	
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
		updateUserInfo();
	}
	
	/**
	 * The task to get user's information from central server.
	 * 
	 * @author tianyiw
	 */
	public class GetUserInfoTask extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... arg0) {
			Communicator communicator = ((TheApplication)(activity.getApplication())).getCommunicator();
			communicator.setUserInfo(user, accessToken);
			return null;
		}
	}
}