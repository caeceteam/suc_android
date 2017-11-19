package com.example.suc.suc_android_solution;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DinerFilterActivity extends AppCompatActivity {

    TextView tvDinerName;
    Button filterDiners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diner_filter);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.nav_toolbar);
        ((TextView) myToolbar.findViewById(R.id.toolbar_title)).setText(R.string.title_filter_diners);
        String filterNameValue = getIntent().getStringExtra("dinerName");

        tvDinerName = (TextView) findViewById(R.id.diner_name_filter);
        filterDiners = (Button) findViewById(R.id.diner_filter_button);
        filterDiners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                if (validFields()) {
                    intent.putExtra("filterName", tvDinerName.getText().toString());
                    setResult(RESULT_OK, intent);
                } else {
                    intent.putExtra("filterName", "");
                    setResult(RESULT_CANCELED, intent);
                }
                finish();
            }
        });

        if (filterNameValue != null && filterNameValue.length() > 0) {
            tvDinerName.setText(filterNameValue);
        }
    }

    private boolean validFields() {
        Boolean returnValue = true;
        if (tvDinerName.getText().length() == 0) {
            return false;
        }

        return returnValue;
    }

}
