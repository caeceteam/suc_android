package com.example.suc.suc_android_solution.Services;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import com.example.suc.suc_android_solution.AuthConfig;
import com.example.suc.suc_android_solution.Clients.DinersClient;
import com.example.suc.suc_android_solution.Clients.UsersClient;
import com.example.suc.suc_android_solution.Models.DeleteResponse;
import com.example.suc.suc_android_solution.Models.Diners;
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

    public Diners getAllDiners(){
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
            Call<Diners> call = dinersClient.getAll(userToken);

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