# FloatingView
Android 首页悬浮广告,可任意拖拽.滑动监听支持绑定RecyclerView和ScrollView\ViewPage,随上下\左右滑动隐藏\显示悬浮广告<br/>
<br/>
  只需在xml布局文件中设置max:cx_animation="true"，即可动画效果。<br/>
  <br/>
  but，todo 拖拽后的悬浮广告移动距离得重新计算，隐藏方向也得做判断，因此这个地方还需要完善。<br/>
  <br/>
  for example:<br/>
  \<com.cxmax.library.widget.FloatingView<br/>
            android:id="@+id/float_view"<br/>
            android:layout_width="wrap_content"<br/>
            android:layout_height="wrap_content"<br/>
            android:layout_margin="16dp"<br/>
            android:src="@mipmap/float_ad_default"<br/>
            max:cx_animation="false" /><br/>
2.支持Gif图片的播放（不使用Gilde）<br/>
<br/>
使用方法： <br/>GifDecoder.with(getActivity()).load(current_appAdStructItem.img_url, new GifDecoder.OnLoadGifListener() {<br/>
                @Override<br/>
                public void loadGifSuccess(File file) {<br/>
                    GifDecoder.with(getActivity()).load(file).into(mFloatingView);<br/>
                }<br/>

                @Override
                public void loadGifFailed() {
                    onGainAdError();
                }
            }).into(mFloatingView);
            
            
  3.效果图：<br/>
  ![image](https://raw.githubusercontent.com/cxMax/FloatingView/master/app/asset/profile.png)
  
