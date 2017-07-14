package com.example.musiclib.util;

import com.example.musiclib.bean.AbstractMusic;

import java.util.List;

/**
 * Created by lizhiguang on 2017/7/12.
 */

public class MemoryCacheUtil {
    private static MemoryCacheUtil instance = null;
    private List<AbstractMusic> mMusics = null;

    private MemoryCacheUtil() {
    }

    public static MemoryCacheUtil getInstance() {
        if (instance == null)
            synchronized (MemoryCacheUtil.class) {
                if (instance == null)
                    instance = new MemoryCacheUtil();
            }
        return instance;
    }

    public synchronized boolean isPrepare() {
        return mMusics != null && mMusics.size() != 0;
    }

    public synchronized List<AbstractMusic> getCache() {
        return mMusics;
    }

    public synchronized void setCache(List<AbstractMusic> musics) {
        mMusics = musics;
    }

    public synchronized void clearCache() {
        if (mMusics != null)
            mMusics.clear();
        mMusics = null;
    }
}
