package com.example.suc.suc_android_solution.Models.Authentication;

import com.example.suc.suc_android_solution.Models.Diner;
import com.example.suc.suc_android_solution.Models.User;

import java.util.Collection;

/**
 * Created by efridman on 24/9/17.
 */

public class AuthenticationResponse {
    private String token;
    private Collection<Diner> diners;
    private User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Collection<Diner> getDiners() {
        return diners;
    }

    public void setDiners(Collection<Diner> diners) {
        this.diners = diners;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
