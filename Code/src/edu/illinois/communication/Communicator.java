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

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import edu.illinois.authentication.OnError;
import edu.illinois.authentication.OnTokenAcquired;
import edu.illinois.classinterfaces.ConnectionManager;
import edu.illinois.digitalstickynotes.MainActivity;
import edu.illinois.utils.Utils;

public class Communicator {
	
	private static final String urlAddress = "http://tianyiwang.info/project/handle_requests.php";
	
	private AccountManager am;
	
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
		
		List<String> result = connectionManager.talkToServers(jInputObject.toString(), true, true);
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
		
		am = AccountManager.get(activity);
		Bundle options = new Bundle();
		
		Account[] accounts = am.getAccountsByType("edu.illinois.digitalstickynotes");
		Account account = accounts[0];
		
		am.invalidateAuthToken("all", null);		//TODO: fix token here.
		am.getAuthToken(account, "all", options, activity, new OnTokenAcquired(activity), new Handler(new OnError()));
		
		////////////////////////////////////////////////
		
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
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
		nameValuePairs.add(new BasicNameValuePair("request_name", "register"));
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("pwd", password));
		nameValuePairs.add(new BasicNameValuePair("firstname", firstName));
		nameValuePairs.add(new BasicNameValuePair("lastname", lastName));
		nameValuePairs.add(new BasicNameValuePair("username", username));
		
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
	 * @return 0 for success. 1 if email is used. 2 if username is used. 3 for other reasons.
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
		httpClient = new DefaultHttpClient();
		httpPost = new HttpPost(urlAddress);
		
		this.activity = activity;
		this.connectionManager = MainActivity.connectionManager;
	}
}