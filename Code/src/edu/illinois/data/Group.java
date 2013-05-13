package edu.illinois.data;

import java.util.ArrayList;

/**
 * This class represents a group of users to support group interactions.
 * 
 * @author Tianyi Wang
 */
public class Group {
	
	private String groupID;
	private ArrayList<User> groupMembers;
	
	/**
	 * Add a user to this group.
	 * 
	 * @param user the {@link User} object.
	 */
	public void addToGroup(User user) {
		this.groupMembers.add(user);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param groupID the unique ID number of this group.
	 */
	public Group(String groupID) {
		this.groupID = groupID;
		this.groupMembers = new ArrayList<User>();
	}
	
	public String getGroupID() {
		return groupID;
	}
}