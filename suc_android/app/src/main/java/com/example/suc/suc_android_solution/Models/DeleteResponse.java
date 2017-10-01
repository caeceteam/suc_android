package com.example.suc.suc_android_solution.Models;

/**
 * Created by efridman on 20/9/17.
 */

public class DeleteResponse {
    private Integer status;
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("Status: %d", status);
    }
}
