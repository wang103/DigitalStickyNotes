package edu.illinois.wifidirect;

import edu.illinois.digitalstickynotes.MainActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;

/**
 * @author tianyiw
 */
public class WifiBroadcastReceiver extends BroadcastReceiver {

	private MainActivity activity;
	
	public WifiBroadcastReceiver(MainActivity activity) {
		super();
		this.activity = activity;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		
		if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
			// Determine if Wifi Direct mode is enabled or not, alert the
			// Activity.
			int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
			if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
				activity.setIsWifiP2pEnabled(true);
			} else {
				activity.setIsWifiP2pEnabled(false);
			}
		}
		else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
			// The peer list has changed.
			
		}
		else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
			// Connection state has changed.
			
		}
		else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
			// Device configuration has changed.
			
		}
	}
}