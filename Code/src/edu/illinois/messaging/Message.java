package edu.illinois.messaging;

import java.util.Date;

import edu.illinois.data.Group;
import edu.illinois.data.User;

/**
 * @author Tianyi Wang
 */
public class Message {
	private String title;
	private String message;

	private Date availableDate;
	private Date expireDate;
	private Date receivedDate;
	
	private User sender;
	private Group receivers;
	
	public Message(String title, String msg, Date availableDate, Date expireDate, Date receivedDate) {
		this.title = title;
		this.message = msg;
		this.availableDate = availableDate;
		this.expireDate = expireDate;
		this.receivedDate = receivedDate;
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

	@Override
	public String toString() {
		return "Message [title=" + title + ", message=" + message
				+ ", availableDate=" + availableDate + ", expireDate="
				+ expireDate + ", receivedDate=" + receivedDate + ", sender="
				+ sender + ", receivers=" + receivers + "]";
	}
}