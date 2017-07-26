package com.lizhiguang.news.adapter;

import android.content.Context;
import android.view.View;

import com.lizhiguang.news.bean.NewsHotMovies;

/**
 * Created by lizhiguang on 2017/5/2.
 */

public class NewsHotMovieAdapter extends BaseRecyclerAdapter<NewsHotMovies> {
    public NewsHotMovieAdapter(Context context, View.OnClickListener listener) {
        super(context, listener);
        showText(false);
    }

    @Override
    String getTitleInPosition(int position) {
        return getItemData(position).getName();
    }

    @Override
    String getTagInPosition(int position) {
        return String.valueOf(getItemData(position).getName());
    }

    @Override
    String getImgInPosition(int position) {
        return getItemData(position).getImgPath();
    }
}
