package com.example.suc.suc_android_solution.Services;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import com.example.suc.suc_android_solution.AuthConfig;
import com.example.suc.suc_android_solution.Clients.EmailClient;
import com.example.suc.suc_android_solution.Clients.UsersClient;
import com.example.suc.suc_android_solution.Models.MailParams;
import com.example.suc.suc_android_solution.Models.User;
import com.example.suc.suc_android_solution.Utils.Network;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.math.BigInteger;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by efridman on 14/10/17.
 */

public class EmailService {
    private Context mContext;
    private AccountManager accountManager;
    private String userToken;

    public EmailService(Context context){
        this.mContext = context;
        accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(AuthConfig.KEY_ACCOUNT_TYPE.getConfig());
        if(accounts.length > 0){
            userToken = accountManager.peekAuthToken(accounts[0], AuthConfig.KEY_SUC_TOKEN.getConfig());
        }
    }

    public void sendNoValidatableRegistrationMail(MailParams mailParams){
        try {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Network.STATIC_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            EmailClient emailsClient = retrofit.create(EmailClient.class);
            Call<Void> call = emailsClient.sendNoValidatableRegistrationMail(mailParams);
            call.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
