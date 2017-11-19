package com.example.suc.suc_android_solution.Services;

/**
 * Created by efridman on 27/8/17.
 */


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import com.example.suc.suc_android_solution.Enumerations.AuthConfig;
import com.example.suc.suc_android_solution.Clients.UsersDinersClient;
import com.example.suc.suc_android_solution.Models.UserDiner;
import com.example.suc.suc_android_solution.Models.UserDiners;
import com.example.suc.suc_android_solution.Utils.Network;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.math.BigInteger;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Servicio utilizado para obtener informacion de usuarios, como por ejemplo, colaboradores.
 */
public class UsersDinersService {
    private Context mContext;
    private AccountManager accountManager;
    private String userToken;
    private String idUser;

    public UsersDinersService(Context context){
        this.mContext = context;
        accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(AuthConfig.KEY_ACCOUNT_TYPE.getConfig());
        if(accounts.length > 0){
            Account account = accounts[0];
            String type = accountManager.getUserData(account, AuthConfig.ARG_ACCOUNT_TYPE.getConfig());
            userToken = accountManager.peekAuthToken(account, type);
            idUser = accountManager.getUserData(account, AuthConfig.ARG_ACCOUNT_ID.getConfig());
        }
    }

    public UserDiners getUserDiners(){
        try {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Network.STATIC_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            UsersDinersClient usersDinersClient = retrofit.create(UsersDinersClient.class);
            Call<UserDiners> call = usersDinersClient.get(userToken,new BigInteger(idUser));

            UserDiners userDiners = call.execute().body();
            return userDiners;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public UserDiner postUserDiner(UserDiner userDiner){
        try {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Network.STATIC_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            UsersDinersClient usersDinersClient = retrofit.create(UsersDinersClient.class);
            userDiner.setIdUser(new BigInteger(idUser));
            Call<UserDiner> call = usersDinersClient.post(userToken,userDiner);

            UserDiner userResponse = call.execute().body();
            return userResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean deleteUserDiner(BigInteger idDiner){
        try {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Network.STATIC_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            UsersDinersClient usersDinersClient = retrofit.create(UsersDinersClient.class);
            Call<UserDiner> call = usersDinersClient.delete(userToken,idDiner, new BigInteger(idUser));

            Boolean deleted = call.execute().code() == 204;
            return deleted;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
