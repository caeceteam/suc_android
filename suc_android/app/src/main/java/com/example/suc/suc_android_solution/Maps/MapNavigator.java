package com.example.suc.suc_android_solution.Maps;

import android.graphics.Point;
import android.support.annotation.NonNull;

import com.example.suc.suc_android_solution.NearestDinersFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

/**
 * Created by efridman on 6/11/17.
 */

public class MapNavigator {private static final int INIT_ZOOM_MARKERS_LIMIT = 3;

    public static final int CAMERA_ANIM_TIME = 300;

    /**
     * The host map
     */
    private final GoogleMap map;

    /**
     * Adapter that populate map and bottom pager
     */
    private final MapPagerAdapter pagerAdapter;

    /**
     * Current map clusters manager
     */
    protected ClusterManager<MapMarkerViewModel> clusterManager;

    /**
     * Current map cluster renderer
     */
    private final NearestDinersFragment.ClusterRenderer clusterRenderer;

    /**
     * Reference of last selected marker position in adapter
     */
    private int lastPosition = -1;

    /**
     * Selected marker model
     */
    private MapMarkerViewModel markerModel;

    /**
     * Selected marker
     */
    private Marker marker;

    /**
     * Current or searched location marker model
     */
    private final MapMarkerViewModel locationMarker;

    /**
     * Selected marker position in adapter
     */
    private int position;

    /**
     * Useful to check location visibility in viewport
     */
    private final ViewPortChecker viewPortChecker;


    public MapNavigator(GoogleMap map, MapPagerAdapter pagerAdapter,
                                ClusterManager<MapMarkerViewModel> clusterManager,
                                MapMarkerViewModel center, ViewPortChecker viewPortChecker) {
        super();
        this.map = map;
        this.pagerAdapter = pagerAdapter;
        this.clusterManager = clusterManager;
        this.clusterRenderer = (NearestDinersFragment.ClusterRenderer) clusterManager.getRenderer();
        this.locationMarker = center;
        this.viewPortChecker = viewPortChecker;
    }

    /**
     * On zoom finished, this re-check marker visibility to act in consequence
     */
    private final GoogleMap.CancelableCallback zoomCallback = new GoogleMap.CancelableCallback() {
        @Override
        public void onFinish() {
            clusterManager.cluster();
            seekMarker(false);
        }
        @Override
        public void onCancel() {
            // Not to be implemented
        }
    };

    /**
     * Sets a marker as selected, and deselects the previous one
     */
    public void setMarkerVisibleAndSelected(int position, boolean initCameraUpdate) {
        // Highlight current marker
        markerModel = pagerAdapter.getMarkerForPosition(position);
        clusterRenderer.setSelectedItem(markerModel);
        marker = clusterRenderer.getMarker(markerModel);
        this.position = position;
        seekMarker(initCameraUpdate);
    }

    /**
     * Starts marker point seek and check
     * @param firstSeekInSearch means is the first point seek for an api search
     */
    private void seekMarker(boolean firstSeekInSearch) {
        if (checkVisibility(markerModel, firstSeekInSearch)) {
            updateSelectedMarker();
        }
    }

    /**
     * Checks if a marker is visible for current zoom (into a cluster, then not into viewport)
     */
    public boolean checkVisibility(MapMarkerViewModel markerModel, boolean initCameraUpdate) {
        boolean visible = true;
        if (isMarkerInCluster(markerModel)) {
            visible = false;
            doZoomIn(initCameraUpdate);
        } else if (isMarkerVisible(markerModel)) {
            marker = clusterRenderer.getMarker(markerModel);
        } else {
            marker = clusterRenderer.getMarker(markerModel);
            // Checks if there is a minimun of visible markers
            if (initCameraUpdate && countVisibleMarkers(pagerAdapter.getMarkers()) < INIT_ZOOM_MARKERS_LIMIT) {
                goToInitPosition();
            } else {
                goToSelectedMarker(markerModel);
            }
        }
        return visible;
    }

    /**
     * Gets visible region for the map. In some cases (devices), projection could be null
     * so we need to avoid crashes here.
     */
    private LatLngBounds getMapVisibleRegion() {
        LatLngBounds region = null;
        if (map.getProjection() != null) {
            region = map.getProjection().getVisibleRegion().latLngBounds;
        }
        return region;
    }

