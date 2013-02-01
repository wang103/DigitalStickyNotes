package edu.illinois.classinterfaces;

import android.content.IntentFilter;
import edu.illinois.database.DatabaseAccessObj;
import edu.illinois.digitalstickynotes.MainActivity;
import edu.illinois.messaging.Message;

/**
 * @author tianyiw
 */
public abstract class ConnectionManager {
	
	protected IntentFilter intentFilter;
	protected DatabaseAccessObj databaseAccessObj;	//TODO: need to initialize this.
	
	abstract public boolean startDiscovery();
	
	abstract public boolean stopDiscovery();
	
	abstract public void unregisterBroadcastReceiver(MainActivity activity);
	
	abstract public boolean connectionEnabled();

	abstract public String talkToServers(String s, boolean talkToOneServer);
	
	protected void insertMessage(Message message) {
		// Insert to the database.
		databaseAccessObj.insertMessage(message);
	}
	
	public ConnectionManager() {
		this.intentFilter = new IntentFilter();
	}
}