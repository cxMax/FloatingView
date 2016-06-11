package com.cxmax.library.listener;

/**
 * 用户行为下滑,上滑
 * Created by caixi on 2016/6/4.
 */
public interface ScrollDirectionListener {

    void onScrollDown();

    void onScrollUp();

    void onScrollLeft();

    void onScrollRight();

    /**
     * View反馈的显示和隐藏行为
     */
    public interface ScrollViewListener{
        void hide();
        void show();
        // TODO: 2016/6/12 左右滑动监听
    }
}
