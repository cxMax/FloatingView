package com.cxmax.floatingview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cxmax.library.FloatingView;

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
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(new RecyclerAdapter(mDatas));
        initView();
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

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{
        private List<String> datas;

        public RecyclerAdapter(List<String> datas) {
            this.datas = datas;
        }

        @Override
        public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.item_recyclerview,parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerHolder holder, int position) {
            holder.tv.setText(datas.get(position));
        }


        @Override
        public int getItemCount() {
            return datas.size();
        }

        class RecyclerHolder extends RecyclerView.ViewHolder{

            TextView tv;

            public RecyclerHolder(View view) {
                super(view);
                tv = (TextView) view.findViewById(R.id.item_recyclerview_tv);
            }
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
