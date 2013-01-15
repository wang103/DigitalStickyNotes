package edu.illinois.digitalstickynotes;

import edu.illinois.bluetooth.BluetoothManager;
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
	
	private boolean isWifiP2pEnabled = false;
	private boolean isBTEnabled = false;
	
	public static int CODE_REQUEST_ENABLE_BT = 1;
	
	public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
		this.isWifiP2pEnabled = isWifiP2pEnabled;
		
		// Notify the user.
		TextView textView = (TextView) findViewById(R.id.show_message);
		if (isWifiP2pEnabled) {
			textView.setText("WIFI Direct is enabled!");
		} else {
			textView.setText("WIFI Direct is not enabled!");
		}
	}
	
	public void setIsBTEnabled(boolean isBTEnabled) {
		this.isBTEnabled = isBTEnabled;
		
		// Notify the user.
		TextView textView = (TextView) findViewById(R.id.show_message);
		if (isBTEnabled) {
			textView.setText("Bluetooth is enabled!");
		} else {
			textView.setText("Bluetooth is not enabled!");
		}
	}
	
	/**
	 * First try WIFI DIRECT. If it's not supported/enabled, ask for user's
	 * permission to use Bluetooth.
	 */
	private void setupConnection() {
		@SuppressWarnings("unused")		//TODO: delete
		WifiDirectManager connectionManager = new WifiDirectManager(this);
		
		if (this.isWifiP2pEnabled) {
			//TODO: do more work with WIFI Direct???
		} else {
			// Try to enable Bluetooth.
			@SuppressWarnings("unused")	//TODO: delete
			BluetoothManager btManager = new BluetoothManager(this);
		}
		
		//TODO: notify user if both WIFI Direct and Bluetooth are disabled.
		// User must enable one of them.
		if (this.isWifiP2pEnabled == false && this.isBTEnabled == false) {
			//...
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setupConnection();
		
		// Now switch activity to show all messages.
		@SuppressWarnings("unused")
		Intent intent = new Intent(this, ShowMessagesActivity.class);
		//TODO: startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CODE_REQUEST_ENABLE_BT) {
			if (resultCode == RESULT_OK) {
				setIsBTEnabled(true);
			} else {
				setIsBTEnabled(false);
			}
		}
	};
	
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