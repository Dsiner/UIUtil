package com.d.lib.common.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.d.lib.common.R;

/**
 * ClearEditText
 * Created by D on 2018/6/15.
 */
@SuppressLint("AppCompatCustomView")
public class ClearEditText extends EditText implements View.OnTouchListener, View.OnFocusChangeListener, TextWatcher {

    private Drawable mClearTextIcon;
    private Drawable mSearchTextIcon;
    private OnFocusChangeListener mOnFocusChangeListener;
    private OnTouchListener mOnTouchListener;

    public ClearEditText(Context context) {
        super(context);
        init(context);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        mClearTextIcon = ContextCompat.getDrawable(context, R.drawable.lib_pub_ic_edit_delete);
        mClearTextIcon.setBounds(0, 0, mClearTextIcon.getIntrinsicWidth(), mClearTextIcon.getIntrinsicHeight());

        mSearchTextIcon = ContextCompat.getDrawable(context, R.drawable.lib_pub_ic_edit_search);
        mSearchTextIcon.setBounds(0, 0, mSearchTextIcon.getIntrinsicWidth(), mSearchTextIcon.getIntrinsicHeight());

        setClearIconVisible(false);
        setSearchIconVisible(true);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        mOnFocusChangeListener = l;
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        mOnTouchListener = l;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
            setSearchIconVisible(false);
        } else {
            setSearchIconVisible(true);
            setClearIconVisible(false);
        }
        if (mOnFocusChangeListener != null) {
            mOnFocusChangeListener.onFocusChange(v, hasFocus);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        final int x = (int) motionEvent.getX();
        if (mClearTextIcon.isVisible() && x > getWidth() - getPaddingRight() - mClearTextIcon.getIntrinsicWidth()) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                setError(null);
                setText("");
            }
            return true;
        }
        return mOnTouchListener != null && mOnTouchListener.onTouch(view, motionEvent);
    }

    @Override
    public final void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (isFocused()) {
            setClearIconVisible(text.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void setSearchIconVisible(final boolean visible) {
        mSearchTextIcon.setVisible(visible, false);
        final Drawable[] compoundDrawables = getCompoundDrawables();
        setCompoundDrawables(visible ? mSearchTextIcon : null,
                compoundDrawables[1],
                compoundDrawables[2],
                compoundDrawables[3]);
    }

    private void setClearIconVisible(final boolean visible) {
        mClearTextIcon.setVisible(visible, false);
        final Drawable[] compoundDrawables = getCompoundDrawables();
        setCompoundDrawables(compoundDrawables[0],
                compoundDrawables[1],
                visible ? mClearTextIcon : null,
                compoundDrawables[3]);
    }
}