package com.example.musiclib;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.example.musiclib.presenter.BasePresenter;
import com.example.musiclib.util.runnable.Runnable1;

/**
 * Created by lizhiguang on 2017/7/11.
 */

public abstract class BaseFragment extends Fragment {
    protected Context mContext;
    protected BasePresenter mPresenter;

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
}
