package com.example.musiclib.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.musiclib.bean.AbstractMusic;
import com.example.musiclib.bean.LocalMusicInfo;
import com.example.musiclib.util.runnable.Runnable2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lizhiguang on 2017/7/12.
 */

public class DatabaseUtil {
    private static final String DataFileName = "/musics.db3";
    private static final String MusicTableName = "music_table";
    private static final String MusicNameLine = "music_name";
    private static final String MusicArtistLineName = "music_artist";
    private static final String MusicPathLineName = "music_path";
    private static final String MusicDurationLineName = "music_duration";
    private static final String MusicTitleLineName = "music_title";
    private static final String MusicAlbumLineName = "music_album";
    private static final String MusicDirLineName = "music_dir";

    public static void initInfo(Context context, List<AbstractMusic> musics) {
        ThreadUtil.runOnBackground(new Runnable2<Context, List<AbstractMusic>>(context, musics) {
            @Override
            public void run() {
                SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(mP1.getFilesDir() + DataFileName, null);
                String dropTable = "drop table " + MusicTableName;
                try {
                    db.execSQL(dropTable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String sql = "create table " + MusicTableName + " (_id integer primary key autoincrement," +
                        MusicNameLine + " varchar(50)," +
                        MusicArtistLineName + " varchar(50)," +
                        MusicPathLineName + " varchar(50)," +
                        MusicDurationLineName + " varchar(50)," +
                        MusicTitleLineName + " varchar(50)," +
                        MusicDirLineName + " varchar(50)," +
                        MusicAlbumLineName + " varchar(50))";
                db.execSQL(sql);
                String insertSql = "insert into " + MusicTableName + " values (null,?,?,?,?,?,?,?)";
                for (int i = 0; i < mP2.size(); i++) {
                    db.execSQL(insertSql, new Object[]{mP2.get(i).name, mP2.get(i).artist, mP2.get(i).path, mP2.get(i).duration,
                            mP2.get(i).title, mP2.get(i).dir, mP2.get(i).album});
                }
                db.close();
            }
        });
    }

    public static void getInfo(Context context, DatabaseResultCallback result) {
        ThreadUtil.runOnBackground(new Runnable2<Context, DatabaseResultCallback>(context, result) {
            @Override
            public void run() {
                List<AbstractMusic> musics = null;
                SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
                        mP1.getFilesDir() + DataFileName, null);
                Cursor cursor = null;
                try {
                    cursor = db.rawQuery("select * from " + MusicTableName, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //add a
                Cursor test = cursor;
                if (test == null) {
                    LogUtil.d("cursor=null");
                } else {
                    LogUtil.d("cursorSize=" + test.getColumnCount());
                    LogUtil.d("cursorCount=" + test.getCount());
//            if (test.moveToFirst()) {
//                do {
//                    LogUtil.d( "cursorN=" + test.getString(test.getColumnIndex(MusicNameLine)));
//                } while (test.moveToNext());
//            } else {
//                LogUtil.d( "how!!!");
//            }
                }

                if (cursor != null) {
                    musics = new ArrayList<>(cursor.getCount());
                    if (cursor.moveToFirst()) {
                        AbstractMusic music;
                        do {
                            music = new LocalMusicInfo();
                            music.name = cursor.getString(cursor.getColumnIndex(MusicNameLine));
                            music.album = cursor.getString(cursor.getColumnIndex(MusicAlbumLineName));
                            music.artist = cursor.getString(cursor.getColumnIndex(MusicArtistLineName));
                            music.duration = cursor.getString(cursor.getColumnIndex(MusicDurationLineName));
                            music.path = cursor.getString(cursor.getColumnIndex(MusicPathLineName));
                            music.title = cursor.getString(cursor.getColumnIndex(MusicTitleLineName));
                            music.dir = cursor.getString(cursor.getColumnIndex(MusicDirLineName));
                            musics.add(music);
                        }
                        while (cursor.moveToNext());
                    }
                    cursor.close();
                }
                db.close();
                mP2.databaseResult(musics);
            }
        });
    }

    public interface DatabaseResultCallback {
        void databaseResult(List<AbstractMusic> musics);
    }
}
