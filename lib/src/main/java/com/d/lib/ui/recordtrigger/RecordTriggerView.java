package com.d.lib.ui.recordtrigger;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;

import com.d.lib.ui.R;
import com.d.lib.ui.UIUtil;

/**
 * 触发器
 * Created by D on 2017/11/1.
 */
public class RecordTriggerView extends AppCompatTextView {
    public static final int STATE_CANCLE = 0;//滑出范围，取消本次录音
    public static final int STATE_VALID = 1;//有效，开始录音
    public static final int STATE_INVALID = 2;//无效，滑出范围
    public static final int STATE_SUBMIT = 3;//未滑出范围，提交本次录音

    private int width;
    private int height;

    private int touchRadius;//touch有效范围，半径
    private float dX, dY;
    private boolean isValid;//touch是否有效，在有效范围内
    private OnTriggerListener listener;
    private int curState;

    public RecordTriggerView(Context context) {
        this(context, null);
    }

    public RecordTriggerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordTriggerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        touchRadius = UIUtil.dip2px(context, 105);
        setGravity(Gravity.CENTER);
        setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.lib_ui_rtv_corner_bg_normal));
        setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        setTextColor(Color.parseColor("#727272"));
        setText(R.string.lib_ui_record_tip_voice);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eX = event.getX();
        float eY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dX = eX;
                dY = eY;
                isValid = true;
                curState = STATE_VALID;
                setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.lib_ui_rtv_corner_bg_select));
                setText(R.string.lib_ui_record_tip_submit);
                if (listener != null) {
                    listener.onStateChange(curState);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                isValid = Math.abs(Math.sqrt((eX - dX) * (eX - dX) + (eY - dY) * (eY - dY))) < touchRadius;
                int state = isValid ? STATE_VALID : STATE_INVALID;
                if (state != curState) {
                    setText(isValid ? R.string.lib_ui_record_tip_submit : R.string.lib_ui_record_tip_cancle);
                    if (listener != null) {
                        listener.onStateChange(state);
                    }
                }
                curState = state;
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                curState = isValid ? STATE_SUBMIT : STATE_CANCLE;
                if (listener != null) {
                    listener.onStateChange(curState);
                }
                setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.lib_ui_rtv_corner_bg_normal));
                setText(R.string.lib_ui_record_tip_voice);
                return true;
        }
        return super.onTouchEvent(event);
    }

    public interface OnTriggerListener {
        void onStateChange(int state);
    }

    public void setOnTriggerListener(OnTriggerListener l) {
        this.listener = l;
    }
}
