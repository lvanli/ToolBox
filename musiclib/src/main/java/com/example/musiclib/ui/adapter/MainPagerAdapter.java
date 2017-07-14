package com.example.musiclib.ui.adapter;

/**
 * Created by lizhiguang on 2017/5/3.
 */

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.musiclib.BaseFragment;

import java.util.List;

/**
 * Created by Lizhaotailang on 2016/8/10.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {


    private List<BaseFragment> mFragments;

    public MainPagerAdapter(FragmentManager fm,
                            List<BaseFragment> fragments) {
        super(fm);
        this.mFragments = fragments;

    }

    public BaseFragment getFragment(int position) {
        if (mFragments == null || position > mFragments.size())
            return null;
        return mFragments.get(position);
    }

    public void setFragments(List<BaseFragment> mFragments) {
        this.mFragments = mFragments;
    }

    @Override
    public BaseFragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragments.get(position).getTitle();
    }

}