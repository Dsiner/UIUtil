package com.d.lib.ui.common.touchevent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * ViewGroupF
 * Created by D on 2017/5/30.
 */
public class ViewGroupF extends ViewGroup {
    public ViewGroupF(Context context) {
        super(context);
    }

    public ViewGroupF(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewGroupF(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        final int action = ev.getAction();
//        if (action == MotionEvent.ACTION_DOWN) {
//            mMotionTarget = null;
//            if (disallowIntercept || !onInterceptTouchEvent(ev)) {
//                for (int i = mChildrenCount - 1; i >= 0; i--) {
//                    final View child = mChildren[i];
//                    if ((child.mViewFlags & VISIBILITY_MASK) == VISIBLE
//                            || child.getAnimation() != null) {
//                        if (frame.contains(scrolledXInt, scrolledYInt)) {
//                            if (child.dispatchTouchEvent(ev)) {
//                                mMotionTarget = child;
//                                return true;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        boolean isUpOrCancel = (action == MotionEvent.ACTION_UP) ||
//                (action == MotionEvent.ACTION_CANCEL);
//        if (mMotionTarget == null) {
//            return super.dispatchTouchEvent(ev);
//        }
//        if (!disallowIntercept && onInterceptTouchEvent(ev)) {
//            ev.setAction(MotionEvent.ACTION_CANCEL);
//            if (!mMotionTarget.dispatchTouchEvent(ev)) {
//            }
//            mMotionTarget = null;
//            return true;
//        }
//        if (isUpOrCancel) {
//            mMotionTarget = null;
//        }
//        return mMotionTarget.dispatchTouchEvent(ev);
//    }
//
//    /**
//     * @return false
//     */
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return false;
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
