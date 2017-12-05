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

import com.example.suc.suc_android_solution.Enumerations.MailType;
import com.example.suc.suc_android_solution.Enumerations.UserRoles;
import com.example.suc.suc_android_solution.Models.MailParams;
import com.example.suc.suc_android_solution.Models.User;
import com.example.suc.suc_android_solution.Services.EmailService;
import com.example.suc.suc_android_solution.Services.UserService;

public class SignUpActivity extends AppCompatActivity {

    Button mSignUpButton;
    EditText mUserName;
    EditText mUserSurname;
    EditText mUserMail;
    EditText mUserPass;
    EditText mUserAlias;

    String passwordStr = "";

    UserService userService;
    EmailService emailService;
    private View form;
    private View mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userService = new UserService(getBaseContext());
        emailService = new EmailService(getBaseContext());

        mSignUpButton = (Button) findViewById(R.id.user_sign_up_button);
        mUserName = (EditText) findViewById(R.id.user_name);
        mUserSurname = (EditText) findViewById(R.id.user_surname) ;
        mUserMail = (EditText) findViewById(R.id.user_mail) ;
        mUserPass = (EditText) findViewById(R.id.user_password) ;
        mUserAlias = (EditText) findViewById(R.id.user_alias) ;
        mProgressView = (View) findViewById(R.id.loading_progress);
        form = (View) findViewById(R.id.user_sign_up_form);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateFields()){
                    registerUser();
                }
            }
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.nav_toolbar);
        ((TextView)myToolbar.findViewById(R.id.toolbar_title)).setText(R.string.title_activity_sign_up);
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

    private boolean validateFields() {
        Boolean fieldsOk = true;
        if(!isEmailValid(mUserMail.getText().toString())){
            mUserMail.setError(getString(R.string.error_invalid_mail));
            fieldsOk = false;
        }

        if(!isPasswordValid(mUserPass.getText().toString())){
            mUserPass.setError(getString(R.string.error_invalid_password));
            fieldsOk = false;
        }

        if(!fieldHasContent(mUserName.getText().toString())){
            mUserName.setError(getString(R.string.error_field_required));
            fieldsOk = false;
        }

        if(!fieldHasContent(mUserSurname.getText().toString())){
            mUserSurname.setError(getString(R.string.error_field_required));
            fieldsOk = false;
        }

        if(!fieldHasContent(mUserAlias.getText().toString())){
            mUserAlias.setError(getString(R.string.error_field_required));
            fieldsOk = false;
        }

        return fieldsOk;

    }

    private boolean isEmailValid(String email) {
        Boolean isValidMail = false;
        String[] emailParts = email.split("@");
        if(emailParts.length == 2){
            if(emailParts[0].length() > 0 && emailParts[1].length() > 0 ){
                isValidMail = true;
            }
        }

        return isValidMail;
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }

    private boolean fieldHasContent(String fieldValue) {
        return fieldValue.length() > 0;
    }

    private void registerUser(){

        showProgress(true);
        String[] parameters = new String[5];
        parameters[0] = mUserAlias.getText().toString();
        parameters[1] = mUserName.getText().toString();
        parameters[2] = mUserSurname.getText().toString();
        parameters[3] = mUserMail.getText().toString();
        parameters[4] = mUserPass.getText().toString();

        passwordStr = parameters[4];

        new SignUpActivity.PostUserTask().execute(parameters);
    }

    public class PostUserTask extends AsyncTask<String, Void, User> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected User doInBackground(String... params) {

            try {
                User newUser = new User.Builder()
                        .setUserRole(UserRoles.COLABORATOR)
                        .setAlias(params[0])
                        .setName(params[1])
                        .setSurname(params[2])
                        .setMail(params[3])
                        .setPass(params[4]).build();

                User registeredUser = userService.postUser(newUser);

                if(registeredUser != null){
                    MailParams mailParams = new MailParams();
                    mailParams.setDestination_email(params[3]);
                    mailParams.setUserName(params[3]);
                    mailParams.setPassword(params[4]);
                    mailParams.setMailType(MailType.NO_VALIDATABLE_REGISTRATION.getValue());
                    emailService.sendNoValidatableRegistrationMail(mailParams);
                }


                return registeredUser;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(User userRegistered) {
            Intent intent = getIntent();
            if(userRegistered != null){
                intent.putExtra("username", userRegistered.getAlias());
                intent.putExtra("password", passwordStr);
                setResult(RESULT_OK, intent);
                finish();

            }else{
                Toast.makeText(getApplicationContext(),"Hubo un error", Toast.LENGTH_SHORT).show();
            }
            showProgress(false);
        }
    }

}
