package com.cxmax.library.listener.viewpager;

import android.support.v4.view.ViewPager;

import com.cxmax.library.listener.ScrollDirectionListener;

/**
 * ViewPager滑动监听
 * Created by caixi on 2016/6/12.
 */
public abstract class ViewPagerScrollDetector implements ViewPager.OnPageChangeListener,ScrollDirectionListener {
    protected ScrollDirectionListener mScrollDirectionListener;
    private boolean isDragged = false;
    private boolean isFirstPage = true;
    private int mLastValue = -1;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (isFirstPage && isDragged){
            if (mLastValue > positionOffsetPixels){ //右滑
                onScrollRight();
            }else if (mLastValue < positionOffsetPixels){ // 左滑
                onScrollLeft();
            }
            mLastValue = positionOffsetPixels;
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0){
            isFirstPage = true;
            onScrollLeft();
        }else{
            isFirstPage = false;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_DRAGGING){
            isDragged = true;
        }else {
            isDragged = false;
        }
    }

}
