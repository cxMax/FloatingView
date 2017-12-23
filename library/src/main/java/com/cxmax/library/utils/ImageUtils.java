package com.cxmax.library.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 17-12-23.
 */

public class ImageUtils {

    public static LayerDrawable createLayerDrawable(@NonNull Drawable upper, @NonNull Drawable lower) {
        Drawable[] layers = new Drawable[2];
        layers[0] = lower;
        layers[1] = upper;
        return new LayerDrawable(layers);
    }

    public static LayerDrawable drawColorOnLayer(LayerDrawable transform, @ColorInt int color) {
        Drawable background = transform.getDrawable(0);
        background.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        return transform;
    }

    public static Bitmap drawableToBitmap(@NonNull Drawable drawable, int width, int height, @NonNull Bitmap.Config config) {
        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, config);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), config);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }
}
