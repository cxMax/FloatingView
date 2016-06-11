package com.cxmax.library.listener.viewpager;

import android.support.v4.view.ViewPager;
import android.util.Log;

import com.cxmax.library.listener.ScrollDirectionListener;

/**
 * ViewPager滑动监听
 * Created by caixi on 2016/6/12.
 */
public abstract class ViewPagerScrollDetector implements ViewPager.OnPageChangeListener,ScrollDirectionListener {
    private int mScrollThreshold;
    protected ScrollDirectionListener mScrollDirectionListener;
    private boolean isDragged = false;
    private boolean isFirstPage = true;

    // TODO: 2016/6/12 这里左右滑动距离判断有问题,需要修改
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        boolean isSignificantDelta = Math.abs(positionOffsetPixels) > mScrollThreshold;
        if (isFirstPage){
            Log.e("info", " positionOffsetPixels = " + positionOffsetPixels);
            Log.e("info", " positionOffset = " + positionOffset);
            //在第一个页面,并且正在拖拽过程中
            if (isSignificantDelta){
                if (positionOffsetPixels > 0){
                    onScrollLeft();
                }else{
                    onScrollRight();
                }
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0){
            isFirstPage = true;
        }else{
            isFirstPage = false;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_DRAGGING){
            isDragged = true;
        }else if (state == ViewPager.SCROLL_STATE_IDLE){
            isDragged = false;
        }else if (state == ViewPager.SCROLL_STATE_SETTLING){
            isDragged = false;
        }
    }

    public void setScrollThreshold(int scrollThreshold) {
        mScrollThreshold = scrollThreshold;
    }

}
