package com.example.suc.suc_android_solution.Tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.suc.suc_android_solution.Models.Diner;
import com.example.suc.suc_android_solution.Models.DinerResponse;
import com.example.suc.suc_android_solution.Models.UserDiner;
import com.example.suc.suc_android_solution.Services.DinerService;
import com.example.suc.suc_android_solution.Services.UsersDinersService;

import java.math.BigInteger;
import java.net.URL;

/**
 * Created by efridman on 13/11/17.
 */

public class GetDinerTask extends AsyncTask<String, Void, Diner> {

    DinerService dinerService;
    Context mContext;

    TaskListener taskListener;

    public GetDinerTask(Context context) {
        mContext = context;
        dinerService = new DinerService(context);
    }

    public void setTaskListener(TaskListener taskListenerImpl) {
        taskListener = taskListenerImpl;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Diner doInBackground(String... params) {

        try {
            Diner response = dinerService.getDiner(new BigInteger(params[0]));
            /*try{
                URL dinerPhotoUrl = new URL(response.photos.get(0).url);
                Bitmap image = BitmapFactory.decodeStream(dinerPhotoUrl.openConnection().getInputStream());
                response.mainPhoto = image;
            }catch (Exception ex){
                //not implemented
            }*/

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Diner diner) {
        taskListener.onComplete(diner);
    }
}