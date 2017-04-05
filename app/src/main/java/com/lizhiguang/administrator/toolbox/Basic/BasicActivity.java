package com.lizhiguang.administrator.toolbox.Basic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.lizhiguang.administrator.toolbox.R;

import java.util.ArrayList;
import java.util.List;

public class BasicActivity extends AppCompatActivity {

    public static final String MYDEBUG = "myDebug";
    private BasicHorizontalListView bottomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.basicFragment);
//        fragment.setTargetFragment(new BasicActivityFragment(),0);
        bottomView = (BasicHorizontalListView) findViewById(R.id.bottomView);
        List<String> list = new ArrayList<>();
        list.add("1231");
        list.add("1232");
        list.add("1233");
        list.add("1234");
        list.add("1235");
        list.add("1236");
        list.add("1237");
        BasicHorizontalListViewAdapter bottomAdapter = new BasicHorizontalListViewAdapter(this);
        bottomAdapter.addItem(R.drawable.img1, "1");
        bottomAdapter.addItem(R.drawable.img1, "2");
        bottomAdapter.addItem(R.drawable.img1, "3");
        bottomAdapter.addItem(R.drawable.img1, "4");
        bottomAdapter.addItem(R.drawable.img1, "5");
        bottomAdapter.addItem(R.drawable.img1, "6");
        //       ArrayAdapter bottomAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,list);
        bottomView.setAdapter(bottomAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_basic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
