package edu.illinois.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import edu.illinois.digitalstickynotesserver.DigitalStickyNotesServer;

/**
 * This class contains useful static methods.
 * 
 * @author tianyiw
 */
public class Utils {
	
	/**
	 * Send HTTP POST request to the central server.
	 * 
	 * @param data the data to be sent.
	 * @param dataType 0 indicates authentication data, 1 indicates register data,
	 * 				   2 indicates API request data, 3 indicates location data.
	 * @return the response from the central server.
	 */
	public static String sendPostToGlobalServer(List<NameValuePair> data, int dataType) {
		if (data == null) {
			return null;
		}
				
		String urlString = null;
		switch (dataType) {
		case 0:
			urlString = DigitalStickyNotesServer.AUTH_URL;
			break;
		case 1:
			urlString = DigitalStickyNotesServer.REG_URL;
			break;
		case 2:
			urlString = DigitalStickyNotesServer.API_URL;
			break;
		case 3:
			urlString = DigitalStickyNotesServer.LOC_URL;
			break;
		default:
			break;
		}
		
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(urlString);
		StringBuffer sb = new StringBuffer();
		try {
			post.setEntity(new UrlEncodedFormEntity(data));
			
			HttpResponse httpResponse = client.execute(post);
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			String line = null;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
}