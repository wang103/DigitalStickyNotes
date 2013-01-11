package edu.illinois.digitalstickynotes;

import android.app.ActionBar.LayoutParams;
import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;

public class ShowMessagesActivity extends ListActivity implements LoaderCallbacks<Cursor> {

	private SimpleCursorAdapter mCursorAdapter;
	
	// Message rows.
	static final String[] PROJECTION = new String[] {"MessageTitle", "MessageContent"};

	// Select criteria.
	static final String SELECTION = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Create a progress bar to display while the list loads
		ProgressBar progressBar = new ProgressBar(this);
		progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, Gravity.CENTER));
		progressBar.setIndeterminate(true);
		getListView().setEmptyView(progressBar);
		
		// Must add the progress bar to the root of the layout.
		ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
		root.addView(progressBar);
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		
	}
}