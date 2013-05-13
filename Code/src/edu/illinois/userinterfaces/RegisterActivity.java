package edu.illinois.userinterfaces;

import edu.illinois.communication.Communicator;
import edu.illinois.digitalstickynotes.R;
import edu.illinois.digitalstickynotes.TheApplication;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;

public class RegisterActivity extends Activity {

	private Communicator communicator;
	
	public final static String INTENT_KEY_EMAIL = "RESULT_EMAIL";
	public final static String INTENT_KEY_PW = "RESULT_PW";
	
	/**
	 * Keep track of the register task to ensure we can cancel it if requested.
	 */
	private UserRegisterTask mRegisterTask = null;
	
	private String mEmail;
	private String mPassword;
	private String mPassword2;
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
		
		// Set the communicator.
		this.communicator = ((TheApplication)(this.getApplication())).getCommunicator();
		
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
		if (mRegisterTask != null) {
			return;
		}
		
		// Reset errors.
		mEmailView.setError(null);
		mPasswordView1.setError(null);
		mPasswordView2.setError(null);
		mFirstNameView.setError(null);
		mLastNameView.setError(null);
		mUsernameView.setError(null);
		
		// Store values at the time of the register attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView1.getText().toString();
		mPassword2 = mPasswordView2.getText().toString();
		mFirstName = mFirstNameView.getText().toString();
		mLastName = mLastNameView.getText().toString();
		mUsername = mUsernameView.getText().toString();
		
		boolean cancel = false;
		View focusView = null;
		
		// Check for validity.
		if (TextUtils.isEmpty(mPassword2)) {
			mPasswordView2.setError(getString(R.string.error_field_required));
			focusView = mPasswordView2;
			cancel = true;
		} else if (mPassword2.equals(mPassword) == false) {
			mPasswordView2.setError(getString(R.string.error_passwords_no_match));
			focusView = mPasswordView2;
			cancel = true;
		}
		
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView1.setError(getString(R.string.error_field_required));
			focusView = mPasswordView1;
			cancel = true;
		} else if (mPassword.length() < LoginActivity.PASSWORD_MIN_LEN) {
			mPasswordView1.setError(getString(R.string.error_short_password));
			focusView = mPasswordView1;
			cancel = true;
		} else if (mPassword.length() > LoginActivity.PASSWORD_MAX_LEN) {
			mPasswordView1.setError(getString(R.string.error_long_password));
			focusView = mPasswordView1;
			cancel = true;
		}
		
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}
		
		if (TextUtils.isEmpty(mLastName)) {
			mLastNameView.setError(getString(R.string.error_field_required));
			focusView = mLastNameView;
			cancel = true;
		}
		
		if (TextUtils.isEmpty(mFirstName)) {
			mFirstNameView.setError(getString(R.string.error_field_required));
			focusView = mFirstNameView;
			cancel = true;
		}
		
		if (TextUtils.isEmpty(mUsername)) {
			mUsernameView.setError(getString(R.string.error_field_required));
			focusView = mUsernameView;
			cancel = true;
		}
		
		if (cancel) {
			// There was an error; don't attempt register and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user register attempt.
			mRegisterStatusMessageView.setText(R.string.login_progress_register);
			showProgress(true);
			mRegisterTask = new UserRegisterTask();
			mRegisterTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mRegisterStatusView.setVisibility(View.VISIBLE);
			mRegisterStatusView.animate().setDuration(shortAnimTime)
			.alpha(show ? 1 : 0)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mRegisterStatusView.setVisibility(show ? View.VISIBLE
							: View.GONE);
				}
			});

			mRegisterFormView.setVisibility(View.VISIBLE);
			mRegisterFormView.animate().setDuration(shortAnimTime)
			.alpha(show ? 0 : 1)
			.setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					mRegisterFormView.setVisibility(show ? View.GONE
							: View.VISIBLE);
				}
			});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mRegisterStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
	
	/**
	 * Represents an asynchronous registration task.
	 */
	public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

		private int errorCode;

		@Override
		protected Boolean doInBackground(Void... params) {
			// Attempt register.
			errorCode = communicator.tryRegister(mEmail, mPassword,
					mFirstName, mLastName, mUsername);
			return errorCode == 0;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mRegisterTask = null;
			showProgress(false);

			if (success) {
				final Intent intent = new Intent();
				intent.putExtra(INTENT_KEY_EMAIL, mEmail);
				intent.putExtra(INTENT_KEY_PW, mPassword);
				setResult(RESULT_OK, intent);
				Log.d("TIANYI", "Registered successfully.");
				finish();
			} else {
				if (errorCode == 1) {
					mEmailView.setError("Email is already used by someone else.");
					mEmailView.requestFocus();
				} else if (errorCode == 2) {
					mUsernameView.setError("Username is already used by someone else.");
					mUsernameView.requestFocus();
				}
			}
		}

		@Override
		protected void onCancelled() {
			mRegisterTask = null;
			showProgress(false);
		}
	}
}