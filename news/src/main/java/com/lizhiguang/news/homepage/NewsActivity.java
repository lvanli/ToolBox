package com.lizhiguang.news.homepage;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lizhiguang.news.R;
import com.lizhiguang.news.homepage.movie.NewsMovieFragment;
import com.lizhiguang.news.homepage.movie.NewsMoviePresenter;
import com.lizhiguang.news.homepage.zhihu.NewsZhiHuFragment;
import com.lizhiguang.news.homepage.zhihu.NewsZhiHuPresenter;
import com.lizhiguang.news.resource.NewsMovieResource;
import com.lizhiguang.news.resource.NewsResourceFromZhiHu;
import com.lizhiguang.news.service.DownloadIntentService;

public class NewsActivity extends AppCompatActivity implements TabHost.OnTabChangeListener {
    private static final String TAG_ZHIHU = "zhihu";
    private static final String TAG_MOVIE = "movie";
    private static final int MENU_ID_BACK = 0;
    private static final int MENU_ID_CACHE_WEEK = 1;
    private static final int MENU_ID_CACHE_RECYCLE = 2;
    private TabHost mTabHost;
    private NewsZhiHuPresenter mZhiHuPresenter;
    private LocalReceiver receiver;
    private String mCurrentTabTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        init();
    }

    private void init() {
        mCurrentTabTag = TAG_ZHIHU;

        mTabHost = (TabHost) findViewById(R.id.news_tab);
        mTabHost.setup();
        mTabHost.setOnTabChangedListener(this);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(R.layout.news_zhihu_content, mTabHost.getTabContentView());
        mTabHost.addTab(mTabHost.newTabSpec(TAG_ZHIHU).setIndicator("知乎").setContent(R.id.news_zhihu_layout));
        mZhiHuPresenter = new NewsZhiHuPresenter().setResource(new NewsResourceFromZhiHu(this));
        transaction.add(R.id.news_zhihu_layout, new NewsZhiHuFragment(this).setPresenter(mZhiHuPresenter));

        inflater.inflate(R.layout.news_movie_content, mTabHost.getTabContentView());
        mTabHost.addTab(mTabHost.newTabSpec(TAG_MOVIE).setIndicator("电影").setContent(R.id.news_movie_layout));
        NewsMoviePresenter moviePresenter = new NewsMoviePresenter().setSource(new NewsMovieResource());
        transaction.add(R.id.news_movie_layout, new NewsMovieFragment(this).setPresenter(moviePresenter));

        transaction.commit();

        IntentFilter filter = new IntentFilter(DownloadIntentService.ACTION_DOWNLOAD_OVER);
        receiver = new LocalReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == MENU_ID_CACHE_WEEK) {
            mZhiHuPresenter.cacheWeekNews();
        } else if (id == MENU_ID_CACHE_RECYCLE) {
            mZhiHuPresenter.recycleAllCache();
        } else if (id == MENU_ID_BACK) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if (mCurrentTabTag.equals(TAG_ZHIHU)) {
            menu.add(0, MENU_ID_CACHE_WEEK, 100, "缓存一周");
            menu.add(0, MENU_ID_CACHE_RECYCLE, 100, "清除缓存");
        } else {
            menu.add(0, MENU_ID_CACHE_RECYCLE, 100, "清除缓存");
        }
        menu.add(0, MENU_ID_BACK, 100, "返回");
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onTabChanged(String tabId) {
        mCurrentTabTag = tabId;
    }

    class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(NewsActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
        }
    }
}
