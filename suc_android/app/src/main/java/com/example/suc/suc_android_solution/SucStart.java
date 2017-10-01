package com.example.suc.suc_android_solution;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SucStart extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToogle;
    private AccountManager accountManager;
    private TextView tvHeaderUserName;
    private TextView tvHeaderUserRole;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suc_start);

        navigationView = (NavigationView) findViewById(R.id.nv_suc_start);

        setNavigationHeaderContent();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.nav_toolbar);
        setSupportActionBar(myToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_nav_suc);
        mToogle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToogle);
        mToogle.syncState();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int selectedId = item.getItemId();
                if(selectedId == R.id.action_logout){
                    logout();
                }

                return true;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToogle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        Account[] accounts = accountManager.getAccountsByType(AuthConfig.KEY_ACCOUNT_TYPE.getConfig());
        AccountManagerCallback<Boolean> callback = new AccountManagerCallback<Boolean>() {
            @Override
            public void run(AccountManagerFuture<Boolean> future) {
                if(future.isDone()){
                    try{
                        if(future.getResult()){
                            Intent loginIntent = new Intent(getBaseContext(), SucMain.class);
                            startActivity(loginIntent);
                        }else{
                            Toast.makeText(getBaseContext(), "Hubo un error cerrando la sesion", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception ex){
                        Toast.makeText(getBaseContext(), "Hubo un error cerrando la sesion", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        accountManager.removeAccount(accounts[0],callback,null);
    }

    private void setNavigationHeaderContent() {

        View nav_header = LayoutInflater.from(this).inflate(R.layout.navigation_header, null);

        accountManager = AccountManager.get(getBaseContext());

        Account[] accounts = accountManager.getAccountsByType(AuthConfig.KEY_ACCOUNT_TYPE.getConfig());
        Account loggedAccount = accounts[0];

        String role = accountManager.getUserData(loggedAccount,AuthConfig.ARG_ACCOUNT_TYPE.getConfig());
        ((TextView) nav_header.findViewById(R.id.tv_header_user_name)).setText(loggedAccount.name);
        ((TextView) nav_header.findViewById(R.id.tv_header_user_role)).setText(role);

        navigationView.addHeaderView(nav_header);
    }
}
