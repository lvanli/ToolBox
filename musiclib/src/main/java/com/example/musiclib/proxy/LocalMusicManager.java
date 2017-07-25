package com.example.musiclib.proxy;

import android.os.RemoteException;

import com.example.musiclib.IMusicControlService;
import com.example.musiclib.bean.AbstractMusic;

import java.util.List;

/**
 * Created by lizhiguang on 2017/7/11.
 */

public class LocalMusicManager {
    private static LocalMusicManager instance = null;
    private IMusicControlService mControl = null;

    private LocalMusicManager() {
    }

    public static LocalMusicManager getInstance() {
        if (instance == null)
            synchronized (LocalMusicManager.class) {
                if (instance == null)
                    instance = new LocalMusicManager();
            }
        return instance;
    }

    public void setControler(IMusicControlService service) {
        mControl = service;
    }

    public void preparePlayingList(List<AbstractMusic> infos, int position) {
        if (mControl != null)
            try {
                mControl.preparePlayingList(position, infos);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
    }

    public void stop() {
        if (mControl != null)
            try {
                mControl.stop();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
    }

    public void play() {
        if (mControl != null)
            try {
                mControl.play();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
    }

    public void pause() {
        if (mControl != null)
            try {
                mControl.pause();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
    }

    public void next() {
        if (mControl != null)
            try {
                mControl.nextSong();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
    }

    public void prev() {
        if (mControl != null)
            try {
                mControl.preSong();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
    }

    public void randomSong() {
        if (mControl != null)
            try {
                mControl.randomSong();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
    }

    public boolean isPlaying() {
        if (mControl != null)
            try {
                return mControl.isPlaying();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        return false;
    }

    public AbstractMusic getNowPlayingSong() {
        if (mControl != null)
            try {
                return mControl.getNowPlayingSong();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        return null;
    }

    public void seekTo(int mesc) {
        if (mControl != null)
            try {
                mControl.seekTo(mesc);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
    }

    public void setMode(int mode) {
        if (mControl != null)
            try {
                mControl.setMode(mode);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
    }
}
