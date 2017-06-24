# FloatingView

### 介绍
Android 首页悬浮广告,可任意拖拽.滑动监听支持绑定RecyclerView和ScrollView\ViewPage,随上下\左右滑动隐藏\显示悬浮广告

只需在xml布局文件中设置max:cx_animation="true"，即可动画效果。

but，todo 拖拽后的悬浮广告移动距离得重新计算，隐藏方向也得做判断，因此这个地方还需要完善。

### sample:
```java
  <com.cxmax.library.widget.FloatingView
            android:id="@+id/float_view"<br/>
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_margin="16dp"
            android:src="@mipmap/float_ad_default"
            max:cx_animation="false" />
 ``` 
### 2.支持Gif图片的播放（不使用Gilde）
### 使用方法：
```java
GifDecoder.with(getActivity()).load(current_appAdStructItem.img_url, new GifDecoder.OnLoadGifListener() {
                @Override
                public void loadGifSuccess(File file) {
                    GifDecoder.with(getActivity()).load(file).into(mFloatingView);
                }

                @Override
                public void loadGifFailed() {
                    onGainAdError();
                }
            }).into(mFloatingView);
            
  ```     
  
### 3.效果图：
  
  ![image](https://raw.githubusercontent.com/cxMax/FloatingView/master/app/asset/profile.png)
  
### License
   Copyright (C) 2016 cxMax  
   Copyright (C) 2016 FloatingView

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
