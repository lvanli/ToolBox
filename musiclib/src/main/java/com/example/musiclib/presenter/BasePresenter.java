package com.example.musiclib.presenter;

import com.example.musiclib.BaseFragment;
import com.example.musiclib.bean.LocalMusicInfo;
import com.example.musiclib.proxy.LocalMusicManager;

import java.util.List;

/**
 * Created by lizhiguang on 2017/5/3.
 */

public abstract class BasePresenter {
    protected BaseFragment mView;
    protected LocalMusicManager mMusicManager = LocalMusicManager.getInstance();

    public BasePresenter() {
    }

    public void setView(BaseFragment mView) {
        this.mView = mView;
    }

    public abstract void start();

    public abstract void reload();

    public void playMusicList(List<LocalMusicInfo> infos, int position) {
        mMusicManager.preparePlayingList(infos, position);
    }
}
