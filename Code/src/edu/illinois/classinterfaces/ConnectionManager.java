package edu.illinois.classinterfaces;

import java.util.List;

import android.content.IntentFilter;
import edu.illinois.digitalstickynotes.MainActivity;

/**
 * @author tianyiw
 */
public abstract class ConnectionManager {
	
	protected IntentFilter intentFilter;
	
	abstract public boolean startDiscovery();
	
	abstract public boolean startDiscoveryAndWait();
	
	abstract public boolean stopDiscovery();
	
	abstract public void unregisterBroadcastReceiver(MainActivity activity);
	
	abstract public boolean connectionEnabled();

	abstract public List<String> talkToServers(String s, boolean talkToOneServer, boolean startDiscovery);
	
	public ConnectionManager() {
		this.intentFilter = new IntentFilter();
	}
}