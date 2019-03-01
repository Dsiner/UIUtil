package com.d.lib.common.view.tab;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d.lib.common.R;

/**
 * TabViewGroup
 * Created by D on 2017/8/25.
 */
public class TabViewGroup extends RelativeLayout implements TabView {
    private Context mContext;
    private TextView mTvTitle, mTvNumber;

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
        this.mContext = context;
        View root = LayoutInflater.from(context).inflate(R.layout.lib_pub_view_tab, this);
        mTvTitle = (TextView) root.findViewById(R.id.tv_title);
        mTvNumber = (TextView) root.findViewById(R.id.tv_number);
    }

    @Override
    public void setText(String text) {
        mTvTitle.setText(text);
    }

    @Override
    public void setPadding(int padding) {
        setPadding(padding, 0, padding, 0);
    }

    @Override
    public void setNumber(String text, int visibility) {
        mTvNumber.setText(text);
        mTvNumber.setVisibility(visibility);
    }

    @Override
    public void notifyData(boolean focus) {
        mTvTitle.setTextColor(ContextCompat.getColor(mContext, focus ? R.color.lib_pub_color_main : R.color.lib_pub_color_gray));
    }

    @Override
    public void onScroll(float factor) {

    }
}
