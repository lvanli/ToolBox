package com.lizhiguang.news.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.lizhiguang.news.util.cache.FileCacheUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutionException;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class DownloadIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_DOWNLOAD = "com.lizhiguang.news.service.action.downlaod";
    public static final String ACTION_DOWNLOAD_OVER = "com.lizhiguang.news.service.action.downlaod.over";

    // TODO: Rename parameters
    public static final String EXTRA_FILE_URL = "com.lizhiguang.news.service.extra.url";
    public static final String EXTRA_FILE_PATH = "com.lizhiguang.news.service.extra.path";

    public DownloadIntentService() {
        super("DownloadIntentService");
    }

    public static void startDownloadImg(Context context, String url, String path) {
        Intent intent = new Intent(context, DownloadIntentService.class);
        intent.setAction(ACTION_DOWNLOAD);
        intent.putExtra(EXTRA_FILE_URL, url);
        intent.putExtra(EXTRA_FILE_PATH, path);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_DOWNLOAD.equals(action)) {
                final String url = intent.getStringExtra(EXTRA_FILE_URL);
                final String path = intent.getStringExtra(EXTRA_FILE_PATH);
                handleActionDownload(url, path);
            }
        }
    }


    public String getIdFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionDownload(String url, String path) {
        try {
            Bitmap bitmap = Glide.with(getApplicationContext()).load(url).asBitmap().into(500, 500).get();
            File file = new File(path);
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e1) {
            e1.printStackTrace();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ACTION_DOWNLOAD_OVER));
    }
}
