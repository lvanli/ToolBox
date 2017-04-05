package com.lizhiguang.administrator.toolbox.Launcher;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by lizhiguang on 16/7/29.
 */
public class AppInfo implements Serializable {
    private String name;
    private String action;
    private String imageUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String image) {
        this.imageUrl = image;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "name='" + name + '\'' +
                ", action='" + action + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

}
