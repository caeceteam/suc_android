package com.example.suc.suc_android_solution.Maps;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

/**
 * Created by efridman on 6/11/17.
 */

public class MapViewPager extends SmartViewPager {

    /**
     * Usefull to identify pager cards on touch events
     */
    public static final String TAG_PAGE_CARD = "tag_page_card";

    /**
     * Pixel tolerance for click.
     */
    public static final int PIXEL_TOLERANCE = 15;

    private boolean isExpand = true;

    public MapViewPager(Context context) {
        super(context);
    }

    public MapViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Initializes pager to bypass outside-cards touch events to the map
     */
    public void init(final View mapView) {
        // Map ViewPager needs a powerful touch listener...
        setOnTouchListener(new MapPagerOnTouchListener(mapView));

        // To use child card expansion logic
        addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                getAdapter().expandCurrentCard();
                isExpand = true;
            }
        });
    }

    /**
     * Checks if a motion event touches a pager card view
     */
    protected boolean touchHitsCard(MotionEvent event) {
        boolean hitsChildren = false;
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i).findViewWithTag(TAG_PAGE_CARD);
            if (v != null && isInRegion(event.getRawX(), event.getRawY(), v)) {
                hitsChildren = true;
                break;
            }
        }
        return hitsChildren;
    }

    /**
     * Checks if a view contains a screen point coordinate
     */
    private boolean isInRegion(float x, float y, View v) {
        int[] coordBuffer = { 0, 0 };
        v.getLocationOnScreen(coordBuffer);
        return coordBuffer[0] + v.getWidth() > x &&    // right edge
                coordBuffer[1] + v.getHeight() > y &&   // bottom edge
                coordBuffer[0] < x &&                   // left edge
                coordBuffer[1] < y;                     // top edge
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        if (adapter instanceof MapPagerAdapter) {
            super.setAdapter(adapter);
        } else {
            throw new IllegalArgumentException("Adapter should be of type CheckoutMapPagerAdapter");
        }
    }

    @Override
    public MapPagerAdapter getAdapter() {
        return (MapPagerAdapter) super.getAdapter();
    }

    /**
     * Gets top view point, taking just the visible cards, not transparent pager area
     * Useful for the map navigator to get visible markers
     */
    public int getMapPagerTop() {
        return super.getTop() + getMeasuredHeight() - getMaxCardHeight();
    }

    /**
     * Returns the largest height among the pager cards
     */
    private int getMaxCardHeight() {
        int maxHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            int height = getChildAt(i).getMeasuredHeight();
            if (height > maxHeight) {
                maxHeight = height;
            }
        }
        return maxHeight;
    }

    @Override
    public void setCurrentItem(int item) {
        getAdapter().expandCurrentCard();
        isExpand = true;
        super.setCurrentItem(item);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean isTouche = true;
        try {
            //TODO: search a best mode to by past
            //Change this onTouch when only have one card for expanding and contracting functions correctly
            if (getAdapter().getCount() != 1) {
                isTouche = super.onTouchEvent(ev);
            }
        } catch (Exception e) {
            Log.d("MapViewPager","la cagamos");
        }

        return isTouche;
    }

    /**************************************
     * Map pager touch events listener
     **************************************/

    class MapPagerOnTouchListener implements View.OnTouchListener {

        private static final int EVENT_OWNER_PAGER = 1;
        private static final int EVENT_OWNER_MAP = 2;
        private float starX;
        private float starY;
        private int eventOwner;
        private boolean dispatchToMap = false;
        // Map view to send events properly
        private final View mapView;


        MapPagerOnTouchListener(View mapView) {
            this.mapView = mapView;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //Save star onTouch position to future validation
                    starX = event.getX();
                    starY = event.getY();
                    // Sets the owner of the new touch movement
                    eventOwner = touchHitsCard(event) ? EVENT_OWNER_PAGER : EVENT_OWNER_MAP;
                    if (eventOwner == EVENT_OWNER_PAGER) {
                        // Disallow map to receive touch events.
                        dispatchToMap = false;
                    } else {
                        // Disallow pager to intercept touch events.
                        ((ViewParent) v).requestDisallowInterceptTouchEvent(true);
                        dispatchToMap = true;
                        getAdapter().contractCurrentCard();
                        isExpand = false;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (eventOwner == EVENT_OWNER_PAGER) {
                        changeCartState(event);
                        // Allow map to receive touch events.
                        dispatchToMap = true;
                    } else {
                        // Allow pager to intercept touch events.
                        ((ViewParent) v).requestDisallowInterceptTouchEvent(false);
                        // Dispatch UP event to pager, so swipe animation is resumed if needed
                        MapViewPager.super.onTouchEvent(event);
                    }
                    break;
                default:
                    // Do nothing here
            }

            // Map view will receive the event if should
            if (eventOwner == EVENT_OWNER_MAP && dispatchToMap) {
                mapView.dispatchTouchEvent(event);
            }

            return eventOwner != EVENT_OWNER_PAGER;
        }
        /**
         * Validate that it is a click on the card and not a swipe
         * @param event The event
         */
        private void changeCartState(final MotionEvent event) {
            if (Math.abs(starY - event.getY()) <= PIXEL_TOLERANCE
                    && Math.abs(starX - event.getX()) <= PIXEL_TOLERANCE) {
                if (isExpand) {
                    getAdapter().contractCurrentCard();
                    isExpand = false;
                } else {
                    getAdapter().expandCurrentCard();
                    isExpand = true;
                }
            }
        }
    }

}