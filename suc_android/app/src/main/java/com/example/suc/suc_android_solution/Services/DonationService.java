package com.example.suc.suc_android_solution.Services;

/**
 * Created by Marco Cupo on 5/11/2017.
 */


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import com.example.suc.suc_android_solution.AuthConfig;
import com.example.suc.suc_android_solution.Clients.DonationsClient;
import com.example.suc.suc_android_solution.Clients.UsersClient;
import com.example.suc.suc_android_solution.Models.DeleteResponse;
import com.example.suc.suc_android_solution.Models.Donation;
import com.example.suc.suc_android_solution.Models.DonationResponse;
import com.example.suc.suc_android_solution.Models.DonationsResponse;
import com.example.suc.suc_android_solution.Models.User;
import com.example.suc.suc_android_solution.Models.UserResponse;
import com.example.suc.suc_android_solution.Models.UsersResponse;
import com.example.suc.suc_android_solution.Utils.Network;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.math.BigInteger;
import java.util.Collection;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Servicio utilizado para obtener informacion de donaciones.
 */
public class DonationService {
    private Context mContext;
    private AccountManager accountManager;
    private String userToken;

    public DonationService(Context context){
        this.mContext = context;
        accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(AuthConfig.KEY_ACCOUNT_TYPE.getConfig());
        if(accounts.length > 0){
            Account account = accounts[0];
            String type = accountManager.getUserData(account, AuthConfig.ARG_ACCOUNT_TYPE.getConfig());
            userToken = accountManager.peekAuthToken(account, type);
        }
    }

    public Collection<Donation> getAllDonations(){
        try {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Network.STATIC_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            DonationsClient donationsClient = retrofit.create(DonationsClient.class);
            Call<DonationsResponse> call = donationsClient.getAll(userToken);

            DonationsResponse donationsResponse = call.execute().body();
            return donationsResponse.getDonations();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Donation getDonation(BigInteger idDonation){
        try {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Network.STATIC_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            DonationsClient donationsClient = retrofit.create(DonationsClient.class);
            Call<DonationResponse> call = donationsClient.get(userToken,idDonation);

            DonationResponse donationResponse = call.execute().body();
            return donationResponse.getDonation();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Donation postDonation(Donation donation){
        try {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Network.STATIC_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            DonationsClient donationsClient = retrofit.create(DonationsClient.class);
            Call<Donation> call = donationsClient.post(userToken,donation);

            Donation donationResponse = call.execute().body();
            return donationResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}