package com.cxmax.floatingview.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cxmax.floatingview.R;

import java.util.List;

/**
 * Created by Administrator on 2016/6/7.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>{
    private List<String> datas;
    private Context mContext;

    public RecyclerAdapter(List<String> datas, Context context) {
        this.datas = datas;
        this.mContext = context;
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerHolder holder = new RecyclerHolder(LayoutInflater.from(mContext)
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
