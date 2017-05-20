package com.lizhiguang.news.resource;

import com.lizhiguang.news.bean.NewsShortDetail;

import java.util.List;

/**
 * Created by lizhiguang on 2017/4/17.
 */

public interface NewsResource {

    void getMainNews(String tag, OnNewsShortListener listener);

    void getDaysNews(String data, OnNewsShortListener listener);

    void getNewsDetail(String url, OnNewsDetailListener listener);

    void cacheTodayNews(String tag);

    void cacheNews(String data);

    void recycleCache(String data);

    void recycleAllCache();

    interface OnNewsDetailListener extends OnNewsRequestListener<String> {
    }

    interface OnNewsShortListener extends OnNewsRequestListener<List<NewsShortDetail>> {
    }

    interface OnNewsRequestListener<T> {
        void Result(T result);
    }
}
