package com.lizhiguang.administrator.toolbox.FiveInARow;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.lizhiguang.administrator.toolbox.Basic.BasicActivity;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lizhiguang on 16/6/29.
 */
public class FIRMapView extends View {
    Paint p;
    int lineHeight;

    public int getMaxLine() {
        return maxLine;
    }

    public void setMaxLine(int maxLine) {
        this.maxLine = maxLine;
    }

    int maxLine;
    FIRManager mManager;

    public int getWho() {
        return mWho;
    }

    public void setWho(int mWho) {
        this.mWho = mWho;
    }

    int mWho;
    float imageSizeFloat = 0.75f;

    public Handler getActivityHandler() {
        return mActivityHandler;
    }

    public void setActivityHandler(Handler mActivityHandler) {
        this.mActivityHandler = mActivityHandler;
    }

    Handler mActivityHandler;

    public FIRMapView(Context context) {
        super(context);
        init(context);
    }

    public FIRMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FIRMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public FIRManager getManager() {
        return mManager;
    }

    public void setManager(FIRManager mManager) {
        mManager.setMaxLine(maxLine);
        mManager.setMapView(this);
        mManager.initMap(maxLine);
        mManager.setWinListener(new FIRManager.OnWinListener() {
            @Override
            public void onWin(int who) {
                AlertDialog dialog = new AlertDialog.Builder(mContext).create();
				dialog.setCancelable(false);
                if (who == FIRManager.BLACK)
                    dialog.setMessage("黑子赢");
                else
                    dialog.setMessage("白子赢");
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mActivityHandler.sendEmptyMessage(0);
                    }
                });
                dialog.show();
            }
        });
        this.mManager = mManager;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FIRMapView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMeasure = MeasureSpec.getSize(widthMeasureSpec);
        int heightMeasure = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int length = widthMeasure > heightMeasure ? heightMeasure : widthMeasure;
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            length = heightMode;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            length = widthMeasure;
        }

        setMeasuredDimension(length, length);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int width = w > h ? h : w;
        setMeasuredDimension(width, width);
    }

    private Context mContext;

    public void init(Context context) {
        mContext = context;
        p = new Paint();
        p.setColor(0xE0222222);
        p.setAntiAlias(true);//抗锯齿
        p.setDither(true);//抖动
        p.setStyle(Paint.Style.FILL);//中空
        maxLine = 15;
        mWho = FIRManager.BLACK;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawMap(canvas);
        drawChess(canvas);
        mManager.afterDraw();
    }

    private void drawChess(Canvas canvas) {
        short map[][] = mManager.getMap();
        int floatSize = (int) (lineHeight * imageSizeFloat / 2);
        for (int i = 0; i < maxLine; i++) {
            for (int j = 0; j < maxLine; j++) {
                if (map[i][j] == mManager.BLACK) {
                    p.setColor(Color.BLACK);
                    canvas.drawCircle((i + 0.5f) * lineHeight, (j + 0.5f) * lineHeight, lineHeight / 2 * imageSizeFloat, p);
                } else if (map[i][j] == mManager.WRITE) {
                    p.setColor(Color.WHITE);
                    canvas.drawCircle((i + 0.5f) * lineHeight, (j + 0.5f) * lineHeight, lineHeight / 2 * imageSizeFloat, p);
                }
            }
        }
    }

    private void drawMap(Canvas canvas) {
        lineHeight = getWidth() / maxLine;
        int begin, end, base;
        p.setColor(Color.BLACK);
        for (int i = 0; i < maxLine; i++) {
            base = (int) ((i + 0.5) * lineHeight);
            begin = (int) (lineHeight * 0.5);
            end = (int) (getWidth() - lineHeight * 0.5);
            canvas.drawLine(base, begin, base, end, p);
            canvas.drawLine(begin, base, end, base, p);
        }
    }

//    @Override
//    protected Parcelable onSaveInstanceState() {
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("state",super.onSaveInstanceState());
//        bundle.putInt("maxLine",maxLine);
//        bundle.putInt("mWho",mWho);
//        bundle.putShortArray("map",mManager.getsMap());
//        bundle.putBoolean("isFirst",mManager.isFirst());
//        bundle.putInt("gameMode",mManager.getGameMode());
//        bundle.putSerializable("manager", mManager);
//        return bundle;
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Parcelable state) {
//        if (state != null) {
//            Bundle bundle = (Bundle) state;
//            super.onRestoreInstanceState(bundle.getParcelable("state"));
//            maxLine = bundle.getInt("maxLine");
//            mWho = bundle.getInt("mWho");
//            mManager = (FIRManager) bundle.getSerializable("manager");
//            return ;
//            mManager.setMaxLine(maxLine);
//            mManager.setIsFirst(bundle.getBoolean("isFirst"));
//            mManager.setGameMode(bundle.getInt("gameMode"));
//            mManager.setMap(bundle.getShortArray("map"));
//        }
//        super.onRestoreInstanceState(state);
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) (event.getX() / lineHeight);
            int y = (int) (event.getY() / lineHeight);
            if (x < maxLine && y < maxLine && mManager.makePoint(x, y, (short) (mWho)) && mManager.getGameMode() == 1)
                mWho = mManager.WRITE + mManager.BLACK - mWho;
            Log.d(BasicActivity.MYDEBUG, "x=" + event.getX() + ",Y=" + event.getY() + ",rx=" + x + ",ry=" + y + ",who=" + mWho);
        }
        return true;
    }
}
