package com.example.suc.suc_android_solution.Tasks;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.suc.suc_android_solution.Enumerations.AuthConfig;
import com.example.suc.suc_android_solution.Models.Donation;
import com.example.suc.suc_android_solution.Models.DonationItem;
import com.example.suc.suc_android_solution.Services.DonationService;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by efridman on 20/11/17.
 */

public class PostDonationTask extends AsyncTask<String, Void, Donation> {

    DonationService donationService;
    Context mContext;

    TaskListener taskListener;

    public PostDonationTask(Context context) {
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
    protected Donation doInBackground(String... params) {

        try {
            DonationItem newDonationItem = new DonationItem.Builder().setDescription(params[2]).build();
            List<DonationItem> items = new ArrayList<DonationItem>();
            items.add(newDonationItem);
            Donation newDonation = new Donation.Builder()
                    .setIdDinerReceiver(new BigInteger(params[0]))
                    .setTitle(params[1])
                    .setItems(items)
                    .build();

            Donation savedDonation = donationService.postDonation(newDonation);

            return savedDonation;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Donation donationSaved) {
        taskListener.onComplete(donationSaved);
    }
}