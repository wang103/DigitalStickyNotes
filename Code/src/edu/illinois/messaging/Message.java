package edu.illinois.messaging;

import java.util.Date;

/**
 * @author Tianyi Wang
 */
public class Message {
	private String message;

	private Date availableDate;
	private Date expireDate;
	
	public Message(String msg, Date availableDate, Date expireDate) {
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