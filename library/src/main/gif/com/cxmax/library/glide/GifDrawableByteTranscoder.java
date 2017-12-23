package com.cxmax.library.glide;

import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 17-12-23.
 */

public class GifDrawableByteTranscoder implements ResourceTranscoder<byte[], GifDrawable> {
    @Override
    public Resource<GifDrawable> transcode(Resource<byte[]> toTranscode) {
        try {
            return new GifDrawableResource(new GifDrawable(toTranscode.get()));
        } catch (IOException ex) {
            return null;
        }
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}
