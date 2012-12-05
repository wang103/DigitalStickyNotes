package edu.illinois.data;

import java.security.PublicKey;

/**
 * @author Tianyi Wang
 */
public class User {
	private String userName;
	//TODO: remove this.
	@SuppressWarnings("unused")
	private PublicKey publicKey;
	
	public User(String userName, PublicKey pk) {
		this.userName = userName;
		this.publicKey = pk;
	}

	public String getUserName() {
		return userName;
	}
}