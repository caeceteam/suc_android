package com.example.suc.suc_android_solution.Clients;

import android.support.annotation.Nullable;

import com.example.suc.suc_android_solution.Models.DeleteResponse;
import com.example.suc.suc_android_solution.Models.Donation;
import com.example.suc.suc_android_solution.Models.DonationResponse;
import com.example.suc.suc_android_solution.Models.DonationsResponse;
import com.example.suc.suc_android_solution.Models.User;
import com.example.suc.suc_android_solution.Models.UserResponse;
import com.example.suc.suc_android_solution.Models.UsersResponse;

import java.math.BigInteger;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Marco Cupo on 5/11/2017.
 */

public interface DonationsClient {
    @GET("/api/donations")
    Call<DonationsResponse> getAll(@Header("x-access-token") String token, @Nullable @Query("page") Integer page);

    @GET("/api/donations/{idDonation}")
    Call<DonationResponse> get(@Header("x-access-token") String token, @Path("idDonation") BigInteger idDonation);

    @POST("/api/donations")
    Call<Donation> post(@Header("x-access-token") String token, @Body Donation donation);
}