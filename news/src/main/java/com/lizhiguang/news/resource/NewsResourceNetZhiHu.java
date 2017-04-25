package com.lizhiguang.news.resource;

import android.content.Context;

import com.lizhiguang.news.util.volley.NewsHtmlRequest;
import com.lizhiguang.news.util.volley.NewsShortRequest;
import com.lizhiguang.news.util.volley.VolleyRequest;

/**
 * Created by lizhiguang on 2017/4/21.
 */

public abstract class NewsResourceNetZhiHu implements NewsResource{
    private static final String NEWS_PRE = "http://news-at.zhihu.com/api/4/news/";
    private static final String NEWS_BEFORE = "before/";
    private static final String NEWS_LATEST = "latest";
    VolleyRequest mRequest;
    public NewsResourceNetZhiHu(Context context) {
        mRequest = new VolleyRequest(context);
    }

    @Override
    public void getMainNews(String tag,OnNewsShortListener listener) {
        mRequest.getRequest(new NewsShortRequest(NEWS_PRE+NEWS_LATEST,listener));
    }

    @Override
    public void getDaysNews(String data, OnNewsShortListener listener) {
        mRequest.getRequest(new NewsShortRequest(NEWS_PRE+NEWS_BEFORE+data,listener));
    }

    @Override
    public void getNewsDetail(String url, OnNewsDetailListener listener) {
        mRequest.getRequest(new NewsHtmlRequest(url,listener));
    }
}
