package com.example.musiclib.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

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
    public static void showSeekBarDialog(Context context, final String beginText, int end, final String unit, final DialogCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("请选择");

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        final TextView tv = new TextView(context);
        tv.setLayoutParams(params);
        tv.setText(beginText);
        tv.setGravity(Gravity.CENTER);

        final SeekBar seekBar = new SeekBar(context);
        seekBar.setLayoutParams(params);
        seekBar.setMax(end);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0)
                    tv.setText(beginText);
                else
                    tv.setText(progress + " " + unit);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        linearLayout.addView(tv);
        linearLayout.addView(seekBar);

        builder.setView(linearLayout);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.onSelect(seekBar.getProgress());
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }
    public interface DialogCallback {
        void onSelect(Object o);
    }
}