    /**
     * Counts current visible marker in map, without search and pager areas
     */
    protected int countVisibleMarkers(List<MapMarkerViewModel> markerModels) {
        int count = 0;
        LatLngBounds visibleRegion = getMapVisibleRegion();
        if (visibleRegion != null) {
            for (MapMarkerViewModel model : markerModels) {
                if (visibleRegion.contains(model.getPosition())) {
                    Point point = map.getProjection().toScreenLocation(model.getPosition());
                    if (viewPortChecker.isPointVisible(point)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * Creates a CameraUpdate to show a minimun count of markers in map
     */
    private CameraUpdate getInitCameraUpdate(MapMarkerViewModel central) {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        final int limit = pagerAdapter.getCount() < INIT_ZOOM_MARKERS_LIMIT ? pagerAdapter.getCount() : INIT_ZOOM_MARKERS_LIMIT;
        for (int i = 0; i < limit; i++) {
            MapMarkerViewModel model = pagerAdapter.getMarkerForPosition(i);
            builder.include(model.getPosition());
        }
        builder.include(central.getPosition());
        return CameraUpdateFactory.newLatLngBounds(builder.build(), viewPortChecker.getPaddingMap());
    }

    /**
     * Checks if selected point is inside a cluster
     */
    public boolean isMarkerInCluster(MapMarkerViewModel model) {
        return clusterRenderer.getMarker(model) == null;
    }

    /**
     * Checks if selected point is inside current viewport (may be in a cluster or not)
     */
    public boolean isMarkerVisible(MapMarkerViewModel model) {
        boolean visible = false;
        LatLngBounds visibleRegion = getMapVisibleRegion();
        if (visibleRegion != null && visibleRegion.contains(model.getPosition())) {
            Point point = map.getProjection().toScreenLocation(model.getPosition());
            if (viewPortChecker.isPointVisible(point)) {
                visible = true;
            }
        }
        return visible;
    }

    /**
     * Makes one level zoom, centering location
     */
    public void doZoomIn(boolean initCameraUpdate) {
        CameraPosition currentPosition = map.getCameraPosition();
        float currentZoom = currentPosition == null ? map.getMinZoomLevel() : currentPosition.zoom;
        float newZoom = currentZoom + 1f;
        if (map.getMaxZoomLevel() < newZoom) {
            newZoom = map.getMaxZoomLevel();
        }

        if (initCameraUpdate) {
            map.animateCamera(getInitCameraUpdate(locationMarker), CAMERA_ANIM_TIME, zoomCallback);
        } else {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(markerModel.getPosition(), newZoom), CAMERA_ANIM_TIME, zoomCallback);
        }
    }

    /**
     * Change marker icons for selected/deselected state
     */
    public void updateSelectedMarker() {
        // Dim last marker
        if (lastPosition != -1 && lastPosition != position) {
            MapMarkerViewModel lastModel = pagerAdapter.getMarkerForPosition(lastPosition);
            Marker lastMarker = clusterRenderer.getMarker(lastModel);
            if (lastMarker != null) {
                lastMarker.setIcon(clusterRenderer.getSmallPin(lastModel.getPinId()));
                lastMarker.setAnchor(NearestDinersFragment.ClusterRenderer.PIN_ANCHOR_CENTER, 1f);
            }
        }
        setSelectedMarker(marker);
        lastPosition = position;
    }

    public void setSelectedMarker(Marker marker) {
        if (marker != null) {
            marker.setIcon(clusterRenderer.getLargePin(markerModel.getPinId()));
            marker.setAnchor(NearestDinersFragment.ClusterRenderer.PIN_ANCHOR_CENTER,
                    NearestDinersFragment.ClusterRenderer.PIN_ANCHOR_BOTTOM);
        }
    }

    /**
     * Animates camera to center point and init camera config
     */
    protected void goToInitPosition() {
        map.animateCamera(getInitCameraUpdate(locationMarker), CAMERA_ANIM_TIME, null);
    }

    /**
     * Animates camera to an specific point
     */
    protected void goToSelectedMarker(@NonNull MapMarkerViewModel model) {
        map.animateCamera(CameraUpdateFactory.newLatLng(model.getPosition()), CAMERA_ANIM_TIME, null);
    }


    /**
     * View port area helper
     */
    public interface ViewPortChecker {

        /**
         * Determines if a point is currently visible
         */
        boolean isPointVisible(Point point);

        /**
         * Obtains padding of map
         */
        int getPaddingMap();
    }


}
