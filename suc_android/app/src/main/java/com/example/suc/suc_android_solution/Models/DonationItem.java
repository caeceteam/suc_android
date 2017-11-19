package com.example.suc.suc_android_solution.Models;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by Marco Cupo on 5/11/2017.
 */

public class DonationItem {
    private BigInteger idDonationItem;
    private BigInteger idDonation;
    private int inputType;
    private int foodType;
    private float quantity;
    private String unit;
    private String description;

    public DonationItem(){}

    public DonationItem.Builder asBuilder() {
        return new DonationItem.Builder()
                .setIdDonationItem(this.idDonationItem)
                .setIdDonation(this.idDonation)
                .setInputType(this.inputType)
                .setFoodType(this.foodType)
                .setQuantity(this.quantity)
                .setUnit(this.unit)
                .setDescription(this.description);
    }

    public BigInteger getIdDonationItem() {
        return idDonationItem;
    }

    public BigInteger getIdDonation() {
        return idDonation;
    }

    public int getInputType() {
        return inputType;
    }

    public int getFoodType() {
        return foodType;
    }

    public float getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public String getDescription() {
        return description;
    }

    public static class Builder{
        private DonationItem donationItemToBuild;

        public Builder() { donationItemToBuild = new DonationItem(); }

        public DonationItem build(){
            DonationItem builtDonationItem = donationItemToBuild;
            donationItemToBuild = new DonationItem();
            return builtDonationItem;
        }

        public Builder setIdDonationItem(BigInteger idDonationItem){
            donationItemToBuild.idDonationItem = idDonationItem;
            return this;
        }

        public Builder setIdDonation(BigInteger idDonation){
            donationItemToBuild.idDonation = idDonation;
            return this;
        }

        public Builder setInputType(int inputType){
            donationItemToBuild.inputType = inputType;
            return this;
        }

        public Builder setFoodType(int foodType){
            donationItemToBuild.foodType = foodType;
            return this;
        }

        public Builder setQuantity(float quantity){
            donationItemToBuild.quantity = quantity;
            return this;
        }

        public Builder setUnit(String unit){
            donationItemToBuild.unit = unit;
            return this;
        }

        public Builder setDescription(String description){
            donationItemToBuild.description = description;
            return this;
        }
    }
}