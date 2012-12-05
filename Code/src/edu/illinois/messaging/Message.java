package edu.illinois.messaging;

import java.util.Date;

import edu.illinois.data.Group;
import edu.illinois.data.User;

/**
 * @author Tianyi Wang
 */
public class Message {
	@SuppressWarnings("unused")
	private String title;			//TODO: remove this.
	private String message;

	private Date availableDate;
	private Date expireDate;
	
	@SuppressWarnings("unused")
	private User sender;			//TODO: remove this.
	@SuppressWarnings("unused")
	private Group receivers;		//TODO: remove this.
	
	public Message(String title, String msg, Date availableDate, Date expireDate) {
		this.title = title;
		this.message = msg;
		this.availableDate = availableDate;
		this.expireDate = expireDate;
	}
	
	public boolean isAvailable() {
		Date curDate = new Date();
		return curDate.after(availableDate);
	}
	
	public boolean hasExpired() {
		Date curDate = new Date();
		return curDate.after(expireDate);
	}
	
	public String getMessage() {
		return message;
	}
}