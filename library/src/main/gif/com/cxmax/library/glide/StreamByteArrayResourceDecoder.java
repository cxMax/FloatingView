package com.cxmax.library.glide;

import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bytes.BytesResource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @describe :
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 17-12-23.
 */

public class StreamByteArrayResourceDecoder implements ResourceDecoder<InputStream, byte[]> {

    @Override
    public Resource<byte[]> decode(InputStream source, int width, int height) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int count;
        while ((count = source.read(buffer)) != -1) {
            bytes.write(buffer, 0, count);
        }
        return new BytesResource(bytes.toByteArray());
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}
