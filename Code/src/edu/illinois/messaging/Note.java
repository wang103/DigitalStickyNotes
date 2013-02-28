package edu.illinois.messaging;

import java.util.Date;

import edu.illinois.data.Group;
import edu.illinois.data.User;

/**
 * @author Tianyi Wang
 */
public class Note {
	private long messageID;
	
	private String title;
	private String message;

	private Date receivedDate;
	private Date availableDate;
	private Date expireDate;
	
	private User sender;
	private Group receivers;
	
	public Note(long messageID, String title, String msg, Date receivedDate,
			Date availableDate, Date expireDate, User sender) {
		this.messageID = messageID;
		this.title = title;
		this.message = msg;
		this.receivedDate = receivedDate;
		this.availableDate = availableDate;
		this.expireDate = expireDate;
		this.sender = sender;
	}
	
	public boolean isAvailable() {
		Date curDate = new Date();
		return curDate.after(availableDate);
	}
	
	public boolean hasExpired() {
		Date curDate = new Date();
		return curDate.after(expireDate);
	}

	@Override
	public String toString() {
		return sender.getUserName() + ": " + title;
	}

	public long getMessageID() {
		return messageID;
	}
	
	public String getTitle() {
		return title;
	}

	public String getMessage() {
		return message;
	}

	public Date getAvailableDate() {
		return availableDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public User getSender() {
		return sender;
	}

	public Group getReceivers() {
		return receivers;
	}
}