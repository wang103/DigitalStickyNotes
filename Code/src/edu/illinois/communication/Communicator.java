package edu.illinois.communication;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

import edu.illinois.classinterfaces.ConnectionManager;
import edu.illinois.digitalstickynotes.MainActivity;
import edu.illinois.utils.Utils;

public class Communicator {
	
	private static final String urlAddress = "http://tianyiwang.info/project/handle_requests.php";
	
	private HttpClient httpClient;
	private HttpPost httpPost;
	
	private Activity activity;
	private ConnectionManager connectionManager;
	
	private boolean tryAuthenticateWithLocalServer(String email, String password) {
		
		JSONObject jInputObject = new JSONObject();
		
		try {
			jInputObject.put("request_name", "authenticate");
			jInputObject.put("email", email);
			jInputObject.put("pwd", password);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		List<String> result = connectionManager.talkToServers(jInputObject.toString(), true);
		boolean success = false;

		if (result.size() > 0) {
			JSONObject jOutputObject = null;
			try {
				jOutputObject = new JSONObject(result.get(0));
				success = jOutputObject.getBoolean("success");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return success;
	}
	
	private boolean tryAuthenticateWithGlobalServer(String email, String password) {
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("request_name", "authenticate"));
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("pwd", password));
		
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpPost);
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
		boolean success = false;
		try {
			jsonObject = new JSONObject(result);
			success = jsonObject.getBoolean("success");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return success;
	}
	
	/**
	 * First try to authenticate the user credential with the global server, if
	 * Internet connection is not available, try to authenticate with the local
	 * server.
	 * 
	 * @param email user's email address.
	 * @param password user's password.
	 * @return whether or not user's credential is authenticated.
	 */
	public boolean tryAuthenticate(String email, String password) {
		
		if (Utils.isNetworkConnected(activity)) {
			Log.d("TIANYI", "Using global server to authenticate.");
			return tryAuthenticateWithGlobalServer(email, password);
		}
		else if (this.connectionManager != null) {
			Log.d("TIANYI", "Using local server to authenticate.");
			return tryAuthenticateWithLocalServer(email, password);
		}
		
		Log.d("TIANYI", "Can't authenticate. No Network available.");
		return false;
	}
	
	public Communicator(Activity activity) {
		httpClient = new DefaultHttpClient();
		httpPost = new HttpPost(urlAddress);
		
		this.activity = activity;
		this.connectionManager = MainActivity.connectionManager;
	}
}