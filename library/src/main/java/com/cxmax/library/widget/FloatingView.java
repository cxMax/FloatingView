package com.cxmax.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.cxmax.library.R;
import com.cxmax.library.listener.ScrollDirectionListener;
import com.cxmax.library.listener.recyclerview.RecyclerViewScrollDetectorImpl;
import com.cxmax.library.listener.scrollview.ObservableScrollView;
import com.cxmax.library.listener.scrollview.ScrollViewScrollDetectorImpl;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by cxmax on 2016/5/31.
 */
public class FloatingView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener,ScrollDirectionListener.ScrollViewListener {
    private final static String TAQ = FloatingView.class.getSimpleName();
    private final static int MAX_WIDTH = 90;
    private final static int MAX_HEIGHT = 90;
    private static final int TRANSLATE_DURATION_MILLIS = 200;//进入和移出的动画时间

    private boolean mVisible;//当前view是否在屏幕内可见
    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator(); //动画插值器
    private int mScrollThreshold;
    private boolean mNeedAnimation; //滑动监听动画

    private Context mContext;
    private boolean mMarginSet;
    private int mWidth,mHeight,mBitmapWidth,mBitmapHeight;
    private Paint mPaint;
    private Bitmap mBitmap;
    private Matrix mMatrix;
    private OnFloatClickListener mOnFloatClickListener;
    private LayerDrawable mLayerDrawable;

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
        init(context, attrs);
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
        mMatrix.setTranslate(mWidth - mBitmapHeight, dip2px(mContext,4));
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

    /**
     * 点击事件绑定
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_DOWN:
                if (mBitmap != null){
                    boolean touchable = (event.getX() > (mWidth - mBitmapWidth) && event.getY() < mBitmapHeight);
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
    }

    @SuppressWarnings("ResourceType")
    private void init(Context context, AttributeSet attributeSet) {
        mVisible = true;
        mContext = context;
        mScrollThreshold = dip2px(mContext,4);
        mPaint = new Paint();
        if (mBitmap == null){
            mBitmap = createLayerDrawable();
        }
        mBitmapWidth = mBitmap.getWidth();
        mBitmapHeight = mBitmap.getHeight();
        mMatrix = new Matrix();
        //初始化自定义属性
        if (attributeSet != null) {
            initAttributes(context, attributeSet);
        }
    }

    private void initAttributes(Context context, AttributeSet attributeSet) {
        TypedArray attr = getTypedArray(context,attributeSet, R.styleable.FloatingView);
        if (attr != null){
            mNeedAnimation = attr.getBoolean(R.styleable.FloatingView_cx_animation,true);
        }
    }

    private TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }

    @SuppressWarnings("ResourceType")
    private Bitmap createLayerDrawable() {
        if (mLayerDrawable == null ){
            Drawable[] layers = new Drawable[2];
            layers[0] = getDrawable(R.drawable.float_ad_close_background);
            layers[1] = getDrawable(R.drawable.float_ad_close);
            mLayerDrawable = new LayerDrawable(layers);
        }
        return drawableToBitmap(mLayerDrawable);
    }

    private boolean hasHoneycombApi() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    @SuppressWarnings("ResourceType")
    private Drawable getDrawable(@DimenRes int id){
        return getResources().getDrawable(id);
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


    /**
     * 绑定recyclerview的滑动监听,上滑出现,下滑隐藏
     * @param recyclerView
     */
    public void attachToRecyclerView(RecyclerView recyclerView){
        attachToRecyclerView(recyclerView, null, null);
    }

    public void attachToRecyclerView(RecyclerView recyclerView, ScrollDirectionListener scrollDirectionListener){
        attachToRecyclerView(recyclerView, scrollDirectionListener, null);
    }

    public void attachToRecyclerView(RecyclerView recyclerView,
                                     ScrollDirectionListener scrollDirectionlistener,
                                     RecyclerView.OnScrollListener onScrollListener){
        RecyclerViewScrollDetectorImpl scrollDetector = new RecyclerViewScrollDetectorImpl();
        scrollDetector.setScrollDirectionListener(scrollDirectionlistener,this);
        scrollDetector.setOnScrollListener(onScrollListener);
        scrollDetector.setScrollThreshold(mScrollThreshold);
        recyclerView.addOnScrollListener(scrollDetector);
    }

    public void attachToScrollView(@NonNull ObservableScrollView scrollView) {
        attachToScrollView(scrollView, null, null);
    }

    public void attachToScrollView(@NonNull ObservableScrollView scrollView,
                                   ScrollDirectionListener scrollDirectionListener) {
        attachToScrollView(scrollView, scrollDirectionListener, null);
    }

    public void attachToScrollView(@NonNull ObservableScrollView scrollView,
                                   ScrollDirectionListener scrollDirectionListener,
                                   ObservableScrollView.OnScrollChangedListener onScrollChangedListener) {
        ScrollViewScrollDetectorImpl scrollDetector = new ScrollViewScrollDetectorImpl();
        scrollDetector.setScrollDirectionListener(scrollDirectionListener, this);
        scrollDetector.setOnScrollChangedListener(onScrollChangedListener);
        scrollDetector.setScrollThreshold(mScrollThreshold);
        scrollView.setOnScrollChangedListener(scrollDetector);
    }

    @Override
    public void show() {
        showAnimation(true);
    }

    @Override
    public void hide() {
        hideAnimation(true);
    }

    public void showAnimation(boolean animate) {
        toggle(true, animate, false);
    }

    public void hideAnimation(boolean animate) {
        toggle(false, animate, false);
    }

    private void toggle(final boolean visible, final boolean animate, boolean force) {
        if (mVisible != visible || force) {
            mVisible = visible;
            int height = getHeight();
            if (height == 0 && !force){
                ViewTreeObserver vto = getViewTreeObserver();
                if (vto.isAlive()){
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            ViewTreeObserver currentVto = getViewTreeObserver();
                            if (currentVto.isAlive()){
                                currentVto.removeOnPreDrawListener(this);
                            }
                            toggle(visible, animate, true);
                            return false;
                        }
                    });
                    return;
                }
            }
            int translationY = visible ? 0 : height + getMarginBottom();
            if (animate) {
                com.nineoldandroids.view.ViewPropertyAnimator.animate(this).setInterpolator(mInterpolator)
                        .setDuration(TRANSLATE_DURATION_MILLIS)
                        .translationY(translationY);
            }else{
                ViewHelper.setTranslationY(this,translationY);
            }

            //正在移动的view仍然可以被点击,因此我们需要将它的点击事件手动的disable
            if (!hasHoneycombApi()){
                setClickable(visible);
            }
        }
    }

    /**
     * 设置关闭背景颜色
     */
    public void setCloseColor(int color){
        Drawable background = mLayerDrawable.getDrawable(0);
        background.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        invalidate();
    }

    private int getMarginBottom() {
        int marginBottom = 0;
        final ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            marginBottom = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        }
        return marginBottom;
    }
    public void setOnFloatClickListener(OnFloatClickListener listener){
        mOnFloatClickListener = listener;
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}




