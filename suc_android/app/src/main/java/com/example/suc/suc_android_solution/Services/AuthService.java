package com.example.suc.suc_android_solution.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.suc.suc_android_solution.SucAuthenticator;

public class AuthService extends Service {
    // Instance field that stores the authenticator object
    // Notice, this is the same Authenticator class we defined earlier
    private SucAuthenticator mAuthenticator;
    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new SucAuthenticator(this);
    }
    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}