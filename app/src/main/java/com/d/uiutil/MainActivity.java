package com.d.uiutil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.d.uiutil.clock.ClockSetActivity;
import com.d.uiutil.heartlayout.HeartActivity;
import com.d.uiutil.lrc.LrcActivity;
import com.d.uiutil.poi.PoiActivity;
import com.d.uiutil.praise.PraiseActivity;
import com.d.uiutil.progress.SettingProgressActivity;
import com.d.uiutil.progress.SnapProgressActivity;
import com.d.uiutil.recordtrigger.RecordTriggerActivity;
import com.d.uiutil.shadow.ShadowActivity;
import com.d.uiutil.sort.SortActivity;
import com.d.uiutil.tab.TabActivity;
import com.d.uiutil.verinor.VerInorActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initClick();
    }

    private void initClick() {
        findViewById(R.id.btn_lrc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LrcActivity.class));
            }
        });
        findViewById(R.id.btn_sort).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SortActivity.class));
            }
        });
        findViewById(R.id.btn_heart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HeartActivity.class));
            }
        });
        findViewById(R.id.btn_settingprogress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingProgressActivity.class));
            }
        });
        findViewById(R.id.btn_praise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PraiseActivity.class));
            }
        });
        findViewById(R.id.btn_tab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TabActivity.class));
            }
        });
        findViewById(R.id.btn_shadow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShadowActivity.class));
            }
        });
        findViewById(R.id.btn_recordtrigger).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RecordTriggerActivity.class));
            }
        });
        findViewById(R.id.btn_verinor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, VerInorActivity.class));
            }
        });
        findViewById(R.id.btn_poi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PoiActivity.class));
            }
        });
        findViewById(R.id.btn_clockset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ClockSetActivity.class));
            }
        });
        findViewById(R.id.btn_snapprogress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SnapProgressActivity.class));
            }
        });
    }
}
