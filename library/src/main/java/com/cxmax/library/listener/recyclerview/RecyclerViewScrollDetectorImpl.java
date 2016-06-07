package com.cxmax.library.listener.recyclerview;

import android.support.v7.widget.RecyclerView;

import com.cxmax.library.listener.ScrollDirectionListener;

/**
 * 这个是具体Recyclerview滑动监听的逻辑实现,只需要在对应View中绑定即可
 * Created by cxMax on 2016/6/7.
 */
public class RecyclerViewScrollDetectorImpl extends RecyclerViewScrollDetector {


    private RecyclerView.OnScrollListener mOnScrollListener;
    private ScrollDirectionListener.ScrollViewListener mListener;

    public void setScrollDirectionListener(ScrollDirectionListener scrollDirectionListener, ScrollDirectionListener.ScrollViewListener listener){
        mScrollDirectionListener = scrollDirectionListener;
        mListener = listener;
    }

    public void setOnScrollListener(RecyclerView.OnScrollListener onScrollListener){
        mOnScrollListener = onScrollListener;
    }

    @Override
    public void onScrollUp() {
        mListener.hide();
    }

    @Override
    public void onScrollDown() {
        mListener.show();
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrolled(recyclerView, dx, dy);
        }
        super.onScrolled(recyclerView, dx, dy);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (mOnScrollListener != null){
            mOnScrollListener.onScrollStateChanged(recyclerView,newState);
        }
        super.onScrollStateChanged(recyclerView, newState);
    }
}
