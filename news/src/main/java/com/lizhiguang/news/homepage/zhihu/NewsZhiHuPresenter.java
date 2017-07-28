package com.lizhiguang.news.homepage.zhihu;

import com.lizhiguang.news.bean.NewsShortDetail;
import com.lizhiguang.news.resource.NewsResource;
import com.lizhiguang.news.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by lizhiguang on 2017/4/5.
 */

public class NewsZhiHuPresenter implements NewsResource.OnNewsShortListener {
    private NewsResource mDataResource;
    private NewsZhiHuFragment mView;
    private Date mDate;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

    public NewsZhiHuPresenter setResource(NewsResource resource) {
        mDataResource = resource;
        return this;
    }

    public void setView(NewsZhiHuFragment firstFragment) {
        mView = firstFragment;
    }

    public void start() {
        init();
    }

    private void init() {
//        getMainPictures();
        if (mDate == null) {
            mDate = new Date(System.currentTimeMillis());//获取当前时间
            getNews();
        }
    }

    public void getMainPictures() {
//        mDataResource.getMainPicturesPath();
//        mView.showMainPictures();
    }

    public void getNews() {
        mDataResource.getMainNews(getTodayTag(), this);
//        mView.showMainNews(mDataResource.getMainNews());
    }

    public void loadNews() {
        mView.showToast("上拉加载");
        mDataResource.getDaysNews(getBeforeDate(), new NewsResource.OnNewsShortListener() {
            @Override
            public void Result(List<NewsShortDetail> result) {
                if (result != null)
                    mView.addMainNews(result);
                else
                    mView.showToast("刷新失败");
            }
        });
    }

    public void refreshNews() {
        mView.showToast("下拉刷新");
        mDataResource.recycleCache(getTodayTag());
        mDataResource.getMainNews(getTodayTag(), this);
    }

    public void cacheWeekNews() {
        Date date = new Date(System.currentTimeMillis());
        mDataResource.cacheTodayNews(getTodayTag());
        for (int i = 1; i <= 7; i++) {
            mDataResource.cacheNews(formatter.format(addDate(date, 0 - i)));
        }
    }

    private String getTodayTag() {
        return formatter.format(new Date(System.currentTimeMillis()));
    }

    private String getBeforeDate() {
        mDate = addDate(mDate, -1);
        return formatter.format(mDate);
    }

    private Date addDate(Date date, int move) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, move);
        return calendar.getTime();
    }

    public void recycleAllCache() {
        mDataResource.recycleAllCache();
        mView.showToast("清除成功");
    }

    @Override
    public void Result(List<NewsShortDetail> result) {
        if (result != null) {
            List<NewsShortDetail> tops = new ArrayList<>();
            for (int i = result.size() - 1; i >= 0; i--) {
                if (result.get(i).isTop()) {
                    tops.add(result.get(i));
                    result.remove(i);
                }
            }
            LogUtil.d("result=" + result.size() + ",tops=" + tops.size());
            mView.showMainNews(result);
            mView.showMainPictures(tops);
        } else
            mView.showToast("获取失败");
    }
}
