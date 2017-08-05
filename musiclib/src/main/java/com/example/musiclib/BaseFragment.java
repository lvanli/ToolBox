package com.example.musiclib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.example.musiclib.bean.AbstractMusic;
import com.example.musiclib.defines.LocalBroadcastDefine;
import com.example.musiclib.presenter.BasePresenter;
import com.example.musiclib.proxy.LocalMusicManager;
import com.lizhiguang.utils.log.LogUtil;
import com.lizhiguang.utils.runnable.Runnable1;
import com.lizhiguang.utils.runnable.Runnable2;

import java.util.List;

/**
 * Created by lizhiguang on 2017/7/11.
 */

public abstract class BaseFragment extends Fragment implements LocalBroadcastDefine{
    protected Context mContext;
    protected BasePresenter mPresenter;
    private Runnable2 connectRun = null;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(INTENT_RECONNECT_SUCCESS)) {
                LogUtil.d("reconnect success");
                if (connectRun != null) {
                    connectRun.run();
                    connectRun = null;
                }
            }
        }
    };

    public BaseFragment(){}

    public BaseFragment(Context context) {
        mContext = context;
    }

    public abstract String getTitle();

    public BasePresenter getPresenter() {
        return mPresenter;
    }

    public BaseFragment setPresenter(BasePresenter presenter) {
        mPresenter = presenter;
        mPresenter.setView(this);
        return this;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() != null) {
            mContext = getActivity();
        }
        LocalBroadcastManager.getInstance(mContext).registerReceiver(receiver,new IntentFilter(INTENT_RECONNECT_SUCCESS));
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(receiver);
    }

    public abstract void updateData(Object o);

    public void showToast(String msg) {
        runOnUiThread(new Runnable1<String>(msg) {
            @Override
            public void run() {
                Toast.makeText(mContext, mP1, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showLongToast(String msg) {
        runOnUiThread(new Runnable1<String>(msg) {
            @Override
            public void run() {
                Toast.makeText(mContext, mP1, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void runOnUiThread(Runnable runnable) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(runnable);
        }
    }

    public abstract void reload();
    public void reconnect(List<AbstractMusic> infos, int position) {
        LogUtil.d("reconnect");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(INTENT_RECONNECT));
        connectRun = new Runnable2<List<AbstractMusic> , Integer>(infos,position) {
            @Override
            public void run() {
                LocalMusicManager.getInstance().preparePlayingList(mP1,mP2);
            }
        };
    }
}
