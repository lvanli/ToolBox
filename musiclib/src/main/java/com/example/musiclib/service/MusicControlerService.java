package com.example.musiclib.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
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
public class MusicControlerService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, BroadcastDefine, PlayModeDefine {
    public static final int MSG_CURRENT = 0;
    public static final int MSG_BUFFER_UPDATE = 1;
    public static final int MSG_NOTICATION_UPDATE = 2;
    public static final int MSG_PLAY = 101;
    public static final String PLAYPRO_EXIT = "com.huwei.intent.PLAYPRO_EXIT_ACTION";
    public static final String NEXTSONG = "com.intent.action.NEXTSONG";
    public static final String PRESONG = "com.intent.action.PRESONG";
    public static final String PLAY_OR_PASUE = "com.intent.action.PLAY_OR_PASUE";
    NotificationManager mNoticationManager;
    Notification mNotification;
    RemoteViews reViews;
    private String TAG = "MusicControlerService";
    private int musicIndex = -1;
    private List<AbstractMusic> musicList;
    private List<Integer> musicPlayList;
    private MediaPlayer mp;
    private int mPlayMode = PLAY_MODE_NORMAL;
    private boolean isForeground;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_CURRENT:
                    Intent intent = new Intent(CURRENT_UPDATE);
                    if (mp.isPlaying()) {
                        int currentTime = mp.getCurrentPosition();
                        Log.i("currentTime", currentTime + "");
                        intent.putExtra("currentTime", currentTime);
                        sendBroadcast(intent);

                        handler.sendEmptyMessageDelayed(MSG_CURRENT, 500);
                    }
                    break;
                case MSG_BUFFER_UPDATE:

