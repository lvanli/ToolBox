package com.example.musiclib.model;


import com.example.musiclib.util.LogUtil;
import com.example.musiclib.util.MemoryCacheUtil;
import com.example.musiclib.util.ScanfMusicUtil;

import java.io.File;

/**
 * Created by lizhiguang on 2017/7/11.
 */

public class LocalMusicListModel {
    public void getLocalMusicList(File file, ScanfMusicUtil.ScanResultCallback callback) {
        if (MemoryCacheUtil.getInstance().isPrepare()) {
            callback.scanResult(MemoryCacheUtil.getInstance().getCache());
            return;
        }
        LogUtil.d("no memory cache");
//        ScanfMusicUtil.scanMusicByFile(file,callback);
    }
}
