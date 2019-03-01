package com.d.lib.common.view.toggle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckedTextView;

/**
 * ToggleTextView
 * Created by D on 2018/1/16.
 */
@SuppressLint("AppCompatCustomView")
public class ToggleTextView extends CheckedTextView implements ToggleView, View.OnClickListener {

    private OnToggleListener mListener;

    public ToggleTextView(Context context) {
        this(context, null);
    }

    public ToggleTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToggleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnClickListener(this);
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
    }

    @Override
    public void setOpen(boolean open) {
        setChecked(open);
    }

    @Override
    public boolean isOpen() {
        return isChecked();
    }

    @Override
    public void onClick(View v) {
        toggle();
        if (mListener != null) {
            mListener.onToggle(isChecked());
        }
    }

    @Override
    public void setOnToggleListener(OnToggleListener l) {
        this.mListener = l;
    }
}