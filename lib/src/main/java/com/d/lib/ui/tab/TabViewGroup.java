package com.d.lib.ui.tab;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d.lib.ui.R;

/**
 * TabViewGroup
 * Created by D on 2017/8/25.
 */
public class TabViewGroup extends RelativeLayout implements TabView {
    private TextView tvTitle;

    public TabViewGroup(Context context) {
        this(context, null);
    }

    public TabViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.view_tab, this);
        tvTitle = (TextView) root.findViewById(R.id.tv_title);
    }

    @Override
    public void setText(String text) {
        tvTitle.setText(text);
    }

    @Override
    public void notifyData(boolean focus) {
        tvTitle.setTextColor(getResources().getColor(focus ? R.color.colorA : R.color.colorT));
    }

    @Override
    public void onScroll(float factor) {

    }
}
