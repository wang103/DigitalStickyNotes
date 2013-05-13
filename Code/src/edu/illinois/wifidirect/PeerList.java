package edu.illinois.wifidirect;

import java.util.ArrayList;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;

/**
 * The listener for WIFI Direct Peer List.
 * 
 * @author tianyiw
 */
public class PeerList implements PeerListListener {

	private ArrayList<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
	
	@Override
	public void onPeersAvailable(WifiP2pDeviceList peerList) {
		peers.clear();
		peers.addAll(peerList.getDeviceList());
		
		//TODO: nofify the WifiDirectManager to auto-connect the peers.
	}
	
	/**
	 * Clear all the peers currently in the list.
	 */
	public void clearAll() {
		peers.clear();
	}
}