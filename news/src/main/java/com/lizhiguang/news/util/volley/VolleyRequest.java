package com.lizhiguang.news.util.volley;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by lizhiguang on 2017/4/17.
 */

public class VolleyRequest {
    RequestQueue mQueue;

    public VolleyRequest(Context context) {
        mQueue = Volley.newRequestQueue(context);
    }

    public void getRequest(BaseRequest request) {
        mQueue.add(request);
    }
}
