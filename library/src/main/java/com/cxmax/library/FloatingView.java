package com.cxmax.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.cxmax.library.utils.ImageUtils;

import pl.droidsonroids.gif.GifDrawable;

/**
 * 1.绑定滑动监听，View在滑动过程中显示隐藏的动画效果。
 * 2.自定义View的绘制，主要用了Bitmap的绘制，在目标广告图的右上角用遮罩绘制删除按钮
 * 3.View的点击事件OnTouchEvent的处理
 * Created by CaiXi on 2016/8/23
 */
public class FloatingView extends AppCompatImageView implements IFloatingView,ViewTreeObserver.OnGlobalLayoutListener {

    private final static String TAG = FloatingView.class.getSimpleName();

    private Context context;
    private Paint paint;
    private int width, height;
    private boolean hasMargin;
    private Matrix matrix;
    private float pointX, pointY;

    /* close bitmap */
    private Bitmap closeBitmap;
    private LayerDrawable closeDrawable;
    private int closeWidth, closeHeight, closePadding;
    /* custom params */
    private boolean draggable;

    IFloatingView.OnClickListener clickListener;

    public FloatingView(Context context) {
        this(context, null);
    }

    public FloatingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeCustomAttrs(context, attrs);
        initialize(context);
        initializeCloseBitmap(context);
    }

    private void initialize(Context context) {
        this.context = context;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        matrix = new Matrix();
        width = context.getResources().getDimensionPixelSize(R.dimen.floating_view_max_width);
        height = context.getResources().getDimensionPixelSize(R.dimen.floating_view_max_height);
    }

    private void initializeCloseBitmap(Context context) {
        closeWidth = context.getResources().getDimensionPixelSize(R.dimen.close_view_width);
        closeHeight = context.getResources().getDimensionPixelSize(R.dimen.close_view_height);
        closePadding = context.getResources().getDimensionPixelSize(R.dimen.close_view_padding);
        closeDrawable = ImageUtils.createLayerDrawable(ContextCompat.getDrawable(context, R.drawable.float_ad_close),
                ContextCompat.getDrawable(context, R.drawable.float_ad_close_background));
        closeBitmap = ImageUtils.drawableToBitmap(closeDrawable, closeWidth, closeHeight, Bitmap.Config.ARGB_4444);
    }

    private void initializeCustomAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.FloatingView, 0, 0);
            if (attr != null) {
                draggable = attr.getBoolean(R.styleable.FloatingView_draggable, false);
                attr.recycle();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = measureSize(widthMeasureSpec, width);
        int h = measureSize(heightMeasureSpec, height);
        setMargin();
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        matrix.setTranslate(width - closeWidth - closePadding, closePadding);
        if (closeBitmap != null) {
            canvas.drawBitmap(closeBitmap, matrix, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                if (closeBitmap != null){
                    boolean touchable = (pointX > (width - closeWidth) && pointY < closeHeight);
                    if (touchable){
                        setVisibility(GONE);
                        if (clickListener != null) {
                            clickListener.onCloseClick();
                        }
                        release();
                        setImageDrawable(null);
                    }else{
                        if (clickListener != null) {
                            clickListener.onFloatClick(this);
                        }
                    }
                    pointX = 0;
                    pointY = 0;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (draggable) {
                    final float distanceX = event.getX() - pointX;
                    final float distanceY = event.getY() - pointY;
                    if (distanceX != 0 && distanceY != 0) {
                        int l = (int) (getLeft() + distanceX);
                        int r = (int) (getRight() + distanceX);
                        int t = (int) (getTop() + distanceY);
                        int b = (int) (getBottom() + distanceY);
                        this.layout(l, t, r, b);
                    }
                } else {
                    pointX = event.getX();
                    pointY = event.getY();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_DOWN:
                pointX = event.getX();
                pointY = event.getY();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onGlobalLayout() {
        if (getVisibility() == GONE){
            release();
        }
    }

    @Override
    public void setCloseColor(@ColorInt int color) {
        recycleClose();
        closeDrawable = ImageUtils.drawColorOnLayer(closeDrawable, color);
        closeBitmap = ImageUtils.drawableToBitmap(closeDrawable, closeWidth, closeHeight, Bitmap.Config.ARGB_4444);
        invalidate();
    }

    @Override
    public void release() {
        recycleClose();
        recycleDrawableIfGif();
    }

    public void setClickListener(IFloatingView.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    private void recycleClose() {
        if (closeBitmap != null && !closeBitmap.isRecycled()) {
            closeBitmap.recycle();
            closeBitmap = null;
        }
    }

    private void recycleDrawableIfGif() {
        // recycle gif if set
        Drawable pic = this.getDrawable();
        if (pic instanceof GifDrawable) {
            GifDrawable recycle = (GifDrawable) pic;
            if (!recycle.isRecycled()) {
                recycle.recycle();
            }
        }
    }

    private static int measureSize(int measureSpec, int defaultSize) {
        int result = defaultSize;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case View.MeasureSpec.UNSPECIFIED:
            case View.MeasureSpec.AT_MOST:
                break;
            case View.MeasureSpec.EXACTLY:
                result = specSize;
                result = Math.max(result, defaultSize);
                break;
        }
        return result;
    }

    private void setMargin() {
        if (!hasMargin){
            if (getLayoutParams() != null){
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) getLayoutParams();
                int leftMargin = lp.leftMargin;
                int topMargin = lp.topMargin;
                int rightMargin = lp.rightMargin;
                int bottomMargin = lp.bottomMargin;
                lp.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
                requestLayout();
                hasMargin = true;
            }
        }
    }

}
