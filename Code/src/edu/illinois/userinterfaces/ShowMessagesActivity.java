package edu.illinois.userinterfaces;

import java.util.Date;
import java.util.List;

import edu.illinois.data.User;
import edu.illinois.database.DatabaseAccessObj;
import edu.illinois.digitalstickynotes.R;
import edu.illinois.messaging.Message;
import android.os.Bundle;
import android.app.ListActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;

public class ShowMessagesActivity extends ListActivity {

	private DatabaseAccessObj databaseAccessObj;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_messages);
		
		databaseAccessObj = new DatabaseAccessObj(this);
		databaseAccessObj.open();
		
		//TODO: delete this.
		Message message = new Message(0, "Hello", "Hello, world", new Date(), new Date(), new Date(), new User("Tianyi", null));
		databaseAccessObj.insertMessage(message);
		
		Message message2 = new Message(1, "Announciment", "Ticket is $50", new Date(), new Date(), new Date(), new User("Tianyi", null));
		databaseAccessObj.insertMessage(message2);
		
		List<Message> messages = databaseAccessObj.getAllAvailableMessages();
		
		// Use the SipleCursorAdapter to show the messages in this ListView.
		ArrayAdapter<Message> adapter = new ArrayAdapter<Message>(this,
				android.R.layout.simple_list_item_1, messages);
		setListAdapter(adapter);
		
		Log.d("TIANYI", "ShowMessages List Activity creation done with " + messages.size() + " messages.");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		databaseAccessObj.close();
	}
	
	@Override
	protected void onResume() {
		databaseAccessObj.open();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		databaseAccessObj.close();
		super.onPause();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_show_messages, menu);
		return true;
	}
}