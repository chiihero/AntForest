package com.chii.antforest.ui.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chii.antforest.R;
import com.chii.antforest.util.AppUtils;
import com.chii.antforest.util.FileUtils;
import com.chii.antforest.util.Log;
import com.chii.antforest.util.RandomUtils;
import com.chii.antforest.util.Statistics;

import de.robv.android.xposed.XposedBridge;

public class MainActivity extends Activity {
    private static String[] strArray;
    TextView tv_statistics;
    Button btn_help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setModuleActive(false);

        tv_statistics = findViewById(R.id.tv_statistics);
        btn_help = findViewById(R.id.btn_help);
        if (strArray == null) {
            strArray = getResources().getStringArray(R.array.sentences);
        }
        if (strArray != null) {
            btn_help.setText(strArray[RandomUtils.nextInt(0, strArray.length)]);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_statistics.setText(Statistics.getText());
    }

    public void onClick(View v) {
        String data = "file://";
        switch (v.getId()) {
            case R.id.btn_forest_log:
                data += FileUtils.getForestLogFile().getAbsolutePath();
                break;

            case R.id.btn_farm_log:
                data += FileUtils.getFarmLogFile().getAbsolutePath();
                break;

            case R.id.btn_other_log:
                data += FileUtils.getOtherLogFile().getAbsolutePath();
                break;

            case R.id.btn_help:
                data = "https://github.com/pansong291/XQuickEnergy/wiki";
                break;
        }
        Intent it = new Intent(this, HtmlViewerActivity.class);
        it.setData(Uri.parse(data));
        startActivity(it);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int state = getPackageManager()
                .getComponentEnabledSetting(new ComponentName(this, getClass().getCanonicalName() + "Alias"));
        menu.add(0, 1, 0, "Hide the application icon")
                .setCheckable(true)
                .setChecked(state > PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
        menu.add(0, 2, 0, "Export the statistic file");
        menu.add(0, 3, 0, "Import the statistic file");
        menu.add(0, 4, 0, "Settings");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                int state = item.isChecked() ? PackageManager.COMPONENT_ENABLED_STATE_DEFAULT : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
                getPackageManager()
                        .setComponentEnabledSetting(new ComponentName(this, getClass().getCanonicalName() + "Alias"), state, PackageManager.DONT_KILL_APP);
                item.setChecked(!item.isChecked());
                break;

            case 2:
                if (FileUtils.copyTo(FileUtils.getStatisticsFile(), FileUtils.getExportedStatisticsFile())) {
                    Toast.makeText(this, "Export success", 0).show();
                }
                break;

            case 3:
                if (FileUtils.copyTo(FileUtils.getExportedStatisticsFile(), FileUtils.getStatisticsFile())) {
                    tv_statistics.setText(Statistics.getText());
                    Toast.makeText(this, "Import success", 0).show();
                }
                break;

            case 4:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setModuleActive(boolean moduleActive) {
        moduleActive = moduleActive || AppUtils.isExpModuleActive(this);
        Log.i("HookLogic","HookLogic >> current package:" + moduleActive);
        TextView tvUnactive = findViewById(R.id.tv_unactive);
        tvUnactive.setVisibility(moduleActive ? View.GONE : View.VISIBLE);
    }
    

}
