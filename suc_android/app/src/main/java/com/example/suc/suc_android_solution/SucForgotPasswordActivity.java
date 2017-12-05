package com.example.suc.suc_android_solution;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suc.suc_android_solution.Models.Authentication.AuthCredentials;
import com.example.suc.suc_android_solution.Models.Authentication.AuthenticationResponse;
import com.example.suc.suc_android_solution.Services.AuthenticationService;

public class SucForgotPasswordActivity extends AppCompatActivity {

    EditText mUserMail;
    Button mRecoverPassButton;

    AuthenticationService authenticationService;
    private View form;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suc_forgot_password);

        authenticationService = new AuthenticationService(getApplicationContext());
        form = (View) findViewById(R.id.forgot_password_form);
        mProgressView = (View) findViewById(R.id.loading_progress);

        mUserMail = (EditText) findViewById(R.id.user_name);
        mRecoverPassButton = (Button) findViewById(R.id.user_forgot_password_button);
        mRecoverPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearPassword();
            }
        });


        Toolbar myToolbar = (Toolbar) findViewById(R.id.nav_toolbar);
        ((TextView) myToolbar.findViewById(R.id.toolbar_title)).setText(R.string.title_activity_forgot_password);
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

            form.setVisibility(show ? View.GONE : View.VISIBLE);
            form.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    form.setVisibility(show ? View.GONE : View.VISIBLE);
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
            form.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void clearPassword() {
        showProgress(true);
        String[] parameters = new String[5];
        parameters[0] = mUserMail.getText().toString();
        new SucForgotPasswordActivity.ClearPasswordTask().execute(parameters);
    }


    public class ClearPasswordTask extends AsyncTask<String, Void, AuthenticationResponse> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected AuthenticationResponse doInBackground(String... params) {

            try {
                AuthCredentials authCredentials = new AuthCredentials(params[0], "");


                AuthenticationResponse response = authenticationService.clearPassword(authCredentials);

                return response;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(AuthenticationResponse authResponse) {
            if (authResponse != null) {
                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                finish();

            } else {
                Toast.makeText(getApplicationContext(), "Hubo un error", Toast.LENGTH_SHORT);
            }
            showProgress(false);
        }
    }
}
