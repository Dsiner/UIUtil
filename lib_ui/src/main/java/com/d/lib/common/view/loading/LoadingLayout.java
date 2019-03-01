package com.d.lib.common.view.loading;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.d.lib.common.R;

/**
 * LoadingLayout
 * Created by D on 2017/5/2.
 */
public class LoadingLayout extends LinearLayout {

    protected LoadingView mLdvLoading;

    public LoadingLayout(Context context) {
        super(context);
        init(context);
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public LoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context) {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        View root = LayoutInflater.from(context).inflate(R.layout.lib_pub_layout_loading, this);
        mLdvLoading = (LoadingView) root.findViewById(R.id.ldv_loading);
    }

    @Override
    public void setVisibility(int visibility) {
        switch (visibility) {
            case VISIBLE:
                mLdvLoading.restart();
                break;
            case GONE:
            case INVISIBLE:
                mLdvLoading.stop();
                break;
        }
        super.setVisibility(visibility);
    }
}