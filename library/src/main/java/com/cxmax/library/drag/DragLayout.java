package com.cxmax.library.drag;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 可拖拽的父容器Layout，只需要将内容图片在xml布局文件中设置在父容器内即可
 * 1.主要利用ViewDragHelper这个类来实现拖拽
 * Created by CaiXi on 2016/8/23.
 */
public class DragLayout extends RelativeLayout{
    private ViewDragHelper drag;

    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        drag = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                final int leftBound = getPaddingLeft();
                final int rightBound = getWidth() - child.getWidth() - leftBound;
                final int newLeft = Math.min(Math.max(left, leftBound), rightBound);
                return newLeft;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                final int topBound = getPaddingTop();
                final int bottomBound = getHeight() - child.getHeight();
                final int newTop = Math.min(Math.max(top, topBound), bottomBound);
                return newTop;
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                moveToSide(releasedChild);
                invalidate();
            }
        });
    }

    private void moveToSide(View view) {
        float top = view.getTop();
        float bottom = getMeasuredHeight() - view.getBottom();
        float right = getMeasuredWidth() - view.getRight();
        float left = view.getLeft();
        //上下滑动
        if ((top < bottom ? top : bottom) / getMeasuredHeight() < (right < left ? right : left) / getMeasuredWidth()) {
            drag.settleCapturedViewAt(view.getLeft(), top < bottom ? 0 : getMeasuredHeight() - view.getMeasuredHeight());
        } else {
            //左右滑动
            drag.settleCapturedViewAt(left < right ? 0 : getMeasuredWidth() - view.getMeasuredWidth(), view.getTop());
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return drag.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        drag.processTouchEvent(event);
        return false;
    }

    @Override
    public void computeScroll() {
        if (drag.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setClickable(true);
        }
    }
}
