package edu.illinois.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import edu.illinois.digitalstickynotesserver.DigitalStickyNotesServer;

public class Utils {
	
	public static String sendPostToGlobalServer(String data) {
		
		if (data == null) {
			return null;
		}
		
		String response = null;
		
		try {
			URL url = new URL(DigitalStickyNotesServer.globalServerAddress);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
			
			writer.write(data);
			writer.flush();
			
			// Get the response.
			StringBuffer stringBuffer = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				stringBuffer.append(line);
			}
			
			reader.close();
			writer.close();
			
			response = stringBuffer.toString();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return response;
	}
}