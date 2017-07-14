package com.example.musiclib.proxy;

import android.os.RemoteException;

import com.example.musiclib.IMusicControlerService;
import com.example.musiclib.bean.AbstractMusic;
import com.example.musiclib.bean.LocalMusicInfo;

import java.util.List;

/**
 * Created by lizhiguang on 2017/7/11.
 */

public class LocalMusicManager {
    private static LocalMusicManager instance = null;
    private IMusicControlerService mControler = null;

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

    public void setControler(IMusicControlerService service) {
        mControler = service;
    }

    public void preparePlayingList(List<LocalMusicInfo> infos, int position) {
        if (mControler != null)
            try {
                mControler.preparePlayingList(position, infos);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
    }

    public void stop() {
        if (mControler != null)
            try {
                mControler.stop();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
    }

    public void play() {
        if (mControler != null)
            try {
                mControler.play();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
    }

    public void pause() {
        if (mControler != null)
            try {
                mControler.pause();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
    }

    public void next() {
        if (mControler != null)
            try {
                mControler.nextSong();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
    }

    public void prev() {
        if (mControler != null)
            try {
                mControler.preSong();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
    }

    public void randomSong() {
        if (mControler != null)
            try {
                mControler.randomSong();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
    }

    public boolean isPlaying() {
        if (mControler != null)
            try {
                return mControler.isPlaying();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        return false;
    }

    public AbstractMusic getNowPlayingSong() {
        if (mControler != null)
            try {
                return mControler.getNowPlayingSong();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        return null;
    }

    public void seekTo(int mesc) {
        if (mControler != null)
            try {
                mControler.seekTo(mesc);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
    }

    public void setMode(int mode) {
        if (mControler != null)
            try {
                mControler.setMode(mode);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
    }
}
