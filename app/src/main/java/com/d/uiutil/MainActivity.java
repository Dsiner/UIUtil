package com.d.uiutil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.d.uiutil.heartlayout.HeartActivity;
import com.d.uiutil.lrc.LrcActivity;
import com.d.uiutil.praise.PraiseActivity;
import com.d.uiutil.shadow.ShadowActivity;
import com.d.uiutil.sort.SortActivity;
import com.d.uiutil.tab.TabActivity;

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
    }
}
