package com.lizhiguang.news.show;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lizhiguang.news.R;
import com.lizhiguang.news.resource.NewsResourceFromZhiHu;
import com.lizhiguang.news.util.LogUtil;
import com.lizhiguang.news.util.cache.FileCacheUtil;

public class NewsShowDetailActivity extends AppCompatActivity {
    WebView webView;
    String url;
    NewsShowDetailPresenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_show_detail);
        LogUtil.d("intent="+getIntent());
        init(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtil.d("intent="+intent);
        init(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (url != null)
            mPresenter.getDetail(url);
    }

    private void init(Intent intent) {
        if (intent == null)
            return;
        url = intent.getStringExtra("url");
        LogUtil.d("url="+url);
        webView = (WebView) findViewById(R.id.news_show_detail_web);
//        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setScrollbarFadingEnabled(true);
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        mPresenter = new NewsShowDetailPresenter(this,new NewsResourceFromZhiHu(this));
    }
    public void showDetail(String detail) {
        //"x-data://base"
        webView.loadDataWithBaseURL("file://" + FileCacheUtil.getDiskCachePath(this)+"/",detail,"text/html","utf-8",null);
    }
}
