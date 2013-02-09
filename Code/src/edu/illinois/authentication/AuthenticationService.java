package edu.illinois.authentication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Service to handle Account authentication. It instantiates the authenticator
 * and returns its IBinder.
 */
public class AuthenticationService extends Service {

	private AccountAuthenticator mAccountAuthenticator;
	
	@Override
	public void onCreate() {
		this.mAccountAuthenticator = new AccountAuthenticator(this);
	};
	
	@Override
	public IBinder onBind(Intent intent) {
		return mAccountAuthenticator.getIBinder();
	}
}