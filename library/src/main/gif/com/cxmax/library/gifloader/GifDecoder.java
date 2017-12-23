package com.cxmax.library.gifloader;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CaiXi on 16/5/23.
 * 加载gif
 */
public class GifDecoder {

    public static Context context;
    private static final String GIF_IMAGE_PATH_NAME = "FloatAdCache";
    private String mUrl;
    private HashMap<String,GifDrawer> mMap = new HashMap<String,GifDrawer>();
    private GifDrawer mFloatAdDrawer;//悬浮广告GIF实例
    private boolean isFloatAd;
    private boolean isFirstTime = true;

    public static class Gif {
        public static GifDecoder instance = new GifDecoder();
    }

    public static GifDecoder with(Context c) {
        context = c;
        return Gif.instance;
    }


    /**
     * load gif file form inputstream
     */
    public GifDrawer load(InputStream is) {
        if (isFloatAd && isFirstTime){
            mFloatAdDrawer = new GifDrawer();
            mFloatAdDrawer.setIs(is);
            isFirstTime = false;
            return mFloatAdDrawer;
        }else{
            GifDrawer drawer = new GifDrawer();
            if (mUrl != null && !"".equals(mUrl) && !mMap.containsKey(mUrl)){
                mMap.put(mUrl,drawer);
            }
            drawer.setIs(is);
            return drawer;
        }
    }

    public HashMap<String,GifDrawer> getMap(){
        return mMap;
    }

    /**
     * load gif file form uri
     */
    public GifDrawer load(Uri uri) {
        InputStream is = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return load(is);
    }

    /**
     * load gif file form sdcard
     */
    public GifDrawer load(File file) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return load(is);
    }

    /**
     * 这个是使用Volley等下载，物理缓存
     */
    public GifDrawer load(final String url ,final OnLoadGifListener onLoadGifListener) {
        this.isFloatAd = true;
        this.mUrl = null;
        FileInputStream is = null;
        final String path = context.getCacheDir().getPath() + File.separator + getGifImagePath(url);
        final File file = new File(path);
        if (file.exists()) {
            try {
                is = new FileInputStream(file);
                return load(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
           /**不存在 先下载下来,具体可使用OkHttp，Volley等三方库来实现物理缓存**/
//            ImageLoaderManager.getInstance(context , ImageLoaderManager.GIF_IMAGE_PATH_NAME).getFileLoader().get(url, new FileLoader.FileListener() {
//                @Override
//                public void onResponse(FileLoader.FileContainer fileContainer, boolean isImmediate) {
//                    onLoadGifListener.loadGifSuccess(file);
//                }
//
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    onLoadGifListener.loadGifFailed();
//                }
//            });
        }

        return load(file);
    }


    /**
     * load gif file form url
     * 这个是在子线程去下载
     */
    public GifDrawer load(String url) {
        this.mUrl = url;
        FileInputStream is = null;
        String path = context.getCacheDir().getPath() + File.separator + getGifImagePath(url);

        File file = new File(path);
        if (file.exists()) {
            try {
                is = new FileInputStream(file);
                return load(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
//           不存在 先下载下来
            GifDrawer gifDrawer = new GifDrawer();
            GifLoaderTask loadGifTask = new GifLoaderTask(gifDrawer, context);
            loadGifTask.execute(url);
            return gifDrawer;
        }

        return load(is);
    }

    public interface OnLoadGifListener{
        void loadGifSuccess(File file);
        void loadGifFailed();
    }

    public GifDrawer getFloatAdDrawer(){
        return mFloatAdDrawer;
    }

    public void pauseGif() {
        HashMap<String, GifDrawer> map = getMap();
        if (!map.isEmpty()) {
            for (Map.Entry<String, GifDrawer> entry : map.entrySet()) {
                if (entry.getValue().getCanvas() != null){
                    entry.getValue().pauseMovie();
                }
            }
        }
    }

    public void awakeGif() {
        HashMap<String, GifDrawer> map = getMap();
        if (!map.isEmpty()) {
            for (Map.Entry<String, GifDrawer> entry : map.entrySet()) {
                if (entry.getValue().getRunnable() != null){
                    entry.getValue().awakeMovie();
                }
            }
        }
    }

    public void closeGif(){
        HashMap<String, GifDrawer> map = getMap();
        if (!map.isEmpty()) {
            for (Map.Entry<String, GifDrawer> entry : map.entrySet()) {
                if (entry.getValue().getCanvas() != null){
                    entry.getValue().closeMovie();
                }
            }
        }
    }

    public static String getGifImagePath(String url){
        if (TextUtils.isEmpty(url)){
            return GIF_IMAGE_PATH_NAME + File.separator;
        }
        return GIF_IMAGE_PATH_NAME + File.separator + String.valueOf(url.hashCode()) + ".0" ;
    }
}
