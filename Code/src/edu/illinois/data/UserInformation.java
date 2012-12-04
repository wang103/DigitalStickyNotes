package edu.illinois.data;

import java.util.ArrayList;

/**
 * @author Tianyi Wang
 */
public class UserInformation {
	private ArrayList<User> friendsList;
	
	public UserInformation() {
		friendsList = new ArrayList<User>();
	}
	
	public void addFriend(User user) {
		friendsList.add(user);
	}
}