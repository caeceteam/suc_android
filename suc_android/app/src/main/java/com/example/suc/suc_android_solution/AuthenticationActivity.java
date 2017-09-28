package com.example.suc.suc_android_solution;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suc.suc_android_solution.Enumerations.UserRoles;
import com.example.suc.suc_android_solution.Models.Authentication.AuthCredentials;
import com.example.suc.suc_android_solution.Models.Authentication.AuthenticationResponse;
import com.example.suc.suc_android_solution.Services.AuthenticationService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

/**
 * A login screen that offers login via email/password.
 */
public class AuthenticationActivity extends AccountAuthenticatorActivity implements LoaderCallbacks<Cursor> {

    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private AuthenticationTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accountManager = AccountManager.get(getBaseContext());
        Account[] accounts = accountManager.getAccountsByType(AuthConfig.KEY_ACCOUNT_TYPE.getConfig());
        if (accounts.length > 0) {
            Intent intent = new Intent(getApplicationContext(), SucMain.class);
            startActivity(intent);
        }

        setContentView(R.layout.activity_authentication);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new AuthenticationTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        return email.length() > 0;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
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
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(AuthenticationActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class AuthenticationTask extends AsyncTask<Void, Void, Intent> {

        private final String mEmail;
        private final String mPassword;

        AuthenticationTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Intent doInBackground(Void... params) {
            Bundle data = new Bundle();
            try {
                AuthCredentials credentials = new AuthCredentials(mEmail, mPassword);
                AuthenticationResponse authResponse = new AuthenticationService().authenticate(credentials);
                data.putString(AccountManager.KEY_ACCOUNT_NAME, mEmail);
                data.putString(AccountManager.KEY_ACCOUNT_TYPE, UserRoles.COLABORATOR.getRole());
                data.putString(AccountManager.KEY_AUTHTOKEN, authResponse.getToken());
                data.putString(AuthConfig.PARAM_USER_PASS.getConfig(), mPassword);
            } catch (Exception ex) {
                data.putString(AuthConfig.KEY_ERROR_MESSAGE.getConfig(), ex.getMessage());
            }

            final Intent res = new Intent();
            res.putExtras(data);
            return res;
        }

        @Override
        protected void onPostExecute(final Intent intent) {
            if (intent.hasExtra(AuthConfig.KEY_ERROR_MESSAGE.getConfig())) {
                Toast.makeText(getBaseContext(), intent.getStringExtra(AuthConfig.KEY_ERROR_MESSAGE.getConfig()), Toast.LENGTH_SHORT).show();
            } else {
                if (finishLogin(intent)) {
                    Intent mainIntent = new Intent(getBaseContext(), UserActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getBaseContext(), "Uy la cagamos", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private boolean finishLogin(Intent intent) {
        Log.d("suc", TAG + "> finishLogin");

        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(AuthConfig.PARAM_USER_PASS.getConfig());
        final Account account = new Account(accountName, AuthConfig.KEY_ACCOUNT_TYPE.getConfig());
        Log.d("sucapp", TAG + "> finishLogin > addAccountExplicitly");
        String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
        String authtokenType = intent.getStringExtra(AuthConfig.KEY_ACCOUNT_TYPE.getConfig());

        // Creating the account on the device and setting the auth token we got
        // (Not setting the auth token will cause another call to the server to authenticate the user)
        boolean added = accountManager.addAccountExplicitly(account, accountPassword, null);
        accountManager.setAuthToken(account, authtokenType, authtoken);

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        return added;
    }
}

