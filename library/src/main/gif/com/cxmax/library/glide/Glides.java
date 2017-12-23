package com.cxmax.library.glide;

import android.content.Context;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.bumptech.glide.load.model.stream.StreamStringLoader;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;

import java.io.IOException;
import java.io.InputStream;

import pl.droidsonroids.gif.GifDrawable;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 17-12-23.
 */

public class Glides {

    public static final StreamModelLoader<String> CACHE_ONLY = new StreamModelLoader<String>() {
        @Override
        public DataFetcher<InputStream> getResourceFetcher(final String model, int i, int i1) {
            return new DataFetcher<InputStream>() {
                @Override
                public InputStream loadData(Priority priority) throws Exception {
                    throw new IOException();
                }

                @Override
                public void cleanup() {

                }

                @Override
                public String getId() {
                    return model;
                }

                @Override
                public void cancel() {

                }
            };
        }
    };

    public static GenericRequestBuilder<String, InputStream, byte[], GifDrawable> gif(Context context) {
        return Glide
                .with(context)
                .using(new StreamStringLoader(context), InputStream.class)
                .from(String.class) // change this if you have a different model like a File and use StreamFileLoader above
                .as(byte[].class)
                .transcode(new GifDrawableByteTranscoder(), GifDrawable.class) // pass it on
                .diskCacheStrategy(DiskCacheStrategy.SOURCE) // cache original
                .decoder(new StreamByteArrayResourceDecoder())  // load original
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<byte[]>(new StreamByteArrayResourceDecoder()));
    }
}
