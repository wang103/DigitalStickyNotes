package edu.illinois.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.illinois.messaging.Message;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseAccessObj {
	private SQLiteDatabase database;
	private SQLiteHelperMessage messageDBHelper;
	private String[] columnNames = {SQLiteHelperMessage.COLUMN_ID,
			SQLiteHelperMessage.COLUMN_RECEIVED_TIME, SQLiteHelperMessage.COLUMN_AVAILABLE_TIME, 
			SQLiteHelperMessage.COLUMN_EXPIRE_TIME, SQLiteHelperMessage.COLUMN_TITLE,
			SQLiteHelperMessage.COLUMN_MESSAGE, SQLiteHelperMessage.COLUMN_SENDER};
	
	public DatabaseAccessObj(Context context) {
		messageDBHelper = new SQLiteHelperMessage(context);
	}
	
	public void open() {
		database = messageDBHelper.getWritableDatabase();
	}
	
	public void close() {
		messageDBHelper.close();
	}
	
	public void insertMessage(Message message) {
		ContentValues values = new ContentValues();
		
		values.put(SQLiteHelperMessage.COLUMN_ID, message.getMessageID());
		values.put(SQLiteHelperMessage.COLUMN_RECEIVED_TIME, message.getReceivedDate().toString());
		values.put(SQLiteHelperMessage.COLUMN_AVAILABLE_TIME, message.getAvailableDate().toString());
		values.put(SQLiteHelperMessage.COLUMN_EXPIRE_TIME, message.getExpireDate().toString());
		values.put(SQLiteHelperMessage.COLUMN_TITLE, message.getTitle());
		values.put(SQLiteHelperMessage.COLUMN_MESSAGE, message.getMessage());
		values.put(SQLiteHelperMessage.COLUMN_SENDER, message.getSender().getUserName());
		
		database.insert(SQLiteHelperMessage.TABLE_NAME, null, values);
	}
	
	public void deleteMessage(Message message) {
		long messageID = message.getMessageID();
		
		database.delete(SQLiteHelperMessage.TABLE_NAME,
				SQLiteHelperMessage.COLUMN_ID + " = " + messageID, null);
	}
	
	/**
	 * This method will return all the available messages, delete all the expired
	 * messages, and ignore all the received, but un-available message.
	 * 
	 * @return a list of available messages.
	 */
	public List<Message> getAllAvailableMessages() {
		List<Message> messages = new ArrayList<Message>();
		
		Cursor cursor = database.query(SQLiteHelperMessage.TABLE_NAME,
				columnNames, null, null, null, null, null);
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Message message = cursorToMessage(cursor);
			
			if (message.hasExpired()) {
				deleteMessage(message);
			} else if (message.isAvailable()) {
				messages.add(message);
			}
			
			cursor.moveToNext();
		}
		
		cursor.close();
		return messages;
	}
	
	private Message cursorToMessage(Cursor cursor) {
		//TODO: fix the string to date conversion.
		Message message = new Message(cursor.getLong(0), cursor.getString(4),
				cursor.getString(5), new Date() /*cursor.getString(1))*/,
				new Date() /*cursor.getString(2))*/, new Date() /*cursor.getString(2)*/);
		return message;
	}
}