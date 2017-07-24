package com.example.musiclib.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.musiclib.IMusicControlerService;
import com.example.musiclib.MusicMainActivity;
import com.example.musiclib.R;
import com.example.musiclib.bean.AbstractMusic;
import com.example.musiclib.defines.BroadcastDefine;
import com.example.musiclib.defines.PlayModeDefine;
import com.example.musiclib.util.LogUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 后台控制播放音乐的service
 */
public class MusicControlService extends Service implements MediaPlayer.OnCompletionListener, BroadcastDefine, PlayModeDefine {
    public static final int MSG_CURRENT = 0;
    public static final String PLAY_EXIT = "com.huwei.intent.PLAY_EXIT_ACTION";
    public static final String NEXT_SONG = "com.intent.action.NEXT_SONG";
    public static final String PRE_SONG = "com.intent.action.PRE_SONG";
    public static final String PLAY_OR_PAUSE = "com.intent.action.PLAY_OR_PAUSE";
    NotificationManager mNotificationManager;
    Notification mNotification;
    RemoteViews reViews;
    private int musicIndex = -1;
    private List<AbstractMusic> musicList;
    private List<Integer> musicPlayList;
    private MediaPlayer mp;
    private int mPlayMode = PLAY_MODE_NORMAL;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_CURRENT:
                    Intent intent = new Intent(CURRENT_UPDATE);
                    if (mp.isPlaying()) {
                        int currentTime = mp.getCurrentPosition();
                        LogUtil.i(currentTime + "");
                        intent.putExtra("currentTime", currentTime);
                        sendBroadcast(intent);

                        handler.sendEmptyMessageDelayed(MSG_CURRENT, 500);
                    }
                    break;
            }
        }
    };
    private IMusicControlerService.Stub mBinder = new IMusicControlerService.Stub() {
        @Override
        public int getPid() throws RemoteException {
            return Process.myPid();
        }

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void play() throws RemoteException {
            if (musicList != null) {
                if (reViews != null) {
                    reViews.setViewVisibility(R.id.button_play_notification_play, View.GONE);
                    reViews.setViewVisibility(R.id.button_pause_notification_play, View.VISIBLE);
                }
                mNotificationManager.notify(NT_PLAYBAR_ID, mNotification);


                //准备播放源，准备后播放
                AbstractMusic music = mBinder.getNowPlayingSong();

                LogUtil.i("play()->" + music.title);
                if (!mp.isPlaying()) {
                    LogUtil.i("Enterplay()");
                    mp.start();
                    updatePlayStatus(true);
                }
            }
        }

        @Override
        public void pause() throws RemoteException {
            if (musicList != null) {
                reViews.setViewVisibility(R.id.button_play_notification_play, View.VISIBLE);
                reViews.setViewVisibility(R.id.button_pause_notification_play, View.GONE);

                mNotificationManager.notify(NT_PLAYBAR_ID, mNotification);

                mp.pause();
                handler.removeMessages(MSG_CURRENT);

                updatePlayStatus(false);
            }
        }

        @Override
        public void stop() throws RemoteException {
            stopForeground(true);
        }

        @Override
        public void seekTo(int mesc) throws RemoteException {
            if (musicList == null || musicList.size() <= 0)
                return;
            mp.seekTo(mesc);
        }

        @Override
        public void preparePlayingList(int index, List list) throws RemoteException {
            musicIndex = index;
            musicList = list;
            musicPlayList = new ArrayList<>(musicList.size());
            reloadPlayIndex();

            LogUtil.d("musicList:" + list + " musicIndex:" + index + "now title:" + ((AbstractMusic) list.get(index)).title);

            if (musicList == null || musicList.size() == 0) {
                Toast.makeText(getBaseContext(), "播放列表为空", Toast.LENGTH_LONG).show();
                return;
            }

            AbstractMusic song = musicList.get(musicPlayList.get(musicIndex));
            prepareSong(song);
        }

        @Override
        public boolean isPlaying() {
            return mp != null && mp.isPlaying();
        }

        @Override
        public int getPlayingSongIndex() throws RemoteException {
            if (musicList == null || musicList.size() <= 0)
                return -1;
            return musicPlayList.get(musicIndex);
        }

        @Override
        public AbstractMusic getNowPlayingSong() throws RemoteException {
            if (musicList == null || musicList.size() <= 0)
                return null;
            return musicList.get(musicPlayList.get(musicIndex));
        }

        @Override
        public void nextSong() throws RemoteException {
            next(false);
        }

        @Override
        public void preSong() throws RemoteException {
            if (musicList == null || musicList.size() <= 0)
                return;
            musicIndex = (musicIndex - 1) % musicList.size();
            prepareSong(musicList.get(musicPlayList.get(musicIndex)));
        }

        @Override
        public void randomSong() throws RemoteException {
            if (musicList == null || musicList.size() <= 0)
                return;
            musicIndex = new Random().nextInt(musicList.size());
            prepareSong(musicList.get(musicPlayList.get(musicIndex)));
        }

        @Override
        public void setMode(int mode) throws RemoteException {
            LogUtil.d("set mode=" + mode);
            mPlayMode = mode;
            reloadPlayIndex();
        }

    };
    private BroadcastReceiver controlReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.d("intent="+intent.getAction());
            switch (intent.getAction()) {
                case PLAY_EXIT:
                    stopSelf();
                    mNotificationManager.cancel(NT_PLAYBAR_ID);

                    Process.killProcess(Process.myPid());
                    break;
                case NEXT_SONG:
                    try {
                        mBinder.nextSong();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;
                case PRE_SONG:
                    try {
                        mBinder.preSong();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case PLAY_OR_PAUSE:
                    try {
                        if (mBinder.isPlaying()) {
                            mBinder.pause();
                        } else {
                            mBinder.play();
                        }

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    static MediaPlayer getMediaPlayer(Context context) {

        MediaPlayer mediaplayer = new MediaPlayer();

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            return mediaplayer;
        }

        try {
            Class<?> cMediaTimeProvider = Class.forName("android.media.MediaTimeProvider");
            Class<?> cSubtitleController = Class.forName("android.media.SubtitleController");
            Class<?> iSubtitleControllerAnchor = Class.forName("android.media.SubtitleController$Anchor");
            Class<?> iSubtitleControllerListener = Class.forName("android.media.SubtitleController$Listener");

            Constructor constructor = cSubtitleController.getConstructor(new Class[]{Context.class, cMediaTimeProvider, iSubtitleControllerListener});

            Object subtitleInstance = constructor.newInstance(context, null, null);

            Field f = cSubtitleController.getDeclaredField("mHandler");

            f.setAccessible(true);
            try {
                f.set(subtitleInstance, new Handler());
            } catch (IllegalAccessException e) {
                return mediaplayer;
            } finally {
                f.setAccessible(false);
            }

            Method setsubtitleanchor = mediaplayer.getClass().getMethod("setSubtitleAnchor", cSubtitleController, iSubtitleControllerAnchor);

            setsubtitleanchor.invoke(mediaplayer, subtitleInstance, null);
            //LogUtil.e("", "subtitle is setted :p");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mediaplayer;
    }

    private void reloadPlayIndex() {
        if (musicPlayList != null) {
            musicPlayList.clear();
            switch (mPlayMode) {
                case PLAY_MODE_NORMAL:
                case PLAY_MODE_SINGLE:
                    for (int i = 0; i < musicList.size(); i++)
                        musicPlayList.add(i);
                    break;
                case PLAY_MODE_RANDOM:
                    for (int i = 0; i < musicList.size(); i++)
                        musicPlayList.add(i);
                    Random random = new Random(SystemClock.elapsedRealtime());
                    int r, temp;
                    for (int i = 0; i < musicList.size(); i++) {
                        r = random.nextInt(musicList.size() - i);
                        temp = musicPlayList.get(r + i);
                        musicPlayList.set(i + r, musicPlayList.get(i));
                        musicPlayList.set(i, temp);
                        if (temp == musicIndex)
                            musicIndex = i;
                    }
                    break;
            }
            for (int i = 0; i < musicPlayList.size(); i++) {
                LogUtil.d(i + "=" + musicPlayList.get(i));
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotification = new Notification();

        if (mp != null) {
            mp.release();
            mp.reset();
        }

        mp = getMediaPlayer(getBaseContext());
        mp.setOnCompletionListener(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(PLAY_EXIT);
        filter.addAction(PRE_SONG);
        filter.addAction(NEXT_SONG);
        filter.addAction(PLAY_OR_PAUSE);
        LogUtil.d("registerReceiver");
        registerReceiver(controlReceiver, filter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.i("mBinder:" + mBinder);
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
//        mp.release();
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        mNotificationManager.cancel(NT_PLAYBAR_ID);

        LogUtil.d("unregisterReceiver");
        unregisterReceiver(controlReceiver);
        super.onDestroy();
    }

    private void updatePlayStatus(boolean isPlaying) {
        Intent intent = new Intent(PLAY_STATUS_UPDATE);
        intent.putExtra("isPlaying", isPlaying);
        sendBroadcast(intent);
    }

    /**
     * 准备音乐并播放
     *
     * @param music 音乐
     */
    private void prepareSong(AbstractMusic music) {
        LogUtil.d("prepareSong music:" + music.name);

        showMusicPlayerNotification(music);

        sendBroadcast(new Intent(PLAYBAR_UPDATE));

        play(music);
    }

    private void next(boolean auto) {
        if (musicList == null || musicList.size() <= 0)
            return;
        if (!auto || mPlayMode != PLAY_MODE_SINGLE) {
            musicIndex = (musicIndex + 1) % musicList.size();
        }
        LogUtil.d("index=" + musicIndex + ",real=" + musicPlayList.get(musicIndex) + ",auto=" + auto + ",mode=" + mPlayMode);
        prepareSong(musicList.get(musicPlayList.get(musicIndex)));
    }

    private void play(AbstractMusic music) {
        if (mp != null) {
            mp.reset();
        }

        try {
            if (mp != null) {
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                LogUtil.i("datasoure:" + music.path);
                mp.setDataSource(music.path);
                mp.prepare();
                mp.start();
                handler.sendEmptyMessage(MSG_CURRENT);
            } else
                LogUtil.e("play mp=null!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        next(true);
    }

    /**
     * 在通知栏显示音乐播放信息
     *
     * @param music ?
     */
    void showMusicPlayerNotification(AbstractMusic music) {
        String title = music.title;
        String artist = music.artist;
        if (title == null)
            title = music.name;
        if (reViews == null) {
            reViews = new RemoteViews(getPackageName(), R.layout.notification_play);
        }

        mNotification.icon = R.drawable.sweet;
        mNotification.tickerText = title + "-" + artist;
        mNotification.when = System.currentTimeMillis();
        mNotification.flags = Notification.FLAG_NO_CLEAR;
        mNotification.contentView = reViews;

        reViews.setTextViewText(R.id.title, title);
        reViews.setTextViewText(R.id.text, artist);

        Intent intent = new Intent(getBaseContext(), MusicMainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, 0);
        reViews.setOnClickPendingIntent(R.id.nt_container, pendingIntent);

        Intent exitIntent = new Intent(PLAY_EXIT);
        PendingIntent exitPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, exitIntent, 0);
        reViews.setOnClickPendingIntent(R.id.button_exit_notification_play, exitPendingIntent);

        Intent nextInent = new Intent(NEXT_SONG);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, nextInent, 0);
        reViews.setOnClickPendingIntent(R.id.button_next_notification_play, nextPendingIntent);

        Intent preInent = new Intent(PRE_SONG);
        PendingIntent prePendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, preInent, 0);
        reViews.setOnClickPendingIntent(R.id.button_previous_notification_play, prePendingIntent);

        Intent playInent = new Intent(PLAY_OR_PAUSE);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, playInent, 0);
        reViews.setOnClickPendingIntent(R.id.button_play_notification_play, playPendingIntent);
        reViews.setOnClickPendingIntent(R.id.button_pause_notification_play, playPendingIntent);


        final NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        builder.setContent(reViews).setSmallIcon(NT_PLAYBAR_ID).setTicker(title).setOngoing(true);

        mNotificationManager.notify(NT_PLAYBAR_ID, mNotification);
    }
}
