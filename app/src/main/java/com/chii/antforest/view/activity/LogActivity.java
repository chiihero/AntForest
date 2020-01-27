package com.chii.antforest.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.chii.antforest.R;
import com.chii.antforest.view.adapter.ListLogViewAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogActivity extends Activity {

    @BindView(R.id.listView_log)
    ListView listViewLog;

    public static String TAG = "LogActivity";
    private List<String> logList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String filePath = intent.getStringExtra("file");
        Log.i(TAG, "onCreate: " + filePath);

        initDate(filePath);
        initList();
    }

    private void initDate(String filePath) {
        try {
            File file = new File(filePath);
            //文件存在
            if (file.isFile() && file.exists()) {
                //打开文件输入流
                InputStreamReader isr = new InputStreamReader(new FileInputStream(filePath));
                //读取文件数据流
                BufferedReader br = new BufferedReader(isr);
                String strLine = null;
                //通过readline按行读取
                while ((strLine = br.readLine()) != null) {
                    //strLine就是一行的内容
                    logList.add(strLine);
                }
                br.close();
                isr.close();
            }
        } catch (IOException e) {
        }
    }

    private void initList() {
        ListLogViewAdapter mAdapter = new ListLogViewAdapter(this, logList);
        listViewLog.setAdapter(mAdapter);
    }
}
