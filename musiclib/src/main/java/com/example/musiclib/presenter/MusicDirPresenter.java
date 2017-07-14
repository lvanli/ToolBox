package com.example.musiclib.presenter;

import android.os.Environment;

import com.example.musiclib.bean.AbstractMusic;
import com.example.musiclib.model.LocalMusicDirListModel;

import java.util.List;

/**
 * Created by lizhiguang on 2017/7/11.
 */

public class MusicDirPresenter extends BasePresenter implements LocalMusicDirListModel.DataResultCallback {
    private LocalMusicDirListModel mMusicModel;

    public MusicDirPresenter() {
        mMusicModel = new LocalMusicDirListModel();
    }

    @Override
    public void start() {
        mMusicModel.getLocalMusicDirList(Environment.getExternalStorageDirectory(), this);
    }

    @Override
    public void reload() {
        mMusicModel.getLocalMusicDirList(Environment.getExternalStorageDirectory(), this);
    }

    @Override
    public void dataResult(List<List<AbstractMusic>> musics) {
        if (musics != null) {
            mView.updateData(musics);
        } else {
            mView.showToast("未搜索到");
        }
    }
}
