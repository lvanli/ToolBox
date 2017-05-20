package com.lizhiguang.news.homepage.movie;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lizhiguang.news.R;
import com.lizhiguang.news.adapter.NewsHotMovieAdapter;
import com.lizhiguang.news.bean.NewsHotMovies;
import com.lizhiguang.news.util.LogUtil;

import java.util.List;

/**
 * Created by lizhiguang on 2017/4/26.
 */

public class NewsMovieFragment extends Fragment implements View.OnClickListener {
    private Context mContext;
    private NewsMoviePresenter mPresenter;
    private RecyclerView mRecyclerView;
    private NewsHotMovieAdapter mRecyclerAdapter;
    public NewsMovieFragment(Context context) {
        mContext = context;
    }
    public NewsMovieFragment setPresenter(NewsMoviePresenter presenter) {
        mPresenter = presenter;
        mPresenter.attachView(this);
        return this;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        LogUtil.d("");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_movie_fragment,null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.news_movie_recycler);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        mRecyclerAdapter.setMode(NewsHotMovieAdapter.MODE_FALL);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mRecyclerAdapter);
        LogUtil.d("");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
        LogUtil.d("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d("");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.d("");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.d("");
    }

    public void init() {
        mRecyclerAdapter = new NewsHotMovieAdapter(mContext,this);
    }

    public void showHotMovies(List<NewsHotMovies> hotMovies) {
        mRecyclerAdapter.setData(hotMovies);
        mRecyclerAdapter.notifyDataSetChanged();
    }

    public void showToast(String msg) {
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        String tag = (String)v.getTag();
        showToast(tag);
    }
}
