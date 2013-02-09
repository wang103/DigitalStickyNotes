package edu.illinois.authentication;

import edu.illinois.digitalstickynotes.MainActivity;
import edu.illinois.userinterfaces.LoginActivity;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * This class is an implementation of AbstractAccountAuthenticator for
 * authenticating accounts in the tianyiwang.info/project domain. This class
 * uses authTokens as part of the authentication process. In the account setup
 * UI, the user enters their username and password. But for the subsequent
 * calls off to the service for syncing, authtoken is used instead. When
 * getAuthToken() is called, need to return the appropriate authToken for the
 * specified account. If we already have an authToken stored in the account, we
 * return that authToken. If we don't, but we do have a username and password,
 * then we'll attempt to talk to the sample service to fetch an authToken. If
 * that fails (or we didn't have a username/password), then we need to prompt
 * the user - so we create an AuthenticatorActivity intent and return that.
 * That will display the dialog that prompts the user for their login
 * information.
 */
public class AccountAuthenticator extends AbstractAccountAuthenticator {

	/**
     * Account type string.
     */
    public static final String ACCOUNT_TYPE = "DigitalStickyNotesAccount";

    /**
     * Authtoken type string. This one type grants all kinds of access.
     */
    public static final String AUTHTOKEN_TYPE = "DigitalStickyNotesAccount";
    
	// Authentication Service context
    private final Context mContext;
    
	public AccountAuthenticator(Context context) {
		super(context);
		this.mContext = context;
	}

	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response,
			String accountType, String authTokenType,
			String[] requiredFeatures, Bundle options)
			throws NetworkErrorException {

		final Intent intent = new Intent(mContext, LoginActivity.class);
		intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
		final Bundle bundle = new Bundle();
		bundle.putParcelable(AccountManager.KEY_INTENT, intent);
		return bundle;
	}

	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse response,
			Account account, Bundle options) throws NetworkErrorException {
		return null;
	}

	@Override
	public Bundle editProperties(AccountAuthenticatorResponse response,
			String accountType) {
		return null;
	}

	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException {
		
		// If the caller requested an authToken type we don't support, then
		// return an error.
		if (!authTokenType.equals(AUTHTOKEN_TYPE)) {
			final Bundle result = new Bundle();
			result.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType");
			return result;
		}

		// Extract the authToken from the Account Manager, and ask the server
		// to refresh it.
		final AccountManager am = AccountManager.get(mContext);
		final String token = am.getPassword(account);
		if (token != null) {
			final String authToken = MainActivity.communicator.tryRefreshToken(token);
			am.setPassword(account, authToken);
			if (!TextUtils.isEmpty(authToken)) {
				final Bundle result = new Bundle();
				result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
				result.putString(AccountManager.KEY_ACCOUNT_TYPE, ACCOUNT_TYPE);
				result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
				return result;
			}
		}

		// If we get here, then we couldn't access the user's token - so we
		// need to re-prompt them for their credentials. We do that by creating
		// an intent to display our AuthenticatorActivity panel.
		final Intent intent = new Intent(mContext, LoginActivity.class);
		intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
		final Bundle bundle = new Bundle();
		bundle.putParcelable(AccountManager.KEY_INTENT, intent);
		return bundle;
	}

	@Override
	public String getAuthTokenLabel(String authTokenType) {
		// null means we don't support multiple authToken types.
		return null;
	}

	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse response,
			Account account, String[] features) throws NetworkErrorException {

		// This call is used to query whether the Authenticator supports
		// specific features. We don't expect to get called, so we always
		// return false (no) for any queries.
		final Bundle result = new Bundle();
		result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
		return result;
	}

	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException {
		return null;
	}
}