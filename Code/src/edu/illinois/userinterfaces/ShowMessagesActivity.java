package edu.illinois.userinterfaces;

import java.util.List;

import edu.illinois.database.DatabaseAccessObj;
import edu.illinois.digitalstickynotes.MainActivity;
import edu.illinois.digitalstickynotes.R;
import edu.illinois.messaging.Note;
import android.os.Bundle;
import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

public class ShowMessagesActivity extends ListActivity implements LoaderCallbacks<Cursor> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_messages);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Create a progress bar to display while the list loads.
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
		progressBar.setIndeterminate(true);
		getListView().setEmptyView(progressBar);

		DatabaseAccessObj databaseAccessObj = MainActivity.databaseManager;
		
		List<Note> messages = databaseAccessObj.getAllAvailableNotes();
		
		// Use the SimpleCursorAdapter to show the messages in this ListView.
		ArrayAdapter<Note> adapter = new ArrayAdapter<Note>(this,
				android.R.layout.simple_list_item_1, messages);
		setListAdapter(adapter);

		Log.d("TIANYI", "ShowMessages List Activity creation done with " + messages.size() + " messages.");
		
		//TODO: start a thread to periodically check new messages in the database.
		
		progressBar.setVisibility(View.GONE);
	}

	@Override
	protected void onListItemClick(ListView l, android.view.View v, int position, long id) {
		Log.d("TIANYI", "List item " + position + " clicked.");
		
		Note message = (Note) getListView().getItemAtPosition(position);
		
		Intent intent = new Intent(this, ShowDetailedMessageActivity.class);
		
		intent.putExtra("Title", message.getTitle());
		intent.putExtra("Message", message.getMessage());
		intent.putExtra("Sender", message.getSender().getUserName());
		intent.putExtra("AvailableTime", message.getAvailableDate().toString());
		intent.putExtra("ReceivedTime", message.getReceivedDate().toString());
		intent.putExtra("ExpireTime", message.getExpireDate().toString());

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
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		
	}
}