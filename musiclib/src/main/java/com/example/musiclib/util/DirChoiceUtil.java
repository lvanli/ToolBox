package com.example.musiclib.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.musiclib.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lizhiguang on 2017/7/13.
 */

public class DirChoiceUtil {
    private static TextView path;
    private static ListView lv;
    private static SimpleListAdapter adapter;

    public static void openChoiceDialog(Context context, final DirResultCallback callback) {

        File file = Environment.getExternalStorageDirectory();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_dir_choice, null);
        lv = (ListView) view.findViewById(R.id.music_dialog_dir_list);
        path = (TextView) view.findViewById(R.id.music_dialog_dir_path);
        path.setText(file.getPath());
        adapter = new SimpleListAdapter(context);

        String list[] = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                LogUtil.d("dir=" + dir + ",name=" + name);
                return new File(dir, name).isDirectory();
            }
        });
        if (list != null) {
            List<String> names = new ArrayList<>(list.length + 1);
            for (String name : list) {
                names.add(name);
            }
            Collections.sort(names);
            adapter.initData(names);
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String dirName = (String) adapter.getItem(position);
                String p;
                if (dirName.equals("..")) {
                    p = new File(path.getText().toString()).getParent();
                } else
                    p = path.getText().toString() + "/" + dirName;
                String list[] = new File(p).list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return new File(dir, name).isDirectory();
                    }
                });
                path.setText(p);
                adapter.clear();
                if (list != null && list.length > 0)
                    for (String name : list) {
                        adapter.add(name);
                    }
                if (!Environment.getExternalStorageDirectory().getPath().equals(p))
                    adapter.add("..");
                adapter.sortAndNotifyDataSetChanged();
            }
        });

        lv.setAdapter(adapter);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.dirResult(path.getText().toString());
                release();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                release();
            }
        });
        builder.setView(view);
        builder.setCancelable(false);
        builder.setTitle("文件夹选择");
        builder.show();
    }

    private static void release() {
        adapter.clear();
        adapter = null;
        path = null;
        lv = null;
    }

    public interface DirResultCallback {
        void dirResult(String path);
    }

    private static class SimpleListAdapter extends BaseAdapter {
        private List<String> items;
        private Context context;

        SimpleListAdapter(Context context) {
            this.context = context;
        }

        public void sortAndNotifyDataSetChanged() {
            if (items != null) {
                Collections.sort(items);
            }
            notifyDataSetChanged();
        }

        public void add(String item) {
            if (items != null)
                items.add(item);
        }

        public void clear() {
            if (items != null && items.size() > 0)
                items.clear();
        }

        public void initData(List<String> data) {
            if (items != null && items.size() > 0)
                items.clear();
            items = data;
        }

        @Override
        public int getCount() {
            return items == null ? 0 : items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, null);
                holder = new Holder();
                holder.tv = (TextView) convertView.findViewById(android.R.id.text1);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.tv.setText(items.get(position));
            return convertView;
        }

        class Holder {
            TextView tv;
        }
    }
}
