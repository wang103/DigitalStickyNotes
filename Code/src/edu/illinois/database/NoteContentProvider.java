package edu.illinois.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class NoteContentProvider extends ContentProvider {

	@Override
	public synchronized int delete(Uri arg0, String arg1, String[] arg2) {
		if (arg0 == null) {
			throw new UnsupportedOperationException();
		}
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		if (arg0 == null) {
			throw new UnsupportedOperationException();
		}
		return null;
	}

	@Override
	public synchronized Uri insert(Uri arg0, ContentValues arg1) {
		if (arg0 == null) {
			throw new UnsupportedOperationException();
		}
		return null;
	}

	@Override
	public boolean onCreate() {
		return false;
	}

	@Override
	public synchronized Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
			String arg4) {
		if (arg0 == null) {
			throw new UnsupportedOperationException();
		}
		return null;
	}

	@Override
	public synchronized int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		if (arg0 == null) {
			throw new UnsupportedOperationException();
		}
		return 0;
	}
}