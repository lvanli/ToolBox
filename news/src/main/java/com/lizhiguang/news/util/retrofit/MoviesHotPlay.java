package com.lizhiguang.news.util.retrofit;

import com.lizhiguang.news.bean.NewsHotMovies;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lizhiguang on 2017/4/26.
 */

public interface MoviesHotPlay {
    @GET("http://api-m.mtime.cn/PageSubArea/HotPlayMovies.api")
    Observable<List<NewsHotMovies>> getHotMovies(@Query("locationId") int areaId);
}
