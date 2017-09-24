package com.example.suc.suc_android_solution.Models;

import java.util.Collection;

/**
 * Created by efridman on 20/9/17.
 */

public class UsersResponse {
    private Pagination pagination;
    private Collection<User> users;


    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public Collection<User> getUsers() {
        return users;
    }
}
