package com.example.musiclib.util.runnable;

/**
 * Created by lizhiguang on 2017/5/3.
 */

public abstract class Runnable2<T1, T2> implements Runnable {
    protected T1 mP1;
    protected T2 mP2;

    public Runnable2(T1 t1, T2 t2) {
        mP1 = t1;
        mP2 = t2;
    }
}
