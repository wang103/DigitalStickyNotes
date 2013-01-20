package edu.illinois.bluetooth;

import java.io.IOException;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class WaitConnectionThread implements Runnable {

	final private UUID uuid = new UUID("1101", true);
	final private String name = "DigitalStickyNotes Server";
	final private String url = "btspp://localhost:" + uuid + ";name=" + name;
	
	@Override
	public void run() {
		// Start waiting for connections from devices.
		LocalDevice localBluetoothDevice = null;
		
		StreamConnectionNotifier connectionNotifier = null;
		StreamConnection streamConnection = null;
		
		// Setup server for listening.
		try {
			localBluetoothDevice = LocalDevice.getLocalDevice();
			localBluetoothDevice.setDiscoverable(DiscoveryAgent.GIAC);
			
			System.out.println("Start advertising service.");
			connectionNotifier = (StreamConnectionNotifier) Connector.open(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Wait for connections.
		while (true) {
			try {
				System.out.println("Waiting for clients...");
				streamConnection = connectionNotifier.acceptAndOpen();
				System.out.println("Client connected!");
				
				Thread processConnectionThread = new Thread(new ProcessConnectionThread(streamConnection));
				processConnectionThread.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}