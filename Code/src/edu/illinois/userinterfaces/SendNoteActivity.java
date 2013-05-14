package edu.illinois.userinterfaces;

import edu.illinois.communication.Communicator;
import edu.illinois.digitalstickynotes.R;
import edu.illinois.digitalstickynotes.TheApplication;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.support.v4.app.NavUtils;

public class SendNoteActivity extends Activity {

	private Communicator communicator;
	
	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private SendNotenTask mTask = null;
	
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

	private View mSendFormView;
	private View mSendStatusView;
	private TextView mSendStatusMessageView;

	private String[] receivers;
	private int locationID;
	private String title;
	private String content;
	private String availableString;
	private String expireString;

	/**
	 * Send a note to the central server.
	 */
	private void sendNote() {
		if (mTask != null) {
			return;
		}

		// Reset errors.
		sendToEditText.setError(null);
		titleEditText.setError(null);
		contentEditText.setError(null);

		// Get the fields.
		String receiversString = sendToEditText.getText().toString();
		receivers = receiversString.split(",");

		locationID = locationSpinner.getSelectedItemPosition();

		title = titleEditText.getText().toString();

		content = contentEditText.getText().toString();

		availableString = availableDatePicker.toString() + availableTimePicker.toString();

		expireString = expireDatePicker.toString() + expireTimePicker.toString();

		boolean cancel = false;
		View focusView = null;

		// Check for fields.
		if (receivers.length == 0) {
			sendToEditText.setError("Must have at least one receiver");
			focusView = sendToEditText;
			cancel = true;
		}

		if (TextUtils.isEmpty(title)) {
			titleEditText.setError("Title cannot be empty");
			focusView = titleEditText;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(content)) {
			contentEditText.setError("Content cannot be empty");
			focusView = contentEditText;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt send and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			startTaskForSend();
		}
	}

	/**
	 * Start the task to send note.
	 */
	private void startTaskForSend() {
		mSendStatusMessageView.setText("sending...");
		showProgress(true);
		mTask = new SendNotenTask();
		mTask.execute((Void) null);
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
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.locations, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		locationSpinner.setAdapter(adapter);
		
		mSendFormView = findViewById(R.id.send_form);
		mSendStatusView = findViewById(R.id.send_status);
		mSendStatusMessageView = (TextView) findViewById(R.id.send_status_message);
		
		sendButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendNote();
			}
		});
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mSendStatusView.setVisibility(View.VISIBLE);
			mSendStatusView.animate().setDuration(shortAnimTime)
			.alpha(show ? 1 : 0)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mSendStatusView.setVisibility(show ? View.VISIBLE
							: View.GONE);
				}
			});

			mSendFormView.setVisibility(View.VISIBLE);
			mSendFormView.animate().setDuration(shortAnimTime)
			.alpha(show ? 0 : 1)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mSendFormView.setVisibility(show ? View.GONE
							: View.VISIBLE);
				}
			});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mSendStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mSendFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
	
	/**
	 * Represents an asynchronous send note task.
	 */
	public class SendNotenTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... arg0) {
			return communicator.sendNote(receivers, availableString, expireString, locationID, title, content);
		}

		@Override
		protected void onPostExecute(final Integer code) {
			mTask = null;
			showProgress(false);

			if (code == 0) {
				Log.d("TIANYI", "Sent successfully.");
				finish();
			} else {
				Log.d("TIANYI", "Sent unsuccessfully.");
			}
		}

		@Override
		protected void onCancelled() {
			mTask = null;
			showProgress(false);
		}
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