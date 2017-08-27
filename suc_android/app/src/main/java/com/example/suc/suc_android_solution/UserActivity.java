package com.example.suc.suc_android_solution;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.suc.suc_android_solution.Models.User;
import com.example.suc.suc_android_solution.Services.UserService;

import java.net.URL;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    TextView tvDisplayUsers;
    UserService userService = new UserService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        tvDisplayUsers = (TextView) findViewById(R.id.tv_display_users);
        new FetchUsersTask().execute();
    }

    public class FetchUsersTask extends AsyncTask<String, Void, List<User>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<User> doInBackground(String... params) {

            try {
                List<User> users = userService.getAllUsers();

                return users;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<User> usersData) {
            if (usersData != null) {
                for (User user:usersData
                     ) {
                    tvDisplayUsers.append(user.toString() + "/n");
                }
            }
        }
    }

}
