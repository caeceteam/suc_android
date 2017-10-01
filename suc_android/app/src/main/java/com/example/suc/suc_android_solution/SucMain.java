package com.example.suc.suc_android_solution;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SucMain extends AppCompatActivity {

    private AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suc_main);

        accountManager = AccountManager.get(getBaseContext());
        Account[] accounts = accountManager.getAccountsByType(AuthConfig.KEY_ACCOUNT_TYPE.getConfig());
        Intent intent;
        if (accounts.length == 1) { // Si hay un usuario logueado, inicio la app desde el comienzo.
            intent = new Intent(getApplicationContext(), SucStart.class);
        }else{ //Sino lo mando al login
            intent = new Intent(getApplicationContext(), AuthenticationActivity.class);
        }

        startActivity(intent);

    }
}



