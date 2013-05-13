package edu.illinois.data;

import java.util.ArrayList;

/**
 * Various information of the client user.
 * 
 * @author Tianyi Wang
 */
public class UserInformation {
	
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
	public UserInformation() {
		friendsList = new ArrayList<User>();
	}
}