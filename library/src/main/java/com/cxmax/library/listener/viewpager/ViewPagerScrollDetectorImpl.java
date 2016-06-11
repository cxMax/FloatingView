package com.cxmax.library.listener.viewpager;

import android.support.v4.view.ViewPager;

import com.cxmax.library.listener.ScrollDirectionListener;

/**
 * Created by Administrator on 2016/6/12.
 */
public class ViewPagerScrollDetectorImpl extends ViewPagerScrollDetector{

    private ScrollDirectionListener.ScrollViewListener mListener;
    private ViewPager.OnPageChangeListener mPageChangeListener;

    public void setScrollDirectionListener(ScrollDirectionListener scrollDirectionListener, ScrollDirectionListener.ScrollViewListener listener) {
        this.mListener = listener;
        this.mScrollDirectionListener = scrollDirectionListener;
    }


    public void setmPageChangeListener(ViewPager.OnPageChangeListener pageChangeListener) {
        this.mPageChangeListener = pageChangeListener;
    }

    @Override
    public void onScrollDown() {

    }

    @Override
    public void onScrollUp() {

    }

    @Override
    public void onScrollLeft() {
        mListener.hide();
    }

    @Override
    public void onScrollRight() {
        mListener.show();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mPageChangeListener != null){
            mPageChangeListener.onPageScrollStateChanged(state);
        }
        super.onPageScrollStateChanged(state);
    }

    @Override
    public void onPageSelected(int position) {
        if (mPageChangeListener != null){
            mPageChangeListener.onPageSelected(position);
        }
        super.onPageSelected(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mPageChangeListener != null){
            mPageChangeListener.onPageScrolled(position , positionOffset , positionOffsetPixels);
        }
        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }
}
