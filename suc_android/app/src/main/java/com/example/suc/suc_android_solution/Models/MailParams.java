package com.example.suc.suc_android_solution.Models;

import com.example.suc.suc_android_solution.Enumerations.MailType;

/**
 * Created by efridman on 14/10/17.
 */

public class MailParams {
    private String user_name;
    private String diner_name;
    private String password;
    private String comment;
    private String url;
    private String new_password;
    private String destination_email;
    private Integer mail_type;

    public MailParams() {
    }


    public void setUserName(String userName) {
        this.user_name = userName;
    }


    public void setDinerName(String dinerName) {
        this.diner_name = dinerName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setNewPassword(String newPassword) {
        this.new_password = newPassword;
    }

    public void setMailType(Integer mailType) {
        this.mail_type = mailType;
    }


    public void setDestination_email(String destination_email) {
        this.destination_email = destination_email;
    }
}
