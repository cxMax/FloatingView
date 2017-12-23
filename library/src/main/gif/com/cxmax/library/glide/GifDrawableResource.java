package com.cxmax.library.glide;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.bumptech.glide.load.engine.Resource;

import pl.droidsonroids.gif.GifDrawable;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 17-12-23.
 */

public class GifDrawableResource implements Resource<GifDrawable> {

    private GifDrawable drawable;

    GifDrawableResource(GifDrawable gifDrawable) {
        this.drawable = gifDrawable;
    }

    @Override
    public GifDrawable get() {
        return drawable;
    }

    @Override
    public int getSize() {
        return (int) drawable.getInputSourceByteCount();
    }

    @Override
    public void recycle() {
        drawable.stop();
        drawable.recycle();
    }
}
