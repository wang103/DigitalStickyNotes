package edu.illinois.messaging;

import java.util.List;

import edu.illinois.classinterfaces.ConnectionManager;
import edu.illinois.digitalstickynotes.MainActivity;
import android.os.Handler;
import android.util.Log;

/**
 * @author tianyiw
 */
public class MessagesUpdater implements Runnable {

	final private int DISCOVERY_LENGTH = 12;	// 12 seconds.
	
	private ConnectionManager connectionManager;
	
	private int updateInterval = 60000;			// 60 seconds by default.
	private Handler updateHandler;
	
	private void updateMessages() {
		
		// For every 'updateInterval' milliseconds, we start to discover
		// devices for 'DISCOVERY_LENGTH' seconds. Then communicate with each
		// devices discovered.
		
		boolean status;
		
		List<String> response = connectionManager.talkToServers("Hello", false);
		Log.d("TIANYI", response.size() + " local server(s) responsed.");
		
		status = connectionManager.startDiscovery();
		
		Log.d("TIANYI", "Discovery started. Status: " + status);

		try {
			Thread.sleep(DISCOVERY_LENGTH * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		status = connectionManager.stopDiscovery();
		
		Log.d("TIANYI", "Discovery finished. Status: " + status);
	}
	
	@Override
	public void run() {
		updateMessages();
		
		updateHandler.postDelayed(this, updateInterval);
	}
	
	public void start() {
		updateHandler.postDelayed(this, 0);
	}
	
	public void terminate() {
		updateHandler.removeCallbacks(this);
	}
	
	public MessagesUpdater() {
		this.updateHandler = new Handler();
		
		this.connectionManager = MainActivity.connectionManager;
	}
}