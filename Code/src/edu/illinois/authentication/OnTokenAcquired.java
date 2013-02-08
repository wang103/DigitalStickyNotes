package edu.illinois.authentication;

import java.io.IOException;

import edu.illinois.digitalstickynotes.MainActivity;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class OnTokenAcquired implements AccountManagerCallback<Bundle> {

	private Activity activity;
	
	public OnTokenAcquired(Activity activity) {
		this.activity = activity;
	}

	@Override
	public void run(AccountManagerFuture<Bundle> result) {
		
		// Get the result of the operation from the AccountManagerFuture.
		Bundle bundle = null;
		try {
			bundle = result.getResult();
		} catch (OperationCanceledException e) {
			e.printStackTrace();
		} catch (AuthenticatorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Intent intent = (Intent) bundle.get(AccountManager.KEY_INTENT);
		if (intent != null) {
			activity.startActivityForResult(intent, MainActivity.ACTIVITY_CODE_TOKEN);
			return;
		}
		
		String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
	}
}