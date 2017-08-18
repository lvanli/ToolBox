package com.example.musiclib.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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
import com.example.musiclib.defines.LocalBroadcastDefine;
import com.example.musiclib.proxy.LocalMusicManager;
import com.example.musiclib.ui.click.NoDoubleClickListener;

/**
 * Created by lizhiguang on 2017/7/12.
 */

public class MusicBottomBarFragment extends Fragment implements BroadcastDefine,LocalBroadcastDefine {
    private TextView mTitle;
    private TextView mArtist;
    private Button mNext, mPrev;
    private ToggleButton mPlay;
    private SeekBar mSeekBar;
    private boolean isTouch;
    private AbstractMusic mPlayMusic;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            switch (action) {
                case PLAY_STATUS_UPDATE:
                    boolean isPlaying = intent.getBooleanExtra("isPlaying", false);
                    mPlay.setChecked(isPlaying);
                    break;
                case INTENT_RECONNECT_SUCCESS:
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
                case PLAY_STATUS_RESET:
                    LocalMusicManager.getInstance().setControler(null);
                    updateBottomBarFromService(null);
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
        initReceiver();
    }

    protected void initListener() {
        mNext.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (mPlayMusic != null)
                    LocalMusicManager.getInstance().next();
            }
        });
        mPrev.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (mPlayMusic != null)
                    LocalMusicManager.getInstance().prev();
            }
        });

        mPlay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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

    protected void initReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PLAYBAR_UPDATE);
        intentFilter.addAction(CURRENT_UPDATE);
        intentFilter.addAction(PLAY_STATUS_UPDATE);
        intentFilter.addAction(PLAY_STATUS_RESET);
        getActivity().registerReceiver(receiver, intentFilter);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,
                new IntentFilter(INTENT_RECONNECT_SUCCESS));
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(receiver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
        super.onDestroy();
    }

    void updateBottomBarFromService(AbstractMusic music) {
        mPlayMusic = music;
        if (music != null) {
            if (!TextUtils.isEmpty(music.title))
                mTitle.setText(music.title);
            else
                mTitle.setText(music.name);
            mArtist.setText(music.artist);
            mPlay.setChecked(LocalMusicManager.getInstance().isPlaying());
            mPlay.setEnabled(true);
        } else {
            mTitle.setText("");
            mArtist.setText("");
            mPlay.setChecked(false);
            mSeekBar.setProgress(0);
            mPlay.setEnabled(false);
        }
    }
}
