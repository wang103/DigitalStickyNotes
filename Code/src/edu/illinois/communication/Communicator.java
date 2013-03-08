package edu.illinois.communication;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

import edu.illinois.classinterfaces.ConnectionManager;
import edu.illinois.data.User;
import edu.illinois.digitalstickynotes.MainActivity;
import edu.illinois.messaging.Note;
import edu.illinois.userinterfaces.LoginActivity;
import edu.illinois.utils.Utils;

public class Communicator {
	
	private static final String BASE_URL = "http://tianyiwang.info/project";
	private static final String AUTH_URL = BASE_URL + "/request_token.php";
	private static final String REG_URL = BASE_URL + "/register.php";
	//TODO: private static final String API_URL = BASE_URL + "/handle_requests.php";
	
	private Activity activity;
	private ConnectionManager connectionManager;
	
	private List<Note> getNotesWithLocalServer(String token) {
	
		JSONObject jInputObject = new JSONObject();

		try {
			jInputObject.put("request_name", "get_notes");
			jInputObject.put("token", token);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		List<String> result = connectionManager.talkToServers(jInputObject.toString(), false, false);
		List<Note> notes = new ArrayList<Note>();

		if (result.size() > 0) {
			JSONObject jOutputObject = null;
			try {
				jOutputObject = new JSONObject(result.get(0));
				int numMessages = jOutputObject.getInt("num_msg");
				JSONArray messageArray = jOutputObject.getJSONArray("messages");
				for (int i = 0; i < numMessages; i++) {
					JSONObject messageObj = messageArray.getJSONObject(i);
					
					String msgIdString = messageObj.getString("message_id");
					String receivedTimeString = messageObj.getString("received_time");
					String availableTimeString = messageObj.getString("availble_time");
					String expireTimeString = messageObj.getString("expire_time");
					String title = messageObj.getString("title");
					String message = messageObj.getString("message");
					String senderString = messageObj.getString("sender_id");
					
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
					
					int msgID = Integer.parseInt(msgIdString);
					Date receivedTime = simpleDateFormat.parse(receivedTimeString);
					Date availableTime = simpleDateFormat.parse(availableTimeString);
					Date expireTime = simpleDateFormat.parse(expireTimeString);
					User sender = new User(senderString);
					
					Note newNote = new Note(msgID, title, message, receivedTime,
							availableTime, expireTime, receivedTimeString,
							availableTimeString, expireTimeString, sender);
					
					notes.add(newNote);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return notes;
	}
	
	/**
	 * Try to get user's notes if there is a nearby local server.
	 * 
	 * @param token the access token.
	 * @return a list of user's notes.
	 */
	public List<Note> getNotes(String token) {
		if (this.connectionManager != null) {
			Log.d("TIANYI", "Using local server to get notes.");
			return getNotesWithLocalServer(token);
		}
		
		Log.d("TIANYI", "Can't get notes. No local server available.");		
		return null;
	}
	
	private String tryAuthenticateWithLocalServer(String email, String password) {

		JSONObject jInputObject = new JSONObject();

		try {
			jInputObject.put("request_name", "authenticate");
			jInputObject.put("grant_type", "password");
			jInputObject.put("email", email);
			jInputObject.put("pwd", password);
			jInputObject.put("client_id", LoginActivity.CLIENT_ID);
			jInputObject.put("client_secret", LoginActivity.CLIENT_SECRET);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	
		List<String> result = connectionManager.talkToServers(jInputObject.toString(), true, true);
		String token = null;

		if (result.size() > 0) {
			JSONObject jOutputObject = null;
			try {
				jOutputObject = new JSONObject(result.get(0));
				token = jOutputObject.getString("access_token");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return token;
	}

	private String tryAuthenticateWithGlobalServer(String email, String password) {
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
		nameValuePairs.add(new BasicNameValuePair("grant_type", "password"));
		nameValuePairs.add(new BasicNameValuePair("username", email));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		nameValuePairs.add(new BasicNameValuePair("client_id", LoginActivity.CLIENT_ID));
		nameValuePairs.add(new BasicNameValuePair("client_secret", LoginActivity.CLIENT_SECRET));
	
		HttpEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(nameValuePairs);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		final HttpPost post = new HttpPost(AUTH_URL);
		post.addHeader(entity.getContentType());
		post.setEntity(entity);
		HttpResponse response = null;

		try {
			response = Utils.getHttpClient().execute(post);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String result = null;
		try {
			result = Utils.inputStreamToString(response.getEntity().getContent());
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONObject jsonObject = null;
		String token = null;
		try {
			jsonObject = new JSONObject(result);
			token = jsonObject.getString("access_token");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return token;
	}
	
	/**
	 * First try to authenticate the user credential with the global server, if
	 * Internet connection is not available, try to authenticate with the local
	 * server.
	 * 
	 * @param email user's email address.
	 * @param password user's password.
	 * @return access token, or null if failed.
	 */
	public String tryAuthenticate(String email, String password) {

		if (Utils.isNetworkConnected(activity)) {
			Log.d("TIANYI", "Using global server to authenticate.");
			return tryAuthenticateWithGlobalServer(email, password);
		}
		else if (this.connectionManager != null) {
			Log.d("TIANYI", "Using local server to authenticate.");
			return tryAuthenticateWithLocalServer(email, password);
		}
		
		Log.d("TIANYI", "Can't authenticate. No Network available.");
		return null;
	}
	
	private int tryRegisterWithLocalServer(String email, String password,
			String firstName, String lastName, String username) {

		JSONObject jInputObject = new JSONObject();
		
		try {
			jInputObject.put("request_name", "register");
			jInputObject.put("email", email);
			jInputObject.put("pwd", password);
			jInputObject.put("firstname", firstName);
			jInputObject.put("lastname", lastName);
			jInputObject.put("username", username);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		List<String> result = connectionManager.talkToServers(jInputObject.toString(), true, true);
		int code = 3;

		if (result.size() > 0) {
			JSONObject jOutputObject = null;
			try {
				jOutputObject = new JSONObject(result.get(0));
				code = jOutputObject.getInt("code");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return code;
	}

	private int tryRegisterWithGlobalServer(String email, String password,
			String firstName, String lastName, String username) {
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("pwd", password));
		nameValuePairs.add(new BasicNameValuePair("firstname", firstName));
		nameValuePairs.add(new BasicNameValuePair("lastname", lastName));
		nameValuePairs.add(new BasicNameValuePair("username", username));
		
		HttpEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(nameValuePairs);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		final HttpPost post = new HttpPost(REG_URL);
		post.addHeader(entity.getContentType());
		post.setEntity(entity);
		HttpResponse response = null;
		
		try {
			response = Utils.getHttpClient().execute(post);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String result = null;
		try {
			result = Utils.inputStreamToString(response.getEntity().getContent());
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONObject jsonObject = null;
		int code = 3;
		try {
			jsonObject = new JSONObject(result);
			code = jsonObject.getInt("code");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return code;
	}
	
	/**
	 * First try to register the user credential with the global server, if
	 * Internet connection is not available, try to register with the local
	 * server.
	 * 
	 * @param email user's email address.
	 * @param password user's password.
	 * @param firstName user's first name.
	 * @param lastName user's last name.
	 * @param username user picked nickname for his/her account.
	 * @return 0 for success. 1 if email is used. 2 if username is used. 3 if no connection.
	 */
	public int tryRegister(String email, String password, String firstName,
			String lastName, String username) {
		
		if (Utils.isNetworkConnected(activity)) {
			Log.d("TIANYI", "Using global server to register.");
			return tryRegisterWithGlobalServer(email, password, firstName, lastName, username);
		}
		else if (this.connectionManager != null) {
			Log.d("TIANYI", "Using local server to register.");
			return tryRegisterWithLocalServer(email, password, firstName, lastName, username);
		}
		
		Log.d("TIANYI", "Can't authenticate. No Network available.");		
		return 3;
	}
	
	public Communicator(Activity activity) {
		this.activity = activity;
		this.connectionManager = MainActivity.connectionManager;
	}
}