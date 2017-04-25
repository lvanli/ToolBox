package com.lizhiguang.news.homepage;

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
import com.lizhiguang.news.adapter.RecyclerAdapter;
import com.lizhiguang.news.bean.NewsShortDetail;
import com.lizhiguang.news.recyclerViewUtil.SimpleRefreshHeaderCreator;
import com.lizhiguang.news.show.NewsShowDetailActivity;
import com.lizhiguang.news.util.LogUtil;
import com.mrw.wzmrecyclerview.AutoLoad.AutoLoadRecyclerView;
import com.mrw.wzmrecyclerview.PullToLoad.OnLoadListener;
import com.mrw.wzmrecyclerview.PullToRefresh.OnRefreshListener;

import java.util.List;

/**
 * Created by lizhiguang on 2017/4/5.
 */

public class NewsFirstFragment extends Fragment implements View.OnClickListener {
    Context mContext;
    ViewPager mViewPager;
    AutoLoadRecyclerView mRecyclerView;
    NewsHomePagerPresenter mPresenter;
    CarouselPagerAdapter mAdapter;
    RecyclerAdapter mRecyclerAdapter;

    public NewsFirstFragment(Context context) {
        mContext = context;
    }

    public NewsFirstFragment setPresenter(NewsHomePagerPresenter presenter) {
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
        View view = inflater.inflate(R.layout.news_recycle, null);
        mViewPager = (ViewPager) view.findViewById(R.id.news_recycle_viewPager);
        mAdapter = new CarouselPagerAdapter(mContext, mViewPager, this);
        mViewPager.setAdapter(mAdapter);

        mRecyclerAdapter = new RecyclerAdapter(mContext, this);
        mRecyclerView = (AutoLoadRecyclerView) view.findViewById(R.id.news_recycle_main);
        if (false) {
            mRecyclerAdapter.setMode(RecyclerAdapter.MODE_FALL);
            mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        } else {
            mRecyclerAdapter.setMode(RecyclerAdapter.MODE_NORMAL);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        }
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mRecyclerAdapter);
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
                mRecyclerAdapter.setData(details);
                mRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    public void addMainNews(final List<NewsShortDetail> details) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int count = mRecyclerAdapter.getItemCount();
                for (int i = 0; i < details.size(); i++)
                    mRecyclerAdapter.addData(details.get(i), count++);
                mRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        if (view instanceof ImageButton) {
            //viewpager
            showToast((String) view.getTag(R.id.tag_first));
            intent = new Intent(mContext, NewsShowDetailActivity.class);
            intent.putExtra("url", (String) view.getTag(R.id.tag_first));
            startActivity(intent);
        } else if (view instanceof TextView) {
            //shortDetail
            showToast((String) view.getTag());
            intent = new Intent(mContext, NewsShowDetailActivity.class);
            intent.putExtra("url", (String) view.getTag());
            startActivity(intent);
        }
    }

    public void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
}
