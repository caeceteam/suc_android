package com.example.suc.suc_android_solution.Maps;

import android.accounts.Account;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suc.suc_android_solution.AuthConfig;
import com.example.suc.suc_android_solution.MainFragment;
import com.example.suc.suc_android_solution.R;

import java.util.List;

/**
 * Created by efridman on 6/11/17.
 */

public class MapPagerAdapter extends PagerAdapter {

    protected final List<MapMarkerViewModel> markers;
    private final View.OnClickListener clickListener;

    protected View currentPage;

    public MapPagerAdapter(@NonNull List<MapMarkerViewModel> markers, View.OnClickListener clickListener) {
        super();
        this.markers = markers;
        this.clickListener = clickListener; // To be used by child
    }

    @Override
    public int getCount() {
        return markers.size();
    }

    protected View.OnClickListener getClickListener() {
        return clickListener;
    }

    @SuppressWarnings({"PMD.CompareObjectsWithEquals"})
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * Get the position for the specified marker
     *
     * @param marker the marker to find in this adapter
     * @return the position within the adapter
     */
    public int getPositionForMarker(MapMarkerViewModel marker) {
        return markers.indexOf(marker);
    }

    /**
     * Get the marker for the specified position
     *
     * @param position the position to access
     * @return the marker for the position
     */
    public MapMarkerViewModel getMarkerForPosition(int position) {
        return markers.get(position);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        currentPage = (View) object;
        super.setPrimaryItem(container, position, object);
    }

    /**
     * Returns current selected page view (Primary item)
     */
    public View getCurrentPage() {
        return currentPage;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View markerDetail = LayoutInflater.from(container.getContext()).inflate(R.layout.map_item_description, container, false);
        final MapMarkerViewModel viewModel = markers.get(position);

        TextView title = (TextView) markerDetail.findViewById(R.id.map_item_title);
        title.setText(viewModel.getTitle());

        TextView description = (TextView) markerDetail.findViewById(R.id.map_item_description);
        description.setText(viewModel.getDescription());

        TextView details = (TextView) markerDetail.findViewById(R.id.map_item_details);
        details.setText(viewModel.getAction());
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Prueba d", viewModel.getIdDiner());
                v.setId(Integer.parseInt(viewModel.getIdDiner()));
                v.setTag(R.id.map_item_details);
               clickListener.onClick(v);
            }
        });

        container.addView(markerDetail);

        return markerDetail;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * @return the list of markers being modeled
     */
    @NonNull
    public List<MapMarkerViewModel> getMarkers() {
        return markers;
    }


    /*********************************************************
     * Each use case may have its own way to manage the cards
     *********************************************************/

    protected void contractCurrentCard() {
        // To be implemented by child
    }

    protected void expandCurrentCard() {
        // To be implemented by child
    }

}