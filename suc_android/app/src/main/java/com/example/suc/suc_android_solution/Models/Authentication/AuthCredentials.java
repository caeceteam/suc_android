package com.example.suc.suc_android_solution.Models.Authentication;

/**
 * Created by efridman on 24/9/17.
 */

public class AuthCredentials {
    private String userName;
    private String password;

    public AuthCredentials(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
