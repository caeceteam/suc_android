package com.example.suc.suc_android_solution;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SucMain extends AppCompatActivity {

    Button bSearchUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suc_main);

        bSearchUsers = (Button) findViewById(R.id.action_users);
        bSearchUsers.setOnClickListener(new View.OnClickListener() {

            /**
             * The onClick method is triggered when this button (bSearchUsers) is clicked.
             *
             * @param v The view that is clicked. In this case, it's bSearchUsers.
             */
            @Override
            public void onClick(View v) {
                Context context = SucMain.this;
                Class destinationActivity = AuthenticationActivity.class;
                Intent startChildActivityIntent = new Intent(context, destinationActivity);

                startActivity(startChildActivityIntent);
            }
        });
    }
}



