package edu.illinois.userinterfaces;

import edu.illinois.digitalstickynotes.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.app.Activity;

public class RegisterActivity extends Activity {

	/**
	 * Keep track of the register task to ensure we can cancel it if requested.
	 */
	private UserRegisterTask mRegisterTask = null;
	
	private String mEmail;
	private String mPassword;
	private String mFirstName;
	private String mLastName;
	private String mUsername;
	
	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView1;
	private EditText mPasswordView2;
	private EditText mFirstNameView;
	private EditText mLastNameView;
	private EditText mUsernameView;
	
	private View mRegisterFormView;
	private View mRegisterStatusView;
	private TextView mRegisterStatusMessageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_register);
		
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Set up the register form.
		mEmailView = (EditText) findViewById(R.id.email_register);
		mPasswordView1 = (EditText) findViewById(R.id.password_register_1);
		mPasswordView2 = (EditText) findViewById(R.id.password_register_2);
		mFirstNameView = (EditText) findViewById(R.id.firstname_register);
		mLastNameView = (EditText) findViewById(R.id.lastname_register);
		mUsernameView = (EditText) findViewById(R.id.username_register);
	
		mRegisterFormView = findViewById(R.id.register_form);
		mRegisterStatusView = findViewById(R.id.register_status);
		mRegisterStatusMessageView = (TextView) findViewById(R.id.register_status_message);
	
		findViewById(R.id.submit_register_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						attempRegister();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_register, menu);
		return true;
	}
	
	/**
	 * Attempts to register the account specified by the register form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attempRegister() {
		//TODO:
	}
	
	/**
	 * Represents an asynchronous registration task.
	 */
	public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			//TODO:
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			//TODO:
		}

		@Override
		protected void onCancelled() {
			//TODO:
		}
	}
}