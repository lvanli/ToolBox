package com.lizhiguang.news.util.retrofit;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.lizhiguang.news.bean.NewsHotMovies;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * Created by lizhiguang on 2017/4/26.
 */

public class NetHelper {
    public static void getHotMovie(int id, Observer<List<NewsHotMovies>> subscriber) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api-m.mtime.cn/")
                .addConverterFactory(MovieHotConverter.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        MoviesHotPlay hotPlay = retrofit.create(MoviesHotPlay.class);
        hotPlay.getHotMovies(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
