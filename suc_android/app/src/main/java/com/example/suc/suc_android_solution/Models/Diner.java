package com.example.suc.suc_android_solution.Models;

import android.graphics.Bitmap;

import com.example.suc.suc_android_solution.Enumerations.DinerStates;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

/**
 * Created by efridman on 24/9/17.
 */

public class Diner {
    @SerializedName("idDiner")
    public BigInteger idDiner;
    private String name;
    private DinerStates state;
    private String street;
    @SerializedName("streetNumber")
    public Integer streetNumber;
    private String floor;
    private String door;
    private BigDecimal latitude;
    private BigDecimal longitude;
    @SerializedName("zipCode")
    public String zipcode;
    private String phone;
    private String description;
    private String link;
    private String mail;

    public List<DinerRequest> requests;
    public List<DinerPhoto> photos;
    public Bitmap mainPhoto;
    public Diner(){}

    public Diner.Builder asBuilder(){
        return new Diner.Builder()
                .setIdDiner(idDiner)
                .setName(name)
                .setState(state)
                .setStreet(street)
                .setStreetNumber(streetNumber)
                .setFloor(floor)
                .setDoor(door)
                .setZipcode(zipcode)
                .setPhone(phone)
                .setDescription(description)
                .setLink(link)
                .setMail(mail)
                .setDescription(description)
                .setLatitude(latitude)
                .setLongitude(longitude);
    }

    public BigInteger getIdDiner() {
        return idDiner;
    }

    public String getName() {
        return name;
    }

    public DinerStates getState() {
        return state;
    }

    public String getStreet() {
        return street;
    }

    public Integer getStreetNumber() {
        return streetNumber;
    }

    public String getFloor() {
        return floor;
    }

    public String getDoor() {
        return door;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getPhone() {
        return phone;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getMail() {
        return mail;
    }

    public static class Builder{
        private Diner dinerToBuild;

        public Builder(){
            dinerToBuild = new Diner();
        }

        public Diner build(){
            Diner builtDiner = dinerToBuild;
            dinerToBuild = new Diner();
            return builtDiner;
        }

        public Builder setIdDiner(BigInteger idDiner){
            dinerToBuild.idDiner = idDiner;
            return this;
        }

        public Builder setName(String name){
            dinerToBuild.name = name;
            return this;
        }

        public Builder setState(DinerStates state){
            dinerToBuild.state = state;
            return this;
        }


        public Builder setState(String state){
            dinerToBuild.state = DinerStates.from(state);
            return this;
        }

        public Builder setState(Integer stateValue){
            dinerToBuild.state = DinerStates.from(stateValue);
            return this;
        }

        public Builder setStreet(String street){
            dinerToBuild.street = street;
            return this;
        }

        public Builder setStreetNumber(Integer streetNumber){
            dinerToBuild.streetNumber = streetNumber;
            return this;
        }

        public Builder setFloor(String floor){
            dinerToBuild.floor = floor;
            return this;
        }

        public Builder setDoor(String door){
            dinerToBuild.door = door;
            return this;
        }

        public Builder setZipcode(String zipcode){
            dinerToBuild.zipcode = zipcode;
            return this;
        }

        public Builder setPhone(String phone){
            dinerToBuild.phone = phone;
            return this;
        }

        public Builder setLink(String link){
            dinerToBuild.link = link;
            return this;
        }

        public Builder setDescription(String description){
            dinerToBuild.description = description;
            return this;
        }

        public Builder setMail(String mail){
            dinerToBuild.mail = mail;
            return this;
        }

        public Builder setLatitude(BigDecimal latitude){
            dinerToBuild.latitude = latitude;
            return this;
        }

        public Builder setLongitude(BigDecimal longitude){
            dinerToBuild.longitude = longitude;
            return this;
        }
    }

    //La igualdad de dos diners esta dependiendo en este momento solamente de su id. SI eventualmente
    //Se necesitara mejorar esto, va a ser medio jodido, pero habra que extender estos metodos.
    @Override
    public boolean equals(Object obj) {
        Diner other = (Diner) obj;
        return this.idDiner.equals(other.idDiner);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + idDiner.hashCode();
        return result;
    }
}
