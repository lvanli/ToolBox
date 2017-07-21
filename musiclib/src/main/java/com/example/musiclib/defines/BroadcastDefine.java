package com.example.musiclib.defines;

/**
 * Created by lizhiguang on 2017/7/12.
 */

public interface BroadcastDefine {
    public static final String PLAYBAR_UPDATE = "com.lizhiguang.intent.PLAYBAR_UPDATE";        //关于歌曲信息控件的更新
    public static final String PLAY_STATUS_UPDATE = "com.lizhiguang.intent.PLAY_STATUS_UPDATE";
    public static final String CURRENT_UPDATE = "com.lizhiguang.intent.DURATION_UPDATE";       //当前状态 有关时间控件的更新
    public static final String BUFFER_UPDATE = "com.lizhiguang.intent.BUFFER_UPDATE";          //在线音乐的缓冲更新
    public static final int NT_PLAYBAR_ID = 987;                                            //Define Notification ID
}
