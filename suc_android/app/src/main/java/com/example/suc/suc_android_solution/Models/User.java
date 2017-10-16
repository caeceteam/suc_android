package com.example.suc.suc_android_solution.Models;

import com.example.suc.suc_android_solution.Enumerations.UserRoles;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by efridman on 27/8/17.
 */

public class User {
    private BigInteger idUser;
    private String name;
    private String surname;
    private String phone;
    private String mail;

    @SerializedName("bornDate")

    private Date bornDate;
    private String alias;
    private String pass;

    @SerializedName("docNum")
    private String docNumber;

    private String street;

    @SerializedName("streetNumber")
    private Integer streetNumber;
    private String floor;
    private String door;
    private UserRoles role;

    public User(){}

    public User.Builder asBuilder(){
        return new User.Builder()
                .setAlias(this.alias)
                .setBornDate(this.bornDate)
                .setDocNumber(this.docNumber)
                .setDoor(this.door)
                .setFloor(this.floor)
                .setIdUser(this.idUser)
                .setMail(this.mail)
                .setName(this.name)
                .setPass(this.pass)
                .setPhone(this.phone)
                .setStreet(this.street)
                .setStreetNumber(this.streetNumber)
                .setSurname(this.surname)
                .setUserRole(this.role);
    }

    public BigInteger getIdUser(){
        return this.idUser;
    }

    public String getName(){
        return this.name;
    }

    public String getSurname(){
        return this.surname;
    }

    public String getPhone() {
        return phone;
    }

    public String getMail() {
        return mail;
    }

    public Date getBornDate() {
        return bornDate;
    }

    public String getAlias() {
        return alias;
    }

    public String getPass() {
        return pass;
    }

    public String getDocNumber() {
        return docNumber;
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

    public UserRoles getRole() {
        return role;
    }

    @Override
    public String toString() {
        return String.format("name: %s, surname: %s", this.name, this.surname);
    }

    public static class Builder{
        private User userToBuild;

        public Builder(){
            userToBuild = new User();
        }

        public User build(){
            User builtUser = userToBuild;
            userToBuild = new User();
            return builtUser;
        }

        public Builder setIdUser(BigInteger idUser){
            userToBuild.idUser = idUser;
            return this;
        }

        public Builder setName(String name){
            userToBuild.name = name;
            return this;
        }

        public Builder setSurname(String surname){
            userToBuild.surname = surname;
            return this;
        }

        public Builder setPhone(String phone){
            userToBuild.phone = phone;
            return this;
        }

        public Builder setMail(String mail){
            userToBuild.mail = mail;
            return this;
        }

        public Builder setAlias(String alias){
            userToBuild.alias = alias;
            return this;
        }

        public Builder setPass(String pass){
            userToBuild.pass = pass;
            return this;
        }

        public Builder setDocNumber(String docNumber){
            userToBuild.docNumber = docNumber;
            return this;
        }

        public Builder setStreet(String street){
            userToBuild.street = street;
            return this;
        }

        public Builder setFloor(String floor){
            userToBuild.floor = floor;
            return this;
        }

        public Builder setDoor(String door){
            userToBuild.door = door;
            return this;
        }

        public Builder setBornDate(Date bornDate){
            userToBuild.bornDate = bornDate;
            return this;
        }

        public Builder setStreetNumber(Integer streetNumber){
            userToBuild.streetNumber = streetNumber;
            return this;
        }

        public Builder setUserRole(UserRoles role){
            userToBuild.role = role;
            return this;
        }

        public Builder setUserRole(String role){
            userToBuild.role = UserRoles.from(role);
            return this;
        }

        public Builder setUserRole(Integer roleValue){
            userToBuild.role = UserRoles.from(roleValue);
            return this;
        }

    }

}

