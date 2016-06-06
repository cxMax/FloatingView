package com.cxmax.library.listener.recyclerview;

import android.support.v7.widget.RecyclerView;

/**
 * recyclerview滑动监听
 * Created by caixi on 2016/6/4.
 */
public abstract class RecyclerViewScrollDetector extends RecyclerView.OnScrollListener{
    private int mScrollThreshold;

    public abstract void onScrollUp();

    public abstract void onScrollDown();

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
