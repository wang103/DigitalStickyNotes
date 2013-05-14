package edu.illinois.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * {@link SQLiteOpenHelper} object for message.
 * 
 * @author tianyiw
 */
public class SQLiteHelperMessage extends SQLiteOpenHelper {

	public static final String TABLE_NAME = "notes";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_RECEIVED_TIME = "received_time";
	public static final String COLUMN_AVAILABLE_TIME = "available_time";
	public static final String COLUMN_EXPIRE_TIME = "expire_time";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_MESSAGE = "message";
	public static final String COLUMN_LOCATION = "received_loc";
	public static final String COLUMN_SENDER = "sender_id";
	public static final String COLUMN_RECEIVER = "receiver_id";

	private static final String DATABASE_NAME = "digitalstickynotes.db";
	private static final int DATABASE_VERSION = 1;

	/**
	 * Constructor.
	 * 
	 * @param context the {@link Context} object.
	 */
	public SQLiteHelperMessage(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Database creation SQL statement
		String databaseCreateStat = "create table " + TABLE_NAME + "(" 
				+ COLUMN_ID + " integer primary key, "
				+ COLUMN_RECEIVED_TIME + " datetime not null, "
				+ COLUMN_AVAILABLE_TIME + " datetime not null, "
				+ COLUMN_EXPIRE_TIME + " datetime not null, "
				+ COLUMN_TITLE + " tinytext not null, "
				+ COLUMN_MESSAGE + " text not null, "
				+ COLUMN_LOCATION + " text not null, "
				+ COLUMN_SENDER + " tinytext not null, "
				+ COLUMN_RECEIVER + " tinytext not null);";

		db.execSQL(databaseCreateStat);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SQLiteHelperMessage.class.getName(), "Upgrading database from version " + oldVersion
				+ " to " + newVersion + ", which will destroy all old data.");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
}