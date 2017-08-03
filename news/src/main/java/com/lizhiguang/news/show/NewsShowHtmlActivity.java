package com.lizhiguang.news.show;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lizhiguang.news.R;
import com.lizhiguang.news.resource.NewsResourceFromZhiHu;
import com.lizhiguang.news.util.cache.FileCacheUtil;
import com.lizhiguang.utils.log.LogUtil;

public class NewsShowHtmlActivity extends AppCompatActivity {
    WebView webView;
    String url;
    NewsShowHtmlPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_show_html);
        LogUtil.d("intent=" + getIntent());
        init(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtil.d("intent=" + intent);
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
        LogUtil.d("url=" + url);
        webView = (WebView) findViewById(R.id.news_show_detail_web);
//        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setScrollbarFadingEnabled(true);
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(Build.VERSION.SDK_INT >= 19) {
                    webView.loadUrl("javascript:(function(){"
                            + "var objs = document.getElementsByTagName('img'); "
                            + "for(var i=0;i<objs.length;i++) {"
                            + // //webview图片自适应，android4.4之前都有用，4.4之后google优化后，无法支持，需要自己手动缩放
                            "if (objs[i].className != \"avatar\")"
                            + " objs[i].style.width = '100%';objs[i].style.height = 'auto';"
                            + "}"
                            + "})()"
                );
                } else{
                    webView.loadUrl("javascript:var imgs = document.getElementsByTagName('img');for(var i = 0; i<imgs.length; i++)" +
                            "if (imgs[i].className != \"avatar\")" +
                            "{imgs[i].style.width = '100%';imgs[i].style.height= 'auto';}");
                }
            }
        });
        mPresenter = new NewsShowHtmlPresenter(this, new NewsResourceFromZhiHu(this));
    }

    public void showDetail(String detail) {
        //"x-data://base"
        webView.loadDataWithBaseURL("file://" + FileCacheUtil.getDiskCachePath(this) + "/", detail, "text/html", "utf-8", null);
    }
}
