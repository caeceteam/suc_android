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


    public BigInteger getIdDonation() {
        return idDonation;
    }

    public void setIdDonation(BigInteger idDonation) {
        this.idDonation = idDonation;
    }

    public BigInteger getIdUserSender() {
        return idUserSender;
    }

    public void setIdUserSender(BigInteger idUserSender) {
        this.idUserSender = idUserSender;
    }

    public BigInteger getIdDinerReceiver() {
        return idDinerReceiver;
    }

    public void setIdDinerReceiver(BigInteger idDinerReceiver) {
        this.idDinerReceiver = idDinerReceiver;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}