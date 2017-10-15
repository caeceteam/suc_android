package com.example.suc.suc_android_solution;

import android.content.Intent;
import android.os.AsyncTask;
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
    Button mSignUpButton;

    AuthenticationService authenticationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suc_forgot_password);

        authenticationService = new AuthenticationService(getApplicationContext());

        mUserMail = (EditText) findViewById(R.id.user_name) ;
        mSignUpButton = (Button) findViewById(R.id.user_forgot_password_button);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearPassword();
            }
        });


        Toolbar myToolbar = (Toolbar) findViewById(R.id.nav_toolbar);
        ((TextView)myToolbar.findViewById(R.id.toolbar_title)).setText(R.string.title_activity_forgot_password);
    }

    private void clearPassword(){

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
                AuthCredentials authCredentials = new AuthCredentials(params[0],"");


                AuthenticationResponse response = authenticationService.clearPassword(authCredentials);

                return response;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(AuthenticationResponse authResponse) {
            if(authResponse != null){
                Intent intent = getIntent();
                setResult(RESULT_OK, intent);
                finish();

            }else{
                Toast.makeText(getApplicationContext(),"Hubo un error", Toast.LENGTH_SHORT);
            }
        }
    }
}
