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

import edu.illinois.classinterfaces.ConnectionManager;
import edu.illinois.digitalstickynotes.MainActivity;

public class Communicator {
	
	private static final String urlAddress = "http://tianyiwang.info/dsn/handle_requests.php";
	
	private HttpClient httpClient;
	private HttpPost httpPost;
	
	@SuppressWarnings("unused")			//TODO: remove this.
	private ConnectionManager connectionManager;
	
	public boolean tryAuthenticate(String email, String password) {
		//TODO: now only try to authenticate against the main database, need to add
		// support for authentication against local server.
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
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

		//TODO: look at response.
		
		return true;
	}
	
	public Communicator() {
		httpClient = new DefaultHttpClient();
		httpPost = new HttpPost(urlAddress);
		
		this.connectionManager = MainActivity.connectionManager;
	}
}