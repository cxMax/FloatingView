package com.cxmax.floatingview.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cxmax.floatingview.R;
import com.cxmax.floatingview.recyclerview.DividerItemDecoration;
import com.cxmax.floatingview.recyclerview.RecyclerAdapter;
import com.cxmax.library.widget.FloatingView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FloatingView.OnFloatClickListener{
    private RecyclerView mRecyclerView;
    private List<String> mDatas;
    private FloatingView mFloatingView;
//    private List<View> mViews;
//    private ViewPager mViewPager;
//    private FloatingView mFloatingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFloatView();
//        initData();
//        initView();
        initData();
        initRecyclerView();
        initView();
    }
    private void initFloatView() {
        mFloatingView = (FloatingView) findViewById(R.id.float_view);
        mFloatingView.setOnFloatClickListener(this);
    }

//    private void initView() {
//        mViewPager = (ViewPager) findViewById(R.id.viewpager);
//        mViewPager.setAdapter(new ViewPagerAdapter(mViews));
//        mFloatingView.attachToViewPager(mViewPager);
//    }
//
//    private void initData() {
//        LayoutInflater inflater = LayoutInflater.from(this);
//        mViews = new ArrayList<View>();
//        mViews.add(inflater.inflate(R.layout.item_viewpager_one,null));
//        mViews.add(inflater.inflate(R.layout.item_viewpager_two,null));
//        mViews.add(inflater.inflate(R.layout.item_viewpager_three,null));
//    }

    @Override
    public void floatClick(View view) {
        Toast.makeText(this,"点击悬浮广告",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void floatCloseClick() {
        Toast.makeText(this,"关闭悬浮广告",Toast.LENGTH_SHORT).show();
    }


    class ViewPagerAdapter extends PagerAdapter {
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
    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(new RecyclerAdapter(mDatas,this));
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mDatas = new ArrayList<>();
        for (int i = 'A'; i < 'z'; i++)
        {
            mDatas.add("这是" + (char) i + "项");
        }
    }
    private void initView() {
        mFloatingView = (FloatingView) findViewById(R.id.float_view);
        mFloatingView.setOnFloatClickListener(this);
        mFloatingView.attachToRecyclerView(mRecyclerView);
    }

}
