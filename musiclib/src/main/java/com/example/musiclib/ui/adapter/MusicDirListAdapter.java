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
 * Created by lizhiguang on 2017/7/12.
 */

public class MusicDirListAdapter extends BaseAdapter {
    private List<List<AbstractMusic>> mInfos;
    private Context mContext;

    public MusicDirListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mInfos == null ? 0 : mInfos.size();
    }

    @Override
    public List<AbstractMusic> getItem(int position) {
        return mInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MusicDirListAdapter.Holder holder;
        if (convertView == null) {
            holder = new MusicDirListAdapter.Holder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.music_dir_list_item, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.music_dir_list_item_name);
            holder.count = (TextView) convertView.findViewById(R.id.music_dir_list_item_count);
            convertView.setTag(holder);
        } else {
            holder = (MusicDirListAdapter.Holder) convertView.getTag();
        }

        holder.name.setText(mInfos.get(position).get(0).dir);
        holder.count.setText(String.valueOf(mInfos.get(position).size()));

        return convertView;
    }

    public List<List<AbstractMusic>> getData() {
        return mInfos;
    }

    public void setData(List<List<AbstractMusic>> infos) {
        mInfos = infos;
    }

    private class Holder {
        TextView name;
        TextView count;
    }
}
