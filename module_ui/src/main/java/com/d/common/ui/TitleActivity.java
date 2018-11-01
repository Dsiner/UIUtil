package com.d.common.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.d.lib.common.utils.ViewHelper;

public class TitleActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_ui_activity_title);
        initClick();
    }

    private void initClick() {
        ViewHelper.setOnClick((View) ViewHelper.findView(this, R.id.tl_title), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, R.id.iv_title_left);
    }
}
