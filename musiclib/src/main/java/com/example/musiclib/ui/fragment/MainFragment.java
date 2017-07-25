package com.example.musiclib.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.musiclib.BaseFragment;
import com.example.musiclib.R;
import com.example.musiclib.bean.AbstractMusic;
import com.example.musiclib.defines.LocalBroadcastDefine;
import com.example.musiclib.presenter.MusicDirPresenter;
import com.example.musiclib.presenter.MusicListPresenter;
import com.example.musiclib.ui.adapter.MainPagerAdapter;
import com.example.musiclib.util.DatabaseUtil;
import com.example.musiclib.util.MemoryCacheUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhiguang on 2017/7/11.
 */

public class MainFragment extends Fragment implements LocalBroadcastDefine, DatabaseUtil.DatabaseResultCallback {
    private Context context;
    private int mCurrentPage = 0;
    private List<BaseFragment> mFragments;
    private ProgressBar loading;
    private ViewPager pager;
    private MainPagerAdapter mAdapter;
    private BroadcastReceiver receiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_main, container, false);
        init(view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    private void init(View view) {
        pager = (ViewPager) view.findViewById(R.id.music_main_pager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.music_tab_layout);
        loading = (ProgressBar) view.findViewById(R.id.music_loading);
        pager.setOffscreenPageLimit(3);
        mFragments = new ArrayList<BaseFragment>();
        mFragments.add(new MusicListFragment(context).setPresenter(new MusicListPresenter()));
        mFragments.add(new MusicDirFragment(context).setPresenter(new MusicDirPresenter()));
        mAdapter = new MainPagerAdapter(getChildFragmentManager(), mFragments);
        pager.setAdapter(mAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setupWithViewPager(pager);
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(INTENT_SEARCH_ERROR);
        filter.addAction(INTENT_RELOAD);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver,filter);
        DatabaseUtil.getInfo(context, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }

    public void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public void showLongToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void databaseResult(List<AbstractMusic> musics) {
        if (musics != null) {
            MemoryCacheUtil.getInstance().setCache(musics);
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(INTENT_RELOAD));
        } else {
            LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(INTENT_SEARCH_ERROR));
        }
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            loading.setVisibility(View.GONE);
            if (intent.getAction().equals(INTENT_RELOAD)) {
                pager.setVisibility(View.VISIBLE);
                for (int i = 0; i < mFragments.size(); i++)
                    mFragments.get(i).reload();
            }
        }
    }
}
