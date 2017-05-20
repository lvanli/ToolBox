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
import com.lizhiguang.news.util.LogUtil;

import java.util.List;

/**
 * Created by lizhiguang on 2017/5/2.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter {
    public static final int MODE_NORMAL = 0;
    public static final int MODE_FALL = 1;
    private Context mContext;
    private int mode = MODE_NORMAL;
    private List<T> data;
    private View.OnClickListener mListener;
    private boolean showText = true;
    private int mBasicHeight = 0;

    BaseRecyclerAdapter(Context context, View.OnClickListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void showText(boolean show) {
        showText = show;
    }

    public void setData(List<T> details) {
        data = details;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void addData(T data, int position) {
        this.data.add(position, data);
        notifyItemInserted(position);
    }

    public void deleteData(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public T getItemData(int position) {
        if (position < data.size())
            return data.get(position);
        else
            return null;
    }

    @Override
    public int getItemCount() {
        if (data != null)
            return data.size();
        else
            return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (mode == MODE_FALL) {
            view = LayoutInflater.from(mContext).inflate(R.layout.news_recycler_fall_item, parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.news_recycler_linear_item, parent, false);
        }
        BaseRecyclerAdapter.MyHolder holder = new BaseRecyclerAdapter.MyHolder(view, mode);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String tag = getTagInPosition(position);
        ((BaseRecyclerAdapter.MyHolder) holder).tv.setText(getTitleInPosition(position));
        ((BaseRecyclerAdapter.MyHolder) holder).tv.setTag(tag);
        ((BaseRecyclerAdapter.MyHolder) holder).tv.setOnClickListener(mListener);
        if (mode == MODE_FALL) {
            ViewGroup.LayoutParams lp = ((MyHolder) holder).itemView.getLayoutParams();
            if (mBasicHeight == 0)
                mBasicHeight = lp.height;
            lp.height = mBasicHeight+ tag.hashCode() % 200;
            LogUtil.d("height="+lp.height);
            ((MyHolder)holder).itemView.setLayoutParams(lp);
            Glide.with(mContext).load(getImgInPosition(position))
                    .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(((BaseRecyclerAdapter.MyHolder) holder).iv);
        }
        else
            Glide.with(mContext).load(getImgInPosition(position))
                    .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(((BaseRecyclerAdapter.MyHolder) holder).iv);
    }

    abstract String getTitleInPosition(int position);
    abstract String getTagInPosition(int position);
    abstract String getImgInPosition(int position);

    class MyHolder extends RecyclerView.ViewHolder {
        TextView tv;
        ImageView iv;

        public MyHolder(View itemView, int mode) {
            super(itemView);
            if (mode == MODE_FALL) {
                tv = (TextView) itemView.findViewById(R.id.news_recycle_fall_item_text);
                iv = (ImageView) itemView.findViewById(R.id.news_recycle_fall_item_image);
            } else {
                tv = (TextView) itemView.findViewById(R.id.news_recycle_linear_item_text);
                iv = (ImageView) itemView.findViewById(R.id.news_recycle_linear_item_image);
            }
            tv.getPaint().setFakeBoldText(true);
            if (!showText)
                tv.setVisibility(View.GONE);
        }
    }

}