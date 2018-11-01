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
    private int layout;
    private Drawable icon;
    private final String content, hint;
    private int visibilityToggle;
    private int visibilityGoto;
    private ImageView ivIcon;
    private TextView tvNumber;
    private TextView tvContent, tvHint;
    private ToggleButton tbToggle;
    private OnToggleListener listener;

    public RowLayout(Context context) {
        this(context, null);
    }

    public RowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_pub_RowLayout);
        layout = typedArray.getResourceId(R.styleable.lib_pub_RowLayout_lib_pub_rl_layout, R.layout.lib_pub_layout_row);
        content = typedArray.getString(R.styleable.lib_pub_RowLayout_lib_pub_rl_text);
        hint = typedArray.getString(R.styleable.lib_pub_RowLayout_lib_pub_rl_hint);
        icon = typedArray.getDrawable(R.styleable.lib_pub_RowLayout_lib_pub_rl_icon);
        visibilityToggle = typedArray.getInteger(R.styleable.lib_pub_RowLayout_lib_pub_rl_toggleVisibility, 0);
        visibilityGoto = typedArray.getInteger(R.styleable.lib_pub_RowLayout_lib_pub_rl_gotoVisibility, 0);
        typedArray.recycle();
        init(context);
    }

    private void init(Context context) {
        View root = LayoutInflater.from(context).inflate(layout, this);
        ivIcon = (ImageView) root.findViewById(R.id.iv_layout_row_icon);
        tvNumber = (TextView) root.findViewById(R.id.tv_layout_row_number);
        tvContent = (TextView) root.findViewById(R.id.tv_layout_row_content);
        tvHint = (TextView) root.findViewById(R.id.tv_layout_row_hint);
        tbToggle = (ToggleButton) root.findViewById(R.id.tb_layout_row_toggle);
        ImageView ivGoto = (ImageView) root.findViewById(R.id.iv_layout_row_goto);
        ivGoto.setVisibility(visibilityGoto);
        if (icon != null) {
            ivIcon.setImageDrawable(icon);
            ivIcon.setVisibility(VISIBLE);
        } else {
            ivIcon.setVisibility(GONE);
        }
        tvContent.setText(content != null ? content : "");
        tvHint.setText(hint != null && visibilityGoto == VISIBLE ? hint : "");
        tbToggle.setVisibility(visibilityToggle);
        tbToggle.setOnToggleListener(new ToggleView.OnToggleListener() {
            @Override
            public void onToggle(boolean isOpen) {
                if (listener != null) {
                    listener.onToggle(RowLayout.this, isOpen);
                }
            }
        });
    }

    /**
     * Set text content
     */
    public void setText(CharSequence text) {
        tvContent.setText(text);
    }

    /**
     * Set auxiliary text content
     */
    public void setHint(CharSequence text, int visibility) {
        if (visibilityGoto != VISIBLE) {
            return;
        }
        tvHint.setText(text);
    }

    /**
     * Set red dot text content
     */
    public void setNumber(CharSequence text, int visibility) {
        if (visibilityGoto != VISIBLE) {
            return;
        }
        tvNumber.setText(text);
        tvNumber.setVisibility(visibility);
    }

    /**
     * Toggle button set to open or close
     */
    public void setOpen(boolean open) {
        tbToggle.setOpen(open);
    }

    /**
     * Toggle button opening or closing status
     */
    public boolean isOpen() {
        return tbToggle.isOpen();
    }

    public interface OnToggleListener {
        void onToggle(View v, boolean isOpen);
    }

    public void setOnToggleListener(OnToggleListener listener) {
        this.listener = listener;
    }
}
