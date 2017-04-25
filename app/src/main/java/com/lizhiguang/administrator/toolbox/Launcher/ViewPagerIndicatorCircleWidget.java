package com.lizhiguang.administrator.toolbox.Launcher;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by lizhiguang on 2017/4/7.
 */

public class ViewPagerIndicatorCircleWidget extends ImageView {
    private static final String TAG = "ViewPagerIndicatorCircl";
    private Paint paint;

    public ViewPagerIndicatorCircleWidget(Context context) {
        this(context, null);
    }

    public ViewPagerIndicatorCircleWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndicatorCircleWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
    }

    public void setFocus(boolean focus) {
        if (focus)
            paint.setStyle(Paint.Style.FILL);
        else
            paint.setStyle(Paint.Style.STROKE);
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw: w=" + getMeasuredWidth() + ",h=" + getMeasuredHeight());
        int radius = getMeasuredHeight() > getMeasuredWidth() ? getMeasuredWidth() : getMeasuredHeight();
        radius = radius / 2 - 2;
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, radius, paint);
    }
}
