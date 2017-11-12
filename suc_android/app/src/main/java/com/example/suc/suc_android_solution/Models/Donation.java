package com.example.suc.suc_android_solution.Models;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by Marco Cupo on 5/11/2017.
 */

public class Donation {
    private BigInteger idDonation;
    private BigInteger idUserSender;
    private BigInteger idDinerReceiver;
    private String title;
    private String description;
    private Date creationDate;
    private Integer status;

    public Donation(){}

    public Donation.Builder asBuilder() {
        return new Donation.Builder()
                .setIdUserSender(this.idUserSender)
                .setIdDinerReceiver(this.idDinerReceiver)
                .setTitle(this.title)
                .setDescription(this.description);
    }

    public BigInteger getIdDonation() {
        return idDonation;
    }

    public BigInteger getIdUserSender() {
        return idUserSender;
    }

    public BigInteger getIdDinerReceiver() {
        return idDinerReceiver;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Integer getStatus() {
        return status;
    }

    public static class Builder{
        private Donation donationToBuild;

        public Builder() { donationToBuild = new Donation(); }

        public Donation build(){
            Donation builtDonation = donationToBuild;
            donationToBuild = new Donation();
            return builtDonation;
        }

        public Builder setIdUserSender(BigInteger idUserSender){
            donationToBuild.idUserSender = idUserSender;
            return this;
        }

        public Builder setIdDinerReceiver(BigInteger idDinerReceiver){
            donationToBuild.idDinerReceiver = idDinerReceiver;
            return this;
        }

        public Builder setTitle(String title){
            donationToBuild.title = title;
            return this;
        }

        public Builder setDescription(String description){
            donationToBuild.description = description;
            return this;
        }
    }
}