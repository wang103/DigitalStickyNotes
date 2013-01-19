package edu.illinois.classinterfaces;

import android.content.IntentFilter;
import edu.illinois.digitalstickynotes.MainActivity;

/**
 * @author tianyiw
 */
public abstract class ConnectionManager {
	protected IntentFilter intentFilter;

	abstract public boolean startDiscovery();
	
	abstract public boolean stopDiscovery();
	
	abstract public void unregisterBroadcastReceiver(MainActivity activity);
	
	abstract public boolean connectionEnabled();

	public ConnectionManager() {
		this.intentFilter = new IntentFilter();
	}
}