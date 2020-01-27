package com.chii.antforest.view.activity;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {
    @BindView(R.id.tv_statistics)
    TextView tvStatistics;
    @BindView(R.id.btn_help)
    Button btnHelp;
    @BindView(R.id.tv_unactive)
    TextView tvUnactive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setModuleActive(false);
        //全局context
        ContextUtil.context = this.getApplicationContext();

        String[] strHelp = getResources().getStringArray(R.array.sentences);
        btnHelp.setText(strHelp[RandomUtils.nextInt(0, strHelp.length)]);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvStatistics.setText(Statistics.getText());
    }

    @OnClick({R.id.btn_forest_log, R.id.btn_farm_log, R.id.btn_other_log, R.id.btn_help})
    public void onViewClicked(View view) {
        String data = "";
        switch (view.getId()) {
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
                Uri contentUrl = Uri.parse(data);
                intent.setData(contentUrl);
                //防止没有浏览器导致崩溃
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                return;
            default:
                return;
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
                getPackageManager().setComponentEnabledSetting(new ComponentName(this,
                                getClass().getCanonicalName() + "Alias"), state,
                        PackageManager.DONT_KILL_APP);
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
        Log.i("HookLogic", "HookLogic >> current package:" + moduleActive);
        if (moduleActive || AppUtils.isExpModuleActive(this)) {
            tvUnactive.setVisibility(View.GONE);
        }
    }


}
