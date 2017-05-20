package com.lizhiguang.news.homepage.zhihu;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lizhiguang.news.R;
import com.lizhiguang.news.adapter.CarouselPagerAdapter;
import com.lizhiguang.news.adapter.NewsShortAdapter;
import com.lizhiguang.news.bean.NewsShortDetail;
import com.lizhiguang.news.util.recycler.SimpleRefreshHeaderCreator;
import com.lizhiguang.news.show.NewsShowHtmlActivity;
import com.lizhiguang.news.util.LogUtil;
import com.mrw.wzmrecyclerview.AutoLoad.AutoLoadRecyclerView;
import com.mrw.wzmrecyclerview.PullToLoad.OnLoadListener;
import com.mrw.wzmrecyclerview.PullToRefresh.OnRefreshListener;

import java.util.List;

/**
 * Created by lizhiguang on 2017/4/5.
 */

public class NewsZhiHuFragment extends Fragment implements View.OnClickListener {
    Context mContext;
    ViewPager mViewPager;
    AutoLoadRecyclerView mRecyclerView;
    NewsZhiHuPresenter mPresenter;
    CarouselPagerAdapter mAdapter;
    NewsShortAdapter mNewsShortAdapter;

    public NewsZhiHuFragment(Context context) {
        mContext = context;
    }

    public NewsZhiHuFragment setPresenter(NewsZhiHuPresenter presenter) {
        this.mPresenter = presenter;
        presenter.setView(this);
        return this;
    }

    @Override
    public void onAttach(Context context) {
        mContext = context;
        LogUtil.d("mC=" + mContext);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_zhihu_fragment, null);
        mViewPager = (ViewPager) view.findViewById(R.id.news_recycle_viewPager);
        mAdapter = new CarouselPagerAdapter(mContext, mViewPager, this);
        mViewPager.setAdapter(mAdapter);

        mNewsShortAdapter = new NewsShortAdapter(mContext, this);
        mRecyclerView = (AutoLoadRecyclerView) view.findViewById(R.id.news_recycle_main);
        if (false) {
            mNewsShortAdapter.setMode(NewsShortAdapter.MODE_FALL);
            mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        } else {
            mNewsShortAdapter.setMode(NewsShortAdapter.MODE_NORMAL);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        }
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mNewsShortAdapter);
        mRecyclerView.setOnLoadListener(new OnLoadListener() {
            @Override
            public void onStartLoading(int skip) {
                mPresenter.loadNews();
                mRecyclerView.completeLoad();
            }
        });
        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onStartRefreshing() {
                mPresenter.refreshNews();
                mRecyclerView.completeRefresh();
            }
        });
        mRecyclerView.setRefreshViewCreator(new SimpleRefreshHeaderCreator());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    public void showMainPictures(final List<NewsShortDetail> pictures) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.setDetails(pictures);
                mAdapter.notifyDataSetChanged();
                mViewPager.setCurrentItem(pictures.size() + 1, false);
            }
        });
    }

    public void showMainNews(final List<NewsShortDetail> details) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mNewsShortAdapter.setData(details);
                mNewsShortAdapter.notifyDataSetChanged();
            }
        });
    }

    public void addMainNews(final List<NewsShortDetail> details) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int count = mNewsShortAdapter.getItemCount();
                for (int i = 0; i < details.size(); i++)
                    mNewsShortAdapter.addData(details.get(i), count++);
                mNewsShortAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        if (view instanceof ImageButton) {
            //viewpager
            showToast((String) view.getTag(R.id.tag_first));
            intent = new Intent(mContext, NewsShowHtmlActivity.class);
            intent.putExtra("url", (String) view.getTag(R.id.tag_first));
            startActivity(intent);
        } else if (view instanceof TextView) {
            //shortDetail
            showToast((String) view.getTag());
            intent = new Intent(mContext, NewsShowHtmlActivity.class);
            intent.putExtra("url", (String) view.getTag());
            startActivity(intent);
        }
    }

    public void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
}
