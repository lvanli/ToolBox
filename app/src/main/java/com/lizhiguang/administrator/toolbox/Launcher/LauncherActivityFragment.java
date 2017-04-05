package com.lizhiguang.administrator.toolbox.Launcher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.lizhiguang.administrator.toolbox.Basic.BasicPermanentIcon;
import com.lizhiguang.administrator.toolbox.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class LauncherActivityFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "BasicActivityFragment";
    private Context mContext;
    private TableLayout mainLayout;
    private List<AppInfo> appInfos;

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View newView = inflater.inflate(R.layout.fragment_launcher, container, false);
        mainLayout = (TableLayout) newView.findViewById(R.id.launcherLayoutMain);
//        initView();
        return newView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        Bundle bundle = getArguments();
        if (bundle == null)
            return;
        ArrayList list = bundle.getParcelableArrayList("list");
        if (list == null || list.size() <= 0) {
            Log.d(TAG, "initView: list Size=0");
            return;
        }
        appInfos = (List) list.get(0);
        Log.d(TAG, "initView: list size=" + appInfos.size());
        TableRow row = null;
        TableLayout.LayoutParams defaultL = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        int i;
        for (i = 0; i < appInfos.size(); i++) {
            if (i % 4 == 0) {
                row = new TableRow(mContext);
                row.setLayoutParams(defaultL);
                row.setWeightSum(4);
            }
            BasicPermanentIcon icon = new BasicPermanentIcon(mContext);
            icon.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            icon.setOnClickListener(this);
            icon.setTag(appInfos.get(i).getAction());
            icon.setImgPath(appInfos.get(i).getImageUrl());
            icon.setDescribe(appInfos.get(i).getName());
            TextView view = new TextView(mContext);
            view.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            view.setText(appInfos.get(i).getName());
            row.addView(icon);
            if (i % 4 == 3)
                mainLayout.addView(row);
        }
        if (i % 4 != 0)
            mainLayout.addView(row);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent((String) v.getTag()));
    }
}
