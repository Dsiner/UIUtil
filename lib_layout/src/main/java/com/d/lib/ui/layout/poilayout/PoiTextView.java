package com.d.lib.ui.layout.poilayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;

/**
 * PoiTextView
 * Created by D on 2017/7/28.
 */
@SuppressLint("AppCompatCustomView")
public class PoiTextView extends TextView {
    private boolean mIsClickValid = true;
    private int mTouchSlop;
    private float mDX, mDY; // TouchEvent_ACTION_DOWN坐标(dX,dY)
    private float mLastY; // TouchEvent最后一次坐标(lastX,lastY)
    private OnTikListener mListener;

    public PoiTextView(Context context) {
        this(context, null);
    }

    public PoiTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PoiTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eX = event.getX();
        float eY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDX = eX;
                mDY = eY;
                mIsClickValid = true;
                return true;
            case MotionEvent.ACTION_MOVE:
                if (mIsClickValid && (Math.abs(eY - mDY) > mTouchSlop || Math.abs(eY - mDY) > mTouchSlop)) {
                    mIsClickValid = false;
                }
                return mIsClickValid;
            case MotionEvent.ACTION_UP:
                if (mIsClickValid && mListener != null) {
                    mListener.onTik(this);
                }
                return mIsClickValid;
        }
        return super.onTouchEvent(event);
    }

    public void setOnTikListener(OnTikListener l) {
        this.mListener = l;
    }

    public interface OnTikListener {
        void onTik(View v);
    }
}
