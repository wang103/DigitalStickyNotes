package edu.illinois.messaging;

import android.os.Handler;

/**
 * @author tianyiw
 */
public class MessagesUpdater implements Runnable {

	private int updateInterval = 5000;		// 5 seconds by default.
	private Handler updateHandler;
	
	private void updateMessages() {
		//TODO: implementation.
		
	}
	
	@Override
	public void run() {
		updateMessages();
		
		updateHandler.postDelayed(this, updateInterval);
	}
	
	public void terminate() {
		updateHandler.removeCallbacks(this);
	}
	
	public MessagesUpdater() {
		this.updateHandler = new Handler();
	}
}