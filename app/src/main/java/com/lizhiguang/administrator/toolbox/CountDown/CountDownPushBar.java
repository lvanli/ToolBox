package com.lizhiguang.administrator.toolbox.CountDown;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;
import android.os.Handler;

import com.lizhiguang.administrator.toolbox.R;


/**
 * Created by lizhiguang on 16/1/8.
 */
public class CountDownPushBar extends ImageView {
    private Context mContext;
    private int sensitivity = 50;
    private static final String MYDEBUG = "myDebug";
    private static final int GAP = 5;
    private int number = 0, maxNumber = 0, textSize = 100, offset = 0, showNumber = 0;

    public CountDownPushBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CountDownPushBar(Context context) {
        super(context);
        init(context, null);
    }

    void init(Context context, AttributeSet attrs) {
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CountDownPushBar);
        maxNumber = typedArray.getInt(R.styleable.CountDownPushBar_maxNumber, 0);
        number = typedArray.getInt(R.styleable.CountDownPushBar_number, 0);
        showNumber = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    private Paint p = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        if (width <= 0) {
            Toast.makeText(mContext, "could not find measure width", Toast.LENGTH_SHORT).show();
            return;
        }

        int x, y;
        int preNumber = 0, nextNumber = 0;
        Rect factRect = new Rect();
        Rect middleRect = new Rect();

        if (showNumber > 0)
            preNumber = showNumber - 1;
        else
            preNumber = maxNumber - 1;
        if (showNumber < maxNumber - 1)
            nextNumber = showNumber + 1;
        else
            nextNumber = 0;

        p.setTextSize(textSize);
        p.getTextBounds(Integer.toString(showNumber), 0, Integer.toString(showNumber).length(), middleRect);

        if (offset <= 0) {
            p.setTextSize(textSize / 2 - textSize * offset / sensitivity / 2);
            p.getTextBounds(Integer.toString(preNumber), 0, Integer.toString(preNumber).length(), factRect);
            x = (width - factRect.width()) / 2;
            y = (height - middleRect.height()) / 2 - GAP - (middleRect.height() + GAP) * offset / sensitivity;
            canvas.drawText(Integer.toString(preNumber), x, y, p);
        }

        p.setTextSize(textSize - Math.abs(textSize * offset / sensitivity / 2));
        p.getTextBounds(Integer.toString(showNumber), 0, Integer.toString(showNumber).length(), factRect);
        x = (width - factRect.width()) / 2;
        y = (height + middleRect.height()) / 2 - (middleRect.height() + GAP) * offset / sensitivity;
        canvas.drawText(Integer.toString(showNumber), x, y, p);

        if (offset >= 0) {
            p.setTextSize(textSize / 2 + textSize * offset / sensitivity / 2);
            p.getTextBounds(Integer.toString(nextNumber), 0, Integer.toString(nextNumber).length(), factRect);
            x = (width - factRect.width()) / 2;
            y = (height + 3 * middleRect.height()) / 2 + GAP - (middleRect.height() + GAP) * offset / sensitivity;
            canvas.drawText(Integer.toString(nextNumber), x, y, p);
        }
    }

    private void moveDisplay(int action, float length) {
        if (length == 0)
            return;
        switch (action) {
            case ACTION_END:
                offset = (int) length % sensitivity;
                if (Math.abs(offset) >= sensitivity / 2) {
                    if (offset < 0)
                        length = length - sensitivity;
                    else if (offset > 0)
                        length = length + sensitivity;
                }
                showNumber = (number + (int) length / sensitivity + maxNumber) % maxNumber;
                number = showNumber;
                offset = 0;
                break;
            case ACTION_MOVE:
                offset = (int) length % sensitivity;
                showNumber = (number + (int) length / sensitivity + maxNumber * 5) % maxNumber;
                break;
        }
        invalidate();
    }

    private static final int ACTION_NOTING = -1;
    private static final int ACTION_END = 0;
    private static final int ACTION_MOVE = 1;
    private static final String DATA_TAG = "move";
    private float touchY, tempY;
    Message sendMessage = new Message();
    Bundle sendBundle = new Bundle();
    Handler pushHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != ACTION_NOTING)
                moveDisplay(msg.what, msg.getData().getFloat(DATA_TAG, 0));
            //       Log.d(MYDEBUG,"action="+msg.what+",data="+msg.getData().getFloat(DATA_TAG));
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        sendMessage.what = ACTION_NOTING;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchY = event.getY();
                tempY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(event.getY() - touchY) > sensitivity) {
                    if (Math.abs(event.getY() - tempY) > 5) {
                        tempY = event.getY();
                        sendMessage.what = ACTION_MOVE;
                        sendBundle.putFloat(DATA_TAG, touchY - event.getY());
                        sendMessage.setData(sendBundle);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(event.getY() - touchY) > sensitivity) {
                    sendMessage.what = ACTION_END;
                    sendBundle.putFloat(DATA_TAG, touchY - event.getY());
                    sendMessage.setData(sendBundle);
                } else {
                    break;
                }
                break;
        }
        pushHandle.handleMessage(sendMessage);
        return true;
    }
}
