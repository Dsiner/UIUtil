package com.d.lib.ui.view.replybg;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.d.lib.common.util.DimenUtils;
import com.d.lib.ui.view.R;

/**
 * Message Background
 * Created by D on 2017/3/3.
 */
public class ReplyBgView extends View {
    private final int LEFT = 0;
    private final int RIGHT = 1;
    private final int TOP = 2;
    private final int BOTTOM = 3;

    private int mWidth;
    private int mHeight;

    private Rect mRect;
    private RectF mRectF;
    private Paint mPaint;
    private Path mPathTrg; // Triangular path
    private int mGravity;
    private int mColorBg;
    private float mRectRadius;
    private float mOffset; // Triangle left offset
    private float mTrgHalfWidth; // Triangle bottom length/2
    private float mTrgHeight; // Triangle height

    public ReplyBgView(Context context) {
        this(context, null);
    }

    public ReplyBgView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReplyBgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.lib_ui_view_ReplyBgView);
        mGravity = typedArray.getInteger(R.styleable.lib_ui_view_ReplyBgView_lib_ui_view_replybv_gravity, TOP);
        mColorBg = typedArray.getColor(R.styleable.lib_ui_view_ReplyBgView_lib_ui_view_replybv_color, ContextCompat.getColor(context, R.color.lib_pub_color_main));
        mRectRadius = typedArray.getDimension(R.styleable.lib_ui_view_ReplyBgView_lib_ui_view_replybv_radius, DimenUtils.dp2px(context, 3));
        mOffset = typedArray.getDimension(R.styleable.lib_ui_view_ReplyBgView_lib_ui_view_replybv_offset, DimenUtils.dp2px(context, 6.5f));
        mTrgHalfWidth = typedArray.getDimension(R.styleable.lib_ui_view_ReplyBgView_lib_ui_view_replybv_trgWidth, DimenUtils.dp2px(context, 6)) / 2;
        mTrgHeight = typedArray.getDimension(R.styleable.lib_ui_view_ReplyBgView_lib_ui_view_replybv_trgHeight, DimenUtils.dp2px(context, 5));
        typedArray.recycle();
    }

    private void init(@SuppressWarnings("unused") Context context) {
        mPathTrg = new Path();
        mRect = new Rect();
        mRectF = new RectF();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mColorBg);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mGravity) {
            case LEFT:
                mPathTrg.moveTo(mTrgHeight, mOffset > 0 ? mOffset : mHeight + mOffset - mTrgHalfWidth * 2);
                mPathTrg.lineTo(0, mOffset > 0 ? mOffset + mTrgHalfWidth : mHeight + mOffset - mTrgHalfWidth);
                mPathTrg.lineTo(mTrgHeight, mOffset > 0 ? mOffset + mTrgHalfWidth * 2 : mHeight + mOffset);
                mPathTrg.close();
                canvas.drawPath(mPathTrg, mPaint);

                mRect.set((int) mTrgHeight, 0, mWidth, mHeight);
                mRectF.set(mRect);
                canvas.drawRoundRect(mRectF, mRectRadius, mRectRadius, mPaint);
                break;

            case RIGHT:
                mPathTrg.moveTo(mWidth - mTrgHeight, mOffset > 0 ? mOffset : mHeight + mOffset - mTrgHalfWidth * 2);
                mPathTrg.lineTo(mWidth, mOffset > 0 ? mOffset + mTrgHalfWidth : mHeight + mOffset - mTrgHalfWidth);
                mPathTrg.lineTo(mWidth - mTrgHeight, mOffset > 0 ? mOffset + mTrgHalfWidth * 2 : mHeight + mOffset);
                mPathTrg.close();
                canvas.drawPath(mPathTrg, mPaint);

                mRect.set(0, 0, (int) (mWidth - mTrgHeight), mHeight);
                mRectF.set(mRect);
                canvas.drawRoundRect(mRectF, mRectRadius, mRectRadius, mPaint);
                break;

            case TOP:
                mPathTrg.moveTo(mOffset > 0 ? mOffset : mWidth + mOffset - mTrgHalfWidth * 2, mTrgHeight);
                mPathTrg.lineTo(mOffset > 0 ? mOffset + mTrgHalfWidth : mWidth + mOffset - mTrgHalfWidth, 0);
                mPathTrg.lineTo(mOffset > 0 ? mOffset + mTrgHalfWidth * 2 : mWidth + mOffset, mTrgHeight);
                mPathTrg.close();
                canvas.drawPath(mPathTrg, mPaint);

                mRect.set(0, (int) mTrgHeight, mWidth, mHeight);
                mRectF.set(mRect);
                canvas.drawRoundRect(mRectF, mRectRadius, mRectRadius, mPaint);
                break;

            case BOTTOM:
                mPathTrg.moveTo(mOffset > 0 ? mOffset : mWidth + mOffset - mTrgHalfWidth * 2, mHeight - mTrgHeight);
                mPathTrg.lineTo(mOffset > 0 ? mOffset + mTrgHalfWidth : mWidth + mOffset - mTrgHalfWidth, mHeight);
                mPathTrg.lineTo(mOffset > 0 ? mOffset + mTrgHalfWidth * 2 : mWidth + mOffset, mHeight - mTrgHeight);
                mPathTrg.close();
                canvas.drawPath(mPathTrg, mPaint);

                mRect.set(0, 0, mWidth, (int) (mHeight - mTrgHeight));
                mRectF.set(mRect);
                canvas.drawRoundRect(mRectF, mRectRadius, mRectRadius, mPaint);
                break;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }
}