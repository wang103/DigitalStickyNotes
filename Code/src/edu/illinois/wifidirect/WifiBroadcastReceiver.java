package edu.illinois.wifidirect;

import edu.illinois.digitalstickynotes.MainActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;

/**
 * Receiver for WIFI Direct broadcast.
 * 
 * @author tianyiw
 */
public class WifiBroadcastReceiver extends BroadcastReceiver {

	private WifiP2pManager mManager;
	private Channel mChannel;
	private MainActivity activity;
	private PeerList peerDevicesListener;

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
			// Determine if WIFI Direct mode is enabled or not, alert the
			// Activity.
			int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
			if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
				activity.setIsWifiP2pEnabled(true);
			} else {
				activity.setIsWifiP2pEnabled(false);
			}
		}
		else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
			// The peer list has changed, update the list.
			if (mManager != null) {
				mManager.requestPeers(mChannel, peerDevicesListener);
			}
		}
		else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
			// Connection state has changed.
			if (mManager == null) {
				return;
			}

			NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
			if (networkInfo.isConnected()) {
				// Connected to the peer requested before.
				mManager.requestConnectionInfo(mChannel, new PeerCommunicator());
				//TODO: implement this, we need a way to maintain the socket built by PeerCommunicator.
			}
		}
		else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
			// Device configuration has changed.
			// TODO: Implementation.

		}
	}

	/**
	 * Constructor.
	 * 
	 * @param mManager the {@link WifiP2pManager} object.
	 * @param mChannel the {@link Channel} object.
	 * @param activity the {@link MainActivity} object.
	 * @param peersDevicesListener the {@link PeerList} object.
	 */
	public WifiBroadcastReceiver(WifiP2pManager mManager, Channel mChannel,
			MainActivity activity,
			PeerList peersDevicesListener) {
		super();

		this.mManager = mManager;
		this.mChannel = mChannel;
		this.activity = activity;
		this.peerDevicesListener = peersDevicesListener;
	}
}