package com.example.suc.suc_android_solution.Models;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

/**
 * Created by efridman on 8/11/17.
 */

public class UserDiner {
    @SerializedName("idDiner")
    public BigInteger idDiner;
    @SerializedName("idUser")
    public BigInteger idUser;
    private Boolean active;
    @SerializedName("isCollaborator")
    public Boolean isCollaborator;

    private Diner diner;

    public BigInteger getIdDiner() {
        return idDiner;
    }

    public void setIdDiner(BigInteger idDiner) {
        this.idDiner = idDiner;
    }

    public BigInteger getIdUser() {
        return idUser;
    }

    public void setIdUser(BigInteger idUser) {
        this.idUser = idUser;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getCollaborator() {
        return isCollaborator;
    }

    public void setCollaborator(Boolean collaborator) {
        isCollaborator = collaborator;
    }

    public Diner getDiner() {
        return diner;
    }

    public void setDiner(Diner diner) {
        this.diner = diner;
    }
}
