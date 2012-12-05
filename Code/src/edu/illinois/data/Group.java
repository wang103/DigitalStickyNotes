package edu.illinois.data;

import java.util.ArrayList;

/**
 * @author Tianyi Wang
 */
public class Group {
	private String groupID;
	private ArrayList<User> groupMembers;
	
	public Group(String groupID) {
		this.groupID = groupID;
		this.groupMembers = new ArrayList<User>();
	}
	
	public void addToGroup(User user) {
		this.groupMembers.add(user);
	}
	
	public String getGroupID() {
		return groupID;
	}
}