package com.lizhiguang.administrator.toolbox.Basic;

/**
 * Created by SDT03809 on 15-12-2.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lizhiguang.administrator.toolbox.R;

public class BasicHorizontalListViewAdapter extends BaseAdapter {
    List<ViewHolder> vhL;

    public BasicHorizontalListViewAdapter(Context con) {
        mInflater = LayoutInflater.from(con);
        vhL = new ArrayList<ViewHolder>();
    }

    @Override
    public int getCount() {
        return vhL.size();
    }

    private LayoutInflater mInflater;

    @Override
    public Object getItem(int position) {
        return vhL.get(position);
    }

    public boolean addItem(int imgID, String title) {
        ViewHolder newVH = new ViewHolder();
        newVH.mImageID = imgID;
        newVH.mTitle = title;
        return vhL.add(newVH);
    }

    private static class ViewHolder {
        private TextView title;
        private ImageView im;
        private int mImageID;
        private String mTitle;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh, mvh;
        mvh = vhL.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.horizontallistview_item, null);
            vh = mvh;
            vh.im = (ImageView) convertView.findViewById(R.id.iv_pic);
            vh.title = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.im.setImageResource(mvh.mImageID);
        vh.title.setText(mvh.mTitle);
        return convertView;
    }
}