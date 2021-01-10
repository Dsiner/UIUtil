package com.d.lib.ui.view.tick;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

import com.d.lib.common.util.DimenUtils;
import com.d.lib.ui.view.R;

public class STickView extends View {
    private final long DURATION = 2500; // Anim duration

    private int mWidth;
    private int mHeight;

    private float mFactor;
    private float mScaleAX = 0.2659f;
    private float mScaleAY = 0.4588f;
    private float mScaleBX = 0.4541f;
    private float mScaleBY = 0.6306f;
    private float mScaleCX = 0.7553f;
    private float mScaleCY = 0.3388f;

    private int mColor;
    private int mColorCircle;
    private float mStrokeWidth;
    private Path mPath;
    private Path mPathTick;
    private Paint mPaintTick;
    private Paint mPaintCircle;
    private PathMeasure mTickPathMeasure;
    private ValueAnimator mAnimation;

    public STickView(Context context) {
        this(context, null);
    }

    public STickView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public STickView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_ui_view_CTickView);
        mColor = typedArray.getColor(R.styleable.lib_ui_view_CTickView_lib_ui_view_ctv_color, Color.parseColor("#ffffff"));
        mColorCircle = typedArray.getColor(R.styleable.lib_ui_view_CTickView_lib_ui_view_ctv_colorCircle, Color.parseColor("#47b018"));
        mStrokeWidth = typedArray.getDimension(R.styleable.lib_ui_view_CTickView_lib_ui_view_ctv_strokeWidth, DimenUtils.dp2px(context, 3.5f));
        typedArray.recycle();
        init();
    }

    public void init() {
        mPath = new Path();
        mPathTick = new Path();

        mTickPathMeasure = new PathMeasure();

        mPaintCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCircle.setColor(mColorCircle);

        mPaintTick = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintTick.setColor(mColor);
        mPaintTick.setStyle(Paint.Style.STROKE);
        mPaintTick.setStrokeWidth(mStrokeWidth);

        mAnimation = ValueAnimator.ofFloat(0f, 1f);
        mAnimation.setDuration(DURATION);
        mAnimation.setInterpolator(new SpringScaleInterpolator(0.4f));
        mAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mPaintCircle != null && mPaintTick != null) {
                    float alpha = 1f * animation.getDuration() / DURATION;
                    alpha = Math.min(alpha, 1f);
                    alpha = Math.max(alpha, 0f);
                    mPaintCircle.setAlpha((int) (255 * alpha));
                    mPaintTick.setAlpha((int) (255 * alpha));
                }
                mFactor = (float) animation.getAnimatedValue();
                mFactor = mFactor / 1.27f;
                mFactor = Math.min(mFactor, 1f);
                mFactor = Math.max(mFactor, 0f);
                postInvalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float width = this.mWidth * 0.85f * mFactor;
        float height = this.mHeight * 0.85f * mFactor;
        float startX = (this.mWidth - width) / 2f;
        float startY = (this.mHeight - height) / 2f;
        mPath.reset();
        mPathTick.reset();
        mPathTick.moveTo(startX + width * mScaleAX, startY + height * mScaleAY);
        mPathTick.lineTo(startX + width * mScaleBX, startY + height * mScaleBY);
        mPathTick.lineTo(startX + width * mScaleCX, startY + height * mScaleCY);
        mTickPathMeasure.setPath(mPathTick, false);
        /*
         * On KITKAT and earlier releases, the resulting path may not display on a hardware-accelerated Canvas.
         * A simple workaround is to add a single operation to this path, such as dst.rLineTo(0, 0).
         */
        mTickPathMeasure.getSegment(0, mTickPathMeasure.getLength(), mPath, true);
        width = this.mWidth * mFactor;
        height = this.mHeight * mFactor;
        startX = (this.mWidth - width) / 2f;
        startY = (this.mHeight - height) / 2f;
        canvas.drawCircle(startX + width / 2f, startY + width / 2f, width / 2f, mPaintCircle);
        mPaintTick.setStrokeWidth(mStrokeWidth * mFactor);
        canvas.drawPath(mPath, mPaintTick);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    /**
     * Start animation
     */
    public void start() {
        stop();
        mPath = new Path();
        if (mAnimation != null) {
            mAnimation.start();
        }
    }

    /**
     * Stop animation
     */
    public void stop() {
        if (mAnimation != null) {
            mAnimation.end();
        }
    }

    class SpringScaleInterpolator implements Interpolator {
        private float factor;

        SpringScaleInterpolator(float factor) {
            this.factor = factor;
        }

        @Override
        public float getInterpolation(float input) {
            return (float) (Math.pow(2, -10 * input) * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
        }
    }
}
