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
	
	private boolean tryAuthenticateWithLocalServer(List<NameValuePair> nameValuePairs) {
		
		return false;
	}
	
	private boolean tryAuthenticateWithGlobalServer(List<NameValuePair> nameValuePairs) {
		
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
	
	public boolean tryAuthenticate(String email, String password) {
		// First try to authenticate with the local server. If local server is
		// not available, try to authenticate with the global server.
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair("request_name", "authenticate"));
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("pwd", password));
		
		if (this.connectionManager != null) {
			return tryAuthenticateWithLocalServer(nameValuePairs);
		} else if (Utils.isNetworkConnected(activity)) {
			return tryAuthenticateWithGlobalServer(nameValuePairs);
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