package com.example.suc.suc_android_solution.Clients;

import com.example.suc.suc_android_solution.Models.Diners;
import com.example.suc.suc_android_solution.Models.UserDiner;
import com.example.suc.suc_android_solution.Models.UserDiners;

import java.math.BigInteger;
import java.util.Collection;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by efridman on 8/11/17.
 */

public interface UsersDinersClient {
    @GET("/api/usersDiners")
    Call<UserDiners> get(@Header("x-access-token") String token,
                         @Query("idUser") BigInteger idUser);

    @POST("/api/usersDiners")
    Call<UserDiner> post(@Header("x-access-token") String token,
                         @Body UserDiner userDiner);

    @DELETE("/api/usersDiners/{idDiner}/{idUser}")
    Call<UserDiner> delete(@Header("x-access-token") String token,
                         @Path("idDiner") BigInteger idDiner,
                         @Path("idUser") BigInteger idUser);
}
