package com.lizhiguang.administrator.toolbox.Basic;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lizhiguang.administrator.toolbox.R;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by SDT03809 on 15-12-11.
 */
public class BasicPermanentIcon extends ImageView {

    private boolean hideText;
    private String describe;
    private String imgPath;
    private int textSize;

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getDescribe() {
        return describe;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getTextSize() {
        return textSize;
    }

    private static final int PADDING_SIZE = 0;
    private static final String MYDEBUG = "myDebug";

    private Paint paint;

    public BasicPermanentIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public BasicPermanentIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BasicPermanentIcon(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BasicPermanentIcon);
            hideText = ta.getBoolean(R.styleable.BasicPermanentIcon_hideText, false);
            describe = ta.getString(R.styleable.BasicPermanentIcon_describe);
            imgPath = ta.getString(R.styleable.BasicPermanentIcon_imgPath);
            textSize = ta.getInteger(R.styleable.BasicPermanentIcon_textSize, 5);
            ta.recycle();
        } else {
            hideText = false;
            describe = null;
            imgPath = null;
            textSize = context.getResources().getInteger(R.integer.text_size);
            int h = (int) getResources().getDimension(R.dimen.default_margin_horizontal);
            int v = (int) getResources().getDimension(R.dimen.default_margin_vertical);
            setPadding(h, v, h, v);
        }
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setLayoutParams(lp);
        paint = new Paint();
        paint.setTextSize(textSize);
        paint.setFlags(Paint.FAKE_BOLD_TEXT_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bitmap = null;
        int textAreaHeight = 0;
        int textAreaWidth = 0;
        if (!hideText && describe != null) {
            Rect textRect = new Rect();
            paint.getTextBounds(describe, 0, describe.length(), textRect);
            textAreaHeight = textRect.height();
            textAreaWidth = textRect.width();
        }
        int bitmapWidth = getMeasuredWidth() - 2 * PADDING_SIZE - getPaddingLeft() - getPaddingRight();
        int bitmapHeight = getMeasuredHeight() - textAreaHeight - PADDING_SIZE - getPaddingTop();
        int rectBitmap = bitmapHeight > bitmapWidth ? bitmapWidth : bitmapHeight;
        try {
            if (imgPath != null) {
                InputStream is = getResources().getAssets().open(imgPath);
                Bitmap sourceBitmap = BitmapFactory.decodeStream(is);
                if (sourceBitmap == null) {
                    Log.e(MYDEBUG, "source is null");
                    return;
                }
                bitmap = Bitmap.createScaledBitmap(sourceBitmap, rectBitmap, rectBitmap, true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!hideText && describe != null) {
            paint.setTextSize(textSize);
            canvas.drawText(describe, (getMeasuredWidth() + getPaddingLeft() - getPaddingRight() - textAreaWidth) / 2, rectBitmap + textAreaHeight + getPaddingTop() + PADDING_SIZE, paint);//getMeasuredHeight() - textAreaHeight / 4
        }
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, getPaddingLeft() + PADDING_SIZE, getPaddingTop() + PADDING_SIZE, paint);
        } else if (describe != null){
            paint.setTextSize(rectBitmap * 0.8f);
            canvas.drawText(describe,0,1,getPaddingLeft()+PADDING_SIZE+(rectBitmap - paint.measureText(describe,0,1)) / 2
                    ,getPaddingTop()+PADDING_SIZE-paint.getFontMetrics().top,paint);
        }
    }
}
