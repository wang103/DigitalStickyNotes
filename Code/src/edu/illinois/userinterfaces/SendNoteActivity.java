package edu.illinois.userinterfaces;

import edu.illinois.communication.Communicator;
import edu.illinois.digitalstickynotes.R;
import edu.illinois.digitalstickynotes.TheApplication;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.support.v4.app.NavUtils;

public class SendNoteActivity extends Activity {

	private Communicator communicator;
	
	// UI references.
	private EditText sendToEditText;
	private Spinner locationSpinner;
	private EditText titleEditText;
	private EditText contentEditText;
	private DatePicker availableDatePicker;
	private TimePicker availableTimePicker;
	private DatePicker expireDatePicker;
	private TimePicker expireTimePicker;
	private Button sendButton;

	/**
	 * Send a note to the central server.
	 */
	private void sendNote() {
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_note);
		
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Set the communicator.
		this.communicator = ((TheApplication)(this.getApplication())).getCommunicator();
		
		sendToEditText = (EditText) findViewById(R.id.send_to_edittext);
		locationSpinner = (Spinner) findViewById(R.id.location_spinner);
		titleEditText = (EditText) findViewById(R.id.title_textview);
		contentEditText = (EditText) findViewById(R.id.content_textview);
		availableDatePicker = (DatePicker) findViewById(R.id.available_datepicker);
		availableTimePicker = (TimePicker) findViewById(R.id.available_timepicker);
		expireDatePicker = (DatePicker) findViewById(R.id.expire_datepicker);
		expireTimePicker = (TimePicker) findViewById(R.id.expire_timepicker);
		sendButton = (Button) findViewById(R.id.send_note_button);
		
		sendButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendNote();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_send_note, menu);
		return true;
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
}