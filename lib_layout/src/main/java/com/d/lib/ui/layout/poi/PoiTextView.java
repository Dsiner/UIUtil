package com.d.lib.ui.layout.poi;

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
    private boolean isClickValid = true;
    private int touchSlop;
    private float dX, dY; // TouchEvent_ACTION_DOWN坐标(dX,dY)
    private float lastY; // TouchEvent最后一次坐标(lastX,lastY)
    private OnTikListener listener;

    public PoiTextView(Context context) {
        this(context, null);
    }

    public PoiTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PoiTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eX = event.getX();
        float eY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dX = eX;
                dY = eY;
                isClickValid = true;
                return true;
            case MotionEvent.ACTION_MOVE:
                if (isClickValid && (Math.abs(eY - dY) > touchSlop || Math.abs(eY - dY) > touchSlop)) {
                    isClickValid = false;
                }
                return isClickValid;
            case MotionEvent.ACTION_UP:
                if (isClickValid && listener != null) {
                    listener.onTik(this);
                }
                return isClickValid;
        }
        return super.onTouchEvent(event);
    }

    public interface OnTikListener {
        void onTik(View v);
    }

    public void setOnTikListener(OnTikListener l) {
        this.listener = l;
    }
}
