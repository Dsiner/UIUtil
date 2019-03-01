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
    private int mLayout;
    private Drawable mIcon;
    private String mContent, mButton;
    private int mVisibilityExit;
    private int mVisibilityGoto;
    private ImageView mIvIcon, mIvGoto, mIvExit;
    private TextView mTvContent, mTvButton;

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
        mLayout = typedArray.getResourceId(R.styleable.lib_pub_NoticeLayout_lib_pub_noticel_layout, R.layout.lib_pub_layout_notice);
        mIcon = typedArray.getDrawable(R.styleable.lib_pub_NoticeLayout_lib_pub_noticel_icon);
        mContent = typedArray.getString(R.styleable.lib_pub_NoticeLayout_lib_pub_noticel_text);
        mVisibilityGoto = typedArray.getInteger(R.styleable.lib_pub_NoticeLayout_lib_pub_noticel_gotoVisibility, 0);
        mVisibilityExit = typedArray.getInteger(R.styleable.lib_pub_NoticeLayout_lib_pub_noticel_exitVisibility, 0);
        mButton = typedArray.getString(R.styleable.lib_pub_NoticeLayout_lib_pub_noticel_button);
        typedArray.recycle();
    }

    private void init(Context context) {
        final View root = LayoutInflater.from(context).inflate(mLayout, this);
        mIvIcon = (ImageView) root.findViewById(R.id.iv_layout_notice_icon);
        mTvContent = (TextView) root.findViewById(R.id.tv_layout_notice_content);
        mTvButton = (TextView) root.findViewById(R.id.tv_layout_notice_button);
        mIvGoto = (ImageView) root.findViewById(R.id.iv_layout_notice_goto);
        mIvExit = (ImageView) root.findViewById(R.id.iv_layout_notice_exit);
        mIvGoto.setVisibility(mVisibilityGoto);
        mIvExit.setVisibility(mVisibilityExit);
        if (mIcon != null) {
            mIvIcon.setImageDrawable(mIcon);
        }
        mTvContent.setText(!TextUtils.isEmpty(mContent) ? mContent : "");
        mTvButton.setVisibility(!TextUtils.isEmpty(mButton) ? VISIBLE : GONE);
        mTvButton.setText(!TextUtils.isEmpty(mButton) ? mButton : "");
    }

    /**
     * Set text content
     */
    public void setText(CharSequence text) {
        mTvContent.setText(text);
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
