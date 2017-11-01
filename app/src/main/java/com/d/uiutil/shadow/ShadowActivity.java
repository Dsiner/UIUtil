package com.d.uiutil.shadow;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.d.uiutil.R;

/**
 * Created by D on 2017/11/1.
 */
public class ShadowActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shadow);
    }
}
