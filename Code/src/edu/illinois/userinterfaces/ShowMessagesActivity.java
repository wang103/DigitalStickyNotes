package edu.illinois.userinterfaces;

import edu.illinois.database.NoteContentProvider;
import edu.illinois.database.SQLiteHelperMessage;
import edu.illinois.digitalstickynotes.R;
import android.net.Uri;
import android.os.Bundle;
import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v4.app.NavUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * A ListActivity to show all of user's notes.
 * 
 * @author tianyiw
 */
public class ShowMessagesActivity extends ListActivity implements LoaderCallbacks<Cursor> {

	private static final int DELETE_ID = Menu.FIRST + 1;
	
	private SimpleCursorAdapter adapter;
	
	/**
	 * Populate the list view with notes.
	 */
	private void fillData() {
		
		// Fields from the database.
		String[] from = new String[] {SQLiteHelperMessage.COLUMN_TITLE, SQLiteHelperMessage.COLUMN_SENDER};
		
		// Fields on the UI to map.
		int[] to = new int[] {R.id.row_title, R.id.row_sender};
		
		getLoaderManager().initLoader(0, null, this);
		
		adapter = new SimpleCursorAdapter(this, R.layout.note_row, null, from, to, 0);
		
		setListAdapter(adapter);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_messages);

		this.getListView().setDividerHeight(2);
		
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		fillData();
		
		registerForContextMenu(getListView());
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			Uri uri = Uri.parse(NoteContentProvider.CONTENT_URI + "/" + info.id);
			getContentResolver().delete(uri, null, null);
			fillData();
			return true;
		}
		
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, android.view.View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Intent intent = new Intent(this, ShowDetailedMessageActivity.class);
		Uri noteUri = Uri.parse(NoteContentProvider.CONTENT_URI + "/" + id);
		intent.putExtra(NoteContentProvider.CONTENT_ITEM_TYPE, noteUri);

		startActivity(intent);
	};
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, R.string.menu_delete);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = {SQLiteHelperMessage.COLUMN_ID, SQLiteHelperMessage.COLUMN_TITLE,
				SQLiteHelperMessage.COLUMN_SENDER};
		CursorLoader cursorLoader = new CursorLoader(this, NoteContentProvider.CONTENT_URI,
				projection, null, null, null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		adapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);
	}
}