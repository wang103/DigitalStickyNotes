package edu.illinois.data;

/**
 * This class represents an user of this service.
 * 
 * @author Tianyi Wang
 */
public class User {
	
	private String userName;
	
	/**
	 * Constructor.
	 * 
	 * @param userName the username.
	 */
	public User(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}
}