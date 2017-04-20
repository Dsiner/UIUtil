package com.d.uiutil.tick;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.d.uiutil.R;

/**
 * CTickView Test
 * Created by D on 2017/4/20.
 */

public class CTickViewTest extends LinearLayout implements View.OnClickListener {
    private CTickView cTickView;

    public CTickViewTest(Context context) {
        this(context, null);
    }

    public CTickViewTest(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CTickViewTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.layout_ctick_view_test, this, true);
        cTickView = (CTickView) view.findViewById(R.id.ctv_tick);
        view.findViewById(R.id.btn_ctv_start).setOnClickListener(this);
        view.findViewById(R.id.btn_ctv_stop).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ctv_start:
                cTickView.start();
                break;
            case R.id.btn_ctv_stop:
                cTickView.stop();
                break;
        }
    }
}
