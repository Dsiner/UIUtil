package com.d.lib.ui.view.tab;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d.lib.ui.view.R;

/**
 * TabViewGroup
 * Created by D on 2017/8/25.
 */
public class TabViewGroup extends RelativeLayout implements TabView {
    private Context context;
    private TextView tvTitle, tvNumber;

    public TabViewGroup(Context context) {
        super(context);
        init(context);
    }

    public TabViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TabViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View root = LayoutInflater.from(context).inflate(R.layout.lib_ui_view_stab_view_tab, this);
        tvTitle = (TextView) root.findViewById(R.id.tv_title);
        tvNumber = (TextView) root.findViewById(R.id.tv_number);
    }

    @Override
    public void setText(String text) {
        tvTitle.setText(text);
    }

    @Override
    public void setPadding(int padding) {
        setPadding(padding, 0, padding, 0);
    }

    @Override
    public void setNumber(String text, int visibility) {
        tvNumber.setText(text);
        tvNumber.setVisibility(visibility);
    }

    @Override
    public void notifyData(boolean focus) {
        tvTitle.setTextColor(ContextCompat.getColor(context, focus ? R.color.lib_ui_common_color_accent : R.color.lib_ui_common_color_text));
    }

    @Override
    public void onScroll(float factor) {

    }
}
