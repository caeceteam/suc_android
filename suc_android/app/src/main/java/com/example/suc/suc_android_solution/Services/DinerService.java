package com.example.suc.suc_android_solution.Services;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.support.annotation.Nullable;

import com.example.suc.suc_android_solution.Enumerations.AuthConfig;
import com.example.suc.suc_android_solution.Clients.DinersClient;
import com.example.suc.suc_android_solution.Models.Diner;
import com.example.suc.suc_android_solution.Models.DinerResponse;
import com.example.suc.suc_android_solution.Models.Diners;
import com.example.suc.suc_android_solution.Utils.Network;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.math.BigInteger;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by efridman on 2/11/17.
 */

public class DinerService {
    private Context mContext;
    private AccountManager accountManager;
    private String userToken;

    public DinerService(Context context){
        this.mContext = context;
        accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(AuthConfig.KEY_ACCOUNT_TYPE.getConfig());
        if(accounts.length > 0){
            Account account = accounts[0];
            String type = accountManager.getUserData(account, AuthConfig.ARG_ACCOUNT_TYPE.getConfig());
            userToken = accountManager.peekAuthToken(account, type);
        }
    }

    public Diner getDiner(BigInteger idDiner){
        try {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Network.STATIC_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();


            DinersClient dinersClient = retrofit.create(DinersClient.class);
            Call<Diner> call = dinersClient.get(userToken, idDiner);

            Diner diner = call.execute().body();
            return diner;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Diners getAllDiners(@Nullable Integer page){
        try {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Network.STATIC_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();


            DinersClient dinersClient = retrofit.create(DinersClient.class);
            Call<Diners> call = dinersClient.getAll(userToken, page);

            Diners diners = call.execute().body();
            return diners;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Diners getAllDinersWithGeo(String latitude, String longitude){
        try {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Network.STATIC_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();


            DinersClient dinersClient = retrofit.create(DinersClient.class);
            Call<Diners> call = dinersClient.getAllWithGeo(userToken, true, latitude, longitude);

            Diners diners = call.execute().body();
            return diners;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
