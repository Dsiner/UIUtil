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
import android.view.animation.LinearInterpolator;

import com.d.lib.ui.view.R;

/**
 * Tick
 * Created by D on 2017/2/28.
 */
public class CTickView extends View {
    private int mWidth;
    private int mHeight;

    private float mFactor; // Factor: 0-1
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

    public CTickView(Context context) {
        this(context, null);
    }

    public CTickView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CTickView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_ui_view_CTickView);
        mColor = typedArray.getColor(R.styleable.lib_ui_view_CTickView_lib_ui_view_ctv_color, Color.parseColor("#ffffff"));
        mColorCircle = typedArray.getColor(R.styleable.lib_ui_view_CTickView_lib_ui_view_ctv_colorCircle, Color.parseColor("#47b018"));
        mStrokeWidth = typedArray.getDimension(R.styleable.lib_ui_view_CTickView_lib_ui_view_ctv_strokeWidth, 5);
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
        mAnimation.setDuration(250);
        mAnimation.setInterpolator(new LinearInterpolator());
        mAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mFactor = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPathTick.moveTo(mWidth * mScaleAX, mHeight * mScaleAY);
        mPathTick.lineTo(mWidth * mScaleBX, mHeight * mScaleBY);
        mPathTick.lineTo(mWidth * mScaleCX, mHeight * mScaleCY);
        mTickPathMeasure.setPath(mPathTick, false);
        /*
         * On KITKAT and earlier releases, the resulting path may not display on a hardware-accelerated Canvas.
         * A simple workaround is to add a single operation to this path, such as dst.rLineTo(0, 0).
         */
        mTickPathMeasure.getSegment(0, mFactor * mTickPathMeasure.getLength(), mPath, true);
        mPath.rLineTo(0, 0);
        canvas.drawCircle(mWidth / 2f, mWidth / 2f, mWidth / 2f, mPaintCircle);
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
}
