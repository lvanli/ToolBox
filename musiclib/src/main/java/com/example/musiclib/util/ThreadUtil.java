package com.example.musiclib.util;

/**
 * Created by lizhiguang on 2017/5/3.
 */

public class ThreadUtil {
    public static void runOnBackground(Runnable runnable) {
        new Thread(runnable).start();
    }
}
