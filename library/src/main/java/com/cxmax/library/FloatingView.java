package com.cxmax.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by cxmax on 2016/5/31.
 */
public class FloatingView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener{
    private final static String TAQ = FloatingView.class.getSimpleName();
    private final static int MAX_WIDTH = 60;
    private final static int MAX_HEIGHT = 60;
    private static final int TRANSLATE_DURATION_MILLIS = 200;//进入和移出的动画时间

    private boolean mVisible;//当前view是否在屏幕内可见
    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private int mScrollThreshold;

    private Context mContext;
    private boolean mMarginSet;
    private int mWidth,mHeight,mBitmapWidth,mBitmapHeight;
    private Paint mPaint;
    private Bitmap mBitmap;
    private Matrix mMatrix;
    private OnFloatClickListener mOnFloatClickListener;

    //添加android5.0material design 水波纹效果
    private int mColorNormal;
    private int mColorPressed;
    private int mColorRipple;
    private int mColorDisabled;
    private boolean mShadow;
    private int mShadowSize;

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
    }

    private void init(Context context, AttributeSet attributeSet) {
        mVisible = true;
        mContext = context;
        mScrollThreshold = dip2px(mContext,4);
        mPaint = new Paint();
        if (mBitmap == null){
            mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.search_clear_normal);
        }
        mBitmapWidth = mBitmap.getWidth();
        mBitmapHeight = mBitmap.getHeight();
        mMatrix = new Matrix();
        //初始化自定义属性
        if (attributeSet != null) {
            initAttributes(context, attributeSet);
        }

//        //水波纹和阴影的效果
//        mColorNormal = getColor(R.color.material_blue_500);
//        mColorPressed = darkenColor(mColorNormal);
//        mColorRipple = lightenColor(mColorNormal);
//        mColorDisabled = getColor(android.R.color.darker_gray);
//        mShadow = true;
//        if (hasLollipopApi()) {
//            StateListAnimator stateListAnimator = AnimatorInflater.loadStateListAnimator(context,
//                    R.anim.press_elevation);
//            setStateListAnimator(stateListAnimator);
//        }
//
//        updateBackground();
    }

    private void updateBackground() {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed},createDrawable(mColorPressed));
        drawable.addState(new int[]{-android.R.attr.state_enabled},createDrawable(mColorDisabled));
        drawable.addState(new int[]{}, createDrawable(mColorNormal));
        setBackgroundCompat(drawable);
    }

    private void initAttributes(Context context, AttributeSet attributeSet) {
        TypedArray attr = getTypedArray(context,attributeSet,R.styleable.FloatingView);
        if (attr != null){
            mColorNormal = attr.getColor(R.styleable.FloatingView_cx_colorNormal,getColor(R.color.material_blue_500));
            mColorPressed = attr.getColor(R.styleable.FloatingView_cx_colorPressed,darkenColor(mColorNormal));
            mColorRipple = attr.getColor(R.styleable.FloatingView_cx_colorNormal,lightenColor(mColorNormal));
            mColorDisabled = attr.getColor(R.styleable.FloatingView_cx_colorDisabled,mColorDisabled);
            mShadow = attr.getBoolean(R.styleable.FloatingView_cx_shadow,true);
        }
    }

    private TypedArray getTypedArray(Context context, AttributeSet attributeSet, int[] attr) {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0);
    }
    private int getColor(@ColorRes int id) {
        return getResources().getColor(id);
    }

    private int getDimension(@DimenRes int id) {
        return getResources().getDimensionPixelSize(id);
    }

    private static int darkenColor(int color){
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.9f;
        return Color.HSVToColor(hsv);
    }
    private static int lightenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 1.1f;
        return Color.HSVToColor(hsv);
    }
    private boolean hasLollipopApi() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    private boolean hasJellyBeanApi() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    private boolean hasHoneycombApi() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
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

    private Drawable createDrawable(int color) {
        OvalShape ovalShape = new OvalShape();
        ShapeDrawable shapeDrawable = new ShapeDrawable(ovalShape);
        shapeDrawable.getPaint().setColor(color);

        if (mShadow && !hasLollipopApi()){ //5.0以下版本
            Drawable shadowDrawable = getResources().getDrawable(R.drawable.fab_shadow);
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{shadowDrawable,shapeDrawable});
            layerDrawable.setLayerInset(1, mShadowSize, mShadowSize, mShadowSize, mShadowSize);
            return layerDrawable;
        }else {
            return shapeDrawable;
        }
    }

    @SuppressLint("NewApi")
    private void setBackgroundCompat(Drawable drawable) {
        if (hasLollipopApi()){
            float elevation;
            if (mShadow){
                elevation = getElevation() > 0.0f ? getElevation() : dip2px(mContext,8);
            }else{
                elevation = 0.0f;
            }
            setElevation(elevation);
            RippleDrawable rippleDrawable = new RippleDrawable(new ColorStateList(new int[][]{{}},new int[]{mColorRipple})
            , drawable, null);
            setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    int size = dip2px(mContext,40);
                    outline.setOval(0, 0, size, size);
                }
            });
            setClipToOutline(true);
            setBackground(rippleDrawable);
        }else if (hasJellyBeanApi()){
            setBackground(drawable);
        }else {
            setBackgroundDrawable(drawable);
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
        scrollDetector.setScrollDirectionListener(scrollDirectionlistener);
        scrollDetector.setOnScrollListener(onScrollListener);
        scrollDetector.setScrollThreshold(mScrollThreshold);
        recyclerView.addOnScrollListener(scrollDetector);
    }

    private class RecyclerViewScrollDetectorImpl extends RecyclerViewScrollDetector{
        private ScrollDirectionListener mScrollDirectionListener;
        private RecyclerView.OnScrollListener mOnScrollListener;

        private void setScrollDirectionListener(ScrollDirectionListener scrollDirectionListener){
            mScrollDirectionListener = scrollDirectionListener;
        }

        public void setOnScrollListener(RecyclerView.OnScrollListener onScrollListener){
            mOnScrollListener = onScrollListener;
        }

        @Override
        void onScrollUp() {
            hide();
            if (mScrollDirectionListener != null) {
                mScrollDirectionListener.onScrollUp();
            }
        }

        @Override
        void onScrollDown() {
            show();
            if (mScrollDirectionListener != null) {
                mScrollDirectionListener.onScrollDown();
            }
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

    public void show() {
        show(true);
    }

    public void hide() {
        hide(true);
    }

    public void show(boolean animate) {
        toggle(true, animate, false);
    }

    public void hide(boolean animate) {
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

}