                    intent = new Intent(BUFFER_UPDATE);
                    int bufferTime = msg.arg1;
                    Log.i("bufferTime", bufferTime + "");
                    intent.putExtra("bufferTime", bufferTime);
                    sendBroadcast(intent);
                    break;
                case MSG_NOTICATION_UPDATE:
                    reViews.setImageViewBitmap(R.id.img_album, (Bitmap) msg.obj);
                    break;
                case MSG_PLAY:
                    AbstractMusic music = (AbstractMusic) msg.obj;
                    play(music);
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
            if (reViews != null) {
                reViews.setViewVisibility(R.id.button_play_notification_play, View.GONE);
                reViews.setViewVisibility(R.id.button_pause_notification_play, View.VISIBLE);
            }
            if (musicList != null) {
                mNoticationManager.notify(NT_PLAYBAR_ID, mNotification);


                //准备播放源，准备后播放
                AbstractMusic music = mBinder.getNowPlayingSong();

                Log.i(TAG, "play()->" + music.title);
                if (!mp.isPlaying()) {
                    Log.i(TAG, "Enterplay()");
                    mp.start();
                    updatePlayStaute(true);
                }
            }
        }

        @Override
        public void pause() throws RemoteException {
            reViews.setViewVisibility(R.id.button_play_notification_play, View.VISIBLE);
            reViews.setViewVisibility(R.id.button_pause_notification_play, View.GONE);

            mNoticationManager.notify(NT_PLAYBAR_ID, mNotification);

            mp.pause();
            handler.removeMessages(MSG_CURRENT);

            updatePlayStaute(false);
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

            Log.d(TAG, "musicList:" + list + " musicIndex:" + index + "now title:" + ((AbstractMusic) list.get(index)).title);

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
        public boolean isForeground() throws RemoteException {
            return isForeground;
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
            switch (intent.getAction()) {
                case PLAYPRO_EXIT:
                    stopSelf();
                    mNoticationManager.cancel(NT_PLAYBAR_ID);

                    Process.killProcess(Process.myPid());
                    break;
                case NEXTSONG:
                    try {
                        mBinder.nextSong();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    break;
                case PRESONG:
                    try {
                        mBinder.preSong();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case PLAY_OR_PASUE:
                    try {
                        if (mBinder.isPlaying()) {
                            //暂停

                            mBinder.pause();
                        } else {
                            //播放

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
            //Log.e("", "subtitle is setted :p");
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

        mNoticationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotification = new Notification();

        if (mp != null) {
            mp.release();
            mp.reset();
        }

        mp = getMediaPlayer(getBaseContext());
        mp.setOnCompletionListener(this);
        mp.setOnBufferingUpdateListener(this);
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i(TAG, "onPrepared");

                handler.sendEmptyMessage(MSG_CURRENT);

                try {
                    mBinder.play();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction(PLAYPRO_EXIT);
        filter.addAction(PRESONG);
        filter.addAction(NEXTSONG);
        filter.addAction(PLAY_OR_PASUE);
//        filter.addAction(BringToFrontReceiver.ACTION_BRING_TO_FRONT);
        registerReceiver(controlReceiver, filter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "mBinder:" + mBinder);
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
        isForeground = false;

        unregisterReceiver(controlReceiver);
        super.onDestroy();
    }

    /**
     * 和上一次操作的歌曲不同，代表新播放的歌曲
     *
     * @param isNewPlayMusic 干嘛的？
     */

    private void updatePlayBar(boolean isNewPlayMusic) {
        Intent intent = new Intent(PLAYBAR_UPDATE);
        intent.putExtra("isNewPlayMusic", isNewPlayMusic);

        sendBroadcast(intent);
    }

    private void updatePlayStaute(boolean isPlaying) {
        Intent intent = new Intent(PLAY_STATUS_UPDATE);
        intent.putExtra("isPlaying", isPlaying);

        sendBroadcast(intent);
    }

    /**
     * 准备音乐并播放
     *
     * @param music ?
     */
    private void prepareSong(AbstractMusic music) {
        Log.d(TAG, "prepareSong music:" + music.name);

        showMusicPlayerNotification(music);
        //TODO lzg
        updatePlayBar(true);//!music.isOnlineMusic());

        //如果是网络歌曲,而且未从网络获取详细信息，则需要获取歌曲的详细信息
//        if (music.getType() == AbstractMusic.MusicType.Online) {
//            final Song song = (Song) music;
//            if (!song.hasGetDetailInfo()) {
//
//                //同步请求到歌曲信息
//                BaiduMusicUtil.querySong(song.song_id, new HttpHandler() {
//                    @Override
//                    public void onSuccess(String response) {
//                        SongPlayResp resp = new Gson().fromJson(response,SongPlayResp.class);
//                        if(resp!=null) {
//                            song.bitrate = resp.bitrate;
//                            song.songinfo = resp.songinfo;
//
//                            Log.i(TAG,"song hasGetDetailInfo:"+song);
//
//                            updatePlayBar(true);
//
//                            Message msg = Message.obtain();
//                            msg.what = MSG_PLAY;
//                            msg.obj = song;
//                            handler.sendMessage(msg);
//
//                            updateArtistView(song);
//                        }
//                    }
//                });
//            } else {
//                play(music);
//            }
//        } else {
        play(music);
//        }
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
                Log.i(TAG, "datasoure:" + music.path);
                //TODO 区别不同播放源 lzg
                mp.setDataSource(music.path);
                mp.prepare();
                handler.sendEmptyMessage(MSG_CURRENT);
                mp.start();
//                mp.prepareAsync();
            } else
                Log.e(TAG, "play mp=null!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        AbstractMusic music = null;
        try {
            music = mBinder.getNowPlayingSong();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (music != null) {
            Message msg = Message.obtain();
            msg.what = MSG_BUFFER_UPDATE;
            msg.arg1 = (int) (percent * Long.parseLong(music.duration) / 1000);

            handler.sendMessage(msg);
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

        reViews.setImageViewResource(R.id.img_album, R.drawable.img_album_background);
//        ImageLoader imageLoader = SweetApplication.getImageLoader();
//        imageLoader.loadImage(music.getArtPic(), new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                super.onLoadingComplete(imageUri, view, loadedImage);
//
//                reViews.setImageViewBitmap(R.id.img_album, loadedImage);
//            }
//        });
//
//        Log.i(TAG, "picUri:" + music.getArtPic());


        Intent intent = new Intent(getBaseContext(), MusicMainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, 0);
        reViews.setOnClickPendingIntent(R.id.nt_container, pendingIntent);

        Intent exitIntent = new Intent(PLAYPRO_EXIT);
        PendingIntent exitPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, exitIntent, 0);
        reViews.setOnClickPendingIntent(R.id.button_exit_notification_play, exitPendingIntent);

        Intent nextInent = new Intent(NEXTSONG);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, nextInent, 0);
        reViews.setOnClickPendingIntent(R.id.button_next_notification_play, nextPendingIntent);

        Intent preInent = new Intent(PRESONG);
        PendingIntent prePendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, preInent, 0);
        reViews.setOnClickPendingIntent(R.id.button_previous_notification_play, prePendingIntent);

        Intent playInent = new Intent(PLAY_OR_PASUE);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, playInent, 0);
        reViews.setOnClickPendingIntent(R.id.button_play_notification_play, playPendingIntent);
        reViews.setOnClickPendingIntent(R.id.button_pause_notification_play, playPendingIntent);


        final NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());
        builder.setContent(reViews).setSmallIcon(NT_PLAYBAR_ID).setTicker(title).setOngoing(true);

        updateArtistView(music);

        mNoticationManager.notify(NT_PLAYBAR_ID, mNotification);
    }

    void updateArtistView(AbstractMusic music) {
        //TODO lzg
//        music.loadArtPic(new AbstractMusic.OnLoadListener() {
//            @Override
//            public void onSuccessLoad(Bitmap bitmap) {
//                Log.i(TAG,"onSuccessLoad bitmap:"+bitmap);
//
//                if(reViews!=null) {
//                    reViews.setImageViewBitmap(R.id.img_album, bitmap);
//                    mNoticationManager.notify(NT_PLAYBAR_ID,mNotification);
//                }
//            }
//        });
    }


}
