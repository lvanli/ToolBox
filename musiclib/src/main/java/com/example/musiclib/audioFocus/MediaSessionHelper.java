package com.example.musiclib.audioFocus;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.example.musiclib.MusicMainActivity;

/**
 * Created by lizhiguang on 2018/5/6.
 */

public class MediaSessionHelper {
    private static MediaSessionHelper instance;
    private final MediaSessionCompat mSessionCompat;

    private MediaSessionHelper(Context context){
        ComponentName componentName = new ComponentName(context,RemoteControlReceiver.class.getName());
        context.getPackageManager().setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
        Intent mediaButton = new Intent(Intent.ACTION_MEDIA_BUTTON);
        mediaButton.setComponent(componentName);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,mediaButton,PendingIntent.FLAG_CANCEL_CURRENT);
        mSessionCompat = new MediaSessionCompat(context,"music",componentName,null);
        mSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mSessionCompat.setMediaButtonReceiver(pendingIntent);
        mSessionCompat.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
                RemoteControlReceiver.getInstance().onReceive(null,mediaButtonEvent);
                return true;
            }
        },new Handler(context.getMainLooper()));
        setPlayStats(PlaybackStateCompat.STATE_NONE);
        if (!mSessionCompat.isActive()) {
            mSessionCompat.setActive(true);
        }
    }
    public static MediaSessionHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (MediaSessionHelper.class) {
                if (instance == null) {
                    instance = new MediaSessionHelper(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public void setPlayStats(int state) {
        mSessionCompat.setPlaybackState(new PlaybackStateCompat.Builder().setState(
                state,0,1.0f).build());
    }
    public void setPlayStats(int state,int position) {
        mSessionCompat.setPlaybackState(new PlaybackStateCompat.Builder().setState(
                state,position,1.0f).build());
    }
}
