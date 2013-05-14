package edu.illinois.data;

/**
 * This class represents an user of this service.
 * 
 * @author Tianyi Wang
 */
public class User {
	
	private String userName;
	private String firstName;
	private String lastName;
	
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

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}