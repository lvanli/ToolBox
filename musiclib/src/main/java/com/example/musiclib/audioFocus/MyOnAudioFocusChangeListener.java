package com.example.musiclib.audioFocus;

import android.media.AudioManager;

import com.example.musiclib.proxy.LocalMusicManager;
import com.lizhiguang.utils.log.LogUtil;

/**
 * Created by lizhiguang on 2017/7/14.
 */

public class MyOnAudioFocusChangeListener implements AudioManager.OnAudioFocusChangeListener {
    private boolean isAutoLoss = false;
    @Override
    public void onAudioFocusChange(int focusChange) {
        LogUtil.d("focusChange:" + focusChange);
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN: // 重新获得焦点,  可做恢复播放，恢复后台音量的操作
                if (isAutoLoss)
                    LocalMusicManager.getInstance().play();
                isAutoLoss = false;
                break;
            case AudioManager.AUDIOFOCUS_LOSS: // 永久丢失焦点除非重新主动获取，这种情况是被其他播放器抢去了焦点，  为避免与其他播放器混音，可将音乐暂停
                if (LocalMusicManager.getInstance().isPlaying()) {
                    isAutoLoss = true;
                    LocalMusicManager.getInstance().stop();
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT: // 暂时丢失焦点，这种情况是被其他应用申请了短暂的焦点，可压低后台音量
                if (LocalMusicManager.getInstance().isPlaying()) {
                    isAutoLoss = true;
                    LocalMusicManager.getInstance().pause();
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK: // 短暂丢失焦点，这种情况是被其他应用申请了短暂的焦点希望其他声音能压低音量（或者关闭声音）凸显这个声音（比如短信提示音），
                break;
        }
    }
}
