package com.lizhiguang.administrator.toolbox.Util;

import android.content.Context;

/**
 * Created by ASUS User on 2017/3/22.
 */

public class GlobalContextUtil {
    private static Context mContext;

    public static void setContext(Context context) {
        mContext = context;
    }

    public static Context getContext() {
        return mContext;
    }
}
