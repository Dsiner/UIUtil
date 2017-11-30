package com.d.uiutil.settingprogress;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.d.lib.ui.settingprogress.SettingProgressView;
import com.d.uiutil.R;

/**
 * SettingProgress
 * Created by D on 2017/11/1.
 */
public class SettingProgressActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingprogress);
        final TextView tvLevel = (TextView) findViewById(R.id.tv_level);
        SettingProgressView spvLevel = (SettingProgressView) findViewById(R.id.spv_level);
        spvLevel.setCurPosition(0);
        spvLevel.setOnProgressChangeListener(new SettingProgressView.OnProgressChangeListener() {
            @Override
            public void onProgressChange(int position) {
                tvLevel.setText("L: " + position);
            }

            @Override
            public void onClick(int position) {
                tvLevel.setText("L: " + position);
            }
        });
    }
}
