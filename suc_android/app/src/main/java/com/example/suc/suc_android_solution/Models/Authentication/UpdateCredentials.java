package com.example.suc.suc_android_solution.Models.Authentication;

/**
 * Created by efridman on 24/9/17.
 */

public class UpdateCredentials {
    private String userName;
    private String oldPassword;
    private String newPassword;

    public UpdateCredentials(String userName, String oldPassword, String newPassword){
        this.userName = userName;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public UpdateCredentials(String userName, String newPassword){
        this.userName = userName;
        this.newPassword = newPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
