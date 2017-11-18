package com.example.suc.suc_android_solution;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.example.suc.suc_android_solution.Enumerations.AuthConfig;
import com.example.suc.suc_android_solution.Maps.MapMarkerViewModel;
import com.example.suc.suc_android_solution.Maps.MapNavigator;
import com.example.suc.suc_android_solution.Maps.MapPagerAdapter;
import com.example.suc.suc_android_solution.Maps.MapViewPager;
import com.example.suc.suc_android_solution.Services.DinerService;
import com.example.suc.suc_android_solution.Tasks.GetNearestDinersMarkers;
import com.example.suc.suc_android_solution.Tasks.TaskListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NearestDinersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NearestDinersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NearestDinersFragment extends Fragment implements
        OnMapReadyCallback,
        LocationListener,
        ClusterManager.OnClusterItemClickListener<MapMarkerViewModel>,
        MapNavigator.ViewPortChecker {

    private static final String FRAGMENT_TAG = "NEAREST_DINERS_FRAGMENT";

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
    PlaceAutocompleteFragment autocompleteFragment;
    LocationManager locationManager;
    Location location;

    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;

    DinerService dinerService;
    GetNearestDinersMarkers getterTask;

    private AccountManager accountManager;

    private OnFragmentInteractionListener mListener;

    public static final String STATUS_MARKER_KEY = "status_marker_key";
    public static final String STATUS_ZOOM_LEVEL = "status_zoom_level";

    public static final float DEFAULT_MAP_ZOOM = 15f;

    private static final int DRAWABLE_LEFT = 0; // Index to get search input left image

    /**
     * Adapter to populate map and pager
     */
    protected MapPagerAdapter mapAdapter;

    /**
     * Markers detail view pager
     */
    protected MapViewPager viewPager;

    /**
     * Pager listener with navigation logic
     */
    protected MapNavigator mapNavigator;

    /**
     * Current item and zoom selected
     */
    protected int selectedItem = 0;
    private float selectedZoom = DEFAULT_MAP_ZOOM;

    /**
     * Clusters manager and renderer
     */
    protected ClusterManager<MapMarkerViewModel> clusterManager;
    protected ClusterRenderer clusterRenderer;

    /**
     * Previously selected marker
     */
    protected MapMarkerViewModel selectedMarker;

    /**
     * Marker model for located or searched location
     */
    protected MapMarkerViewModel centerMarker;

    /**
     * In map marker for located or searched location
     */
    protected Marker centerMapMarker;


    private boolean hasPendingCameraAnimation;
    private Cluster<MapMarkerViewModel> cluster;

    private int centerItemPinId;


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

        accountManager = AccountManager.get(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        }
        try {
            mView = inflater.inflate(R.layout.fragment_nearest_diners, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        dinerService = new DinerService(mView.getContext());

        fetchLocation();
        addSearchToMap();
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

        mapAdapter = createMapAdapter(new ArrayList<MapMarkerViewModel>());
        viewPager = (MapViewPager) mView.findViewById(R.id.map_pager);
        viewPager.setAdapter(mapAdapter); // set empty adapter
        viewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.map_pager_margin));

        mMapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (hasPendingCameraAnimation && cluster != null) {
                    explodeCluster();
                    hasPendingCameraAnimation = false;
                }
            }
        });
    }


    private void explodeCluster() {
        final LatLngBounds.Builder builder = LatLngBounds.builder();
        final Collection<MapMarkerViewModel> clusterItems = cluster.getItems();
        for (MapMarkerViewModel marker : clusterItems) {
            builder.include(marker.getPosition());
        }
        final LatLngBounds bounds = builder.build();

        mGoogleMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(bounds, 0),
                MapNavigator.CAMERA_ANIM_TIME, null);
    }

    private void addSearchToMap() {
        /*autocompleteFragment = (PlaceAutocompleteFragment)
                getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);*/
        createGooglePlacesSearchComponent();

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

    private void createGooglePlacesSearchComponent() {
        autocompleteFragment = new PlaceAutocompleteFragment();
        autocompleteFragment.onCreateView(LayoutInflater.from(getContext()), (ViewGroup) mView.getParent(), null);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.search_container, autocompleteFragment);
        fragmentTransaction.commit();
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

        boolean hasMultiTouchSupport = mView.getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(!hasMultiTouchSupport);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);

        // setup the cluster manager
        setUpClusterer();

        if (mapAdapter.getCount() == 0) {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(getInitPosition(), DEFAULT_MAP_ZOOM), MapNavigator.CAMERA_ANIM_TIME, null);

        } else {
            displayMarkers(); // The map is ready now and we had some markers to apply
        }

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


    /**
     * Creates cluster manager, its renderer, and sets its listeners
     */
    private void setUpClusterer() {
        clusterManager = getClusterManager(mView.getContext(), mGoogleMap);
        mGoogleMap.setOnCameraIdleListener(clusterManager);
        clusterRenderer = new ClusterRenderer(mView.getContext(), mGoogleMap, clusterManager);
        clusterManager.setRenderer(clusterRenderer);
        mGoogleMap.setOnMarkerClickListener(clusterManager);
        // This "opens" a cluster to show its markers
        // Same functionality is achieved by default double tapping cluster
        clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MapMarkerViewModel>() {
            @Override
            public boolean onClusterClick(Cluster<MapMarkerViewModel> cluster) {
                NearestDinersFragment.this.cluster = cluster;
                // Checks if map has undergone layout.
                if (mMapView != null && mMapView.getRight() > 0) {
                    LatLngBounds.Builder builder = LatLngBounds.builder();
                    final Collection<MapMarkerViewModel> iterator = cluster.getItems();
                    for (MapMarkerViewModel marker : iterator) {
                        builder.include(marker.getPosition());
                    }
                    final LatLngBounds bounds = builder.build();

                    mGoogleMap.animateCamera(
                            CameraUpdateFactory.newLatLngBounds(bounds, 0),
                            MapNavigator.CAMERA_ANIM_TIME, null);
                    return true;
                }
                hasPendingCameraAnimation = true;
                return false;
            }
        });
    }

    protected ClusterManager<MapMarkerViewModel> getClusterManager(Context context, final GoogleMap mGoogleMap) {
        clusterManager = new ClusterManager<MapMarkerViewModel>(context, mGoogleMap) {
            @Override
            public void onCameraIdle() {
                CameraPosition position = mGoogleMap.getCameraPosition();
                if (position != null && hasLocation(position)) {
                    super.onCameraIdle();
                }
            }

            private boolean hasLocation(@NonNull CameraPosition position) {
                final LatLng target = position.target;
                return target != null && target.latitude + target.longitude != 0;
            }

        };
        return clusterManager;
    }

    private void initializeGetterTask() {
        getterTask = new GetNearestDinersMarkers(mView.getContext(), mGoogleMap);
        getterTask.setTaskListener(new TaskListener() {
            @Override
            public void onComplete(Object response) {
                getterTask = null;
            }

            @Override
            public void onMarkersReady(ArrayList<MapMarkerViewModel> markers) {
                if (markers.size() > 0) {
                    MapMarkerViewModel centerMarker = markers.get(0);
                    centerItemPinId = centerMarker.getPinId();
                    setMarkers(centerMarker, markers);
                    displayMarkers();
                }

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

    @Override
    public boolean isPointVisible(Point point) {
        return false;
    }

    @Override
    public int getPaddingMap() {
        return 0;
    }

    /**
     * Sets this list of markers to show when map is ready
     *
     * @param center  the center marker view model
     * @param markers the list of marker view models to render in the map
     */
    public void setMarkers(@NonNull MapMarkerViewModel center, @NonNull List<MapMarkerViewModel> markers) {
        mapAdapter = createMapAdapter(markers);
        centerMarker = center;
        selectedZoom = DEFAULT_MAP_ZOOM;
        selectedItem = getCurrentItem();
    }

    @Override
    public boolean onClusterItemClick(MapMarkerViewModel mapMarkerViewModel) {
//Prevent reselection current item
        int selectedItemPosition = mapAdapter.getPositionForMarker(mapMarkerViewModel);
        if (selectedItemPosition != getCurrentItem()) {
            selectedItem = selectedItemPosition;
            viewPager.setCurrentItem(selectedItem);
        }

        return true;
    }

    public LatLng getInitPosition() {
        String latitude = ARGENTINA.get("latitude");
        String longitude = ARGENTINA.get("longitude");
        location = new Location("");
        location.setLatitude(Double.parseDouble(latitude));
        location.setLongitude(Double.parseDouble(longitude));
        return new LatLng(location.getLatitude(), location.getLongitude());
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

    public static class ClusterRenderer extends DefaultClusterRenderer<MapMarkerViewModel> {

        public static final double PIN_SCALE = 0.8f;
        public static final float PIN_ANCHOR_CENTER = 0.5f;
        public static final float PIN_ANCHOR_BOTTOM = 1f;

        private final Map<Integer, BitmapDescriptor> pinDescriptors;
        private final Map<Integer, BitmapDescriptor> largePinDescriptors;
        private final int meliBlue;
        private final Context context;

        private MapMarkerViewModel selectedItem;

        private MapNavigator mapNavigator;

        ClusterRenderer(Context context, GoogleMap map, ClusterManager<MapMarkerViewModel> clusterManager) {
            super(context, map, clusterManager);
            this.pinDescriptors = new HashMap<>();
            this.largePinDescriptors = new HashMap<>();
            this.meliBlue = ContextCompat.getColor(context, R.color.ui_suc_blue);
            this.context = context;
        }

        /**
         * Load the pin asset for each marker
         *
         * @param item          the marker model
         * @param markerOptions the inner map marker options
         */
        @Override
        protected void onBeforeClusterItemRendered(MapMarkerViewModel item, MarkerOptions markerOptions) {
            super.onBeforeClusterItemRendered(item, markerOptions);

            BitmapDescriptor pin = pinDescriptors.get(item.getPinId());
            if (pin == null) {
                if (item.getPinId() == 0) {
                    pin = BitmapDescriptorFactory.defaultMarker();
                    largePinDescriptors.put(item.getPinId(), pin);
                } else {
                    pin = BitmapDescriptorFactory.fromBitmap(createLargeBitmapPin(item.getPinId()));
                    largePinDescriptors.put(item.getPinId(), pin);
                }
                pinDescriptors.put(item.getPinId(), BitmapDescriptorFactory.fromBitmap(createSmallBitmapPin(item.getPinId())));
            }

            float vAnchor = item.isCenterMarker() ? PIN_ANCHOR_CENTER : PIN_ANCHOR_BOTTOM;

            markerOptions.icon(pin).anchor(PIN_ANCHOR_CENTER, vAnchor);
        }

        @Override
        protected int getColor(int clusterSize) {
            return meliBlue;
        }

        public BitmapDescriptor getLargePin(@DrawableRes int id) {
            return largePinDescriptors.get(id);
        }

        public BitmapDescriptor getSmallPin(@DrawableRes int id) {
            return pinDescriptors.get(id);
        }

        private Bitmap createLargeBitmapPin(@DrawableRes int id) {
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), id);
            Bitmap result = Bitmap.createScaledBitmap(bmp, (int) (150), (int) (150), false);

            Paint markerPaint = new Paint();
            markerPaint.setAntiAlias(true);
            Canvas canvas = new Canvas(result);
            canvas.drawBitmap(result, 0, 0, markerPaint);

            return result;
        }

        private Bitmap createSmallBitmapPin(@DrawableRes int id) {
            Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), id);
            Bitmap result = Bitmap.createScaledBitmap(bmp, (int) (150 * PIN_SCALE), (int) (150 * PIN_SCALE), false);

            Paint markerPaint = new Paint();
            markerPaint.setAntiAlias(true);
            Canvas canvas = new Canvas(result);
            canvas.drawBitmap(result, 0, 0, markerPaint);

            return result;
        }

        /**
         * Listener method useful to mark points as selected in some strange cases.
         * Like when user zooms out, causing a marker to hide in a cluster, and then zomming in again, causing the marker to be shawn again.
         */
        protected void onClusterItemRendered(MapMarkerViewModel clusterItem, Marker marker) {
            super.onClusterItemRendered(clusterItem, marker);
            if (clusterItem.equals(selectedItem)) {
                mapNavigator.setSelectedMarker(marker);
            }
        }

        public void setSelectedItem(MapMarkerViewModel selectedItem) {
            this.selectedItem = selectedItem;
        }

        public void setMapNavigator(MapNavigator mapNavigator) {
            this.mapNavigator = mapNavigator;
        }
    }

    /**
     * Creates the map adapter
     *
     * @param markers the list of marker view models to render in the map
     */
    protected MapPagerAdapter createMapAdapter(@NonNull List<MapMarkerViewModel> markers) {
        return new MapPagerAdapter(markers, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCardClicked(v.getId());
            }
        });
    }

    /**
     * Action to execute when a pager card is clicked
     */
    protected void onCardClicked(Object ref) {
        Account[] accounts = accountManager.getAccountsByType(AuthConfig.KEY_ACCOUNT_TYPE.getConfig());

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DinerDetailsFragment dinerFragment = DinerDetailsFragment.newInstance(accounts[0].name, getActivity().getTitle().toString(), ref.toString());
        fragmentTransaction.replace(R.id.suc_content, dinerFragment);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }


    /**
     * Removes center marker from map. Needed because this marker goes outside the cluster manager.
     */
    protected void clearCenterMarkerFromMap() {
        if (centerMapMarker != null) {
            centerMapMarker.remove();
        }
    }

    /**
     * Display this list of markers in the map
     */
    public void displayMarkers() {
        clearCenterMarkerFromMap();

        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(centerItemPinId);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 150, 150, false);

        MarkerOptions locationMarker = new MarkerOptions().position(centerMarker.getPosition())
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        centerMapMarker = mGoogleMap.addMarker(locationMarker); // Adds location marker

        clusterManager.clearItems();
        clusterManager.addItems(mapAdapter.getMarkers()); // Adds points markers
        clusterManager.setOnClusterItemClickListener(this);

        // If the map is already loaded, display the markers
        goToLocation(getPointToShow());
    }

    /**
     * Moves camera to location centered
     */
    private void goToLocation(MapMarkerViewModel location) {
        if (mapAdapter != null) {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location.getPosition(), selectedZoom),
                    MapNavigator.CAMERA_ANIM_TIME, new GoogleMap.CancelableCallback() {
                        @Override
                        public void onFinish() {
                            // This is here otherwise the pager is not shown the first time
                            setupMapPager();
                            // This is here otherwise the markers are not shown the first time, and
                            // navigator keeps zooming in for ever because it never find marker in renderer
                            clusterManager.cluster();
                        }

                        @Override
                        public void onCancel() {
                            // Not to be implemented
                        }
                    });
        }
    }

    /**
     * Sets up the items bottom pager
     */
    private void setupMapPager() {
        int initialItem = getCurrentItem();
        mapNavigator = new MapNavigator(mGoogleMap, mapAdapter, clusterManager, centerMarker, this);
        clusterRenderer.setMapNavigator(mapNavigator);
        viewPager.removeAllViews();
        viewPager.setAdapter(mapAdapter);
        viewPager.clearOnPageChangeListeners();
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mapNavigator.setMarkerVisibleAndSelected(position, false); // Redirect event to navigator to show marker
                selectedItem = position;
            }
        });
        viewPager.setCurrentItem(initialItem);
        viewPager.init(mMapView);
        // Selects initial position
        mapNavigator.setMarkerVisibleAndSelected(initialItem, true);
    }


    /**
     * Returns position of pre-selected marker, if there is one
     */
    @SuppressWarnings({"PMD.NullAssignment", "PMD.ConfusingTernary"})
    protected int getCurrentItem() {
        int currentItemPosition = 0;
        // Pre-selected store (review edit)
        if (selectedMarker != null) {
            currentItemPosition = mapAdapter.getPositionForMarker(selectedMarker);
            selectedMarker = null;
            // Config change selected store (back from contact)
        } else if (selectedItem > 0 && selectedItem < mapAdapter.getCount()) {
            currentItemPosition = selectedItem;
        }
        return currentItemPosition;
    }

    /**
     * Returns marker of point to show
     */
    protected MapMarkerViewModel getPointToShow() {
        return selectedItem >= 0 && selectedItem < mapAdapter.getCount()
                ? mapAdapter.getMarkerForPosition(selectedItem) : centerMarker;
    }

}
