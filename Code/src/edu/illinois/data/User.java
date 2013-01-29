package edu.illinois.data;

import java.security.PublicKey;

/**
 * @author Tianyi Wang
 */
public class User {
	private String userName;
	private PublicKey publicKey;
	
	public User(String userName, PublicKey pk) {
		this.userName = userName;
		this.publicKey = pk;
	}

	public String getUserName() {
		return userName;
	}
}