package com.d.ui.view.recordtrigger;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.d.lib.common.utils.ViewHelper;
import com.d.lib.ui.view.recordtrigger.RecordLightView;
import com.d.lib.ui.view.recordtrigger.RecordTriggerView;
import com.d.ui.view.R;

/**
 * Created by D on 2017/11/1.
 */
public class RecordTriggerActivity extends Activity {
    private RecordTriggerView rtvTrigger;
    private RecordLightView rlvLight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordtrigger);
        initBack();
        bindView();
        rtvTrigger.setOnTriggerListener(new RecordTriggerView.OnTriggerListener() {
            @Override
            public void onStateChange(int state) {
                rlvLight.setState(state);
            }
        });
    }

    private void bindView() {
        rtvTrigger = (RecordTriggerView) findViewById(R.id.rtv_trigger);
        rlvLight = (RecordLightView) findViewById(R.id.rlv_light);
    }

    private void initBack() {
        ViewHelper.setOnClick(this, R.id.iv_title_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
