package com.d.uiutil.slidelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.d.uiutil.Util;

/**
 * SlideLayout
 * Created by D on 2017/5/19.
 */
public class SlideLayout extends ViewGroup {
    private int width;
    private int height;
    private Scroller scroller;
    private int touchSlop;
    private int slideSlop;
    private float dX, dY;
    private float lastX;//onInterceptTouchEvent最后一次坐标(lastX,lastY)
    private int leftBorder;
    private int rightBorder;
    private int duration = 200;
    private boolean isOpen;

    public SlideLayout(Context context) {
        this(context, null);
    }

    public SlideLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        scroller = new Scroller(context);
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        slideSlop = Util.dip2px(context, 30);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            //为ViewGroup中的每一个子控件测量大小
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        if (changed && count > 0) {
            int left = 0, top = 0;
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                //为ViewGroup中的每一个子控件在水平方向上进行布局
                int childWidth = child.getMeasuredWidth();
                child.layout(left, top, left + childWidth, child.getMeasuredHeight());
                left += childWidth;
            }
            //初始化左右边界值
            leftBorder = getChildAt(0).getLeft();
            rightBorder = getChildAt(count - 1).getRight();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float eX = ev.getRawX();
        float eY = ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = dX = eX;
                dY = eY;
            case MotionEvent.ACTION_MOVE:
                lastX = eX;
                //当手指横向拖动值大于TouchSlop时，认为应该进行滚动，禁止父控件拦截事件，并拦截子控件的事件
                if (Math.abs(eX - dX) > touchSlop && Math.abs(eX - dX) > Math.abs(eY - dY)) {
                    requestDisallowInterceptTouchEvent(true);
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eX = event.getRawX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int offset = (int) (lastX - eX);
                lastX = eX;
                if (getScrollX() + offset < leftBorder) {
                    scrollTo(leftBorder, 0);
                    return true;
                } else if (getScrollX() + offset > rightBorder - width) {
                    scrollTo(rightBorder - width, 0);
                    return true;
                }
                scrollBy(offset, 0);
                break;
            case MotionEvent.ACTION_UP:
                if (eX - dX < -slideSlop) {
                    toggle(true);
                } else if (eX - dX > slideSlop) {
                    toggle(false);
                } else {
                    toggle(isOpen);
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void toggle(boolean open) {
        isOpen = open;
        if (isOpen) {
            smoothScrollTo(rightBorder - width, duration);
        } else {
            smoothScrollTo(0, duration);
        }
    }

    private void smoothScrollTo(int dstX, int duration) {
        int offset = dstX - getScrollX();
        scroller.startScroll(getScrollX(), 0, offset, 0, duration);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        toggle(open);
    }

    public void close() {
        setOpen(false);
    }
}
