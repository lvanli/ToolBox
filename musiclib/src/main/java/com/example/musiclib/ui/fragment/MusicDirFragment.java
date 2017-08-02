package com.example.musiclib.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.musiclib.BaseFragment;
import com.example.musiclib.R;
import com.example.musiclib.bean.AbstractMusic;
import com.example.musiclib.presenter.MusicListPresenter;
import com.example.musiclib.ui.adapter.MusicDirListAdapter;
import com.lizhiguang.utils.runnable.Runnable1;

import java.util.List;

/**
 * Created by lizhiguang on 2017/7/11.
 */

public class MusicDirFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private ListView mListView;
    private MusicDirListAdapter mDirAdapter;

    public MusicDirFragment(){
        super();
    }

    public MusicDirFragment(Context context) {
        super(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_dir_list, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        mListView = (ListView) view.findViewById(R.id.music_dir_list_view);
        mDirAdapter = new MusicDirListAdapter(mContext);
        mListView.setAdapter(mDirAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public String getTitle() {
        return "文件夹";
    }

    @Override
    public void updateData(Object o) {
        if (o != null && mListView != null) {
            mListView.post(new Runnable1<Object>(o) {
                @Override
                public void run() {
                    mDirAdapter.setData((List<List<AbstractMusic>>) mP1);
                    mDirAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void reload() {
        mPresenter.reload();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (getActivity() != null) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack("MusicDetailFragment");
            transaction.replace(R.id.music_main_fragment,new MusicDetailFragment(mContext).setData(mDirAdapter.getItem(position)).setPresenter(new MusicListPresenter()));
            transaction.commit();
        }
    }
}
