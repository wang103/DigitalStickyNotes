package edu.illinois.digitalstickynotes;

import edu.illinois.bluetooth.BluetoothManager;
import edu.illinois.classinterfaces.ConnectionManager;
import edu.illinois.communication.Communicator;
import edu.illinois.messaging.MessagesUpdater;
import edu.illinois.userinterfaces.ClientSetupActivity;
import edu.illinois.userinterfaces.LoginActivity;
import edu.illinois.userinterfaces.ServerSetupActivity;
import edu.illinois.userinterfaces.ShowMessagesActivity;
import edu.illinois.wifidirect.WifiDirectManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {

	public static ConnectionManager connectionManager;
	public static Communicator communicator;
	public static MessagesUpdater messagesUpdater;

	private boolean isWifiP2pEnabled = false;
	private boolean isBTEnabled = false;
	
	public static final int ACTIVITY_CODE_SIGN_IN = 0;
	public static final int ACTIVITY_CODE_BLUETOOTH = 1;
	
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
			connectionManager.unregisterBroadcastReceiver(this);
			
			// Try to enable Bluetooth.
			connectionManager = new BluetoothManager(this);
			if (connectionManager.connectionEnabled()) {
				this.setIsBTEnabled(true, true);
			}
		}
	}

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

	private void postSetupConnection() {
		communicator = new Communicator(this);
		
		//TODO: Only do this if not log in already
		Intent loginIntent = new Intent(this, LoginActivity.class);
		startActivityForResult(loginIntent, ACTIVITY_CODE_SIGN_IN);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACTIVITY_CODE_SIGN_IN) {
			if (resultCode == RESULT_OK) {
				//TODO: uncomment below.
				//startMessagesUpdater();
				//switchViewToShowMessages();
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
		messagesUpdater = new MessagesUpdater();
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
		//TODO: fix.
		Intent intent = new Intent(this, ShowMessagesActivity.class);
		startActivity(intent);
	}
	
	/**
	 * First try WIFI DIRECT. If it's not supported/enabled, ask for user's
	 * permission to use Bluetooth.
	 */
	private void setupConnection() {
		connectionManager = new WifiDirectManager(this);
		//TODO: for now, WIFI Direct is not supported on my device.
		// Fall back to BT right away.
		setIsWifiP2pEnabled(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setupConnection();
		Log.d("TIANYI", "Connection setup done");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if (isWifiP2pEnabled || isBTEnabled) {
			this.stopMessagesUpdater();
			connectionManager.unregisterBroadcastReceiver(this);
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
			//TODO: implementation.
			startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}