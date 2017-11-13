package com.example.suc.suc_android_solution.Tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.suc.suc_android_solution.Models.UserDiner;
import com.example.suc.suc_android_solution.Models.UserDiners;
import com.example.suc.suc_android_solution.Services.UsersDinersService;

import java.math.BigInteger;

/**
 * Created by efridman on 13/11/17.
 */

public class GetUserDinersTask extends AsyncTask<String, Void, UserDiners> {

    UsersDinersService usersDinersService;
    Context mContext;

    TaskListener taskListener;

    public GetUserDinersTask(Context context) {
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
    protected UserDiners doInBackground(String... params) {

        try {
            UserDiners response = usersDinersService.getUserDiners();

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(UserDiners userDiners) {
        taskListener.onComplete(userDiners);
    }
}
