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

public class NewsShortAdapter extends BaseRecyclerAdapter<NewsShortDetail> {

    public NewsShortAdapter(Context context, View.OnClickListener listener) {
        super(context, listener);
    }

    @Override
    String getTitleInPosition(int position) {
        return getItemData(position).getTitle();
    }

    @Override
    String getTagInPosition(int position) {
        return getItemData(position).getUrl();
    }

    @Override
    String getImgInPosition(int position) {
        return getItemData(position).getImagePath();
    }
}
