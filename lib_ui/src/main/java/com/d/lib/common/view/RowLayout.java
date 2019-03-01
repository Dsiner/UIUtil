package com.d.lib.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.d.lib.common.R;
import com.d.lib.common.view.toggle.ToggleButton;
import com.d.lib.common.view.toggle.ToggleView;

/**
 * TitleLayout
 * Created by D on 2017/5/3.
 */
public class RowLayout extends RelativeLayout {
    private int mLayout;
    private Drawable mIcon;
    private final String mContent, mHint;
    private int mVisibilityToggle;
    private int mVisibilityGoto;
    private ImageView mIvIcon;
    private TextView mTvNumber;
    private TextView mTvContent, mTvHint;
    private ToggleButton mTbToggle;
    private OnToggleListener mListener;

    public RowLayout(Context context) {
        this(context, null);
    }

    public RowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_pub_RowLayout);
        mLayout = typedArray.getResourceId(R.styleable.lib_pub_RowLayout_lib_pub_rl_layout, R.layout.lib_pub_layout_row);
        mContent = typedArray.getString(R.styleable.lib_pub_RowLayout_lib_pub_rl_text);
        mHint = typedArray.getString(R.styleable.lib_pub_RowLayout_lib_pub_rl_hint);
        mIcon = typedArray.getDrawable(R.styleable.lib_pub_RowLayout_lib_pub_rl_icon);
        mVisibilityToggle = typedArray.getInteger(R.styleable.lib_pub_RowLayout_lib_pub_rl_toggleVisibility, 0);
        mVisibilityGoto = typedArray.getInteger(R.styleable.lib_pub_RowLayout_lib_pub_rl_gotoVisibility, 0);
        typedArray.recycle();
        init(context);
    }

    private void init(Context context) {
        View root = LayoutInflater.from(context).inflate(mLayout, this);
        mIvIcon = (ImageView) root.findViewById(R.id.iv_layout_row_icon);
        mTvNumber = (TextView) root.findViewById(R.id.tv_layout_row_number);
        mTvContent = (TextView) root.findViewById(R.id.tv_layout_row_content);
        mTvHint = (TextView) root.findViewById(R.id.tv_layout_row_hint);
        mTbToggle = (ToggleButton) root.findViewById(R.id.tb_layout_row_toggle);
        ImageView ivGoto = (ImageView) root.findViewById(R.id.iv_layout_row_goto);
        ivGoto.setVisibility(mVisibilityGoto);
        if (mIcon != null) {
            mIvIcon.setImageDrawable(mIcon);
            mIvIcon.setVisibility(VISIBLE);
        } else {
            mIvIcon.setVisibility(GONE);
        }
        mTvContent.setText(mContent != null ? mContent : "");
        mTvHint.setText(mHint != null && mVisibilityGoto == VISIBLE ? mHint : "");
        mTbToggle.setVisibility(mVisibilityToggle);
        mTbToggle.setOnToggleListener(new ToggleView.OnToggleListener() {
            @Override
            public void onToggle(boolean isOpen) {
                if (mListener != null) {
                    mListener.onToggle(RowLayout.this, isOpen);
                }
            }
        });
    }

    /**
     * Set text content
     */
    public void setText(CharSequence text) {
        mTvContent.setText(text);
    }

    /**
     * Set auxiliary text content
     */
    public void setHint(CharSequence text, int visibility) {
        if (mVisibilityGoto != VISIBLE) {
            return;
        }
        mTvHint.setText(text);
    }

    /**
     * Set red dot text content
     */
    public void setNumber(CharSequence text, int visibility) {
        if (mVisibilityGoto != VISIBLE) {
            return;
        }
        mTvNumber.setText(text);
        mTvNumber.setVisibility(visibility);
    }

    /**
     * Toggle button set to open or close
     */
    public void setOpen(boolean open) {
        mTbToggle.setOpen(open);
    }

    /**
     * Toggle button opening or closing status
     */
    public boolean isOpen() {
        return mTbToggle.isOpen();
    }

    public interface OnToggleListener {
        void onToggle(View v, boolean isOpen);
    }

    public void setOnToggleListener(OnToggleListener listener) {
        this.mListener = listener;
    }
}
