package com.example.musiclib.util.runnable;

/**
 * Created by lizhiguang on 2017/5/3.
 */

public abstract class Runnable3<T1, T2, T3> implements Runnable {
    protected T1 mP1;
    protected T2 mP2;
    protected T3 mP3;

    public Runnable3(T1 t1, T2 t2, T3 t3) {
        mP1 = t1;
        mP2 = t2;
        mP3 = t3;
    }
}
