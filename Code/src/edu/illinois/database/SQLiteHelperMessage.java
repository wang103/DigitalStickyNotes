package edu.illinois.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelperMessage extends SQLiteOpenHelper {
	
	public static final String TABLE_NAME = "messages";
	public static final String COLUMN_ID = "message_id";
	public static final String COLUMN_RECEIVED_TIME = "received_time";
	public static final String COLUMN_AVAILABLE_TIME = "available_time";
	public static final String COLUMN_EXPIRE_TIME = "expire_time";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_MESSAGE = "message";
	public static final String COLUMN_SENDER = "sender_id";
	
	private static final String DATABASE_NAME = "messages.db";
	private static final int DATABASE_VERSION = 1;
	
	public SQLiteHelperMessage(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Database creation sql statement
		String databaseCreateStat = "create table " + TABLE_NAME + "(" 
				+ COLUMN_ID + " integer primary key, "
				+ COLUMN_RECEIVED_TIME + " datetime not null, "
				+ COLUMN_AVAILABLE_TIME + " datetime not null, "
				+ COLUMN_EXPIRE_TIME + " datetime not null, "
				+ COLUMN_TITLE + " tinytext not null, "
				+ COLUMN_MESSAGE + " text not null, "
				+ COLUMN_SENDER + " tinytext not null);";
		
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