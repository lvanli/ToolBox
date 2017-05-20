package com.lizhiguang.news.resource;

import com.lizhiguang.news.bean.NewsHotMovies;
import com.lizhiguang.news.util.retrofit.NetHelper;

import java.util.List;

import io.reactivex.Observer;


/**
 * Created by lizhiguang on 2017/4/26.
 */

public class NewsMovieResource {
    public void getHotMovies(String area, Observer<List<NewsHotMovies>> subscriber) {
        //TODO base area find id
        if (area.equals("深圳")) {
            NetHelper.getHotMovie(366,subscriber);
        }
    }
}
