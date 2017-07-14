package com.example.musiclib.bean;

import android.os.Parcel;

/**
 * Created by lizhiguang on 2017/7/11.
 */

public class LocalMusicInfo extends AbstractMusic {
    public static final Creator<LocalMusicInfo> CREATOR = new Creator<LocalMusicInfo>() {
        public LocalMusicInfo createFromParcel(Parcel source) {
            return new LocalMusicInfo(source);
        }

        public LocalMusicInfo[] newArray(int size) {
            return new LocalMusicInfo[size];
        }
    };

    public LocalMusicInfo() {
    }

    public LocalMusicInfo(Parcel in) {
        this.album = in.readString();
        this.artist = in.readString();
        this.duration = in.readString();
        this.name = in.readString();
        this.path = in.readString();
        this.title = in.readString();
        this.dir = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.album);
        dest.writeString(this.artist);
        dest.writeString(this.duration);
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeString(this.title);
        dest.writeString(this.dir);
    }

    @Override
    public AbstractMusic createFromParcel(Parcel source) {
        return new LocalMusicInfo(source);
    }

    @Override
    public AbstractMusic[] newArray(int size) {
        return new AbstractMusic[size];
    }
}
