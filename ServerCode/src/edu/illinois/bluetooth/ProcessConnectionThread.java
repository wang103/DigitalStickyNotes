package edu.illinois.bluetooth;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.StreamConnection;

public class ProcessConnectionThread implements Runnable {

	final static private int MAX_MSG_LENGTH = 1024;
	
	private StreamConnection streamConnection;
	
	public ProcessConnectionThread(StreamConnection streamConnection) {
		this.streamConnection = streamConnection;
	}

	@Override
	public void run() {
		try {
			InputStream inputStream = streamConnection.openInputStream();
			
			byte message[] = new byte[MAX_MSG_LENGTH];
			inputStream.read(message);
			
			System.out.println(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}