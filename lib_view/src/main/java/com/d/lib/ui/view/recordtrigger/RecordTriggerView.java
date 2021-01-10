package com.d.lib.ui.view.recordtrigger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.TextView;

import com.d.lib.common.util.DimenUtils;
import com.d.lib.ui.view.R;

/**
 * 触发器
 * Created by D on 2017/11/1.
 */
@SuppressLint("AppCompatCustomView")
public class RecordTriggerView extends TextView {
    public static final int STATE_CANCLE = 0; // 滑出范围，取消本次录音
    public static final int STATE_VALID = 1; // 有效，开始录音
    public static final int STATE_INVALID = 2; // 无效，滑出范围
    public static final int STATE_SUBMIT = 3; // 未滑出范围，提交本次录音

    private int mWidth;
    private int mHeight;

    private int mTouchRadius; // Touch有效范围，半径
    private float mDX, mDY;
    private boolean mIsValid; // Touch是否有效，在有效范围内
    private OnTriggerListener mListener;
    private int mCurState;

    public RecordTriggerView(Context context) {
        super(context);
        init(context);
    }

    public RecordTriggerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RecordTriggerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mTouchRadius = DimenUtils.dp2px(context, 105);
        setGravity(Gravity.CENTER);
        setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.lib_ui_view_rtv_corner_bg_normal));
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        setTextColor(Color.parseColor("#727272"));
        setText(R.string.lib_ui_view_record_tip_voice);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eX = event.getX();
        float eY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDX = eX;
                mDY = eY;
                mIsValid = true;
                mCurState = STATE_VALID;
                setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.lib_ui_view_rtv_corner_bg_select));
                setText(R.string.lib_ui_view_record_tip_submit);
                if (mListener != null) {
                    mListener.onStateChange(mCurState);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                mIsValid = Math.abs(Math.sqrt((eX - mDX) * (eX - mDX) + (eY - mDY) * (eY - mDY))) < mTouchRadius;
                int state = mIsValid ? STATE_VALID : STATE_INVALID;
                if (state != mCurState) {
                    setText(mIsValid ? R.string.lib_ui_view_record_tip_submit : R.string.lib_ui_view_record_tip_cancle);
                    if (mListener != null) {
                        mListener.onStateChange(state);
                    }
                }
                mCurState = state;
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mCurState = mIsValid ? STATE_SUBMIT : STATE_CANCLE;
                if (mListener != null) {
                    mListener.onStateChange(mCurState);
                }
                setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.lib_ui_view_rtv_corner_bg_normal));
                setText(R.string.lib_ui_view_record_tip_voice);
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void setOnTriggerListener(OnTriggerListener l) {
        this.mListener = l;
    }

    public interface OnTriggerListener {
        void onStateChange(int state);
    }
}
