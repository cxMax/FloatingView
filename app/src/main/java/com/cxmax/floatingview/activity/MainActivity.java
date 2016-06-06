package com.cxmax.floatingview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initRecyclerView();
        initView();
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

    @Override
    public void floatClick(View view) {
        Toast.makeText(this,"hhhhhhh",Toast.LENGTH_SHORT).show();
    }
}
