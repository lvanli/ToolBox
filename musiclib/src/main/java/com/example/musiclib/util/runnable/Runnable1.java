package com.example.musiclib.util.runnable;

/**
 * Created by lizhiguang on 2017/5/3.
 */

public abstract class Runnable1<T> implements Runnable {
    protected T mP1;

    public Runnable1(T t) {
        mP1 = t;
    }
}
