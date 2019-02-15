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
    private SnapProgressLayout snapProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        bindView();
        initSetting();
    }

    private void bindView() {
        snapProgress = ViewHelper.findView(this, R.id.snap);
        ViewHelper.setOnClick(this, R.id.iv_title_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    @Override
    protected void onDestroy() {
        snapProgress.onDestroy();
        super.onDestroy();
    }
}
