package com.example.suc.suc_android_solution.Tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.suc.suc_android_solution.Models.Diners;
import com.example.suc.suc_android_solution.Models.DonationsResponse;
import com.example.suc.suc_android_solution.Services.DinerService;
import com.example.suc.suc_android_solution.Services.DonationService;

/**
 * Created by efridman on 19/11/17.
 */

public class GetAllDonationsTask extends AsyncTask<String, Void, DonationsResponse> {

    DonationService donationService;
    Context mContext;

    TaskListener taskListener;

    public GetAllDonationsTask(Context context) {
        mContext = context;
        donationService = new DonationService(context);
    }

    public void setTaskListener(TaskListener taskListenerImpl) {
        taskListener = taskListenerImpl;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected DonationsResponse doInBackground(String... params) {

        try {
            DonationsResponse response = donationService.getAllDonations(Integer.parseInt(params[0]));
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
    protected void onPostExecute(DonationsResponse donations) {
        taskListener.onComplete(donations);
    }
}