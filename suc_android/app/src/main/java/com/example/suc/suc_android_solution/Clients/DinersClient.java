package com.example.suc.suc_android_solution.Clients;

import com.example.suc.suc_android_solution.Models.DeleteResponse;
import com.example.suc.suc_android_solution.Models.Diner;
import com.example.suc.suc_android_solution.Models.DinerResponse;
import com.example.suc.suc_android_solution.Models.Diners;
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
 * Created by efridman on 13/9/17.
 */

public interface DinersClient {
    @GET("/api/diners")
    Call<Diners> getAll(@Header("x-access-token") String token);

    @GET("/api/diners/{idDiner}")
    Call<Diner> get(@Header("x-access-token") String token, @Path("idDiner") BigInteger idDiner);

    @GET("/api/diners")
    Call<Diners> getAllWithGeo(@Header("x-access-token") String token,
                               @Header("x-geo-enabled") Boolean geolocation,
                               @Query("latitude") String latitude, @Query("longitude") String longitude);

}