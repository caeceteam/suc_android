package com.example.suc.suc_android_solution.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;

import com.example.suc.suc_android_solution.Models.Diner;
import com.example.suc.suc_android_solution.Models.Diners;
import com.example.suc.suc_android_solution.R;
import com.example.suc.suc_android_solution.Services.DinerService;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by efridman on 4/11/17.
 */

public class GetNearestDinersMarkers extends AsyncTask<String, Void, Diners> {

    DinerService dinerService;
    Context mContext;

    GoogleMap mGoogleMap;

    TaskListener taskListener;

    public GetNearestDinersMarkers(Context context, GoogleMap map){
        mContext = context;
        mGoogleMap = map;
        dinerService = new DinerService(context);
    }

    public void setTaskListener(TaskListener taskListenerImpl){
        taskListener = taskListenerImpl;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Diners doInBackground(String... params) {

        try {
            String latitude = params[0];
            String longitude = params[1];
            Diners diners = dinerService.getAllDinersWithGeo(latitude, longitude);

            return diners;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Diners diners) {
        if (diners != null && diners.getDiners().size() > 0) {
            for (Diner diner : diners.getDiners()
                    ) {
                if (diner.getLatitude() != null && diner.getLongitude() != null) {
                    mGoogleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_local_dining_black_24dp))
                            .position(new LatLng(Double.parseDouble(diner.getLatitude().toString()), Double.parseDouble(diner.getLongitude().toString()))));
                }
            }
        }
        taskListener.onComplete();
    }
}

