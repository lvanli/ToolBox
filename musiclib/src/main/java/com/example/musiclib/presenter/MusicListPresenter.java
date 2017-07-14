package com.example.musiclib.presenter;

import android.os.Environment;

import com.example.musiclib.bean.AbstractMusic;
import com.example.musiclib.model.LocalMusicListModel;
import com.example.musiclib.util.LogUtil;
import com.example.musiclib.util.ScanfMusicUtil;

import java.util.List;

/**
 * Created by lizhiguang on 2017/7/11.
 */

public class MusicListPresenter extends BasePresenter implements ScanfMusicUtil.ScanResultCallback {
    LocalMusicListModel mMusicModel;

    public MusicListPresenter() {
        mMusicModel = new LocalMusicListModel();
    }

    @Override
    public void start() {
        mMusicModel.getLocalMusicList(Environment.getExternalStorageDirectory(), this);
    }

    @Override
    public void reload() {
        mMusicModel.getLocalMusicList(Environment.getExternalStorageDirectory(), this);
    }

    @Override
    public void scanResult(List<AbstractMusic> musics) {
        if (musics != null) {
            LogUtil.d("first=" + musics.get(0).toString());
            mView.updateData(musics);
        } else {
            mView.showToast("未搜索到");
        }
    }
}
