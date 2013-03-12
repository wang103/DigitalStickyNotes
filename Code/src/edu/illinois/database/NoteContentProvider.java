package edu.illinois.database;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class NoteContentProvider extends ContentProvider {
	
	private SQLiteHelperMessage messageDBHelper;

	@Override
	public boolean onCreate() {
		messageDBHelper = new SQLiteHelperMessage(getContext());
		return false;
	}
	
	@Override
	public synchronized Cursor query(Uri uri, String[] projection,
			String selection, String[] selectionArgs, String sortOrder) {
		
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		
		// Check if the caller has requested a column which does not exist.
		checkColumns(projection);
		
		queryBuilder.setTables(SQLiteHelperMessage.TABLE_NAME);
		
		
		
		return null;
	}
	
	@Override
	public String getType(Uri uri) {
		return null;
	}
	
	@Override
	public synchronized Uri insert(Uri uri, ContentValues values) {
		
		return null;
	}
	
	@Override
	public synchronized int delete(Uri uri, String selection, String[] selectionArgs) {
		
		return 0;
	}

	@Override
	public synchronized int update(Uri uri, ContentValues values,
			String selection, String[] selectionArgs) {
		
		return 0;
	}
	
	/**
	 * Check if the columns exist in the note table.
	 * If not, throws {@link IllegalArgumentException}.
	 * 
	 * @param projection the columns to be checked.
	 */
	private void checkColumns(String[] projection) {
		String[] available = {SQLiteHelperMessage.COLUMN_ID,
				SQLiteHelperMessage.COLUMN_RECEIVED_TIME, SQLiteHelperMessage.COLUMN_AVAILABLE_TIME,
				SQLiteHelperMessage.COLUMN_EXPIRE_TIME, SQLiteHelperMessage.COLUMN_TITLE,
				SQLiteHelperMessage.COLUMN_MESSAGE, SQLiteHelperMessage.COLUMN_SENDER};
		
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
			
			if (availableColumns.containsAll(requestedColumns) == false) {
				throw new IllegalArgumentException("Unknown columns in projection");
			}
		}
	}
}