package com.example.suc.suc_android_solution.Tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.suc.suc_android_solution.Models.UserDiner;
import com.example.suc.suc_android_solution.Services.UsersDinersService;

import java.math.BigInteger;

/**
 * Created by efridman on 8/11/17.
 */

public class UserDinerUnFollowTask extends AsyncTask<String, Void, Boolean> {

    UsersDinersService usersDinersService;
    Context mContext;

    TaskListener taskListener;

    public UserDinerUnFollowTask(Context context) {
        mContext = context;
        usersDinersService = new UsersDinersService(context);
    }

    public void setTaskListener(TaskListener taskListenerImpl) {
        taskListener = taskListenerImpl;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {

        try {
            Boolean response = usersDinersService.deleteUserDiner(new BigInteger(params[0]));

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Boolean userDiner) {
        taskListener.onComplete(userDiner);
    }
}