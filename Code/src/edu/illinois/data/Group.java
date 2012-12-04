package edu.illinois.data;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Tianyi Wang
 */
public class Group {
	private ArrayList<User> groupMembers;
	
	public Group() {
		groupMembers = new ArrayList<User>();
	}
	
	public void sendMessageToGroup(String msg) {
		Iterator<User> itr = groupMembers.iterator();
		while (itr.hasNext()) {
			User user = itr.next();
			user.sendMessage(msg);
		}
	}
}