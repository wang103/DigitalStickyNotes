package edu.illinois.classinterfaces;

import java.util.List;

import android.content.IntentFilter;
import edu.illinois.database.DatabaseAccessObj;
import edu.illinois.digitalstickynotes.MainActivity;
import edu.illinois.messaging.Note;

/**
 * @author tianyiw
 */
public abstract class ConnectionManager {
	
	protected IntentFilter intentFilter;
	protected DatabaseAccessObj databaseAccessObj;	//TODO: need to initialize this.
	
	abstract public boolean startDiscovery();
	
	abstract public boolean startDiscoveryAndWait();
	
	abstract public boolean stopDiscovery();
	
	abstract public void unregisterBroadcastReceiver(MainActivity activity);
	
	abstract public boolean connectionEnabled();

	abstract public List<String> talkToServers(String s, boolean talkToOneServer, boolean startDiscovery);
	
	protected void insertMessage(Note message) {
		// Insert to the database.
		databaseAccessObj.insertNote(message);
	}
	
	public ConnectionManager() {
		this.intentFilter = new IntentFilter();
	}
}