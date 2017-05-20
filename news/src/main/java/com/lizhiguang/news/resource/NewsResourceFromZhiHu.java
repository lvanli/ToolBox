package com.lizhiguang.news.resource;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.lizhiguang.news.bean.NewsShortDetail;
import com.lizhiguang.news.service.DownloadIntentService;
import com.lizhiguang.news.util.LogUtil;
import com.lizhiguang.news.util.cache.FileCacheUtil;

import java.io.File;
import java.util.List;


/**
 * Created by lizhiguang on 2017/4/17.
 */

public class NewsResourceFromZhiHu extends NewsResourceNetZhiHu implements NewsResource.OnNewsShortListener {
    private FileCacheUtil mCacheUtil;
    private Context mContext;

    public NewsResourceFromZhiHu(Context context) {
        super(context);
        mContext = context;
        mCacheUtil = new FileCacheUtil(context);
    }

    @Override
    public void getMainNews(String tag, OnNewsShortListener listener) {
        if (mCacheUtil.isExist(tag)) {
            Object o = mCacheUtil.getCache(tag);
            if (o != null && o instanceof List) {
                listener.Result((List<NewsShortDetail>) o);
                return;
            }
        }
        super.getMainNews(tag, new OnNewsShortCacheListener(tag, listener));
    }

    @Override
    public void getDaysNews(String data, OnNewsShortListener listener) {
        if (mCacheUtil.isExist(data)) {
            Object o = mCacheUtil.getCache(data);
            if (o != null && o instanceof List) {
                listener.Result((List<NewsShortDetail>) o);
                return;
            }
        }
        super.getDaysNews(data, new OnNewsShortCacheListener(data, listener));
    }

    @Override
    public void getNewsDetail(String url, OnNewsDetailListener listener) {
        String tag = getIdFromUrl(url);
        if (mCacheUtil.isExist(tag)) {
            Object o = mCacheUtil.getCache(tag);
            if (o != null && o instanceof String) {
                listener.Result((String) o);
                return;
            }
        }
        super.getNewsDetail(url, new OnNewsDetailCacheListener(tag, listener));
    }

    @Override
    public void cacheTodayNews(String tag) {
        super.getMainNews(tag, new OnNewsShortCacheListener(tag, this));
    }

    @Override
    public void cacheNews(String data) {
        super.getDaysNews(data, new OnNewsShortCacheListener(data, this));
    }

    @Override
    public void recycleCache(String data) {
        mCacheUtil.recycleCache(data);
    }

    @Override
    public void recycleAllCache() {
        mCacheUtil.clearCache();
    }

    public String getIdFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    @Override
    public void Result(List<NewsShortDetail> result) {
        if (result != null) {
            boolean download = false;
            for (int i = 0; i < result.size(); i++) {
                LogUtil.d("cacheUrl=" + result.get(i).getUrl() + ",path=" + result.get(i).getImagePath() + ",context=" + mContext);
                String tag = getIdFromUrl(result.get(i).getUrl());
                if (!mCacheUtil.isExist(tag)) {
                    download = true;
                    super.getNewsDetail(result.get(i).getUrl(), new OnNewsDetailCacheListener(getIdFromUrl(result.get(i).getUrl()), null, true));
                    Glide.with(mContext).load(result.get(i).getImagePath()).downloadOnly(200, 200);
                }
            }
            if (!download) {
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(DownloadIntentService.ACTION_DOWNLOAD_OVER));
            }
        }
    }

    private class OnNewsShortCacheListener implements OnNewsShortListener {
        OnNewsShortListener mListener;
        String mTag;

        OnNewsShortCacheListener(String tag, OnNewsShortListener listener) {
            mTag = tag;
            mListener = listener;
        }

        @Override
        public void Result(List<NewsShortDetail> result) {
            mCacheUtil.putCache(mTag, result);
            if (mListener != null)
                mListener.Result(result);
        }
    }

    private class OnNewsDetailCacheListener implements OnNewsDetailListener {
        private OnNewsDetailListener mListener;
        private String mTag;
        private boolean mCachePhoto;

        OnNewsDetailCacheListener(String tag, OnNewsDetailListener listener) {
            mTag = tag;
            mListener = listener;
            mCachePhoto = false;
        }

        OnNewsDetailCacheListener(String tag, OnNewsDetailListener listener, boolean cachePhoto) {
            mTag = tag;
            mListener = listener;
            mCachePhoto = cachePhoto;
        }

        @Override
        public void Result(String result) {
            if (mListener != null)
                mListener.Result(result);
            if (mCachePhoto)
                saveHtml(result);
            else
                mCacheUtil.putCache(mTag, result);
        }

        private void saveHtml(String result) {
            int p = 0, b, e;
            while (p >= 0) {
                p = result.indexOf("<img", p);
                if (p != -1) {
                    p = result.indexOf("src=", p);
                    b = result.indexOf("\"", p);
                    e = result.indexOf("\"", b + 1);
                    String imagePath = result.substring(b + 1, e);
                    p = b;
                    LogUtil.d("imagePath=" + imagePath);
                    result = result.replace(imagePath, getIdFromUrl(imagePath));
                    DownloadIntentService.startDownloadImg(mContext, imagePath, mCacheUtil.getDiskCachePath(mContext) + File.separator + getIdFromUrl(imagePath));
                }
            }
            mCacheUtil.putCache(mTag, result);
        }
    }
}
