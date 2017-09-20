package com.example.suc.suc_android_solution.Clients;

import com.example.suc.suc_android_solution.Models.DeleteResponse;
import com.example.suc.suc_android_solution.Models.User;
import com.example.suc.suc_android_solution.Models.UsersResponse;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by efridman on 13/9/17.
 */

public interface UsersClient {
    @GET("/api/users")
    Call<UsersResponse> getAll();

    @GET("/api/users/{idUser}")
    Call<User> get(@Path("idUser") BigInteger idUser);

    @POST("/api/users")
    Call<User> post(@Body User user);

    @PUT("/api/users/{idUser}")
    Call<User> put(@Path("idUser") BigInteger idUser, @Body User user);

    @DELETE("/api/users/{idUser}")
    Call<DeleteResponse> delete(@Path("idUser") BigInteger idUser);



}