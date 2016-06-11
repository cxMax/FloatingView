package com.cxmax.library.listener.scrollview;

import android.widget.ScrollView;

import com.cxmax.library.listener.ScrollDirectionListener;

/**
 * Created by Administrator on 2016/6/8.
 */
public class ScrollViewScrollDetectorImpl extends ScrollViewDetector{

    private ObservableScrollView.OnScrollChangedListener mOnScrollChangedListener;
    private ScrollDirectionListener.ScrollViewListener mListener;

    public void setScrollDirectionListener(ScrollDirectionListener scrollDirectionListener, ScrollDirectionListener.ScrollViewListener listener) {
        mScrollDirectionListener = scrollDirectionListener;
        mListener = listener;
    }

    public void setOnScrollChangedListener(ObservableScrollView.OnScrollChangedListener onScrollChangedListener) {
        mOnScrollChangedListener = onScrollChangedListener;
    }

    @Override
    public void onScrollDown() {
        mListener.show();
    }

    @Override
    public void onScrollUp() {
        mListener.hide();
    }

    @Override
    public void onScrollLeft() {

    }

    @Override
    public void onScrollRight() {

    }

    @Override
    public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
        if (mOnScrollChangedListener != null) {
            mOnScrollChangedListener.onScrollChanged(who, l, t, oldl, oldt);
        }
        super.onScrollChanged(who, l, t, oldl, oldt);
    }
}
