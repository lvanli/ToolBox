package com.lizhiguang.news.util.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.lizhiguang.news.resource.NewsResource;

import java.io.UnsupportedEncodingException;

/**
 * Created by lizhiguang on 2017/4/17.
 */

public abstract class BaseRequest<T> extends Request<T> {
    protected static final String PROTOCOL_CHARSET = "utf-8";
    protected Response.Listener<T> mSuccessListener;

    public BaseRequest(String url, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        mSuccessListener = listener;
    }

    public BaseRequest(String url, NewsResource.OnNewsRequestListener listener) {
        super(Method.GET, url, new EListener(listener));
        mSuccessListener = new SListener(listener);
    }


    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            return Response.success(parseStringToDetail(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        if (mSuccessListener != null) {
            mSuccessListener.onResponse(response);
        }
    }

    @Override
    protected void onFinish() {
        super.onFinish();
        mSuccessListener = null;
    }

    protected abstract T parseStringToDetail(String jsonString);


    private class SListener implements Response.Listener<T> {
        private NewsResource.OnNewsRequestListener listener;

        SListener(NewsResource.OnNewsRequestListener listener) {
            this.listener = listener;
        }

        @Override
        public void onResponse(T response) {
            listener.Result(response);
        }
    }

    private static class EListener implements Response.ErrorListener {
        private NewsResource.OnNewsRequestListener listener;

        EListener(NewsResource.OnNewsRequestListener listener) {
            this.listener = listener;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            listener.Result(null);
        }
    }
}
