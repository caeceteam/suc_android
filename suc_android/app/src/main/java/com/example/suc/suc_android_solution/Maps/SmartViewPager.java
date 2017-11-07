package com.example.suc.suc_android_solution.Maps;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


import com.example.suc.suc_android_solution.R;
import com.google.android.gms.common.annotation.KeepName;

/**
 * Created by efridman on 6/11/17.
 */

public class SmartViewPager extends ViewPager {

    private boolean isSwipeEnabled = true;
    private OnViewPagerOnDraw onViewPagerOnDraw;

    public SmartViewPager(Context context) {
        super(context);
        init(null);
    }

    public void setOnViewPagerOnDraw(OnViewPagerOnDraw onViewPagerOnDraw) {
        this.onViewPagerOnDraw = onViewPagerOnDraw;
    }

    public SmartViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.SucSmartViewPager, 0, 0);
        boolean isPagingEnabled = a.getBoolean(R.styleable.SucSmartViewPager_sucSmartViewPagerPagingEnabled, true);
        setPagingEnabled(isPagingEnabled);
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (onViewPagerOnDraw != null) {
            onViewPagerOnDraw.onViewPagerOnDraw();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isSwipeEnabled() && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isSwipeEnabled() && super.onTouchEvent(ev);
    }

    private boolean isSwipeEnabled() {
        return isSwipeEnabled && getChildCount() > 1;
    }

    public void setPagingEnabled(boolean isPagingEnabled) {
        this.isSwipeEnabled = isPagingEnabled;
    }

    @KeepName
    public interface OnViewPagerOnDraw {

        void onViewPagerOnDraw();
    }
}