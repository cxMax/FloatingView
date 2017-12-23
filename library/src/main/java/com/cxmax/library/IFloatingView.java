package com.cxmax.library;

import android.support.annotation.ColorInt;
import android.view.View;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 17-12-23.
 */

public interface IFloatingView {

    void setCloseColor(@ColorInt int color);

    void release();

    public interface OnClickListener {
        void onFloatClick(View v);

        void onCloseClick();
    }

}
