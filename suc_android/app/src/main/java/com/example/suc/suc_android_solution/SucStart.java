package com.example.suc.suc_android_solution;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.suc.suc_android_solution.Models.Donation;
import org.w3c.dom.Text;
import com.example.suc.suc_android_solution.Enumerations.AuthConfig;

public class SucStart extends AppCompatActivity
        implements MyAccountFragment.OnFragmentInteractionListener,
        ChangePasswordFragment.OnFragmentInteractionListener,
        MainFragment.OnFragmentInteractionListener,
        NearestDinersFragment.OnFragmentInteractionListener,
        DinerDetailsFragment.OnFragmentInteractionListener,
        DonationFragment.OnFragmentInteractionListener
        DinerDetailsFragment.OnFragmentInteractionListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToogle;
    private AccountManager accountManager;
    private NavigationView navigationView;
    private TextView tvToolbarTitle;
    private Account loggedAccount;


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
        showMain();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_nav_suc);
        mToogle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToogle);
        mToogle.syncState();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int selectedId = item.getItemId();
                switch (selectedId) {
                    case R.id.action_main:
                        showMain();
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
                    case R.id.action_donate:
                        donate();
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
        if (mToogle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        Account[] accounts = accountManager.getAccountsByType(AuthConfig.KEY_ACCOUNT_TYPE.getConfig());
        AccountManagerCallback<Boolean> callback = new AccountManagerCallback<Boolean>() {
            @Override
            public void run(AccountManagerFuture<Boolean> future) {
                if (future.isDone()) {
                    try {
                        if (future.getResult()) {
                            Intent loginIntent = new Intent(getBaseContext(), SucMain.class);
                            startActivity(loginIntent);
                        } else {
                            Toast.makeText(getBaseContext(), "Hubo un error cerrando la sesion", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception ex) {
                        Toast.makeText(getBaseContext(), "Hubo un error cerrando la sesion", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        accountManager.removeAccount(accounts[0], callback, null);
    }

    private void myAccount() {
        String MY_ACCOUNT_TAG = "myAccountTag";
        FragmentManager fragmentManager = getFragmentManager();
        /**
         * Al agregar esto al principio, logro que no se sume de forma indefinida el mismo fragmento en el stack.
         * De tal forma, al hacer back, vuelvo al main.
         */
        fragmentManager.popBackStack();
        /*****************************/
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MyAccountFragment myAccountFragment = MyAccountFragment.newInstance(loggedAccount.name, getTitle().toString());
        fragmentTransaction.replace(R.id.suc_content, myAccountFragment, MY_ACCOUNT_TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void showMain() {
        String MAIN_TAG = "mainTag";
        FragmentManager fragmentManager = getFragmentManager();
        /**
         * Al agregar esto al principio, logro que no se sume de forma indefinida el mismo fragmento en el stack.
         * De tal forma, al hacer back, vuelvo al main.
         */
        fragmentManager.popBackStack();
        /*****************************/
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MainFragment mainFragment = MainFragment.newInstance(loggedAccount.name, getTitle().toString());
        fragmentTransaction.replace(R.id.suc_content, mainFragment, MAIN_TAG);
        fragmentTransaction.disallowAddToBackStack();
        fragmentTransaction.commit();
    }

    private void showNearestDiners() {
        String NEAREST_DINERS_TAG = "nearestDinersTag";
        FragmentManager fragmentManager = getFragmentManager();
        /**
         * Al agregar esto al principio, logro que no se sume de forma indefinida el mismo fragmento en el stack.
         * De tal forma, al hacer back, vuelvo al main.
         */
        fragmentManager.popBackStack();
        /*****************************/
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        NearestDinersFragment nearestDinersFragment = NearestDinersFragment.newInstance(loggedAccount.name, getTitle().toString());
        fragmentTransaction.replace(R.id.suc_content, nearestDinersFragment, NEAREST_DINERS_TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void changePassword() {
        String CHANGE_PASSWORD_TAG = "changePasswordTag";
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ChangePasswordFragment changePasswordFragment = ChangePasswordFragment.newInstance(loggedAccount.name, getTitle().toString());
        fragmentTransaction.replace(R.id.suc_content, changePasswordFragment, CHANGE_PASSWORD_TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void setNavigationHeaderContent() {

        View nav_header = LayoutInflater.from(this).inflate(R.layout.navigation_header, null);

        accountManager = AccountManager.get(getBaseContext());

        Account[] accounts = accountManager.getAccountsByType(AuthConfig.KEY_ACCOUNT_TYPE.getConfig());
        loggedAccount = accounts[0];

        String role = accountManager.getUserData(loggedAccount, AuthConfig.ARG_ACCOUNT_TYPE.getConfig());
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
    public void onBackPressed() {
        FragmentManager fragmentManager = getFragmentManager();
        if(fragmentManager.getBackStackEntryCount() == 0){
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.action_logout)
                    .setMessage(R.string.popup_close_session_advice)
                    .setPositiveButton(R.string.close, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            logout();
                        }

                    })
                    .setNegativeButton(R.string.popup_dont_close_session, null)
                    .show();
        }else{
            fragmentManager.popBackStack();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void donate() {

        Account[] accounts = accountManager.getAccountsByType(AuthConfig.KEY_ACCOUNT_TYPE.getConfig());

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DonationFragment donationFragment = DonationFragment.newInstance(accounts[0].name, getTitle().toString());
        fragmentTransaction.replace(R.id.suc_content, donationFragment);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }
}
