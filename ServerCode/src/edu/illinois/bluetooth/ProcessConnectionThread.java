package edu.illinois.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.io.StreamConnection;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import edu.illinois.utils.Utils;

public class ProcessConnectionThread implements Runnable {

	final static private int MAX_MSG_LENGTH = 1024;
	
	private StreamConnection streamConnection;
	
	public ProcessConnectionThread(StreamConnection streamConnection) {
		this.streamConnection = streamConnection;
	}
	
	private void handleAuthenticate(JSONObject jsonObj, OutputStream outputStream) {
		
		String grantType = (String) jsonObj.get("grant_type");
		String email = (String) jsonObj.get("email");
		String pwd = (String) jsonObj.get("pwd");
		String clientID = (String) jsonObj.get("client_id");
		String clientSecret = (String) jsonObj.get("client_secret");
		
		List<NameValuePair> data = new ArrayList<NameValuePair>(5);
		data.add(new BasicNameValuePair("grant_type", grantType));
		data.add(new BasicNameValuePair("username", email));
		data.add(new BasicNameValuePair("password", pwd));
		data.add(new BasicNameValuePair("client_id", clientID));
		data.add(new BasicNameValuePair("client_secret", clientSecret));
		
		String response = Utils.sendPostToGlobalServer(data, 0);
		
		System.out.println("Response from the global server: " + response);
		
		// Send the result back to the client.
		byte[] outputBuffer = response.getBytes();
		try {
			outputStream.write(outputBuffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void handleRegistration(JSONObject jsonObj, OutputStream outputStream) {
		
		String email = (String) jsonObj.get("email");
		String pwd = (String) jsonObj.get("pwd");
		String firstName = (String) jsonObj.get("firstname");
		String lastName = (String) jsonObj.get("lastname");
		String username = (String) jsonObj.get("username");
		
		List<NameValuePair> data = new ArrayList<NameValuePair>(5);
		data.add(new BasicNameValuePair("email", email));
		data.add(new BasicNameValuePair("pwd", pwd));
		data.add(new BasicNameValuePair("firstname", firstName));
		data.add(new BasicNameValuePair("lastname", lastName));
		data.add(new BasicNameValuePair("username", username));
		
		String response = Utils.sendPostToGlobalServer(data, 1);
		
		System.out.println("Response from the global server: " + response);
		
		// Send the result back to the client.
		byte[] outputBuffer = response.getBytes();
		try {
			outputStream.write(outputBuffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		try {
			InputStream inputStream = streamConnection.openInputStream();
			OutputStream outputStream = streamConnection.openOutputStream();
			
			// First get client's message.
			
			byte[] inputBuffer = new byte[MAX_MSG_LENGTH];
			int bytes;		// bytes read from inputStream
			bytes = inputStream.read(inputBuffer);
			String inputMessage = new String(inputBuffer, 0, bytes);
			
			System.out.println("Received " + bytes + " bytes of client message: " + inputMessage);

			// Now handle client's request.
			try {
				JSONObject jsonObj = (JSONObject) new JSONParser().parse(inputMessage);
				
				String request = (String) jsonObj.get("request_name");
				
				if (request.equals("authenticate")) {
					handleAuthenticate(jsonObj, outputStream);
				} else if (request.equals("register")) {
					handleRegistration(jsonObj, outputStream);
				} else {
					System.out.println("Unsupported request from client!");
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			// Done with this connection, clean up.
			outputStream.close();
			inputStream.close();
			streamConnection.close();
			System.out.println("Client disconnected.");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}