package com.d.lib.ui.layout.poilayout;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.d.lib.pulllayout.rv.PullRecyclerView;

/**
 * PoiListView
 * Created by D on 2017/7/24.
 */
public class PoiListView extends PullRecyclerView {
    private float mTouchX, mTouchY; // TouchEvent_ACTION_DOWN坐标(x, y)
    private float mLastTouchY; // TouchEvent最后一次坐标(x, y)
    private int mTouchSlop;
    private boolean mIsMoveValid;
    private boolean mCanScroll = true;

    public PoiListView(Context context) {
        this(context, null);
    }

    public PoiListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PoiListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float x = event.getY();
        final float y = event.getY();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchY = mTouchY = y;
                mCanScroll = true;
                super.onTouchEvent(event);
                return true;

            case MotionEvent.ACTION_MOVE:
                if (!mCanScroll) {
                    return false;
                }
                final int offset = (int) (mLastTouchY - y);
                mLastTouchY = y;
                mCanScroll = !isOnTop() || offset > 0;
                super.onTouchEvent(event);
                return mCanScroll;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mCanScroll = true;
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean isOnTop() {
        final LayoutManager layoutManager = getLayoutManager();
        if (!(layoutManager instanceof LinearLayoutManager)) {
            return false;
        }
        final int firstPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        final View childView = getChildAt(0);
        return childView != null && firstPosition <= 1 && childView.getTop() >= 0;
    }
}
