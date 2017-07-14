package com.example.musiclib.util;

import android.media.MediaMetadataRetriever;

import com.example.musiclib.bean.AbstractMusic;
import com.example.musiclib.bean.LocalMusicInfo;
import com.example.musiclib.util.runnable.Runnable2;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhiguang on 2017/7/11.
 */

public class ScanfMusicUtil {

    private static final int MIN_FILE_SEARCH_SIZE = 1 * 1024 * 1024;
    static List<File> dirFile = new ArrayList<File>();
    // 合法的后缀名称
    private static String[] postfixs = new String[]{".aac", ".m4a",
            ".mp3"};

    private static boolean isValid(String postfix) {
        for (int i = 0; i < postfixs.length; i++) {
            if (postfix.endsWith(postfixs[i])) {
                return true;
            }
        }

        return false;
    }

    // 获取所有文件
    private static List<File> getFiles(File file) {
        dirFile.clear();
        scan(file);
        return dirFile;
    }

    // private static long valueableLength = 500 * 1024;

    /**
     * 只支持aac，MP3，m4a,tmd(txz media data)这几种格式
     *
     * @param path
     */
    private static void scan(String path) {
        File file = new File(path);
        scan(file);
    }

    private static void scan(File file) {
        File[] listFiles = null;

        listFiles = file.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String filename) {
                if (new File(dir, filename).isDirectory())
                    return true;
                if (filename.contains(".")) {// 有后缀名
                    return isValid(filename);
                }
                return false;
            }
        });
//        if (listFiles == null)
//            LogUtil.d("list==null:"+file.getAbsolutePath());
//        else if (listFiles.length == 0)
//            LogUtil.d("list==0:"+file.getAbsolutePath());
        if (listFiles == null || listFiles.length <= 0) {
            return;
        }
        for (int i = 0; i < listFiles.length; i++) {
            File childFile = listFiles[i];
            // LogUtil.logd("File:" + childFile.getName());
            if (childFile.exists()) {
                if (childFile.isDirectory()) {
                    scan(childFile.getAbsolutePath());
                } else {
                    // String name = childFile.getAbsolutePath();
                    if (childFile.getName().contains(".")) {
                        if (childFile.length() > MIN_FILE_SEARCH_SIZE) {
                            dirFile.add(childFile);
                        }
                    }
                }
            }
        }
    }

    public static void scanMusicByFile(File file, ScanResultCallback result) {
        ThreadUtil.runOnBackground(new Runnable2<File, ScanResultCallback>(file, result) {
            @Override
            public void run() {
                List<AbstractMusic> infos = null;
                List<File> files = getFiles(mP1);
                if (files != null && files.size() > 0) {
                    infos = new ArrayList<>(files.size());
                    LocalMusicInfo info;
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    for (File file : files) {
                        LogUtil.d("filename=" + file.getAbsolutePath() + file.getName());
                        try {
                            info = new LocalMusicInfo();
                            retriever.setDataSource(file.getPath());
                            info.album = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                            info.artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                            info.duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                            info.title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                            info.path = file.getAbsolutePath();
                            info.dir = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - file.getName().length());
                            info.name = file.getName();
                            infos.add(info);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    retriever.release();
                }
                mP2.scanResult(infos);
            }
        });
    }

    public interface ScanResultCallback {
        void scanResult(List<AbstractMusic> musics);
    }
}
