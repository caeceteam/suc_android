package com.example.suc.suc_android_solution.Models;

import java.util.ArrayList;

/**
 * Created by efridman on 16/10/17.
 */

public class UserResponse {
    private User user;
    private ArrayList<Diner> diners;

    public User getUser() {
        return user;
    }

    public ArrayList<Diner> getDiners() {
        return diners;
    }

    public void setDiners(ArrayList<Diner> diners) {
        this.diners = diners;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
