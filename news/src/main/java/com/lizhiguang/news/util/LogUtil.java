package com.lizhiguang.news.util;

import android.util.Log;

/**
 * Created by lizhiguang on 2017/4/14.
 */

public class LogUtil {
    public static void v(String msg) {
        log(msg, Log.VERBOSE);
    }
    public static void d(String msg) {
        log(msg, Log.DEBUG);
    }
    public static void i(String msg) {
        log(msg, Log.INFO);
    }
    public static void w(String msg) {
        log(msg, Log.WARN);
    }
    public static void e(String msg) {
        log(msg, Log.ERROR);
    }
    private static void log(String msg, int priority) {
        StackTraceElement[] stackTraceElement = Thread.currentThread()
                .getStackTrace();
        int currentIndex = 4;
        String fullClassName = stackTraceElement[currentIndex].getClassName();
        String className = fullClassName.substring(fullClassName
                .lastIndexOf(".") + 1);
        String methodName = stackTraceElement[currentIndex].getMethodName();
        String lineNumber = String
                .valueOf(stackTraceElement[currentIndex].getLineNumber());
        String output = "[("+ className + ".java:" + lineNumber +"):"+methodName+"]"+msg;
        switch (priority) {
            case Log.VERBOSE:
                Log.v(className,output);
                break;
            case Log.DEBUG:
                Log.d(className,output);
                break;
            case Log.INFO:
                Log.i(className,output);
                break;
            case Log.WARN:
                Log.w(className,output);
                break;
            case Log.ERROR:
                Log.e(className,output);
                break;
        }
    }
}
