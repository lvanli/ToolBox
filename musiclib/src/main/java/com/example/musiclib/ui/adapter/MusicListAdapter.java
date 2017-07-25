package com.example.musiclib.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.musiclib.R;
import com.example.musiclib.bean.AbstractMusic;

import java.util.List;

/**
 * Created by lizhiguang on 2017/7/11.
 */

public class MusicListAdapter extends BaseAdapter {
    private List<AbstractMusic> mInfos;
    private Context mContext;

    public MusicListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mInfos == null ? 0 : mInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.music_list_item, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.music_list_item_name);
            holder.duration = (TextView) convertView.findViewById(R.id.music_list_item_duration);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.name.setText(mInfos.get(position).name);
        long duration = Long.parseLong(mInfos.get(position).duration);
        String d;
        if (duration % 60000 / 1000 < 10)
            d = (duration / 60000) + ":0" + (duration % 60000 / 1000);
        else
            d = (duration / 60000) + ":" + (duration % 60000 / 1000);
        holder.duration.setText(d);
        return convertView;
    }

    public List<AbstractMusic> getData() {
        return mInfos;
    }

    public void setData(List<AbstractMusic> infos) {
        mInfos = infos;
    }

    private class Holder {
        TextView name;
        TextView duration;
    }
}
