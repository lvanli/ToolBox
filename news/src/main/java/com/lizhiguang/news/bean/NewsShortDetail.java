package com.lizhiguang.news.bean;

import java.io.Serializable;

/**
 * Created by lizhiguang on 2017/4/17.
 */

public class NewsShortDetail implements Serializable {
    private String title;
    private String path;
    private String url;
    private boolean isTop;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public boolean isTop() {
        return isTop;
    }
}
