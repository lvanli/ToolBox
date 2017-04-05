package com.lizhiguang.administrator.toolbox.Launcher;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Vector;

/**
 * Created by lizhiguang on 16/6/15.
 */
class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private Vector<Fragment> allViews;

    public Vector<Fragment> getAllViews() {
        return allViews;
    }

    public MyFragmentPagerAdapter(FragmentManager fm, Vector<Fragment> views) {
        super(fm);
        allViews = views;
    }

    @Override
    public Fragment getItem(int position) {
        return allViews.get(position);
    }

    @Override
    public int getCount() {
        return allViews.size();
    }
}
