package com.d.ui.layout.arcmenu;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.d.lib.common.util.ViewHelper;
import com.d.ui.layout.R;

/**
 * Created by D on 2018/11/11.
 */
public class ArcMenuActivity extends Activity implements View.OnClickListener {

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
        setContentView(R.layout.activity_arcmenu);
        bindView();
    }

    private void bindView() {
        ViewHelper.setOnClickListener(this, this, R.id.iv_title_left);
    }
}
