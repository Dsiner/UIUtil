package com.d.lib.ui.poi;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.d.lib.xrv.XRecyclerView;

/**
 * PoiListView
 * Created by D on 2017/7/24.
 */
public class PoiListView extends XRecyclerView {
    private float dX, dY;//TouchEvent_ACTION_DOWN坐标(dX,dY)
    private float lastY;//TouchEvent最后一次坐标(lastX,lastY)
    private int touchSlop;
    private boolean isMoveValid;
    private boolean canScroll = true;

    public PoiListView(Context context) {
        this(context, null);
    }

    public PoiListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PoiListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float eX = event.getY();
        final float eY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = dY = eY;
                canScroll = true;
                super.onTouchEvent(event);
                return true;
            case MotionEvent.ACTION_MOVE:
                if (!canScroll) {
                    return false;
                }
                int offset = (int) (lastY - eY);
                lastY = eY;
                canScroll = !isTop() || offset > 0;
                super.onTouchEvent(event);
                return canScroll;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                canScroll = true;
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean isTop() {
        LayoutManager manager = getLayoutManager();
        if (manager == null || !(manager instanceof LinearLayoutManager)) {
            return false;
        }
        int fpos = ((LinearLayoutManager) manager).findFirstVisibleItemPosition();
        View childView = getChildAt(0);
        return childView != null && fpos <= 1 && childView.getTop() >= 0;
    }
}
