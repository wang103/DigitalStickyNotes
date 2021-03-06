package edu.illinois.digitalstickynotes;

import edu.illinois.bluetooth.BluetoothManager;
import edu.illinois.classinterfaces.ConnectionManager;
import edu.illinois.communication.Communicator;
import edu.illinois.data.UserInformation;
import edu.illinois.messaging.NotesUpdater;
import edu.illinois.userinterfaces.ClientSetupActivity;
import edu.illinois.userinterfaces.LoginActivity;
import edu.illinois.userinterfaces.SendNoteActivity;
import edu.illinois.userinterfaces.ServerSetupActivity;
import edu.illinois.userinterfaces.ShowMessagesActivity;
import edu.illinois.wifidirect.WifiDirectManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private ConnectionManager connectionManager;
	private NotesUpdater notesUpdater;
	private UserInformation userInfo;

	// For application states.
	private boolean isWifiP2pEnabled = false;
	private boolean isBTEnabled = false;

	// For Activity result.
	public static final int ACTIVITY_CODE_SIGN_IN = 0;
	public static final int ACTIVITY_CODE_BLUETOOTH = 1;
	public static final int ACTIVITY_CODE_REGISTER = 2;

	// Preference settings.
	public static final String PREF_NAME = "edu.illinois.digitalstickynotes";
	public static final String PREF_TOKEN_KEY = "edu.illinois.digitalstickynotes.token";
	public static final String PREF_USER_NAME = "edu.illinois.digitalstickynotes.username";
	public static final String PREF_FIRST_NAME = "edu.illinois.digitalstickynotes.firstname";
	public static final String PREF_LAST_NAME = "edu.illinois.digitalstickynotes.lastname";

	// UI references.
	private TextView signedInTextView;
	private Button myNotesButton;
	private Button sendNoteButton;
	private Button signInButton;
	private Button signOutButton;

	/**
	 * Set whether or not the Wifi Direct is enabled.
	 * 
	 * @param isWifiP2pEnabled true if Wifi Direct is enabled.
	 */
	public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
		this.isWifiP2pEnabled = isWifiP2pEnabled;

		// Notify the user.
		TextView textView = (TextView) findViewById(R.id.show_message);
		if (isWifiP2pEnabled) {
			textView.setText("WIFI Direct is enabled!");

			postSetupConnection();
		}
		else {
			textView.setText("WIFI Direct is not enabled!");

			// Unregister WIFI Direct's broadcast receiver.
			connectionManager.destroy(this);

			// Try to enable Bluetooth.
			connectionManager = new BluetoothManager(this);
			if (connectionManager.connectionEnabled()) {
				this.setIsBTEnabled(true, true);
			}
		}
	}

	/**
	 * Set whether or not the Bluetooth is enabled.
	 * 
	 * @param isBTEnabled true if Bluetooth is enabled.
	 * @param doPost true to start post setup connection.
	 */
	public void setIsBTEnabled(boolean isBTEnabled, boolean doPost) {
		this.isBTEnabled = isBTEnabled;

		// Notify the user.
		TextView textView = (TextView) findViewById(R.id.show_message);
		if (isBTEnabled) {
			textView.setText("Bluetooth is enabled!");

			if (doPost) {
				postSetupConnection();
			}
		}
		else {
			textView.setText("WifiDirect and Bluetooth are both not enabled! " +
					"Please enable at least one of them in the client settings.");

			connectionManager = null;
		}
	}

	/**
	 * Call this method after connection (WIFI Direct or Bluetooth) is
	 * established to do additional setup.
	 */
	private void postSetupConnection() {

		Communicator communicator = new Communicator(this, connectionManager);
		((TheApplication)(this.getApplication())).setCommunicator(communicator);

		// Check to see if the app has the access token, if not, start the
		// login activity to ask for user's credentials.
		SharedPreferences prefs = this.getSharedPreferences(PREF_NAME, 0);

		if (prefs.contains(PREF_TOKEN_KEY)) {
			setToken(prefs.getString(PREF_TOKEN_KEY, null));
			userInfo.getUser().setUserName(prefs.getString(PREF_USER_NAME, null));
			userInfo.getUser().setFirstName(prefs.getString(PREF_FIRST_NAME, null));
			userInfo.getUser().setLastName(prefs.getString(PREF_LAST_NAME, null));
			postSigningIn();
		} else {
			this.signInButton.setEnabled(true);
			this.signOutButton.setEnabled(false);
		}
	}

	/**
	 * Start the periodic updater.
	 */
	private void startMessagesUpdater() {
		Log.d("TIANYI", "MessagesUpdater started.");
		notesUpdater = new NotesUpdater(this, ((TheApplication)getApplication()).getCommunicator());
		notesUpdater.start();
	}

	/**
	 * Stop the periodic updater.
	 */
	private void stopMessagesUpdater() {
		if (notesUpdater != null) {
			notesUpdater.terminate();
			notesUpdater = null;
		}
	}

	/**
	 * Call this method after access token is acquired.
	 */
	private void postSigningIn() {
		this.signedInTextView.setText("You are signed in as: " + userInfo.getUser().getUserName());
		this.myNotesButton.setEnabled(true);
		this.sendNoteButton.setEnabled(true);
		this.signInButton.setEnabled(false);
		this.signOutButton.setEnabled(true);
		
		startMessagesUpdater();
	}

	/**
	 * Switch view to show user's notes.
	 */
	private void switchViewToShowNotes() {
		Intent intent = new Intent(this, ShowMessagesActivity.class);
		startActivity(intent);
	}

	/**
	 * Switch view to send user's note.
	 */
	private void switchViewToSendNote() {
		Intent intent = new Intent(this, SendNoteActivity.class);
		startActivity(intent);
	}
	
	/**
	 * Switch view to sign in.
	 */
	private void switchViewToSignIn() {
		Intent loginIntent = new Intent(this, LoginActivity.class);
		startActivityForResult(loginIntent, ACTIVITY_CODE_SIGN_IN);
	}

	/**
	 * First try WIFI DIRECT. If it's not supported/enabled, ask for user's
	 * permission to use Bluetooth.
	 */
	private void setupConnection() {
		connectionManager = new WifiDirectManager(this);
		// TODO: For now, WIFI Direct is not supported. This may change in the
		// future. Fall back to Bluetooth right away.
		setIsWifiP2pEnabled(false);
	}
	
	/**
	 * Sign out user's account.
	 */
	private void signOut() {
		userInfo.setAccessToken(null);
		
		SharedPreferences prefs = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.remove(PREF_TOKEN_KEY);
		editor.remove(PREF_USER_NAME);
		editor.remove(PREF_FIRST_NAME);
		editor.remove(PREF_LAST_NAME);
		editor.commit();
		
		signedInTextView.setText("");
		myNotesButton.setEnabled(false);
		sendNoteButton.setEnabled(false);
		signInButton.setEnabled(true);
		signOutButton.setEnabled(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		userInfo = new UserInformation();
		((TheApplication)(this.getApplication())).setUserInfo(userInfo);
		
		signedInTextView = (TextView) findViewById(R.id.signed_in_prompt);
		
		myNotesButton = (Button) findViewById(R.id.my_notes_button);
		myNotesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switchViewToShowNotes();
			}
		});
		
		// Can only go to my notes if signed in.
		myNotesButton.setEnabled(false);

		sendNoteButton = (Button) findViewById(R.id.send_note_button);
		sendNoteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switchViewToSendNote();
			}
		});
		
		// Can only send note if signed in.
		sendNoteButton.setEnabled(false);

		signInButton = (Button) findViewById(R.id.login_button);
		signInButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switchViewToSignIn();
			}
		});
		
		// Can only sign in if connection is established.
		signInButton.setEnabled(false);

		signOutButton = (Button) findViewById(R.id.logout_button);
		signOutButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				signOut();
			}
		});
		
		// Can only sign out if signed in.
		signOutButton.setEnabled(false);
		
		setupConnection();
		Log.d("TIANYI", "Connection setup done");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (isWifiP2pEnabled || isBTEnabled) {
			this.stopMessagesUpdater();
			connectionManager.destroy(this);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACTIVITY_CODE_SIGN_IN) {
			TextView textView = (TextView) findViewById(R.id.show_message);
			if (resultCode == RESULT_OK) {
				// Store the access token in the preferences.
				String token = data.getStringExtra(LoginActivity.INTENT_KEY_TOKEN);
				userInfo.setAccessToken(token);
				
				SharedPreferences prefs = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString(PREF_TOKEN_KEY, token);
				editor.putString(PREF_USER_NAME, userInfo.getUser().getUserName());
				editor.putString(PREF_FIRST_NAME, userInfo.getUser().getFirstName());
				editor.putString(PREF_LAST_NAME, userInfo.getUser().getLastName());
				
				editor.commit();
				
				postSigningIn();
				
				textView.setText("");
			} else {
				textView.setText("Please sign in first.");
			}
		}
		else if (requestCode == ACTIVITY_CODE_BLUETOOTH) {
			if (resultCode != RESULT_OK) {
				setIsBTEnabled(false, false);
				this.signInButton.setEnabled(false);
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.client_settings:
			intent = new Intent(this, ClientSetupActivity.class);
			startActivity(intent);
			return true;
		case R.id.server_settings:
			intent = new Intent(this, ServerSetupActivity.class);
			startActivity(intent);
			return true;
		case R.id.menu_settings:
			startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		// Confirm if user really want to exit the app.
		Builder alertBuilder = new AlertDialog.Builder(this);
		alertBuilder.setMessage("Do you want to exit the app?");
		alertBuilder.setCancelable(false);
		alertBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				MainActivity.this.finish();
			}
		});
		alertBuilder.setNegativeButton("NO", null);
		alertBuilder.show();
	}

	public void setToken(String token) {
		userInfo.setAccessToken(token);
	}

	public String getToken() {
		return userInfo.getAccessToken();
	}
}