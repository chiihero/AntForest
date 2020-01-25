package com.chii.antforest.ui.activity;

import android.app.Activity;
import android.content.ComponentName;
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
import com.chii.antforest.util.ContextUtil;
import com.chii.antforest.util.FileUtils;
import com.chii.antforest.util.Log;
import com.chii.antforest.util.RandomUtils;
import com.chii.antforest.util.Statistics;

public class MainActivity extends Activity {
    private static String[] strArray;
    TextView tvStatistics;
    Button btnHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setModuleActive(false);
        //全局context
        ContextUtil.context = this.getApplicationContext();

        tvStatistics = findViewById(R.id.tv_statistics);
        btnHelp = findViewById(R.id.btn_help);
        if (strArray == null) {
            strArray = getResources().getStringArray(R.array.sentences);
        }
        btnHelp.setText(strArray[RandomUtils.nextInt(0, strArray.length)]);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvStatistics.setText(Statistics.getText());
    }

    public void onClick(View v) {
        String data = "file://";
        switch (v.getId()) {
            case R.id.btn_forest_log:
                data = FileUtils.getForestLogFile().getAbsolutePath();
                break;

            case R.id.btn_farm_log:
                data = FileUtils.getFarmLogFile().getAbsolutePath();
                break;

            case R.id.btn_other_log:
                data = FileUtils.getOtherLogFile().getAbsolutePath();
                break;

            case R.id.btn_help:
                data = "https://github.com/chiihero/AntForest";
                //从其他浏览器打开
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri content_url = Uri.parse(data);
                intent.setData(content_url);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                return;
            default:
        }
        Intent intent = new Intent();
        intent.putExtra("file", data);
        intent.setClass(MainActivity.this, LogActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int state = getPackageManager()
                .getComponentEnabledSetting(new ComponentName(this,
                        getClass().getCanonicalName() + "Alias"));
//        menu.add(0, 1, 0, "Hide the application icon")
//                .setCheckable(true)
//                .setChecked(state > PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
//        menu.add(0, 2, 0, "Export the statistic file");
//        menu.add(0, 3, 0, "Import the statistic file");
//        menu.add(0, 4, 0, "Settings");
//        return super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_hide).setChecked(state > PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_hide:
                int state = item.isChecked() ? PackageManager.COMPONENT_ENABLED_STATE_DEFAULT :
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
                getPackageManager().setComponentEnabledSetting(new ComponentName(this,getClass().getCanonicalName() + "Alias"), state,PackageManager.DONT_KILL_APP);
                item.setChecked(!item.isChecked());
                break;

            case R.id.action_export:
                if (FileUtils.copyTo(FileUtils.getStatisticsFile(),
                        FileUtils.getExportedStatisticsFile())) {
                    Toast.makeText(this, "Export success", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.action_import:
                if (FileUtils.copyTo(FileUtils.getExportedStatisticsFile(),
                        FileUtils.getStatisticsFile())) {
                    tvStatistics.setText(Statistics.getText());
                    Toast.makeText(this, "Import success", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void setModuleActive(boolean moduleActive) {
        moduleActive = moduleActive || AppUtils.isExpModuleActive(this);
        Log.i("HookLogic", "HookLogic >> current package:" + moduleActive);
        TextView tvUnactive = findViewById(R.id.tv_unactive);
        tvUnactive.setVisibility(moduleActive ? View.GONE : View.VISIBLE);
    }


}
