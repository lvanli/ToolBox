package com.lizhiguang.news.util.retrofit;

import com.lizhiguang.news.bean.NewsMovieDetail;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lizhiguang on 2017/4/26.
 */

public interface MovieDetail {
    @GET("http://ticket-api-m.mtime.cn/movie/detail.api")
    Observable<List<NewsMovieDetail>> getMovieDetail(@Query("locationId") int areaId, @Query("movieId") int movieId);
}
