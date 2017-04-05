package com.lizhiguang.administrator.toolbox.CountDown;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lizhiguang.administrator.toolbox.R;

import java.util.Timer;
import java.util.TimerTask;

public class CountDownActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String DATA_TAG = "second";
    private static final String MYDEBUG = "myDebug";
    private Button countDownButton, countDownReset;
    private TextView countDownText;
    private boolean isBegin;
    private int second;
    private Timer countDownTimer;
    private TimerTask countDownTimerTask;
    private CountDownPushBar countDownHour, countDownMinute, countDownSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);
        countDownButton = (Button) findViewById(R.id.countDownButton);
        countDownReset = (Button) findViewById(R.id.countDownReset);
        countDownText = (TextView) findViewById(R.id.countDownText);
        countDownHour = (CountDownPushBar) findViewById(R.id.countDownHour);
        countDownMinute = (CountDownPushBar) findViewById(R.id.countDownMinute);
        countDownSecond = (CountDownPushBar) findViewById(R.id.countDownSecond);
        isBegin = true;
        countDownButton.setOnClickListener(this);
        countDownReset.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_back:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    Handler countDownHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            changeText(msg.arg1);
            startTime();
        }
    };

    private void changeText(int mSecond) {
        countDownText.setText(mSecond / 36000 + ":" + (mSecond / 600) % 60 + ":" + (mSecond / 10) % 60 + ":" + mSecond % 10);
    }

    private void startTime() {
        if (second == 0) {
            countDownButton.setText("Begin");
            countDownReset.setClickable(true);
            isBegin = !isBegin;
            return;
        }
        countDownTimer = new Timer();
        countDownTimerTask = new TimerTask() {
            @Override
            public void run() {
                // Log.d(MYDEBUG,"run"+second);
                if (second == 0) {
                    return;
                }
                second--;
                Message sendMessage = countDownHandle.obtainMessage();
                sendMessage.arg1 = second;
                countDownHandle.sendMessage(sendMessage);
            }
        };
        countDownTimer.schedule(countDownTimerTask, 100);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.countDownReset:
                second = countDownHour.getNumber() * 36000 + countDownMinute.getNumber() * 600 + countDownSecond.getNumber() * 10;
                //        Log.d(MYDEBUG,"hour="+countDownHour.getNumber()+",minute="+countDownMinute.getNumber()+",second="+countDownSecond.getNumber());
                changeText(second);
                break;
            case R.id.countDownButton:
                if (isBegin) {
                    Log.d(MYDEBUG, "begin");
                    countDownButton.setText("Stop");
                    countDownReset.setClickable(false);
                    startTime();
                } else {
                    Log.d(MYDEBUG, "end");
                    countDownButton.setText("Begin");
                    countDownReset.setClickable(true);
                    countDownTimer.cancel();
                }
                isBegin = !isBegin;
                break;
        }
    }
}
