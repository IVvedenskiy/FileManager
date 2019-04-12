package com.example.iigar.filemanager;

import android.os.Environment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String location = null;
    private String longLocation = null;
    private ArrayList<File> dir;
    private ListAdapter listAdapter;
    private GestureDetectorCompat gestureDetector;
    private ScaleGestureDetector gestureScaleDetector;

    private ArrayList<String> copyList = new ArrayList<>();

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("location", location);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        location = savedInstanceState.getString("location");
    }

    @Override
    protected void onResume() {
        super.onResume();
        showDir();
    }

    @Override
    public void onBackPressed() {
        if (closeDir()){
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListSlide lgd = new ListSlide();
        lgd.setActivity(this);
        gestureDetector = new GestureDetectorCompat(this, lgd);

        ListScale lsgd = new ListScale();
        lsgd.setActivity(this);
        gestureScaleDetector = new ScaleGestureDetector(this, lsgd);

        location = Environment.getExternalStorageDirectory().toString();
    }

    public ArrayList<File> LS(String fn) {
        ArrayList<File> files = new ArrayList<File>();
        File f = new File(fn);
        if (f.canRead()) {
            for (File file : f.listFiles()) {
                files.add(file);
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Ошибка открытия!", Toast.LENGTH_SHORT);
            toast.show();
        }
        return files;
    }

    public boolean closeDir() {
        if (listAdapter.getSelectedItem() != -1) {
            listAdapter.setSelectedItem(-1);
            location = new File(location).getParent();
            return false;
        } else {
            if (!location.equals(Environment.getExternalStorageDirectory().toString())) {
                location = new File(location).getParent();
                showDir();
                return false;
            }
            return true;
        }
    }

    public void openDir(){
        if(listAdapter.getSelectedItem() != -1){
            if (new File(location).isDirectory()) {
              showDir();
            } else {
                Toast toast = Toast.makeText(this,
                        "Это не папка!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public void showDir() {
        TextView tw = (TextView) findViewById(R.id.nav_text);
        tw.setText(location);
        dir = LS(location);

        listAdapter = new ListAdapter(this, dir);
        final ListView listView = (ListView) findViewById(R.id.list_area);
        listView.setAdapter(listAdapter);

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                gestureScaleDetector.onTouchEvent(event);
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                listAdapter.setSelectedItem(position);
                location = dir.get(position).toString();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listAdapter.setSelectedItem(position);
                longLocation = dir.get(position).toString();
                copyList.add(longLocation);
                if(copyList.size()==2){
                    Log.i("xxxxxxeee",copyList.get(0));
                    Log.i("xxxxxxeee",copyList.get(1));
                    if (fileScanner.copyFileOrDirectory(copyList.get(0), copyList.get(1))) {
                        Toast.makeText(MainActivity.this, "Скопировано", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Ошибка копирования", Toast.LENGTH_SHORT).show();
                    }
                    copyList.clear();
                }
                return false;
            }
        });
    }
    private FileScanner fileScanner = new FileScanner();
}