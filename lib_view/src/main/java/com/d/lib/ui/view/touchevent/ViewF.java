package com.d.lib.ui.view.touchevent;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;

/**
 * ViewF - Gingerbread - 2.3.7
 * Created by D on 2017/5/30.
 */
public class ViewF extends View {

    /**
     * Mask for use with setFlags indicating bits used for indicating whether
     * this view is enabled
     * {@hide}
     */
    static final int ENABLED_MASK = 0x00000020;
    /**
     * This view is enabled. Intrepretation varies by subclass.
     * Use with ENABLED_MASK when calling setFlags.
     * {@hide}
     */
    static final int ENABLED = 0x00000000;
    /**
     * This view is disabled. Intrepretation varies by subclass.
     * Use with ENABLED_MASK when calling setFlags.
     * {@hide}
     */
    static final int DISABLED = 0x00000020;
    /**
     * <p>Indicates this view can be clicked. When clickable, a View reacts
     * to clicks by notifying the OnClickListener.<p>
     * {@hide}
     */
    static final int CLICKABLE = 0x00004000;
    /**
     * <p>
     * Indicates this view can be long clicked. When long clickable, a View
     * reacts to long clicks by notifying the OnLongClickListener or showing a
     * context menu.
     * </p>
     * {@hide}
     */
    static final int LONG_CLICKABLE = 0x00200000;
    int mPrivateFlags;
    /**
     * The view flags hold various views states.
     * {@hide}
     */
    @ViewDebug.ExportedProperty
    int mViewFlags;
    private OnTouchListener mOnTouchListener;

    public ViewF(Context context) {
        super(context);
    }

    public ViewF(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewF(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Pass the touch screen motion event down to the target view, or this
     * view if it is the target.
     *
     * @param event The motion event to be dispatched.
     * @return True if the event was handled by the view, false otherwise.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (mOnTouchListener != null && (mViewFlags & ENABLED_MASK) == ENABLED &&
                mOnTouchListener.onTouch(this, event)) {
            return true;
        }
        return onTouchEvent(event);
    }

    /**
     * Implement this method to handle touch screen motion events.
     *
     * @param event The motion event.
     * @return True if the event was handled, false otherwise.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int viewFlags = mViewFlags;

        if ((viewFlags & ENABLED_MASK) == DISABLED) {
            // A disabled view that is clickable still consumes the touch
            // events, it just doesn't respond to them.
            return (((viewFlags & CLICKABLE) == CLICKABLE ||
                    (viewFlags & LONG_CLICKABLE) == LONG_CLICKABLE));
        }

        if (((viewFlags & CLICKABLE) == CLICKABLE ||
                (viewFlags & LONG_CLICKABLE) == LONG_CLICKABLE)) {
            return true;
        }
        return false;
    }
}
