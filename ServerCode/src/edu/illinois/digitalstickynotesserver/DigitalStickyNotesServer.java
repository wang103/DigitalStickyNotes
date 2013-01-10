package edu.illinois.digitalstickynotesserver;

import edu.illinois.bluetooth.WaitConnectionThread;

public class DigitalStickyNotesServer {

	public static void main(String[] args) {
		Thread waitConnectionThread = new Thread(new WaitConnectionThread());
		waitConnectionThread.start();
	}
}