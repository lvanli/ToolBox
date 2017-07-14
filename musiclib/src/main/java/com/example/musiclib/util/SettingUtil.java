package com.example.musiclib.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lizhiguang on 2017/7/13.
 */

public class SettingUtil {
    public static final String SETTING_SCAN_PATH = "scanPath";
    public static final String SETTING_PLAY_MODE = "playMode";
    private static final String PREFERENCE_NAME = "musicSetting";

    public static String getString(Context context, String tag) {
        SharedPreferences data = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return data.getString(tag, null);
    }

    public static void setString(Context context, String tag, String data) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(tag, data);
        editor.apply();
    }

    public static int getInt(Context context, String tag) {
        SharedPreferences data = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return data.getInt(tag, 0);
    }

    public static void setInt(Context context, String tag, int data) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(tag, data);
        editor.apply();
    }
}
