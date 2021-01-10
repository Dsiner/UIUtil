package com.d.lib.ui.view.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.d.lib.common.util.DimenUtils;
import com.d.lib.ui.view.R;

/**
 * SettingProgressView
 * Created by D on 2016/7/7.
 */
public class SettingProgressView extends View {
    private int mWidth;
    private int mHeight;

    private Paint mPaint;
    private Paint mPaintShader; // 用于绘制当前选中的渐变阴影
    private int mItemCount;
    private int mColorSelected;
    private int mColorUnselected;
    private float mRadiusSmall;
    private float mRadiusBig;
    private float mRadiusSpace;
    private float mLineHeight; // 中间连接线高度
    private float mShadeRadius; // 渐变阴影的宽度
    private float mItemWidth;
    private float mFirstItemRange;
    private float mBigRadiusWidth;
    private int mCurColor; // 当前选中的颜色
    private int mCurPosition;
    private int mClickPosition;
    private int mTouchSlop;
    private boolean mCanDrag;
    private boolean mIsClickValid; // 点击是否有效
    private float mDivX, mDivY; // 当前Move位置
    private float mLastX;
    private float mLastY;
    private OnProgressChangeListener mListener;

    public SettingProgressView(Context context) {
        super(context, null);
    }

    public SettingProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_ui_view_SettingProgressView);
        mItemCount = typedArray.getInteger(R.styleable.lib_ui_view_SettingProgressView_lib_ui_view_spv_count, 5);
        mColorSelected = typedArray.getColor(R.styleable.lib_ui_view_SettingProgressView_lib_ui_view_spv_colorSelect, Color.parseColor("#69B068"));
        mColorUnselected = typedArray.getColor(R.styleable.lib_ui_view_SettingProgressView_lib_ui_view_spv_colorUnselect, Color.GRAY);
        mRadiusSmall = typedArray.getDimension(R.styleable.lib_ui_view_SettingProgressView_lib_ui_view_spv_radiusSmall, DimenUtils.dp2px(context, 5));
        mRadiusBig = typedArray.getDimension(R.styleable.lib_ui_view_SettingProgressView_lib_ui_view_spv_radiusBig, DimenUtils.dp2px(context, 10));
        mRadiusSpace = typedArray.getDimension(R.styleable.lib_ui_view_SettingProgressView_lib_ui_view_spv_radiusSpace, DimenUtils.dp2px(context, 2));
        typedArray.recycle();
        init(context);
    }

    private void init(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // 禁用硬件加速
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mLineHeight = DimenUtils.dp2px(context, 2);
        mShadeRadius = DimenUtils.dp2px(context, 5);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintShader = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCurColor = Color.WHITE;
        mPaintShader.setColor(mCurColor);
        mPaintShader.setShadowLayer(mShadeRadius, 0, 0, mColorUnselected);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mColorUnselected);
        mItemWidth = (mWidth - (mRadiusSpace + mRadiusBig) * 2) / (mItemCount - 1);
        float startx = mRadiusSpace + mRadiusBig;
        float starty = mHeight / 2 - mLineHeight / 2;
        float endx;
        float endy = starty + mLineHeight;
        if (mCanDrag) {
            endx = mDivX;
        } else {
            endx = mRadiusSpace + mRadiusBig + mItemWidth * mCurPosition;
        }
        canvas.drawRect(startx, starty, endx, endy, mPaint);

        mPaint.setColor(mColorSelected);
        canvas.drawRect(endx, starty, mWidth - mRadiusSpace - mRadiusBig, endy, mPaint);

        starty += mLineHeight / 2;
        for (int i = 0; i < mItemCount; i++) {
            startx = mRadiusBig + mRadiusSpace + mItemWidth * i;
            if (i == mCurPosition) {
                if (mCanDrag) {
                    if (mDivX > startx) {
                        drawCircle(canvas, mColorUnselected, startx, starty);
                    } else if (mDivX < startx) {
                        drawCircle(canvas, mColorSelected, startx, starty);
                    }
                    mPaint.setColor(mColorSelected);
                    canvas.drawCircle(mDivX, starty, mRadiusBig + mRadiusSpace - mShadeRadius, mPaint);
                } else {
                    canvas.drawCircle(startx, starty, mRadiusBig + mRadiusSpace - mShadeRadius, mPaintShader);
                }
            } else if (i < mCurPosition) {
                drawCircle(canvas, mColorUnselected, startx, starty);
            } else {
                drawCircle(canvas, mColorSelected, startx, starty);
            }
        }
    }

    /**
     * 绘制圆
     */
    private void drawCircle(Canvas canvas, int color, float startx, float starty) {
        mPaint.setColor(color);
        canvas.drawCircle(startx, starty, mRadiusSmall + mRadiusSpace, mPaint);
        mPaint.setColor(color);
        canvas.drawCircle(startx, starty, mRadiusSmall, mPaint);
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
        mFirstItemRange = mRadiusBig + mRadiusSpace + mItemWidth / 2;
        mBigRadiusWidth = (mRadiusBig + mRadiusSpace) * 2;
        int position = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = mDivX = event.getX();
                mLastY = mDivY = event.getY();
                mClickPosition = -1;
                if (event.getX() < mBigRadiusWidth) {
                    mClickPosition = 0;
                } else if (event.getX() >= mWidth - mBigRadiusWidth) {
                    mClickPosition = mItemCount - 1;
                } else if (event.getX() >= mFirstItemRange) {
                    mClickPosition = (int) ((event.getX() - mFirstItemRange) / mItemWidth) + 1;
                    if (!(Math.abs(mBigRadiusWidth / 2 + mItemWidth * mClickPosition - event.getX()) <= mBigRadiusWidth / 2)) {
                        mClickPosition = -1; // 未点中大圆范围内
                    }
                }
                if (mClickPosition == mCurPosition) {
                    mCanDrag = true;
                    mIsClickValid = false; // 点击无效，交给ACTION_MOVE处理
                    if (mDivX < mBigRadiusWidth / 2) {
                        mDivX = mBigRadiusWidth / 2;
                    }
                    if (mDivX > mWidth - mBigRadiusWidth / 2) {
                        mDivX = mWidth - mBigRadiusWidth / 2;
                    }
                    invalidate();
                } else {
                    mCanDrag = false;
                    mIsClickValid = true; // 点击有效，交给ACTION_UP处理
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (!mCanDrag) {
                    return true;
                }
                // Disable parent view interception events
                getParent().requestDisallowInterceptTouchEvent(true);
                mDivX = event.getX();
                mDivY = event.getY();
                if (mDivX < mBigRadiusWidth / 2) {
                    mDivX = mBigRadiusWidth / 2;
                }
                if (mDivX > mWidth - mBigRadiusWidth / 2) {
                    mDivX = mWidth - mBigRadiusWidth / 2;
                }
                mIsClickValid = false; // 只要移动，点击无效，交给ACTION_UP处理
                if (event.getX() >= mWidth - mFirstItemRange) {
                    position = mItemCount - 1;
                } else if (event.getX() >= mFirstItemRange) {
                    position = (int) ((event.getX() - mFirstItemRange) / mItemWidth) + 1;
                }
                if (mCurPosition != position) {
                    mCurPosition = position;
                    if (mListener != null) {
                        mListener.onProgressChange(mCurPosition);
                    }
                }
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                mCanDrag = false;
                if (!mIsClickValid) {
                    invalidate();
                    if (mListener != null) {
                        mListener.onClick(mCurPosition);
                    }
                    return true;
                } else {
                    if (Math.abs(event.getX() - mLastX) >= mTouchSlop || Math.abs(event.getY() - mLastY) >= mTouchSlop) {
                        return true;
                    }
                }
                if (event.getX() >= mWidth - mFirstItemRange) {
                    position = mItemCount - 1;
                } else if (event.getX() >= mFirstItemRange) {
                    position = (int) ((event.getX() - mFirstItemRange) / mItemWidth) + 1;
                }
                if (mCurPosition != position) {
                    mCurPosition = position;
                }
                invalidate();
                if (mListener != null) {
                    mListener.onClick(mCurPosition);
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 设置当前选中
     */
    public SettingProgressView setCurPosition(int curPosition) {
        this.mCurPosition = curPosition;
        return this;
    }

    public void setOnProgressChangeListener(OnProgressChangeListener listener) {
        this.mListener = listener;
    }

    public interface OnProgressChangeListener {

        /**
         * @param position From 0 to item count - 1
         */
        void onProgressChange(int position);

        /**
         * @param position from 0 to item count - 1
         */
        void onClick(int position);
    }
}
