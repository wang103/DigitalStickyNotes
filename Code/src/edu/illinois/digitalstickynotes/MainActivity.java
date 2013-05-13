package edu.illinois.digitalstickynotes;

import edu.illinois.bluetooth.BluetoothManager;
import edu.illinois.classinterfaces.ConnectionManager;
import edu.illinois.communication.Communicator;
import edu.illinois.messaging.NotesUpdater;
import edu.illinois.userinterfaces.ClientSetupActivity;
import edu.illinois.userinterfaces.LoginActivity;
import edu.illinois.userinterfaces.SendNoteActivity;
import edu.illinois.userinterfaces.ServerSetupActivity;
import edu.illinois.userinterfaces.ShowMessagesActivity;
import edu.illinois.wifidirect.WifiDirectManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	public ConnectionManager connectionManager;
	public NotesUpdater messagesUpdater;

	private boolean isWifiP2pEnabled = false;
	private boolean isBTEnabled = false;
	
	// For Activity result.
	public static final int ACTIVITY_CODE_SIGN_IN = 0;
	public static final int ACTIVITY_CODE_BLUETOOTH = 1;
	public static final int ACTIVITY_CODE_REGISTER = 2;

	// Preference settings.
	public static final String PREF_NAME = "edu.illinois.digitalstickynotes";
	public static final String PREF_TOKEN_KEY = "edu.illinois.digitalstickynotes.token";
	
	// Client's access token.
	private String token = null;
	
	/**
	 * Set the access token.
	 * 
	 * @param token the access token.
	 */
	public void setToken(String token) {
		this.token = token;
	}
	
	/**
	 * Get the access token.
	 * 
	 * @return the access token.
	 */
	public String getToken() {
		return token;
	}
	
	/**
	 * Set whether or not the Wifi Direct is enabled.
	 * 
	 * @param isWifiP2pEnabled true if Wifi Direct is enabled. Otherwise false.
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
	 * @param isBTEnabled true if Bluetooth is enabled. Otherwise false.
	 * @param doPost true to start post setup connection. Otherwise false.
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
	 * established to do more setup.
	 */
	private void postSetupConnection() {
		Communicator communicator = new Communicator(this, connectionManager);
		((TheApplication)(this.getApplication())).setCommunicator(communicator);
		
		//TODO: store the username too.
		// Check to see if the app has the access token, if not, start the
		// login activity to ask for user's credentials.
		SharedPreferences prefs = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		
		if (prefs.contains(PREF_TOKEN_KEY)) {
			setToken(prefs.getString(PREF_TOKEN_KEY, null));
			postSigningIn();
		} else {
			Intent loginIntent = new Intent(this, LoginActivity.class);
			startActivityForResult(loginIntent, ACTIVITY_CODE_SIGN_IN);
		}
	}

	/**
	 * Call this method after access token is acquired.
	 */
	private void postSigningIn() {
		startMessagesUpdater();
		switchViewToShowMessages();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACTIVITY_CODE_SIGN_IN) {
			if (resultCode == RESULT_OK) {
				// Store the access token in the preferences.
				String token = data.getStringExtra(LoginActivity.INTENT_KEY_TOKEN);
				
				SharedPreferences prefs = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
				prefs.edit().putString(PREF_TOKEN_KEY, token);
				
				setToken(token);
				
				postSigningIn();
			} else {
				TextView textView = (TextView) findViewById(R.id.show_message);
				textView.setText("Please sign in first (Client Settings).");
			}
		} else if (requestCode == ACTIVITY_CODE_BLUETOOTH) {
			if (resultCode != RESULT_OK) {
				setIsBTEnabled(false, false);
			}
		}
	};
	
	/**
	 * Start the periodic updater.
	 */
	private void startMessagesUpdater() {
		Log.d("TIANYI", "MessagesUpdater started.");
		messagesUpdater = new NotesUpdater(this, ((TheApplication)getApplication()).getCommunicator());
		messagesUpdater.start();
	}
	
	/**
	 * Stop the periodic updater.
	 */
	private void stopMessagesUpdater() {
		if (messagesUpdater != null) {
			messagesUpdater.terminate();
			messagesUpdater = null;
		}
	}

	private void switchViewToShowMessages() {
		Intent intent = new Intent(this, ShowMessagesActivity.class);
		startActivity(intent);
	}
	
	private void switchViewToSendNote() {
		Intent intent = new Intent(this, SendNoteActivity.class);
		startActivity(intent);
	}
	
	/**
	 * First try WIFI DIRECT. If it's not supported/enabled, ask for user's
	 * permission to use Bluetooth.
	 */
	private void setupConnection() {
		connectionManager = new WifiDirectManager(this);
		// For now, WIFI Direct is not supported. This may change in the future.
		// Fall back to BT right away.
		setIsWifiP2pEnabled(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		findViewById(R.id.my_notes_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						switchViewToShowMessages();
					}
		});
		
		findViewById(R.id.send_note_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						switchViewToSendNote();
					}
		});
		
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
}