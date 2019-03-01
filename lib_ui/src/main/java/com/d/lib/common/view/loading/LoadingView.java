package com.d.lib.common.view.loading;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.d.lib.common.R;

import java.lang.ref.WeakReference;

/**
 * Loading
 * Created by Administrator on 2016/8/27.
 */
public class LoadingView extends View {
    public final static int TYPE_DAISY = 0;
    public final static int TYPE_DOT = 1;

    private float mWidth;
    private float mHeight;

    private Context mContext;
    private Paint mPaint;
    private RectF mTempRect;
    private int mType = TYPE_DAISY;
    private long mDuration;
    private int mCount = 12;
    private int mColor;
    private int mMinAlpha;
    private float mWidthRate;
    private float mHeightRate;
    private float mRectWidth;
    private int mJ;
    private Handler mHandler;
    private Task mRunnable;
    private boolean mIsFirst;

    private static class Task implements Runnable {

        WeakReference<LoadingView> weakRef;

        Task(LoadingView view) {
            this.weakRef = new WeakReference<>(view);
        }

        @Override
        public void run() {
            if (isFinished()) {
                return;
            }
            LoadingView theView = weakRef.get();
            theView.invalidate();
            theView.mHandler.postDelayed(theView.mRunnable, theView.mDuration / theView.mCount);
        }

        private boolean isFinished() {
            return weakRef == null || weakRef.get() == null
                    || weakRef.get().mContext == null
                    || weakRef.get().mContext instanceof Activity && ((Activity) weakRef.get().mContext).isFinishing();
        }
    }

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.mIsFirst = true;
        this.mColor = ContextCompat.getColor(context, R.color.lib_pub_color_main);
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mPaint.setColor(mColor);
        this.mDuration = 1000;
        this.mMinAlpha = 50;
        this.mWidthRate = 1f / 3;
        this.mHeightRate = 1f / 2;
        this.mHandler = new Handler();
        this.mRunnable = new Task(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mWidth == 0 || mHeight == 0 || mTempRect == null) {
            return;
        }
        canvas.translate(mWidth / 2, mHeight / 2);
        mJ++;
        mJ %= mCount;
        int alpha;
        for (int i = 0; i < mCount; i++) {
            canvas.rotate(360f / mCount);
            alpha = (i - mJ + mCount) % mCount;
            alpha = (int) (((alpha) * (255f - mMinAlpha) / mCount + mMinAlpha));
            mPaint.setAlpha(alpha);
            switch (mType) {
                case TYPE_DAISY:
                    /** Daisy rotation **/
                    canvas.drawRoundRect(mTempRect, mRectWidth / 2, mRectWidth / 2, mPaint);
                    break;
                case TYPE_DOT:
                    /** Dot rotation **/
                    canvas.drawCircle((mTempRect.left + mTempRect.right) / 2, (mTempRect.top + mTempRect.bottom) / 2, mRectWidth * 2 / 3, mPaint);
                    break;
            }
        }
        if (mIsFirst) {
            mIsFirst = false;
            mHandler.postDelayed(mRunnable, mDuration / mCount);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        refreshField();
    }

    private void refreshField() {
        final float h = mWidth > mHeight ? mHeight : mWidth;
        final float rectHeight = h * mHeightRate / 2;
        final float radius = h * (1 - mHeightRate / 2) / 2;
        mRectWidth = rectHeight * mWidthRate;
        if (mTempRect == null) {
            mTempRect = new RectF(-mRectWidth / 2, -(radius + rectHeight / 2), mRectWidth / 2, -(radius - rectHeight / 2));
        } else {
            mTempRect.set(-mRectWidth / 2, -(radius + rectHeight / 2), mRectWidth / 2, -(radius - rectHeight / 2));
        }
    }

    @Override
    public void setVisibility(int visibility) {
        switch (visibility) {
            case VISIBLE:
                restart();
                break;
            case GONE:
            case INVISIBLE:
                stop();
                break;
        }
        super.setVisibility(visibility);
    }

    @Override
    protected void onAttachedToWindow() {
        if (!mIsFirst) {
            restart();
        }
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        stop();
        super.onDetachedFromWindow();
    }

    public void setType(int type) {
        this.mType = type;
        this.invalidate();
    }

    /**
     * Restart
     */
    public void restart() {
        stop();
        mHandler.post(mRunnable);
    }

    /**
     * Stop
     */
    public void stop() {
        mIsFirst = false;
        mHandler.removeCallbacks(mRunnable);
    }
}
