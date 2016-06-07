package com.cxmax.library.listener.recyclerview;

import android.support.v7.widget.RecyclerView;

import com.cxmax.library.listener.ScrollDirectionListener;

/**
 * recyclerview滑动监听
 * Created by caixi on 2016/6/4.
 */
public abstract class RecyclerViewScrollDetector extends RecyclerView.OnScrollListener implements ScrollDirectionListener {
    private int mScrollThreshold;
    protected ScrollDirectionListener mScrollDirectionListener;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        boolean isSignificantDelta = Math.abs(dy) > mScrollThreshold;
        if (isSignificantDelta){
            if (dy > 0){
                onScrollUp();
            }else{
                onScrollDown();
            }
        }
    }

    public void setScrollThreshold(int scrollThreshold) {
        mScrollThreshold = scrollThreshold;
    }

}
