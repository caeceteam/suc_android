package com.example.suc.suc_android_solution;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suc.suc_android_solution.Enumerations.AuthConfig;

public class SucStart extends AppCompatActivity
        implements MyAccountFragment.OnFragmentInteractionListener,
        ChangePasswordFragment.OnFragmentInteractionListener,
        MainFragment.OnFragmentInteractionListener,
        NearestDinersFragment.OnFragmentInteractionListener,
        DinerDetailsFragment.OnFragmentInteractionListener
{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToogle;
    private AccountManager accountManager;
    private NavigationView navigationView;
    private TextView tvToolbarTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suc_start);

        navigationView = (NavigationView) findViewById(R.id.nv_suc_start);

        setNavigationHeaderContent();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.nav_toolbar);
        setSupportActionBar(myToolbar);
        setTitle(R.string.title_activity_start);

        //iniciamos la app en el main
        showMain(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_nav_suc);
        mToogle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToogle);
        mToogle.syncState();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int selectedId = item.getItemId();
                switch (selectedId){
                    case R.id.action_main:
                        showMain(false);
                        break;
                    case R.id.action_nearest_diners:
                        showNearestDiners();
                        break;
                    case R.id.action_logout:
                        logout();
                        break;
                    case R.id.action_my_account:
                        myAccount();
                        break;
                    case R.id.action_change_password:
                        changePassword();
                        break;
                }

                mDrawerLayout.closeDrawer(Gravity.START);
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

    private void myAccount() {

        Account[] accounts = accountManager.getAccountsByType(AuthConfig.KEY_ACCOUNT_TYPE.getConfig());

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MyAccountFragment myAccountFragment = MyAccountFragment.newInstance(accounts[0].name, getTitle().toString());
        fragmentTransaction.replace(R.id.suc_content, myAccountFragment);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();

    }

    private void showMain(Boolean starting) {

        Account[] accounts = accountManager.getAccountsByType(AuthConfig.KEY_ACCOUNT_TYPE.getConfig());

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MainFragment mainFragment = MainFragment.newInstance(accounts[0].name, getTitle().toString());
        fragmentTransaction.replace(R.id.suc_content, mainFragment);
        if(starting){
            fragmentTransaction.disallowAddToBackStack();
        }else{
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();

    }
    private void showNearestDiners() {

        Account[] accounts = accountManager.getAccountsByType(AuthConfig.KEY_ACCOUNT_TYPE.getConfig());

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        NearestDinersFragment nearestDinersFragment = NearestDinersFragment.newInstance(accounts[0].name, getTitle().toString());
        fragmentTransaction.replace(R.id.suc_content, nearestDinersFragment);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();

    }
    private void changePassword() {

        Account[] accounts = accountManager.getAccountsByType(AuthConfig.KEY_ACCOUNT_TYPE.getConfig());

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ChangePasswordFragment changePasswordFragment = ChangePasswordFragment.newInstance(accounts[0].name, getTitle().toString());
        fragmentTransaction.replace(R.id.suc_content, changePasswordFragment);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();

    }

    private void setNavigationHeaderContent() {

        View nav_header = LayoutInflater.from(this).inflate(R.layout.navigation_header, null);

        accountManager = AccountManager.get(getBaseContext());

        Account[] accounts = accountManager.getAccountsByType(AuthConfig.KEY_ACCOUNT_TYPE.getConfig());
        Account loggedAccount = accounts[0];

        String role = accountManager.getUserData(loggedAccount,AuthConfig.ARG_ACCOUNT_TYPE.getConfig());
        ((TextView) nav_header.findViewById(R.id.tv_header_user_name)).setText(loggedAccount.name);
        ((TextView) nav_header.findViewById(R.id.tv_header_user_role)).setText(role);

        ((ImageView) nav_header.findViewById(R.id.im_person_pin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAccount();
                mDrawerLayout.closeDrawer(Gravity.START);
            }
        });

        navigationView.addHeaderView(nav_header);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
