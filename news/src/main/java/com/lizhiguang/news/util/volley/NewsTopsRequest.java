package com.lizhiguang.news.util.volley;

import com.android.volley.Response;
import com.lizhiguang.news.bean.NewsTopPictures;
import com.lizhiguang.news.resource.NewsResource;

import java.util.List;

/**
 * Created by lizhiguang on 2017/4/17.
 */

public class NewsTopsRequest extends BaseRequest<List<NewsTopPictures>> {
    public NewsTopsRequest(String url, Response.Listener<List<NewsTopPictures>> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }
    public NewsTopsRequest(String url,NewsResource.OnNewsRequestListener listener) {
        super(url,listener);
    }

    @Override
    protected List<NewsTopPictures> parseStringToDetail(String jsonString) {
        return null;
    }

}
