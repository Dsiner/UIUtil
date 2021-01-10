package com.d.lib.ui.view.touchevent;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * ViewGroupF - Gingerbread - 2.3.7
 * Created by D on 2017/5/30.
 */
public class ViewGroupF extends ViewF {

    /**
     * When set, this ViewGroup should not intercept touch events.
     * {@hide}
     */
    protected static final int FLAG_DISALLOW_INTERCEPT = 0x80000;
    /**
     * Mask for use with setFlags indicating bits used for visibility.
     * {@hide}
     */
    static final int VISIBILITY_MASK = 0x0000000C;
    /**
     * Indicates whether the view is temporarily detached.
     *
     * @hide
     */
    static final int CANCEL_NEXT_UP_EVENT = 0x04000000;
    private final Rect mTempRect = new Rect();
    /**
     * Internal flags.
     * <p>
     * This field should be made private, so it is hidden from the SDK.
     * {@hide}
     */
    protected int mGroupFlags;
    private ViewGroupF mParent;
    // Child views of this ViewGroup
    private ViewF[] mChildren;
    // Number of valid children in the mChildren array, the rest should be null or not
    // considered as children
    private int mChildrenCount;
    // Target of Motion events
    private ViewF mMotionTarget;

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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!onFilterTouchEventForSecurity(ev)) {
            return false;
        }

        final int action = ev.getAction();
        final float xf = ev.getX();
        final float yf = ev.getY();
        final float scrolledXFloat = xf + getScrollX();
        final float scrolledYFloat = yf + getScrollY();
        final Rect frame = mTempRect;

        boolean disallowIntercept = (mGroupFlags & FLAG_DISALLOW_INTERCEPT) != 0;

        if (action == MotionEvent.ACTION_DOWN) {
            if (mMotionTarget != null) {
                // this is weird, we got a pen down, but we thought it was
                // already down!
                // XXX: We should probably send an ACTION_UP to the current
                // target.
                mMotionTarget = null;
            }
            if (disallowIntercept || !onInterceptTouchEvent(ev)) {
                // reset this event's action (just to protect ourselves)
                ev.setAction(MotionEvent.ACTION_DOWN);
                // We know we want to dispatch the event down, find a child
                // who can handle it, start with the front-most child.
                final int scrolledXInt = (int) scrolledXFloat;
                final int scrolledYInt = (int) scrolledYFloat;
                final ViewF[] children = mChildren;
                final int count = mChildrenCount;

                for (int i = count - 1; i >= 0; i--) {
                    final ViewF child = children[i];
                    if ((child.mViewFlags & VISIBILITY_MASK) == VISIBLE
                            || child.getAnimation() != null) {
                        child.getHitRect(frame);
                        if (frame.contains(scrolledXInt, scrolledYInt)) {
                            // offset the event to the view's coordinate system
                            final float xc = scrolledXFloat - child.getLeft();
                            final float yc = scrolledYFloat - child.getTop();
                            ev.setLocation(xc, yc);
                            child.mPrivateFlags &= ~CANCEL_NEXT_UP_EVENT;
                            if (child.dispatchTouchEvent(ev)) {
                                // Event handled, we have a target now.
                                mMotionTarget = child;
                                return true;
                            }
                            // The event didn't get handled, try the next view.
                            // Don't reset the event's location, it's not
                            // necessary here.
                        }
                    }
                }
            }
        }

        boolean isUpOrCancel = (action == MotionEvent.ACTION_UP) ||
                (action == MotionEvent.ACTION_CANCEL);

        if (isUpOrCancel) {
            // Note, we've already copied the previous state to our local
            // variable, so this takes effect on the next event
            mGroupFlags &= ~FLAG_DISALLOW_INTERCEPT;
        }

        // The event wasn't an ACTION_DOWN, dispatch it to our target if
        // we have one.
        final ViewF target = mMotionTarget;
        if (target == null) {
            // We don't have a target, this means we're handling the
            // event as a regular view.
            ev.setLocation(xf, yf);
            if ((mPrivateFlags & CANCEL_NEXT_UP_EVENT) != 0) {
                ev.setAction(MotionEvent.ACTION_CANCEL);
                mPrivateFlags &= ~CANCEL_NEXT_UP_EVENT;
            }
            return super.dispatchTouchEvent(ev);
        }

        // if have a target, see if we're allowed to and want to intercept its
        // events
        if (!disallowIntercept && onInterceptTouchEvent(ev)) {
            final float xc = scrolledXFloat - (float) target.getLeft();
            final float yc = scrolledYFloat - (float) target.getTop();
            mPrivateFlags &= ~CANCEL_NEXT_UP_EVENT;
            ev.setAction(MotionEvent.ACTION_CANCEL);
            ev.setLocation(xc, yc);
            if (!target.dispatchTouchEvent(ev)) {
                // target didn't handle ACTION_CANCEL. not much we can do
                // but they should have.
            }
            // clear the target
            mMotionTarget = null;
            // Don't dispatch this event to our own view, because we already
            // saw it when intercepting; we just want to give the following
            // event to the normal onTouchEvent().
            return true;
        }

        if (isUpOrCancel) {
            mMotionTarget = null;
        }

        // finally offset the event to the target's coordinate system and
        // dispatch the event.
        final float xc = scrolledXFloat - (float) target.getLeft();
        final float yc = scrolledYFloat - (float) target.getTop();
        ev.setLocation(xc, yc);

        if ((target.mPrivateFlags & CANCEL_NEXT_UP_EVENT) != 0) {
            ev.setAction(MotionEvent.ACTION_CANCEL);
            target.mPrivateFlags &= ~CANCEL_NEXT_UP_EVENT;
            mMotionTarget = null;
        }

        return target.dispatchTouchEvent(ev);
    }

    /**
     * {@inheritDoc}
     */
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (disallowIntercept == ((mGroupFlags & FLAG_DISALLOW_INTERCEPT) != 0)) {
            // We're already in this state, assume our ancestors are too
            return;
        }

        if (disallowIntercept) {
            mGroupFlags |= FLAG_DISALLOW_INTERCEPT;
        } else {
            mGroupFlags &= ~FLAG_DISALLOW_INTERCEPT;
        }

        // Pass it up to our parent
        if (mParent != null) {
            mParent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }

    /**
     * Implement this method to intercept all touch screen motion events.  This
     * allows you to watch events as they are dispatched to your children, and
     * take ownership of the current gesture at any point.
     * <p>
     * <p>Using this function takes some care, as it has a fairly complicated
     * interaction with {@link View#onTouchEvent(MotionEvent)
     * View.onTouchEvent(MotionEvent)}, and using it requires implementing
     * that method as well as this one in the correct way.  Events will be
     * received in the following order:
     * <p>
     * <ol>
     * <li> You will receive the down event here.
     * <li> The down event will be handled either by a child of this view
     * group, or given to your own onTouchEvent() method to handle; this means
     * you should implement onTouchEvent() to return true, so you will
     * continue to see the rest of the gesture (instead of looking for
     * a parent view to handle it).  Also, by returning true from
     * onTouchEvent(), you will not receive any following
     * events in onInterceptTouchEvent() and all touch processing must
     * happen in onTouchEvent() like normal.
     * <li> For as long as you return false from this function, each following
     * event (up to and including the final up) will be delivered first here
     * and then to the target's onTouchEvent().
     * <li> If you return true from here, you will not receive any
     * following events: the target view will receive the same event but
     * with the action {@link MotionEvent#ACTION_CANCEL}, and all further
     * events will be delivered to your onTouchEvent() method and no longer
     * appear here.
     * </ol>
     *
     * @param ev The motion event being dispatched down the hierarchy.
     * @return Return true to steal motion events from the children and have
     * them dispatched to this ViewGroup through onTouchEvent().
     * The current target will receive an ACTION_CANCEL event, and no further
     * messages will be delivered here.
     */
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    /**
     * Implement this method to handle touch screen motion events.
     *
     * @param event The motion event.
     * @return True if the event was handled, false otherwise.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
