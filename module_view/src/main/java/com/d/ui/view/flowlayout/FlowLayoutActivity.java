package com.d.ui.view.flowlayout;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.d.lib.common.utils.Util;
import com.d.lib.common.utils.ViewHelper;
import com.d.lib.ui.view.flowlayout.FlowLayout;
import com.d.ui.view.R;

import java.util.ArrayList;
import java.util.List;

public class FlowLayoutActivity extends Activity {
    private FlowLayout flFlow;
    private FlowTagAdapter flowTagAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowlayout);
        bindView();
        init();
    }

    private void bindView() {
        flFlow = ViewHelper.findView(this, R.id.fl_flow);
        ViewHelper.setOnClick(this, R.id.iv_title_left, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        flowTagAdapter = new FlowTagAdapter(this, getDatas(),
                R.layout.adapter_flowlayout_tag);
        flowTagAdapter.setOnClickListener(new FlowTagAdapter.OnClickListener() {
            @Override
            public void onClick(View v, String tag) {
                Util.toast(getApplicationContext(), "Click at: " + tag);
            }
        });
        flFlow.setAdapter(flowTagAdapter);
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
