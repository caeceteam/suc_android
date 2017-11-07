package com.example.suc.suc_android_solution.Tasks;

import com.example.suc.suc_android_solution.Maps.MapMarkerViewModel;

import java.util.ArrayList;

/**
 * Created by efridman on 4/11/17.
 */

public interface TaskListener {
    void onComplete();
    void onMarkersReady(ArrayList<MapMarkerViewModel> markers);
}
