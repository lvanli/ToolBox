package com.lizhiguang.administrator.toolbox.Launcher;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhiguang on 2017/4/7.
 */

public class ViewPagerIndicator implements ViewPager.OnPageChangeListener {
    private static final String TAG = "ViewPagerIndicator";
    private Context context;
    private ViewPager viewPager;
    private LinearLayout dotLayout;
    private int size;
    private List<ViewPagerIndicatorCircleWidget> dotViewLists = new ArrayList<>();

    public ViewPagerIndicator(Context context, ViewPager viewPager, LinearLayout dotLayout, int size) {
        this.context = context;
        this.viewPager = viewPager;
        this.dotLayout = dotLayout;
        this.size = size;

        for (int i = 0; i < size; i++) {
            ViewPagerIndicatorCircleWidget imageView = new ViewPagerIndicatorCircleWidget(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            //为小圆点左右添加间距
            params.leftMargin = 10;
            params.rightMargin = 10;
            params.width = 15;
            params.height = 15;
            if (i == 0) {
                imageView.setFocus(true);
            } else {
                imageView.setFocus(false);
            }
            //为LinearLayout添加ImageView
            dotLayout.addView(imageView, params);
            dotViewLists.add(imageView);
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, "onPageSelected: po=" + position + ",s=" + size);
        for (int i = 0; i < size; i++) {
            //选中的页面改变小圆点为选中状态，反之为未选中
            if ((position % size) == i) {
                dotViewLists.get(i).setFocus(true);
            } else {
                dotViewLists.get(i).setFocus(false);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
