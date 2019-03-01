package com.d.lib.common.view.toggle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;

import com.d.lib.common.R;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * ToggleButton
 * Created by D on 2017/6/6.
 */
public class ToggleButton extends View implements ToggleView {
    private int mWidth;
    private int mHeight;

    private Rect mRect;
    private RectF mRectF;
    private Paint mPaintThumb;
    private Paint mPaintTrack;
    private Paint mPaintPadding;
    private Paint mPaintShader;

    private int mTouchSlop;
    private boolean mIsOpen; // 当前的位置
    private boolean mIsClickValid; // 点击是否有效

    private float mPadding; // Variables 背景边框线宽度
    private int mDuration; // Variables 动画时长

    private int mColorThumb, mColorTrackOpen, mColorTrackOff, mColorPadding;
    private float mDX, mDY;
    private ValueAnimator mAnimation;
    private float mFactor = 1; // 进度因子: 0-1
    private OnToggleListener mListener;

    public ToggleButton(Context context) {
        this(context, null);
    }

    public ToggleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypedArray(context, attrs);
        init(context);
    }

    private void initTypedArray(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_pub_ToggleButton);
        mColorThumb = typedArray.getColor(R.styleable.lib_pub_ToggleButton_lib_pub_tbtn_colorThumb, ContextCompat.getColor(context, R.color.lib_pub_color_white));
        mColorTrackOpen = typedArray.getColor(R.styleable.lib_pub_ToggleButton_lib_pub_tbtn_colorTrackOpen, ContextCompat.getColor(context, R.color.lib_pub_color_main));
        mColorTrackOff = typedArray.getColor(R.styleable.lib_pub_ToggleButton_lib_pub_tbtn_colorTrackOff, ContextCompat.getColor(context, R.color.lib_pub_color_white));
        mColorPadding = typedArray.getColor(R.styleable.lib_pub_ToggleButton_lib_pub_tbtn_colorPadding, ContextCompat.getColor(context, R.color.lib_pub_color_hint));
        mPadding = (int) typedArray.getDimension(R.styleable.lib_pub_ToggleButton_lib_pub_tbtn_padding, 1);
        mDuration = typedArray.getInteger(R.styleable.lib_pub_ToggleButton_lib_pub_tbtn_duration, 0);
        typedArray.recycle();
    }

    private void init(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_SOFTWARE, null); // Disable hardware acceleration
        }
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        mRect = new Rect();
        mRectF = new RectF();

        mPaintThumb = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintThumb.setColor(mColorThumb);

        mPaintTrack = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintTrack.setColor(mColorTrackOff);

        mPaintPadding = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintPadding.setColor(mColorPadding);

        mPaintShader = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintShader.setColor(mColorThumb);
        mPaintShader.setShadowLayer(mPadding * 2, 0, 0, mColorPadding);

        mAnimation = ValueAnimator.ofFloat(0f, 1f);
        mAnimation.setDuration(mDuration);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mFactor = (float) animation.getAnimatedValue(); // 更新进度因子
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float rectRadius = mHeight / 2f;
        mRect.set(0, 0, mWidth, mHeight);
        mRectF.set(mRect);
        canvas.drawRoundRect(mRectF, rectRadius, rectRadius, mIsOpen ? mPaintTrack : mPaintPadding);

        mRect.set((int) mPadding, (int) mPadding, (int) (mWidth - mPadding), (int) (mHeight - mPadding));
        mRectF.set(mRect);
        canvas.drawRoundRect(mRectF, rectRadius, rectRadius, mPaintTrack);

        float c0 = mHeight / 2;
        float c1 = mWidth - mHeight / 2;
        float start = !mIsOpen ? c1 : c0;
        float end = mIsOpen ? c1 : c0;
        float offsetX = start + (end - start) * mFactor; // 通过属性动画因子，计算此瞬间圆心的横坐标

        canvas.drawCircle(offsetX, mHeight / 2, mHeight / 2 - mPadding, mPaintShader);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!(mFactor == 0 || mFactor == 1)) {
            return false;
        }
        float eX = event.getX();
        float eY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDX = eX;
                mDY = eY;
                mIsClickValid = true;
            case MotionEvent.ACTION_MOVE:
                if (mIsClickValid && (Math.abs(eX - mDX) > mTouchSlop || Math.abs(eY - mDY) > mTouchSlop)) {
                    mIsClickValid = false;
                }
                return mIsClickValid;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (!mIsClickValid || eX < 0 || eX > mWidth || eY < 0 || eY > mHeight) {
                    return false;
                }
                toggle();
                return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 开始动画
     */
    public void start() {
        stop();
        if (mAnimation != null) {
            mAnimation.start();
        }
    }

    /**
     * 停止动画
     */
    public void stop() {
        if (mAnimation != null) {
            mAnimation.cancel();
        }
    }

    @Override
    public void toggle() {
        mIsOpen = !mIsOpen;
        if (mIsOpen) {
            mPaintTrack.setColor(mColorTrackOpen);
        } else {
            mPaintTrack.setColor(mColorTrackOff);
        }
        if (mDuration <= 0) {
            mFactor = 1f;
            invalidate();
        } else {
            start();
        }
        if (mListener != null) {
            mListener.onToggle(mIsOpen);
        }
    }

    @Override
    public void setOpen(boolean open) {
        if (mFactor != 0 && mFactor != 1) {
            return;
        }
        stop();
        mIsOpen = open;
        mFactor = 1f;
        if (mIsOpen) {
            mPaintTrack.setColor(mColorTrackOpen);
        } else {
            mPaintTrack.setColor(mColorTrackOff);
        }
        invalidate();
    }

    @Override
    public boolean isOpen() {
        return mIsOpen;
    }

    @Override
    public void setOnToggleListener(OnToggleListener l) {
        this.mListener = l;
    }
}