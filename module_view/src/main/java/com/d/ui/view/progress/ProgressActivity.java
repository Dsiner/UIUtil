package com.d.ui.view.progress;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.d.lib.common.util.ViewHelper;
import com.d.lib.ui.view.progress.SettingProgressView;
import com.d.ui.view.R;

/**
 * ProgressActivity
 * Created by D on 2017/11/1.
 */
public class ProgressActivity extends Activity implements View.OnClickListener {
    private SnapProgressLayout snap;

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (R.id.iv_title_left == resId) {
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        bindView();
        initSetting();
    }

    private void bindView() {
        snap = ViewHelper.findViewById(this, R.id.snap);

        ViewHelper.setOnClickListener(this, this, R.id.iv_title_left);
    }

    private void initSetting() {
        final TextView tvLevel = ViewHelper.findViewById(this, R.id.tv_level);
        final SettingProgressView spvLevel = ViewHelper.findViewById(this, R.id.spv_level);
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
        snap.onDestroy();
        super.onDestroy();
    }
}
