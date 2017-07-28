package com.example.musiclib.audioFocus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import com.example.musiclib.proxy.LocalMusicManager;
import com.example.musiclib.util.LogUtil;

/**
 * Created by lizhiguang on 2017/7/14.
 */

public class RemoteControlReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)) {
            KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            LogUtil.d("keyAction=" + event.getKeyCode());
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    if (LocalMusicManager.getInstance().isPlaying())
                        LocalMusicManager.getInstance().stop();
                    else
                        LocalMusicManager.getInstance().play();
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                    LocalMusicManager.getInstance().play();
                    break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                    LocalMusicManager.getInstance().stop();
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    LocalMusicManager.getInstance().prev();
                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    LocalMusicManager.getInstance().next();
                    break;
            }
        }
    }
}
