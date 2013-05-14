package edu.illinois.database;

import java.util.Arrays;
import java.util.HashSet;

import edu.illinois.messaging.Note;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * The {@link ContentProvider} for {@link Note} in the database.
 * 
 * @author tianyiw
 */
public class NoteContentProvider extends ContentProvider {
	
	private SQLiteHelperMessage messageDBHelper;

	private static final int NOTES = 10;
	private static final int NOTE_ID = 20;
	
	private static final String AUTHORITY = "edu.illinois.digitalstickynotes.notecontentprovider";
	
	private static final String BASE_PATH = "notes";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
	
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/notes";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/note";
	
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, NOTES);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", NOTE_ID);
	}
	
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
		
		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case NOTES:
			break;
		case NOTE_ID:
			// Adding the ID to the original query.
			queryBuilder.appendWhere(SQLiteHelperMessage.COLUMN_ID + "=" + uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		SQLiteDatabase db = messageDBHelper.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder);
		
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		
		return cursor;
	}
	
	@Override
	public String getType(Uri uri) {
		return null;
	}
	
	@Override
	public synchronized Uri insert(Uri uri, ContentValues values) {
		
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase db = messageDBHelper.getWritableDatabase();
		long id = 0;
		
		switch (uriType) {
		case NOTES:
			id = db.insert(SQLiteHelperMessage.TABLE_NAME, null, values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		
		return Uri.parse(BASE_PATH + "/" + id);
	}
	
	@Override
	public synchronized int delete(Uri uri, String selection,
			String[] selectionArgs) {
		
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase db = messageDBHelper.getWritableDatabase();
		int rowsDeleted = 0;
		
		switch (uriType) {
		case NOTES:
			rowsDeleted = db.delete(SQLiteHelperMessage.TABLE_NAME, selection,
					selectionArgs);
			break;
		case NOTE_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = db.delete(SQLiteHelperMessage.TABLE_NAME,
						SQLiteHelperMessage.COLUMN_ID + "=" + id, null);
			} else  {
				rowsDeleted = db.delete(SQLiteHelperMessage.TABLE_NAME,
						SQLiteHelperMessage.COLUMN_ID + "=" + id + " and " + selection,
						selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		
		return rowsDeleted;
	}

	@Override
	public synchronized int update(Uri uri, ContentValues values,
			String selection, String[] selectionArgs) {
		
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase db = messageDBHelper.getWritableDatabase();
		int rowsUpdated = 0;
		
		switch (uriType) {
		case NOTES:
			rowsUpdated = db.update(SQLiteHelperMessage.TABLE_NAME, values,
					selection, selectionArgs);
			break;
		case NOTE_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = db.update(SQLiteHelperMessage.TABLE_NAME, values,
						SQLiteHelperMessage.COLUMN_ID + "=" + id, null);
			} else {
				rowsUpdated = db.update(SQLiteHelperMessage.TABLE_NAME, values,
						SQLiteHelperMessage.COLUMN_ID + "=" + id + " and " + selection,
						selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		
		return rowsUpdated;
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
				SQLiteHelperMessage.COLUMN_MESSAGE, SQLiteHelperMessage.COLUMN_LOCATION,
				SQLiteHelperMessage.COLUMN_SENDER, SQLiteHelperMessage.COLUMN_RECEIVER};
		
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
			
			if (availableColumns.containsAll(requestedColumns) == false) {
				throw new IllegalArgumentException("Unknown columns in projection");
			}
		}
	}
}