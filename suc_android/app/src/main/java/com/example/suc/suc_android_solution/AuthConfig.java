package com.example.suc.suc_android_solution;

/**
 * Created by efridman on 27/9/17.
 */

public enum AuthConfig {
    ARG_ACCOUNT_TYPE("ARG_ACCOUNT_TYPE"),
    ARG_ACCOUNT_ID("ARG_ACCOUNT_ID"),
    ARG_AUTH_TYPE("ARG_AUTH_TYPE"),
    ARG_IS_ADDING_NEW_ACCOUNT("ARG_IS_ADDING_NEW_ACCOUNT"),
    ARG_ACCOUNT_NAME("ARG_ACCOUNT_NAME"),
    KEY_ACCOUNT_NAME("Sucapp"),
    KEY_ACCOUNT_TYPE("suc_account_type"),
    KEY_SUC_TOKEN("SUC_TOKEN"),
    KEY_ERROR_MESSAGE("KEY_ERROR_MESSAGE"),
    PARAM_USER_PASS("suc_user_pass");


    private String config;

    private AuthConfig(String name){
        this.config = name;
    }

    public String getConfig(){
        return this.config;
    }
}
