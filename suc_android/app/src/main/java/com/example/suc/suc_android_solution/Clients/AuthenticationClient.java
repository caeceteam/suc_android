package com.example.suc.suc_android_solution.Clients;

import com.example.suc.suc_android_solution.Models.Authentication.AuthCredentials;
import com.example.suc.suc_android_solution.Models.Authentication.AuthenticationResponse;
import com.example.suc.suc_android_solution.Models.Authentication.UpdateCredentials;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by efridman on 24/9/17.
 */

public interface AuthenticationClient {
    @POST("/authentication")
    Call<AuthenticationResponse> post(@Body AuthCredentials credentials);

    @PUT("/authentication")
    Call<AuthenticationResponse> put(@Body UpdateCredentials credentials);

    @PUT("/authentication")
    Call<AuthenticationResponse> put(@Body AuthCredentials credentials, @Header("x-cleaning-pass") String cleaning_pass);
}
