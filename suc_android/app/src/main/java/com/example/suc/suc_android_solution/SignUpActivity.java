package com.example.suc.suc_android_solution;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.suc.suc_android_solution.Enumerations.MailType;
import com.example.suc.suc_android_solution.Enumerations.UserRoles;
import com.example.suc.suc_android_solution.Models.MailParams;
import com.example.suc.suc_android_solution.Models.User;
import com.example.suc.suc_android_solution.Services.EmailService;
import com.example.suc.suc_android_solution.Services.UserService;

import java.util.Collection;

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

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.nav_toolbar);
        myToolbar.setTitle(R.string.title_activity_sign_up);
    }

    private void registerUser(){

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
            if(userRegistered != null){
                Intent intent = getIntent();
                intent.putExtra("username", userRegistered.getName());
                intent.putExtra("password", passwordStr);
                setResult(RESULT_OK, intent);
                finish();

            }else{
                Toast.makeText(getApplicationContext(),"Hubo un error", Toast.LENGTH_SHORT);
            }
        }
    }

}
