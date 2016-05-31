package com.cxmax.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.DimenRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by cxmax on 2016/5/31.
 */
public class FloatingView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener{
    private final static String TAQ = FloatingView.class.getSimpleName();
    private final static int MAX_WIDTH = 60;
    private final static int MAX_HEIGHT = 60;

    private Context mContext;
    private boolean mMarginSet;
    private int mWidth,mHeight,mBitmapWidth,mBitmapHeight;
    private Paint mPaint;
    private Bitmap mBitmap;
    private Matrix mMatrix;
    private OnFloatClickListener mOnFloatClickListener;

    public interface OnFloatClickListener{
        void floatClick(View view);
    }

    public FloatingView(Context context) {
        this(context, null);
    }

    public FloatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FloatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //设置宽度
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY){
            mWidth = specSize;
        }else if (specMode == MeasureSpec.AT_MOST){
            mWidth = Math.min(dip2px(mContext, MAX_WIDTH),specSize);
        }

        //设置高度
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY){
            mHeight = specSize;
        }else if (specMode == MeasureSpec.AT_MOST){
            mHeight = Math.min(dip2px(mContext,MAX_HEIGHT), specSize);
        }

        setMargins();

        setMeasuredDimension(mWidth, mHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mMatrix.postTranslate(0, 0);
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
    }

    /**
     * View状态发生改变会回调这个监听,也就是在view消失的时候,回收bitmap
     */
    @Override
    public void onGlobalLayout() {
        if (getVisibility() == GONE){
            if (!mBitmap.isRecycled()){
                mBitmap.recycle();
            }
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_DOWN:
                if (mBitmap != null){
                    boolean touchable = (event.getX() < mBitmapWidth && event.getY() < mBitmapHeight);
                    if (touchable){
                        setVisibility(GONE);
                    }else {
                        mOnFloatClickListener.floatClick(this);
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e(TAQ,"==========onDetachedFromWindow============");
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        mPaint = new Paint();
        if (mBitmap == null){
            mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.search_clear_normal);
        }
        mBitmapWidth = mBitmap.getWidth();
        mBitmapHeight = mBitmap.getHeight();
        mMatrix = new Matrix();
        setOnFloatClickListener(mOnFloatClickListener);
    }

    private void setMargins(){
        if (!mMarginSet){
            if (getLayoutParams() instanceof ViewGroup.LayoutParams){
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) getLayoutParams();
                int leftMargin = lp.leftMargin;
                int topMargin = lp.topMargin;
                int rightMargin = lp.rightMargin;
                int bottomMargin = lp.bottomMargin;
                lp.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

                requestLayout();
                mMarginSet = true;
            }
        }
    }

    public void setOnFloatClickListener(OnFloatClickListener listener){
        mOnFloatClickListener = listener;
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}




