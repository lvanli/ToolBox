package com.lizhiguang.news.util.cache;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;


import com.lizhiguang.utils.log.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Created by lizhiguang on 2017/4/21.
 */

public class FileCacheUtil {
    private static final double MB = 1024 * 1024;
    Context mContext;

    public FileCacheUtil(Context context) {
        mContext = context;
    }

    public static String getDiskCachePath(Context context) {
        File file;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            file = new File(context.getExternalCacheDir().getPath());
            if (!file.exists())
                file.mkdir();
            return context.getExternalCacheDir().getPath();
        } else {
            file = new File(context.getCacheDir().getPath());
            if (!file.exists())
                file.mkdir();
            return context.getCacheDir().getPath();
        }
    }

    public boolean isExist(String tag) {
        LogUtil.d("tag=" + tag + ",path=" + getDiskCachePath(mContext));
        String path = getDiskCachePath(mContext) + File.separator + tag + ".cache";
        File file = new File(path);
        return file.exists();
    }

    private int getFreeMB() {
        StatFs stat = new StatFs(getDiskCachePath(mContext));
        double free;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
            free = stat.getAvailableBlocksLong() * stat.getBlockSizeLong() / MB;
        else
            free = ((double) stat.getAvailableBlocks() * (double) stat
                    .getBlockSize()) / MB;
        return (int) free;
    }

    public void putCache(String tag, Object o) {
        if (getFreeMB() <= 1) {
            return;
        }
        String path = getDiskCachePath(mContext) + File.separator + tag + ".cache";
        LogUtil.d("path=" + path);
        File file = new File(path);
        try {
            if (!file.exists())
                file.createNewFile();
            OutputStream outputStream = new FileOutputStream(file);
            ObjectOutputStream oOutputStream = new ObjectOutputStream(outputStream);
            oOutputStream.writeObject(o);
            oOutputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object getCache(String tag) {
        String path = getDiskCachePath(mContext) + File.separator + tag + ".cache";
        File file = new File(path);
        if (!file.exists())
            return null;
        try {
            InputStream inputStream = new FileInputStream(file);
            ObjectInputStream oInputStream = new ObjectInputStream(inputStream);
            Object o = oInputStream.readObject();
            oInputStream.close();
            inputStream.close();
            return o;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void recycleCache(String tag) {
        String path = getDiskCachePath(mContext) + File.separator + tag + ".cache";
        File file = new File(path);
        if (!file.exists())
            return;
        file.delete();
    }

    public void clearCache() {
        File file = new File(getDiskCachePath(mContext));
        if (file.exists() && file.isDirectory()) {
            File files[] = file.listFiles();
            if (files != null)
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isFile())
                        files[i].delete();
                }
        }
    }
}
