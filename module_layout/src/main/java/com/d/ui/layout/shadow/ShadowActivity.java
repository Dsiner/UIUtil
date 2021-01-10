package com.d.ui.layout.shadow;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.d.lib.common.util.ViewHelper;
import com.d.ui.layout.R;

/**
 * Created by D on 2017/11/1.
 */
public class ShadowActivity extends Activity implements View.OnClickListener {

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
        setContentView(R.layout.activity_shadow);
        bindView();
    }

    private void bindView() {
        ViewHelper.setOnClickListener(this, this, R.id.iv_title_left);
    }
}
