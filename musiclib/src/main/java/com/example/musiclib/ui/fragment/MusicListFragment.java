package com.example.musiclib.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.musiclib.BaseFragment;
import com.example.musiclib.R;
import com.example.musiclib.bean.LocalMusicInfo;
import com.example.musiclib.proxy.LocalMusicManager;
import com.example.musiclib.ui.adapter.MusicListAdapter;
import com.example.musiclib.util.SettingUtil;
import com.example.musiclib.util.runnable.Runnable1;

import java.util.List;

/**
 * Created by lizhiguang on 2017/7/11.
 */

public class MusicListFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private ListView mMusicList;
    private MusicListAdapter mListAdapter;

    public MusicListFragment() {
        super();
    }

    public MusicListFragment(Context context) {
        super(context);
    }

    @Override
    public String getTitle() {
        return "歌曲";
    }

    @Override
    public void updateData(Object o) {
        if (mMusicList != null)
            mMusicList.post(new Runnable1<Object>(o) {
                @Override
                public void run() {
                    if (mP1 != null) {
                        List<LocalMusicInfo> infos = (List<LocalMusicInfo>) mP1;
                        showToast("共" + infos.size() + "首");
                        mListAdapter.setData(infos);
                        mListAdapter.notifyDataSetChanged();
                    } else {
                        showToast("搜索异常");
                    }
                }
            });
    }

    @Override
    public void reload() {
        mPresenter.reload();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);
        init(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    private void init(View view) {
        mMusicList = (ListView) view.findViewById(R.id.music_list_view);
        mListAdapter = new MusicListAdapter(mContext);
        mMusicList.setAdapter(mListAdapter);
        mMusicList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LocalMusicManager.getInstance().setMode(SettingUtil.getInt(mContext, SettingUtil.SETTING_PLAY_MODE));
        mPresenter.playMusicList(mListAdapter.getData(), position);
    }
}
