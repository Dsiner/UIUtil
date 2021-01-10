package com.d.ui.view.flowlayout;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.d.lib.common.util.ToastUtils;
import com.d.lib.common.util.ViewHelper;
import com.d.lib.ui.view.flowlayout.FlowLayout;
import com.d.ui.view.R;

import java.util.ArrayList;
import java.util.List;

public class FlowLayoutActivity extends Activity implements View.OnClickListener {
    private FlowLayout fl_flow;

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
        setContentView(R.layout.activity_flowlayout);
        bindView();
        init();
    }

    private void bindView() {
        fl_flow = ViewHelper.findViewById(this, R.id.fl_flow);

        ViewHelper.setOnClickListener(this, this, R.id.iv_title_left);
    }

    private void init() {
        FlowTagAdapter flowTagAdapter = new FlowTagAdapter(this, getDatas(),
                R.layout.adapter_flowlayout_tag);
        flowTagAdapter.setOnClickListener(new FlowTagAdapter.OnClickListener() {
            @Override
            public void onClick(View v, String tag) {
                ToastUtils.toast(getApplicationContext(), "Click at: " + tag);
            }
        });
        fl_flow.setAdapter(flowTagAdapter);
    }

    @NonNull
    private List<FlowBean> getDatas() {
        List<FlowBean> hots = new ArrayList<>();
        hots.add(new FlowBean("Dumb Blonde"));
        hots.add(new FlowBean("NO WHY"));
        hots.add(new FlowBean("I don't wanna see u anymore"));
        hots.add(new FlowBean("Lemon"));
        hots.add(new FlowBean("Creep"));
        hots.add(new FlowBean("Monsters"));
        hots.add(new FlowBean("Way Back Home"));
        hots.add(new FlowBean("Wolves"));
        hots.add(new FlowBean("Nevada"));
        hots.add(new FlowBean("Shape of You"));
        hots.add(new FlowBean("Counting Sheep"));
        return hots;
    }
}
