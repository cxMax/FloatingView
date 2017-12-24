# FloatingView

### 介绍
Android 首页悬浮广告,可任意拖拽, 支持Gif图片的播放（不使用Gilde播放 / 使用glide播放优化gif内存占用问题）  
继承ImageView, 拥有与ImageView一样的api

### 功能
* 显示/隐藏
* 拖拽 - 只需在xml布局文件中设置app:draggable="true"，即可。

### 用法:
```java
  <com.cxmax.library.FloatingView
        android:id="@+id/float_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="16dp"
        android:src="@mipmap/float_ad_default"
        app:draggable="true" />
 ``` 
### 关于Gif图片的播放
* 不使用Gilde： 
ps : gif图的播放在java层实现, 内存和性能表现并不好.
```java
GifDecoder.with(getActivity()).load(current_appAdStructItem.img_url, new GifDecoder.OnLoadGifListener() {
                @Override
                public void loadGifSuccess(File file) {
                    GifDecoder.with(getActivity()).load(file).into(mFloatingView);
                }

                @Override
                public void loadGifFailed() {
                    // fail
                }
            }).into(mFloatingView);
            
  ```     
* 使用Glide 
1. 引用第三方库, 让gif播放在Native层实现, 避免java层内存增长和性能问题
```
compile 'pl.droidsonroids.gif:android-gif-drawable:1.2.7'
```
2. 引用Glide, 并改写Glide做了上层封装, 用法跟glide网络加载普通图片一样.  
使用第三方库的GifDrawable(native层实现)替换Glide的GifDrawable(java层实现) , 具体做了封装, 有兴趣的话看library的实现;
```
            Glide
                .with(context)
                .using(new StreamStringLoader(context), InputStream.class)
                .from(String.class) 
                .as(byte[].class)
                .transcode(new GifDrawableByteTranscoder(), GifDrawable.class) 
                .diskCacheStrategy(DiskCacheStrategy.SOURCE) 
                .decoder(new StreamByteArrayResourceDecoder())  
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<byte[]>(new StreamByteArrayResourceDecoder()))
                .load(gifUrl)
                .error(placeholder)
                .fallback(placeholder)
                .into(imageView);
```

### 效果图：
  
  ![image](https://raw.githubusercontent.com/cxMax/FloatingView/master/app/asset/profile.png)
  
### License MIT
   Copyright (C) 2016 cxMax  
   Copyright (C) 2016 FloatingView
