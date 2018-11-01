package com.d.common.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.d.lib.common.utils.ViewHelper;
import com.d.lib.common.view.RowLayout;

public class RowActivity extends Activity implements View.OnClickListener {
    private RowLayout[] rlRows = new RowLayout[6];

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (resId == R.id.iv_title_left) {
            finish();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_ui_activity_row);
        initView();
    }

    private void initView() {
        rlRows[0] = ViewHelper.findView(this, R.id.rl_row0);
        rlRows[1] = ViewHelper.findView(this, R.id.rl_row1);
        rlRows[2] = ViewHelper.findView(this, R.id.rl_row2);
        rlRows[3] = ViewHelper.findView(this, R.id.rl_row3);
        rlRows[4] = ViewHelper.findView(this, R.id.rl_row4);
        rlRows[5] = ViewHelper.findView(this, R.id.rl_row5);

        rlRows[1].setNumber("99", View.VISIBLE);

        rlRows[3].setOpen(false);
        rlRows[4].setOpen(true);

        ViewHelper.setOnClick(this, this, R.id.iv_title_left);

        ViewHelper.setOnClick(this, this, R.id.rl_row0);
        ViewHelper.setOnClick(this, this, R.id.rl_row1);
        ViewHelper.setOnClick(this, this, R.id.rl_row2);
        ViewHelper.setOnClick(this, this, R.id.rl_row3);
        ViewHelper.setOnClick(this, this, R.id.rl_row4);
        ViewHelper.setOnClick(this, this, R.id.rl_row5);
    }
}
