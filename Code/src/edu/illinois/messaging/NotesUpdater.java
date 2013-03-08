package edu.illinois.messaging;

import java.util.List;

import edu.illinois.communication.Communicator;
import edu.illinois.digitalstickynotes.MainActivity;
import android.os.Handler;
import android.util.Log;

/**
 * @author tianyiw
 */
public class NotesUpdater implements Runnable {

	private MainActivity mainActivity;
	private Communicator communicator;
	
	private int updateInterval = 60000;			// 60 seconds by default.
	private Handler updateHandler;

	private void updateMessages() {
		
		if (communicator == null) {
			return;
		}
		
		List<Note> notes = communicator.getNotes(mainActivity.getToken());
		
		Log.d("TIANYI", "Received " + notes.size() + " notes.");
		
		for (Note note : notes) {
			mainActivity.databaseManager.insertNote(note);
		}
		
		//TODO: notify the view to update.
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
	
	public NotesUpdater(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
		this.communicator = MainActivity.communicator;

		this.updateHandler = new Handler();
	}
}