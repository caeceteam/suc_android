package com.example.suc.suc_android_solution.Models;

import com.example.suc.suc_android_solution.Models.Pagination;
import com.example.suc.suc_android_solution.Models.User;

import java.util.Collection;

/**
 * Created by Marco Cupo on 5/11/2017.
 */

public class DonationsResponse {
    private Pagination pagination;
    private Collection<Donation> donations;


    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public Collection<Donation> getDonations() {
        return donations;
    }
}
