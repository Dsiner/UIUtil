package com.d.lib.ui.touchevent;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * ViewF
 * Created by D on 2017/5/30.
 */
public class ViewF extends View {
    public ViewF(Context context) {
        super(context);
    }

    public ViewF(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewF(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    /**
//     * @return onTouchEvent(event)
//     */
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        if (mOnTouchListener != null && (mViewFlags & ENABLED_MASK) == ENABLED &&
//                mOnTouchListener.onTouch(this, event)) {
//            return true;
//        }
//        return onTouchEvent(event);
//    }
//
//    /**
//     * @return false
//     */
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        final int viewFlags = mViewFlags;
//        if ((viewFlags & ENABLED_MASK) == DISABLED) {
//            // A disabled view that is clickable still consumes the touch
//            // events, it just doesn't respond to them.
//            return (((viewFlags & CLICKABLE) == CLICKABLE ||
//                    (viewFlags & LONG_CLICKABLE) == LONG_CLICKABLE));
//        }
//        if (((viewFlags & CLICKABLE) == CLICKABLE ||
//                (viewFlags & LONG_CLICKABLE) == LONG_CLICKABLE)) {
//            return true;
//        }
//        return false;
//    }
}
