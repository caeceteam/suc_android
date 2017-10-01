package com.example.suc.suc_android_solution.Services;

/**
 * Created by efridman on 27/8/17.
 */


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.suc.suc_android_solution.AuthConfig;
import com.example.suc.suc_android_solution.Clients.UsersClient;
import com.example.suc.suc_android_solution.Enumerations.UserRoles;
import com.example.suc.suc_android_solution.Models.DeleteResponse;
import com.example.suc.suc_android_solution.Models.User;
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
 * Servicio utilizado para obtener informacion de usuarios, como por ejemplo, colaboradores.
 */
public class UserService {
    private Context mContext;
    private AccountManager accountManager;
    private String userToken;

    public UserService(Context context){
        this.mContext = context;
        accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(AuthConfig.KEY_ACCOUNT_TYPE.getConfig());
        userToken = accountManager.peekAuthToken(accounts[0], AuthConfig.KEY_SUC_TOKEN.getConfig());
    }

    public Collection<User> getAllUsers(){
        try {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Network.STATIC_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();


            UsersClient usersClient = retrofit.create(UsersClient.class);
            Call<UsersResponse> call = usersClient.getAll(userToken);

            UsersResponse usersResponse = call.execute().body();
            return usersResponse.getUsers();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public User getUser(BigInteger idUser){
        try {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Network.STATIC_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            UsersClient usersClient = retrofit.create(UsersClient.class);
            Call<User> call = usersClient.get(userToken,idUser);

            User userResponse = call.execute().body();
            return userResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public User postUser(User user){
        try {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Network.STATIC_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            UsersClient usersClient = retrofit.create(UsersClient.class);
            Call<User> call = usersClient.post(userToken,user);

            User userResponse = call.execute().body();
            return userResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public User putUser(BigInteger idUser, User user){
        try {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Network.STATIC_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            UsersClient usersClient = retrofit.create(UsersClient.class);
            Call<User> call = usersClient.put(userToken,idUser, user);

            User userResponse = call.execute().body();
            return userResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public DeleteResponse deleteUser(BigInteger idUser){
        try {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Network.STATIC_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            UsersClient usersClient = retrofit.create(UsersClient.class);
            Call<DeleteResponse> call = usersClient.delete(userToken,idUser);

            DeleteResponse userResponse = call.execute().body();
            return userResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
