package com.example.suc.suc_android_solution.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;

/**
 * Created by efridman on 20/9/17.
 */

public class UserDiners {
    public Pagination pagination;
    @SerializedName("usersDiners")
    public Collection<UserDiner> usersDiners;


    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public Collection<UserDiner> getUsersDiners() {
        return usersDiners;
    }
}
