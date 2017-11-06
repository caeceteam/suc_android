package com.example.suc.suc_android_solution;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suc.suc_android_solution.Models.Diner;
import com.example.suc.suc_android_solution.Models.Diners;
import com.example.suc.suc_android_solution.Services.DinerService;
import com.example.suc.suc_android_solution.Tasks.GetNearestDinersMarkers;
import com.example.suc.suc_android_solution.Tasks.TaskListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NearestDinersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NearestDinersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NearestDinersFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    private static final String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    private static final String ARG_LAST_TITLE = "LAST_TITLE";
    private static final Map<String, String> ARGENTINA = new HashMap<String, String>() {{
        put("latitude", "-34.6512146");
        put("longitude", "-58.6421107");
    }};
    private static final String TAG = "SARLANGA";


    private String mAccountName;
    private String lastActivityTitle;

    PlacePicker placePicker;

    LocationManager locationManager;
    Location location;

    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;

    DinerService dinerService;
    GetNearestDinersMarkers getterTask;

    private OnFragmentInteractionListener mListener;

    public NearestDinersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param accountName nombre del usuario.
     * @param lastTitle   Titulo que estaba antes en el stack
     * @return A new instance of fragment NearestDinersFragment.
     */
    public static NearestDinersFragment newInstance(String accountName, String lastTitle) {
        NearestDinersFragment fragment = new NearestDinersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ACCOUNT_NAME, accountName);
        args.putString(ARG_LAST_TITLE, lastTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAccountName = getArguments().getString(ARG_ACCOUNT_NAME);
            lastActivityTitle = getArguments().getString(ARG_LAST_TITLE);
        }

        Activity activity = getActivity();
        activity.setTitle(R.string.title_nearest_diners);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_nearest_diners, container, false);
        dinerService = new DinerService(mView.getContext());

        fetchLocation();

        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) mView.findViewById(R.id.map_nearest_diners);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

/*
* The following code example shows setting an AutocompleteFilter on a PlaceAutocompleteFragment to
* set a filter returning only results with a precise address.
*/
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY)
                .setCountry("AR")
                .build();
        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setHint(getString(R.string.map_geo_search_input_hint));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));
                if (getterTask == null) {
                    initializeGetterTask();
                }
                getterTask.execute(String.valueOf(place.getLatLng().latitude), String.valueOf(place.getLatLng().longitude));
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().setTitle(lastActivityTitle);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(mView.getContext());
        mGoogleMap = googleMap;

        if (getterTask == null) {
            initializeGetterTask();
        }

        mGoogleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {

            @Override
            public void onCameraMove() {
                float[] distanceArray = new float[3];
                final LatLng actualLatLng = mGoogleMap.getCameraPosition().target;

                VisibleRegion visibleRegion = mGoogleMap.getProjection().getVisibleRegion();
                final double maxDistance = calculateVisibleRadius(visibleRegion);
                Location.distanceBetween(actualLatLng.latitude,
                        actualLatLng.longitude,
                        location.getLatitude(),
                        location.getLongitude(), distanceArray);
                if (distanceArray[0] > 2000) {
                    final Snackbar snackbar = Snackbar
                            .make(mView, "Buscar comedores en esta zona?", BaseTransientBottomBar.LENGTH_INDEFINITE)
                            .setAction("Dale", new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {
                                    if (getterTask == null) {
                                        initializeGetterTask();
                                        location.setLatitude(actualLatLng.latitude);
                                        location.setLongitude(actualLatLng.longitude);
                                        getterTask.execute(String.valueOf(actualLatLng.latitude), String.valueOf(actualLatLng.longitude));
                                    }
                                }

                            });

                    snackbar.show();
                }
            }
        });


        String latitude = ARGENTINA.get("latitude");
        String longitude = ARGENTINA.get("longitude");

        if (location != null) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
        } else {
            location = new Location("");
            location.setLatitude(Double.parseDouble(latitude));
            location.setLongitude(Double.parseDouble(longitude));
        }
        getterTask.execute(latitude, longitude);
    }


    private void initializeGetterTask(){
        getterTask = new GetNearestDinersMarkers(mView.getContext(), mGoogleMap);
        getterTask.setTaskListener(new TaskListener() {
            @Override
            public void onComplete() {
                getterTask = null;
            }
        });
    }
    @Override
    public void onLocationChanged(Location location) {
        if (location != null && mGoogleMap != null) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(mView.getContext(), getString(R.string.gps_disabled),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {

        Toast.makeText(mView.getContext(), getString(R.string.searching_position),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private boolean fetchLocation() {
        locationManager = (LocationManager) mView.getContext().getSystemService(Context.LOCATION_SERVICE);
        /**
         * Chequeamos que esten habilitados los permisos para llamar al location manager
         */
        if (ActivityCompat.checkSelfPermission(mView.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mView.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, this);
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        return true;
    }

    private double calculateVisibleRadius(VisibleRegion visibleRegion) {
        float[] distanceWidth = new float[1];
        float[] distanceHeight = new float[1];

        LatLng farRight = visibleRegion.farRight;
        LatLng farLeft = visibleRegion.farLeft;
        LatLng nearRight = visibleRegion.nearRight;
        LatLng nearLeft = visibleRegion.nearLeft;

        //calculate the distance width (left <-> right of map on screen)
        Location.distanceBetween(
                (farLeft.latitude + nearLeft.latitude) / 2,
                farLeft.longitude,
                (farRight.latitude + nearRight.latitude) / 2,
                farRight.longitude,
                distanceWidth
        );

        //calculate the distance height (top <-> bottom of map on screen)
        Location.distanceBetween(
                farRight.latitude,
                (farRight.longitude + farLeft.longitude) / 2,
                nearRight.latitude,
                (nearRight.longitude + nearLeft.longitude) / 2,
                distanceHeight
        );

        //visible radius is (smaller distance) / 2:
        return (distanceWidth[0] < distanceHeight[0]) ? distanceWidth[0] / 2 : distanceHeight[0] / 2;
    }
}
