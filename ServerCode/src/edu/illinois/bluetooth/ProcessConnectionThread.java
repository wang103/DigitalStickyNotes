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

import edu.illinois.digitalstickynotesserver.DigitalStickyNotesServer;
import edu.illinois.utils.Utils;

/**
 * A thread that process each incoming Bluetooth connection by completing
 * the client's request.
 * 
 * @author tianyiw
 */
public class ProcessConnectionThread implements Runnable {

	final static private int MAX_MSG_LENGTH = 1024;
	
	private StreamConnection streamConnection;
	
	/**
	 * Constructor.
	 * 
	 * @param streamConnection the {@link StreamConnection} object.
	 */
	public ProcessConnectionThread(StreamConnection streamConnection) {
		this.streamConnection = streamConnection;
	}
	
	/**
	 * Handle getting user's information.
	 * 
	 * @param jsonObj the {@link JSONObject} object.
	 * @param outputStream the {@link OutputStream} object.
	 */
	private void handleGetUsrInfo(JSONObject jsonObj, OutputStream outputStream) {

		String token = (String) jsonObj.get("token");

		List<NameValuePair> data = new ArrayList<NameValuePair>(2);
		data.add(new BasicNameValuePair("request_name", "get_usr_info"));
		data.add(new BasicNameValuePair("oauth_token", token));
		
		String response = Utils.sendPostToGlobalServer(data, 2);
		
		System.out.println("Response from the global server: " + response);
		
		// Send the result back to the client.
		byte[] outputBuffer = response.getBytes();
		try {
			outputStream.write(outputBuffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Handle getting user's notes.
	 * 
	 * @param jsonObj the {@link JSONObject} object.
	 * @param outputStream the {@link OutputStream} object.
	 */
	private void handleGetNotes(JSONObject jsonObj, OutputStream outputStream) {
		
		String token = (String) jsonObj.get("token");
		String location_id = DigitalStickyNotesServer.locationID;
		String location_pwd = DigitalStickyNotesServer.locationPassword;
		
		List<NameValuePair> data = new ArrayList<NameValuePair>(4);
		data.add(new BasicNameValuePair("request_name", "get_notes"));
		data.add(new BasicNameValuePair("oauth_token", token));
		data.add(new BasicNameValuePair("location_id", location_id));
		data.add(new BasicNameValuePair("server_pwd", location_pwd));
		
		String response = Utils.sendPostToGlobalServer(data, 2);
		
		System.out.println("Response from the global server: " + response);
		
		// Send the result back to the client.
		byte[] outputBuffer = response.getBytes();
		try {
			outputStream.write(outputBuffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Handle user account authentication.
	 * 
	 * @param jsonObj the {@link JSONObject} object.
	 * @param outputStream the {@link OutputStream} object.
	 */
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
	
	/**
	 * Handle user account registration.
	 * 
	 * @param jsonObj the {@link JSONObject} object.
	 * @param outputStream the {@link OutputStream} object.
	 */
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
				}
				else if (request.equals("register")) {
					handleRegistration(jsonObj, outputStream);
				}
				else if (request.equals("get_notes")) {
					handleGetNotes(jsonObj, outputStream);
				}
				else if (request.equals("get_usr_info")) {
					handleGetUsrInfo(jsonObj, outputStream);
				}
				else {
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