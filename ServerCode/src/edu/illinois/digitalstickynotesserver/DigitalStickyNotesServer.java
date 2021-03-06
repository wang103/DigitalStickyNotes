package edu.illinois.digitalstickynotesserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import edu.illinois.bluetooth.WaitConnectionThread;
import edu.illinois.utils.Utils;

/**
 * DigitalStickyNotesServer -- the local server that can interact with mobile
 * devices. Can represent location, and communicate with the central server on
 * behalf of the mobile device.
 * 
 * @author tianyiw
 */
public class DigitalStickyNotesServer {

	public static final String BASE_URL = "http://tianyiwang.info/project";
	public static final String AUTH_URL = BASE_URL + "/request_token.php";
	public static final String REG_URL = BASE_URL + "/register.php";
	public static final String API_URL = BASE_URL + "/handle_requests.php";
	public static final String LOC_URL = BASE_URL + "/check_local.php";
	
	public static String locationID = null;
	public static String locationPassword = null;
	public static String locationName = null;
	
	/**
	 * Check the local server's credentials
	 * 
	 * @return 0 if authorized. -1 if failed.
	 */
	private static int checkLocalServerCredentials() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.println("Please type in local server's location ID:");
		try {
			locationID = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Please type in local server's location password:");
		try {
			locationPassword = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Clear the console for security purpose.
		try {
			Runtime.getRuntime().exec("clear");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Authorizing...\n");
		
		List<NameValuePair> data = new ArrayList<NameValuePair>(2);
		data.add(new BasicNameValuePair("location_id", locationID));
		data.add(new BasicNameValuePair("server_pwd", locationPassword));
		
		String response = Utils.sendPostToGlobalServer(data, 3);
		
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(response);
			
			boolean success = (boolean) jsonObj.get("success");
			
			if (!success) {
				return -1;
			}
			
			locationName = (String) jsonObj.get("name");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	/**
	 * The main method.
	 * 
	 * @param args the arguments.
	 */
	public static void main(String[] args) {
		if (checkLocalServerCredentials() != 0) {
			System.out.println("Incorrect credentials. Exiting...");
			return;
		}
		
		System.out.println("Location server: " + locationName);
		
		Thread waitConnectionThread = new Thread(new WaitConnectionThread());
		waitConnectionThread.start();
	}
}