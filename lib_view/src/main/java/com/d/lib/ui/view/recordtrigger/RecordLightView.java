package com.d.lib.ui.view.recordtrigger;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.d.lib.ui.view.R;

/**
 * 指示器
 * Created by D on 2017/11/1.
 */
public class RecordLightView extends LinearLayout {
    private final String toastUp = "手指上滑，取消发送";
    private final String toastCancel = "松开手指，取消发送";
    private ImageView ivFlag;
    private TextView tvToast;

    public RecordLightView(Context context) {
        super(context);
        init(context);
    }

    public RecordLightView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public RecordLightView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.lib_ui_view_rtv_view, this);
        ivFlag = (ImageView) root.findViewById(R.id.iv_flag);
        tvToast = (TextView) root.findViewById(R.id.tv_toast);
    }

    public void setState(int state) {
        switch (state) {
            case RecordTriggerView.STATE_VALID:
                setVisibility(VISIBLE);
                ivFlag.setImageResource(R.drawable.lib_ui_view_rtv_ic_voice);
                tvToast.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lib_pub_color_trans));
                tvToast.setText(toastUp);
                break;
            case RecordTriggerView.STATE_INVALID:
                setVisibility(VISIBLE);
                ivFlag.setImageResource(R.drawable.lib_ui_view_rtv_ic_back);
                tvToast.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.lib_ui_view_rtv_corner_bg));
                tvToast.setText(toastCancel);
                break;
            case RecordTriggerView.STATE_CANCLE:
                setVisibility(GONE);
                break;
            case RecordTriggerView.STATE_SUBMIT:
                setVisibility(GONE);
                break;
        }
    }
}
