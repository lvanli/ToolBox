package com.lizhiguang.news.util.retrofit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lizhiguang.news.bean.NewsHotMovies;
import com.lizhiguang.news.util.LogUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by lizhiguang on 2017/4/26.
 */

public class MovieHotConverter extends Converter.Factory {
    public static MovieHotConverter create() {
        return new MovieHotConverter();
    }

    @Override
    public Converter<ResponseBody, List<NewsHotMovies>> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
//        if (!(type instanceof List))
//            return null;
        return new MovieHotResponseConverter();
    }
    public static class MovieHotResponseConverter implements Converter<ResponseBody,List<NewsHotMovies>> {

        @Override
        public List<NewsHotMovies> convert(ResponseBody value) throws IOException {
            JSONObject root = JSON.parseObject(value.string());
            LogUtil.d("");
            List<NewsHotMovies> movies = null;
            NewsHotMovies movie;
            if (root.containsKey("count") && root.containsKey("movies")) {
                movies = new ArrayList<NewsHotMovies>(root.getIntValue("count"));
                JSONArray array = root.getJSONArray("movies");
                JSONObject object;
                for (int i=0;i<array.size();i++) {
                    object = array.getJSONObject(i);
                    if (object.containsKey("titleCn") && object.containsKey("movieId") && object.containsKey("img")) {
                        movie = new NewsHotMovies();
                        movie.setId(object.getIntValue("movieId"));
                        movie.setName(object.getString("titleCn"));
                        movie.setImgPath(object.getString("img"));
                        movies.add(movie);
                    }
                }
            }
            return movies;
        }
    }
}
