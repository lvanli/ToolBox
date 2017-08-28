package com.example.musiclib;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.session.MediaSession;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.musiclib.audioFocus.MyOnAudioFocusChangeListener;
import com.example.musiclib.audioFocus.RemoteControlReceiver;
import com.example.musiclib.bean.AbstractMusic;
import com.example.musiclib.defines.LocalBroadcastDefine;
import com.example.musiclib.proxy.LocalMusicManager;
import com.example.musiclib.service.MusicControlService;
import com.example.musiclib.ui.fragment.MainFragment;
import com.example.musiclib.util.DatabaseUtil;
import com.example.musiclib.util.DialogUtil;
import com.example.musiclib.util.DirChoiceUtil;
import com.example.musiclib.util.MemoryCacheUtil;
import com.example.musiclib.util.ScanfMusicUtil;
import com.example.musiclib.util.SettingUtil;
import com.lizhiguang.utils.log.LogUtil;

import java.io.File;
import java.util.List;

public class MusicMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LocalBroadcastDefine, ScanfMusicUtil.ScanResultCallback, DirChoiceUtil.DirResultCallback {
    private static final String TAG = "MusicMainActivity";
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private MainFragment mainFragment;
    private IMusicControlService musicControl;
    private boolean isServiceBinding = false;
    private AudioManager mAudioManager;
    private ComponentName mRemoteComponentName;
    private MediaSession mSession;
    private MyOnAudioFocusChangeListener onAudioFocusChangeListener = new MyOnAudioFocusChangeListener();
    private ActivityReceiver receiver = new ActivityReceiver();
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(TAG, "onServiceConnected");
            isServiceBinding = true;
            musicControl = IMusicControlService.Stub.asInterface(iBinder);
            LocalMusicManager.getInstance().setControler(musicControl);
            initMusicManager();
            LocalBroadcastManager.getInstance(MusicMainActivity.this).sendBroadcast(new Intent(INTENT_RECONNECT_SUCCESS));
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            LocalMusicManager.getInstance().setControler(null);
            isServiceBinding = false;
            musicControl = null;
        }
    };

    private void initMusicManager() {
        LocalMusicManager.getInstance().setMode(SettingUtil.getInt(this, SettingUtil.SETTING_PLAY_MODE));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_main);
        init();
    }

    private void init() {
        navigationView = (NavigationView) findViewById(R.id.music_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawer = (DrawerLayout) findViewById(R.id.music_main_drawer);
        mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.music_main_fragment, mainFragment, "MainFragment")
                .commit();

        if (!isServiceBinding) {
            Log.i(TAG, "start binding service");
            Intent intent = new Intent(this, MusicControlService.class);
            bindService(intent, mConnection, BIND_AUTO_CREATE);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(INTENT_SEARCH_ERROR);
        filter.addAction(INTENT_RESCAN);
        filter.addAction(INTENT_RECONNECT);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);

        //线控音频焦点注册
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mRemoteComponentName = new ComponentName(getPackageName(), RemoteControlReceiver.class.getName());
        requestAudioFocus();
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onDestroy();
        if(isServiceBinding)
            unbindService(mConnection);
        abandonAudioFocus();
    }

    private void abandonAudioFocus() {
        if (mAudioManager == null || mRemoteComponentName == null) return;
        mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
        mAudioManager.unregisterMediaButtonEventReceiver(mRemoteComponentName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mSession != null) {
                mSession.setCallback(null);
                mSession.setActive(false);
                mSession.release();
            }
        }
    }

    private void requestAudioFocus() {
        int result = mAudioManager
                .requestAudioFocus(onAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            mAudioManager.registerMediaButtonEventReceiver(mRemoteComponentName);
//        else
//            setMediaButtonEvent();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setMediaButtonEvent() {
        mSession = new MediaSession(this, "music");
        mSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS | MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mSession.setCallback(new MediaSession.Callback() {

            @Override
            public void onPlay() {
                super.onPlay();
                LocalMusicManager.getInstance().play();
            }

            @Override
            public void onPause() {
                super.onPause();
                LocalMusicManager.getInstance().pause();
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                LocalMusicManager.getInstance().next();
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                LocalMusicManager.getInstance().prev();
            }

            @Override
            public void onStop() {
                super.onStop();
                LocalMusicManager.getInstance().stop();
            }

            @Override
            public void onSeekTo(long pos) {
                super.onSeekTo(pos);
                LocalMusicManager.getInstance().seekTo((int) pos);
            }
        });
        mSession.setActive(true);
    }

    @Override
    public void dirResult(String path) {
        LogUtil.d("path=" + path);
        if (!TextUtils.isEmpty(path)) {
            SettingUtil.setString(this, SettingUtil.SETTING_SCAN_PATH, path);
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(INTENT_RESCAN));
        }
    }

    public void scanMusicWithFile(File file) {
        ScanfMusicUtil.scanMusicByFile(file, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            getPermission();
        if (!isServiceBinding) {
            Intent intent = new Intent(this, MusicControlService.class);
            startService(intent);

            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            drawer.openDrawer(navigationView);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(navigationView)) {
            drawer.closeDrawers();
        }
        else if(getSupportFragmentManager().getBackStackEntryCount()!=0){
            getSupportFragmentManager().popBackStack();
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public void scanResult(List<AbstractMusic> musics) {
        if (musics == null || musics.size() == 0) {
            LogUtil.d("找不到文件");
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(INTENT_SEARCH_ERROR));
            return;
        }
        DatabaseUtil.initInfo(this, musics);
        MemoryCacheUtil.getInstance().setCache(musics);
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(INTENT_RELOAD));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer.closeDrawers();
        if (item.getItemId() == R.id.music_nav_scan_sdcard) {
            scanMusicWithFile(Environment.getExternalStorageDirectory());
            return true;
        } else if (item.getItemId() == R.id.music_nav_select_dir) {
            DirChoiceUtil.openChoiceDialog(this, this);
            return true;
        } else if (item.getItemId() == R.id.music_nav_select_circle) {
            DialogUtil.showSelectDialog(this, new String[]{"循环播放", "单曲播放", "随机播放"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    saveMode(which);
                    LocalMusicManager.getInstance().setMode(which);
                }
            });
            return true;
        } else if (item.getItemId() == R.id.music_nav_exit) {
            stopService(new Intent(this, MusicControlService.class));
            finish();
            return true;
        } else if (item.getItemId() == R.id.music_nav_return) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.music_nav_auto_close) {
            DialogUtil.showSeekBarDialog(this, "关闭", 90, "分钟", new DialogUtil.DialogCallback() {
                @Override
                public void onSelect(Object o) {
                    int process = (int)o;
                    LocalMusicManager.getInstance().setAutoCloseTime(process);
                }
            });
        }
        return false;
    }

    private void saveMode(int mode) {
        SettingUtil.setInt(this, SettingUtil.SETTING_PLAY_MODE, mode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    void getPermission() {

        int permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED || permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    124);
        }
    }

    class ActivityReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(INTENT_SEARCH_ERROR))
                Toast.makeText(context, "未搜索到内容", Toast.LENGTH_SHORT).show();
            else if (intent.getAction().equals(INTENT_RESCAN)) {
                String path = SettingUtil.getString(context, SettingUtil.SETTING_SCAN_PATH);
                scanMusicWithFile(new File(path));
            } else if (intent.getAction().equals(INTENT_RECONNECT)) {
                if (!isServiceBinding) {
                    Log.i(TAG, "start binding service");
                    Intent connIntent = new Intent(MusicMainActivity.this, MusicControlService.class);
                    bindService(connIntent, mConnection, BIND_AUTO_CREATE);
                }
            }
        }
    }
}
