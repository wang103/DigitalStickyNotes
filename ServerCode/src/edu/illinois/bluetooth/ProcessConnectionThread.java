package edu.illinois.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
			OutputStream outputStream = streamConnection.openOutputStream();
			
			// First get identification information from the client, then send
			// out client's notes if there is any.
			byte[] inputBuffer = new byte[MAX_MSG_LENGTH];
			int bytes;		// bytes read from inputStream
			bytes = inputStream.read(inputBuffer);
			String inputMessage = new String(inputBuffer);
			
			System.out.println(bytes + " bytes of client ID information: " + inputMessage);
			
			String outputMessage = "Hello from server.\n";
			byte[] outputBuffer = outputMessage.getBytes();
			outputStream.write(outputBuffer);
			
			// Done with this connection, clean up.
			outputStream.close();
			inputStream.close();
			streamConnection.close();
			System.out.println("Client disconnected.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}