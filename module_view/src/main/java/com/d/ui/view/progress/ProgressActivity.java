package com.d.ui.view.progress;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.d.lib.common.utils.ViewHelper;
import com.d.lib.ui.view.progress.SettingProgressView;
import com.d.ui.view.R;

/**
 * ProgressActivity
 * Created by D on 2017/11/1.
 */
public class ProgressActivity extends Activity {
    private SnapProgressTest snapProgressTest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        initBack();
        initSetting();
        initSnap();
    }

    private void initSetting() {
        final TextView tvLevel = (TextView) findViewById(R.id.tv_level);
        SettingProgressView spvLevel = (SettingProgressView) findViewById(R.id.spv_level);
        spvLevel.setCurPosition(0);
        spvLevel.setOnProgressChangeListener(new SettingProgressView.OnProgressChangeListener() {
            @Override
            public void onProgressChange(int position) {
                tvLevel.setText("Level: " + position);
            }

            @Override
            public void onClick(int position) {
                tvLevel.setText("Level: " + position);
            }
        });
    }

    private void initSnap() {
        snapProgressTest = ViewHelper.findView(this, R.id.snap);
    }

    private void initBack() {
        ViewHelper.setOnClick(this, R.id.iv_title_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        snapProgressTest.onDestroy();
        super.onDestroy();
    }
}
