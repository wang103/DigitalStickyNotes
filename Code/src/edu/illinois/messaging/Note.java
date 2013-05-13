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
	
	private String receivedDateString;
	private String availableDateString;
	private String expireDateString;
	
	private User sender;
	private Group receivers;
	
	/**
	 * Constructor.
	 * 
	 * @param messageID message ID.
	 * @param title message title.
	 * @param msg message content.
	 * @param receivedDate message received time.
	 * @param availableDate message available time.
	 * @param expireDate message expiration time.
	 * @param receivedDateString
	 * @param availableDateString
	 * @param expireDateString
	 * @param sender
	 */
	public Note(long messageID, String title, String msg, Date receivedDate,
			Date availableDate, Date expireDate, String receivedDateString,
			String availableDateString, String expireDateString, User sender) {
		this.messageID = messageID;
		this.title = title;
		this.message = msg;
		this.receivedDate = receivedDate;
		this.availableDate = availableDate;
		this.expireDate = expireDate;
		this.receivedDateString = receivedDateString;
		this.availableDateString = availableDateString;
		this.expireDateString = expireDateString;
		this.sender = sender;
	}
	
	/**
	 * Check if the note should be available to the user.
	 * 
	 * @return true if the note is available. False otherwise.
	 */
	public boolean isAvailable() {
		Date curDate = new Date();
		return curDate.after(availableDate);
	}
	
	/**
	 * 
	 * @return
	 */
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
	
	public String getAvailableDateString() {
		return availableDateString;
	}

	public String getExpireDateString() {
		return expireDateString;
	}

	public String getReceivedDateString() {
		return receivedDateString;
	}

	public User getSender() {
		return sender;
	}

	public Group getReceivers() {
		return receivers;
	}
}