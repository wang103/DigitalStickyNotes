package edu.illinois.digitalstickynotesserver;

import edu.illinois.bluetooth.WaitConnectionThread;

public class DigitalStickyNotesServer {

	public static final String BASE_URL = "http://tianyiwang.info/project";
	public static final String AUTH_URL = BASE_URL + "/request_token.php";
	public static final String REG_URL = BASE_URL + "/register.php";
	public static final String API_URL = BASE_URL + "/handle_requests.php";
	
	public static void main(String[] args) {
		Thread waitConnectionThread = new Thread(new WaitConnectionThread());
		waitConnectionThread.start();
	}
}