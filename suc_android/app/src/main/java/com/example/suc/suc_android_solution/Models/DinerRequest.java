package com.example.suc.suc_android_solution.Models;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by efridman on 16/11/17.
 */

public class DinerRequest {

    public BigInteger idDinerRequest;
    public BigInteger idDiner;
    public String title;
    public String description;
    public Date creationDate;

    @Override
    public boolean equals(Object obj) {
        DinerRequest other = (DinerRequest) obj;
        return other.idDinerRequest.equals(this.idDinerRequest) &&
                other.idDiner.equals(this.idDiner) &&
                other.title.equals(this.title) &&
                other.description.equals(this.description) &&
                other.creationDate.equals(this.creationDate);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.idDinerRequest.hashCode();
        result = 31 * result + this.idDiner.hashCode();
        result = 31 * result + this.title.hashCode();
        result = 31 * result + this.description.hashCode();
        result = 31 * result + this.creationDate.hashCode();

        return result;
    }
}
