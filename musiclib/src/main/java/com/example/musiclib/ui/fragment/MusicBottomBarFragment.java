package com.example.musiclib.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.musiclib.R;
import com.example.musiclib.bean.AbstractMusic;
import com.example.musiclib.defines.BroadcastDefine;
import com.example.musiclib.proxy.LocalMusicManager;
import com.example.musiclib.ui.click.NoDoubleClickListener;

/**
 * Created by lizhiguang on 2017/7/12.
 */

public class MusicBottomBarFragment extends Fragment implements BroadcastDefine {
    private TextView mTitle;
    private TextView mArtist;
    private Button mNext, mPrev;
    private ToggleButton mPlay;
    private SeekBar mSeekBar;
    private boolean isTouch;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();

            switch (action) {
                case PLAY_STATUS_UPDATE:
                    boolean isPlaying = intent.getBooleanExtra("isPlaying", false);
                    mPlay.setChecked(LocalMusicManager.getInstance().isPlaying());
                    break;
                case PLAYBAR_UPDATE:
                    AbstractMusic music = LocalMusicManager.getInstance().getNowPlayingSong();
                    if (music != null) {
                        mSeekBar.setMax(Integer.parseInt(music.duration));
                        updateBottomBarFromService(music);
                    }
                    break;
                case CURRENT_UPDATE:
                    if (!isTouch)
                        mSeekBar.setProgress(intent.getIntExtra("currentTime", 0));
                    break;
            }
        }

    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_bottom_bar, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        mSeekBar = (SeekBar) view.findViewById(R.id.music_bottom_seek);
        mTitle = (TextView) view.findViewById(R.id.music_bottom_title);
        mArtist = (TextView) view.findViewById(R.id.music_bottom_artist);
        mNext = (Button) view.findViewById(R.id.music_bottom_next);
        mPrev = (Button) view.findViewById(R.id.music_bottom_prev);
        mPlay = (ToggleButton) view.findViewById(R.id.music_bottom_play);
        isTouch = false;
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTouch = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                LocalMusicManager.getInstance().seekTo(seekBar.getProgress());
                isTouch = false;
            }
        });
        initListener();
        initReciever();
    }

    protected void initListener() {
        mNext.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                LocalMusicManager.getInstance().next();
            }
        });
        mPrev.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                LocalMusicManager.getInstance().prev();
            }
        });

        mPlay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked != LocalMusicManager.getInstance().isPlaying()) {
                    //播放意图
                    if (isChecked) {
                        LocalMusicManager.getInstance().play();
                    } else {
                        LocalMusicManager.getInstance().pause();
                    }
                }
            }
        });
    }

    protected void initReciever() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PLAYBAR_UPDATE);
        intentFilter.addAction(CURRENT_UPDATE);
        intentFilter.addAction(PLAY_STATUS_UPDATE);
        getActivity().registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    void updateBottomBarFromService(AbstractMusic music) {
        if (music != null) {
            if (!TextUtils.isEmpty(music.title))
                mTitle.setText(music.title);
            else
                mTitle.setText(music.name);
            mArtist.setText(music.artist);
            mPlay.setChecked(LocalMusicManager.getInstance().isPlaying());
        }
    }
}
