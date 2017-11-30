package com.d.lib.ui.poi;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.d.lib.ui.UIUtil;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * PoiLayout
 * Created by D on 2017/7/21.
 */
public class PoiLayout extends ViewGroup {
    public final static int STATUS_DEFAULT = 0;
    public final static int STATUS_EXTEND = 1;
    public final static int STATUS_CLOSE = 2;

    private int width;
    private int height;

    private int offsetY;
    private int topBorder;
    private int bottomBorder;
    private int touchSlop;
    private int slideSlop;
    private int duration = 210;
    private float dX, dY;//TouchEvent_ACTION_DOWN坐标(dX,dY)
    private float lastY;//TouchEvent最后一次坐标(lastX,lastY)
    private boolean isEventValid = true;//本次touch事件是否有效
    private boolean isMoveValid;
    private int status;
    private ValueAnimator animation;
    private float factor;//进度因子:0-1
    private int curY, dstY;
    private OnChangeListener listener;
    private int offsetB;

    private int offsetExtend;
    private int offsetClose;
    private int offsetDefault;

    public PoiLayout(Context context) {
        this(context, null);
    }

    public PoiLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PoiLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        slideSlop = UIUtil.dip2px(context, 45);
        offsetB = UIUtil.dip2px(context, 40);
        animation = ValueAnimator.ofFloat(0f, 1f);
        animation.setDuration(duration);
        animation.setInterpolator(new LinearInterpolator());
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                factor = (float) animation.getAnimatedValue();//更新进度因子
                float scrollY = curY + (dstY - curY) * factor;
                scrollTo(0, (int) scrollY);
                postInvalidate();//刷新
                if (listener != null) {
                    listener.onScroll(getOffset(scrollY));
                }
            }
        });
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                factor = 1;
                if (listener != null) {
                    listener.onChange(status);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                factor = 1;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private float getOffset(float scrollY) {
        float offset;
        if (scrollY > 0) {
            offset = scrollY / offsetExtend;
        } else {
            offset = -scrollY / offsetClose;
        }
        if (offset < -1) {
            offset = -1;
        }
        if (offset > 1) {
            offset = 1;
        }
        return offset;
    }

    public void start() {
        stop();
        if (animation != null) {
            animation.start();
        }
    }

    public void stop() {
        if (animation != null) {
            animation.end();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        offsetY = height - UIUtil.dip2px(getContext(), 210);
        offsetExtend = offsetY;
        offsetClose = offsetY + offsetB - height;
        offsetDefault = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        if (count > 0) {
            int top = offsetY;
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                int childHeight = child.getMeasuredHeight();
                switch (i) {
                    case 0:
                        child.layout(0, top, width, top + childHeight);
                        top += childHeight;
                        break;
                    case 1:
                        child.layout(0, top, width, top + height);
                        top += childHeight;
                        break;
                    case 2:
                        child.layout(0, offsetY, width, offsetY + childHeight);
                        break;
                }
            }
            topBorder = getChildAt(0).getTop();
            bottomBorder = getChildAt(count - 2).getBottom();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final float eX = ev.getX();
        final float eY = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dX = eX;
                lastY = dY = eY;
                isMoveValid = false;
                isEventValid = true;
                if (getScrollY() + eY > offsetY) {
                    if (!(factor == 0 || factor == 1)) {
                        isEventValid = false;
                    } else {
                        super.dispatchTouchEvent(ev);
                    }
                    return true;
                } else {
                    return false;
                }
            case MotionEvent.ACTION_MOVE:
                if (!isEventValid) {
                    return false;
                }
                int offset = (int) (lastY - eY);
                lastY = eY;
                if ((status == STATUS_EXTEND || status == STATUS_CLOSE) && super.dispatchTouchEvent(ev)) {
                    return true;
                }
                if (!isMoveValid && Math.abs(eY - dY) > touchSlop && Math.abs(eY - dY) > Math.abs(eX - dX)) {
                    isMoveValid = true;
                }
                if (isMoveValid) {
                    if (getScrollY() + offset <= offsetClose) {
                        scrollTo(0, offsetClose);
                        dY = eY;//reset eY
                        status = STATUS_CLOSE;
                        if (listener != null) {
                            listener.onScroll(getScrollY());
                            listener.onChange(status);
                        }
                    } else if (getScrollY() + offset >= offsetExtend) {
                        scrollTo(0, offsetExtend);
                        dY = eY;//reset eY
                        status = STATUS_EXTEND;
                        if (listener != null) {
                            listener.onScroll(getScrollY());
                            listener.onChange(status);
                        }
                    } else {
                        scrollBy(0, offset);
                        if (listener != null) {
                            listener.onScroll(getScrollY());
                        }
                    }
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (!isEventValid) {
                    return false;
                }
                if (isMoveValid && getScrollY() > offsetClose && getScrollY() < offsetExtend) {
                    dealUp(getScrollY());
                    isMoveValid = false;
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void dealUp(int scrollY) {
        switch (status) {
            case STATUS_EXTEND:
                if (scrollY < offsetDefault) {
                    toggle(STATUS_CLOSE);
                } else if (scrollY < offsetExtend - slideSlop) {
                    toggle(STATUS_DEFAULT);
                } else {
                    toggle(STATUS_EXTEND);
                }
                break;
            case STATUS_CLOSE:
                if (scrollY > offsetDefault) {
                    toggle(STATUS_EXTEND);
                } else if (scrollY > offsetClose + slideSlop) {
                    toggle(STATUS_DEFAULT);
                } else {
                    toggle(STATUS_CLOSE);
                }
                break;
            case STATUS_DEFAULT:
                if (scrollY > slideSlop) {
                    toggle(STATUS_EXTEND);
                } else if (scrollY < -slideSlop) {
                    toggle(STATUS_CLOSE);
                } else {
                    toggle(STATUS_DEFAULT);
                }
                break;
        }
    }

    public void toggle(int status) {
        this.status = status;
        curY = getScrollY();
        switch (status) {
            case STATUS_EXTEND:
                dstY = offsetExtend;
                start();
                break;
            case STATUS_CLOSE:
                dstY = offsetClose;
                start();
                break;
            case STATUS_DEFAULT:
                dstY = offsetDefault;
                start();
                break;
        }
    }

    public interface OnChangeListener {
        void onChange(int status);

        void onScroll(float offset);
    }

    public void setOnChangeListener(OnChangeListener l) {
        this.listener = l;
    }
}
