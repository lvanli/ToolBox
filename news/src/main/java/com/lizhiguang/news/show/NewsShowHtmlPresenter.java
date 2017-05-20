package com.lizhiguang.news.show;

import com.lizhiguang.news.resource.NewsResource;

/**
 * Created by lizhiguang on 2017/4/21.
 */

public class NewsShowHtmlPresenter implements NewsResource.OnNewsDetailListener {
    NewsShowHtmlActivity mView;
    NewsResource mDataSource;

    NewsShowHtmlPresenter(NewsShowHtmlActivity view, NewsResource resource) {
        mView = view;
        mDataSource = resource;
    }

    public void getDetail(String url) {
        mDataSource.getNewsDetail(url, this);
    }

    @Override
    public void Result(String result) {
        if (result == null)
            mView.showDetail("网络错误");
        else
            mView.showDetail(result);
    }
}
