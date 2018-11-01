package com.d.common.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.d.lib.common.utils.ViewHelper;
import com.d.lib.common.view.DSLayout;

public class DSActivity extends Activity implements View.OnClickListener {
    private final String urlGif = "https://raw.githubusercontent.com/Dsiner/Common/master/module_ui/src/main/res/drawable-xxhdpi/";
    private final String urlPng = "https://raw.githubusercontent.com/Dsiner/Common/master/lib/src/main/res/drawable-xxhdpi/";

    private DSLayout dslDs;

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (resId == R.id.iv_title_left) {
            finish();
        } else if (resId == R.id.btn_dsl) {
            //Retry Button
            dslDs.setState(DSLayout.STATE_LOADING);

        } else if (resId == R.id.btn_0_0) {
            dslDs.setState(DSLayout.STATE_LOADING);
        } else if (resId == R.id.btn_0_1) {
            dslDs.setState(DSLayout.STATE_EMPTY);
        } else if (resId == R.id.btn_0_2) {
            dslDs.setState(DSLayout.STATE_NET_ERROR);

        } else if (resId == R.id.btn_1_0) {
            dslDs.setState(DSLayout.STATE_EMPTY)
                    .gif(R.drawable.module_ui_gif_loading)
                    .desc(getResources().getString(R.string.module_ui_ds_loading))
                    .button("", View.GONE);
        } else if (resId == R.id.btn_1_1) {
            dslDs.setState(DSLayout.STATE_EMPTY)
                    .gif(R.drawable.module_ui_gif_no_data)
                    .desc(getResources().getString(R.string.module_ui_ds_no_data))
                    .button("", View.GONE);
        } else if (resId == R.id.btn_1_2) {
            dslDs.setState(DSLayout.STATE_EMPTY)
                    .gif(R.drawable.module_ui_gif_net_error)
                    .desc(getResources().getString(R.string.module_ui_ds_net_err))
                    .button(getResources().getString(R.string.module_ui_ds_retry), View.VISIBLE);

        } else if (resId == R.id.btn_2_0) {
            dslDs.setState(DSLayout.STATE_EMPTY)
                    .gif(urlGif + "module_ui_gif_search.gif")
                    .desc(getResources().getString(R.string.module_ui_ds_scan));
        } else if (resId == R.id.btn_2_1) {
            dslDs.setState(DSLayout.STATE_EMPTY)
                    .icon(urlPng + "lib_pub_ic_no_data.png");
        } else if (resId == R.id.btn_2_2) {
            dslDs.setState(DSLayout.STATE_NET_ERROR)
                    .icon(urlPng + "lib_pub_ic_network_err.png");
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_ui_activity_ds);
        initView();
    }

    private void initView() {
        dslDs = ViewHelper.findView(this, R.id.dsl_ds);

        ViewHelper.setOnClick(this, this, R.id.iv_title_left);
        ViewHelper.setOnClick(this, this, R.id.btn_dsl);

        ViewHelper.setOnClick(this, this, R.id.btn_0_0);
        ViewHelper.setOnClick(this, this, R.id.btn_0_1);
        ViewHelper.setOnClick(this, this, R.id.btn_0_2);

        ViewHelper.setOnClick(this, this, R.id.btn_1_0);
        ViewHelper.setOnClick(this, this, R.id.btn_1_1);
        ViewHelper.setOnClick(this, this, R.id.btn_1_2);

        ViewHelper.setOnClick(this, this, R.id.btn_2_0);
        ViewHelper.setOnClick(this, this, R.id.btn_2_1);
        ViewHelper.setOnClick(this, this, R.id.btn_2_2);
    }
}
