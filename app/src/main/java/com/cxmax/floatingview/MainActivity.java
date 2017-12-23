package com.cxmax.floatingview;

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
import com.cxmax.library.FloatingView;
import com.cxmax.library.IFloatingView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IFloatingView.OnClickListener{
    private RecyclerView recyclerView;
    private List<String> data;
    private FloatingView floatingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFloatView();
        initData();
        initRecyclerView();
    }
    private void initFloatView() {
        floatingView = (FloatingView) findViewById(R.id.float_view);
        floatingView.setClickListener(this);
    }

    @Override
    public void onFloatClick(View v) {
        Toast.makeText(this,"点击悬浮广告",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCloseClick() {
        Toast.makeText(this,"关闭悬浮广告",Toast.LENGTH_SHORT).show();

    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(new RecyclerAdapter(data,this));
    }

    /**
     * 初始化数据
     */
    private void initData() {
        data = new ArrayList<>();
        for (int i = 'A'; i < 'z'; i++)
        {
            data.add("这是" + (char) i + "项");
        }
    }
}
