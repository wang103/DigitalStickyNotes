package edu.illinois.messaging;

import java.util.Date;

import edu.illinois.data.Group;
import edu.illinois.data.User;

/**
 * This class represents a digital sticky note.
 * 
 * @author Tianyi Wang
 */
public class Note {
	private long messageID;
	
	private String title;
	private String message;

	private Date receivedDate;
	private Date availableDate;
	private Date expireDate;
	
	private String receivedLocation;
	
	private User sender;
	private Group receivers;
	
	/**
	 * Check if the note should be available to the user.
	 * 
	 * @return true if the note is available. Otherwise false.
	 */
	public boolean isAvailable() {
		Date curDate = new Date();
		return curDate.after(availableDate);
	}
	
	/**
	 * Check if the note has expired.
	 * 
	 * @return true if the note is expired. Otherwise false.
	 */
	public boolean hasExpired() {
		Date curDate = new Date();
		return curDate.after(expireDate);
	}

	/**
	 * Constructor.
	 * 
	 * @param messageID message ID.
	 * @param title message title.
	 * @param msg message content.
	 * @param receivedDate message received time.
	 * @param availableDate message available time.
	 * @param expireDate message expiration time.
	 * @param location the location this message was received at.
	 * @param sender the sender of this note.
	 */
	public Note(long messageID, String title, String msg, Date receivedDate,
			Date availableDate, Date expireDate, String location, User sender) {
		this.messageID = messageID;
		this.title = title;
		this.message = msg;
		this.receivedDate = receivedDate;
		this.availableDate = availableDate;
		this.expireDate = expireDate;
		this.receivedLocation = location;
		this.sender = sender;
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

	public String getReceivedLocation() {
		return receivedLocation;
	}
	
	public User getSender() {
		return sender;
	}

	public Group getReceivers() {
		return receivers;
	}
}