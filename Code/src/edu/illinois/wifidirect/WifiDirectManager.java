package edu.illinois.wifidirect;

import java.util.List;

import edu.illinois.classinterfaces.ConnectionManager;
import edu.illinois.digitalstickynotes.MainActivity;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;

/**
 * Manage the WIFI Direct connections.
 * 
 * @author tianyiw
 */
public class WifiDirectManager extends ConnectionManager {
	
	private WifiP2pManager mManager;
	private Channel mChannel;
	private WifiBroadcastReceiver broadcastReceiver;
	private final PeerList peerDevicesListener = new PeerList();
	
	/**
	 * Call this method to start connecting to a peer. A callback function will
	 * be invoked when the connection is established.
	 * 
	 * @param peer the peer device to connect to.
	 */
	public void connectToPeer(WifiP2pDevice peer) {
		WifiP2pConfig config = new WifiP2pConfig();
		config.deviceAddress = peer.deviceAddress;
		config.wps.setup = WpsInfo.PBC;
		
		mManager.connect(mChannel, config, new ActionListener() {
			@Override
			public void onSuccess() {
				// Let WifiBroadcastReceiver notify us. Ignore it here.
			}
			
			@Override
			public void onFailure(int reason) {
				// TODO: notify someone.
			}
		});
	}
	
	/**
	 * Initiate peer discovery to start the discovery process. Discovery will
	 * remain active until a connection is initiated or a P2P group is formed.
	 */
	public void startOneDiscovery() {
		mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
			@Override
			public void onSuccess() {
				// Initiation successful. Do nothing.
			}
			
			@Override
			public void onFailure(int reason) {
				// TODO: notify the user.
			}
		});
	}
	
	/**
	 * Remove all peers and clear all fields. This is called when WifiBroadcastReceiver
	 * receives a state change event.
	 */
	public void resetData() {
		peerDevicesListener.clearAll();
	}

	@Override
	public boolean startDiscovery() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean startDiscoveryAndWait() {
		// TODO Auto-generated method stub
		return false;
	};
	
	@Override
	public boolean stopDiscovery() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean connectionEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Unregister the {@link BroadcastReceiver}.
	 * 
	 * @param activity the {@link Activity} object.
	 */
	private void unregisterBroadcastReceiver(Activity activity) {
		activity.unregisterReceiver(broadcastReceiver);
	}
	
	@Override
	public void destroy(Activity activity) {
		unregisterBroadcastReceiver(activity);
	};
	
	@Override
	public List<String> talkToServers(String s, boolean talkToOneServer, boolean startDiscovery) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Initialize the {@link BroadcastReceiver} for WIFI Direct connections.
	 * 
	 * @param activity the {@link MainActivity} object.
	 */
	private void initBroadcastReceiver(MainActivity activity) {
		broadcastReceiver = new WifiBroadcastReceiver(mManager, mChannel,
													  activity, peerDevicesListener);
		activity.registerReceiver(broadcastReceiver, intentFilter);
	}
	
	/**
	 * Initialize the Channel for WIFI Direct connections.
	 * 
	 * @param activity the {@link Activity} object.
	 */
	private void initChannel(Activity activity) {
		mManager = (WifiP2pManager) activity.getSystemService(Context.WIFI_P2P_SERVICE);
		mChannel = mManager.initialize(activity, activity.getMainLooper(), null);
	}

	/**
	 * Initialize the {@link IntentFilter} for WIFI Direct connections.
	 */
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
	
	/**
	 * Constructor.
	 * 
	 * @param activity the {@link MainActivity} object.
	 */
	public WifiDirectManager(MainActivity activity) {
		super();
		
		initIntentFilter();
		initChannel(activity);
		initBroadcastReceiver(activity);
	}
}