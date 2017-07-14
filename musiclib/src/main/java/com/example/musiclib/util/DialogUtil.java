package com.example.musiclib.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by lizhiguang on 2017/7/13.
 */

public class DialogUtil {
    public static void showSelectDialog(Context context, String[] items, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("请选择");
        builder.setItems(items, listener);
        builder.setNegativeButton("取消", null);
        builder.show();
    }
}
