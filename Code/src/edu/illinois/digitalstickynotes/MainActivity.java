package edu.illinois.digitalstickynotes;

import edu.illinois.bluetooth.BluetoothManager;
import edu.illinois.classinterfaces.ConnectionManager;
import edu.illinois.messaging.MessagesUpdater;
import edu.illinois.wifidirect.WifiDirectManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {

	public static String identificationTag = "default tag";
	
	private ConnectionManager connectionManager;
	
	private boolean isWifiP2pEnabled = false;
	private boolean isBTEnabled = false;
	
	public static int CODE_REQUEST_ENABLE_BT = 1;
	
	private MessagesUpdater messagesUpdater;
	
	public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
		this.isWifiP2pEnabled = isWifiP2pEnabled;
		
		// Notify the user.
		TextView textView = (TextView) findViewById(R.id.show_message);
		if (isWifiP2pEnabled) {
			textView.setText("WIFI Direct is enabled!");

			switchViewToShowMessages();
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
	
	public void setIsBTEnabled(boolean isBTEnabled, boolean switchView) {
		this.isBTEnabled = isBTEnabled;
		
		// Notify the user.
		TextView textView = (TextView) findViewById(R.id.show_message);
		if (isBTEnabled) {
			textView.setText("Bluetooth is enabled!");

			if (switchView) {
				switchViewToShowMessages();
			}
		}
		else {
			textView.setText("WifiDirect and Bluetooth are both not enabled!");
			
			connectionManager = null;
		}
	}
	
	private void startMessagesUpdater() {
		messagesUpdater = new MessagesUpdater(connectionManager);
		messagesUpdater.run();
	}
	
	private void stopMessagesUpdater() {
		messagesUpdater.terminate();
	}

	/**
	 * Call this message once connection is established.
	 */
	private void switchViewToShowMessages() {
		// Start the periodic updater.
		startMessagesUpdater();
		
		//TODO: implementation.
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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