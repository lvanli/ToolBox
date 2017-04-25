package com.lizhiguang.administrator.toolbox.Launcher;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lizhiguang.administrator.toolbox.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class LauncherActivity extends AppCompatActivity {
    private static final int ONE_PAGE_APP = 16;
    ViewPager mainPager;
    private static final String TAG = "myDebug";
    private List<AppInfo> appInfos;
    private Vector<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        bindWidget();
        init();
    }

    private void bindWidget() {
        mainPager = (ViewPager) findViewById(R.id.launcherPager);
    }

    private void init() {
        if (getInformation() == 0) {
            int maxPager = (appInfos.size() + ONE_PAGE_APP - 1) / ONE_PAGE_APP;
            List<AppInfo> subInfos = null;
            Bundle bundle;
            ArrayList list;
            Log.d(TAG, "init: maxPager=" + maxPager + ",appinfos=" + appInfos.size());
            fragments = new Vector<>(maxPager);
            for (int i = 0; i < maxPager; i++) {
                if (i != maxPager - 1)
                    subInfos = appInfos.subList(ONE_PAGE_APP * i, ONE_PAGE_APP * (i + 1));
                else
                    subInfos = appInfos.subList(ONE_PAGE_APP * i, appInfos.size());
                bundle = new Bundle();
                list = new ArrayList();
                Log.d(TAG, "init: subinfos=" + subInfos.size());
                list.add(subInfos);
                bundle.putParcelableArrayList("list", list);
                LauncherActivityFragment fragment = new LauncherActivityFragment();
                fragment.setArguments(bundle);
                fragments.add(fragment);
            }
            MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);
            mainPager.addOnPageChangeListener(new ViewPagerIndicator(this, mainPager, (LinearLayout) findViewById(R.id.launcherPagerIndicator), adapter.getCount()));
            mainPager.setAdapter(adapter);
            return;
        }
        Toast.makeText(this, "初始化失败", Toast.LENGTH_SHORT).show();
    }

    private int getInformation() {
        try {
            DocumentBuilderFactory readBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder readBuilder = readBuilderFactory.newDocumentBuilder();
            Log.d(TAG, "null?" + getAssets().open("data.xml"));
            Document readDocument = readBuilder.parse(getAssets().open("data.xml"));
            Element readElement = readDocument.getDocumentElement();
            Log.d(TAG, "null??" + readElement.getNodeName());
            NodeList readList = readElement.getElementsByTagName("information");
            Element myApplication;
            Log.d(TAG, "readL=" + readList.getLength());
            appInfos = new ArrayList<>(readList.getLength());
            for (int i = 0; i < readList.getLength(); i++) {
                AppInfo appInfo = new AppInfo();
                myApplication = (Element) readList.item(i);
                appInfo.setName(myApplication.getElementsByTagName("name").item(0).getTextContent());
                appInfo.setAction(myApplication.getElementsByTagName("action").item(0).getTextContent());
                appInfo.setImageUrl(myApplication.getElementsByTagName("image").item(0).getTextContent());
                appInfos.add(appInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }
}
