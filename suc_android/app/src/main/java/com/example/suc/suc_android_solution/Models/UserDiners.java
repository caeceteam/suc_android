package com.example.suc.suc_android_solution.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;

/**
 * Created by efridman on 20/9/17.
 */

public class UserDiners {
    @SerializedName("usersDiners")
    public Collection<UserDiner> usersDiners;

    public Collection<UserDiner> getUsersDiners() {
        return usersDiners;
    }
}
