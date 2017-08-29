package com.lizhiguang.news.util.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jimi_wu.ptlrecyclerview.DefaultHeaderAndFooterCreator.DefaultRefreshHeaderCreator;
import com.lizhiguang.news.R;

/**
 * Created by lizhiguang on 2017/4/21.
 */

public class SimpleRefreshHeaderCreator extends DefaultRefreshHeaderCreator {
    private ImageView iv;
    private TextView tv;

    @Override
    public View getRefreshView(Context context, RecyclerView recyclerView) {
        View view = super.getRefreshView(context, recyclerView);
        iv = (ImageView) view.findViewById(R.id.iv);
        tv = (TextView) view.findViewById(R.id.tv);
        setVisibility(View.GONE);
        return view;
    }

    @Override
    public boolean onStartPull(float distance, int lastState) {
        super.onStartPull(distance, lastState);
        setVisibility(View.GONE);
        return true;
    }

    @Override
    public boolean onReleaseToRefresh(float distance, int lastState) {
        super.onReleaseToRefresh(distance, lastState);
        setVisibility(View.VISIBLE);
        return true;
    }

    @Override
    public void onStartRefreshing() {
        super.onStartRefreshing();
        setVisibility(View.GONE);
    }

    private void setVisibility(int visibility) {
        iv.setVisibility(visibility);
        tv.setVisibility(visibility);
    }
}
