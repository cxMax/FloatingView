package com.cxmax.floatingview.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cxmax.floatingview.R;
import com.cxmax.library.widget.FloatingView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caixi on 2016/6/11.
 */
public class ViewPagerActivity extends AppCompatActivity implements FloatingView.OnFloatClickListener{
    private List<View> mViews;
    private ViewPager mViewPager;
    private FloatingView mFloatingView;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_viewpager);
        initData();
        initView();
        initFloatView();
    }

    private void initFloatView() {
        mFloatingView = (FloatingView) findViewById(R.id.float_view);
        mFloatingView.setOnFloatClickListener(this);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new ViewPagerAdapter(mViews));
    }

    private void initData() {
        LayoutInflater inflater = LayoutInflater.from(this);
        mViews = new ArrayList<View>();
        mViews.add(inflater.inflate(R.layout.item_viewpager_one,null));
        mViews.add(inflater.inflate(R.layout.item_viewpager_two,null));
        mViews.add(inflater.inflate(R.layout.item_viewpager_three,null));
    }

    @Override
    public void floatClick(View view) {
        Toast.makeText(this,"hhhhhhh",Toast.LENGTH_SHORT).show();
    }


    class ViewPagerAdapter extends PagerAdapter{
        private List<View> mViews = new ArrayList<>();

        public ViewPagerAdapter(List<View> views) {
            this.mViews = views;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViews.get(position), 0);//添加页卡
            return mViews.get(position);
        }

        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
