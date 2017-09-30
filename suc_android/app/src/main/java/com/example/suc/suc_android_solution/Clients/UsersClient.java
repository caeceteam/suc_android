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
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by efridman on 13/9/17.
 */

public interface UsersClient {
    @GET("/api/users")
    Call<UsersResponse> getAll(@Header("x-access-token") String token);

    @GET("/api/users/{idUser}")
    Call<User> get(@Header("x-access-token") String token, @Path("idUser") BigInteger idUser);

    @POST("/api/users")
    Call<User> post(@Header("x-access-token") String token, @Body User user);

    @PUT("/api/users/{idUser}")
    Call<User> put(@Header("x-access-token") String token, @Path("idUser") BigInteger idUser, @Body User user);

    @DELETE("/api/users/{idUser}")
    Call<DeleteResponse> delete(@Header("x-access-token") String token, @Path("idUser") BigInteger idUser);



}