package com.d.ui.view.recordtrigger;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.d.lib.common.util.ViewHelper;
import com.d.lib.ui.view.recordtrigger.RecordLightView;
import com.d.lib.ui.view.recordtrigger.RecordTriggerView;
import com.d.ui.view.R;

/**
 * Created by D on 2017/11/1.
 */
public class RecordTriggerActivity extends Activity implements View.OnClickListener {
    private RecordTriggerView rtv_trigger;
    private RecordLightView rlv_light;

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
        setContentView(R.layout.activity_recordtrigger);
        bindView();
        init();
    }

    private void bindView() {
        rtv_trigger = ViewHelper.findViewById(this, R.id.rtv_trigger);
        rlv_light = ViewHelper.findViewById(this, R.id.rlv_light);

        ViewHelper.setOnClickListener(this, this, R.id.iv_title_left);
    }

    private void init() {
        rtv_trigger.setOnTriggerListener(new RecordTriggerView.OnTriggerListener() {
            @Override
            public void onStateChange(int state) {
                rlv_light.setState(state);
            }
        });
    }
}
