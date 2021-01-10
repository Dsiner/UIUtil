package com.d.lib.ui.layout.poilayout;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.d.lib.common.util.DimenUtils;

/**
 * PoiLayout
 * Created by D on 2017/7/21.
 */
public class PoiLayout extends ViewGroup {
    public final static int STATUS_DEFAULT = 0;
    public final static int STATUS_EXTEND = 1;
    public final static int STATUS_CLOSE = 2;

    private int mWidth;
    private int mHeight;

    private int mOffsetY;
    private int mTopBorder;
    private int mBottomBorder;
    private int mTouchSlop;
    private int mSlideSlop;
    private int mDuration = 210;
    private float mDX, mDY; // TouchEvent_ACTION_DOWN坐标(dX,dY)
    private float mLastY; // TouchEvent最后一次坐标(lastX,lastY)
    private boolean mIsEventValid = true; // 本次Touch事件是否有效
    private boolean mIsMoveValid;
    private int mStatus;
    private ValueAnimator mAnimation;
    private float mFactor; // 进度因子: 0-1
    private int mCurY, mDstY;
    private int mOffsetB;
    private int mOffsetExtend;
    private int mOffsetClose;
    private int mOffsetDefault;
    private OnChangeListener mListener;

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
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mSlideSlop = DimenUtils.dp2px(context, 45);
        mOffsetB = DimenUtils.dp2px(context, 40);
        mAnimation = ValueAnimator.ofFloat(0f, 1f);
        mAnimation.setDuration(mDuration);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mFactor = (float) animation.getAnimatedValue();
                float scrollY = mCurY + (mDstY - mCurY) * mFactor;
                scrollTo(0, (int) scrollY);
                invalidate();
                if (mListener != null) {
                    mListener.onScroll(getOffset(scrollY));
                }
            }
        });
        mAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mFactor = 1;
                if (mListener != null) {
                    mListener.onChange(mStatus);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mFactor = 1;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private float getOffset(float scrollY) {
        float offset;
        if (scrollY > 0) {
            offset = scrollY / mOffsetExtend;
        } else {
            offset = -scrollY / mOffsetClose;
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
        if (mAnimation != null) {
            mAnimation.start();
        }
    }

    public void stop() {
        if (mAnimation != null) {
            mAnimation.end();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mOffsetY = mHeight - DimenUtils.dp2px(getContext(), 210);
        mOffsetExtend = mOffsetY;
        mOffsetClose = mOffsetY + mOffsetB - mHeight;
        mOffsetDefault = 0;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        if (count > 0) {
            int top = mOffsetY;
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                int childHeight = child.getMeasuredHeight();
                switch (i) {
                    case 0:
                        child.layout(0, top, mWidth, top + childHeight);
                        top += childHeight;
                        break;
                    case 1:
                        child.layout(0, top, mWidth, top + mHeight);
                        top += childHeight;
                        break;
                    case 2:
                        child.layout(0, mOffsetY, mWidth, mOffsetY + childHeight);
                        break;
                }
            }
            mTopBorder = getChildAt(0).getTop();
            mBottomBorder = getChildAt(count - 2).getBottom();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final float eX = ev.getX();
        final float eY = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDX = eX;
                mLastY = mDY = eY;
                mIsMoveValid = false;
                mIsEventValid = true;
                if (getScrollY() + eY > mOffsetY) {
                    if (!(mFactor == 0 || mFactor == 1)) {
                        mIsEventValid = false;
                    } else {
                        super.dispatchTouchEvent(ev);
                    }
                    return true;
                } else {
                    return false;
                }
            case MotionEvent.ACTION_MOVE:
                if (!mIsEventValid) {
                    return false;
                }
                int offset = (int) (mLastY - eY);
                mLastY = eY;
                if ((mStatus == STATUS_EXTEND || mStatus == STATUS_CLOSE) && super.dispatchTouchEvent(ev)) {
                    return true;
                }
                if (!mIsMoveValid && Math.abs(eY - mDY) > mTouchSlop && Math.abs(eY - mDY) > Math.abs(eX - mDX)) {
                    mIsMoveValid = true;
                }
                if (mIsMoveValid) {
                    if (getScrollY() + offset <= mOffsetClose) {
                        scrollTo(0, mOffsetClose);
                        mDY = eY; // Reset eY
                        mStatus = STATUS_CLOSE;
                        if (mListener != null) {
                            mListener.onScroll(getScrollY());
                            mListener.onChange(mStatus);
                        }
                    } else if (getScrollY() + offset >= mOffsetExtend) {
                        scrollTo(0, mOffsetExtend);
                        mDY = eY; // Reset eY
                        mStatus = STATUS_EXTEND;
                        if (mListener != null) {
                            mListener.onScroll(getScrollY());
                            mListener.onChange(mStatus);
                        }
                    } else {
                        scrollBy(0, offset);
                        if (mListener != null) {
                            mListener.onScroll(getScrollY());
                        }
                    }
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (!mIsEventValid) {
                    return false;
                }
                if (mIsMoveValid && getScrollY() > mOffsetClose && getScrollY() < mOffsetExtend) {
                    dealUp(getScrollY());
                    mIsMoveValid = false;
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void dealUp(int scrollY) {
        switch (mStatus) {
            case STATUS_EXTEND:
                if (scrollY < mOffsetDefault) {
                    toggle(STATUS_CLOSE);
                } else if (scrollY < mOffsetExtend - mSlideSlop) {
                    toggle(STATUS_DEFAULT);
                } else {
                    toggle(STATUS_EXTEND);
                }
                break;
            case STATUS_CLOSE:
                if (scrollY > mOffsetDefault) {
                    toggle(STATUS_EXTEND);
                } else if (scrollY > mOffsetClose + mSlideSlop) {
                    toggle(STATUS_DEFAULT);
                } else {
                    toggle(STATUS_CLOSE);
                }
                break;
            case STATUS_DEFAULT:
                if (scrollY > mSlideSlop) {
                    toggle(STATUS_EXTEND);
                } else if (scrollY < -mSlideSlop) {
                    toggle(STATUS_CLOSE);
                } else {
                    toggle(STATUS_DEFAULT);
                }
                break;
        }
    }

    public void toggle(int status) {
        this.mStatus = status;
        mCurY = getScrollY();
        switch (status) {
            case STATUS_EXTEND:
                mDstY = mOffsetExtend;
                start();
                break;
            case STATUS_CLOSE:
                mDstY = mOffsetClose;
                start();
                break;
            case STATUS_DEFAULT:
                mDstY = mOffsetDefault;
                start();
                break;
        }
    }

    public void setOnChangeListener(OnChangeListener l) {
        this.mListener = l;
    }

    public interface OnChangeListener {
        void onChange(int status);

        void onScroll(float offset);
    }
}
