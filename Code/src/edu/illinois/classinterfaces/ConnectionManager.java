package edu.illinois.classinterfaces;

import java.util.List;

import android.app.Activity;
import android.content.IntentFilter;

/**
 * Manage a connection, which could be Bluetooth or WIFI Direct.
 * 
 * @author tianyiw
 */
public abstract class ConnectionManager {
	
	protected IntentFilter intentFilter;
	
	/**
	 * Start to discover devices nearby.
	 * 
	 * @return true on success, false on error.
	 */
	abstract public boolean startDiscovery();
	
	/**
	 * Stop the discovery of devices.
	 * 
	 * @param seconds after how many seconds before stopping discovery.
	 * @return true on success, false on error.
	 */
	abstract public boolean stopDiscovery(int seconds);
	
	/**
	 * Check whether or not the connection is enabled.
	 * 
	 * @return true if connection is enabled, false otherwise.
	 */
	abstract public boolean connectionEnabled();
	
	/**
	 * This method cleans up the connection.
	 * 
	 * @param activity the {@link Activity} object.
	 */
	abstract public void destroy(Activity activity);

	/**
	 * Send message to the discovered nearby devices/local servers.
	 * 
	 * @param s the message to send.
	 * @param talkToOneServer true if only send to one arbitrary local server, false if send to all local servers.
	 * @param startDiscovery start the discovery process before sending the message.
	 * @return a list of responses from local servers.
	 */
	abstract public List<String> talkToServers(String s, boolean talkToOneServer, boolean startDiscovery);
	
	/**
	 * Constructor.
	 */
	public ConnectionManager() {
		this.intentFilter = new IntentFilter();
	}
}