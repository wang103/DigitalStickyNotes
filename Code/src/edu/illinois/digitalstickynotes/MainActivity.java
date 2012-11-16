package edu.illinois.digitalstickynotes;

import edu.illinois.wifidirect.WifiDirectManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

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
}