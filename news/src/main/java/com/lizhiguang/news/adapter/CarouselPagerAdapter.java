package com.lizhiguang.news.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.lizhiguang.news.R;
import com.lizhiguang.news.bean.NewsShortDetail;

import java.util.List;

/**
 * Created by lizhiguang on 2017/4/17.
 */

public class CarouselPagerAdapter extends PagerAdapter {
    public static final int CAROUSEL_MULTIPLE = 3;
    List<NewsShortDetail> details;
    private Context mContext;
    private ViewPager mViewPager;
    private View.OnClickListener mListener;

    public CarouselPagerAdapter(Context context, ViewPager pager, View.OnClickListener listener) {
        mContext = context;
        mViewPager = pager;
        mListener = listener;
    }

    public void setDetails(List<NewsShortDetail> details) {
        this.details = details;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageButton imageButton = new ImageButton(mContext);
        imageButton.setTag(R.id.tag_first, details.get(position % details.size()).getUrl());
        imageButton.setOnClickListener(mListener);
        container.addView(imageButton, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        Glide.with(mContext).load(details.get(position%details.size()).getImagePath()).bitmapTransform(new GrayscaleTransformation(mContext)).override(container.getMeasuredWidth(),container.getMeasuredHeight()).centerCrop().into(imageButton);
        Glide.with(mContext).load(details.get(position % details.size()).getImagePath()).into(imageButton);
        return imageButton;
    }

    @Override
    public int getCount() {
        if (details == null)
            return 0;
        else
            return details.size() * CAROUSEL_MULTIPLE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        int position = mViewPager.getCurrentItem();
        // ViewPager的更新即将完成，替换position，以达到无限循环的效果
        if (position == getCount() / CAROUSEL_MULTIPLE - 1) {
            position = getCount() / CAROUSEL_MULTIPLE * (CAROUSEL_MULTIPLE - 1) - 1;
            mViewPager.setCurrentItem(position, false);
        } else if (position == getCount() / CAROUSEL_MULTIPLE * (CAROUSEL_MULTIPLE - 1)) {
            position = getCount() / CAROUSEL_MULTIPLE;
            mViewPager.setCurrentItem(position, false);
        }
        super.finishUpdate(container);
    }
}
