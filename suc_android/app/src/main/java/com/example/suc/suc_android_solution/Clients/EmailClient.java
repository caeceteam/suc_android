package com.example.suc.suc_android_solution.Clients;

import com.example.suc.suc_android_solution.Models.MailParams;
import com.example.suc.suc_android_solution.Models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by efridman on 14/10/17.
 */

public interface EmailClient {

    @POST("/emails")
    Call<Void> sendNoValidatableRegistrationMail(@Body MailParams mailParams);
}
