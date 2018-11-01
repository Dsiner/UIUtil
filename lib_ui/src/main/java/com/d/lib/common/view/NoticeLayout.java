package com.d.lib.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d.lib.common.R;

/**
 * NoticeLayout
 * Created by D on 2017/5/3.
 */
public class NoticeLayout extends RelativeLayout {
    private int layout;
    private Drawable icon;
    private String content, button;
    private int visibilityExit;
    private int visibilityGoto;
    private ImageView ivIcon, ivGoto, ivExit;
    private TextView tvContent, tvButton;

    public NoticeLayout(Context context) {
        super(context);
        initAttr(context, null);
        init(context);
    }

    public NoticeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        init(context);
    }

    public NoticeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init(context);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_pub_NoticeLayout);
        layout = typedArray.getResourceId(R.styleable.lib_pub_NoticeLayout_lib_pub_noticel_layout, R.layout.lib_pub_layout_notice);
        icon = typedArray.getDrawable(R.styleable.lib_pub_NoticeLayout_lib_pub_noticel_icon);
        content = typedArray.getString(R.styleable.lib_pub_NoticeLayout_lib_pub_noticel_text);
        visibilityGoto = typedArray.getInteger(R.styleable.lib_pub_NoticeLayout_lib_pub_noticel_gotoVisibility, 0);
        visibilityExit = typedArray.getInteger(R.styleable.lib_pub_NoticeLayout_lib_pub_noticel_exitVisibility, 0);
        button = typedArray.getString(R.styleable.lib_pub_NoticeLayout_lib_pub_noticel_button);
        typedArray.recycle();
    }

    private void init(Context context) {
        final View root = LayoutInflater.from(context).inflate(layout, this);
        ivIcon = (ImageView) root.findViewById(R.id.iv_layout_notice_icon);
        tvContent = (TextView) root.findViewById(R.id.tv_layout_notice_content);
        tvButton = (TextView) root.findViewById(R.id.tv_layout_notice_button);
        ivGoto = (ImageView) root.findViewById(R.id.iv_layout_notice_goto);
        ivExit = (ImageView) root.findViewById(R.id.iv_layout_notice_exit);
        ivGoto.setVisibility(visibilityGoto);
        ivExit.setVisibility(visibilityExit);
        if (icon != null) {
            ivIcon.setImageDrawable(icon);
        }
        tvContent.setText(!TextUtils.isEmpty(content) ? content : "");
        tvButton.setVisibility(!TextUtils.isEmpty(button) ? VISIBLE : GONE);
        tvButton.setText(!TextUtils.isEmpty(button) ? button : "");
    }

    /**
     * Set text content
     */
    public void setText(CharSequence text) {
        tvContent.setText(text);
    }

    public void setVisibility(int resId, int visibility) {
        View v = findViewById(resId);
        if (v != null) {
            v.setVisibility(visibility);
        }
    }

    public void setText(int resId, CharSequence text) {
        View v = findViewById(resId);
        if (v != null && v instanceof TextView) {
            ((TextView) v).setText(text);
        }
    }

    public void setOnClickListener(int resId, final OnClickListener l) {
        View v = findViewById(resId);
        if (v != null) {
            v.setOnClickListener(l);
        }
    }
}
