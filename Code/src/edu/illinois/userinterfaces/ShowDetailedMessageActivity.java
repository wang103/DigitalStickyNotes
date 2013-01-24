package edu.illinois.userinterfaces;

import edu.illinois.digitalstickynotes.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class ShowDetailedMessageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_detailed_message);
		
		Bundle extras = getIntent().getExtras();
		
		String title = extras.getString("Title");
		String message = extras.getString("Message");
		String sender = extras.getString("Sender");
		String availableDate = extras.getString("AvailableTime");
		String receivedDate = extras.getString("ReceivedTime");
		String expireDate = extras.getString("ExpireTime");
		
		TextView messageTextView = (TextView) findViewById(R.id.message_content);
		TextView availableTextView = (TextView) findViewById(R.id.available_time);
		TextView receivedTextView = (TextView) findViewById(R.id.received_time);
		TextView expireTextView = (TextView) findViewById(R.id.expire_time);
		TextView senderTextView = (TextView) findViewById(R.id.sender);
		
		setTitle(title);
		messageTextView.setText("Note: " + message);
		availableTextView.setText("Available Time: " + availableDate);
		receivedTextView.setText("Received Time: " + receivedDate);
		expireTextView.setText("Expire Time: " + expireDate);
		senderTextView.setText("Sender: " + sender);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_show_detailed_message, menu);
		return true;
	}
}