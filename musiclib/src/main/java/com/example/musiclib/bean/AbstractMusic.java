package com.example.musiclib.bean;

import android.os.Parcelable;

/**
 * Created by lizhiguang on 2017/7/11.
 */

public abstract class AbstractMusic implements Parcelable, Parcelable.Creator<AbstractMusic> {
    public static Creator<AbstractMusic> CREATOR;
    public String album;
    public String artist;
    public String path;
    public String name;
    public String duration;
    public String title;
    public String dir;

    public AbstractMusic() {
        //给CREATOR赋值
        CREATOR = this;
    }

    @Override
    public String toString() {
        return "AbstractMusic{" +
                "album='" + album + '\'' +
                ", artist='" + artist + '\'' +
                ", path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", duration='" + duration + '\'' +
                ", title='" + title + '\'' +
                ", dir='" + dir + '\'' +
                '}';
    }
}
