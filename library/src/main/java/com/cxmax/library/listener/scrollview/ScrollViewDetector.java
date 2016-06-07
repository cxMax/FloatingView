package com.cxmax.library.listener.scrollview;

import android.widget.ScrollView;

import com.cxmax.library.listener.ScrollDirectionListener;

/**
 * ScrollView滑动监听
 * Created by cxMax on 2016/6/7.
 */
public abstract class ScrollViewDetector implements ObservableScrollView.OnScrollChangedListener,ScrollDirectionListener {
    private int mLastScrollY;
    private int mScrollThreshold;
    protected ScrollDirectionListener mScrollDirectionListener;

    @Override
    public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
        boolean isSignificantDelta = Math.abs(t - mLastScrollY) > mScrollThreshold;
        if (isSignificantDelta){
            if (t > mLastScrollY){
                onScrollUp();
            }else {
                onScrollDown();
            }
        }
        mLastScrollY = t;
    }

    public void setScrollThreshold(int scrollThreshold) {
        mScrollThreshold = scrollThreshold;
    }
}
