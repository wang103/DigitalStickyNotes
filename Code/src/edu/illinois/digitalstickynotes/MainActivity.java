package edu.illinois.digitalstickynotes;

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
	
	@SuppressWarnings("unused")		//TODO: delete
	private boolean isWifiP2pEnabled = false;
	
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		@SuppressWarnings("unused")		//TODO: delete
		WifiDirectManager connectionManager = new WifiDirectManager(this);
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