package com.lizhiguang.news.homepage.movie;


import com.lizhiguang.news.bean.NewsHotMovies;
import com.lizhiguang.news.resource.NewsMovieResource;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Created by lizhiguang on 2017/4/26.
 */

public class NewsMoviePresenter {
    private NewsMovieFragment mView;
    private NewsMovieResource mDataResource;
    public void attachView(NewsMovieFragment fragment) {
        mView = fragment;
    }
    public NewsMoviePresenter setSource(NewsMovieResource resource) {
        mDataResource = resource;
        return this;
    }
    public void start() {
        mDataResource.getHotMovies("深圳", new Observer<List<NewsHotMovies>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<NewsHotMovies> value) {
                mView.showHotMovies(value);
            }

            @Override
            public void onError(Throwable e) {
                mView.showToast("err:"+e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                mView.showToast("获取成功");
            }
        });
    }
}
