package com.lizhiguang.news.homepage;

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

import com.lizhiguang.news.R;
import com.lizhiguang.news.resource.NewsResourceFromZhiHu;
import com.lizhiguang.news.service.DownloadIntentService;

public class NewsActivity extends AppCompatActivity {
    private TabHost mTabHost;
    private NewsHomePagerPresenter mPresenter;
    private LocalReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        init();
    }
    private void init() {
        mTabHost = (TabHost) findViewById(R.id.news_tab);
        mTabHost.setup();
        LayoutInflater inflater = LayoutInflater.from(this);
        inflater.inflate(R.layout.news_first_content,mTabHost.getTabContentView());
        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("知乎").setContent(R.id.news_first_layout));
        mPresenter = new NewsHomePagerPresenter().setResource(new NewsResourceFromZhiHu(this));
        getFragmentManager().beginTransaction().add(R.id.news_first_layout,new NewsFirstFragment(this).setPresenter(mPresenter)).commit();

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
        getMenuInflater().inflate(R.menu.menu_news,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.news_menu_cache_week) {
            mPresenter.cacheWeekNews();
        } else if (id == R.id.news_menu_cache_recycle) {
            mPresenter.recycleAllCache();
        }
        return super.onOptionsItemSelected(item);
    }

    class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(NewsActivity.this,"下载完成",Toast.LENGTH_SHORT).show();
        }
    }
}
