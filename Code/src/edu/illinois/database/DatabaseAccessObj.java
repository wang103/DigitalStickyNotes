package edu.illinois.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.illinois.data.User;
import edu.illinois.messaging.Note;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * The database access object, allows multiple operations with the database.
 * This class is only used for debugging purpose.
 * Actual interactions with the database is done directly via ContentProvider.
 * 
 * @author tianyiw
 */
public class DatabaseAccessObj {
	
	private SQLiteDatabase database;
	private SQLiteHelperMessage messageDBHelper;
	private String[] columnNames = {SQLiteHelperMessage.COLUMN_ID,
			SQLiteHelperMessage.COLUMN_RECEIVED_TIME, SQLiteHelperMessage.COLUMN_AVAILABLE_TIME, 
			SQLiteHelperMessage.COLUMN_EXPIRE_TIME, SQLiteHelperMessage.COLUMN_TITLE,
			SQLiteHelperMessage.COLUMN_MESSAGE, SQLiteHelperMessage.COLUMN_SENDER};
	
	/**
	 * Open the database. Must be called before any other operations.
	 */
	public void open() {
		database = messageDBHelper.getWritableDatabase();
	}
	
	/**
	 * Close the database.
	 */
	public void close() {
		messageDBHelper.close();
		database = null;
	}

	/**
	 * Insert a new note into the SQLite database.
	 * 
	 * @param note a {@link Note} object.
	 */
	public void insertNote(Note note) {
		ContentValues values = new ContentValues();
		
		values.put(SQLiteHelperMessage.COLUMN_ID, note.getMessageID());
		values.put(SQLiteHelperMessage.COLUMN_RECEIVED_TIME, note.getReceivedDateString());
		values.put(SQLiteHelperMessage.COLUMN_AVAILABLE_TIME, note.getAvailableDateString());
		values.put(SQLiteHelperMessage.COLUMN_EXPIRE_TIME, note.getExpireDateString());
		values.put(SQLiteHelperMessage.COLUMN_TITLE, note.getTitle());
		values.put(SQLiteHelperMessage.COLUMN_MESSAGE, note.getMessage());
		values.put(SQLiteHelperMessage.COLUMN_SENDER, note.getSender().getUserName());
		
		database.insert(SQLiteHelperMessage.TABLE_NAME, null, values);
	}
	
	/**
	 * Delete a note from the SQLite database.
	 * 
	 * @param note a {@link Note} object.
	 */
	public void deleteNote(Note note) {
		long messageID = note.getMessageID();
		
		database.delete(SQLiteHelperMessage.TABLE_NAME,
				SQLiteHelperMessage.COLUMN_ID + " = " + messageID, null);
	}
	
	/**
	 * This method will return all the available notes, delete all the expired
	 * notes, and ignore all the received, but un-available notes.
	 * 
	 * @return a list of available notes.
	 */
	public List<Note> getAllAvailableNotes() {
		List<Note> messages = new ArrayList<Note>();
	
		Cursor cursor = database.query(SQLiteHelperMessage.TABLE_NAME,
				columnNames, null, null, null, null, null);
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Note message = cursorToNote(cursor);
			
			if (message.hasExpired()) {
				deleteNote(message);
			} else if (message.isAvailable()) {
				messages.add(message);
			}

			cursor.moveToNext();
		}

		cursor.close();
		return messages;
	}

	/**
	 * Helper method to convert a {@link Cursor} object to a {@link Note} object.
	 * 
	 * @param cursor a {@link Cursor} object.
	 * @return a {@link Note} object.
	 */
	private Note cursorToNote(Cursor cursor) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		
		Note note = null;
		try {
			note = new Note(cursor.getLong(0), cursor.getString(4), cursor.getString(5),
					simpleDateFormat.parse(cursor.getString(1)),
					simpleDateFormat.parse(cursor.getString(2)),
					simpleDateFormat.parse(cursor.getString(3)),
					cursor.getString(1), cursor.getString(2), cursor.getString(3),
					new User(cursor.getString(6)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return note;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param context the {@link Context} object.
	 */
	public DatabaseAccessObj(Context context) {
		messageDBHelper = new SQLiteHelperMessage(context);
	}
}