package com.lizhiguang.news.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lizhiguang.news.R;
import com.lizhiguang.news.bean.NewsShortDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhiguang on 2017/4/17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter {
    public static final int MODE_NORMAL = 0;
    public static final int MODE_FALL = 1;
    private Context mContext;
    private int mode = MODE_NORMAL;
    private List<NewsShortDetail> datas;
    private List<Integer> mHeights;
    private View.OnClickListener mListener;
    public RecyclerAdapter (Context context,View.OnClickListener listener) {
        mContext = context;
        mListener = listener;
    }
    public void setData(List<NewsShortDetail> details) {
        mHeights = new ArrayList<>(details.size());
        for (int i=0;i<details.size();i++) {
            mHeights.add((int)(Math.random()*300+200));
        }
        datas = details;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
    public void addData(NewsShortDetail data,int position) {
        datas.add(position,data);
        mHeights.add(position,(int)(Math.random()*300+200));
        notifyItemInserted(position);
    }
    public void deleteData(int position) {
        datas.remove(position);
        mHeights.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (mode == MODE_FALL) {
            view = LayoutInflater.from(mContext).inflate(R.layout.news_recycler_fall_item, parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.news_recycler_linear_item, parent, false);
        }
        MyHolder holder = new MyHolder(view,mode);
//        parent.addView(view);
//        LogUtil.d("p="+view.getParent());
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyHolder)holder).tv.setText(datas.get(position).getTitle());
        if (mode == MODE_FALL) {
            ViewGroup.LayoutParams lp = ((MyHolder)holder).itemView.getLayoutParams();
            lp.height = mHeights.get(position);
            ((MyHolder)holder).itemView.setLayoutParams(lp);
        }
        ((MyHolder)holder).tv.setTag(datas.get(position).getUrl());
        ((MyHolder)holder).tv.setOnClickListener(mListener);
        Glide.with(mContext).load(datas.get(position).getPath())
                .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(((MyHolder)holder).iv);
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView tv;
        ImageView iv;
        public MyHolder(View itemView,int mode) {
            super(itemView);
            if (mode == MODE_FALL) {
                tv = (TextView) itemView.findViewById(R.id.news_recycle_fall_item_text);
                iv = (ImageView) itemView.findViewById(R.id.news_recycle_fall_item_image);
            } else {
                tv = (TextView) itemView.findViewById(R.id.news_recycle_linear_item_text);
                iv = (ImageView) itemView.findViewById(R.id.news_recycle_linear_item_image);
            }
            tv.getPaint().setFakeBoldText(true);
        }
    }
    public NewsShortDetail getItemData(int position) {
        if (position < datas.size())
            return datas.get(position);
        else
            return null;
    }

    @Override
    public int getItemCount() {
        if (datas != null)
            return datas.size();
        else
            return 0;
    }
}
