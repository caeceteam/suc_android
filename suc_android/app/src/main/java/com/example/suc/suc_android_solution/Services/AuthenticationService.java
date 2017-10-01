package com.example.suc.suc_android_solution.Services;

import com.example.suc.suc_android_solution.Clients.AuthenticationClient;
import com.example.suc.suc_android_solution.Clients.UsersClient;
import com.example.suc.suc_android_solution.Models.Authentication.AuthCredentials;
import com.example.suc.suc_android_solution.Models.Authentication.AuthenticationResponse;
import com.example.suc.suc_android_solution.Models.Authentication.UpdateCredentials;
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
 * Created by efridman on 24/9/17.
 */

public class AuthenticationService {


    public AuthenticationResponse authenticate(AuthCredentials credentials){
        try {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Network.STATIC_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            AuthenticationClient authClient = retrofit.create(AuthenticationClient.class);
            Call<AuthenticationResponse> call = authClient.post(credentials);

            AuthenticationResponse authResponse = call.execute().body();
            return authResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public AuthenticationResponse changePassword(UpdateCredentials credentials){
        try {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Network.STATIC_API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            AuthenticationClient authClient = retrofit.create(AuthenticationClient.class);
            Call<AuthenticationResponse> call = authClient.put(credentials);

            AuthenticationResponse authResponse = call.execute().body();
            return authResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
