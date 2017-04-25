package com.lizhiguang.news.util.volley;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Response;
import com.lizhiguang.news.bean.NewsShortDetail;
import com.lizhiguang.news.resource.NewsResource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhiguang on 2017/4/17.
 */

public class NewsShortRequest extends BaseRequest<List<NewsShortDetail>> {
    private static final String NEWS_PRE = "http://news-at.zhihu.com/api/4/news/";

    public NewsShortRequest(String url, Response.Listener<List<NewsShortDetail>> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    public NewsShortRequest(String url, NewsResource.OnNewsRequestListener listener) {
        super(url, listener);
    }

    @Override
    protected List<NewsShortDetail> parseStringToDetail(String jsonString) {
        List<NewsShortDetail> details = new ArrayList<>();
        JSONObject json = JSONObject.parseObject(jsonString);
        JSONObject object;
        if (json != null && json.size() != 0) {
            if (json.containsKey("stories")) {
                JSONArray array = json.getJSONArray("stories");
                for (int i = 0; i < array.size(); i++) {
                    object = array.getJSONObject(i);
                    if (object.containsKey("images") && object.containsKey("title") && object.containsKey("id")) {
                        NewsShortDetail detail = new NewsShortDetail();
                        detail.setPath(object.getJSONArray("images").getString(0));
                        detail.setTitle(object.getString("title"));
                        detail.setUrl(NEWS_PRE + object.getIntValue("id"));
                        detail.setTop(false);
//                        LogUtil.d("path="+object.getJSONArray("images").getString(0));
                        details.add(detail);
                    }
                }
            }
            if (json.containsKey("top_stories")) {
                JSONArray array = json.getJSONArray("top_stories");
                for (int i = 0; i < array.size(); i++) {
                    object = array.getJSONObject(i);
                    if (object.containsKey("image") && object.containsKey("title") && object.containsKey("id")) {
                        NewsShortDetail detail = new NewsShortDetail();
                        detail.setPath(object.getString("image"));
                        detail.setTitle(object.getString("title"));
                        detail.setUrl(NEWS_PRE + object.getIntValue("id"));
                        detail.setTop(true);
//                        LogUtil.d("path="+object.getString("image"));
                        details.add(detail);
                    }
                }
            }
        }
        return details;
    }
}
