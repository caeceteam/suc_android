package com.example.suc.suc_android_solution.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Parcel;
import android.renderscript.ScriptGroup;

import com.example.suc.suc_android_solution.Maps.MapMarkerViewModel;
import com.example.suc.suc_android_solution.Models.Diner;
import com.example.suc.suc_android_solution.Models.Diners;
import com.example.suc.suc_android_solution.R;
import com.example.suc.suc_android_solution.Services.DinerService;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

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
        ArrayList<MapMarkerViewModel> markers = new ArrayList<MapMarkerViewModel>();
        if (diners != null && diners.getDiners().size() > 0) {
            for (Diner diner : diners.getDiners()
                    ) {
                if (diner.getLatitude() != null && diner.getLongitude() != null) {
                    MapMarkerViewModel marker = new MapMarkerViewModel(Double.parseDouble(diner.getLatitude().toString()), Double.parseDouble(diner.getLongitude().toString()));
                    marker.setTitle(diner.getName());
                    marker.setDescription(getDinerAddress(diner));
                    marker.setIconId(R.mipmap.ic_local_dining_black_24dp);
                    marker.setPinId(R.mipmap.ic_local_dining_black_24dp);
                    marker.setAction(mContext.getString(R.string.map_item_details));
                    marker.setIdDiner(diner.getIdDiner().toString());
                    markers.add(marker);
                    //mGoogleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_local_dining_black_24dp))
                      //      .position(new LatLng(Double.parseDouble(diner.getLatitude().toString()), Double.parseDouble(diner.getLongitude().toString()))));
                }
            }
        }
        taskListener.onMarkersReady(markers);
    }

    private String getDinerAddress(Diner diner) {
        return String.format("Dirección: %s %d - CP: %s \nTel: %s \nMail: %s", diner.getStreet(), diner.getStreetNumber(), diner.getZipcode(), diner.getPhone(), diner.getMail());
    }
}

