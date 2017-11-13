package com.example.suc.suc_android_solution.Tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.suc.suc_android_solution.Maps.MapMarkerViewModel;
import com.example.suc.suc_android_solution.Models.Diner;
import com.example.suc.suc_android_solution.Models.Diners;
import com.example.suc.suc_android_solution.Models.UserDiner;
import com.example.suc.suc_android_solution.R;
import com.example.suc.suc_android_solution.Services.DinerService;
import com.example.suc.suc_android_solution.Services.UsersDinersService;
import com.google.android.gms.maps.GoogleMap;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by efridman on 8/11/17.
 */

public class UserDinerFollowTask extends AsyncTask<String, Void, UserDiner> {

    UsersDinersService usersDinersService;
    Context mContext;

    TaskListener taskListener;

    public UserDinerFollowTask(Context context) {
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
    protected UserDiner doInBackground(String... params) {

        try {
            UserDiner userDiner = new UserDiner();
            userDiner.setIdDiner(new BigInteger(params[0]));
            userDiner.setActive(true);
            userDiner.setCollaborator(true);
            UserDiner response = usersDinersService.postUserDiner(userDiner);
            response = response != null ? response : new UserDiner();
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(UserDiner userDiner) {
        taskListener.onComplete(userDiner);
    }
}