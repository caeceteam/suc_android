package com.example.suc.suc_android_solution.Models;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * Created by Marco Cupo on 5/11/2017.
 */

public class Donation {
    private BigInteger idDonation;
    private BigInteger idUserSender;
    private BigInteger idDinerReceiver;
    private Diner diner;
    private String title;
    private String description;
    private Date creationDate;
    private Integer status;
    private List<DonationItem> items;

    public Donation(){}

    public Donation.Builder asBuilder() {
        return new Donation.Builder()
                .setIdUserSender(this.idUserSender)
                .setIdDinerReceiver(this.idDinerReceiver)
                .setTitle(this.title)
                .setDescription(this.description)
                .setItems(this.items)
                .setDiner(this.diner);
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

    public Diner getDiner() {
        return diner;
    }

    public List<DonationItem> getItems() {
        return items;
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

        public Builder setDiner(Diner diner){
            donationToBuild.diner = diner;
            return this;
        }

        public  Builder setItems(List<DonationItem> items){
            donationToBuild.items = items;
            return this;
        }
    }

    @Override
    public boolean equals(Object obj) {
        Donation other = (Donation) obj;

        return other.idDonation.equals(this.idDonation) &&
                other.idUserSender.equals(this.idUserSender) &&
                other.idDinerReceiver.equals(this.idDinerReceiver) &&
                other.title.equals(this.title) &&
                other.description.equals(this.description) &&
                other.creationDate.equals(this.creationDate) &&
                other.status.equals(this.status);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result * this.idDonation.hashCode();
        result = 31 * result * this.idUserSender.hashCode();
        result = 31 * result * this.idDinerReceiver.hashCode();
        result = 31 * result * this.title.hashCode();
        result = 31 * result * this.description.hashCode();
        result = 31 * result * this.creationDate.hashCode();
        result = 31 * result * this.status.hashCode();

        return result;
    }
}