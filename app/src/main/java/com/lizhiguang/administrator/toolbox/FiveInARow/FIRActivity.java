package com.lizhiguang.administrator.toolbox.FiveInARow;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;

import com.lizhiguang.administrator.toolbox.R;

import java.util.ArrayList;


public class FIRActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "myDebug";
    FIRMapView mapView;
    Button hthButton, htmButton;
    LinearLayout menuLayout;
    FIRManager mFirManager;
    int mWhoFirst;
    int mMapSize;
    MenuItem mFirstHand, mMapSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fir);
        bindWidget();
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fir, menu);
        mFirstHand = menu.findItem(R.id.action_first_hand);
        mMapSetting = menu.findItem(R.id.action_map_size);
        return true;
    }

    public void savePreferences() {
        SharedPreferences save = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = save.edit();
        editor.putInt("mWhoFirst", mWhoFirst);
        editor.putInt("mMapSize", mMapSize);
        editor.commit();
    }

    public void setFirst() {
        mFirManager.setWhoFirst(mWhoFirst);
        mapView.setWho(mWhoFirst);
    }

    public void setMapSize() {
        mapView.setMaxLine(mMapSize);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_first_hand:
                AlertDialog dialog = new AlertDialog.Builder(this).create();
                View view = LayoutInflater.from(this).inflate(R.layout.fir_first_hand_choose, null);
                final RadioButton computerFirst = (RadioButton) view.findViewById(R.id.fir_menu_computer_first);
                final RadioButton playerFirst = (RadioButton) view.findViewById(R.id.fir_menu_player_first);
                if (mWhoFirst == FIRManager.WRITE) {
                    computerFirst.setChecked(true);
                }
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (computerFirst.isChecked()) {
                            mWhoFirst = FIRManager.WRITE;
                            setFirst();
                            savePreferences();
                        } else if (playerFirst.isChecked()) {
                            mWhoFirst = FIRManager.BLACK;
                            setFirst();
                            savePreferences();
                        }
                    }
                });
                dialog.setView(view);
                dialog.show();
                break;
            case R.id.action_map_size:
                AlertDialog sizeDialog = new AlertDialog.Builder(this).create();
                View sizeView = LayoutInflater.from(this).inflate(R.layout.fir_map_size_choose, null);
                final ListView sizeList = (ListView) sizeView.findViewById(R.id.fir_menu_map_size_list);
                ArrayList<Integer> list = new ArrayList<>(6);
                for (int i = 10; i < 16; i++) {
                    list.add(i);
                }
                ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, list);
                sizeList.setAdapter(adapter);
                sizeDialog.setView(sizeView);
                sizeDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMapSize = sizeList.getCheckedItemPosition() + 10;
                        Log.d(TAG, "mapSize=" + mMapSize);
                        setMapSize();
                        savePreferences();
                    }
                });
                sizeDialog.show();
                break;
            case R.id.action_back:
                if (mapView.getVisibility() == View.VISIBLE) {
                    stopGame();
                } else {
                    finish();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    void bindWidget() {
        mapView = (FIRMapView) findViewById(R.id.fir_map_view);
        hthButton = (Button) findViewById(R.id.fir_menu_hth);
        htmButton = (Button) findViewById(R.id.fir_menu_htm);
        menuLayout = (LinearLayout) findViewById(R.id.fir_menu_layout);
    }

    void init() {
        hthButton.setOnClickListener(this);
        htmButton.setOnClickListener(this);
        mapView.setActivityHandler(myHandle);
        mFirManager = new FIRManager();
        SharedPreferences get = getPreferences(MODE_PRIVATE);
        mWhoFirst = get.getInt("mWhoFirst", FIRManager.BLACK);
        mMapSize = get.getInt("mMapSize", 15);
        setFirst();
        setMapSize();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fir_menu_hth:
                startGame(1);
                break;
            case R.id.fir_menu_htm:
                startGame(0);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mapView.getVisibility() == View.VISIBLE) {
                stopGame();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void startGame(int type) {
        mapView.setManager(mFirManager);
        mFirManager.setGameMode(type);
        mapView.setVisibility(View.VISIBLE);
        menuLayout.setVisibility(View.GONE);
        mMapSetting.setVisible(false);
        mFirstHand.setVisible(false);
    }

    public void stopGame() {
        mapView.setVisibility(View.GONE);
        menuLayout.setVisibility(View.VISIBLE);
        mMapSetting.setVisible(true);
        mFirstHand.setVisible(true);
        setFirst();
    }

    public Handler myHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0)
                stopGame();
        }
    };
}
