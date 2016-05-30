package com.cxmax.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.DimenRes;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/5/31.
 */
public class FloatingView extends ImageView{

    private boolean mMarginsSet;
    private Drawable mCloseDrawable;
    private Paint mPaint;

    public FloatingView(Context context) {
        this(context, null);
    }

    public FloatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public FloatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @SuppressWarnings("ResourceType")
    private void init(Context context, AttributeSet attrs) {
        //有需要自定义属性,就加上,暂时现在没写
        if (mCloseDrawable == null){
            mCloseDrawable = getDrawable(R.drawable.delete_selector);
        }
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //这里图片宽高写死了,但如果需要xml设定View的大小,可以按照需要设置MeasureSpec.AT_MOST,MeasureSpec.EXACTLY这两种情况
        int size = getDimension(R.dimen.normal_size);
        setMargins();

        setMeasuredDimension(size,size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect(0,getHeight(),0,0);
//        canvas.drawBitmap(mCloseDrawable,rect,rect,mPaint);
    }

    private int getDimension(@DimenRes int id){
        return getResources().getDimensionPixelSize(id);
    }

    @SuppressWarnings("ResourceType")
    private Drawable getDrawable(@DimenRes int id){
        return getResources().getDrawable(id);
    }

    private void setMargins(){
        if (!mMarginsSet) {
            if (getLayoutParams() instanceof ViewGroup.MarginLayoutParams){
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) getLayoutParams();
                int leftMargin = lp.leftMargin;
                int topMargin = lp.topMargin;
                int rightMargin = lp.rightMargin;
                int bottomMargin = lp.bottomMargin;
                lp.setMargins(leftMargin,topMargin,rightMargin,bottomMargin);

                requestLayout();
                mMarginsSet = true;
            }
        }
    }
}
