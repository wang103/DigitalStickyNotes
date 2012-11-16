package edu.illinois.wifidirect;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;

/**
 * @author tianyiw
 */
public class WifiDirectManager {
	private final IntentFilter intentFilter = new IntentFilter();
	private WifiP2pManager mManager;
	private Channel mChannel;
	
	private void initIntentFilter() {
		// Indicates a change in the WiFi Peer-to-Peer status.
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

		// Indicates a change in the list of available peers.
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

		// Indicates the state of WiFi P2P connectivity has changed.
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

		// Indicates this device's details have changed.
		intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
	}
	
	private void initChannel(Activity activity) {
		mManager = (WifiP2pManager) activity.getSystemService(Context.WIFI_P2P_SERVICE);
		mChannel = mManager.initialize(activity, activity.getMainLooper(), null);
	}
	
	public WifiDirectManager(Activity activity) {
		super();
		initIntentFilter();
		initChannel(activity);
	}
}
