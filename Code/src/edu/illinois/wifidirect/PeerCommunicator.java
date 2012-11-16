package edu.illinois.wifidirect;

import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;

/**
 * Allow interaction between two peers after the WIFI Direct connection is
 * established.
 */
public class PeerCommunicator implements ConnectionInfoListener {

	@Override
	public void onConnectionInfoAvailable(WifiP2pInfo info) {
		if (info.groupFormed) {
			//TODO: setup for server/client, then we can query for sticky notes.
		}
	}
}