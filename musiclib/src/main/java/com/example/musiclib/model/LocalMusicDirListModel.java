package com.example.musiclib.model;

import com.example.musiclib.bean.AbstractMusic;
import com.example.musiclib.util.MemoryCacheUtil;
import com.lizhiguang.utils.log.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhiguang on 2017/7/12.
 */

public class LocalMusicDirListModel {
    public void getLocalMusicDirList(File file, DataResultCallback callback) {
        if (MemoryCacheUtil.getInstance().isPrepare()) {
            List<AbstractMusic> musicList = MemoryCacheUtil.getInstance().getCache();
            List<List<AbstractMusic>> musics = new ArrayList<>();
            List<AbstractMusic> temp;
            boolean found;
            for (int i = 0; i < musicList.size(); i++) {
                found = false;
                for (int j = 0; j < musics.size(); j++) {
                    if (musicList.get(i).dir.equals(musics.get(j).get(0).dir)) {
                        musics.get(j).add(musicList.get(i));
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    temp = new ArrayList<AbstractMusic>();
                    temp.add(musicList.get(i));
                    musics.add(temp);
                }
            }
            callback.dataResult(musics);
            return;
        }
        LogUtil.d("no memory cache");
//        ScanfMusicUtil.scanMusicByFile(file,callback);
    }

    public interface DataResultCallback {
        void dataResult(List<List<AbstractMusic>> musics);
    }
}
