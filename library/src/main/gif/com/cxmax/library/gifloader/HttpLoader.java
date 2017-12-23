package com.cxmax.library.gifloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by CaiXi on 16/5/23.
 */
public class HttpLoader {

    public static InputStream getInputStreanFormUrl(String param) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(param).openConnection();
        return conn.getInputStream();
    }

}
