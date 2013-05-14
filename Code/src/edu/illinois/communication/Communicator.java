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
import edu.illinois.messaging.Note;
import edu.illinois.userinterfaces.LoginActivity;
import edu.illinois.utils.Utils;

/**
 * This class is used for communicating with either the global server or the
 * local servers.
 * 
 * @author tianyiw
 */
public class Communicator {
	
	private static final String BASE_URL = "http://tianyiwang.info/project";
	private static final String AUTH_URL = BASE_URL + "/request_token.php";		// URL for authentication.
	private static final String REG_URL = BASE_URL + "/register.php";			// URL for registration.
	private static final String API_URL = BASE_URL + "/handle_requests.php";	// URL for requests.
	
	private Activity activity;
	private ConnectionManager connectionManager;
	
	/**
	 * First try to send the note via internet connection. If not available,
	 * try send the note using a local server.
	 * 
	 * @param receivers array of receivers' id of this note.
	 * @param availableTime when this note becomes available.
	 * @param expireTime when this note becomes expired.
	 * @param locationID the location this note is associated with.
	 * @param title the title.
	 * @param note the content of the note.
	 * @return 0 on success. -1 on fail.
	 */
	public int sendNote(String[] receivers, String availableTime, String expireTime,
			int locationID, String title, String note) {
		return -1;
	}
	
	/**
	 * Set user information with a local server.
	 * 
	 * @param user the {@link User} object.
	 * @param token the access token.
	 * @return 0 on success. -1 on fail.
	 */
	private int setUserInfoWithLocalServer(User user, String token) {

		JSONObject jInputObject = new JSONObject();

		try {
			jInputObject.put("request_name", "get_usr_info");
			jInputObject.put("token", token);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		List<String> result = connectionManager.talkToServers(jInputObject.toString(), true, true);
		
		if (result.size() > 0) {
			JSONObject jOutputObject = null;
			try {
				jOutputObject = new JSONObject(result.get(0));
				user.setUserName(jOutputObject.getString("username"));
				user.setFirstName(jOutputObject.getString("firstname"));
				user.setLastName(jOutputObject.getString("lastname"));
				return 0;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return -1;
	}

	/**
	 * Set user information with the global server.
	 * 
	 * @param user the {@link User} object.
	 * @param token the access token.
	 * @return 0 on success. -1 on fail.
	 */
	private int setUserInfoWithGlobalServer(User user, String token) {

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("request_name", "get_usr_info"));
		nameValuePairs.add(new BasicNameValuePair("oauth_token", token));

		HttpEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(nameValuePairs);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		final HttpPost post = new HttpPost(API_URL);
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

		if (response == null) {
			return -1;
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
		try {
			jsonObject = new JSONObject(result);
			user.setUserName(jsonObject.getString("username"));
			user.setFirstName(jsonObject.getString("firstname"));
			user.setLastName(jsonObject.getString("lastname"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return 0;
	}
	
	/**
	 * Set the user information.
	 * 
	 * @param user the {@link User} object.
	 * @param token the access token.
	 * @return 0 on success. -1 on fail.
	 */
	public int setUserInfo(User user, String token) {
		if (Utils.isNetworkConnected(activity)) {
			Log.d("TIANYI", "Using global server to get user info.");
			return setUserInfoWithGlobalServer(user, token);
		}
		else if (this.connectionManager != null) {
			Log.d("TIANYI", "Using local server to authenticate.");
			return setUserInfoWithLocalServer(user, token);
		}
		
		Log.d("TIANYI", "Can't get user info. No Network available.");		
		return -1;
	}
	
	/**
	 * Get user's notes of the location.
	 * 
	 * @param token the access token.
	 * @return a list of user's notes from that location.
	 */
	private List<Note> getNotesWithLocalServer(String token) {
	
		JSONObject jInputObject = new JSONObject();

		try {
			jInputObject.put("request_name", "get_notes");
			jInputObject.put("token", token);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		List<String> result = connectionManager.talkToServers(jInputObject.toString(), false, true);
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
					String availableTimeString = messageObj.getString("available_time");
					String expireTimeString = messageObj.getString("expire_time");
					String title = messageObj.getString("title");
					String message = messageObj.getString("message");
					String location = messageObj.getString("server_loc");
					String senderString = messageObj.getString("sender_id");

					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

					int msgID = Integer.parseInt(msgIdString);
					Date receivedTime = simpleDateFormat.parse(receivedTimeString);
					Date availableTime = simpleDateFormat.parse(availableTimeString);
					Date expireTime = simpleDateFormat.parse(expireTimeString);
					User sender = new User(senderString);
					
					Note newNote = new Note(msgID, title, message, receivedTime,
							availableTime, expireTime, location, sender);
					
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
	 * Try to get user's notes of the location if there is a nearby local
	 * server.
	 * 
	 * @param token the access token.
	 * @return a list of user's notes from that location. Null if connection is
	 * not initialized.
	 */
	public List<Note> getNotes(String token) {
		if (this.connectionManager != null) {
			Log.d("TIANYI", "Using local server to get notes.");
			return getNotesWithLocalServer(token);
		}
		
		Log.d("TIANYI", "Can't get notes. No local server available.");		
		return null;
	}

	/**
	 * Try to authenticate the user credential with a local server.
	 * 
	 * @param email user's email address.
	 * @param password user's password.
	 * @return access token, or null if failed.
	 */
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

	/**
	 * Try to authenticate the user credential with the global server.
	 * 
	 * @param email user's email address.
	 * @param password user's password.
	 * @return access token, or null if failed.
	 */
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

		if (response == null) {
			return null;
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
	
	/**
	 * Try to register the user credential with a local server.
	 * 
	 * @param email user's email address.
	 * @param password user's password.
	 * @param firstName user's first name.
	 * @param lastName user's last name.
	 * @param username user picked nickname for his/her account.
	 * @return 0 for success. 1 if email is used. 2 if username is used. 3 if no connection.
	 */
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

	/**
	 * Try to register the user credential with the global server.
	 * 
	 * @param email user's email address.
	 * @param password user's password.
	 * @param firstName user's first name.
	 * @param lastName user's last name.
	 * @param username user picked nickname for his/her account.
	 * @return 0 for success. 1 if email is used. 2 if username is used. 3 if no connection.
	 */
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
	
	/**
	 * Constructor.
	 * 
	 * @param activity the {@link Activity} object.
	 * @param connectionManager the {@link ConnectionManager} object.
	 */
	public Communicator(Activity activity, ConnectionManager connectionManager) {
		this.activity = activity;
		this.connectionManager = connectionManager;
	}
}